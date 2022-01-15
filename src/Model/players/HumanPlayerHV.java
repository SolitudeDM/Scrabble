package Model.players;

import Model.Board;
import Model.Game;
import Model.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class HumanPlayerHV extends Player {

    public HumanPlayerHV(String name, Game game){
        super(name,game);
    }


    //SWAP

    @Override
    public void determineMove(Board board) {
        // Create a Scanner object to read input.
        Scanner keyboard = new Scanner(System.in);
        boolean moveMade = false;

        while (!moveMade) {

            System.out.println("enter your choice(command; coordinates; orientation; word)");

            String choice = keyboard.nextLine();
            String[] splittedChoice = choice.split("; ");

            if (splittedChoice.length < 1) {
                System.out.println("Empty command");
            }

            switch (splittedChoice[0].toUpperCase()) {

                case ("PLACE"):

                    boolean orientation;

                    if ((splittedChoice[2].equals("V"))) {
                        orientation = true;
                    } else if (splittedChoice[2].equals("H")) {
                        orientation = false;
                    } else {
                        System.out.println("POSHEL V ZHEPU");
                        break;
                    }

                    place(splittedChoice[1], orientation, splittedChoice[3], board);
                    moveMade = true;
//FORMAT --> PLACE; H8; V; COCK
                    break;

                case ("SWAP"):



                    break;

                case ("EXIT"):

                    break;

                default:
                    System.out.println("Invalid command " + splittedChoice[0]);
                    break;
            }
        }

//        if(board.isEmptySquare(board.getSquare(parseInt(splittedChoice[1]), parseInt(splittedChoice[2])))){
//            board.setTile(parseInt(splittedChoice[1]), parseInt(splittedChoice[2]), );

    }

    /**
     * This method is responsible for placing a word on the board
     * @param coordinates are the coordinates of the required square
     * @param vertical is used to see whether the word should be set vertically or horizontally
     * @param word is the word that is going to be placed
     * @param board is the board where the word is going to be placed at
     * @requires all the parameter != null
     * @ensures to put the given word to the board*/
    public void place(String coordinates, boolean vertical, String word, Board board) {


//       //  Check if word exists
//        if (!isValidWord(word)) {
//            // moveMade = true
//            return;
//        }

        // Add score
        int wordScore = 0;


        // Revert coordinates of letter-number to number-number

        String[] index = coordinates.split("");
        if (index.length != 2) {
            index[1] = index[1] + index [2];
        }
        index[0] = String.valueOf(letterToCoordinate(index[0].charAt(0)));

        //Fix for the sliding coordinates problem
        index[0] =  String.valueOf(Integer.parseInt(index[0]) - 1);
        index[1] =  String.valueOf(Integer.parseInt(index[1]) - 1);

        //Fix for the marrowed coordinates
        String tempCoordinate = index[0];
        index[0] = index[1];
        index[1] = tempCoordinate;

        String[] lettersUsed = (word.toUpperCase().split(""));
        ArrayList<Tile> tilesUsed = new ArrayList<>();

        // remove letters that are already on the board from the lettersUsed, so only the fresh ones will be counted
        for (String letter : lettersUsed) {

            tilesUsed.add(this.getGame().getTile(letter.charAt(0)));
        }

        // Remove all the existing letters from "lettersUsed" horizontal
        if (!vertical) {
            for (int i = Integer.parseInt(index[1]); i < word.length(); i++) {
//                wordScore += (board.getSquare(i,Integer.parseInt(index[0])).getTile().getLetterPoints()) *board.getSquare(i,Integer.parseInt(index[0])).getType()

               tilesUsed.remove(board.getSquare(i,Integer.parseInt(index[0])).getTile());
            }
        }

        // Remove all the existing letters from "lettersUsed" vertical
        if (vertical) {
            for (int i = Integer.parseInt(index[0]); i < word.length(); i++) {
                tilesUsed.remove(board.getSquare(i,Integer.parseInt(index[1])).getTile());
            }
        }

        //ALL THAT IS LEFT TO DO IS TO INSERT THE LETTERS AND SUM UP THE POINTS
        if (searchHand(tilesUsed)) {
                if(vertical) {
                    int i = 0;
                    while (i < word.length()) {
                        for (Tile tile : tilesUsed) {
                            board.setTile(Integer.parseInt(index[0]) + i, Integer.parseInt(index[1]), tile);
                            i++;
                        }
                    }
                }
                if(!vertical) {
                    int i = 0;
                        while (i < word.length()) {
                            for (Tile tile : tilesUsed) {
                                board.setTile(Integer.parseInt(index[0]), Integer.parseInt(index[1]) + i, tile);
                                i++;
                            }
                        }

//                board.setTile(Integer.parseInt(index[0]),Integer.parseInt(index[1]), tile);
                }
        }

    }
    /**
     * This method converts the letter index to numeric index using ASCII
     * @requires letter != null
     * @param letter is the letter that is going to be represented as a number
     * @ensures to???*/
    public int letterToCoordinate(char letter) {
        int temp = (int)letter;
        int temp_integer = 64; //for upper case
        if(temp<=90 & temp>=65) {
            return (temp - temp_integer);
        }
        return -69;
    }

}
