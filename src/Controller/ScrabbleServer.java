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

    public Game getGame() {
        return game;
    }

    public List<ScrabbleClientHandler> getClients() {
        return clients;
    }
//    public Player getCurrentPlayer() {
//        return currentPlayer;
//    }
//
//    public synchronized void setCurrentPlayer(Player currentPlayer) {
//        this.currentPlayer = currentPlayer;
//    }

    public void setUp(){
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
                System.out.println("Connection issues");
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


    @Override
    public void handleConnection(String playerName) {
        Player clientPlayer = new HumanPlayer_v3(playerName, game);
        players.add(clientPlayer);
        currentPlayerIndex = players.size() - 1;
        currentPlayer = players.get(currentPlayerIndex);

        sendMessageToAll(ProtocolMessages.CONFIRM_CONNECT + ProtocolMessages.DELIMITER + "Player " + ANSI.PURPLE_BOLD_BRIGHT + playerName + ANSI.RESET +" connected to the server. Type 'fs' to start game! \n");

    }

    @Override
    public void handlePlace(String coordinates, boolean orientation, String word, ScrabbleClientHandler caller) {
        if(currentPlayer.getName().equals(caller.getName())){
            currentPlayer.setMove(new Move(game, currentPlayer));
            try {
                currentPlayer.getMove().place(coordinates, orientation, word, game.getBoard());
            } catch (SquareNotEmptyException e) {
                for(ScrabbleClientHandler h : clients){
                    h.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + e.getMessage() + "\n");
                }
            }

            game.handOut();
            for (ScrabbleClientHandler h : clients) {
                if (currentPlayer.getName().equals(h.getName())) {
                    if(currentPlayer.getMove().isRequestAnother()) {
                        h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + "Invalid input, try one more time! (Type 'help' for help menu) \n");
                        continue;
                    }
                    else {
                        if(currentPlayer.getMove().isMoveMade()) {
                            h.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + "Your move is invalid, you lose your turn \n");
                        }
                        else if(!currentPlayer.getMove().isRequestAnother()){
                            h.sendMessage(ProtocolMessages.UPDATE_TABLE + ProtocolMessages.DELIMITER + "Updated board: \n" + game.getBoard().toString() + "\n" + game.tilesToString(currentPlayer) + "\n" + "You made your move, its opponent's turn now! \n");
                            continue;
                        }
                    }
                }
                for (Player p : players) {
                    if(p.getName().equals(h.getName())) {
                        if(!currentPlayer.getMove().isRequestAnother()) {
                            if (currentPlayer.getMove().isMoveMade()) {
                                h.sendMessage(ProtocolMessages.UPDATE_TABLE + ProtocolMessages.DELIMITER + "Updated board: \n" + game.getBoard().toString() + "\n" + game.tilesToString(p) + "\n" + "It's your turn!" + "\n");
                            } else {
                                h.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + currentPlayer.getName() + "'s move was invalid, your turn now! \n");
                            }
                        }
                    }
                }
            }
            if(!currentPlayer.getMove().isRequestAnother()) {
                currentPlayerIndex++;
                currentPlayerIndex %= players.size();
                currentPlayer = players.get(currentPlayerIndex);
            }

        } else{
            caller.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + "It is not your turn, mate! \n");
        }
        if(game.isFinished()){
            sendMessageToAll(ProtocolMessages.FINISH_GAME + ProtocolMessages.DELIMITER + game.determineWinner() + "\n");
        }
    }


    @Override
    public void handleForceStart(ScrabbleClientHandler caller) {
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
                        if(currentPlayer.getMove().isMoveMade()) {
                            h.sendMessage(ProtocolMessages.GIVE_TILE + ProtocolMessages.DELIMITER + "Board with your new tiles: \n" + game.getBoard().toString() + "\n" + game.tilesToString(currentPlayer) + "\n Opponent's turn now! \n");
                            break;
                        }else{
                            h.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + "You don't have the tiles you want to replace! You lost your turn! \n");
                            break;
                        }
                    }else{
                        h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + "You skipped your turn \n");
                    }

                }else {
                    if (!swap) {
                        h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + "PLayer " + currentPlayer.getName() + " skipped his turn \n");
                    }
                    else{
                        h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + "Your opponent swapped tiles, your turn now! \n");
                    }
                }

            }
            currentPlayer = players.get(currentPlayerIndex);
        } else{
            caller.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + "It is not your turn, mate! \n");
        }
        if(game.isFinished()){
            sendMessageToAll(ProtocolMessages.FINISH_GAME + ProtocolMessages.DELIMITER + game.determineWinner() + "\n");
        }
    }

    @Override
    public void handleExit() {
        game.setFinishGame(true);
        sendMessageToAll(ProtocolMessages.FINISH_GAME + ProtocolMessages.DELIMITER + game.determineWinner() + "\n");

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
