import java.util.Scanner;
public class ChessGame {
    
    public static void main (String [] args)
    {
        if(args.length<1 || (!args[0].equals("1") && !args[0].equals("0")))
        {
            System.out.println("In riga di comando specificare la modalità di avvio:\n 0 - player vs player\n 1 - player vs bot(random)");
        }
        if(args[0].equals("0"))
        {
            humanGame();
        }else{
            botGame();
        }
    }
    public static void humanGame()
    {
        System.out.println("Inizializzazione della partita...");
        Board b = new Board();
        System.out.println(b);
        System.out.println(turno(b.turn)+" to move");
        System.out.print("Move?  <start><end> | r(resign)");
        Scanner input = new Scanner(System.in);
        while(input.hasNextLine())
        {
            String move = input.nextLine();
            if(move.equalsIgnoreCase("r")){
                System.out.println(turno(!b.turn)+" won by resignation");
                System.exit(0);
            }
            boolean invalid=true;
            int [] mov=null;
            Move [] leg = b.legalMoves(b.turn);
            while(invalid)
            {
                try
                {
                    mov =parseMove(move);
                }catch(RuntimeException e)
                {
                    System.out.println("invalid sintax or invalid start|end square, please try again");
                    break;
                }
            
                for(Move m : leg)
                {
                    if(m.start==mov[0]&&m.end==mov[1])
                    {
                        b.makeMove(m);
                        invalid=false;
                        break;
                    }
                }
                if(invalid)
                {
                    System.out.println("Illegal Move, please try again");
                    break;
                }
            }
            if(invalid)
            {
                continue;
            }
            b.turn=!b.turn;
            if(b.legalMoves(b.turn).length==0)
            {
                System.out.println(b);
                if(b.inCheck(b.turn))
                {
                    System.out.println(turno(!b.turn)+" won by checkmate");
                }else{
                    System.out.println("draw by stalemate");
                }
                System.exit(0);
            }
            System.out.println(b);
            System.out.println(turno(b.turn)+" to move");
        }


    }
    public static int [] parseMove(String s)
    {
        if(s.length()!=4)
        {
            throw new IllegalArgumentException();
        }
        int [] out = new int[2];
        out[0]=Board.cordsToInt(s.charAt(0)-'a'+1, s.charAt(1)-'0');
        out[1]=Board.cordsToInt(s.charAt(2)-'a'+1, s.charAt(3)-'0');
        return out;
    }
    public static String turno(boolean color)
    {
        return color?"White":"Black";
    }
    public static void botGame()
    {
        System.out.println("Inizializzazione della partita...");
        Board b = new Board();
        System.out.println(b);
        System.out.println(turno(b.turn)+" to move");
        System.out.print("Move?  <start><end> | r(resign)");
        Scanner input = new Scanner(System.in);
        while(b.turn==Piece.BLACK || input.hasNextLine())
        {
            if(b.turn==Piece.WHITE)
            {
                String move = input.nextLine();
                if(move.equalsIgnoreCase("r")){
                    System.out.println(turno(!b.turn)+" won by resignation");
                    System.exit(0);
                }
                boolean invalid=true;
                int [] mov=null;
                Move [] leg = b.legalMoves(b.turn);
                while(invalid)
                {
                    try
                    {
                        mov =parseMove(move);
                    }catch(RuntimeException e)
                    {
                        System.out.println("invalid sintax or invalid start|end square, please try again");
                        break;
                    }
                
                    for(Move m : leg)
                    {
                        if(m.start==mov[0]&&m.end==mov[1])
                        {
                            b.makeMove(m);
                            invalid=false;
                            break;
                        }
                    }
                    if(invalid)
                    {
                        System.out.println("Illegal Move, please try again");
                        break;
                    }
                }
                if(invalid)
                {
                    continue;
                }
            }else
            {
                Move [] move = b.legalMoves(b.turn);
                int i= (int) (move.length*Math.random());
                b.makeMove(move[i]);
            }
            b.turn=!b.turn;
            if(b.legalMoves(b.turn).length==0)
            {
                System.out.println(b);
                if(b.inCheck(b.turn))
                {
                    System.out.println(turno(!b.turn)+" won by checkmate");
                }else{
                    System.out.println("draw by stalemate");
                }
                System.exit(0);
            }
            System.out.println(b);
            System.out.println(turno(b.turn)+" to move");
        }
    }

}
