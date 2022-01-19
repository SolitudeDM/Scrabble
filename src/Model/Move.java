package Model;

import Exceptions.EmptyCommandException;
import Exceptions.InvalidCommandException;
import Exceptions.SquareNotEmptyException;
import Exceptions.WrongOrientationException;
import Model.players.Player;
import main.java.InMemoryScrabbleWordChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a move in Scrabble game
 * @author Mark Zhitchenko and Dani Mahaini*/
public class Move {

    private Game game;
    private Player player;
    private int score = 0;
    private boolean valid;
    private boolean doubleWord;
    private boolean tripleWord;
    private boolean bingo;
    private boolean moveMade;


    public Move(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    /**
     * This method is responsible for the options the player has for the turn
     * @requires choice to be of the right format, for placing a word(PLACE; H8; V; WORD), for swapping tiles (SWAP; A B C D)
     * @ensures to place a word if the move is valid
     * @ensures to swap the chosen tiles if that is possible*/
    public void options(String choice) throws EmptyCommandException, WrongOrientationException, InvalidCommandException {
        moveMade = false;

        while (!moveMade) {
            String[] splittedChoice = choice.split("; ");

            if (splittedChoice.length < 1) {
                throw new EmptyCommandException("Command is empty!");
            }

            switch (splittedChoice[0].toUpperCase()) {

                case ("PLACE"):

                    boolean orientation;

                    if ((splittedChoice[2].equals("V"))) {
                        orientation = true;
                    } else if (splittedChoice[2].equals("H")) {
                        orientation = false;
                    } else {
                        throw new WrongOrientationException("The orientation should be horizontal(H) or vertical(V)!");
                    }

                    try {
                        place(splittedChoice[1], orientation, splittedChoice[3], game.getBoard());
                    } catch (SquareNotEmptyException e) {
                        e.printStackTrace();
                    }
                    moveMade = true;
//FORMAT --> PLACE; H8; V; WORD
                    break;

                case ("SWAP"):
//FORMAT --> SWAP; A B C D
                    ArrayList<Tile> newHand = new ArrayList<>(player.getHand());
                    String[] swap = splittedChoice[1].split(" ");
                    ArrayList<Tile> swappedTiles = new ArrayList<>();
                    int tilesToSwap = 0;
                    boolean letterDoesNotBelong = true;

                    for(String letter : swap){
                        letterDoesNotBelong = true;
                        swappedTiles.add(game.getTile(letter.charAt(0)));

                        for (int i = 0; i < player.getHand().size() && letterDoesNotBelong; i++) {
                            if (!(player.getHand().get(i).getLetter() == letter.charAt(0))) {
                                letterDoesNotBelong = true;
                            } else {
                                for (int j = 0; j < newHand.size(); j++) {
                                    if (newHand.get(j).getLetter() == letter.charAt(0)) {
                                        newHand.remove(j);
                                    }
                                }
//                                newHand.remove(game.getTile(letter.charAt(0)));
                                tilesToSwap++;
                                letterDoesNotBelong = false;
                            }
                        }
                    }

                    List<Tile> newTiles = game.getTileSack().subList(0, tilesToSwap);
                    newHand.addAll(newTiles);

                    ArrayList<Tile> tileSackCopy = new ArrayList<>(game.getTileSack());
                    tileSackCopy.removeAll(newTiles);
                    tileSackCopy.addAll(swappedTiles);
                    game.setTileSack(tileSackCopy);

                    player.setHand(newHand);

                    moveMade = true;
                    break;

                case ("EXIT"):
                    game.setFinishGame(true);
                    moveMade = true;
                    break;

                default:
                    throw new InvalidCommandException("Command invalid, valid commands are(PLACE | SWAP | EXIT)");
            }
        }
    }

    /**
     * This method is responsible for placing a word on the board
     * @param coordinates are the coordinates of the required square
     * @param vertical is used to see whether the word should be set vertically or horizontally
     * @param word is the word that is going to be placed
     * @param board is the board where the word is going to be placed at
     * @requires all the parameter != null
     * @ensures to put the given word to the board*/
    public void place(String coordinates, boolean vertical, String word, Board board) throws SquareNotEmptyException {

        boolean firstMove = false;

        InMemoryScrabbleWordChecker checker = new InMemoryScrabbleWordChecker();
       //  Check if word exists
        if (checker.isValidWord(word) == null) {
            moveMade = true;
            return;
        }


        //in case the row index is a double-digit number
        String[] index = coordinates.split("");
        if (index.length != 2) {
            index[1] = index[1] + index [2];
        }
        // Revert coordinates of letter-number to number-number
        index[0] = String.valueOf(letterToCoordinate(index[0].charAt(0)));

        //Fix for the sliding coordinates problem
        index[0] =  String.valueOf(Integer.parseInt(index[0]) - 1);
        index[1] =  String.valueOf(Integer.parseInt(index[1]) - 1);

        //Fix for the mirrored coordinates
        String tempCoordinate = index[0];
        index[0] = index[1];
        index[1] = tempCoordinate;

        String[] lettersUsed = (word.toUpperCase().split(""));
        ArrayList<Tile> tilesUsed = new ArrayList<>();

        // remove letters that are already on the board from the lettersUsed, so only the fresh ones will be counted
        for (String letter : lettersUsed) {

            tilesUsed.add(player.getGame().getTile(letter.charAt(0)));
        }

        ArrayList<Tile> tilesUsedCopy = new ArrayList<>(tilesUsed);

        //doubleWord and tripleWord booleans are initialized
         doubleWord = false;
         tripleWord = false;

         //Check if cells are available
        if (vertical) {
            for (int i = Integer.parseInt(index[0]); i < word.length() + Integer.parseInt(index[0]); i++) {
                if (!tilesUsed.contains(board.getSquare(i, Integer.parseInt(index[1])).getTile())) {
                    if (!board.isEmptySquare(board.getSquare(i, Integer.parseInt(index[1])))) {
                        moveMade = true;
                        throw new SquareNotEmptyException("Square is already occupied!");
                    }
                }
            }
        }

        //Check if cells are available
        if (!vertical) {
            for (int i = Integer.parseInt(index[1]); i < word.length() + Integer.parseInt(index[1]); i++) {
                if (!tilesUsed.contains(board.getSquare(Integer.parseInt(index[0]), i).getTile())) {
                    if (!board.isEmptySquare(board.getSquare(Integer.parseInt(index[1]), i))) {
                        moveMade = true;
                        throw new SquareNotEmptyException("Square is already occupied!");
                    }
                }
            }
        }

        // Remove all the existing letters from "lettersUsed" vertical
        if (vertical) {
            for (int i = Integer.parseInt(index[0]); i < word.length() + Integer.parseInt(index[0]); i++) {
//                wordScore += (board.getSquare(i,Integer.parseInt(index[0])).getTile().getLetterPoints()) *board.getSquare(i,Integer.parseInt(index[0])).getType()
//                calculate(i, Integer.parseInt(index[1]), board);

                tilesUsed.remove(board.getSquare(i, Integer.parseInt(index[1])).getTile());
            }
        }

        // Remove all the existing letters from "lettersUsed" horizontal
        if (!vertical) {
            for (int i = Integer.parseInt(index[1]); i < word.length() + Integer.parseInt(index[1]); i++) {

//                calculate(Integer.parseInt(index[0]), i, board);

                tilesUsed.remove(board.getSquare(Integer.parseInt(index[0]), i).getTile());
            }
        }


        //ALL THAT IS LEFT TO DO IS TO INSERT THE LETTERS AND SUM UP THE POINTS
        if (player.searchHand(tilesUsed)) {
            if(vertical) {
                int i = 0;
                while (i < word.length()) {
                    for (Tile tile : tilesUsedCopy) {
                        board.setTile(Integer.parseInt(index[0]) + i, Integer.parseInt(index[1]), tile);
                        calculate(Integer.parseInt(index[0]) + i,  Integer.parseInt(index[1]), board);
                        i++;
                    }
                }
                if (doubleWord) {
                    score *= 2;
                }
                if (tripleWord) {
                    score *= 3;
                }
            }

            if(!vertical) {
                int i = 0;
                while (i < word.length()) {
                    for (Tile tile : tilesUsedCopy) {
                        board.setTile(Integer.parseInt(index[0]), Integer.parseInt(index[1]) + i, tile);
                        calculate(Integer.parseInt(index[0]),  Integer.parseInt(index[1]) + i, board);
                        i++;
                    }
                    if (doubleWord) {
                        score *= 2;
                    }
                    if (tripleWord) {
                        score *= 3;
                    }
                }

//                board.setTile(Integer.parseInt(index[0]),Integer.parseInt(index[1]), tile);
            }
        }
        player.setScore(player.getScore() + score);
        System.out.println(score);
    }


    /**
     * This method calculates the score of a move
     * @requires row != null && row >= 0 && row <= 14
     * @requires col != null && col >= 0 && col <= 14
     * @requires board != null
     * @param row is the index of the row
     * @param col is index of the column
     * @param board is the board where the move was made
     * @ensures to calculate the right score of a move and set it to the variable score*/
    public void calculate(int row, int col, Board board) {
        switch (board.getSquare(row, col).getType()) {
            case NORMAL:
                score += board.getSquare(row, col).getTile().getLetterPoints();
                break;
            case DOUBLE_LETTER:
                score += board.getSquare(row, col).getTile().getLetterPoints() * 2;
                break;
            case TRIPLE_LETTER:
                score += board.getSquare(row, col).getTile().getLetterPoints() * 3;
                break;
            case DOUBLE_WORD:
                score += board.getSquare(row, col).getTile().getLetterPoints();
                doubleWord = true;
                break;
            case TRIPLE_WORD:
                score += board.getSquare(row, col).getTile().getLetterPoints();
                tripleWord = true;
                break;
            default:
                score += board.getSquare(row, col).getTile().getLetterPoints();
                break;
        }
    }

    /**
     * This method converts the letter index to numeric index using ASCII
     * @requires letter != null
     * @param letter is the letter that is going to be represented as a number
     * @ensures to return an integer value of an alphabetic letter*/
    public int letterToCoordinate(char letter) {
        int temp = (int)letter;
        int temp_integer = 64; //for upper case
        if(temp<=90 & temp>=65) {
            return (temp - temp_integer);
        }
        return -69;
    }
}

