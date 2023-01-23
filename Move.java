public class Move
{
    public final int start;
    public final int end;
    public Move(int start,int end)
    {
        this.start=start;
        this.end=end;
    }
    public Move reverse()
    {
        return new Move(end,start);
    }
}
