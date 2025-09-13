import java.util.Hashtable;

public class ChessEngine {
    public static class dpElement{
        public Double eval;
        public Integer depth;
        public dpElement(Double e,Integer d)
        {
            eval=e;
            depth=d;
        }
    }
    static int trans=0;
    static Hashtable<Integer,dpElement> dp;
    public static Move bestMove(Board b, Move [] moves,int depth,boolean turn)
    {
        //if(dp==null){
        dp=new Hashtable<Integer,dpElement>();
        //}
        double bestEval=Double.MAX_VALUE;
        int bestI=0;
        for(int i=0;i<moves.length;i++)
        {
            b.makeMove(moves[i]);
            double eval = alphaBetaMax(b,-Double.MAX_VALUE,Double.MAX_VALUE,depth-1,!turn);
            b.unMakeMove(moves[i]);
            //System.out.println("TRANSIT EVL: "+eval);
            if(eval<bestEval)
            {
                bestEval=eval;
                bestI=i;
            }
        }
        System.out.println("EVAL: "+bestEval);
        System.out.println("TRANS : "+trans);
        return moves[bestI];
    }
    private static double alphaBetaMax(Board b,double alpha, double beta,int depth,boolean turn)
    {
        //System.out.println("HASH : "+b.hashCode());
        if(depth==0)
        {
            return evaluate(b);
        }
        if(dp.containsKey(b.hashCode()))
        {

            if(!(depth>dp.get(b.hashCode()).depth)){
               // trans++;
               // System.out.println("Trans "+trans);
                return dp.get(b.hashCode()).eval;
            }
        }
        Move [] moves = b.legalMoves(turn);
        if(moves.length==0)
        {
            //System.out.println("DAnger");
            return (b.inCheck(turn)?-Double.MAX_VALUE:0);
        }
        for(int i=0;i<moves.length;i++)
        {
            b.makeMove(moves[i]);
            double eval = alphaBetaMin(b,alpha,beta,depth-1,!turn);
            b.unMakeMove(moves[i]);
            if(eval>=beta)
            {
                return beta;
            }
            if(eval>alpha)
            {
                //System.out.println("SEtted Alpha :" + alpha+" depth: "+ depth);
                alpha=eval;
            }

        }
        dp.remove(b.hashCode());
        dp.put(b.hashCode(),new dpElement(alpha,depth));
        return alpha;
    }
    private static double alphaBetaMin(Board b,double alpha, double beta,int depth,boolean turn)
    {
        //System.out.println("HASH : "+b.hashCode()+" Depth "+ depth);
        if(depth==0)
        {
            return evaluate(b);
        }
        if(dp.containsKey(b.hashCode()))
        {
            if(!(depth>dp.get(b.hashCode()).depth)){
                //trans++;
                //System.out.println("Trans "+trans);
                return dp.get(b.hashCode()).eval;
            }
        }
        Move [] moves = b.legalMoves(turn);
        if(moves.length==0)
        {
            //System.out.println("DAnger");
            return (b.inCheck(turn)?Double.MAX_VALUE:0);
        }
        for(int i=0;i<moves.length;i++)
        {
            b.makeMove(moves[i]);
            double eval = alphaBetaMax(b,alpha,beta,depth-1,!turn);
            b.unMakeMove(moves[i]);
            if(eval<=alpha)
            {
                return alpha;
            }
            if(eval<beta)
            {
                //System.out.println("SEtted beta :" + beta+ " depth: " + depth);
                beta=eval;
            }

        }
        dp.remove(b.hashCode());
        dp.put(b.hashCode(),new dpElement(beta,depth));
        return beta;
    }
    public static double evaluate(Board b)
    {
        double score=0;
        Piece [] p = b.getState();
        for(int i=0;i<p.length;i++)
        {
            if(p[i]!=null)
            {
                score+=p[i].toScore()*(p[i].color()?1:-1);
            }
        }

        return score;
    }
}
