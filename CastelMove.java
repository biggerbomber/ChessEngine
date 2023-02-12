public class CastelMove extends Move {
    
    public CastelMove(int start,int end)
    {
        super(start,end,false);
        rev=false;
    }
    public Move reverse(){
        CastelMove out = new CastelMove(end,start);
        out.rev=true;
        out.beforeMoved=false;
        return out;
    }
}
