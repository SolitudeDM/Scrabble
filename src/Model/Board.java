package Model;

/**
 * Model.Board for the scrabble game
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
     * This two-dimensional array represents the pattern of squares on the board*/
    //add a custom pattern later
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
     * Model.Board constructor creates a new board with a two-dimensional array of squares*/
    public Board(Square[][] squares) {
        this.squares = squares;
    }

    /**
     * This method sets the board with the required squares on the right places, using the pattern array*/
    public void setBoard() {
        for (int i = 0; i < RESOLUTION; i++) {
            for (int j = 0; j < RESOLUTION; j++) {
                    squares[i][j] = new Square(j,i);
                if (pattern[i][j].equals("NO")) {
                    setSquare(i, j, Type.NORMAL);
                }
                if (pattern[i][j].equals("CE")) {
                    setSquare(i, j, Type.CENTER);
                }
                if (pattern[i][j].equals("2L")) {
                    setSquare(i, j, Type.DOUBLE_LETTER);
                }
                if (pattern[i][j].equals("3L")) {
                    setSquare(i, j, Type.TRIPPLE_LETTER);
                }
                if (pattern[i][j].equals("2W")) {
                    setSquare(i, j, Type.DOUBLE_WORD);
                }
                if (pattern[i][j].equals("3W")) {
                    setSquare(i, j, Type.TRIPPLE_WORD);
                }
            }
        }
    }

    /**
     * This method prints the board to the console*/
    public void showBoard() {
            System.out.println("   A  B  C  D  E  F  G  H  I  J  K  L  M  N  O");
        for (int i = 0; i < RESOLUTION; i++) {
            if (i + 1 < 10) {
                System.out.print(i + 1 + "  ");
            } else {
                System.out.print(i + 1 + " ");
            }
            for (int j = 0; j < RESOLUTION; j++) {

                if (isEmptySquare(squares[i][j])) {
                    System.out.print(squares[i][j].toString());
                    System.out.print(" ");
                } else {
                    System.out.print(squares[i][j].getTile().getLetter());
                    System.out.print(" ");
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    /**
     * @ensures to set a given square to a given type
     * @param row is the row index
     * @param col is the column index
     * @param type is the type that a square will be set to*/
    public void setSquare(int row, int col, Type type) {
        this.squares[row][col].setRow(row);
        this.squares[row][col].setColumn(col);
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
        assert ((row < RESOLUTION) && (col < RESOLUTION) && (row >= 0) && (col >= 0));
        return this.squares[row][col];
    }

    public boolean isEmptySquare(Square square){
        return square.getTile() == null;
    }


}
