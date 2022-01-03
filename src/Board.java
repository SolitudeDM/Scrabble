/**
 * Board for the scrabble game
 * @author Mark Zhitchenko and Dani Mahaini*/
public class Board {

    /**
     * Resolution of our board*/
    public static final int RESOLUTION = 15;

    /**
     * squares is our two-dimensional array
     * @invariant there are always RESOLUTION * RESOLUTION squares on a board*/
    private Square[][] squares;

    /**
     * Board constructor creates a new board with a two-dimensional array of squares*/
    public Board(Square[][] squares) {
        this.squares = squares;
    }

    /**
     * @ensures to set a given square to a given type
     * @param row is the row index
     * @param col is the column index
     * @param type is the type that a square will be set to*/
    public void setSquare(int row, int col, Type type) {
        this.squares[row][col].setType(type);
    }

    /**
     * @ensures to set a tile on a given square
     * @param row is the row index of the square
     * @param col is the column index of the square
     * @param tile is the tile that will be set on the square*/
    public void setTile(int row, int col, Tile tile) {
        this.squares[row][col].setTile(tile);
    }

    /**
     * @requires the required square to be within the bounds
     * @param row is the row index
     * @param col is the column index
     * @ensures to return a square with the given indexes
     * @return the required square
     */
    public Square getSquare(int row, int col) {
        assert ((row <= RESOLUTION) && (col <= RESOLUTION) && (row > 0) && (col > 0));
        return this.squares[row][col];
    }


}
