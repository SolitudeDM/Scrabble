package Model;

import Model.players.HumanPlayer_v3;
import Model.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EndOfTheGameTest {

    private Board board;
    private Square[][] squares;

    @BeforeEach
    public void setup(){
        squares = new Square[15][15];
        board = new Board(squares);
        board.setBoard();
    }

    @Test
    public void outOfTilesTest(){

        ArrayList<Player> players = new ArrayList<>();

        Game game = new Game(players, board);

        HumanPlayer_v3 player1 = new HumanPlayer_v3("Dummy 1", game);
        HumanPlayer_v3 player2 = new HumanPlayer_v3("Dummy 2", game);

        game.setPlayers(players);

        players.add(player1);
        players.add(player2);


        ArrayList<Tile> tiles = new ArrayList<>();

        player1.getHand().removeAll(player1.getHand());
        assertFalse(game.isFinished());

        player1.getHand().removeAll(player1.getHand());
        player2.getHand().removeAll(player1.getHand());
        assertFalse(game.isFinished());

        player1.getHand().removeAll(player1.getHand());
        player2.getHand().removeAll(player1.getHand());
        game.getTileSack().removeAll(game.getTileSack());
        assert(game.isFinished());

    }

    @Test
    public void skipTest(){

        ArrayList<Player> players = new ArrayList<>();

        Game game = new Game(players, board);

        HumanPlayer_v3 player1 = new HumanPlayer_v3("Dummy 1", game);
        HumanPlayer_v3 player2 = new HumanPlayer_v3("Dummy 2", game);

        game.setPlayers(players);

        players.add(player1);
        players.add(player2);

        game.getPlayers().get(0).setSkips(1);
        game.getPlayers().get(1).setSkips(1);
        assertFalse(game.isFinished());

        game.getPlayers().get(0).setSkips(3);
        game.getPlayers().get(1).setSkips(3);
        assert(game.isFinished());

        game.getPlayers().get(0).setSkips(2);
        game.getPlayers().get(1).setSkips(2);
        assert(game.isFinished());
    }

    @Test
    public void outOfSpace(){

        ArrayList<Player> players = new ArrayList<>();

        Game game = new Game(players, board);

        HumanPlayer_v3 player1 = new HumanPlayer_v3("Dummy 1", game);
        HumanPlayer_v3 player2 = new HumanPlayer_v3("Dummy 2", game);

        game.setPlayers(players);

        players.add(player1);
        players.add(player2);


        Tile dummyTile = new Tile('@', 0, 225);

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                squares[i][j].setTile(dummyTile);
            }
        }
        assert(game.isFinished());

        squares[8][8].setTile(null);
        assertFalse(game.isFinished());

    }

}
