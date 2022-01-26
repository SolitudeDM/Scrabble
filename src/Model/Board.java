package Model;

import View.utils.ANSI;

/**
 * Board for the scrabble game
 * @author Mark Zhitchenko and Dani Mahaini*/
public class Board {

    /**
     * Resolution of our board
     */
    public static final int RESOLUTION = 15;

    /**
     * squares is our two-dimensional array
     *
     * @invariant there are always RESOLUTION * RESOLUTION squares on a board
     */
    private Square[][] squares;
    /**
     * This two-dimensional array represents the pattern of squares on the board
     */
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
     * Model.Board constructor creates a new board with a two-dimensional array of squares
     */
    public Board(Square[][] squares) {
        this.squares = squares;
    }

    /**
     * This method sets the board with the required squares on the right places, using the pattern array
     */
    public void setBoard() {
        for (int i = 0; i < RESOLUTION; i++) {
            for (int j = 0; j < RESOLUTION; j++) {
                squares[i][j] = new Square(j, i);
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
                    setSquare(i, j, Type.TRIPLE_LETTER);
                }
                if (pattern[i][j].equals("2W")) {
                    setSquare(i, j, Type.DOUBLE_WORD);
                }
                if (pattern[i][j].equals("3W")) {
                    setSquare(i, j, Type.TRIPLE_WORD);
                }
            }
        }
    }

    public Square[][] getSquares() {
        return squares;
    }

    /**
     * This method prints the board to the console
     */
    public void showBoard() {
        System.out.print(ANSI.PURPLE);
        System.out.print("╔═════════════════════");
        System.out.print(ANSI.PURPLE_BACKGROUND);
        System.out.print(ANSI.WHITE_BOLD_BRIGHT);
        System.out.print("Scrabble");
        System.out.print(ANSI.RESET);
        System.out.print(ANSI.PURPLE);
        System.out.print("═════════════════════╗");
        System.out.println();
        System.out.print("║");
        System.out.print(ANSI.PURPLE_BOLD_BRIGHT);
        System.out.print("    A  B  C  D  E  F  G  H  I  J  K  L  M  N  O  ");

        System.out.print(ANSI.PURPLE);
        System.out.print(" ║");
        System.out.print(ANSI.RESET);

        System.out.print(ANSI.PURPLE);
        System.out.println();
        for (int i = 0; i < RESOLUTION; i++) {
            if (i + 1 < 10) {
                System.out.print(ANSI.PURPLE);
                System.out.print("║");
                System.out.print(ANSI.RESET);
                System.out.print(ANSI.PURPLE_BOLD_BRIGHT);
                System.out.print((i + 1) + "  ");
            } else {
                System.out.print(ANSI.PURPLE);
                System.out.print("║");
                System.out.print(ANSI.RESET);
                System.out.print(ANSI.PURPLE_BOLD_BRIGHT);
                System.out.print(i + 1 + " ");
            }

            for (int j = 0; j < RESOLUTION; j++) {
                System.out.print(ANSI.WHITE);
                if (isEmptySquare(squares[i][j])) {

                    switch (squares[i][j].getType()) {
                        case CENTER:
                            System.out.print(ANSI.RED_BACKGROUND);
                            System.out.print(ANSI.WHITE_BOLD_BRIGHT);
                            System.out.print(squares[i][j].toString());
                            System.out.print(ANSI.RESET);
                            break;
                        case DOUBLE_LETTER:
                            System.out.print(ANSI.BLUE_BACKGROUND);
                            System.out.print(ANSI.WHITE_BOLD_BRIGHT);
                            System.out.print(squares[i][j].toString());
                            System.out.print(ANSI.RESET);
                            break;
                        case TRIPLE_LETTER:
                            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
                            System.out.print(ANSI.WHITE_BOLD_BRIGHT);
                            System.out.print(squares[i][j].toString());
                            System.out.print(ANSI.RESET);
                            break;
                        case DOUBLE_WORD:
                            System.out.print(ANSI.PURPLE_BACKGROUND);
                            System.out.print(ANSI.WHITE_BOLD_BRIGHT);
                            System.out.print(squares[i][j].toString());
                            System.out.print(ANSI.RESET);
                            break;
                        case TRIPLE_WORD:
                            System.out.print(ANSI.PURPLE_BACKGROUND_BRIGHT);
                            System.out.print(ANSI.WHITE_BOLD_BRIGHT);
                            System.out.print(squares[i][j].toString());
                            System.out.print(ANSI.RESET);
                            break;
                        default:
                            System.out.print(ANSI.RESET);
                            System.out.print(ANSI.WHITE);
                            System.out.print(squares[i][j].toString());
                            System.out.print(ANSI.RESET);
                            break;
                    }

//                    System.out.print(squares[i][j].toString());
//                    System.out.print(ANSI.RESET);
                    System.out.print(" ");
                } else {
                    System.out.print(ANSI.YELLOW_BACKGROUND_BRIGHT);
                    System.out.print(ANSI.BLACK_BOLD);
                    System.out.print(squares[i][j].getTile().getLetter());
                    System.out.print(" ");
                    System.out.print(ANSI.RESET);
                    System.out.print(" ");
                }
            }
            System.out.print(ANSI.PURPLE);
            System.out.println("  ║");
            System.out.print(ANSI.RESET);
        }
        System.out.print(ANSI.PURPLE);
//        System.out.println();
        System.out.print("║  ░▒▓");
        System.out.print(ANSI.WHITE_BRIGHT);
        System.out.print(ANSI.PURPLE_BACKGROUND);
        System.out.print("Made by: Mark Zhitchenko && Dani Mahaini");
        System.out.print(ANSI.RESET);
        System.out.print(ANSI.PURPLE);
        System.out.println("▓▒░  ║");
        System.out.println("╚══════════════╦════════════════════╦══════════════╝");
        System.out.print(ANSI.RESET);
    }

    /**
     * @param row  is the row index
     * @param col  is the column index
     * @param type is the type that a square will be set to
     * @ensures to set a given square to a given type
     */
    public void setSquare(int row, int col, Type type) {
        this.squares[row][col].setRow(row);
        this.squares[row][col].setColumn(col);
        this.squares[row][col].setType(type);
    }

    /**
     * @param row  is the row index of the square
     * @param col  is the column index of the square
     * @param tile is the tile that will be set on the square
     * @ensures to set a tile on a given square
     */
    public void setTile(int row, int col, Tile tile) {
        this.squares[row][col].setTile(tile);
//        showBoard();
    }

    /**
     * @param row is the row index
     * @param col is the column index
     * @return the required square
     * @requires the required square to be within the bounds
     * @ensures to return a square with the given indexes
     */
    public Square getSquare(int row, int col) {
        assert ((row < RESOLUTION) && (col < RESOLUTION) && (row >= 0) && (col >= 0));
        return this.squares[row][col];
    }

    public boolean isEmptySquare(Square square) {
        return square.getTile() == null;
    }

    public boolean isEmpty() {

        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                if (!isEmptySquare(squares[row][col])) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board clone() {
        Square[][] newSquares = new Square[RESOLUTION][RESOLUTION];

        for (int x = 0; x < RESOLUTION; x++) {
            for (int y = 0; y < RESOLUTION; y++) {
                newSquares[x][y] = squares[x][y].clone();
                newSquares[x][y].setTile(squares[x][y].getTile());
            }
        }
        return new Board(newSquares);
    }

}


