package Model;

import Model.players.HumanPlayer_v3;
import Model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameMethodsTest {
    private Game game;
    private Board board;
    private ArrayList<Player> players;
    private ArrayList<Tile> tileSack;

    @BeforeEach
    void setUp(){
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
    void createTileSackTest() {
        assertEquals(100, tileSack.size());
    }

    @Test
    void handOutTest() {
        game.handOut();

        assertEquals(7, players.get(0).getHand().size());
        assertEquals(7, players.get(1).getHand().size());
    }

    @Test
    void getTileTest() {
        assertEquals('A', game.getTile('A').getLetter());
        assertEquals('B', game.getTile('B').getLetter());
        assertEquals('Z', game.getTile('Z').getLetter());
        assertEquals(' ', game.getTile(' ').getLetter());
        assertEquals('Q', game.getTile('Q').getLetter());
    }
}