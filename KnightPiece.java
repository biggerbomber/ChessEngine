public class KnightPiece extends Piece 
{
    public KnightPiece(boolean color)
    {
        super(color);    
    } 
    public String toString()
    {
        return ""+Board.charSet.charAt((1+6*(isColor(BLACK)? 1:0)));
    }   
    public double toScore()
    {
        return 3000;
    } 
}
