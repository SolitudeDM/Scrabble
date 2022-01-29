package Controller.Protocols;

public class ProtocolMessages {
    /** Delimiter used to separate arguments sent over the network. */
    public static final String DELIMITER = ";;";

    /** <b> Usage:</b> "c;[player_name]", <b> Example:</b> "c; PlayerA" */
    public static final String CONNECT = "c";

    public static final String CONFIRM_CONNECT = "cc";

//    /** <b> Usage:</b> "jr;[room_id]", <b> Example:</b> "jr; 101" */
//    public static final String JOIN_ROOM = "jr";

//    /** <b> Usage:</b> "cr;[number_player]", <b> Example:</b> "cr; 2" */
//    public static final String CREATE_ROOM = "cr";

    /**
     * client command to start game*/
    public static final String FORCE_START = "fs";

    /** <b> Usage:</b> "ig;[table];[player_name#player_number],[player_name#player_number],...", <b> Example:</b> "ig; , ,t, // , ,e, //t,e,s,t// , ,t, ;PlayerA#1,PlayerB#2"
     * server response on 'fs' command*/
    public static final String INITIATE_GAME = "ig";

//    /** <b> Usage:</b> "rp;[player_name#player_number]", <b> Example:</b> "rp; PlayerC#3" */
//    public static final String ADD_OR_REMOVE_PLAYER = "rp";

    /** <b> Usage:</b> "m;[alignment];[coordinates],[word]", <b> Example:</b> "m;A2;H;wood" */
    public static final String MAKE_MOVE = "m";

    /** <b> Usage:</b> "ut;[table]", <b> Example:</b> "ut; , ,t, // , ,e, //t,e,s,t// , ,t, "
     * server response after client's move (updates the board)*/
    public static final String UPDATE_TABLE = "ut";

    /** <b> Usage:</b> "f;[accepted]", <b> Example:</b> "f; true"*/
    public static final String FEEDBACK = "f";


    /** <b> Usage:</b> "s"*/
    public static final String SKIP_TURN = "s";


//    /** <b> Usage:</b> "us;[player_name#player_number];[score]", <b> Example:</b> "us;PlayerC#3;10" */
//    public static final String UPDATE_SCORE = "us";

    /** <b> Usage:</b> "SWAP; [tile] [tile] [tile]", <b> Example:</b> "r; A B C" */
    public static final String REPLACE_TILES = "r";

    /** <b> Usage:</b> "gt;[tile][tile]", <b> Example:</b> "gt;ABC"
     * server response on 'r' command*/
    public static final String GIVE_TILE = "gt";

    /** <b> Usage:</b> "EXIT" */
    public static final String FINISH_GAME = "x";

    /**
     * server response to some exceptions*/
    public static final String CUSTOM_EXCEPTION = "!";
}
