import java.util.*;

import javax.security.auth.callback.LanguageCallback;

import java.io.*;
public class BoardTester {
    public static void main(String [] args)
    {
        Board b= new Board();
        b.setBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w");

        for(int j=1;j<10;j++){

            System.out.println("N : "+j);
          // try{
                System.out.println("Posistion "+positionTester(b, j ,Piece.WHITE));
           /*  }catch(NoPieceFoundException e){
                System.out.println(b);
                System.out.println(e.toString());
                System.exit(1);
            }*/
            

        }
    }
    public static long positionTester(Board b,int depth,boolean color)
    {
        if(depth==0){
            return 1;
        }
        long sum=0;
        //System.out.println("JJJJJJJ: "+depth);
        //System.out.println(b);
        Move [] moves = b.legalMoves(color);
        System.out.println(moves.length);
        for(int i=0;i<moves.length;i++){
            b.makeMove(moves[i]);
            sum+=positionTester(b,depth-1,!color);
            b.unMakeMove(moves[i]);
        }
        return sum;
    }
}
