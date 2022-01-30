//package Controller;
//
//import Controller.Protocols.ClientProtocol;
//import Controller.Protocols.ProtocolMessages;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//public class ScrabbleClientHandler2 implements Runnable{
//    private Socket client;
//    private BufferedReader in;
//    private PrintWriter out;
//    private ScrabbleServer2 server;
//
//    public ScrabbleClientHandler2(Socket clientSocket, ScrabbleServer2 server) throws IOException {
//        this.client = clientSocket;
//        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//        out = new PrintWriter(client.getOutputStream() , true);
//        this.server = server;
//    }
//
//
//    @Override
//    public void run() {
//        try {
//            while (true) {
//                //Reading the client's request
//                String request = in.readLine();
//                String[] splittedMsg = request.split(ProtocolMessages.DELIMITER);
//                switch(splittedMsg[0]){
//                    case ProtocolMessages.CONNECT:
//                         server.handleConnection(splittedMsg[1]);
//                         break;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            out.close();
//            try {
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
