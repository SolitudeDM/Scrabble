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
     * @requires all parameters != null && row and col to be withing the board boundaries
     * @ensures to set a given square to a given type
     */
    public void setSquare(int row, int col, Type type) {
        this.squares[row][col].setType(type);
    }

    /**
     * @param row  is the row index of the square
     * @param col  is the column index of the square
     * @param tile is the tile that will be set on the square
     * @requires parameters != null && row and col to be within the board boundaries
     * @ensures to set a tile on a given square
     */
    public void setTile(int row, int col, Tile tile) {
        this.squares[row][col].setTile(tile);
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

    /**
     * @param square is the square to check
     * @requires square != null
     * @ensures to return true if the square has no tile attached && return false if the square has a tile attached to it
     * @return true if square is indeed empty
     * @return false if square is not empty*/
    public boolean isEmptySquare(Square square) {
        return square.getTile() == null;
    }

    /**
     * Checks whether the whole boad is empty
     * @ensures to return false if any square on the board has a tile && to return true if all squares have no tiles
     * @return true if board is indeed empty
     * @return false if board is not empty*/
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
                newSquares[x][y].setType(squares[x][y].getType());
                newSquares[x][y].setTile(squares[x][y].getTile());
            }
        }
        return new Board(newSquares);
    }

    @Override
    public String toString() {
    String board ="";

        board+=(ANSI.PURPLE);
        board+=("╔═════════════════════");
        board+=(ANSI.PURPLE_BACKGROUND);
        board+=(ANSI.WHITE_BOLD_BRIGHT);
        board+=("Scrabble");
        board+=(ANSI.RESET);
        board+=(ANSI.PURPLE);
        board+=("═════════════════════╗");
        board+= "\n";
        board+=("║");
        board+=(ANSI.PURPLE_BOLD_BRIGHT);
        board+=("    A  B  C  D  E  F  G  H  I  J  K  L  M  N  O  ");

        board+=(ANSI.PURPLE);
        board+=(" ║");
        board+=(ANSI.RESET);

        board+=(ANSI.PURPLE);
        board+="\n";
        for (int i = 0; i < RESOLUTION; i++) {
            if (i + 1 < 10) {
                board+=(ANSI.PURPLE);
                board+=("║");
                board+=(ANSI.RESET);
                board+=(ANSI.PURPLE_BOLD_BRIGHT);
                board+=((i + 1) + "  ");
            } else {
                board+=(ANSI.PURPLE);
                board+=("║");
                board+=(ANSI.RESET);
                board+=(ANSI.PURPLE_BOLD_BRIGHT);
                board+=(i + 1 + " ");
            }

            for (int j = 0; j < RESOLUTION; j++) {
                board+=(ANSI.WHITE);
                if (isEmptySquare(squares[i][j])) {

                    switch (squares[i][j].getType()) {
                        case CENTER:
                            board+=(ANSI.RED_BACKGROUND);
                            board+=(ANSI.WHITE_BOLD_BRIGHT);
                            board+=(squares[i][j].toString());
                            board+=(ANSI.RESET);
                            break;
                        case DOUBLE_LETTER:
                            board+=(ANSI.BLUE_BACKGROUND);
                            board+=(ANSI.WHITE_BOLD_BRIGHT);
                            board+=(squares[i][j].toString());
                            board+=(ANSI.RESET);
                            break;
                        case TRIPLE_LETTER:
                            board+=(ANSI.BLUE_BACKGROUND_BRIGHT);
                            board+=(ANSI.WHITE_BOLD_BRIGHT);
                            board+=(squares[i][j].toString());
                            board+=(ANSI.RESET);
                            break;
                        case DOUBLE_WORD:
                            board+=(ANSI.PURPLE_BACKGROUND);
                            board+=(ANSI.WHITE_BOLD_BRIGHT);
                            board+=(squares[i][j].toString());
                            board+=(ANSI.RESET);
                            break;
                        case TRIPLE_WORD:
                            board+=(ANSI.PURPLE_BACKGROUND_BRIGHT);
                            board+=(ANSI.WHITE_BOLD_BRIGHT);
                            board+=(squares[i][j].toString());
                            board+=(ANSI.RESET);
                            break;
                        default:
                            board+=(ANSI.RESET);
                            board+=(ANSI.WHITE);
                            board+=(squares[i][j].toString());
                            board+=(ANSI.RESET);
                            break;
                    }

                    board+=" ";
                } else {
                    board+=(ANSI.YELLOW_BACKGROUND_BRIGHT);
                    board+=(ANSI.BLACK_BOLD);
                    board+=(squares[i][j].getTile().getLetter());
                    board+=(" ");
                    board+=(ANSI.RESET);
                    board+=(" ");
                }
            }
            board+=(ANSI.PURPLE);
            board+=("  ║");
            board+="\n";
            board+=(ANSI.RESET);
        }
        board+=(ANSI.PURPLE);
        board+=("║  ░▒▓");
        board+=(ANSI.WHITE_BRIGHT);
        board+=(ANSI.PURPLE_BACKGROUND);
        board+=("Made by: Mark Zhitchenko && Dani Mahaini");
        board+=(ANSI.RESET);
        board+=(ANSI.PURPLE);
        board+=("▓▒░  ║");
        board+="\n";
        board+=("╚══════════════╦════════════════════╦══════════════╝");
        board+=(ANSI.RESET);

        return board;
    }

}


