package Controller;

import Controller.Protocols.ProtocolMessages;
import Model.ScrabbleServer;
import Model.players.Player;
import View.utils.ANSI;

import java.io.*;
import java.net.Socket;

/** Class for a Scrabble client handler
 * @author Dani Mahaini && Mark Zhitchenko*/
public class ScrabbleClientHandler implements Runnable{
    /** Input and output streams + the socket*/
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;

    /** the connected ScrabbleServer*/
    private ScrabbleServer server;

    /** Name of the clientHandler*/
    private String name;

    /** boolean to keep track of made player(so one client can't create multiple players)*/
    private boolean playerMade = false;

    private boolean fsCalled = false;

    /**
     * Constructor of the class, initialises the variables*/
    public ScrabbleClientHandler(Socket sock, ScrabbleServer server, String name){
        try{
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.server = server;
            this.name = name;
        } catch(IOException e){
            shutdown();
        }
    }

    /** A getter for the variable name*/
    public String getName() {
        return name;
    }

    @Override
    public void run(){
        String message;
        try{
            message = in.readLine();
            while(message != null){
                handleCommand(message);
                message = in.readLine();
            }
            shutdown();
        }catch(IOException e){
            shutdown();
        }
    }

    /**
     * This method handles the message sent by the clients and calls the according method from the server
     * @param message is the message received
     * @ensures to call the right method from the server according to the command sent*/
    private void handleCommand(String message) throws IOException{
        String[] splittedMsg = message.split(ProtocolMessages.DELIMITER);
        outer:
        switch(splittedMsg[0]){
            case ProtocolMessages.CONNECT:
                if(server.isGameRunning()){
                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "You can't connect to a running game! \n");
                    server.getPlayers().removeIf(p -> p.getName().equals(name));
                    shutdown();
                    break;
                }
                if(server.getPlayers().size() >= 4){
                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "Sorry, the game is already full (max 4 players)! Shutting down connection... \n");
                    shutdown();
                    break;
                }
                for(ScrabbleClientHandler h : server.getClients()) {
                    if (splittedMsg.length != 2 || splittedMsg[1].isBlank()) {
                        sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "Command 'c' should include the name. Type 'help' for more information about commands! \n");
                        break outer;
                    } else {
                        if(playerMade){
                            sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "Player is already created! \n");
                            break outer;
                        }
                        else if(h.getName().equals(splittedMsg[1])) {
                            sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "This name already exists, try another one! \n");
                            break outer;
                        }
                    }
                }
                server.handleConnection(splittedMsg[1]);
                this.name = splittedMsg[1];
                playerMade = true;
                break;
            case ProtocolMessages.MAKE_MOVE:
                if(splittedMsg.length != 4){
                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "The move command should be of type: 'm' 'coordinates' 'orientation' 'word'! Type 'help' for the help menu. \n");
                    break;
                }
                boolean vertical = false;
                if (splittedMsg[2].equalsIgnoreCase("V")) {
                    vertical = true;
                } else if (splittedMsg[2].equalsIgnoreCase("H")) {
                    vertical = false;
                } else {
                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "Orientation should be: H (horizontal) or V (vertical)! \n");
                    break;
                }
                server.handlePlace(splittedMsg[1], vertical, splittedMsg[3], this);
                break;
            case ProtocolMessages.FORCE_START:
//                if(server.isGameRunning()){
//                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "You can't connect to a running game! Type 'D' to disconnect. \n");
//                    server.getPlayers().removeIf(p -> p.getName().equals(name));
//                    shutdown();
//                    break;
//                }
                if(server.getPlayers().size() < 2){
                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "You can't start the game without at least 2 players! Wait for connections of other players! \n");
                    break;
                }
                if(!fsCalled) {
                    server.handleForceStart(this);
                }
                else if(server.isGameCreated()){
                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "Game is already created! \n");
                    break;
                }
                else {
                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "You already sent 'fs' and everyone knows that you are ready! \n");
                    break;
                }
                fsCalled = true;
                break;
            case ProtocolMessages.SKIP_TURN:
                server.handleSkipAndSwap(this, null);
                break;
            case ProtocolMessages.REPLACE_TILES:
                if(splittedMsg.length != 2 || splittedMsg[1].isBlank()){
                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "Command 'r' should also include your tiles! Type 'help' for the help menu. \n");
                    break;
                }
                server.handleSkipAndSwap(this, splittedMsg[1]);
                break;
            case ProtocolMessages.CUSTOM_COMMAND:
                if(splittedMsg.length > 1) {
                    for (ScrabbleClientHandler h : server.getClients()) {
                        h.sendMessage(ProtocolMessages.CUSTOM_COMMAND + ProtocolMessages.DELIMITER + ANSI.PURPLE_BRIGHT + name + ANSI.WHITE_BRIGHT + ":" + ANSI.WHITE + message.substring(message.indexOf("/") + 1) + ANSI.RESET + "\n");
                    }
                } else{
                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "To chat enter '/' 'your message'. \n");
                }
                break;
            case ProtocolMessages.DISCONNECT:
                server.handleExit();
                throw new IOException("Client disconnected");
        }
    }

    /**
     * This method shuts down the client, by closing the reader, writer, socket and removing this client handler from the List of clients on the server*/
    public void shutdown(){
        try{
            in.close();
            out.close();
            sock.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        server.removeClient(this);
    }

    /**
     * This method sends message into the stream
     * @param msg is the message that will be sent
     * @requires msg != null
     * @ensures to send msg to the stream */
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
}
