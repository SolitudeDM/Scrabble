public class Board {

    public static final int RESOLUTION = 15;

    private Square[][] squares;

    public Board(Square[][] squares) {
        this.squares = squares;
    }

    public void setSquare(int row, int col, Square.Type type) {


    /**
     * @requires the required square to be within the bounds
     * @param row
     * @param col
     * @return the required square
     */
    public Square getSquare(int row, int col) {
        assert ((row <= RESOLUTION) && (col <= RESOLUTION) && (row > 0) && (col > 0));
        return this.squares[row][col];
    }


}
