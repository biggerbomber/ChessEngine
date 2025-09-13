public class RookPiece extends Piece 
{
    public RookPiece(boolean color)
    {
        super(color);    
    }   
    public String toString()
    {
        return ""+Board.charSet.charAt((3+6*(isColor(BLACK)? 1:0)));
    }  
    public double toScore()
    {
        return 5000;
    } 
}
