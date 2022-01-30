//package Controller;
//
//
//import Controller.Protocols.ClientProtocol;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//public class ScreabbleClient2 implements ClientProtocol {
//
//    private static final String SERVER_IP = "127.0.0.1";
//    private static final int SERVER_PORT = 8888;
//
//    private String name;
//
//    public ScreabbleClient2(String name) {
//        this.name = name;
//    }
//
//    public static void main(String[] args) throws IOException {
//
//        //Created a connection to the server
//        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
//
//        // Object "in" that intakes the stream
//        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//        // Object "out" that adds to the stream
//        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
//        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//
//        while (true) {
//            System.out.println("> ");
//            String command = keyboard.readLine();
//
//            if (command.equals("quit")) {
//                break;
//            }
//
//            out.println(command);
//
//            String serverResponse = in.readLine();
//            System.out.println(serverResponse);
//
//        }
//
//        socket.close();
//        System.exit(0);
//    }
//
//    @Override
//    public void doConnect(String playerName) {
//
//    }
//
//    @Override
//    public void doPlace(String coordinates, boolean orientation, String word) {
//
//    }
//
//    @Override
//    public void doInitiateGame() {
//
//    }
//
//    @Override
//    public void doSkip() {
//
//    }
//
//    @Override
//    public void doSwap(String tiles) {
//
//    }
//
//    @Override
//    public void doExit() {
//
//    }
//}
