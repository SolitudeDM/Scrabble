package Controller;

import Controller.Protocols.ClientProtocol;
import Controller.Protocols.ProtocolMessages;
import Model.players.HumanPlayer_v3;
import Model.players.Player;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ScrabbleClient implements ClientProtocol {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    private boolean stop = false;


    public void createConnection(){
        clearConnection();
        while(socket == null){
            String host = "localhost";
            int port = 8888;
            try{

                InetAddress addr = InetAddress.getByName(host);
                socket = new Socket(addr, port);

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }catch(IOException e){
                System.out.println("ERROR: could not create a socket on "
                        + host + " and port " + port + ".");
            }
        }
    }

    public synchronized void sendMessage(String msg) {
        if (out != null) {
            try {
                out.write(msg);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Could not write to server!");
        }
    }

    public String readLineFromServer() {
        if (in != null) {
            try {
                // Read and return answer from Server
                String answer = in.readLine();
                if (answer == null) {
                    System.out.println("Could not read from server!");
                }
                return answer;
            } catch (IOException e) {
                System.out.println("Could not read from server!");
            }
        } else {
            System.out.println("Could not read from server!");
        }
        return null;
    }

    public void doHandshake(){
//        sendMessage("Hello");
        if(readLineFromServer().equals("Hello")){
            System.out.println("Connection established");
        }

    }

    public void clearConnection(){
        socket = null;
        in = null;
        out = null;
    }

    public void closeConnection(){
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        boolean run = true;
        createConnection();
        while (run) {
            clientCommands();
        }
    }

    public void clientCommands(){
        Scanner sc = new Scanner(System.in);
        String message = sc.nextLine();
        sendMessage(message);
        String[] splitMsg = message.split(ProtocolMessages.DELIMITER);
        switch(splitMsg[0]) {
            case ProtocolMessages.CONNECT:
                doConnect(splitMsg[1]);
                break;
            case "Hello":
                doHandshake();
                break;
        }
    }



    @Override
    public void doConnect(String playerName) {
        sendMessage(ProtocolMessages.CONNECT + ProtocolMessages.DELIMITER + playerName);
        System.out.println(readLineFromServer());
    }

    @Override
    public void doPlace(String coordinates, boolean orientation, String word) {

    }

    @Override
    public void doSkip() {

    }

    @Override
    public void doSwap(String tiles) {

    }

    @Override
    public void doExit() {

    }

    public static void main(String[] args) {
        new ScrabbleClient().start();
    }
}
