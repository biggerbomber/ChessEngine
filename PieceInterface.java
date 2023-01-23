public interface PieceInterface
{
    boolean isColor(boolean c);
    boolean isReachableSquare(int pos);
    Move [] legalMoves();
}