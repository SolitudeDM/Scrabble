package Controller;

import Controller.Protocols.ProtocolMessages;
import Controller.Protocols.ServerProtocol;
import Exceptions.SquareNotEmptyException;
import Model.Board;
import Model.Game;
import Model.Move;
import Model.Square;
import Model.players.HumanPlayer_v3;
import Model.players.Player;
import View.utils.ANSI;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ScrabbleServer implements ServerProtocol {
    private ServerSocket ssock;
    private List<ScrabbleClientHandler> clients;
//    private Player clientPlayer;
    private ArrayList<Player> players;
//
//    private PrintWriter printWriter;

    private Game game;

    private Player currentPlayer;
    private int currentPlayerIndex;


    public ScrabbleServer(){

        this.clients = new ArrayList<>();
        this.players = new ArrayList<Player>();
        //not sure what to add next
    }

//    public void setCurrentPlayer(Player currentPlayer) {
//        this.currentPlayer = currentPlayer;
//    }

    public void setUp(){
//        setUpGame();
        ssock = null;
        while(ssock == null){
            int port = 8888;
            try{
                ssock = new ServerSocket(port, 0, InetAddress.getByName("localhost"));
            }catch(IOException e){
                System.out.println("ERROR: could not create a socket on 'localhost' and port " + port + ".");
            }
        }
    }

    public void run(){
        boolean openNewSock = true;
        while(openNewSock){
            try {
                setUp();
                int PlayerIndex = 1;
                while(true) {
//                  System.out.println("Server is waiting for the connection...");
                    String name = "Player" + PlayerIndex;  //change this in the future
                    Socket sock = ssock.accept();
                    ScrabbleClientHandler handler = new ScrabbleClientHandler(sock, this, name);
                    new Thread(handler).start();

                    if (!handler.getName().equals("Player")) {
                        clients.add(handler);
                        System.out.println(handler.getName());
                    }

                    PlayerIndex++;

                }

            } catch(IOException e) {
                System.out.println("Poshel naxyi");
                openNewSock = false;
            }
        }


        sendMessageToAll(clients.size() + " clients active, waiting for player's connections...");


        if (players.size() >= 2) {
            setUpGame();
        }

    }


    public String getHello(String message){
        System.out.println("Hello");
//        printWriter.println((message));

        return "Hello";
    }


    public void setUpGame(){
        Square[][] squares = new Square[15][15];
        Board board = new Board(squares);
        board.setBoard();
//        board.showBoard();

        this.game = new Game(players, board);

//        game.setPlayers(players);

        game.handOut();


    }

    public void removeClient(ScrabbleClientHandler client) {
        this.clients.remove(client);
    }

//    public Player currentClient(Player currentPlayer){
//
//        //return player owner (client)
//    }

//    public void start(){
//        boolean run = true;
//        while(run){
//            run();
//        }
//    }

    @Override
    public String handleConnection(String playerName) {
        if(playerName == null){
            return "No entered name";
        }
        else{
            Player clientPlayer = new HumanPlayer_v3(playerName, game);
//            clients.
            players.add(clientPlayer);
            System.out.println(players.size());
            currentPlayerIndex = players.size() - 1;
            currentPlayer = players.get(currentPlayerIndex);

            return ProtocolMessages.CONFIRM_CONNECT + ProtocolMessages.DELIMITER + "Player " + ANSI.PURPLE_BOLD_BRIGHT + playerName + ANSI.RESET +" connected to the server \n";
        }
    }

    @Override
    public void handlePlace(String coordinates, boolean orientation, String word, ScrabbleClientHandler caller) {
        if(currentPlayer.getName().equals(caller.getName())){
            currentPlayer.setMove(new Move(game, currentPlayer));
            try {
                currentPlayer.getMove().place(coordinates, orientation, word, game.getBoard());
            } catch (SquareNotEmptyException e) {
                for(ScrabbleClientHandler h : clients){
                    h.sendMessage(e.getMessage());
                }
            }
            currentPlayerIndex++;
            currentPlayerIndex %= players.size();
            currentPlayer = players.get(currentPlayerIndex);

            game.handOut();
            for(Player p : players) {
                for (ScrabbleClientHandler h : clients) {
                    if (p.getName().equals(h.getName())) {
                        h.sendMessage(ProtocolMessages.UPDATE_TABLE + ProtocolMessages.DELIMITER + "Updated board: \n" + game.getBoard().toString() + "\n" + game.tilesToString(p) + "\n");
                        break;
                    }
                }
            }
        } else{
            caller.sendMessage("It is not your turn, mate! \n");
        }
    }


    @Override
    public void handleForceStart() {
        setUpGame();
        for(Player p : players) {
            p.setGame(game);
            for (ScrabbleClientHandler h : clients) {
                if (p.getName().equals(h.getName())) {
                    h.sendMessage(ProtocolMessages.INITIATE_GAME + ProtocolMessages.DELIMITER + "Starting game... players: " + players.get(0).getName() + " & " + players.get(1).getName() + "\n" + game.getBoard().toString() + "\n" + game.tilesToString(p) + "\n");
                    break;
                }
            }
        }
    }

    @Override
    public void handleSkipAndSwap(ScrabbleClientHandler caller, String tiles) {
        boolean swap = false;

        if(tiles != null){
            swap = true;
        }
        if(currentPlayer.getName().equals(caller.getName())){
            if(swap) {
                currentPlayer.setMove(new Move(game, currentPlayer));
                currentPlayer.getMove().swap(tiles);
            }

            currentPlayerIndex++;
            currentPlayerIndex %= players.size();

            for (ScrabbleClientHandler h : clients) {
                if (currentPlayer.getName().equals(h.getName())) {
                    if(swap) {
                        h.sendMessage(ProtocolMessages.GIVE_TILE + ProtocolMessages.DELIMITER + "Board with your new tiles: \n" + game.getBoard().toString() + "\n" + game.tilesToString(currentPlayer) + "\n");
                        break;
                    }else{
                        h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + "You skipped your turn \n");
                    }
                }else{
                    h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + "PLayer " + currentPlayer.getName() + " skipped his turn \n");
                }

            }
            currentPlayer = players.get(currentPlayerIndex);
        } else{
            caller.sendMessage("It is not your turn, mate! \n");
        }
    }

    @Override
    public String handleExit() {
        return null;
    }

    public void sendMessageToAll(String msg){
        for(ScrabbleClientHandler h : clients){
            h.sendMessage(msg);
        }
    }

    public static void main(String[] args) {
        new ScrabbleServer().run();
    }
}
