package Model.players;

import Model.Board;
import Model.Game;
import Model.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class HumanPlayerHV extends Player{

    public HumanPlayerHV(String name, Game game){
        super(name,game);
    }


    //SWAP

    @Override
    public String determineMove(Board board) {
        // Create a Scanner object to read input.
        Scanner keyboard = new Scanner(System.in);
        boolean moveMade = false;

        while (!moveMade) {

            HashMap<String[], String> tileset = new HashMap<>();

            System.out.println("enter your choice(command; coordinates; orientation; word)");

            String choice = keyboard.nextLine();
            String[] splittedChoice = choice.split("; ");

            if (splittedChoice.length < 1) {
                System.out.println("Empty command");
                return null;
            }

            switch (splittedChoice[0].toUpperCase()) {

                case ("PLACE"):

                    boolean orientation;

                    if ((splittedChoice[2].equals("V"))) {
                        orientation = false;
                    } else if (splittedChoice[2].equals("H")) {
                        orientation = true;
                    } else {
                        System.out.println("POSHEL V ZHEPU");
                        break;
                    }

                    place(splittedChoice[1], orientation, splittedChoice[3], board);
//FORMAT --> PLACE H8; V; COCK
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

        return null;
    }

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
        index[1] = String.valueOf(letterToCoordinate(index[1].charAt(0)));

        String[] lettersUsed = (word.toUpperCase().split(""));
        ArrayList<Tile> tilesUsed = new ArrayList<>();

        // remove letters that are already on the board from the lettersUsed, so only the fresh ones will be counted
        for (String letter : lettersUsed) {
            tilesUsed.add(this.getGame().getTile(letter.charAt(0)));
        }

        // Remove all the existing letters from "lettersUsed" horizontal
        if (!vertical) {
            for (int i = Integer.parseInt(index[1]); i < word.length(); i++) {
                board.getSquare(i,Integer.parseInt(index[0])).getTile();
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
            for (Tile tile : tilesUsed) {
                board.setTile(Integer.parseInt(index[0]),Integer.parseInt(index[1]), tile);
            }
        }

    }

    public int letterToCoordinate(char letter) {
        int temp = (int)letter;
        int temp_integer = 64; //for upper case
        if(temp<=90 & temp>=65) {
            return (temp - temp_integer);
        }
        return -69;
    }

}
