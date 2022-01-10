import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Game {

    private ArrayList<Tile> tileSack;
    private Player[] players;
    private Board board;

    private HashMap<Player, List<Tile>> hands;

    public Game(Player[] players, Board board) {
        this.players = players;
        this.board = board;
        tileSack = createTileSack();
        handOut();
    }

    /**
     * This method creates,fills and shuffles a sack with tiles
     * @ensures to return a complete tileSack
     * @return Arraylist of tiles with the right quantity*/
    public static ArrayList<Tile> createTileSack() {
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

        Collections.shuffle(completeTileSack);
        return completeTileSack;
    }

    /**
     * This method hands out tiles for the players
     * @ensures number of entries in HashMap hands to be equal to amount of players and that every player will have 7 tiles*/
    public void handOut(){
        for(Player p : players){
            List<Tile> given = tileSack.subList(0,7);
            tileSack.remove(given);
            hands.put(p, given);
        }
    }

    /**
     * This method return a Tile based on its letter
     * @requires letter != null
     * @param letter is the letter we want to get a Tile for
     * @ensures to return the right Tile
     * @return Tile with required letter
     */
    public Tile getTile(char letter){
        for (int i = 0; i < tileSack.size(); i++){
            if(letter == tileSack.get(i).getLetter()){
                return tileSack.get(i);
            }
        }
        return null;
    }

    /**
     * This method finds players hand and prints all the tiles he has
     * @param player is the player we want to show tiles to
     * @requires player != null
     * @requires player to be included in the players array of the game
     * @ensures to print all the tiles this player has*/
    public void showTiles(Player player){
        assert player != null;
        for(Player p : hands.keySet()){
            if (p.equals(player)){
                for(Tile t : hands.get(p)){
                    System.out.println(t.getLetter());
                }
            }
        }
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
//        ArrayList<Tile> test = createTileSack();
//        System.out.println(test);
    }
}
