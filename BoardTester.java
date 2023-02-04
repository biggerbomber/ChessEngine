
public class BoardTester {
    public static int maxDepth =2;
    public static void main(String [] args)
    {
        Board b= new Board();
        b.setBoard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 b");
        b.makeMove(new FirstPawnMove(Board.cordsToInt(7, 2),Board.cordsToInt(7, 2)));

        //System.out.println(b);
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

        Move [] moves = b.legalMoves(color);
        //System.out.println("DEPTH: "+depth +"\n"+b);
        for(int i=0;i<moves.length;i++){

            b.makeMove(moves[i]);  
            long temp=positionTester(b,depth-1,!color);
            sum+=temp;
            b.unMakeMove(moves[i]);

            if(depth==maxDepth){
                System.out.println(b.printMove(moves[i])+":"+temp);
            }
        }

        return sum;
    }
}
