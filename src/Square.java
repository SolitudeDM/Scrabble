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
     * @param row is the row index of the square
     * @param tile is the tile on the square(initially empty)*/
    //we need to make the square initially empty, because we just created the square, do we even need to include tile in the constructor???
    public Square(Type type, int column, int row, Tile tile){
        this.column = column;
        this.row = row;
        this.type = type;
        this.tile = tile;
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
}
