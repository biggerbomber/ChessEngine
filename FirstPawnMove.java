public class FirstPawnMove extends Move {

    
    public FirstPawnMove(int start,int end)
    {
        super(start, end);
    }
    public Move reverse()
    {
        FirstPawnMove out = new FirstPawnMove(end,start);
        out.rev=true;
        out.prevEnPass=prevEnPass;
        return out;
    }
}
