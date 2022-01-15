package Model.players;

import Model.Board;
import Model.Game;
import Model.Move;
import Model.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class HumanPlayer_v3 extends Player {

    private Move move;

    public HumanPlayer_v3(String name, Game game){
        super(name,game);
        this.move = new Move(this.getGame(), this);
    }

    public Move getMove() {
        return move;
    }

    @Override
    public String determineMove(Board board) {

        Scanner keyboard = new Scanner(System.in);
        
            System.out.println("enter your choice(command; coordinates; orientation; word)");

            String choice = keyboard.nextLine();
            return choice;

    }
}
