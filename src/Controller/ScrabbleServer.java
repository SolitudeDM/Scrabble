package Controller;

import Controller.Protocols.ServerProtocol;
import Exceptions.EmptyCommandException;
import Exceptions.InvalidCommandException;
import Exceptions.SquareNotEmptyException;
import Exceptions.WrongOrientationException;
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


    public ScrabbleServer(){

        this.clients = new ArrayList<>();
        this.players = new ArrayList<Player>();
        //not sure what to add next
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

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
                while(clients.size() < 2) {

//                  System.out.println("Server is waiting for the connection...");
                    String name = "Player";  //change this in the future
                    Socket sock = ssock.accept();
                    ScrabbleClientHandler handler = new ScrabbleClientHandler(sock, this, name);
                    new Thread(handler).start();


                    if (!handler.getName().equals("Player")) {
                        clients.add(handler);
                        System.out.println(handler.getName());
                    }


                }

                openNewSock = false;
            } catch(IOException e) {
                System.out.println("Poshel naxyi");
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
            setCurrentPlayer(clientPlayer);

            return "Player " + ANSI.PURPLE_BOLD_BRIGHT + playerName + ANSI.RESET +" connected to the server";
        }
    }

    @Override
    public String handlePlace(String coordinates, boolean orientation, String word) {
        if(currentPlayer.equals(players.get(0))){
            currentPlayer.setMove(new Move(game, currentPlayer));
            try {
                players.get(0).getMove().place(coordinates, orientation, word, game.getBoard());
            } catch (SquareNotEmptyException e) {
                System.out.println(e.getMessage());
            }
            setCurrentPlayer(players.get(1));
        } else{
            currentPlayer.setMove(new Move(game, currentPlayer));
            try {
                players.get(1).getMove().place(coordinates, orientation, word, game.getBoard());
            } catch (SquareNotEmptyException e) {
                System.out.println(e.getMessage());
            }
            setCurrentPlayer(players.get(0));
        }
        return "Updated Board" + "\n" + game.getBoard().toString();
    }

//    public String rotation(){
//        for (int i = 0; i < players.size(); i++) {
//            for (int j = 0; j < clients.size(); j ++) {
//                System.out.println(clients.get(j).getName());
//                if (clients.get(j).getName().equals(players.get(i).getName())) {
//                    sendMessageToAll("Its " + players.get(i).getName() + "'s turn");
//                    clients.get(j).sendMessage(game.getBoard().toString());
//                    clients.get(j).sendMessage(game.tilesToString(players.get(i)));
//                    clients.get(j).sendMessage("Please make turn");
//
//                    return "Its " + players.get(i).getName() + "'s turn";
//
//                }
//            }
//        }
//        return null;
//    }

    @Override
    public String handleInitiateGame() {
        setUpGame();
        return "Starting game... players: " + players.get(0).getName() + " & " + players.get(1).getName() + "\n" + game.getBoard().toString() + "\n" + game.tilesToString(players.get(0));
    }

    @Override
    public String handleSkip() {
        return null;
    }

    @Override
    public String handleSwap(String tiles) {
        return null;
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
