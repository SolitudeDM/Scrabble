//package Controller.Protocols;
//
//import java.net.Socket;
//
//public interface ServerProtocol {
//    /**
//     * Called when new connection is established
//     * @ensures it create a new ClientHandler
//     */
//    public void register(String client_id, Socket socket);
//
//    /**
//     * Called when a new room is being created
//     * @return a room id that the person just created
//     */
//    public String doCreateRoom(String player_number) ;
//
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
//}
