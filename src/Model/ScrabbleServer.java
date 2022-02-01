package Model;

import Controller.Protocols.ProtocolMessages;
import Controller.Protocols.ServerProtocol;
import Controller.ScrabbleClientHandler;
import Exceptions.SquareNotEmptyException;
import Model.players.HumanPlayer;
import Model.players.Player;
import View.utils.ANSI;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/** Class for the scrabble server
 * @author Dani Mahaini && Mark Zhitchenko */
public class ScrabbleServer implements ServerProtocol {
    private ServerSocket ssock;
    private List<ScrabbleClientHandler> clients;
    private ArrayList<Player> players;
    private Game game;
    private Player currentPlayer;
    private int currentPlayerIndex;

    /**
     * ScrabbleServer constructor that initialises the clients List and players List*/
    public ScrabbleServer(){
        this.clients = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    /** a getter to see the game variable */
    public Game getGame() {
        return game;
    }

    /** a getter to see the clients variable*/
    public List<ScrabbleClientHandler> getClients() {
        return clients;
    }
    /** a getter for the players List*/
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * This method sets up the server by creating a ServerSocket
     * @ensures to set up a server or show an appropriate error message*/
    public void setUp(){
        ssock = null;
        while(ssock == null){
            int port = 8888;
            try{
                ssock = new ServerSocket(port, 0, InetAddress.getByName("localhost"));
            }catch(IOException e){
                System.out.println("ERROR: could not create a socket on 'localhost' and port " + port + ".");
            }
        }
    }

    /**
     * This method creates new client handlers for each connected client
     * @ensures to create a ScrabbleClientHandler instance for each client connected and start a new thread for each client handler*/
    public void run(){
        boolean openNewSock = true;
        while(openNewSock){
            try {
                setUp();
                int PlayerIndex = 1;
                while(true) {
                    String name = "Player" + PlayerIndex;
                    Socket sock = ssock.accept();
                    ScrabbleClientHandler handler = new ScrabbleClientHandler(sock, this, name);
                    new Thread(handler).start();

                    if (!handler.getName().equals("Player")) {
                        clients.add(handler);
                    }

                    PlayerIndex++;

                }

            } catch(IOException e) {
                System.out.println("Connection issues");
                openNewSock = false;
            }
        }

    }

    /**
     * This method sets up game by creating a new Board, assigning the players and handing out the tiles
     * @ensures to properly initialise the game variable of the class*/
    public void setUpGame(){
        Square[][] squares = new Square[15][15];
        Board board = new Board(squares);
        board.setBoard();

        this.game = new Game(players, board);

        game.handOut();


    }

    /** This method removes a client handler from the list of clients
     * @param client is the client that is going to be removed
     * @requires clients.contains(client)
     * @ensures to remove client form clients List*/
    public void removeClient(ScrabbleClientHandler client) {
        this.clients.remove(client);
    }


    @Override
    public void handleConnection(String playerName) {
        Player clientPlayer = new HumanPlayer(playerName, game);
        players.add(clientPlayer);
        currentPlayerIndex = players.size() - 1;
        currentPlayer = players.get(currentPlayerIndex);

        sendMessageToAll(ProtocolMessages.CONFIRM_CONNECT + ProtocolMessages.DELIMITER + ANSI.RESET + "Player " + ANSI.PURPLE_BOLD_BRIGHT + playerName + ANSI.RESET +" connected to the server." + ANSI.WHITE_BRIGHT + " Type " + ANSI.YELLOW_BRIGHT+ "'fs' " + ANSI.WHITE_BRIGHT + "to start game! \n");

    }

    @Override
    public void handlePlace(String coordinates, boolean orientation, String word, ScrabbleClientHandler caller) {
        if(currentPlayer.getName().equals(caller.getName())){
            currentPlayer.setMove(new Move(game, currentPlayer));
            try {
                currentPlayer.getMove().place(coordinates, orientation, word, game.getBoard());
            } catch (SquareNotEmptyException e) {
                for(ScrabbleClientHandler h : clients){
                    h.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + e.getMessage() + "\n");
                }
            }

            game.handOut();

            currentPlayerIndex++;
            currentPlayerIndex %= players.size();

            boolean test = false;
            for (ScrabbleClientHandler h : clients) {
                if (currentPlayer.getName().equals(h.getName())) {
                    if(currentPlayer.getMove().isRequestAnother()) {
                        h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + "Invalid input, try one more time! (Type 'help' for help menu) \n");
                        continue;
                    }
                    else {
                        if(currentPlayer.getMove().isMoveLost()) {
                            h.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + "Your move is invalid, you lose your turn \n");
                        }
                        else if(!currentPlayer.getMove().isRequestAnother()){
                            h.sendMessage(ProtocolMessages.UPDATE_TABLE + ProtocolMessages.DELIMITER + "Updated board: \n" + game.getBoard().toString() + "\n" + game.tilesToString(currentPlayer) + "\n" + "You made your move, it's " + players.get(currentPlayerIndex).getName()+ "'s turn now! \n");
                            continue;
                        }
                    }
                }

                for (Player p : players) {
                    if(p.getName().equals(h.getName())) {
                        if(!currentPlayer.getMove().isRequestAnother()) {
                            if (!currentPlayer.getMove().isMoveLost()) {
                                h.sendMessage(ProtocolMessages.UPDATE_TABLE + ProtocolMessages.DELIMITER + "Updated board: \n" + game.getBoard().toString() + "\n" + game.tilesToString(p) + "\n" + "It's " + players.get(currentPlayerIndex).getName() + "'s turn!" + "\n");
                            }
                            if(currentPlayer.getMove().isMoveLost()){
                                test = true;
                            }
                            if(p.getName().equals(h.getName()) && !p.equals(currentPlayer)) {
                                if (test) {
                                    h.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + currentPlayer.getName() + "'s move was invalid, it's " +players.get(currentPlayerIndex).getName() + "'s turn now! \n");
                                    test = false;
                                }
                            }
                        }
                    }
                }
            }
            if(!currentPlayer.getMove().isRequestAnother()) {
                currentPlayer = players.get(currentPlayerIndex);
            }

        } else{
            caller.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + "It is not your turn, mate! \n");
        }
        if(game.isFinished()){
            sendMessageToAll(ProtocolMessages.FINISH_GAME + ProtocolMessages.DELIMITER + game.determineWinner() + "\n");
        }
    }


    @Override
    public void handleForceStart(ScrabbleClientHandler caller) {
        setUpGame();
        for(Player p : players) {
            p.setGame(game);
            for (ScrabbleClientHandler h : clients) {
                if (p.getName().equals(h.getName())) {
                    h.sendMessage(ProtocolMessages.INITIATE_GAME + ProtocolMessages.DELIMITER + "Starting game... players: " + players.get(0).getName() + " & " + players.get(1).getName() + "\n" + game.getBoard().toString() + "\n" + game.tilesToString(p) + "\n");
                    break;
                }
            }
            if(caller.getName().equals(p.getName())) {
                currentPlayerIndex = players.indexOf(p);
                currentPlayer = p;
                sendMessageToAll(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + "It's " + caller.getName() + "'s turn! (First move should include the CENTER square!) \n");
            }
        }
    }

    @Override
    public void handleSkipAndSwap(ScrabbleClientHandler caller, String tiles) {
        boolean swap = false;

        if(tiles != null){
            swap = true;
        }
        if(currentPlayer.getName().equals(caller.getName())){
            if(swap) {
                currentPlayer.setMove(new Move(game, currentPlayer));
                currentPlayer.getMove().swap(tiles);
            }
            else{
                currentPlayer.setMove(new Move(game, currentPlayer));
                currentPlayer.getMove().swap(" ");
            }

            currentPlayerIndex++;
            currentPlayerIndex %= players.size();

            for (ScrabbleClientHandler h : clients) {
                if (currentPlayer.getName().equals(h.getName())) {
                    if(swap) {
                        if(currentPlayer.getMove().isMoveMade()) {
                            h.sendMessage(ProtocolMessages.GIVE_TILE + ProtocolMessages.DELIMITER + "Board with your new tiles: \n" + game.getBoard().toString() + "\n" + game.tilesToString(currentPlayer) + "\n Opponent's turn now! \n");
                        }else if(currentPlayer.getMove().isRequestAnother()){
                            h.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + "You don't have the tiles you want to replace! Try again! \n");
                        }
                    }else{
                        h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + "You skipped your turn \n");
                    }

                }else {
                    if (!swap) {
                        h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + "PLayer " + currentPlayer.getName() + " skipped his turn! It's " + players.get(currentPlayerIndex).getName() + "'s turn now! \n");
                    }
                    else {
                        if (!currentPlayer.getMove().isRequestAnother()) {
                            h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + "Your opponent swapped tiles, " + players.get(currentPlayerIndex).getName() + "'s turn now! \n");
                        }
                    }
                }

            }
            if(!currentPlayer.getMove().isRequestAnother() || !swap) {
                currentPlayer = players.get(currentPlayerIndex);
            }
        } else{
            caller.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + "It is not your turn, mate! \n");
        }
        if(game.isFinished()){
            sendMessageToAll(ProtocolMessages.FINISH_GAME + ProtocolMessages.DELIMITER + game.determineWinner() + "\n");
        }
    }

    @Override
    public void handleExit() {
        game.setFinishGame(true);
        sendMessageToAll(ProtocolMessages.FINISH_GAME + ProtocolMessages.DELIMITER + "Player " + currentPlayer.getName() + " finished the game! \n"+ game.determineWinner() + "\n");

    }

    /**
     * This method sends a message to all the clients
     * @param msg is the message to send
     * @requires msg != null
     * @ensures to send the message to all clients*/
    public void sendMessageToAll(String msg){
        for(ScrabbleClientHandler h : clients){
            h.sendMessage(msg);
        }
    }

    public static void main(String[] args) {
        new ScrabbleServer().run();
    }
}
