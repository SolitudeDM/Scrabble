package Model;

import Model.players.HumanPlayer;
import Model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
/** This test class test whether the correct player is the winner
 * @author Dani Mahaini & Mark Zhitchenko */
class DetermineWinnerTest {
    private Game game;
    private Board board;
    private Square[][] squares;
    private ArrayList<Player> players;
    private Player p1;
    private Player p2;

    @BeforeEach
    void setUp() {
        squares = new Square[15][15];
        board = new Board(squares);
        board.setBoard();

        players = new ArrayList<>();
        p1 = new HumanPlayer("P1", game);
        p2 = new HumanPlayer("P2", game);
        players.add(p1);
        players.add(p2);

        game = new Game(players, board);
    }

    /** Here we test if both player's hands and tileSack are empty, so we expect the player with most score to be the winner*/
    @Test
    public void emptyHandsTest(){
        ArrayList<Tile> empty = new ArrayList<>();
        p1.setHand(empty);
        p2.setHand(empty);

        p2.setScore(5);
        p1.setScore(2);

        game.setFinishGame(true);

        assertEquals("The winner is: " + p2.getName() +  ". His score: " + p2.getScore(), game.determineWinner());
    }

    /** Here we test if the scores were changed correctly in the determineWinner() method*/
    @Test
    public void scoreChangesTest(){
        Tile t1 = new Tile('A', 1, 9);
        Tile t2 = new Tile('B', 3, 2);
        Tile t3 = new Tile('Z', 10, 2);

        ArrayList<Tile> hand1 = new ArrayList<>();
        ArrayList<Tile> hand2 = new ArrayList<>();

        hand1.add(t1);
        hand1.add(t2);
        hand1.add(t3);


        p1.setHand(hand1);
        p2.setHand(hand2);

        p1.setScore(5);
        p2.setScore(5);

        game.setFinishGame(true);

        game.determineWinner();

        //check if the scores were changed correctly as expected, we expect that player 2 will win
        assertEquals(0, p1.getScore());
        assertEquals(19, p2.getScore());
    }

    /** Here we check if the tie situation is handled as expected
     * In case of a tie after all manipulations with tiles, we will check who had the highest score before the subtraction of tiles left*/
    @Test
    public void tieSituationTest(){
        Tile t1 = new Tile('A', 1, 9);
        Tile t2 = new Tile('B', 3, 2);
        Tile t3 = new Tile('Z', 10, 2);
        Tile t4 = new Tile('E', 1, 12);

        ArrayList<Tile> hand1 = new ArrayList<>();
        hand1.add(t1);
        hand1.add(t2);
        hand1.add(t3);

        ArrayList<Tile> hand2 = new ArrayList<>();
        hand2.add(t1);
        hand2.add(t2);
        hand2.add(t3);
        hand2.add(t4);

        p1.setHand(hand1);
        p2.setHand(hand2);

        p1.setScore(5);
        p2.setScore(6);

        game.setFinishGame(true);

        game.determineWinner();

    }
}