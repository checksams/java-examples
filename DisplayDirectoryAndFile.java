/**
* Created On: 30th Aug 2018
* Created By: Samuel Nyong'a
* Puropose: To Traverse a directory and display the files ans folders in it
*           To Seach a directory for exact match of files in it.
*
*/

//package com.mkyong.io;

import java.io.File;

public class DisplayDirectoryAndFile{
    private static String os;
    private static String separator;
    
	public static void main (String args[]) {
        separator = "/";
        os = System.getProperty("os.name");
        if (os.toLowerCase().contains("win")){
            separator = "\\";
        }
        //System.out.println("OS="+os.toLowerCase());
        String dir = args[0];
        
        if (args[1].equals("s")){
            searchIt(new File(dir), args[2]);
        }
        else{
            displayIt(new File(dir));
        }
	}
	
	public static void displayIt(File node){
		
		System.out.println(node.getAbsoluteFile());
		
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				displayIt(new File(node, filename));
			}
		}
		
	}
    
	public static void searchIt(File node, String searchName){
        
		if(node.isDirectory()){
			String[] subNote = node.list();
            for(String search : node.list()){                
                if(searchName.equals(search)){
                    System.out.println("Located "+ node.getAbsoluteFile()+ separator +search);
                }                
            }
            
			for(String filename : subNote){
				searchIt(new File(node, filename), searchName);
			}
		}
		
	}
}
