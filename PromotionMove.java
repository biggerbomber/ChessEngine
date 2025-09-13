public class PromotionMove extends Move {
    public static final int QUEEN=0;
    public static final int ROOK=1;
    public static final int BISHOP=2;
    public static final int KNIGHT=3;

    public int piecePromoted;
    public PromotionMove(int start,int end,int piece)
    {
        super(start,end);
        if(piece<QUEEN || piece >KNIGHT ){
            throw new IllegalArgumentException();
        }
        piecePromoted=piece;
        rev=false;
    }
    public PromotionMove(int start,int end,int piece,Piece cap,boolean bmc)
    {
        super(start,end,cap,bmc);
        if(piece<QUEEN || piece >KNIGHT ){
            throw new IllegalArgumentException();
        }
        piecePromoted=piece;
        rev=false;
    }
    public Move reverse()
    {
        PromotionMove out;
        if(!capture)
        {
            out = new PromotionMove(end,start,piecePromoted);
        }else{
            out = new PromotionMove(end,start,piecePromoted,pieceCap,befMovcap);
        }
        out.rev=true;
        out.prevEnPass=prevEnPass;
        return out;
    }
}
