package Model;

import Exceptions.EmptyCommandException;
import Exceptions.InvalidCommandException;
import Exceptions.WrongOrientationException;
import Model.players.HumanPlayer;
import Model.players.Player;
import View.utils.ANSI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private ArrayList<Tile> tileSack;

    // TileSack for searches
    private static final ArrayList<Tile> INITIAL_TILES = createTileSack();

    private ArrayList<Player> players;
    private Board board;
    private boolean finishGame;
    private ArrayList<String> usedCoordinates;

    /**
     * Game constructor creates an instance of a Game
     * @param players is the list of players of the Game
     * @param board is the board where the game will be played
     * @requires players.size() >= 2 && players.size() <= 4 && board != null
     * @ensures to create a Game instance*/
    public Game(ArrayList<Player> players, Board board) {
        this.players = players;
        this.board = board;
        tileSack = createTileSack();
        usedCoordinates = new ArrayList<>();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<String> getUsedCoordinates() {
        return usedCoordinates;
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

    public void setFinishGame(boolean finishGame){
        this.finishGame = finishGame;
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

    public ArrayList<Tile> getInitialTiles() {
        return INITIAL_TILES;
    }

    /**
     * This method hands out tiles for the players
     * @ensures that every player will have 7 tiles (if the size of the tileSack allows that)
     */
    public void handOut(){
        for(Player p : players){
            if (p.getHand().size() > 7) {
                return;
            }
            int missingTiles = 7 - p.getHand().size();
            if (missingTiles != 0 && tileSack.size() > missingTiles) {
                List<Tile> given = tileSack.subList(0,missingTiles);
                ArrayList<Tile> result = p.getHand();
                for (Tile tile : given) {
                    result.add(tile);
                }
                p.setHand(result);
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
        for (int i = 0; i < INITIAL_TILES.size(); i++){
            if(letter == INITIAL_TILES.get(i).getLetter()){
                return INITIAL_TILES.get(i);
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
        System.out.print(ANSI.BLUE_BOLD_BRIGHT);
        System.out.print(player.getName());
        for (int i = 0; i < 15 - player.getName().length(); i++) {
            System.out.print(" ");
        }
        System.out.print(ANSI.PURPLE);
        System.out.print("║");
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

        System.out.print(ANSI.PURPLE);
        System.out.print("║");

        System.out.print(ANSI.BLUE_BOLD_BRIGHT);

        System.out.print("    Score:");

        System.out.print(player.getScore());
        System.out.print(ANSI.PURPLE);

        System.out.println();
        System.out.print("               ╚═════");

        System.out.print(ANSI.PURPLE_UNDERLINED);
        System.out.print("Your Tiles");
        System.out.print(ANSI.RESET);
        System.out.print(ANSI.PURPLE);
        System.out.print("═════╝");
        System.out.println();
        System.out.print(ANSI.RESET);

    }


    /**
     * This method checks if the game is finished
     * @return true if the tileSack is empty and one of the player's hands is empty*/
    public boolean isFinished(){
        int skippers = 0;
        for(Player p : players){
            if(tileSack.size() == 0 && p.getHand().size() == 0){
                return true;
            }
            else if(finishGame){
                return true;
            }

            if (p.getSkips() >= 2) {
                skippers++;
            }
        }

        if (skippers == players.size()) {
            return  true;
        }


        boolean boardFull = false;
        //Check if board is filled
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board.getSquare(i,j).getTile() == null) {
                    return false;
                } else {
                    boardFull = true;
                }
            }
        }

        if (boardFull) {
            return true;
        }

        return false;
    }

    /**
     * This method determines the winner after the game is finished*/
    public String determineWinner(){
        if(isFinished()){

            Player privileged = null;
            boolean tieSituation = false;

            for (Player p : players) {
                if (p.getHand().size() == 0) {
                    privileged = p;
                    break;
                }
            }

            if (privileged != null) {
                int markup = 0;
                for (Player p : players) {
                    if (p != privileged) {
                        for (Tile t : p.getHand()) {
                            markup += t.getLetterPoints();
                        }
                    }
                }
            }

            //subtract score for tiles that remained in the hand at the end

            for (Player p : players) {
                for (Tile t : p.getHand()) {
                    p.setScore(p.getScore() - t.getLetterPoints());
                }
            }


            int highestScore = 0;
            Player tempWinner = players.get(0);

            for (Player player : players){
                if(player.getScore() > highestScore){
                    highestScore = player.getScore();
                    tempWinner = player;
                } else if (player.getScore() == highestScore){
                    tieSituation = true;
                }
            }

            if (tieSituation) {

                for (Player p : players) {
                    for (Tile t : p.getHand()) {
                        p.setScore(p.getScore() + t.getLetterPoints());
                    }
                }


                highestScore = 0;
                tempWinner = players.get(0);

                for (Player player : players){
                    if(player.getScore() > highestScore){
                        highestScore = player.getScore();
                        tempWinner = player;
                    }
                }
            }


            return "The winner is: " + tempWinner.getName() +  ". His score: " + highestScore;
        }
        return null;
    }

    /**
     * This method returns a String representation of player's tiles
     * @param player is the player whose tiles will be shown
     * @requires player != null && players.contains(player)
     * @ensures to return a String of the player's tiles
     * @return tiles of the player */
    public String tilesToString(Player player) {
        assert player != null;
        ArrayList<Tile> hand = player.getHand();

        String handString = "";
        handString += (ANSI.BLUE_BOLD_BRIGHT);
        handString += (player.getName());
        handString += (player.getName());
        for (int i = 0; i < 15 - player.getName().length(); i++) {
            handString +=(" ");
        }
        handString +=(ANSI.PURPLE);
        handString +=("║");
        handString +=(ANSI.RESET);
        for (int i = 0; i < hand.size(); i++) {
            handString +=(ANSI.YELLOW_BACKGROUND_BRIGHT);
            handString +=(ANSI.BLACK_BOLD);
            handString +=(hand.get(i).getLetter() + " ");
            handString +=(ANSI.RESET);
            if (i != 6) {
                handString +=(" ");
            }
        }

        handString +=(ANSI.PURPLE);
        handString +=("║");

        handString +=(ANSI.BLUE_BOLD_BRIGHT);

        handString +=("    Score:");

        handString +=(player.getScore());
        handString +=(ANSI.PURPLE);

        handString += "\n";
        handString +=("               ╚═════");

        handString +=(ANSI.PURPLE_UNDERLINED);

        handString +=("Your Tiles");
        handString +=(ANSI.RESET);
        handString +=(ANSI.PURPLE);
        handString +=("═════╝");
        handString += "\n";
        handString +=(ANSI.RESET);

        return handString;
    }


    public static void main(String[] args) throws EmptyCommandException, InvalidCommandException, WrongOrientationException {

        //Here we checked if the board will be printed properly, using the setBoard() and showBoard() methods
        Square[][] squares = new Square[15][15];
        Board board = new Board(squares);
        board.setBoard();
        board.showBoard();

        ArrayList<Player> players = new ArrayList<>();

        Game game = new Game(players, board);
        HumanPlayer player1 = new HumanPlayer("Player 1", game);
        HumanPlayer player2 = new HumanPlayer("Player 2", game);
        players.add(player1);
        players.add(player2);

        game.setPlayers(players);

        game.handOut();


        //here we start the local game(that was used for testing before we started implementing networking)
        while (!game.isFinished()) {
            int currentPlayer = 0;

            for (currentPlayer = 0; currentPlayer < players.size();) {
                game.showTiles(players.get(currentPlayer));
                players.get(currentPlayer).setMove(new Move(game, players.get(currentPlayer)));

                try {
                    players.get(currentPlayer).getMove().options(players.get(currentPlayer).determineMove(board));
                } catch (EmptyCommandException | WrongOrientationException | InvalidCommandException e) {
                    System.out.println(e.getMessage());
                }

                board.showBoard();

                if (players.get(currentPlayer).getMove().isMoveMade() || players.get(currentPlayer).getMove().isMoveLost()) {
                    currentPlayer++;
                }
                game.handOut();
                if(game.isFinished()){
                    break;
                }
            }

        }

        game.determineWinner();
    }
}
