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

        if(board.isEmptySquare(board.getSquare(parseInt(splittedChoice[1]), parseInt(splittedChoice[2])))){
            board.setTile(parseInt(splittedChoice[1]), parseInt(splittedChoice[2]), );
        }
    }
}
