package View;

import Controller.Protocols.ProtocolMessages;
import View.utils.ANSI;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * class for a Scrabble client
 * @author Dani Mahaini && Mark Zhitchenko*/
public class ScrabbleClient implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
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
                System.out.print(ANSI.RED_BOLD_BRIGHT);
                System.out.println("ERROR: could not create a socket on "
                        + host + " and port " + port + ".");
                System.out.print(ANSI.RESET);
                while (true) {
                    System.out.print(ANSI.WHITE_BRIGHT);
                    System.out.println("Want to connect again?(true/false)");
                    System.out.print(ANSI.RESET);
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

    /**
     * This method reads multiple lines from the server and returns them
     * @ensures to return the lines from server or inform the user if any errors appear
     * @return all the lines sent by the server as a single String*/
    public String readMultipleLinesFromServer() throws IOException {
        if (in != null) {
            // Read and return answer from Server
            StringBuilder sb = new StringBuilder();
            String line = in.readLine();
            if(line == null){
                throw new IOException("Disconnecting...");
            }
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
        System.out.println(ANSI.WHITE_BRIGHT + "Welcome to scrabble," + ANSI.WHITE_BOLD_BRIGHT +  " connect" + ANSI.WHITE_BRIGHT +" to the server using command "+ ANSI.YELLOW_BRIGHT +"'c' 'your name'" + ANSI.WHITE_BRIGHT + ", or type" + ANSI.YELLOW_BRIGHT + " 'help' " + ANSI.WHITE_BRIGHT + "for the help menu.");
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
            case ProtocolMessages.CUSTOM_COMMAND:
                sendMessage(String.join(ProtocolMessages.DELIMITER, splitMsg));
                break;
            default:
                System.out.println("Invalid command, type 'help' for the help menu.");
                break;
        }
    }

    /**
     * This method shows the messages sent by the server using the readMultipleLinesFromServer() method*/
    public void receiveMessage() throws IOException {
//        while(true){
        String serverMsg;
        serverMsg = readMultipleLinesFromServer();
        String[] split = serverMsg.split(ProtocolMessages.DELIMITER);
        switch(split[0]){
            case ProtocolMessages.INITIATE_GAME:
                System.out.println(split[1]);
                break;
            case ProtocolMessages.CONFIRM_CONNECT:
                System.out.println(split[1]);
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
            case ProtocolMessages.CUSTOM_COMMAND:
                StringBuilder sb = new StringBuilder();
                for(String s : split){
                    if(s.equals("/")){
                        continue;
                    }
                    sb.append(s);
                    sb.append(" ");
                }
                System.out.println(sb);
                break;
        }
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
        System.out.println(ANSI.WHITE_BRIGHT + "To " + ANSI.YELLOW_UNDERLINED + "connect" + ANSI.WHITE_BRIGHT + " to the server: " + ANSI.YELLOW_BRIGHT + "'c' 'name'. \n" + ANSI.WHITE_BRIGHT + "To " + ANSI.YELLOW_UNDERLINED + "start" + ANSI.WHITE_BRIGHT + " the game: " + ANSI.YELLOW_BRIGHT + "'fs'. \n" +
                ANSI.WHITE_BRIGHT +"To " + ANSI.YELLOW_UNDERLINED + "make a move" + ANSI.WHITE_BRIGHT + ": " + ANSI.YELLOW_BRIGHT + "'m' 'coordinates' 'orientation(H or V)' 'word' " + ANSI.WHITE_BRIGHT + ". Coordinates, orientation and word should be " + ANSI.WHITE_BOLD_BRIGHT + " upper cases!\n" +
                ANSI.WHITE_BRIGHT + "To " + ANSI.YELLOW_UNDERLINED + "skip" + ANSI.WHITE_BRIGHT + " your turn: " + ANSI.YELLOW_BRIGHT + " 's' \n" + ANSI.WHITE_BRIGHT + "To " + ANSI.YELLOW_UNDERLINED + "replace" + ANSI.WHITE_BRIGHT + " tiles:" + ANSI.YELLOW_BRIGHT + " 'r' 'tile1' 'tile2' 'tile3' etc... " + ANSI.WHITE_BRIGHT + "Write the letter of the tile in" + ANSI.WHITE_BOLD_BRIGHT + " upper case" + ANSI.WHITE_BRIGHT + " as well!\n" +
                ANSI.WHITE_BRIGHT +"To " + ANSI.YELLOW_UNDERLINED + "disconnect" + ANSI.WHITE_BRIGHT + " and finish game: " + ANSI.YELLOW_BRIGHT + "'D' \n" + ANSI.WHITE_BRIGHT + "To " + ANSI.YELLOW_UNDERLINED + "chat" + ANSI.WHITE_BRIGHT + ":" + ANSI.YELLOW_BRIGHT + " '/' 'your message'" + ANSI.WHITE_BRIGHT + " \nP.S. Consider the spaces between your input and upper/lower cases as shown above! \n" +
                ANSI.WHITE_BRIGHT +"P.P.S if you want to use any already placed tiles in your move, rewrite the whole word, including the placed tiles.");
    }

    public static void main(String[] args) {
        new ScrabbleClient().start();
    }

}
