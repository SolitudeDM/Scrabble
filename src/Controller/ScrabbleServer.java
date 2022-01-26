package Controller;

import java.net.ServerSocket;
import java.util.List;

public class ScrabbleServer {
    private ServerSocket ssock;
    private List<ScrabbleClientHandler> clients;






    public void removeClient(ScrabbleClientHandler client) {
        this.clients.remove(client);
    }
}
