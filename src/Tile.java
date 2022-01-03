/**
 * Represents a tile in the scrabble game
 * @author Dani Mahaini and Mark Zhitchenko*/
public class Tile {

    /**
     * Variables that a tile in scrabble has*/
    private char letter;
    private int letterPoints;

    /**
     * Tile constructor, creates a new tile with the given letter and its points*/
    public Tile(char letter, int letterPoints){
        this.letter = letter;
        this.letterPoints = letterPoints;
    }

    /**
     * getters and setters for the two variables of a tile*/
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
}
