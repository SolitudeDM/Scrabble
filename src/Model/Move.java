package Model;

import Model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class Move {

    private Game game;
    private Player player;
    private int score;
    private boolean valid;

    public Move(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public void options(String choice) {
        boolean moveMade = false;

        while (!moveMade) {
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

                    place(splittedChoice[1], orientation, splittedChoice[3], game.getBoard());
                    moveMade = true;
//FORMAT --> PLACE; H8; V; COCK
                    break;

                case ("SWAP"):
                    ArrayList<Tile> newHand = new ArrayList<>(player.getHand());
                    String[] swap = splittedChoice[1].split(" ");
                    int tilesToSwap = swap.length;
                    for(String letter : swap){
                        if (!player.getHand().contains(game.getTile(letter.charAt(0)))) {
                            System.out.println("You don't have this Tile!");
                            break;
                        } else {
                            player.getHand().remove(game.getTile(letter.charAt(0)));
                            newHand.remove(game.getTile(letter.charAt(0)));
//                            tilesToSwap++;
                        }
                    }

                    List<Tile> newTiles = game.getTileSack().subList(0, tilesToSwap);
                    newHand.addAll(newTiles);
                    game.getTileSack().removeAll(newTiles);
                    //game.getTileSack().add()
                    player.setHand(newHand);

                    moveMade = true;
                    break;

                case ("EXIT"):

                    break;

                default:
                    System.out.println("Invalid command " + splittedChoice[0]);
                    break;
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
    public void place(String coordinates, boolean vertical, String word, Board board) {


//       //  Check if word exists
//        if (!isValidWord(word)) {
//            // moveMade = true
//            return;
//        }

        // Revert coordinates of letter-number to number-number

        String[] index = coordinates.split("");
        if (index.length != 2) {
            index[1] = index[1] + index [2];
        }
        index[0] = String.valueOf(letterToCoordinate(index[0].charAt(0)));

        //Fix for the sliding coordinates problem
        index[0] =  String.valueOf(Integer.parseInt(index[0]) - 1);
        index[1] =  String.valueOf(Integer.parseInt(index[1]) - 1);

        //Fix for the mirrowed coordinates
        String tempCoordinate = index[0];
        index[0] = index[1];
        index[1] = tempCoordinate;

        String[] lettersUsed = (word.toUpperCase().split(""));
        ArrayList<Tile> tilesUsed = new ArrayList<>();

        // remove letters that are already on the board from the lettersUsed, so only the fresh ones will be counted
        for (String letter : lettersUsed) {

            tilesUsed.add(player.getGame().getTile(letter.charAt(0)));
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
        if (player.searchHand(tilesUsed)) {
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

    public int letterToCoordinate(char letter) {
        int temp = (int)letter;
        int temp_integer = 64; //for upper case
        if(temp<=90 & temp>=65) {
            return (temp - temp_integer);
        }
        return -69;
    }
}
