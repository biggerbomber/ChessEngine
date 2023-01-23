import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.sql.rowset.BaseRowSet;

import java.io.*;
import java.text.BreakIterator;
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
    public void place(Piece p, int n){
        table[n]=p;
    }
    public Move[] possiblePieceMove(int n)
    {
        Move[] out = new Move[20];
        int vSize=0;
        Piece p=table[n];
        if(p==null){
            return new Move[0];
        }
        int [] direzioni ={-9,-7,7,9,-8,1,8,-1};
        int [] modulo ={0,7,0,7,9,7,9,0};
        if(p instanceof QueenPiece)
        {
            for(int  d=0;d<direzioni.length;d++)
            {
                for(int i = n+direzioni[d];i>=0 && i<64 && (i-+direzioni[d])%8!=modulo[d];i+=direzioni[d]){
                    out=append(out,vSize++,new Move(n,i));
                    if(table[i]!=null)
                    {
                        break;
                    }  
                }
            }
        }else if(p instanceof RookPiece)
        {
            for(int d=4;d<direzioni.length;d++)
            {
                for(int i = n+direzioni[d];i>=0 && i<64 &&  (i-+direzioni[d])%8!=modulo[d];i+=direzioni[d]){
                    out=append(out,vSize++,new Move(n,i));
                    if(table[i]!=null)
                    {
                        break;
                    }  
                }
            }
        }
        else if(p instanceof BishopPiece)
        {
            for(int  d=0;d<4;d++)
            {
                for(int i = n+direzioni[d];i>=0 && i<64 && (i-+direzioni[d])%8!=modulo[d];i+=direzioni[d]){
                    out=append(out,vSize++,new Move(n,i));
                    if(table[i]!=null)
                    {
                        break;
                    }  
                }
            }
        }else if(p instanceof KingPiece){
            for(int  d=0;d<direzioni.length;d++)
            {
                for(int i = n+direzioni[d];i>=0 && i<64 && (i-+direzioni[d])%8!=modulo[d];){
                    out=append(out,vSize++,new Move(n,i));
                    break;
                }
            }

        }else if(p instanceof KnightPiece)
        {
            
        }
        out=resize(out, vSize);
        return out;
    }
    private Move[] append(Move [] arr,int vSize,Move m)
    {
        if(arr.length<=vSize){
            arr=resize(arr,vSize*2);
        }

        arr[vSize]=m;
        return arr;
    }
    private Move [] resize(Move[] arr,int newSize)
    {
        Move [] newVett= new Move[newSize];
		
		for(int i=0;i<arr.length && i<newSize;i++)
		{
			newVett[i]=arr[i];
		}
		return newVett;
    }
    
    public String toString()
    {
       
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