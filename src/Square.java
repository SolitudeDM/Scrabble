public class Square {

    private int column;
    private int row;
    private Type type;
    private Tile tile;

    public Square(Type type, int column, int row, Tile tile){
        this.column = column;
        this.row = row;
        this.type = type;
        this.tile = tile;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}
