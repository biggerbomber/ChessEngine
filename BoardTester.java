import java.util.*;
import java.io.*;
public class BoardTester {
    public static void main(String [] args)
    {
        Board b= new Board();
        b.setBoard("r3k2r/pp1n2pp/2p2q2/b2p1n2/BP1Pp3/P1N2P2/2PB2PP/R2Q1RK1 w");
        PrintWriter printWriter = new PrintWriter(System.out,true);
        printWriter.println(b);

        //b.makeMove(new Move(Board.cordsToInt(5,2),Board.cordsToInt(5,4)));
        //b.makeMove(new Move(Board.cordsToInt(5,7),Board.cordsToInt(5,5)));
        //printWriter.println(b);
    }
}
