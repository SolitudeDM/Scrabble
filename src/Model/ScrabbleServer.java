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
    private int fsCount;
    private boolean gameCreated = false;
    private boolean gameRunning = false;

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

    /** a getter for the gameCreated boolean */
    public boolean isGameCreated(){
        return gameCreated;
    }

    /** a getter for the gameRunning boolean*/
    public boolean isGameRunning() {
        return gameRunning;
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
                    String name = "Player " + PlayerIndex;
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

        sendMessageToAll(ProtocolMessages.CONFIRM_CONNECT + ProtocolMessages.DELIMITER + ANSI.RESET + "Player " + ANSI.PURPLE_BOLD_BRIGHT + playerName + ANSI.RESET +" connected to the server." + ANSI.WHITE_BRIGHT + " Type " + ANSI.YELLOW_BRIGHT+ "'fs' " + ANSI.WHITE_BRIGHT + " if you are ready to start the game! \n");

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
                        h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "Invalid input, try one more time! (Type 'help' for help menu) " + ANSI.RESET + "\n");
                        continue;
                    }
                    else {
                        if(currentPlayer.getMove().isMoveLost()) {
                            h.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "Your move is invalid, you lose your turn " + ANSI.RESET + "\n");
                        }
                        else if(!currentPlayer.getMove().isRequestAnother()){
                            h.sendMessage(ProtocolMessages.UPDATE_TABLE + ProtocolMessages.DELIMITER + ANSI.WHITE_BRIGHT + "Updated board: \n" + game.getBoard().toString() + "\n" + game.tilesToString(currentPlayer) + "\n" + ANSI.WHITE_BRIGHT + "You made your move, it's "  + ANSI.PURPLE_BRIGHT + players.get(currentPlayerIndex).getName() + ANSI.WHITE_BRIGHT + "'s turn now! " + ANSI.RESET + "\n");
                            continue;
                        }
                    }
                }

                for (Player p : players) {
                    if(p.getName().equals(h.getName())) {
                        if(!currentPlayer.getMove().isRequestAnother()) {
                            if (!currentPlayer.getMove().isMoveLost()) {
                                h.sendMessage(ProtocolMessages.UPDATE_TABLE + ProtocolMessages.DELIMITER + ANSI.WHITE_BRIGHT + "Updated board: \n" + game.getBoard().toString() + "\n" + game.tilesToString(p) + "\n" + ANSI.WHITE_BRIGHT + "It's "  + ANSI.PURPLE_BRIGHT + players.get(currentPlayerIndex).getName() + ANSI.WHITE_BRIGHT + "'s turn!" + ANSI.RESET + "\n");
                            }
                            if(currentPlayer.getMove().isMoveLost()){
                                test = true;
                            }
                            if(p.getName().equals(h.getName()) && !p.equals(currentPlayer)) {
                                if (test) {
                                    h.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + currentPlayer.getName() + ANSI.WHITE_BRIGHT + "'s move was invalid, it's " + ANSI.PURPLE_BRIGHT + players.get(currentPlayerIndex).getName() + "'s turn!" + ANSI.RESET + "\n");
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
            caller.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER  + ANSI.RED_BOLD_BRIGHT + "It is not your turn, mate!" + ANSI.RESET + "\n");
        }
        if(game.isFinished()){
            sendMessageToAll(ProtocolMessages.FINISH_GAME + ProtocolMessages.DELIMITER + game.determineWinner() + "\n");
        }
    }


    @Override
    public void handleForceStart(ScrabbleClientHandler caller) {
        if(!gameCreated) {
            setUpGame();
            fsCount++;

            for (Player p : players) {
                p.setGame(game);
                if (caller.getName().equals(p.getName())) {
                    currentPlayerIndex++;
                    currentPlayerIndex %= players.size();
                    currentPlayer = players.get(currentPlayerIndex);
                }
                if (fsCount == players.size() && fsCount == clients.size()) {
                    for (ScrabbleClientHandler h : clients) {
                        if (p.getName().equals(h.getName())) {
                            if (players.size() == 2) {
                                h.sendMessage(ProtocolMessages.INITIATE_GAME + ProtocolMessages.DELIMITER + ANSI.WHITE_BRIGHT + "Starting game... players: " + players.get(0).getName() + " & " + players.get(1).getName() + "\n" + game.getBoard().toString() + "\n" + game.tilesToString(p) + ANSI.RESET + "\n" + ANSI.WHITE_BRIGHT + "It's " + ANSI.PURPLE_BRIGHT + currentPlayer.getName() + ANSI.WHITE_BRIGHT + "'s turn! (First move should include the CENTER square!) \n");
                                gameCreated = true;
                                gameRunning = true;
                                break;
                            } else if (players.size() == 3) {
                                h.sendMessage(ProtocolMessages.INITIATE_GAME + ProtocolMessages.DELIMITER + ANSI.WHITE_BRIGHT + "Starting game... players: " + players.get(0).getName() + " & " + players.get(1).getName() + " & " + players.get(2).getName() + "\n" + game.getBoard().toString() + "\n" + game.tilesToString(p) + ANSI.RESET + "\n" + ANSI.WHITE_BRIGHT + "It's " + ANSI.PURPLE_BRIGHT + currentPlayer.getName() + ANSI.WHITE_BRIGHT + "'s turn! (First move should include the CENTER square!) \n");
                                gameCreated = true;
                                gameRunning = true;
                                break;
                            } else if (players.size() == 4) {
                                h.sendMessage(ProtocolMessages.INITIATE_GAME + ProtocolMessages.DELIMITER + ANSI.WHITE_BRIGHT + "Starting game... players: " + players.get(0).getName() + " & " + players.get(1).getName() + " & " + players.get(2).getName() + " & " + players.get(3).getName() + "\n" + game.getBoard().toString() + "\n" + game.tilesToString(p) + ANSI.RESET + "\n" + ANSI.WHITE_BRIGHT + "It's " + ANSI.PURPLE_BRIGHT + currentPlayer.getName() + ANSI.WHITE_BRIGHT + "'s turn! (First move should include the CENTER square!) \n");
                                gameCreated = true;
                                gameRunning = true;
                                break;
                            }
                        }
                    }
                } else {
                    sendMessageToAll(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + "Player " + ANSI.PURPLE_BOLD_BRIGHT + caller.getName() + ANSI.WHITE_BRIGHT + " is ready to start, type "+ ANSI.YELLOW_BRIGHT + "'fs'" + ANSI.WHITE_BRIGHT + "if you are ready as well! \n");
                    break;
                }
            }
        } else {
            caller.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "Game is already created! \n");
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
                            h.sendMessage(ProtocolMessages.GIVE_TILE + ProtocolMessages.DELIMITER + ANSI.WHITE_BRIGHT + "Board with your new tiles: \n" + game.getBoard().toString() + "\n" + game.tilesToString(currentPlayer) + "\n Opponent's turn now! " + ANSI.RESET + "\n");
                        }else if(currentPlayer.getMove().isRequestAnother()){
                                h.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "You don't have the tiles you want to replace! Try again! " + ANSI.RESET + "\n");
                        }
                    }else{
                        h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + ANSI.WHITE_BRIGHT + "You skipped your turn " + ANSI.RESET + "\n");
                    }

                }else {
                    if (!swap) {
                        h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + ANSI.WHITE_BRIGHT + "PLayer " + ANSI.PURPLE_BRIGHT + currentPlayer.getName() + ANSI.WHITE_BRIGHT + " skipped his turn! It's " + ANSI.PURPLE_BRIGHT + players.get(currentPlayerIndex).getName() + ANSI.WHITE_BRIGHT + "'s turn now!" + ANSI.RESET + "\n");
                    }
                    else {
                        if (!currentPlayer.getMove().isRequestAnother()) {
                            h.sendMessage(ProtocolMessages.FEEDBACK + ProtocolMessages.DELIMITER + ANSI.WHITE_BRIGHT + "Your opponent swapped tiles, " + ANSI.PURPLE_BRIGHT + players.get(currentPlayerIndex).getName() + ANSI.WHITE_BRIGHT + "'s turn now! " + ANSI.RESET + "\n");
                        }
                    }
                }

            }
            if(!currentPlayer.getMove().isRequestAnother() || !swap) {
                currentPlayer = players.get(currentPlayerIndex);
            }
        } else{
            caller.sendMessage(ProtocolMessages.CUSTOM_EXCEPTION + ProtocolMessages.DELIMITER + ANSI.RED_BOLD_BRIGHT + "It is not your turn, mate! \n");
        }
        if(game.isFinished()){
            sendMessageToAll(ProtocolMessages.FINISH_GAME + ProtocolMessages.DELIMITER + game.determineWinner() + "\n");
        }
    }

    @Override
    public void handleExit() {
        game.setFinishGame(true);
        sendMessageToAll(ProtocolMessages.FINISH_GAME + ProtocolMessages.DELIMITER + ANSI.WHITE_BRIGHT + "Player " + ANSI.PURPLE_BRIGHT + currentPlayer.getName() + ANSI.WHITE_BRIGHT + " finished the game! \n" + game.determineWinner() + ANSI.RESET + "\n");

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
