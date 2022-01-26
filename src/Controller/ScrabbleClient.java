package Controller;

import Model.players.Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class ScrabbleClient {
    Player player;
    Socket socket;
    BufferedReader in;
    BufferedWriter out;
}
