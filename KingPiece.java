public class KingPiece extends Piece 
{
    public KingPiece(boolean color)
    {
        super(color);    
    }
        
    public String toString()
    {
        return ""+Board.charSet.charAt((5+6*(isColor(BLACK)? 1:0)));
    }
    public double toScore()
    {
        return 0;
    } 
}
