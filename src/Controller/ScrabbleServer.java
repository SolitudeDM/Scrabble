package Controller;

import Controller.Protocols.ServerProtocol;
import Model.Board;
import Model.Game;
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

    protected Game game;


    public ScrabbleServer(){

        this.clients = new ArrayList<>();
        this.players = new ArrayList<Player>();
        //not sure what to add next
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
            try{
                setUp();
                while(clients.size() < 2){
//                    System.out.println("Server is waiting for the connection...");
                    String name = "Player";  //change this in the future
                    Socket sock = ssock.accept();
                    ScrabbleClientHandler handler = new ScrabbleClientHandler(sock, this, name);
                    new Thread(handler).start();
                    clients.add(handler);
                }
                openNewSock = false;
            }catch(IOException e){
                System.out.println("Poshel naxyi");
            }
        }
        sendMessageToAll(clients.size() + " clients active, waiting for player's connections...");
        setUpGame();
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

    public Player currentPlayer(){
        Player currentPlayer = players.get(0);
        if(currentPlayer.getMove().isMoveMade()){
            currentPlayer = players.get(1);
        }
        return currentPlayer;
    }

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
            players.add(clientPlayer);
            System.out.println(players.size());
            return "Player " + ANSI.PURPLE_BOLD_BRIGHT + playerName + ANSI.RESET +" connected to the server";
        }
    }

    @Override
    public String handlePlace(String coordinates, boolean orientation, String word) {
        return null;
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
