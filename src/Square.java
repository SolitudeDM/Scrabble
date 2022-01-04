/**
 * Represents a square on the board in the scrabble game
 * @author Mark Zhitchenko and Dani Mahaini*/
public class Square {

    /**
     * Variables of a square*/
    private int column;
    private int row;
    private Type type;
    private Tile tile;

    /**
     * Square constructor, creates a new square with a given type, index and tile
     * @param type is the type of the square
     * @param column is the column index of the square
     * @param row is the row index of the square*/
    public Square(Type type, int column, int row){
        this.column = column;
        this.row = row;
        this.type = type;
        this.tile = null;
    }

    /**
     * A setter for variable type*/
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * A setter for variable tile*/
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setColumn(int column){
        this.column = column;
    }

    public void setRow(int row){
        this.row = row;
    }
}
