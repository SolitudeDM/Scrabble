package Controller;

import Controller.Protocols.ProtocolMessages;
import View.utils.ANSI;

import java.io.*;
import java.net.Socket;

public class ScrabbleClientHandler implements Runnable{
    /** Input and output streams + the socket*/
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;



    /** the connected ScrabbleServer*/
    private ScrabbleServer server;

    /** Name of the clientHandler*/
    private String name;



    public ScrabbleClientHandler(Socket sock, ScrabbleServer server, String name){
        try{
//            printWriter = new PrintWriter(sock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.server = server;
            this.name = name;
        } catch(IOException e){
            shutdown();
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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

    private void handleCommand(String message) throws IOException{
        String[] splittedMsg = message.split(ProtocolMessages.DELIMITER);
        outer:
        switch(splittedMsg[0]){
            case "Hello":
                sendMessage(server.getHello(message));
                break;
            case ProtocolMessages.CONNECT:
                for(ScrabbleClientHandler h : server.getClients()){
                    if(h.getName().equalsIgnoreCase(splittedMsg[1])){
                        sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + "This name already exists, try another one! (upper cases won't help) \n");
                        break outer;
                    }
                }
                sendMessage(server.handleConnection(splittedMsg[1]));
                this.name = splittedMsg[1]; //todo add if statements for checking splittedMsg.length
                break;
            case ProtocolMessages.MAKE_MOVE:
                if(splittedMsg.length != 4){
                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + "The move command should be of type: m-coordinates-orientation-word! \n");
                    break;
                }
                boolean vertical = false;
                if (splittedMsg[2].equals("V")) {
                    vertical = true;
                } else if (splittedMsg[2].equals("H")) {
                    vertical = false;
                } else {
                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + "Orientation should be: H (horizontal) or V (vertical)! \n");
                    break;
                }
                server.handlePlace(splittedMsg[1], vertical, splittedMsg[3], this);
                break;
            case ProtocolMessages.FORCE_START:
                if(server.getClients().size() < 2){
                    sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + "You can't start the game without at least 2 players!(You are the only one on the server now) \n");
                    break;
                }
                server.handleForceStart(this);
                break;
            case ProtocolMessages.SKIP_TURN:
                server.handleSkipAndSwap(this, null);
                break;
            case ProtocolMessages.REPLACE_TILES:
                server.handleSkipAndSwap(this, splittedMsg[1]);
                break;
            case ProtocolMessages.DISCONNECT:
                server.handleExit();
                throw new IOException("Client disconnected");
        }
    }

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
