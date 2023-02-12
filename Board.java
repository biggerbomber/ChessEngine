import java.util.NoSuchElementException;
import java.util.Scanner;

import java.io.*;
public class Board
{
    public static String charSet;
    public static final String DEFAULT_STARTING_POSISTION ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public int enPassantSquare=-10;
    public Piece table[];
    public boolean turn;
    public boolean [] rightCastle;
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
    public String printMove(Move m){
        return (char)('a'-1+intToCords(m.start)[0])+""+intToCords(m.start)[1]+(char)('a'-1+intToCords(m.end)[0])+""+(intToCords(m.end)[1]);
        //return table[m.start]+" : "+(char)('a'-1+intToCords(m.start)[0])+""+(intToCords(m.start)[1]+"->"+(char)('a'-1+intToCords(m.end)[0])+""+(intToCords(m.end)[1]));
    }
    public void setBoard(String f)
    {
        makeEmpty();
        //Scanner fen= new Scanner(f);
        //fen.useDelimiter("/| ");
        //int pos=0;
        String [] boardFen = f.split(" ");
        if(boardFen.length<4)
        {
            throw new InvalidFormatFenException();
        }

        //pezzi
        Scanner rows = new Scanner(boardFen[0]);
        rows.useDelimiter("/");
        int pos=0;
        while(rows.hasNext())
        {
            
            String row=rows.next();
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

        //turno
        if(!(boardFen[1].equals("w")||boardFen[1].equals("b")))
        {
            throw new InvalidFormatFenException();
        }
        turn=boardFen[1].equals("w");


        //castello
        if(boardFen[2].contains("K"))
        {
            rightCastle[0]=true;
        }
        if(boardFen[2].contains("Q"))
        {
            rightCastle[1]=true;
        }
        if(boardFen[2].contains("k"))
        {
            rightCastle[2]=true;
        }   
        if(boardFen[2].contains("q"))
        {
            rightCastle[3]=true;
        }


        // en passant square
        enPassantSquare=-10;
        if(!boardFen[3].contains("-"))
        {
            char row = boardFen[3].charAt(0);
            char colum = boardFen[3].charAt(1);
            if(row<'a'||row>'h'||colum<'1'||colum>'8')
            {
                throw new InvalidFormatFenException();
            }
            enPassantSquare = Board.cordsToInt(row-'a', colum-'0');
        }


        

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
        rightCastle =  new boolean[4];
        for(int i=0;i<rightCastle.length;i++)
        {
            rightCastle[i]=false;
        }
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
    public Move[] possibleMove(boolean color){

        Move [] out= new Move[0];
        for(int i=0;i<64;i++)
        {
            if(table[i]!=null && table[i].isColor(color))
            {
                out=appendAll(out,possiblePieceMove(i));
            }
        }
        return out; 
    }
    private void checkStatus()
    {
        if(charSet == null || charSet.length()<12){
            throw new InvalidFormatSettingException();
        }
    }
    public void makeMove(Move m)
    {

        if(!m.rev)
        {
            if(table[m.start]==null){
                System.out.println(this);
                throw new NoPieceFoundException(); 
            }
            table[m.start].moved=true;
            if(m instanceof FirstPawnMove){
                ((FirstPawnMove)m).prevEnPass=enPassantSquare;
                enPassantSquare=m.end+(table[m.start].isColor(Piece.WHITE)?8:-8);
                table[m.end]=table[m.start];
                table[m.start]=null;
                return; 
            }
            enPassantSquare=-10;
            if(m instanceof CastelMove)
            {

                int row = intToCords(m.start)[1];
                int dir = m.end-m.start;

                table[m.end]=table[m.start];
                table[m.start]=null;

                int rookColum= Integer.signum(dir)>0?8:1;
                table[m.start+ Integer.signum(dir)]=table[cordsToInt(rookColum,row)];
                table[cordsToInt(rookColum,row)]=null;
                table[m.start+ Integer.signum(dir)].moved=true;

            }else if(m instanceof PromotionMove)
            {
                PromotionMove pm= (PromotionMove)m;
                if(pm.piecePromoted==PromotionMove.QUEEN){
                    table[pm.end]=new QueenPiece(table[pm.start].color());
                }else if(pm.piecePromoted==PromotionMove.ROOK)
                {
                    table[pm.end]=new RookPiece(table[pm.start].color());
                }
                else if(pm.piecePromoted==PromotionMove.BISHOP)
                {
                    table[pm.end]=new BishopPiece(table[pm.start].color());  
                }else if(pm.piecePromoted==PromotionMove.KNIGHT)
                {
                    table[pm.end]=new KnightPiece(table[pm.start].color());
                }
                table[m.start]=null;
                table[pm.end].moved=true;
            }else if(m instanceof EnPassantMove)
            {
                table[m.end]=table[m.start];
                table[m.start]=null;
                int dir = Integer.signum(m.end%8-m.start%8);
                table[m.start+dir]=null;
            }else{
                Piece temp=table[m.start];
                table[m.start]=null;
                table[m.end]=temp;  
            }
        }else{
            table[m.start].moved=m.beforeMoved;
            if(m instanceof CastelMove){
                table[m.end]=table[m.start];
                table[m.start]=null;
                table[m.end].moved=false;
                int row = intToCords(m.start)[1];
                int dirRook =(m.end-m.start<0)?8:1;
                int dirKing =(m.end-m.start<0)?1:-1;
                table[m.end+dirKing]=null;
                table[cordsToInt(dirRook,row)]= new RookPiece(table[m.end].color());
                table[cordsToInt(dirRook,row)].moved=false;
                return;
            }
            if(m instanceof EnPassantMove){
                enPassantSquare=m.start;
                table[m.end]=table[m.start];
                table[m.start]=null;
                table[m.end-Integer.signum(m.end%8-m.start%8)]=m.pieceCap;
                return; 
            }
            if(m instanceof FirstPawnMove){
                enPassantSquare=((FirstPawnMove)m).prevEnPass;
            }
            if(m instanceof PromotionMove)
            {

                table[m.end]= new PawnPiece(table[m.start].color());
                table[m.start]=null;
            }else{
                Piece temp=table[m.start];
                table[m.start]=null;
                table[m.end]=temp; 
            }
            if(m.capture){
                table[m.start]=m.pieceCap;
                table[m.start].moved=m.befMovcap;
            }
            
        }
        
    }
    public Move[] legalMoves(boolean turn){
        Move [] pm= possibleMove(turn);
        
        Move [] out = new Move[pm.length];
        int vSize=0;
        for(int i=0;i<pm.length;i++){

                if(isLegalMove(pm[i])){
                    out[vSize++]=pm[i];
                }
        }
        out=resize(out,vSize);
        return out;
    }
    public boolean isLegalMove(Move m){
        if(m instanceof CastelMove)
        {
            Move uno;
            Move due;
            int dir = Integer.signum(m.end-m.start);

            uno= new Move(m.start,m.start);
            due = new Move(m.start,m.start+dir);
            if(!isLegalMove(uno) || !isLegalMove(due))
            {
                return false;
            }
        }
        makeMove(m);
        boolean color = table[m.end].color();
        int kingPos=-1;
        for(int i=0;i<=63;i++){
            if(table[i]!=null && table[i] instanceof KingPiece && table[i].isColor(color))
            {
                kingPos=i;
                break;
            }
        }
        if(kingPos==-1){
            throw new KingNotFoundException();
        }
        int [] direzioni ={-9,-7,7,9,-8,1,8,-1};
        int [] modulo ={0,7,0,7,9,7,9,0};

        for(int  d=4;d<direzioni.length;d++)
        {
            for(int i = kingPos+direzioni[d];i>=0 && i<64 && (i-+direzioni[d])%8!=modulo[d];i+=direzioni[d]){
                if(table[i]!=null && table[i].color()!=color && (table[i] instanceof QueenPiece || table[i] instanceof RookPiece))
                {
                    unMakeMove(m);
                    return false;
                }else if(table[i]!=null){
                    break;
                } 
            }
        }
        for(int  d=0;d<4;d++)
        {
            for(int i = kingPos+direzioni[d];i>=0 && i<64 && (i-+direzioni[d])%8!=modulo[d];i+=direzioni[d]){
                if(table[i]!=null && table[i].color()!=color && (table[i] instanceof QueenPiece || table[i] instanceof BishopPiece))
                {
                    unMakeMove(m);
                    return false;
                }
                else if(table[i]!=null){
                    break;
                } 
            }
        }
        for(int  d=0;d<direzioni.length;d++)
        {
            for(int i = kingPos+direzioni[d];i>=0 && i<64 && (i-+direzioni[d])%8!=modulo[d];){
                if(table[i]!=null && table[i].color()!=color && (table[i] instanceof KingPiece))
                {
                    unMakeMove(m);
                    return false;
                }
                break; 
            }
        }
        int dirKing = color==Piece.WHITE?-1:1;
        int maxKingRow =color==Piece.WHITE?8:1;
        if(kingPos%8!=0 && intToCords(kingPos)[1]!=maxKingRow)
        {

            if(table[kingPos+8*dirKing-1] instanceof PawnPiece && !table[kingPos+8*dirKing-1].isColor(color) )
            {
                unMakeMove(m);
                return false;
            }
        }

        if(kingPos%8!=7 && intToCords(kingPos)[1]!=maxKingRow)
        {

            if(table[kingPos+8*dirKing+1] instanceof PawnPiece && !table[kingPos+8*dirKing+1].isColor(color))
            {
                unMakeMove(m);
                return false;
            }
        }
        int n= kingPos;
        int [] kDri ={6,-10,-17,15,-15,17,-6,10};
        int [] kMod = { 1,1,0,0,7,7,6,6};
        int startD=0;
        int endD=kDri.length;
        if(n%8==kMod[0]){startD=2;}
        if(n%8==kMod[2]){startD=4;}
        if(n%8==kMod[5]){endD=4;}
        if(n%8==kMod[7]){endD=6;}
        for(int  d=startD;d<endD;d++)
        {
            for(int i = n+kDri[d];i>=0 && i<64 && (i-+kDri[d])%8!=kMod[d];){
                if(table[i]!=null && table[i].color()!=color && (table[i] instanceof KnightPiece))
                {
                    unMakeMove(m); 
                    return false;
                } 
                break;
            }
        }
        unMakeMove(m);
        return true;
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
                    if(table[i]!=null && table[i].color()!=p.color())
                    {
                        out=append(out,vSize++,new Move(n,i,table[i],table[i].isMoved())); 
                        break;
                    }
                    if(table[i]!=null)break; 
                    out=append(out,vSize++,new Move(n,i)); 
                }
            }
        }
        else if(p instanceof RookPiece)
        {
            for(int d=4;d<direzioni.length;d++)
            {
                for(int i = n+direzioni[d];i>=0 && i<64 &&  (i-+direzioni[d])%8!=modulo[d];i+=direzioni[d]){
                    if(table[i]!=null && table[i].color()!=p.color())
                    {
                        out=append(out,vSize++,new Move(n,i,table[i],table[n].moved,table[i].isMoved())); 
                        break;
                    }
                    if(table[i]!=null)break; 
                    out=append(out,vSize++,new Move(n,i,table[n].moved));  
                }
            }
        }
        else if(p instanceof BishopPiece)
        {
            for(int  d=0;d<4;d++)
            {
                for(int i = n+direzioni[d];i>=0 && i<64 && (i-+direzioni[d])%8!=modulo[d];i+=direzioni[d]){
                    if(table[i]!=null && table[i].color()!=p.color())
                    {
                        out=append(out,vSize++,new Move(n,i,table[i],table[i].isMoved())); 
                        break;
                    } 
                    if(table[i]!=null)break; 
                    out=append(out,vSize++,new Move(n,i));   
                }
            }
        }
        else if(p instanceof KingPiece)
        {
            for(int  d=0;d<direzioni.length;d++)
            {
                for(int i = n+direzioni[d];i>=0 && i<64 && (i-+direzioni[d])%8!=modulo[d];){
                    if(table[i]!=null && table[i].color()!=p.color())
                    {
                        out=append(out,vSize++,new Move(n,i,table[i],table[n].moved,table[i].isMoved())); 
                        break;
                    } 
                    if(table[i]!=null)break; 
                    out=append(out,vSize++,new Move(n,i,table[n].moved));
                    break;
                }
            }
            if(!p.isMoved()&& n%8==4){

                int row=7*(p.isColor(Piece.WHITE)?0:1)+1;
                if(table[cordsToInt(1,row)] instanceof RookPiece && !table[cordsToInt(1,row)].isMoved() && table[cordsToInt(1,row)].color()==p.color())
                {
                    boolean valid=true;

                    for(int i=2;i<intToCords(n)[0];i++)
                    {

                        if(table[cordsToInt(i,row)]!=null){
                            valid=false;
                            break;
                        }
                    }

                    if(valid&&rightCastle[p.isColor(Piece.WHITE)?1:3]){
                        out=append(out,vSize++,new CastelMove(n,n-2));
                    }
                }
                if(table[cordsToInt(8,row)] instanceof RookPiece && !table[cordsToInt(8,row)].isMoved() && table[cordsToInt(8,row)].color()==p.color())
                {
                    boolean valid=true;
                    for(int i=intToCords(n)[0]+1;i<8;i++)
                    {
                        if(table[cordsToInt(i,row)]!=null){
                            valid=false;
                            break;
                        }
                    }
                    if(valid&&rightCastle[p.isColor(Piece.WHITE)?0:2]){
                        out=append(out,vSize++,new CastelMove(n,n+2));
                    }
                }
            }

        }
        else if(p instanceof KnightPiece)
        {

            int [] kDri ={6,-10,-17,15,-15,17,-6,10};

            int [] kMod = { 1,1,0,0,7,7,6,6};
            int startD=0;
            int endD=kDri.length;
            if(n%8==kMod[0]){
                startD=2;
            }
            if(n%8==kMod[2]){
                startD=4;
            }
            if(n%8==kMod[5]){
                endD=4;
            }
            if(n%8==kMod[7]){
                endD=6;
            }
            for(int  d=startD;d<endD;d++)
            {
                for(int i = n+kDri[d];i>=0 && i<64 && (i-+kDri[d])%8!=kMod[d];){
                  
                   if(table[i]!=null && table[i].color()!=p.color())
                   {
                       out=append(out,vSize++,new Move(n,i,table[i],table[i].isMoved())); 
                       break;
                   } 
                   if(table[i]!=null)break; 
                    out=append(out,vSize++,new Move(n,i));
                    break;
                }
            }
        }
        else if(p instanceof PawnPiece)
        {
            int row = intToCords(n)[1];
            int promotionRow;
            int startingRow;
            int moviment;
            if(p.isColor(Piece.WHITE)){
                promotionRow=7;
                startingRow=2;
                moviment=-1;
            }else{
                promotionRow=2;
                startingRow=7;
                moviment=1;
            }

            if(row==startingRow){
                if(table[n+moviment*8]==null && table[n+moviment*8*2]==null)
                {
                    out=append(out,vSize++,new FirstPawnMove(n,n+moviment*8*2));
                }
            }
            if(row==promotionRow){
                if(n+moviment*8<64 && n+moviment*8>=0){
                    for(int k=0;k<4;k++){
                        if(table[n+moviment*8]==null){
                            out=append(out,vSize++,new PromotionMove(n,n+moviment*8,k));
                        }
                        if(n%8!=0 && table[(n+moviment*8)-1]!=null && table[(n+moviment*8)-1].color()!=p.color()){
                            out=append(out,vSize++,new PromotionMove(n,(n+moviment*8)-1,k,table[(n+moviment*8)-1],table[(n+moviment*8)-1].isMoved()));
                        }
                        if(n%8!=7 && table[(n+moviment*8)+1]!=null && table[(n+moviment*8)+1].color()!=p.color()){
                            out=append(out,vSize++,new PromotionMove(n,(n+moviment*8)+1,k,table[(n+moviment*8)+1],table[(n+moviment*8)+1].isMoved()));
                        }
                    }
                }
            }else{
                if(n+moviment*8<64 && n+moviment*8>=0){
                    if(table[n+moviment*8]==null){
                        out=append(out,vSize++,new Move(n,n+moviment*8));
                    }
                    if(n%8!=0 && table[(n+moviment*8)-1]!=null && table[(n+moviment*8)-1].color()!=p.color()){
                        out=append(out,vSize++,new Move(n,(n+moviment*8)-1,table[(n+moviment*8)-1],table[(n+moviment*8)-1].isMoved()));
                    }else if(n%8!=0 && (n+moviment*8)-1 == enPassantSquare){
                        out=append(out,vSize++,new EnPassantMove(n,(n+moviment*8)-1,table[n-1]));
                    }
                    if(n%8!=7 && table[(n+moviment*8)+1]!=null  && table[(n+moviment*8)+1].color()!=p.color()){
                        out=append(out,vSize++,new Move(n,(n+moviment*8)+1,table[(n+moviment*8)+1],table[(n+moviment*8)+1].isMoved()));
                    }else if(n%8!=7 && (n+moviment*8)+1 == enPassantSquare){
                        out=append(out,vSize++,new EnPassantMove(n,(n+moviment*8)+1,table[n+1]));
                    }
                }
            }

        }
        out=resize(out, vSize);
        return out;
    }
    private Move[] appendAll(Move [] arr1,Move[] arr2){
        Move [] out = new Move[arr1.length+arr2.length];
        System.arraycopy(arr1, 0 , out, 0, arr1.length);
        System.arraycopy(arr2, 0 , out, arr1.length, arr2.length);
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

    public static long perft(String fen, int moves)
    {
        Board b = new Board(fen);
        return nPosition(b,moves,b.turn);
    }
    private static long nPosition(Board b,int depth,boolean player)
    {
        if(depth==0){
            return 1;
        }
        long sum=0;

        Move [] moves = b.legalMoves(player);

        for(int i=0;i<moves.length;i++){

            b.makeMove(moves[i]);  
            sum+=nPosition(b,depth-1,!player);
            b.unMakeMove(moves[i]);
        }

        return sum;
    }
}
class NoPieceFoundException  extends RuntimeException{}
class InvalidSquareException extends RuntimeException{}
class InvalidFormatSettingException extends RuntimeException{}
class InvalidFormatFenException extends RuntimeException{}
class KingNotFoundException extends RuntimeException{}