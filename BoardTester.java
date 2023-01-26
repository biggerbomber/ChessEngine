
public class BoardTester {
    public static int maxDepth =2;
    public static void main(String [] args)
    {
        Board b= new Board();
        b.setBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w");


          // try{
                System.out.println("Posistion "+positionTester(b,maxDepth  ,b.turn));
           /*  }catch(NoPieceFoundException e){
                System.out.println(b);
                System.out.println(e.toString());
                System.exit(1);
            }*/
            

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
        //System.out.println(moves.length);
        for(int i=0;i<moves.length;i++){
            b.makeMove(moves[i]);  
            long temp=positionTester(b,depth-1,!color);
            sum+=temp;
            b.unMakeMove(moves[i]);

            if(depth==maxDepth){
                System.out.println(b.printMove(moves[i])+" : "+temp);
            }
        }

        return sum;
    }
}
