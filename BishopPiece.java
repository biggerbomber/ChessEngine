public class BishopPiece extends Piece 
{
    public BishopPiece(boolean color)
    {
        super(color);    
    } 
    public String toString()
    {
        return ""+Board.charSet.charAt((2+6*(isColor(BLACK)? 1:0)));
    }
    public double toScore()
    {
        return 3000;
    }   
}
