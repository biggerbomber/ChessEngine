public class QueenPiece extends Piece 
{
    public QueenPiece(boolean color)
    {
        super(color);    
    }
    public String toString()
    {
        return ""+Board.charSet.charAt((4+6*(isColor(BLACK)? 1:0)));
    }  
    public double toScore()
    {
        return 9000;
    }   
}
