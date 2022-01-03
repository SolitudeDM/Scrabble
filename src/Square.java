public class Square {

    public enum Type{
        TRIPPLE_WORD, DOUBLE_WORD, CENTER, TRIPPLE_LETTER, DOUBLE_LETTER, NORMAL
    }

    private int column;
    private int row;
    private Type type;

    public Square(Type type, int column, int row){
        this.column = column;
        this.row = row;
        this.type = type;
    }

}
