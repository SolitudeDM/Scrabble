/**
 * Represents a square on the board in the scrabble game
 * @author Mark Zhitchenko and Dani Mahaini*/
public class Square {

    /**
     * Variables of a square*/
    private int column;
    private int row;
    private Type type;
    private Tile tile;

    /**
     * Square constructor, creates a new square with a given type, index and tile
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

    /**
     * A setter for variable column*/
    public void setColumn(int column){
        this.column = column;
    }

    /**
     * A setter for variable row*/
    public void setRow(int row){
        this.row = row;
    }

    public Type getType() {
        return type;
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
            case TRIPPLE_LETTER:
                return ("3L");
            case DOUBLE_WORD:
                return ("2W");
            case TRIPPLE_WORD:
                return ("3W");
            default:
                return ("__");

        }
    }
}
