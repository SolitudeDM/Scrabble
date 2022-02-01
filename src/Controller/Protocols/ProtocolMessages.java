package Controller.Protocols;

/**
 * All of these messages except of the DELIMITER were stated in the documentation of the protocol.
 * The DELIMITER was changed here because a single semicolon ruined the sending of the updated board in our implementation,
 * we couldn't find a good solution to that and keep the original DELIMITER (hopefully this is not a major issue).
 * The commented constants are used for room(not a requirement) and we don't implement it in our project*/
public class ProtocolMessages {

    /** Delimiter used to separate arguments sent over the network.
     * This is the only constant we changed in our project (the reason described above)*/
    public static final String DELIMITER = ";;";

    /** <b> Usage:</b> "c;[player_name]", <b> Example:</b> "c; PlayerA" */
    public static final String CONNECT = "c";

    /** used by the server to confirm connection*/
    public static final String CONFIRM_CONNECT = "cc";

//    /** <b> Usage:</b> "jr;[room_id]", <b> Example:</b> "jr; 101" */
//    public static final String JOIN_ROOM = "jr";

//    /** <b> Usage:</b> "cr;[number_player]", <b> Example:</b> "cr; 2" */
//    public static final String CREATE_ROOM = "cr";

    /**
     * client command to start game*/
    public static final String FORCE_START = "fs";

    /** server response on 'fs' command*/
    public static final String INITIATE_GAME = "ig";

//    /** <b> Usage:</b> "rp;[player_name#player_number]", <b> Example:</b> "rp; PlayerC#3" */
//    public static final String ADD_OR_REMOVE_PLAYER = "rp";

    /** <b> Usage:</b> "m [coordinates] [orientation] [word]", <b> Example:</b> "m A2 H WOOD" */
    public static final String MAKE_MOVE = "m";

    /** server response after client's move (updates the board)*/
    public static final String UPDATE_TABLE = "ut";

    /** used when feedback is sent to the client */
    public static final String FEEDBACK = "f";


    /** <b> Usage:</b> "s"*/
    public static final String SKIP_TURN = "s";


//    /** <b> Usage:</b> "us;[player_name#player_number];[score]", <b> Example:</b> "us;PlayerC#3;10" */
//    public static final String UPDATE_SCORE = "us";

    /** <b> Usage:</b> "r [tile] [tile] [tile]", <b> Example:</b> "r A B C" */
    public static final String REPLACE_TILES = "r";

    /** server response on 'r' command*/
    public static final String GIVE_TILE = "gt";

    /** used by server to show that game is finished*/
    public static final String FINISH_GAME = "x";

    /** <b> Usage:</b> "D"
     * used to finish game urgently*/
    public static final String DISCONNECT = "D";

    /** response to some exceptions*/
    public static final String CUSTOM_EXCEPTION = "!";

    /** response to some commands*/
    public static final String CUSTOM_COMMAND = "/";
}
