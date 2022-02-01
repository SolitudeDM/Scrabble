package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This Test tests the Board class
 * @author Dani Mahaini & Mark Zhitchenko*/
class BoardTest {

    private Board board;
    private Square[][] squares;

    @BeforeEach
    public void setup(){
        squares = new Square[15][15];
        board = new Board(squares);
        board.setBoard();
    }

    /**
     * Test of the setBoard() method*/
    @Test
    public void setBoardTest(){
        //we already invoked setBoard() in the setup() method, so we do not call it again in this test

        assertEquals(Type.CENTER, board.getSquare(7,7).getType());
        assertEquals(Type.TRIPLE_WORD, board.getSquare(0,0).getType());
        assertEquals(Type.DOUBLE_WORD, board.getSquare(1,1).getType());
        assertEquals(Type.DOUBLE_LETTER, board.getSquare(3,0).getType());
        assertEquals(Type.TRIPLE_LETTER, board.getSquare(5,13).getType());
        assertEquals(Type.NORMAL, board.getSquare(14,6).getType());
        assertEquals(Type.DOUBLE_LETTER, board.getSquare(8,6).getType());
    }

    /**
     * Test of the setSquare() method*/
    @Test
    public void setSquareTest(){
        board.setSquare(1, 1, Type.DOUBLE_LETTER);
        board.setSquare(3, 3, Type.NORMAL);
        board.setSquare(1, 5, Type.DOUBLE_WORD);
        board.setSquare(10, 14, Type.TRIPLE_WORD);

        assertEquals(Type.DOUBLE_LETTER, board.getSquare(1,1).getType());
        assertEquals(Type.NORMAL, board.getSquare(3,3).getType());
        assertEquals(Type.DOUBLE_WORD, board.getSquare(1,5).getType());
        assertEquals(Type.TRIPLE_WORD, board.getSquare(10,14).getType());
    }

    /**
     * Test of the setTile() method */
    @Test
    public void setTileTest(){
        Tile t1 = new Tile('A', 1, 9);
        board.setTile(0, 1 , t1);
        Tile t2 = new Tile('B', 3, 2);
        board.setTile(1,2,t2);
        Tile t3 = new Tile('C', 3, 2);
        board.setTile(1, 3, t3);

        assertEquals(t1, board.getSquare(0,1).getTile());
        assertEquals(t2, board.getSquare(1,2).getTile());
        assertEquals(t3, board.getSquare(1,3).getTile());
    }

    /**
     * Test of the getSquare() method */
    @Test
    public void getSquareTest(){
        assertEquals(board.getSquare(0, 1), squares[0][1]);
        assertEquals(board.getSquare(2, 4), squares[2][4]);
        assertEquals(board.getSquare(10, 1), squares[10][1]);
        assertEquals(board.getSquare(14, 10), squares[14][10]);
        assertEquals(board.getSquare(9, 5), squares[9][5]);
    }

    /**
     * Test of the isEmptySquare() method*/
    @Test
    public void isEmptySquareTest(){
        assertTrue(board.isEmptySquare(squares[3][7]));
        assertTrue(board.isEmptySquare(squares[5][13]));

        Tile t1 = new Tile('A', 1, 9);
        board.setTile(7, 8, t1);
        Tile t2 = new Tile('B', 3, 2);
        board.setTile(9, 12, t2);

        assertFalse(board.isEmptySquare(squares[9][12]));
        assertFalse(board.isEmptySquare(squares[7][8]));
    }

    /**
     * Test of the isEmpty() method*/
    @Test
    public void isEmptyTest(){
        //check if board is empty before adding anything to it
        assertTrue(board.isEmpty());

        //add a tile and check if board is empty returns false
        Tile t1 = new Tile('A', 1, 9);
        board.setTile(1, 3, t1);

        assertFalse(board.isEmpty());
    }

    /**
     * Test of the clone() method*/
    @Test
    public void cloneTest(){
        //we set Tile t1 on board and then make a clone of the board, we expect t1 to be present in the cloneBoard
        Tile t1 = new Tile('A', 1, 9);
        board.setTile(7, 7, t1);
        Board cloneBoard = board.clone();

        //we create a new Tile t2 and set it only in cloneBoard, so we expect that the board will not have it
        Tile t2 = new Tile('B', 3, 2);
        cloneBoard.setTile(9, 8, t2);

        //Check if t1 is indeed on the cloneBoard
        assertEquals(t1, cloneBoard.getSquare(7, 7).getTile());

        //check if t2 is not on the initial board and that t2 is on the cloneBoard
        assertNull(board.getSquare(9, 8).getTile());
        assertEquals(t2, cloneBoard.getSquare(9, 8).getTile());
    }
}