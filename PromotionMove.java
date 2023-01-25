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
    public Move reverse()
    {
        PromotionMove out = new PromotionMove(end,start,piecePromoted);
        out.rev=true;
        return out;
    }
}
