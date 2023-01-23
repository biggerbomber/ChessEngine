public class Piece
{
    public static final boolean WHITE=true;
    public static final boolean BLACK=false;

    private final boolean color;

    public Piece(boolean color)
    {
        this.color=color;
    }

    public boolean isColor(boolean b)
    {
        return b==color;
    }

}
