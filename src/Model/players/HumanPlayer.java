package Model.players;

import Model.Board;
import Model.Game;
import Model.Move;
import Model.Tile;
import View.utils.ANSI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, Game game){
        super(name,game);

    }

    @Override
    public String determineMove(Board board) {

        Scanner keyboard = new Scanner(System.in);

        System.out.print(ANSI.WHITE_BRIGHT);
        System.out.println("      Please enter your choice using this format: ");
        System.out.print("To place a tile:");
        System.out.print(ANSI.PURPLE_BOLD_BRIGHT);
        System.out.print("PLACE;");
        System.out.print(ANSI.PURPLE_BOLD);
        System.out.print(" coordinates;");
        System.out.print(ANSI.BLUE_BOLD);
        System.out.print(" orientation;");
        System.out.print(ANSI.BLUE_BOLD_BRIGHT);
        System.out.print(" word;");

        System.out.print(ANSI.WHITE_BRIGHT);
        System.out.println();
        System.out.print("      To swap tiles: ");
        System.out.print(ANSI.PURPLE_BOLD_BRIGHT);
        System.out.print("SWAP;");
        System.out.print(ANSI.PURPLE_BOLD);
        System.out.print(" tile1 ");
        System.out.print(ANSI.BLUE_BOLD);
        System.out.print(" tile2 ");
        System.out.print(ANSI.BLUE_BOLD_BRIGHT);
        System.out.print(" tile3 ...");

        System.out.print(ANSI.WHITE_BRIGHT);
        System.out.println();
        System.out.print("              To exit the game: ");
        System.out.print(ANSI.RED_BOLD_BRIGHT);
        System.out.print("EXIT");
        System.out.println();
        System.out.print("                ");

            String choice = keyboard.nextLine();
            return choice;

    }
}
