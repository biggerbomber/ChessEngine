
public class Move
{
    public final int start;
    public final int end;
    public final boolean capture;
    public final Piece pieceCap;
    public boolean rev;
    public boolean beforeMoved=false;
    public boolean befMovcap;
    public Move(int start,int end)
    {
        this.start=start;
        this.end=end;
        capture=false;
        pieceCap=null;
        rev=false;
    }
    public Move(int start,int end,boolean bm)
    {
        this.start=start;
        this.end=end;
        capture=false;
        pieceCap=null;
        rev=false;
        beforeMoved=bm;
    }

    public Move(int start,int end,Piece c,boolean bmc)
    {
        this.start=start;
        this.end=end;
        capture=true;
        pieceCap=c;
        rev=false;
        befMovcap=bmc;
    }
    public Move(int start,int end,Piece c,boolean bm,boolean bmc)
    {
        this.start=start;
        this.end=end;
        capture=true;
        pieceCap=c;
        rev=false;
        beforeMoved=bm;
        befMovcap=bmc;
    }
    public Move reverse()
    {
        Move out;
        if(capture)
        {
            out=new Move(end,start,pieceCap,befMovcap);
        }else{
            out=new Move(end,start);
        }
        out.rev=true;
        out.beforeMoved=beforeMoved;
        return out;
    }
}
