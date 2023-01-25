public class CastelMove extends Move {
    
    public CastelMove(int start,int end)
    {
        super(start,end);
        rev=false;
    }
    public Move reverse(){
        CastelMove out = new CastelMove(end,start);
        out.rev=true;
        return out;
    }
}
