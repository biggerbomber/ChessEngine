import java.util.*;
import java.io.*;
public class BoardTester {
    public static void main(String [] args)
    {
        Board b= new Board();
        b.setBoard("8/8/8/8/8/8/8/8 w");
        PrintWriter printWriter = new PrintWriter(System.out,true);
        int [] squareTester = {0,1,6,7,8,35,48,55,56,63};
        for(int j=0;j<squareTester.length;j++){
            b.place(new QueenPiece(true),squareTester[j]);
            Move [] moves = b.possiblePieceMove(squareTester[j]);
            for(int i=0;i<moves.length;i++){
                b.place(new Piece(false),moves[i].end);
            }
            //b.makeMove(new Move(Board.cordsToInt(5,2),Board.cordsToInt(5,4)));
            //b.makeMove(new Move(Board.cordsToInt(5,7),Board.cordsToInt(5,5)));
            printWriter.println(b);
            b.makeEmpty();
        }
    }
}
