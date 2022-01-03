public class Tile {

    private char letter;
    private int letterPoints;

    public Tile(char letter, int letterPoints){
        this.letter = letter;
        this.letterPoints = letterPoints;
    }

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
