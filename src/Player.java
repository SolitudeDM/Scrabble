import java.util.ArrayList;

/**
 * Abstract class for a player in scrabble game, will later on be subclassed by HumanPlayer and ComputerPlayer*/
public abstract class Player {
    private String name;
    private int score;
    private ArrayList<Tile> hand;

    /**
     * Constructor that creates a player instance
     * @requires name != null
     * @ensures the Name of the player will be the parameter name */
    public Player(String name){
        assert name!= null;
        this.name = name;
    }

    /**
     * Getters and setters for the class variables
     * Note: name only has a getter, because it is a read only property of the class*/
    public String getName(){
        return this.name;
    }
    public int getScore(){
        return this.score;
    }
    public void setScore(int score){
        this.score = score;
    }
    public ArrayList<Tile> getHand() {
        return this.hand;
    }
    public void setHand(ArrayList<Tile> hand) {
        this.hand = hand;
    }

    public abstract String determineMove(Board board);


}
