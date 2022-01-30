//package Controller;
//
//import Controller.Protocols.ClientProtocol;
//import Controller.Protocols.ServerProtocol;
//import Model.players.HumanPlayer_v3;
//import Model.players.Player;
//import View.utils.ANSI;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class ScrabbleServer2 implements ServerProtocol {
//    //Port used by server
//    private  static final int PORT = 8888;
//
//    private  ArrayList<ScrabbleClientHandler2> clientsFunc;
//    private ArrayList<ScreabbleClient2> clients;
//    private ArrayList<Player> players;
//
//    private  ExecutorService poop;
//
//
//    public static void main(String[] args) throws IOException {
//        ScrabbleServer2 server = new ScrabbleServer2();
//        server.run();
//    }
//
//
//    public ScrabbleServer2() {
//        this.clientsFunc  = new ArrayList<>();
//        this.poop = Executors.newFixedThreadPool(4);
//    }
//
//    public void run() throws IOException {
//        ServerSocket listener = new ServerSocket(PORT);
//        System.out.println("Server is waiting for clients...");
//        while (true) {
//            // A socket object that corresponds to a particular connection
//            Socket client = listener.accept();
//
//            System.out.println("New client had connected, waiting for verification");
//
//            ScrabbleClientHandler2 clientThread = new ScrabbleClientHandler2(client, this);
//            clientsFunc.add(clientThread);
//
//            poop.execute(clientThread);
//        }
//    }
//
//    @Override
//    public String handleConnection(String playerName) {
//        clients.add(new ScreabbleClient2(playerName));
//        return "Client " + ANSI.PURPLE_BOLD_BRIGHT + playerName + ANSI.RESET +" connected to the server";
//    }
//
//    @Override
//    public String handlePlace(String coordinates, boolean orientation, String word) {
//        return null;
//    }
//
//    @Override
//    public String handleInitiateGame() {
//        return null;
//    }
//
//    @Override
//    public String handleSkip() {
//        return null;
//    }
//
//    @Override
//    public String handleSwap(String tiles) {
//        return null;
//    }
//
//    @Override
//    public String handleExit() {
//        return null;
//    }
//}
