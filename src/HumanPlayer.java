import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class HumanPlayer extends Player{

    public HumanPlayer(String name){
        super(name);
    }

    @Override
    public String determineMove(Board board) {
        // Create a Scanner object to read input.
        Scanner keyboard = new Scanner(System.in);
        System.out.println("enter your choice(command, row, col, letter)");

        String choice = keyboard.nextLine();
        String[] splittedChoice = choice.split(" ");

        if (splittedChoice.length < 1) {
            System.out.println("Empty command");
            return null;
        }

        switch (splittedChoice[0].toUpperCase()) {

            case ("PLACE"):

                HashMap<String[], String> tileset = new HashMap<>();
                ArrayList<Tile> lettersUsed = new ArrayList<>();

            for (int i = 1; i <= splittedChoice.length - 1; i = i + 3) {
                String[] Index = new String[2];
                // add method to convert letter to position in the alphabet
                Index[0] = splittedChoice[i];
                Index[1] = splittedChoice[i + 1];
                tileset.put(Index, splittedChoice[i + 2]);

//                lettersUsed.add(getTile(splittedChoice[i + 2]));

            }



                break;

            case ("SWAP"):

                break;

            case ("EXIT"):

                break;

            default:
                System.out.println("Invalid command " + splittedChoice[0]);
                break;
        }

//        if(board.isEmptySquare(board.getSquare(parseInt(splittedChoice[1]), parseInt(splittedChoice[2])))){
//            board.setTile(parseInt(splittedChoice[1]), parseInt(splittedChoice[2]), );

    }
}
