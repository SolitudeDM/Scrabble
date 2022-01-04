public class Game {
    /**
     * Here we checked if the board will be printed properly, using the setBoard() and showBoard() methods*/
    public static void main(String[] args) {
        Square[][] squares = new Square[15][15];
        Board board = new Board(squares);
        board.setBoard();
        board.showBoard();
    }
}
