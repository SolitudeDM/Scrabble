import java.util.ArrayList;

public class Game {

    private Player[] players;

    public Game(Player[] players) {
        this.players = players;
    }

    /**
     * This method creates and fills in a sack with tiles
     * @ensures to return a complete tileSack
     * @return Arraylist of tiles with the right quantity*/
    public ArrayList<Tile> createTileSack() {
        //this is a list of letters without duplicates (quantity not counted yet)
        ArrayList<Tile> tileSack = new ArrayList<>();

        tileSack.add(new Tile('A', 1, 9));
        tileSack.add(new Tile('B', 3, 2));
        tileSack.add(new Tile('C', 3, 2));
        tileSack.add(new Tile('D', 2, 4));
        tileSack.add(new Tile('E', 1, 12));
        tileSack.add(new Tile('F', 4, 2));
        tileSack.add(new Tile('G', 2, 2));
        tileSack.add(new Tile('H', 4, 2));
        tileSack.add(new Tile('I', 1, 8));
        tileSack.add(new Tile('J', 8, 2));
        tileSack.add(new Tile('K', 5, 2));
        tileSack.add(new Tile('L', 1, 4));
        tileSack.add(new Tile('M', 3, 2));
        tileSack.add(new Tile('N', 1, 6));
        tileSack.add(new Tile('O', 1, 8));
        tileSack.add(new Tile('P', 3, 2));
        tileSack.add(new Tile('Q', 10, 1));
        tileSack.add(new Tile('R', 1, 6));
        tileSack.add(new Tile('S', 1, 4));
        tileSack.add(new Tile('T', 1, 6));
        tileSack.add(new Tile('U', 1, 4));
        tileSack.add(new Tile('V', 4, 2));
        tileSack.add(new Tile('W', 4, 2));
        tileSack.add(new Tile('X', 8, 1));
        tileSack.add(new Tile('Y', 4, 2));
        tileSack.add(new Tile('Z', 10, 1));
        tileSack.add(new Tile(' ', 0, 2));

        //this list has all the letters with the right amount of duplicates
        ArrayList<Tile> completeTileSack = new ArrayList<>();

        for(Tile t : tileSack){
            for(int i = 0; i < t.getLetterQuantity(); i++){
                completeTileSack.add(new Tile(t.getLetter(), t.getLetterPoints(), 0));
            }
        }

        return completeTileSack;
    }

    public static void main(String[] args) {
        //Here we checked if the board will be printed properly, using the setBoard() and showBoard() methods
        Square[][] squares = new Square[15][15];
        Board board = new Board(squares);
        board.setBoard();
        board.showBoard();

        /*Here we checked if the tileSack is created properly (commented out because we don't need the createTileSack() method to be static
        made it static only for the testing
         */
        //ArrayList<Tile> test = createTileSack();
        //System.out.println(test);
    }
}
