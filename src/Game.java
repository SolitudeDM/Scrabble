import java.util.ArrayList;

public class Game {

    public ArrayList<Tile> tileSack = new ArrayList<Tile>();

    public void fillSack() {
        tileSack.add(new Tile())
    }

    /**
     * Here we checked if the board will be printed properly, using the setBoard() and showBoard() methods*/
    public static void main(String[] args) {
        Square[][] squares = new Square[15][15];
        Board board = new Board(squares);
        board.setBoard();
        board.showBoard();
    }
}
