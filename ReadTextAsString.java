// Java Program to illustrate reading from text file
// as string in Java
// -classpath F:\developments\java
//package io;
import java.nio.file.*;;
public class ReadTextAsString
{
  public static String readFileAsString(String fileName)throws Exception
  {
    String data = "";
    data = new String(Files.readAllBytes(Paths.get(fileName)));
    return data;
  }
 
  public static void main(String[] args) throws Exception
  {
    String data = readFileAsString("F:/Developments/Java/ReadTextAsString.java");
    System.out.println(data);
    String chr;
    for (int i = 1; i <= data.length(); i++){
    	chr = data.substring(i-1,i
    			);
        //System.out.println("i="+ i + " chr="+chr);
    }
  }
}
