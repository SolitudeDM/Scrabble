package Model;

import Exceptions.EmptyCommandException;
import Exceptions.InvalidCommandException;
import Exceptions.WrongOrientationException;
import Model.players.HumanPlayer_v3;
import Model.players.Player;
import View.utils.ANSI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Game {



    private ArrayList<Tile> tileSack;
    // Tilesack for searches
    private final ArrayList<Tile> initialTiles;
    private ArrayList<Player> players;
    private Board board;

    //private HashMap<Player, ArrayList<Tile>> hands;

    /**
     * Game constructor creates an instance of a Game
     * @param players is the list of players of the Game
     * @param board is the board where the game will be played
     * @requires players.size() >= 2 && players.size() <= 4 && board != null
     * @ensures to create a Game instance*/
    public Game(ArrayList<Player> players, Board board) {
        assert players.size() >= 2 && players.size() <= 4 && board != null;
        this.players = players;
        this.board = board;
        tileSack = createTileSack();
        initialTiles = createTileSack();
    }



    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Tile> getTileSack(){
        return tileSack;
    }

    public void setTileSack(ArrayList<Tile> tileSack){
        this.tileSack = tileSack;
    }

    /**
     * This method creates,fills and shuffles a sack with tiles
     * @ensures to return a complete tileSack
     * @return Arraylist of tiles with the right quantity*/
    public static ArrayList<Tile> createTileSack() {
        //this is a list of letters without duplicates (quantity not counted yet)
        ArrayList<Tile> tileSack = new ArrayList<>();

        tileSack.add(new Tile('A', 1, 9));
        tileSack.add(new Tile('B', 3, 2));
        tileSack.add(new Tile('C', 3, 2));
        tileSack.add(new Tile('D', 2, 4));
        tileSack.add(new Tile('E', 1, 12));
        tileSack.add(new Tile('F', 4, 2));
        tileSack.add(new Tile('G', 2, 2));
        tileSack.add(new Tile('H', 4, 2));
        tileSack.add(new Tile('I', 1, 8));
        tileSack.add(new Tile('J', 8, 2));
        tileSack.add(new Tile('K', 5, 2));
        tileSack.add(new Tile('L', 1, 4));
        tileSack.add(new Tile('M', 3, 2));
        tileSack.add(new Tile('N', 1, 6));
        tileSack.add(new Tile('O', 1, 8));
        tileSack.add(new Tile('P', 3, 2));
        tileSack.add(new Tile('Q', 10, 1));
        tileSack.add(new Tile('R', 1, 6));
        tileSack.add(new Tile('S', 1, 4));
        tileSack.add(new Tile('T', 1, 6));
        tileSack.add(new Tile('U', 1, 4));
        tileSack.add(new Tile('V', 4, 2));
        tileSack.add(new Tile('W', 4, 2));
        tileSack.add(new Tile('X', 8, 1));
        tileSack.add(new Tile('Y', 4, 2));
        tileSack.add(new Tile('Z', 10, 1));
        tileSack.add(new Tile(' ', 0, 2));

        //this list has all the letters with the right amount of duplicates
        ArrayList<Tile> completeTileSack = new ArrayList<>();

        for(Tile t : tileSack){
            for(int i = 0; i < t.getLetterQuantity(); i++){
                completeTileSack.add(new Tile(t.getLetter(), t.getLetterPoints(), 0));
            }
        }

        Collections.shuffle(completeTileSack);
        return completeTileSack;
    }

    /**
     * This method hands out tiles for the players
     * @ensures that every player will have 7 tiles (if the size of the tileSack allows that)*/
    public void handOut(){
        for(Player p : players){
            int missingTiles = 7 - p.getHand().size();
            if (missingTiles != 0 && tileSack.size() > missingTiles) {
                List<Tile> given = tileSack.subList(0,missingTiles);
//                hands = new HashMap<>();
//                hands.put(p, new ArrayList<>(given));
                ArrayList<Tile> result = p.getHand();
                for (Tile tile : given) {
                    result.add(tile);
                }
                p.setHand(result);
//                p.setHand(new ArrayList<>(given));
                tileSack.removeAll(given);
            }
            else if(missingTiles != 0 && tileSack.size() < missingTiles && tileSack.size() != 0){
                int remainingTiles = tileSack.size();
                List<Tile> given2 = tileSack.subList(0, remainingTiles);
                ArrayList<Tile> newHand = p.getHand();
                for(Tile t : given2){
                    newHand.add(t);
                }
                p.setHand(newHand);
                tileSack.removeAll(given2);
            }
            else if(tileSack.size() == 0){
                System.out.println("The tileSack is empty, the game is almost finished!");
            }
        }
    }

    /**
     * This method return a Model.Tile based on its letter
     * @requires letter != null
     * @param letter is the letter we want to get a Model.Tile for
     * @ensures to return the right Model.Tile
     * @return Model.Tile with required letter
     */
    public Tile getTile(char letter){
        for (int i = 0; i < initialTiles.size(); i++){
            if(letter == initialTiles.get(i).getLetter()){
                return initialTiles.get(i);
            }
        }
        return null;
    }

    /**
     * This method finds players hand and prints all the tiles he has
     * @param player is the player we want to show tiles to
     * @requires player != null
     * @requires player to be included in the players array of the game
     * @ensures to print all the tiles this player has*/
    public void showTiles(Player player){
        assert player != null;
        ArrayList<Tile> hand = player.getHand();
//        System.out.println();
        System.out.print(ANSI.PURPLE);
        System.out.print("               ║");
        System.out.print(ANSI.RESET);
        for (int i = 0; i < hand.size(); i++) {
            System.out.print(ANSI.YELLOW_BACKGROUND_BRIGHT);
            System.out.print(ANSI.BLACK_BOLD);
            System.out.print(hand.get(i).getLetter() + " ");
            System.out.print(ANSI.RESET);
            if (i != 6) {
                System.out.print(" ");
            }
        }
//        for (Tile tile : hand) {
//            System.out.print(ANSI.YELLOW_BACKGROUND_BRIGHT);
//            System.out.print(ANSI.BLACK_BOLD);
//            System.out.print(tile.getLetter() + " ");
//            System.out.print(ANSI.RESET);
//            System.out.println(" ");
//        }
        System.out.print(ANSI.PURPLE);
        System.out.print("║");
        System.out.println();
        System.out.print("               ╚═════Your Tiles═════╝");
        System.out.println();
        System.out.print(ANSI.RESET);


//        for(Player p : hands.keySet()){
//            if (p.equals(player)){
//
//                for(Tile t : hands.get(p)){
//                    System.out.print(t.getLetter() + " ");
//                }
//                System.out.println();
//            }
        }


        /**
         * This method checks if the game is finished
         * @return true if the tileSack is empty and one of the player's hands is empty*/
        public boolean isFinished(){
            for(Player p : players){
                if(tileSack.size() == 0 && p.getHand().size() == 0){
                    return true;
                }
//                if(p.determineMove(board).equals("EXIT")){
//                    return true;
//                }
            }
            return false;
        }

        /**
         * This method determines the winner after the game is finished*/
        public void determineWinner(){
            if(isFinished()){
                int highestScore = 0;
                Player tempWinner = players.get(0);
                for (Player player : players){
                    if(player.getScore() > highestScore){
                        highestScore = player.getScore();
                        tempWinner = player;
                    }
                }
                System.out.println("The winner is: " + tempWinner.getName() +  ". His score: " + highestScore);
            }
        }

    public static void main(String[] args) throws EmptyCommandException, InvalidCommandException, WrongOrientationException {

        //Here we checked if the board will be printed properly, using the setBoard() and showBoard() methods
        Square[][] squares = new Square[15][15];
        Board board = new Board(squares);
        board.setBoard();
        board.showBoard();

        ArrayList<Player> players = new ArrayList<>();

        Game game = new Game(players, board);
        HumanPlayer_v3 player1 = new HumanPlayer_v3("Boris", game);
        HumanPlayer_v3 player2 = new HumanPlayer_v3("Viktor", game);
        players.add(player1);
        players.add(player2);

        game.setPlayers(players);

        game.handOut();



        while (!game.isFinished()) {
            int currentPlayer = 0;

            for (currentPlayer = 0; currentPlayer < players.size(); currentPlayer++) {
                game.showTiles(players.get(currentPlayer));
                players.get(currentPlayer).setMove(new Move(game, players.get(currentPlayer)));
                players.get(currentPlayer).getMove().options(players.get(currentPlayer).determineMove(board));
                board.showBoard();
//                players.get(currentPlayer).getMove().calculateScore();
                game.handOut();
            }

        }

//        game.showTiles(player1);
//
//
//        player1.getMove().options(player1.determineMove(board));

//        board.showBoard();
//        game.showTiles(player1);
//        game.handOut();
//        game.showTiles(player1);
        /*Here we checked if the tileSack is created properly(commented out because we don't need the createTileSack() method to be static
        made it static only for the testing
         */
//        ArrayList<Model.Tile> test = createTileSack();
//        System.out.println(test);
    }
}
