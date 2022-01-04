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
    private String[][] pattern = {
            {"3W", "NO", "NO", "2L", "NO", "NO", "NO", "3W", "NO", "NO", "NO", "2L", "NO", "NO", "3W"},
            {"NO", "2W", "NO", "NO", "NO", "3L", "NO", "NO", "NO", "3L", "NO", "NO", "NO", "2W", "NO"},
            {"NO", "NO", "2W", "NO", "NO", "NO", "2L", "NO", "2L", "NO", "NO", "NO", "2W", "NO", "NO"},
            {"2L", "NO", "NO", "2W", "NO", "NO", "NO", "2L", "NO", "NO", "NO", "2W", "NO", "NO", "2L"},
            {"NO", "NO", "NO", "NO", "2W", "NO", "NO", "NO", "NO", "NO", "2W", "NO", "NO", "NO", "NO"},
            {"NO", "3L", "NO", "NO", "NO", "3L", "NO", "NO", "NO", "3L", "NO", "NO", "NO", "3L", "NO"},
            {"NO", "NO", "2L", "NO", "NO", "NO", "2L", "NO", "2L", "NO", "NO", "NO", "2L", "NO", "NO"},
            {"3W", "NO", "NO", "2L", "NO", "NO", "NO", "CE", "NO", "NO", "NO", "2L", "NO", "NO", "3W"},
            {"NO", "NO", "2L", "NO", "NO", "NO", "2L", "NO", "2L", "NO", "NO", "NO", "2L", "NO", "NO"},
            {"NO", "3L", "NO", "NO", "NO", "3L", "NO", "NO", "NO", "3L", "NO", "NO", "NO", "3L", "NO"},
            {"NO", "NO", "NO", "NO", "2W", "NO", "NO", "NO", "NO", "NO", "2W", "NO", "NO", "NO", "NO"},
            {"2L", "NO", "NO", "2W", "NO", "NO", "NO", "2L", "NO", "NO", "NO", "2W", "NO", "NO", "2L"},
            {"NO", "NO", "2W", "NO", "NO", "NO", "2L", "NO", "2L", "NO", "NO", "NO", "2W", "NO", "NO"},
            {"NO", "2W", "NO", "NO", "NO", "3L", "NO", "NO", "NO", "3L", "NO", "NO", "NO", "2W", "NO"},
            {"3W", "NO", "NO", "2L", "NO", "NO", "NO", "3W", "NO", "NO", "NO", "2L", "NO", "NO", "3W"},
     };



    /**
     * Board constructor creates a new board with a two-dimensional array of squares*/
    public Board(Square[][] squares) {
        this.squares = squares;
    }

    public void SetBoard() {
        for (int i = 1; i <= RESOLUTION; i++) {
            for (int j = 1; j <= RESOLUTION; j++) {

                if (pattern[i][j].equals("NO")) {
                    squares[i][j].
                }
                if (pattern[i][j].equals("CE")) {

                }
                if (pattern[i][j].equals("2L")) {

                }
                if (pattern[i][j].equals("3L")) {

                }
                if (pattern[i][j].equals("2W")) {

                }
                if (pattern[i][j].equals("3W")) {

                }

            }
        }
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
