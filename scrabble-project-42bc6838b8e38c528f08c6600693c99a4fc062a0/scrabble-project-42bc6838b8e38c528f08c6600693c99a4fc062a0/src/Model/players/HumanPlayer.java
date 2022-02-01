//package Model.players;
//
//import Model.Board;
//import Model.Game;
//import Model.Tile;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Scanner;
//
//import static java.lang.Integer.parseInt;
//
//public class HumanPlayer extends Player{
//
//    public HumanPlayer(String name, Game game){
//        super(name,game);
//    }
//
//
//    //SWAP
//
//    @Override
//    public String determineMove(Board board) {
//        // Create a Scanner object to read input.
//        Scanner keyboard = new Scanner(System.in);
//        boolean moveMade = false;
//
//        while (!moveMade) {
//
//            HashMap<String[], String> tileset = new HashMap<>();
//
//            System.out.println("enter your choice(command, row, col, letter)");
//
//            String choice = keyboard.nextLine();
//            String[] splittedChoice = choice.split(" ");
//
//            if (splittedChoice.length < 1) {
//                System.out.println("Empty command");
//                return null;
//            }
//
//            switch (splittedChoice[0].toUpperCase()) {
//
//                case ("PLACE"):
//
//                    ArrayList<Tile> lettersUsed = new ArrayList<>();
//                    for (int i = 1; i <= splittedChoice.length - 1; i = i + 3) {
//                        String[] Index = new String[2];
//
//                        Index[0] = String.valueOf(letterToCoordinate(splittedChoice[i].charAt(0)));
//                        Index[1] = splittedChoice[i + 1];
//                        tileset.put(Index, splittedChoice[i + 2]);
//
//                lettersUsed.add(this.getGame().getTile(splittedChoice[i + 2].charAt(0)));
//
//                        if (searchHand(lettersUsed)) {
//
//                        // Make two ArrayLists to store all the rows and columns that will be scanned for new, generated words...
//                        ArrayList<Integer> rowsInvolved = new ArrayList<>();
//                        ArrayList<Integer> colInvolved = new ArrayList<>();
//
//                        // Make a board copy to check if word was established using ALL of the letters used
//
//
//                        // Scan through the all the row and col coordinates in the hashmap
//                            for (String[] key : tileset.keySet()) {
//                                if (!rowsInvolved.contains(Integer.parseInt(key[1]))) {
//                                    rowsInvolved.add(Integer.parseInt(key[1]));
//                                }
//                                if (!colInvolved.contains(Integer.parseInt(key[2]))) {
//                                    colInvolved.add(Integer.parseInt(key[2]));
//                                }
//                            }
//
//                            //Horizontal check
//                            // Check wheteher new words had been established (make a new array of words that are used)
//                            // Remove the letters from the UsedLetters if they were involved in new words establishment
//                            for (int j = 0; j < rowsInvolved.size(); j++) {
//
//                            }
//
//                            //Vertical check
//                            // Remove the letters from the UsedLetters if they were involved in new words establishment
//
//
//
//                            moveMade = true;
//                        }
//                    }
//                    break;
//
//                case ("SWAP"):
//
//
//
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
//
////        if(board.isEmptySquare(board.getSquare(parseInt(splittedChoice[1]), parseInt(splittedChoice[2])))){
////            board.setTile(parseInt(splittedChoice[1]), parseInt(splittedChoice[2]), );
//
//        return null;
//    }
//
//
//
//    public int letterToCoordinate(char letter) {
//        int temp = (int)letter;
//        int temp_integer = 64; //for upper case
//        if(temp<=90 & temp>=65) {
//            return (temp - temp_integer);
//        }
//        return -69;
//    }
//}
