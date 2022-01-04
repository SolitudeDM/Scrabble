public class Game {
    public static void main(String[] args) {
        Square[][] squares = new Square[15][15];
        Board board = new Board(squares);
        board.setBoard();
        board.showBoard();
    }
}
