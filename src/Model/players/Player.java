package Model.players;

import Model.Board;
import Model.Game;
import Model.Move;
import Model.Tile;

import java.util.ArrayList;

/**
 * Abstract class for a player in scrabble game, will later on be subclassed by Model.players.HumanPlayer and ComputerPlayer*/
public abstract class Player {
    private String name;
    private int score;
    private ArrayList<Tile> hand;
    private Game game;
    private Move move;
    /**
     * Constructor that creates a player instance
     * @requires name != null
     * @ensures the Name of the player will be the parameter name */
    public Player(String name, Game game){
        assert name!= null;
        this.name = name;
        this.game = game;
        this.hand = new ArrayList<Tile>();
        this.score = 0;
//        this.move = new Move(this.getGame(), this);
        this.move = null;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
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

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }


    public abstract String determineMove(Board board);

    /**
     * This method checks whether a player has the tiles he wanted to place and adapts his hand if possible
     * @requires lettersUsed != null && lettersUsed.size() > 0
     * @ensures to return true if the player has all the tiles he wants to place && to remove all used tiles from his hand
     * @param lettersUsed is the ArrayList with Tiles containing Tiles the player used*/
    public boolean searchHand(ArrayList<Tile> lettersUsed) {
        //Add copy of hand tiles
        ArrayList<Tile> handDupe = new ArrayList<>(getHand());
        boolean letterFound = false;
        //Tile variable that will be used for checking if blank tiles were used
        Tile tileNotFound;

        for (int i = 0; i < lettersUsed.size(); i++) {
            for (int j = 0; j < handDupe.size(); j++) {
                if ((handDupe.get(j).getLetter()) == (lettersUsed.get(i).getLetter())) {
                    handDupe.remove(j);
                    letterFound = true;
                    break;
                }else{
                    letterFound = false;
                }
            }
            //check for blank tiles
            if (!letterFound) {
                tileNotFound = lettersUsed.get(i);
                for(int r = 0; r < handDupe.size(); r++) {
                    if(handDupe.get(r).getLetter() == game.getTile(' ').getLetter()){
                        handDupe.get(r).setLetter(tileNotFound.getLetter());
                        this.score -= tileNotFound.getLetterPoints();
                        handDupe.remove(r);
                        letterFound = true;
                        //this break is needed in case a player has two blank tiles we change and use only one
                        break;
                    }
                    else{
                        letterFound = false;
                    }
                }
            }
            //check again after the blank tiles analysis
            if(!letterFound){
                return false;
            }
        }
        setHand(handDupe);

        return true;
    }

}
