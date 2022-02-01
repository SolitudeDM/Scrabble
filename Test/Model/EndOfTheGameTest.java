package Model;

import Model.players.HumanPlayer;
import Model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/** This test class tests whether the game ends when an end-game condition is met*/
public class EndOfTheGameTest {

    private Board board;
    private Square[][] squares;

    @BeforeEach
    public void setup(){
        squares = new Square[15][15];
        board = new Board(squares);
        board.setBoard();
    }

    /**
     * Test whether the game finishes when the tileSack is empty and when one of the player's hands is empty*/
    @Test
    public void outOfTilesTest(){

        ArrayList<Player> players = new ArrayList<>();

        Game game = new Game(players, board);

        HumanPlayer player1 = new HumanPlayer("Dummy 1", game);
        HumanPlayer player2 = new HumanPlayer("Dummy 2", game);

        game.setPlayers(players);

        players.add(player1);
        players.add(player2);

        game.handOut();


        player1.getHand().removeAll(player1.getHand());
        assertFalse(game.isFinished());

        player1.getHand().removeAll(player1.getHand());
        player2.getHand().removeAll(player1.getHand());
        assertFalse(game.isFinished());

        //both player's hands are empty and tileSack is empty
        player1.getHand().removeAll(player1.getHand());
        player2.getHand().removeAll(player1.getHand());
        game.getTileSack().removeAll(game.getTileSack());
        assertTrue(game.isFinished());

        //one player's hand is empty and tileSack is empty
        player2.getHand().removeAll(player1.getHand());
        game.getTileSack().removeAll(game.getTileSack());
        assertTrue(game.isFinished());

    }

    /**
     * Test whether the game finishes if all the players skipped two times in a row*/
    @Test
    public void skipTest(){

        ArrayList<Player> players = new ArrayList<>();

        Game game = new Game(players, board);

        HumanPlayer player1 = new HumanPlayer("Dummy 1", game);
        HumanPlayer player2 = new HumanPlayer("Dummy 2", game);

        game.setPlayers(players);

        players.add(player1);
        players.add(player2);

        game.getPlayers().get(0).setSkips(1);
        game.getPlayers().get(1).setSkips(1);
        assertFalse(game.isFinished());

        //test if game finishes when both players skipped more than twice
        game.getPlayers().get(0).setSkips(3);
        game.getPlayers().get(1).setSkips(3);
        assertTrue(game.isFinished());

        //test if game finishes when both players skipped twice
        game.getPlayers().get(0).setSkips(2);
        game.getPlayers().get(1).setSkips(2);
        assertTrue(game.isFinished());
    }

    /**
     * test if game finishes when there are no empty squares left */
    @Test
    public void outOfSpace(){

        ArrayList<Player> players = new ArrayList<>();

        Game game = new Game(players, board);

        HumanPlayer player1 = new HumanPlayer("Dummy 1", game);
        HumanPlayer player2 = new HumanPlayer("Dummy 2", game);

        game.setPlayers(players);

        players.add(player1);
        players.add(player2);


        Tile dummyTile = new Tile('@', 0, 225);

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                squares[i][j].setTile(dummyTile);
            }
        }
        assertTrue(game.isFinished());

        squares[8][8].setTile(null);
        assertFalse(game.isFinished());

    }

}
