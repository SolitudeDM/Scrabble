package Controller.Protocols;

public interface ClientProtocol {
    /**
     * Called when the player join a room
     * @param room_id
     * @return an initialization of the room (table, players that are already in the game);
     */
    public String doJoinRoom(String room_id);

    /**
     * Called when a new room is being created
     * @return a room id that the person just created
     */
    public String doCreateRoom(String player_number);

    /**
     * Called when the player does a move
     * @param alignment
     * @param coordinates
     * @param word
     * @return if move has been accepted
     */
    public String doMove(String alignment, String coordinates, String word);

    /**
     * Called when a player want to skip his turn
     * @return if skipping is accepted
     */
    public String doSkip();

    /**
     * Called when a player want to replace some tiles
     * @param tiles - tiles to be replaced
     * @return new tiles to be sent to the client
     */
    public String doReplaceTiles(String tiles);

    /**
     * Called when a player disconnects from the server
     */
    public void doDisconnect();
}
