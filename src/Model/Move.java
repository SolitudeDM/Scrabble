package Model;

import Exceptions.EmptyCommandException;
import Exceptions.InvalidCommandException;
import Exceptions.SquareNotEmptyException;
import Exceptions.WrongOrientationException;
import Model.players.Player;
import main.java.InMemoryScrabbleWordChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Model.Board.RESOLUTION;

/**
 * This class represents a move in Scrabble game
 * @author Mark Zhitchenko and Dani Mahaini*/
public class Move {

    private Game game;
    private Player player;
    private int score = 0;
    private boolean valid;
//    private boolean doubleWord;
//    private boolean tripleWord;
    private int doubleWord;
    private int tripleWord;
    private boolean bingo;
    private boolean moveMade;
    private boolean requestAnother;

    public Move(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public boolean isMoveMade() {
        return moveMade;
    }

    /**
     * This method is responsible for the options the player has for the turn
     *
     * @requires choice to be of the right format, for placing a word(PLACE; H8; V; WORD), for swapping tiles (SWAP; A B C D)
     * @ensures to place a word if the move is valid
     * @ensures to swap the chosen tiles if that is possible
     */
    public void options(String choice) throws EmptyCommandException, WrongOrientationException, InvalidCommandException {
        moveMade = false;

        while (!moveMade && !requestAnother) {
            String[] splittedChoice = choice.split("; ");

            if (choice.isEmpty()) {
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
//                    moveMade = true;
//FORMAT --> PLACE; H8; V; WORD
                    break;

                case ("SWAP"):
//FORMAT --> SWAP; A B C D
                    ArrayList<Tile> newHand = new ArrayList<>(player.getHand());
                    String[] swap = splittedChoice[1].split(" ");
                    ArrayList<Tile> swappedTiles = new ArrayList<>();
                    int tilesToSwap = 0;
                    boolean letterDoesNotBelong = true;

                    for (String letter : swap) {
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

                    if (tilesToSwap > game.getTileSack().size()) {
                        System.out.println("There is only " + game.getTileSack().size() + " tiles left in the sack");
                        requestAnother = true;
                        return;
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

                case ("DEBUG"):
                    player.setHand(game.getInitialTiles());
                    moveMade = true;
                    break;

                default:
                    throw new InvalidCommandException("Command invalid, valid commands are(PLACE | SWAP | EXIT)");

            }
        }
    }

    /**
     * This method is responsible for placing a word on the board
     *
     * @param coordinates are the coordinates of the required square
     * @param vertical    is used to see whether the word should be set vertically or horizontally
     * @param word        is the word that is going to be placed
     * @param board       is the board where the word is going to be placed at
     * @requires all the parameters != null && coordinates within the board boundaries && word to be an existing word
     * @ensures to put the given word to the board
     */
    public void place(String coordinates, boolean vertical, String word, Board board) throws SquareNotEmptyException {
        InMemoryScrabbleWordChecker checker = new InMemoryScrabbleWordChecker();
        boolean tilesAbused = false;
        boolean bingo = false;


        //  Check if word exists
        if (checker.isValidWord(word) == null) {
            moveMade = true;
            System.out.println("Word " + word + " does not exist!");
            return;
        }

        //in case the row index is a two-digit number
        String[] index = coordinates.split("");
        if (index.length != 2) {
            index[1] = index[1] + index[2];
        }

        // Revert coordinates of letter-number to number-number
        index[0] = String.valueOf(letterToCoordinate(index[0].charAt(0)));

        //Fix for the sliding coordinates problem
        index[0] = String.valueOf(Integer.parseInt(index[0]) - 1);
        index[1] = String.valueOf(Integer.parseInt(index[1]) - 1);

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

        if (tilesUsed.size() >= 7) {
            bingo = true;
        }

        ArrayList<Tile> tilesUsedCopy = new ArrayList<>(tilesUsed);
//        ArrayList<Tile> tilesUsedCopy2 = new ArrayList<>(tilesUsed);

        //doubleWord and tripleWord booleans are initialized
        doubleWord = 0;
        tripleWord = 0;


        //Check if rigged correctly
        if (!checkRig(board, Integer.parseInt(index[0]), Integer.parseInt(index[1]), word, vertical)) {
            System.out.println("Invalid placement!");
            requestAnother = true;
            return;
        }


        //Check if cells are available
        if (vertical) {

            if (Integer.parseInt(index[0]) - 1 + word.length() >= 15) {
                System.out.println("Word won't fit vertically");
                requestAnother = true;
                return;
            }

            tilesAbused = true;
            for (int i = Integer.parseInt(index[0]); i < word.length() + Integer.parseInt(index[0]); i++) {
                if (!tilesUsed.contains(board.getSquare(i, Integer.parseInt(index[1])).getTile())) {
                    tilesAbused = false;
                    if (!board.isEmptySquare(board.getSquare(i, Integer.parseInt(index[1])))) {
                        requestAnother = true;
                        throw new SquareNotEmptyException("Square is already occupied!");
                    }
                }

            }
        }

        //Check if cells are available
        if (!vertical) {

            if (Integer.parseInt(index[1]) + word.length()  - 1 >= 15) {
                System.out.println("Word won't fit horizontally");
                return;
            }

            tilesAbused = true;
            for (int i = Integer.parseInt(index[1]); i < word.length() + Integer.parseInt(index[1]); i++) {
                if (!tilesUsed.contains(board.getSquare(Integer.parseInt(index[0]), i).getTile())) {
                    tilesAbused = false;
                    if (!board.isEmptySquare(board.getSquare(Integer.parseInt(index[0]), i))) {
                        requestAnother = true;
                        throw new SquareNotEmptyException("Square is already occupied!");
                    }
                }

            }
        }

        if (tilesAbused) {
            System.out.println("Заабузел B)");
            requestAnother = true;
            return;
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


        Board boardCopy = board.clone();

        //ALL THAT IS LEFT TO DO IS TO INSERT THE LETTERS AND SUM UP THE POINTS
        if (player.searchHand(tilesUsed)) {
            if (vertical) {
                int i = 0;
                while (i < word.length()) {
                    for (Tile tile : tilesUsedCopy) {
                        boardCopy.setTile(Integer.parseInt(index[0]) + i, Integer.parseInt(index[1]), tile);

//                        int temp = Integer.parseInt(index[0]) + i;

//                        game.getUsedCoordinates().add(temp + ", " + index[1]);

//                        calculate(Integer.parseInt(index[0]) + i, Integer.parseInt(index[1]), boardCopy);
                        i++;
                    }
                }
                if (doubleWord != 0) {
                    score *= 2 * doubleWord;
                }
                if (tripleWord != 0) {
                    score *= 3 * tripleWord;
                }
            }

            if (!vertical) {
                int i = 0;
                while (i < word.length()) {
                    for (Tile tile : tilesUsedCopy) {
                        boardCopy.setTile(Integer.parseInt(index[0]), Integer.parseInt(index[1]) + i, tile);

//                        int temp = Integer.parseInt(index[1]) + i;
//
//                        game.getUsedCoordinates().add(index[0] + ", " + (temp));

//                        calculate(Integer.parseInt(index[0]), Integer.parseInt(index[1]) + i,  boardCopy);
                        i++;
                    }
                    if (doubleWord != 0) {
                        score *= 2 * doubleWord;
                    }
                    if (tripleWord != 0) {
                        score *= 3 * tripleWord;
                    }
                }

//                board.setTile(Integer.parseInt(index[0]),Integer.parseInt(index[1]), tile);
            }
        }


        neighboursCheck(boardCopy, Integer.parseInt(index[0]), Integer.parseInt(index[1]), word, vertical);
        if (moveMade || requestAnother){
            return;
        }

//ALL THAT IS LEFT TO DO IS TO INSERT THE LETTERS AND SUM UP THE POINTS
        if (player.searchHand(tilesUsed)) {
            if (vertical) {
                int i = 0;
                while (i < word.length()) {
                    for (Tile tile : tilesUsedCopy) {
                        board.setTile(Integer.parseInt(index[0]) + i, Integer.parseInt(index[1]), tile);

//                        int temp = Integer.parseInt(index[0]) + i;

//                        game.getUsedCoordinates().add(temp + ", " + index[1]);

                        calculate(Integer.parseInt(index[0]) + i, Integer.parseInt(index[1]), board);
                        i++;
                    }
                }
                if (doubleWord != 0) {
                    score *= 2 * doubleWord;
                }
                if (tripleWord != 0) {
                    score *= 3 * tripleWord;
                }
            }

            if (!vertical) {
                int i = 0;
                while (i < word.length()) {
                    for (Tile tile : tilesUsedCopy) {
                        board.setTile(Integer.parseInt(index[0]), Integer.parseInt(index[1]) + i, tile);

//                        int temp = Integer.parseInt(index[1]) + i;
//
//                        game.getUsedCoordinates().add(index[0] + ", " + (temp));

                        calculate(Integer.parseInt(index[0]), Integer.parseInt(index[1]) + i,  board);
                        i++;
                    }
                    if (doubleWord != 0) {
                        score *= 2 * doubleWord;
                    }
                    if (tripleWord != 0) {
                        score *= 3 * tripleWord;
                    }
                }

//                board.setTile(Integer.parseInt(index[0]),Integer.parseInt(index[1]), tile);
            }
        }

        if (player.searchHandDelete(tilesUsed)) {
            if (vertical) {
                int i = 0;
                while (i < word.length()) {
                    for (Tile tile : tilesUsedCopy) {
                        board.setTile(Integer.parseInt(index[0]) + i, Integer.parseInt(index[1]), tile);

                        int temp = Integer.parseInt(index[0]) + i;

                        if (board.getSquare(temp, Integer.parseInt(index[1])).getTile() != null) {
                            board.getSquare(temp, Integer.parseInt(index[1])).setType(Type.NORMAL);
                        }

                        game.getUsedCoordinates().add(temp + ", " + index[1]);

                        i++;
                    }
                }
            }

            if (!vertical) {
                int i = 0;
                while (i < word.length()) {
                    for (Tile tile : tilesUsedCopy) {
                        board.setTile(Integer.parseInt(index[0]), Integer.parseInt(index[1]) + i, tile);

                        int temp = Integer.parseInt(index[1]) + i;

                        if (board.getSquare(Integer.parseInt(index[0]), temp).getTile() != null) {
                            board.getSquare(Integer.parseInt(index[0]), temp).setType(Type.NORMAL);
                        }

                        game.getUsedCoordinates().add(index[0] + ", " + (temp));

                        i++;
                    }

                }

//                board.setTile(Integer.parseInt(index[0]),Integer.parseInt(index[1]), tile);
            }
        }


//        for (Tile t : tilesUsed) {
//            t.setPlaced(true);
//        }
//
//        if (vertical) {
//            for (int i = Integer.parseInt(index[0]); i < word.length() + Integer.parseInt(index[0]); i++) {
//
//                if (board.getSquare(i, Integer.parseInt(index[1])).getTile().isPlaced()) {
//                    board.getSquare(i, Integer.parseInt(index[1])).setType(Type.NORMAL);
//                }
//
//
//            }
//        }
//
//        // Remove all the existing letters from "lettersUsed" horizontal
//        if (!vertical) {
//            for (int i = Integer.parseInt(index[1]); i < word.length() + Integer.parseInt(index[1]); i++) {
//
//                if (board.getSquare(Integer.parseInt(index[0]), i).getTile().isPlaced()) {
//                    board.getSquare(Integer.parseInt(index[0]), i).setType(Type.NORMAL);
//                }
//
//            }
//        }

        if (bingo) {
            score += 50;
        }

        player.setScore(player.getScore() + score);
        System.out.println(score);
        moveMade = true;
    }


    /**
     * This method calculates the score of a move
     *
     * @param row   is the index of the row
     * @param col   is index of the column
     * @param board is the board where the move was made
     * @requires row != null && row >= 0 && row <= 14
     * @requires col != null && col >= 0 && col <= 14
     * @requires board != null
     * @ensures to calculate the right score of a move and set it to the variable score
     */
    public void calculate(int row, int col, Board board) {
        switch (game.getBoard().getSquare(row, col).getType()) {
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
            case CENTER:
                score += board.getSquare(row, col).getTile().getLetterPoints();
                doubleWord += 1;
                break;
            case TRIPLE_WORD:
                score += board.getSquare(row, col).getTile().getLetterPoints();
                tripleWord += 1;
                break;
            default:
                score += board.getSquare(row, col).getTile().getLetterPoints();
                break;
        }
    }

    /**
     * This method converts the letter index to numeric index using ASCII
     *
     * @param letter is the letter that is going to be represented as a number
     * @requires letter != null
     * @ensures to return an integer value of an alphabetic letter
     */
    public int letterToCoordinate(char letter) {
        int temp = (int) letter;
        int temp_integer = 64; //for upper case
        if (temp <= 90 & temp >= 65) {
            return (temp - temp_integer);
        }
        return -69;
    }

    public boolean checkRig(Board board, int row, int col, String word, boolean vertical) {

        // Check if the first move is correctly placed
        if (board.isEmpty()) {
            //Check if cells are available
            if (vertical) {
                for (int i = row; i < word.length() + row; i++) {
                    if (board.getSquare(i, col).getType() == Type.CENTER) {
                        return true;
                    }
                }
            }
            //Check if cells are available
            if (!vertical) {
                for (int i = col; i < word.length() + col; i++) {
                    if (board.getSquare(row, i).getType() == Type.CENTER) {
                        return true;
                    }
                }
            }

        }

        if (!board.isEmpty()) {
            //Check if cells are available
            if (vertical) {
                for (int i = row; i < word.length() + row; i++) {

                    if (board.getSquare(i, col).getTile() != null || (i != 14 && board.getSquare(i + 1, col).getTile() != null) || ( i != 0 && board.getSquare(i - 1, col).getTile() != null) ||( col != 14 &&  board.getSquare(i, col + 1).getTile() != null) || (col != 0 && board.getSquare(i, col - 1).getTile() != null)) {
                        return true;
                    }
                }
            }
            //Check if cells are available
            if (!vertical) {
                for (int i = col; i < word.length() + col; i++) {
                    if (board.getSquare(row, i).getTile() != null || (i != 14 &&  board.getSquare(row, i + 1).getTile() != null) || (i != 0 && board.getSquare(row, i - 1).getTile() != null) ||( row != 14 && board.getSquare(row + 1, i).getTile() != null) || ( row != 0 && board.getSquare(row - 1, i).getTile() != null)) {
                        return true;
                    }
                }
            }

        }



        return false;
    }

    /**
     * This method checks whether a placed word created any other new words on the board and
     * calculates the score of the new words if there are any
     *
     * @param board is the board the game is being played on
     * @param row is the row index
     * @param col is the column index
     * @param word is the word we check neighbours for
     * @param vertical is the orientation of the placed word
     * @requires all parameters != null && index to be within the board boundaries
     * @ensures to properly check all the newly created words and calculate their score*/
    public void neighboursCheck(Board board, int row, int col, String word, boolean vertical) {
        InMemoryScrabbleWordChecker checker = new InMemoryScrabbleWordChecker();
        boolean directionChecked = false;


        String wordToCheck = "";
        int[] coordinates = {-1, -1};
        if (!vertical) {

            if(col != 0 && board.getSquare(row, col - 1 ).getTile() != null) {

                System.out.println("Please, write the whole word in one line");
                requestAnother = true;
            } else if ( col + word.length() < 15 && board.getSquare(row, col + word.length() + 1 ).getTile() != null) {

                System.out.println("Please, write the whole word in one line");
                requestAnother = true;
            }

            for (int i = col; i < col + word.length(); i++) {

                if (!game.getUsedCoordinates().contains(row + ", " + i)) {
                    if (row != 0) {
                        if (board.getSquare(row - 1, i).getTile() != null) {
                            directionChecked = true;
                            int j = row - 1;
                            while (board.getSquare(j, i).getTile() != null && j != 0) {
                                j--;
                            }
                            coordinates[0] = j + 1;
                            coordinates[1] = i;
                            while (board.getSquare(j + 1, i).getTile() != null && j != 14) {
                                wordToCheck += board.getSquare(j + 1, i).getTile().getLetter();
                                j++;
                            }

                            if (checker.isValidWord(wordToCheck) != null) {
                                for (int k = 0; k < wordToCheck.length(); k++) {
                                    score += game.getTile(wordToCheck.charAt(k)).getLetterPoints();

                                }
                                player.setScore(player.getScore() + score);
                                score = 0;
                                System.out.println("Word " + wordToCheck + " changed score to " + player.getScore());
                            } else {
                                System.out.println(wordToCheck + " is not a word");
                                moveMade = true;
                            }
                        }
                    }

                    wordToCheck = "";

                    if (!directionChecked) {
                        if (row != 14) {
                            if (board.getSquare(row + 1, i).getTile() != null) {
                                int j = row + 1;
                                coordinates[0] = j - 1;
                                coordinates[1] = i;
                                while (board.getSquare(j - 1, i).getTile() != null && j != 14) {
                                    wordToCheck += board.getSquare(j - 1, i).getTile().getLetter();
                                    j++;
                                }

                                if (checker.isValidWord(wordToCheck) != null) {
                                    for (int k = 0; k < wordToCheck.length(); k++) {
                                        score += game.getTile(wordToCheck.charAt(k)).getLetterPoints();
                                    }
                                    player.setScore(player.getScore() + score);
                                    score = 0;
                                    System.out.println("Word " + wordToCheck + " changed score tO " + player.getScore());
                                } else {
                                    System.out.println(wordToCheck + " is not a word");
                                    moveMade = true;
                                }
                            }
                        }
                    }

//                    if (board.getSquare(row + 1, i).getTile() != null) {
//                        //to be implemented
//                    }
                }
                directionChecked = false;
            }
        }

            if (vertical) {

                if(row != 0 && board.getSquare(row - 1, col).getTile() != null) {

                    System.out.println("Please, write the whole word in one vertical line");
                    requestAnother = true;
                } else if(row + word.length() < 15 && board.getSquare(row + word.length() + 1, col).getTile() != null) {

                    System.out.println("Please, write the whole word in one line");
                    requestAnother = true;
                }

                for (int i = row; i < row + word.length(); i++) {
                     if (!game.getUsedCoordinates().contains(i + ", " + col)) {
                     if (col != 0) {
                          if (board.getSquare(i, col - 1).getTile() != null) {
                              directionChecked = true;
                              int j = col - 1;
                              while (board.getSquare(i, j).getTile() != null && j != 0) {
                                  j--;
                              }
                              coordinates[1] = j + 1;
                              coordinates[0] = i;
                              while (board.getSquare(i, j + 1).getTile() != null && j != 14) {
                                  wordToCheck += board.getSquare(i, j + 1).getTile().getLetter();
                                  j++;
                              }

                              if (checker.isValidWord(wordToCheck) != null) {
                                  for (int k = 0; k < wordToCheck.length(); k++) {
                                      score += game.getTile(wordToCheck.charAt(k)).getLetterPoints();
                                  }

                               } else {
                                  System.out.println(wordToCheck + " is not a word");
                                  moveMade = true;
                              }
                            }

                    }

                     wordToCheck = "";

                     if (!directionChecked) {
                         if (col != 14) {
                             if (board.getSquare(i, col + 1).getTile() != null) {
                                 int j = col + 1;
                                 coordinates[1] = j - 1;
                                 coordinates[0] = i;
                                 while (board.getSquare(i, j - 1).getTile() != null && j != 14) {
                                     wordToCheck += board.getSquare(j - 1, i).getTile().getLetter();
                                     j++;
                                 }

                                 if (checker.isValidWord(wordToCheck) != null) {
                                     for (int k = 0; k < wordToCheck.length(); k++) {
                                         score += game.getTile(wordToCheck.charAt(k)).getLetterPoints();
                                     }
                                 } else {
                                     System.out.println(wordToCheck + " is not a word");
                                     moveMade = true;
                                 }
                             }
                         }
                     }
                     }
                    directionChecked = false;
                }

                }

        }

//    public static Square[][] deepCopyIntMatrix(Square[][] input) {
//        if (input == null)
//            return null;
//        Square[][] result = new Square[input.length][];
//        for (int r = 0; r < input.length; r++) {
//            result[r] = input[r].clone();
//        }
//        return result;
//    }

    }

