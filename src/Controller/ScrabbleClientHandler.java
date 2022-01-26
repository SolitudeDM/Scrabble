package Controller;

import java.io.*;
import java.net.Socket;

public class ScrabbleClientHandler {
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
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.server = server;
            this.name = name;
        } catch(IOException e){
            shutdown();
        }
    }

    public void run(){
        String message;
        try{
            message = in.readLine();
            while(message != null){
                handleCommand(message);
                out.newLine();
                out.flush();
                message = in.readLine();
            }
            shutdown();
        }catch(IOException e){
            shutdown();
        }
    }

    private void handleCommand(String message) throws IOException{
        //to be implemented
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
}
