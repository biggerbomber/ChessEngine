public class FirstPawnMove extends Move {

    public FirstPawnMove(int start,int end)
    {
        super(start, end);
    }
    public Move reverse()
    {
        return new FirstPawnMove(end,start);
    }
}
