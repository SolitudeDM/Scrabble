package Model;

/**
 * Represents a tile in the scrabble game
 * @author Dani Mahaini and Mark Zhitchenko*/
public class Tile {

    /**
     * Variables that a tile in scrabble has*/
    private char letter;
    private int letterPoints;
    private int letterQuantity;
    private boolean substituted;

    /**
     * Model.Tile constructor, creates a new tile with the given letter and its points*/
    public Tile(char letter, int letterPoints, int letterQuantity){
        this.letter = letter;
        this.letterPoints = letterPoints;
        this.letterQuantity = letterQuantity;
        this.substituted = false;
    }

    /**
     * getters and setters for the variables of a tile*/
    public void setLetter(char letter){
        this.letter = letter;
    }
    public char getLetter(){
        return this.letter;
    }

    public void setLetterPoints(int letterPoints){
        this.letterPoints = letterPoints;
    }
    public int getLetterPoints(){
        return this.letterPoints;
    }

    public int getLetterQuantity(){
        return this.letterQuantity;
    }

    public boolean isSubstituted() {
        return substituted;
    }
    public void setSubstituted(boolean substituted) {
        this.substituted = substituted;
    }

}
