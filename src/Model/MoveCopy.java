//package Model;
//
//import Model.players.Player;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * This class represents a move in Scrabble game
// * @author Mark Zhitchenko and Dani Mahaini*/
//public class MoveCopy {
//
//    private Game game;
//    private Player player;
//    private int score;
//    private boolean valid;
//
//    public MoveCopy(Game game, Player player) {
//        this.game = game;
//        this.player = player;
//    }
//
//    public void options(String choice) {
//        boolean moveMade = false;
//
//        while (!moveMade) {
//            String[] splittedChoice = choice.split("; ");
//
//            if (splittedChoice.length < 1) {
//                System.out.println("Empty command");
//            }
//
//            switch (splittedChoice[0].toUpperCase()) {
//
//                case ("PLACE"):
//
//                    boolean orientation;
//
//                    if ((splittedChoice[2].equals("V"))) {
//                        orientation = true;
//                    } else if (splittedChoice[2].equals("H")) {
//                        orientation = false;
//                    } else {
//                        System.out.println("POSHEL V ZHEPU");
//                        break;
//                    }
//
//                    place(splittedChoice[1], orientation, splittedChoice[3], game.getBoard());
//                    moveMade = true;
////FORMAT --> PLACE; H8; V; WORD
//                    break;
//
//                case ("SWAP"):
////FORMAT --> SWAP; A B C D
//                    ArrayList<Tile> newHand = new ArrayList<>(player.getHand());
//                    String[] swap = splittedChoice[1].split(" ");
//                    ArrayList<Tile> swappedTiles = new ArrayList<>();
//                    int tilesToSwap = 0;
//                    boolean letterDoesNotBelong = true;
//
//                    for(String letter : swap){
//                        letterDoesNotBelong = true;
//                        swappedTiles.add(game.getTile(letter.charAt(0)));
//
//                        for (int i = 0; i < player.getHand().size() && letterDoesNotBelong; i++) {
//                            if (!(player.getHand().get(i).getLetter() == letter.charAt(0))) {
//                                letterDoesNotBelong = true;
//                            } else {
//                                for (int j = 0; j < newHand.size(); j++) {
//                                    if (newHand.get(j).getLetter() == letter.charAt(0)) {
//                                        newHand.remove(j);
//                                    }
//                                }
////                                newHand.remove(game.getTile(letter.charAt(0)));
//                                tilesToSwap++;
//                                letterDoesNotBelong = false;
//                            }
//                        }
//                    }
//
//                    List<Tile> newTiles = game.getTileSack().subList(0, tilesToSwap);
//                    newHand.addAll(newTiles);
//
//                    ArrayList<Tile> tileSackCopy = new ArrayList<>(game.getTileSack());
//                    tileSackCopy.removeAll(newTiles);
//                    tileSackCopy.addAll(swappedTiles);
//                    game.setTileSack(tileSackCopy);
//
//                    player.setHand(newHand);
//
//                    moveMade = true;
//                    break;
//
//                case ("EXIT"):
//
//                    break;
//
//                default:
//                    System.out.println("Invalid command " + splittedChoice[0]);
//                    break;
//            }
//        }
//    }
//
//    /**
//     * This method is responsible for placing a word on the board
//     * @param coordinates are the coordinates of the required square
//     * @param vertical is used to see whether the word should be set vertically or horizontally
//     * @param word is the word that is going to be placed
//     * @param board is the board where the word is going to be placed at
//     * @requires all the parameter != null
//     * @ensures to put the given word to the board*/
//    public void place(String coordinates, boolean vertical, String word, Board board) {
//
//
////       //  Check if word exists
////        if (!isValidWord(word)) {
////            // moveMade = true
////            return;
////        }
//
//        // Revert coordinates of letter-number to number-number
//
//        String[] index = coordinates.split("");
//        if (index.length != 2) {
//            index[1] = index[1] + index [2];
//        }
//        index[0] = String.valueOf(letterToCoordinate(index[0].charAt(0)));
//
//        //Fix for the sliding coordinates problem
//        index[0] =  String.valueOf(Integer.parseInt(index[0]) - 1);
//        index[1] =  String.valueOf(Integer.parseInt(index[1]) - 1);
//
//        //Fix for the mirrowed coordinates
//        String tempCoordinate = index[0];
//        index[0] = index[1];
//        index[1] = tempCoordinate;
//
//        String[] lettersUsed = (word.toUpperCase().split(""));
//        ArrayList<Tile> tilesUsed = new ArrayList<>();
//
//        // remove letters that are already on the board from the lettersUsed, so only the fresh ones will be counted
//        for (String letter : lettersUsed) {
//
//            tilesUsed.add(player.getGame().getTile(letter.charAt(0)));
//        }
//
//        // Remove all the existing letters from "lettersUsed" horizontal
//        if (!vertical) {
//            for (int i = Integer.parseInt(index[1]); i < word.length(); i++) {
////                wordScore += (board.getSquare(i,Integer.parseInt(index[0])).getTile().getLetterPoints()) *board.getSquare(i,Integer.parseInt(index[0])).getType()
//
//                tilesUsed.remove(board.getSquare(i,Integer.parseInt(index[0])).getTile());
//            }
//        }
//
//        // Remove all the existing letters from "lettersUsed" vertical
//        if (vertical) {
//            for (int i = Integer.parseInt(index[0]); i < word.length(); i++) {
//                tilesUsed.remove(board.getSquare(i,Integer.parseInt(index[1])).getTile());
//            }
//        }
//
//        //ALL THAT IS LEFT TO DO IS TO INSERT THE LETTERS AND SUM UP THE POINTS
//        if (player.searchHand(tilesUsed)) {
//            if(vertical) {
//                int i = 0;
//                while (i < word.length()) {
//                    for (Tile tile : tilesUsed) {
//                        board.setTile(Integer.parseInt(index[0]) + i, Integer.parseInt(index[1]), tile);
//                        i++;
//                    }
//                }
//            }
//
//            if(!vertical) {
//                int i = 0;
//                while (i < word.length()) {
//                    for (Tile tile : tilesUsed) {
//                        board.setTile(Integer.parseInt(index[0]), Integer.parseInt(index[1]) + i, tile);
//                        i++;
//                    }
//                }
//
////                board.setTile(Integer.parseInt(index[0]),Integer.parseInt(index[1]), tile);
//            }
//        }
//
//    }
//
//    /**
//     * This method converts the letter index to numeric index using ASCII
//     * @requires letter != null
//     * @param letter is the letter that is going to be represented as a number
//     * @ensures to return an integer value of an alphabetic letter*/
//    public int letterToCoordinate(char letter) {
//        int temp = (int)letter;
//        int temp_integer = 64; //for upper case
//        if(temp<=90 & temp>=65) {
//            return (temp - temp_integer);
//        }
//        return -69;
//    }
//}
