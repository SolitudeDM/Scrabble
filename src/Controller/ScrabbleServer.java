package Controller;

import Controller.Protocols.ServerProtocol;
import Model.Game;
import Model.players.HumanPlayer_v3;
import Model.players.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ScrabbleServer implements ServerProtocol {
    private ServerSocket ssock;
    private List<ScrabbleClientHandler> clients;
    private Player clientPlayer;
    private ArrayList<Player> players;
//
//    private PrintWriter printWriter;

    protected Game game;


    public ScrabbleServer(){

        this.clients = new ArrayList<>();
        //not sure what to add next
    }

    public void setUp(){
        //setUpGame();
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
                while(true){
//                    System.out.println("Server is waiting for the connection...");
                    String name = "Player";  //change this in the future
                    Socket sock = ssock.accept();
                    ScrabbleClientHandler handler = new ScrabbleClientHandler(sock, this, name);
                    new Thread(handler).start();
                    clients.add(handler);
                }
            }catch(IOException e){
                System.out.println("Poshel naxyi");
            }
        }
    }

    public String getHello(String message){
            System.out.println("Hello");
//        printWriter.println((message));

        return "Hello";
    }


    public void setUpGame(){
        this.game = new Game();
    }

    public void removeClient(ScrabbleClientHandler client) {
        this.clients.remove(client);
    }

    public void start(){
        boolean run = true;
        while(run){
            run();
        }
    }

    public static void main(String[] args) {
        new ScrabbleServer().start();
    }

    @Override
    public String handleConnection(String playerName) {
        if(playerName == null){
            return "No entered name";
        }
        else{
            clientPlayer = new HumanPlayer_v3()
            return "Player" + playerName + " connected to the server";
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
}
