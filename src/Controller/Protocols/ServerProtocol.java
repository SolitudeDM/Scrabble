package Controller.Protocols;

import Controller.ScrabbleClientHandler;

public interface ServerProtocol {
    /**
     * Called when new connection is established
     * @ensures to connect a player and send confirmation
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
     * @ensures to send updated board with the made move
     */
    public void handlePlace(String coordinates, boolean orientation, String word, ScrabbleClientHandler caller);

    /**
     * Called when client wants to initiate game*/
    public void handleForceStart(ScrabbleClientHandler caller);

    /**
     * Called when client wants to skip turn */
    public void handleSkipAndSwap(ScrabbleClientHandler caller, String tiles);

    /**
     * Called when client wants to exit the game*/
    public void handleExit();
//    /**
//     * Called when a new room is being created
//     * @return a room id that the person just created
//     */
//    public String doCreateRoom(String player_number) ;

//    /**
//     * Sends
//     * @send the table info, player_list with their associated client ids
//     * @ensures it is synchronized
//     */
//    public void doStart(IRoom room);
//
//    /**`
//     * Sends an information when a players join a room to all the players
//     * @param room
//     * @param identifier - player name and client id
//     * @send a player identifier
//     * @ensures it is synchronized
//     */
//    public void doJoin(IRoom room, String identifier);
//
//    /**
//     * Sends a score update to all the players
//     * @param room
//     * @param info - the player identifier and the score
//     * @sends the info to all the players
//     * @ensures it is synchronized
//     */
//    public void doUpdateScore(IRoom room, String info);
//
//    /**
//     * Sends a table update to all the players
//     * @param room
//     * @param table - the table string representation
//     * @sends the table to all the players
//     * @ensures it is synchronized
//     */
//    public void doUpdateTable(IRoom room, String table);
//
//    /**
//     * Called when a game ends
//     * @param room
//     * @param best_player the best player id
//     * @param score the best score
//     * @sends information about the ended game
//     * @ensures it is synchronized
//     */
//    public void doFinish(IRoom room, String best_player, int score);
}
