package Controller;

import Controller.Protocols.ProtocolMessages;
import View.utils.ANSI;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ScrabbleClient {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String playerReference;

    private boolean playerMade = false;


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
            }catch(IOException e) {
                System.out.println("ERROR: could not create a socket on "
                        + host + " and port " + port + ".");
                while (true) {
                    System.out.println("Want to connect again?(true/false)");
                    Scanner in = new Scanner(System.in);
                    try {
                        if (in.nextBoolean()) {
                            break;
                        } else {
                            System.exit(0);
                        }
                    }catch(InputMismatchException ignored){

                    }
                }
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
    public String readMultipleLinesFromServer() {
        if (in != null) {
            try {
                // Read and return answer from Server
                StringBuilder sb = new StringBuilder();
                String line = in.readLine();
                while (line != null && !line.isBlank()){
                    sb.append(line + System.lineSeparator());
                    line = in.readLine();
                }
                System.out.println(ANSI.RED + sb.toString() + ANSI.RESET); //todo delete this later
                return sb.toString();
            } catch (IOException e) {
                System.out.println("Could not read from server!");
            }
        } else {
            System.out.println("Could not read from server!");

        }
        return null;
    }

    /*we used this method in the beginning of the implementation to check whether the client connects to the server
     for the handshake process we now use connect commands*/
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
        System.out.println("Welcome to scrabble, connect to the server using command 'c'_'your name' ");
        boolean run = true;
        createConnection();
        while (run) {
            clientCommands();
            receiveMessage();
        }
    }

    public void clientCommands(){
        Scanner sc = new Scanner(System.in);
        String message = sc.nextLine();
        String[] splitMsg = message.split(" ");
        switch(splitMsg[0]) {
            case ProtocolMessages.CONNECT:
                if(!playerMade) {
                    sendMessage(String.join(ProtocolMessages.DELIMITER, splitMsg));
                    playerReference = splitMsg[1];
                } else{
                    System.out.println("Sorry, the player is already created, so please stop it, seriously...");
                }
                break;
            case "Hello":
                sendMessage(message);
                doHandshake();
                break;
            case ProtocolMessages.MAKE_MOVE:
                sendMessage(String.join(ProtocolMessages.DELIMITER, splitMsg));
                break;
            case ProtocolMessages.FORCE_START:
                sendMessage(String.join(ProtocolMessages.DELIMITER, splitMsg));
                break;
            case ProtocolMessages.SKIP_TURN:
                sendMessage(String.join(ProtocolMessages.DELIMITER, splitMsg));
                break;
            case ProtocolMessages.REPLACE_TILES:
                sendMessage(ProtocolMessages.REPLACE_TILES + ProtocolMessages.DELIMITER + message.substring(message.indexOf(" ") + 1));
                break;
        }
    }

    public void receiveMessage(){
//        while(true){
            String serverMsg = readMultipleLinesFromServer();
            String[] split = serverMsg.split(ProtocolMessages.DELIMITER);
            switch(split[0]){
                case ProtocolMessages.INITIATE_GAME:
                    System.out.println(split[1]);
                    break;
                case ProtocolMessages.CONFIRM_CONNECT:
                    System.out.println(split[1]);
                    if (split[1].contains("connected to the server")) {
                        playerMade = true;
                    } else {
                        playerReference = null;
                        playerMade = false;
                    }
                    break;
                case ProtocolMessages.UPDATE_TABLE:
                    System.out.println(split[1]);
                    break;
                case ProtocolMessages.FEEDBACK:
                    System.out.println(split[1]);
                    break;
                case ProtocolMessages.GIVE_TILE:
                    System.out.println(split[1]);
                    break;
                case ProtocolMessages.CUSTOM_EXCEPTION:
                    System.out.println(split[1]);
                    break;
            }
        //}
    }

    public static void main(String[] args) {
        new ScrabbleClient().start();
    }
}
