package Controller;

import Model.players.Player;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ScrabbleClient {
    private Player clientPlayer;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public void setClientPlayer(Player player) {
        this.clientPlayer = player;
    }

    public Player getClientPlayer() {
        return clientPlayer;
    }

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
        sendMessage("Hello");
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
}
