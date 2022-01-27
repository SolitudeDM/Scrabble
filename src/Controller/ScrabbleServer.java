package Controller;

import Model.Game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ScrabbleServer {
    private ServerSocket ssock;
    private List<ScrabbleClientHandler> clients;

    private Game game;

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
        if(message.equals("Hello")){
            return "Hello";
        }
        else{
            return "Wrong handshake message";
        }
    }


//    public void setUpGame(){
//        this.game = new Game();
//    }

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

}
