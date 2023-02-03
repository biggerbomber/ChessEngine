import java.util.*;
import java.io.*;

public class Confronta
{
    public static void main(String [] args)
    {
        HashMap<String,Integer> uno = new HashMap<String,Integer>();

        try(FileReader f1= new FileReader("in1.txt");
            FileReader f2= new FileReader("in2.txt");){

            Scanner in1 = new Scanner(f1);
            //in1.useDelimiter(":|\n| ");
            while(in1.hasNextLine()){
                Scanner temp = new Scanner(in1.nextLine());
                temp.useDelimiter(":");
                String a= temp.next();
                Integer b=  Integer.parseInt(temp.next());;
                uno.put(a, b);
            }

            Scanner in2 = new Scanner(f2);
            //in2.useDelimiter(":|\n| ");
            while(in2.hasNextLine()){
                Scanner temp = new Scanner(in2.nextLine());
                temp.useDelimiter(":");
                String a= temp.next();
                Integer b= Integer.parseInt(temp.next().substring(1));
                
                Integer c= uno.get(a); 
                if(c==null || !b.equals(c)){
                    System.out.println(a);
                }
            }

            
        }catch(FileNotFoundException e){

        }catch(IOException e){

        }
    }


}