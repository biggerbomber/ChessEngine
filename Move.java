
public class Move
{
    public final int start;
    public final int end;
    public final boolean capture;
    public final Piece pieceCap;
    public boolean rev;
    public Move(int start,int end)
    {
        this.start=start;
        this.end=end;
        capture=false;
        pieceCap=null;
        rev=false;
    }
    public Move(int start,int end,Piece c)
    {
        this.start=start;
        this.end=end;
        capture=true;
        pieceCap=c;
        rev=false;
    }
    public Move reverse()
    {
        Move out;
        if(capture)
        {
            out=new Move(end,start,pieceCap);
        }else{
            out=new Move(end,start);
        }
        out.rev=true;
        return out;
    }
}
