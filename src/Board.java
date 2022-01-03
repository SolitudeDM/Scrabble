public class Board {

    public static final int RESOLUTION = 15;

    private Square[][] squares;

    public Board(Square[][] squares) {
        this.squares = squares;
    }

    public void setSqare(int row, int col, Type type) {
        this.squares[row][col].setType(type);
    }

    public void setTile(int row, int col, Tile tile) {
        this.squares[row][col].setTile(tile);
    }


}
