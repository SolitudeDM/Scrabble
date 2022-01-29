package Controller.Protocols;

public interface ClientProtocol {

    /**
     * Establish connection
     * @param playerName
     * */
    public void doConnect(String playerName);

//    /**
//     * Called when the player join a room
//     * @param roomId
//     * @return an initialization of the room (table, players that are already in the game);
//     */
//    public String doJoinRoom(String roomId);

//    /**
//     * Called when a new room is being created
//     * @return a room id that the person just created
//     */
//    public String doCreateRoom(String playerNumber);

    /**
     * Called when the player does a move
     * @param coordinates
     * @param orientation
     * @param word
     * @return if move has been accepted
     */
    public void doPlace(String coordinates, boolean orientation, String word);

    /**
     * called when the players connected and ready to start the game*/
    public void doForceStart();

    /**
     * Called when a player want to skip his turn
     * @return if skipping is accepted
     */
    public void doSkip();

    /**
     * Called when a player want to replace some tiles
     * @param tiles - tiles to be replaced
     * @return new tiles to be sent to the client
     */
    public void doSwap(String tiles);

    /**
     * Called when a player wants to exit the game
     */
    public void doExit();
}
