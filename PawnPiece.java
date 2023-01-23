public class PawnPiece extends Piece 
{
    public PawnPiece(boolean color)
    {
        super(color);    
    }    
    public String toString()
    {
        return ""+Board.charSet.charAt((0+6*(isColor(BLACK)? 1:0)));
    }
}
