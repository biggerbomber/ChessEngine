public class Piece
{
    public static final boolean WHITE=true;
    public static final boolean BLACK=false;
    public boolean moved; 

    private final boolean color;

    public Piece(boolean color)
    {
        this.color=color;
        moved=false;
    }
    public void moved(){
        moved=true;
    }
    public boolean isMoved()
    {
        return moved;
    }
    public boolean isColor(boolean b)
    {
        return b==color;
    }
    public boolean color()
    {
        return color;
    }
    public String toString()
    {
        return "x";
    }
}
