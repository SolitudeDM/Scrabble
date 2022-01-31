package Model;

import Exceptions.SquareNotEmptyException;
import Model.players.HumanPlayer_v3;
import Model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    private Game game;
    private Board board;
    private ArrayList<Player> players;
    private ArrayList<Tile> tileSack;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        Square[][] squares = new Square[15][15];
        board = new Board(squares);
        board.setBoard();

        HumanPlayer_v3 p1 = new HumanPlayer_v3("P1", game);
        HumanPlayer_v3 p2 = new HumanPlayer_v3("P2", game);

        players.add(p1);
        players.add(p2);

        game = new Game(players, board);
        tileSack = Game.createTileSack();
    }

    @Test
    void placeTest() throws SquareNotEmptyException {
        ArrayList<Tile> newHand = new ArrayList<>();
        newHand.add(new Tile('P', 3, 2));
        newHand.add(new Tile('I', 1, 8));

        Move move1 = new Move(game, players.get(0));
        players.get(0).setGame(game);
        players.get(0).setHand(newHand);
        move1.place("G8", false, "PI", board);
        assertEquals('P', board.getSquare(7,6).getTile().getLetter());
        assertEquals('I', board.getSquare(7, 7).getTile().getLetter());
        assertEquals(8, move1.getScore());

        ArrayList<Tile> newHand2 = new ArrayList<>();
        newHand2.add(new Tile('B', 3, 2));
        newHand2.add(new Tile('E', 1, 12));

        Move move2 = new Move(game, players.get(1));
        players.get(1).setGame(game);
        players.get(1).setHand(newHand2);
        move2.place("I7", true, "BE", board);
        assertEquals('B', board.getSquare(6,8).getTile().getLetter());
        assertEquals('E', board.getSquare(7, 8).getTile().getLetter());
        assertEquals(12, move2.getScore());


    }

    @Test
    void swap() {
        //check for skip turn
        Player currentPlayer = players.get(0);
        Move move3 = new Move(game, players.get(0));
        players.get(0).setGame(game);
        players.get(0).setMove(move3);
        move3.swap(" ");
        if(players.get(0).getMove().isMoveMade()){
            currentPlayer = players.get(1);
        }

        assertEquals(players.get(1), currentPlayer);

        //check for replacing tiles
        ArrayList<Tile> newHand3 = new ArrayList<>();

        Tile t1 = new Tile('P', 3, 2);
        Tile t2 = new Tile('I', 1, 8);

        newHand3.add(t1);
        newHand3.add(t2);

        Move move4 = new Move(game, players.get(1));
        players.get(1).setGame(game);
        players.get(1).setMove(move4);
        players.get(1).setHand(newHand3);
        move4.swap("P I");

        //the old tiles are not there anymore
        assertFalse(players.get(1).getHand().contains(t1));
        assertFalse(players.get(1).getHand().contains(t2));

        //hand has still the same size (so new tiles were given)
        assertEquals(2, players.get(1).getHand().size());
    }
}