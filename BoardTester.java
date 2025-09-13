
public class BoardTester {
    public static int maxDepth =6;
    public static void main(String [] args)
    {
        Board b= new Board();
        b.setBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ");
        //b.makeMove(new Move(63, 7, b.table[7]));
        System.out.println(b);
       // System.out.println("Posistion "+positionTester(b,maxDepth ,b.turn,""));
        //System.out.println("Posistion "+Board.perft("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1",maxDepth));
    }
    public static long positionTester(Board b,int depth,boolean color,String movelist)
    {
        if(depth==0){
            return 1;
        }
        long sum=0;
        //Move [] kmov = b.possiblePieceMove(4);
       // Move [] movesp = b.possibleMove(color);
       // System.out.println(" P Moves: "+movesp.length);
        Move [] moves = b.legalMoves(color);
       // System.out.println("Depth :"+depth+" Moves: "+movelist);
       // for(Move m : kmov)
       // {
       //     System.out.println(b.printMove(m));
       // }
       // System.out.println("-----------------");
        for(int i=0;i<moves.length;i++){

            b.makeMove(moves[i]);  
            long temp=positionTester(b,depth-1,!color,movelist+"-"+b.printMove(moves[i]));
            sum+=temp;
            b.unMakeMove(moves[i]);

            if(depth==maxDepth){
                System.out.println(b.printMove(moves[i])+":"+temp);
            }
        }

        return sum;
    }
}
