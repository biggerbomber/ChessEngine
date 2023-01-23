import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.*;
public class Board
{
    public static String charSet;
    public static final String DEFAULT_STARTING_POSISTION ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w";
    private Piece table[];
    public boolean turn;
    public Board()
    {
        makeEmpty();
        setBoard(DEFAULT_STARTING_POSISTION);
    }
    public Board(String fen)
    {
        makeEmpty();
        setBoard(fen);
    }
    public void setBoard(String f)
    {
        makeEmpty();
        Scanner fen= new Scanner(f);
        fen.useDelimiter("/| ");
        int pos=0;
        while(fen.hasNext())
        {
            String row=fen.next();
           // System.out.println("ROW: "+row);
            if(!fen.hasNext()){
                turn=row.equals("w");
                continue;
            }
            for(int i=0;i<row.length();i++)
            {
                char c= row.charAt(i);
                if('1'<=c && c<='9')
                {
                    pos+=c-'0';
                    continue;
                }

                boolean col = Character.isUpperCase(c);
                c=Character.toLowerCase(c);

                switch(c){
                    case 'k':
                        table[pos]=new KingPiece(col);
                        break;
                    case 'q':
                        table[pos]=new QueenPiece(col);
                        break;
                    case 'b':
                        table[pos]= new BishopPiece(col);
                        break;
                    case 'n':
                        table[pos]= new KnightPiece(col);
                        break;
                    case 'r':
                        table[pos]= new RookPiece(col);
                        break;
                    case 'p':
                        table[pos]=new PawnPiece(col);
                        break;
                }
                pos++;

            }
        }
        fen.close();

    }
    public static int cordsToInt(int x,int y)
    {
        if(x<1||x>8||y<1||y>8){
            throw new InvalidSquareException();
        }
        return x-1+8*(7-y+1);
    }
    public static int[] intToCords(int n)
    {
        if(n<0||n>63){
            throw new InvalidSquareException();
        }
        int [] out =new int[2];
        out[0]=n%8+1;
        out[1]=8-n/8;
        return out;
    }
    public void makeEmpty()
    {        
        table= new Piece[64];
        try(FileReader file = new FileReader("settings.txt");
            Scanner settings = new Scanner(file))
        {
            
            while(settings.hasNextLine())
            {
                Scanner line = new Scanner(settings.nextLine());
                line.useDelimiter(":");
                String option = line.next();
                String value = line.next();
                line.close();
                if(option.equals("CharSet")){
                    charSet = value;
                }
            }
            checkStatus();

        }catch(NoSuchElementException e){
            throw new InvalidFormatSettingException();
        }catch(FileNotFoundException e){
            System.out.println("File settings not found");
            System.exit(1);
        }catch(IOException e){
            System.out.println("error" + e);
            System.exit(1);
        }
    }
    private void checkStatus()
    {
        if(charSet == null || charSet.length()<12){
            throw new InvalidFormatSettingException();
        }
    }
    public void makeMove(Move m)
    {
        if(table[m.start]==null){
            throw new NoPieceFoundException(); 
        }

        table[m.end]=table[m.start];
        table[m.start]=null;
    }

    public void unMakeMove(Move m)
    {
        makeMove(m.reverse());
    }

    public String toString()
    {
       // "━┃";
       // "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        //String out="\n\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n";
        String out="\n\n---------------------------------\n|";
        for(int i=0;i<table.length;i++)
        {
            if((i)%8==0 && i!=63 && i!=0){
                out+="\n---------------------------------\n|";
            }
            if(table[i]==null){
                out+=" "+" "+" |";
            }else{
                out+=" "+table[i]+" |";
            }
        }
       // out+="\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n";
        out+="\n---------------------------------\n\n";
        return out;
    }
    public Piece [] getState()
    {
        Piece [] out = new Piece[64];
        System.arraycopy(table, 0, out, 0, 64);
        return out;
    }
}
class NoPieceFoundException  extends RuntimeException{}
class InvalidSquareException extends RuntimeException{}
class InvalidFormatSettingException extends RuntimeException{}