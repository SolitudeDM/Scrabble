package Model;

/**
 * Represents a square on the board in the scrabble game
 * @author Mark Zhitchenko and Dani Mahaini*/
public class Square implements Cloneable{

    /**
     * Variables of a square*/
    private int column;
    private int row;
    private Type type;
    private Tile tile;

    /**
     * Model.Square constructor, creates a new square with a given type, index and tile
//     * @param type is the type of the square
     * @param column is the column index of the square
     * @param row is the row index of the square*/
    public Square(int column, int row){
        this.column = column;
        this.row = row;
        this.tile = null;
    }

    /**
     * A setter for variable type*/
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * A setter for variable tile*/
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Type getType() {
        return type;
    }
    public Tile getTile(){
        return this.tile;
    }

    /**
     * This to String() method is used for printing the squares on the board*/
    @Override
    public String toString() {
        switch (getType()) {
            case CENTER:
                return ("CE");
            case DOUBLE_LETTER:
                return ("2L");
            case TRIPLE_LETTER:
                return ("3L");
            case DOUBLE_WORD:
                return ("2W");
            case TRIPLE_WORD:
                return ("3W");
        }

        if (String.valueOf(getTile()) == null) {
            return getTile() + " ";
        }

        return ("░░");
    }

    public Square clone() {
        return new Square(this.column, this.row);
    }
}
