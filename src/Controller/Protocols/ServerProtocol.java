package Controller.Protocols;

import Controller.ScrabbleClientHandler;

public interface ServerProtocol {
    /**
     * Called when new connection is established, creates a new instance of HumanPlayer and add it to players List
     * @param playerName is the name of connected user
     * @requires playerName != null
     * @ensures to connect a player, add him to players List and send confirmation
     */
    public void handleConnection(String playerName);

    /**
     * Called when client wants to place tiles
     * @requires coordinates != null && to be within the board boundaries
     * @requires orientation != null && to be either 'H' or 'V'
     * @requires word != null && to be a valid word
     * @param coordinates coordinates of the move
     * @param orientation orientation of the move
     * @param word word to be placed
     * @param caller is the caller of the command
     * @ensures to send updated board with the made move or send the appropriate error message(if needed)
     */
    public void handlePlace(String coordinates, boolean orientation, String word, ScrabbleClientHandler caller);

    /**
     * Called when client wants to initiate game
     * @param caller is the caller of the method
     * @requires clients.contains(caller)
     * @ensures to initiate the game and set the caller as the currentPlayer or send the appropriate error message(if called when players.size() < 2) */
    public void handleForceStart(ScrabbleClientHandler caller);

    /**
     * Called when client wants to skip turn or swap tiles
     * @param caller is the caller of the method
     * @param tiles are the tiles the player wants to swap (if tiles == null this means the player skips his turn)
     * @requires clients.contains(caller)
     * @ensures to skip the turn or to swap the tiles or send appropriate error message(if tiles are not in the hand of the player)*/
    public void handleSkipAndSwap(ScrabbleClientHandler caller, String tiles);

    /**
     * Called when client wants to exit the game
     * @ensures to finish the game and show the determined winner*/
    public void handleExit();
}
