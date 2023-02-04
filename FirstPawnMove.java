public class FirstPawnMove extends Move {

    int prevEnPass=-1;
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
