package View;

import Controller.Protocols.ProtocolMessages;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * class for a client*/
public class ScrabbleClient implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
//    private boolean playerMade = false;
    private boolean quit = false;


    /**
     * This method is responsible for creating connection with the server using the same host name and port.
     * Also creates reader and writer that work with the socket streams
     * @ensures to create the connection with the server if server is started, or ask to retry connection until the server is started with the same host and port*/
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

    /**
     * This method sends a message into the stream
     * @param msg is the message to be sent
     * @requires msg != null
     * @ensures to send the message into the stream or inform the user if any errors appear*/
    public synchronized void sendMessage(String msg) {
        if (out != null) {
            try {
                out.write(msg);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                System.out.println("Server is unreachable!");
            }
        } else {
            System.out.println("Could not write to server!");
        }
    }
//    public String readLineFromServer() {
//        if (in != null) {
//            try {
//                // Read and return answer from Server
//                String answer = in.readLine();
//                if (answer == null) {
//                    System.out.println("Could not read from server!");
//                }
//                return answer;
//            } catch (IOException e) {
//                System.out.println("Could not read from server!");
//            }
//        } else {
//            System.out.println("Could not read from server!");
//        }
//        return null;
//    }

    /**
     * This method reads multiple lines from the server and returns them
     * @ensures to return the lines from server or inform the user if any errors appear
     * @return all the lines sent by the server as a single String*/
    public String readMultipleLinesFromServer() throws IOException {
        if (in != null) {
                // Read and return answer from Server
                StringBuilder sb = new StringBuilder();
                String line = in.readLine();
                while (line != null && !line.isBlank()){
                    sb.append(line + System.lineSeparator());
                    line = in.readLine();
                }
                return sb.toString();
        } else {
            System.out.println("Could not read from server!");

        }
        return null;
    }

    /*we used this method in the beginning of the implementation to check whether the client connects to the server
     for the handshake process we now use connect commands*/
//    public void doHandshake(){
//        sendMessage("Hello");
//        if(readLineFromServer().equals("Hello")){
//            System.out.println("Connection established");
//        }
//
//    }

    /**
     * This method clears the connection with the server, setting the socket, reader and writer to null*/
    public void clearConnection(){
        socket = null;
        in = null;
        out = null;
    }

    /**
     * This method starts a new ScrabbleClient by creating a connection and a new thread*/
    public void start(){
        System.out.println("Welcome to scrabble, connect to the server using command 'c' 'your name', or type 'help' for the help menu.");
        boolean run = true;
        createConnection();
        new Thread(this).start();
        while (run) {
            try{
                receiveMessage();
            } catch (IOException e) {
                if(!quit) {
                    System.out.println("Can't reach the server, type 'D' to close the game");
                }
                run = false;
            }
        }
    }

    /**
     * This method reads the input of the user and reacts to it according to the command used, most of the cases it also sends an appropriate message to the server
     */
    public void clientCommands() throws IOException {
        Scanner sc = new Scanner(System.in);
        String message = sc.nextLine();
        String[] splitMsg = message.split(" ");
        switch(splitMsg[0]) {
            case ProtocolMessages.CONNECT:
                sendMessage(String.join(ProtocolMessages.DELIMITER, splitMsg));
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
            case ProtocolMessages.DISCONNECT:
                sendMessage(ProtocolMessages.DISCONNECT);
                quit = true;
                throw new IOException("Game Over!");
            case "help":
                printHelpMenu();
                break;
            default:
                System.out.println("Invalid command, type 'help' for the help menu.");
        }
    }

    /**
     * This method shows the messages sent by the server using the readMultipleLinesFromServer() method*/
    public void receiveMessage() throws IOException {
//        while(true){
        String serverMsg = null;
        serverMsg = readMultipleLinesFromServer();
        String[] split = serverMsg.split(ProtocolMessages.DELIMITER);
            switch(split[0]){
                case ProtocolMessages.INITIATE_GAME:
                    System.out.println(split[1]);
                    break;
                case ProtocolMessages.CONFIRM_CONNECT:
                    System.out.println(split[1]);
//                    if (split[1].contains("connected to the server")) {
//                        playerMade = true;
//                    } else {
//                        playerMade = false;
//                    }
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
                case  ProtocolMessages.FINISH_GAME:
                    System.out.println(split[1]);
                    throw new IOException("Game Over!");
            }
        //}
    }
    @Override
    public void run() {
        try {
            while(true) {
                clientCommands();
            }
        }catch (IOException e){
            System.out.println("Game finished!");
        }
    }
    /**
     * This method prints the help menu to the console */
    public void printHelpMenu(){
        System.out.println("To connect to the server: 'c' 'name'. \nTo start the game: 'fs'. \n" +
                "To make a move: 'm' 'coordinates' 'orientation(H or V)' 'word' \n" +
                "To skip your turn: 's' \nTo replace tiles: 'r' 'tile1' 'tile2' 'tile3' etc... \n" +
                "To disconnect and finish game: 'D'");
    }

    public static void main(String[] args) {
        new ScrabbleClient().start();
    }

}
