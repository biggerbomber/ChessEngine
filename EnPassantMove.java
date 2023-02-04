public class EnPassantMove extends Move {
    
    public EnPassantMove(int start,int end,Piece p)
    {
        super(start,end,p);
    }
    public Move reverse()
    {
        EnPassantMove out;
       
        out=new EnPassantMove(end,start,pieceCap);
        
        out.rev=true;
        return out;
    }
}
