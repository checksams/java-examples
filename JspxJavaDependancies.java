/**
*Java Program to illustrate the objects that a jsp file depends on
*The data is exported into a csv file called F:/Developments/Java/doc.csv. You can change this to any other csv file.
*Change the variables jspx,src,binaryFiles,adfconfig,facesconfig depending on the location they lie on the local machine
*If you run this direclty from command prompt make sure that you have setup the classpath environment variable
* -classpath F:\developments\java
* Author:	Samuel Nyong'a
* Date: 	02-Sep-2018
*/
//package io;

import java.nio.file.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class JspxJavaDependancies
{
	//Path where jspx files are stored
	private static String jspx = "C:/LMSV40/lmsversion4.0/LMSVersion4/public_html";	
	//Path where java source code is stored
	private static String src = "C:/LMSV40/lmsversion4.0/LMSVersion4/src";
	//Path to the compiled Binary files for the application
	private static String binaryFiles = "C:/LMSV40/lmsversion4.0/LMSVersion4/classes/LMS/view";	
	//Path where configurations for java beans are stored 
	private static String adfconfig = "C:/LMSV40/lmsversion4.0/LMSVersion4/public_html/WEB-INF/adfc-config.xml";	
	//Path where configurations for java beans are stored (faces-config.xml is used if the controller of the bean settings are not found in adfc-config.xml)
	private static String facesconfig = "C:/LMSV40/lmsversion4.0/LMSVersion4/public_html/WEB-INF/faces-config.xml";
	
	private static int jspxCnt = 0;
	private static String[] jspxList = new String[100000];
    private static String separator = "/";
    private static List<csvList> csvLst;
	
  public static String readFileAsString(String fileName)throws Exception
  {
    String data = "";
    data = new String(Files.readAllBytes(Paths.get(fileName)));
    return data;
  }
 
  public static void main(String[] args) throws Exception
  {
	String filename = "F:/Developments/Java/batchPrint.jspx";
    csvLst = new ArrayList<csvList>();

    //Print out the file details==========
    //System.out.println(data);
    String chr;
    /*for (int i = 1; i <= data.length(); i++){
    	chr = data.substring(i-1,i
    			);
        //System.out.println("i="+ i + " chr="+chr);
    }*/
    
    for(int i=0; i < 100000; i++){
    	jspxList[i] = "";
    }
    jspxList = displayJspx(new File(jspx));
    for(int i=0; i < jspxList.length; i++){
    	//System.out.println(jspxList[i]);
    	if(jspxList[i].equalsIgnoreCase("")){break;}
    	//if(jspxList[i].contains("claimNotifications.jspx")){
	    	filename = jspxList[i];
	        String data = readFileAsString(filename);
	    	
	    	if(data != null){
		        findBeanBindings(data,"binding=\"#{",filename);
		        findViewClasses(filename);
	    	}
        //}
    }

    String csvString = "";
	csvString = csvString 
			+ "jspx"
			+ "," + "xml"
			+ "," + "java"
			+ "," + "JavaMethods"
			+ "," + "JavaMethodUsage"
			+ "," + "Controller"
			+ "," + "ReportCode"
			+ "," + "CommandLabel"
			+ "\n";

    //get the stored procedures for the Java Methods
	/*for(int i=0; i < csvLst.size(); i++){
    	if(csvLst.get(i).getJspx() == null){break;}
        String java = csvLst.get(i).getjava()!=null ? csvLst.get(i).getjava() : "";
        String javaMethods = csvLst.get(i).getJavaMethods()!=null ? csvLst.get(i).getJavaMethods() : "";
        String[] storedProcs = new String[20];
        if(java.equalsIgnoreCase("") || javaMethods.equalsIgnoreCase("") || java.equalsIgnoreCase("request")){}{
        	storedProcs = findStoredProcedures(javaMethods, java);
        	for(int j = 0; j < storedProcs.length; j++){
        		if(storedProcs[j]==null){break;}
        		if(storedProcs[j].equalsIgnoreCase("")){break;}
        		System.out.println("	"+storedProcs[j]);
        	}
        }
    }*/

    //Generate CSV file
    for(int i=0; i < csvLst.size(); i++){
        	if(csvLst.get(i).getJspx() == null){break;}
        String jspx = csvLst.get(i).getJspx()!=null ? csvLst.get(i).getJspx() : "";
        String xml = csvLst.get(i).getXml()!=null ? csvLst.get(i).getXml() : "";
        String java = csvLst.get(i).getjava()!=null ? csvLst.get(i).getjava() : "";
        String javaMethods = csvLst.get(i).getJavaMethods()!=null ? csvLst.get(i).getJavaMethods() : "";
        String controller = csvLst.get(i).getController()!=null ? csvLst.get(i).getController() : "";
        String javaMethodUsage = csvLst.get(i).getJavaMethodUsage()!=null ? csvLst.get(i).getJavaMethodUsage() : "";
        String reportCode = csvLst.get(i).getReportCode()!=null ? csvLst.get(i).getReportCode() : "";
        String reportLabel = csvLst.get(i).getReportLabel()!=null ? csvLst.get(i).getReportLabel() : "";

    	csvString = csvString 
				+ jspx
				+ "," + xml
				+ "," + java
				+ "," + javaMethods
				+ "," + javaMethodUsage
				+ "," + controller
				+ "," + reportCode
				+ "," + reportLabel
				+ "\n";
				
    }
    
    FileWriter fw = new FileWriter("F:/Developments/Java/doc.csv");
    fw.write(csvString);
    fw.flush();
    
  }
  
  public static void findBeanBindings(String data, String searchStr,String filename) throws Exception{
    int strLength = searchStr.length();
    String dynamicSearchStr;
    String closeBracketSignSearch;
    String[] binding= new String[10000];
    String foundStr;
    int bind = 0, maxBind=0;
    int bindStrLen = 0;
    Boolean exists = false;
    for (int i = strLength; i <= data.length(); i++){
    	
    	dynamicSearchStr = data.substring(i-strLength,i);
    	if (dynamicSearchStr.equalsIgnoreCase(searchStr)) {
    		bindStrLen = 0;
    		for (int j = i; j <= data.length(); j++){
    			closeBracketSignSearch = data.substring(j-1,j);
    			
    			//Extract the binding bean
    			if(closeBracketSignSearch.equalsIgnoreCase(".")){
    				foundStr = data.substring(i,i+bindStrLen-1);
    				exists = false;
    				//Check if the string found already exists in the found occurances of bindings
    				for (int k=0; k <= 100; k++ ){
    					if (foundStr.equalsIgnoreCase(binding[k])){
    						exists = true;
    					}
    				}
    				if(exists.equals(false)){
        				binding[bind++] = foundStr;
        				maxBind++;
    				}
    				break;
    			}else{
        			bindStrLen++;
    			}
    		}
    		//Print out a found match
    		//System.out.println("i="+ i +" bindStrLen="+bindStrLen+" bind-1="+(bind-1) +" dynamicSearchStr="+dynamicSearchStr+" FOUND bind-1="+binding[bind-1]);
    	}else{
    		//System.out.println("i="+ i + " dynamicSearchStr="+dynamicSearchStr);
    	}
    }
    //Print out the distnct found controller bindings==============================
    System.out.println("====Controller bindings===.../LMSVersion4/public_html/WEB-INF/adfc-config.xml=================================");
    if(maxBind > 0){maxBind = maxBind-1;}
    for (int i = 0; i <= maxBind; i++){
    	String beanClass = "";
    	if (binding[i] != null){
	    	try{
	    		beanClass = findBeanClass(binding[i],adfconfig);
	    		if (beanClass.equalsIgnoreCase("")||beanClass == null){
	    			beanClass = findBeanClass(binding[i],facesconfig);
	    		}
	    	} catch(Exception e){
	    		System.out.println("Reading bean xml file...");
	    		e.printStackTrace();
	    	}
    	}
    	System.out.println(" "+filename +"	"+binding[i]+"	beanClass="+beanClass);
    	csvList csvListItem = new csvList();
    	csvListItem.setJspx(filename);
    	csvListItem.setController(binding[i]);
    	csvListItem.setJava(beanClass);
    	csvLst.add(csvListItem);
    }
    //Check for actionListeners============================================
    findListenerClass(filename);
    

    //Check for rendering classes============================================
    findRenderClass(filename);
    
    //Print out the distnct found view bindings==============================
    System.out.println("====View bindings====================================");
    
   
  }//end findBeanBindings
  
  public static String findBeanClass(String searchStr, String configFile)  throws Exception{
	  String data = readFileAsString(configFile);
	  int strLength = searchStr.length();
	  String dynamicSearchStr;
	  String closeSignSearch;
	  String startSignSearch;
	  String foundStr = "";
	  int bindStrLen = 0;
	  int closeCnt	= 0;
	  Boolean exists = false;
	  
	  for (int i = strLength; i <= data.length(); i++){
	    	
	    	dynamicSearchStr = data.substring(i-strLength,i);
	    	if (dynamicSearchStr.equalsIgnoreCase(searchStr) && data.substring(i,i+22).contains("</managed-bean-name")) {

	    		for (int j = i; j <= i+500; j++){
	    			closeSignSearch = data.substring(j,j+1);

	    			if(closeSignSearch.equalsIgnoreCase(">")){
	    				++closeCnt;
	    			}
	    			
	    			//Extract the binding bean
	    			bindStrLen = 0;
	    			if(closeSignSearch.equalsIgnoreCase(">")&&closeCnt==2){
	    				for(int k=j; k<=k+500; k++){
	    					startSignSearch = data.substring(k,k+1);
	    					//System.out.println("startSignSearch="+startSignSearch );
	    					if (startSignSearch.equalsIgnoreCase("<")){
	    						
	    						foundStr = data.substring(j+1,j+bindStrLen);
	    						break;
	    					}else{
	    						bindStrLen++;
	    					}
	    				}
	    				break;
	    			}
	    		}
	    		break;
	    		//Print out a found match
	    		//System.out.println("i="+ i +" bindStrLen="+bindStrLen +" dynamicSearchStr="+dynamicSearchStr+" FOUND foundStr="+foundStr);
	    	}else{
	    		//System.out.println("i="+ i + " dynamicSearchStr="+dynamicSearchStr+" searchStr="+searchStr);
	    	}
	  }
	  //System.out.println("tttt beanClass foundStr="+foundStr );
	  return foundStr;
  }//end findBeanClass
  

  public static String findRenderClass(String filename)  throws Exception{

	  String[] result = new String[2];
	  String searchStr = "rendered=\"#{"; 
	  String data = readFileAsString(filename);
	  int strLength = searchStr.length();
	  String dynamicSearchStr;
	  String closeSignSearch;
	  String foundStr = "";
	  String beanClass = "";
	  csvList csvListItem = null;
	  String reportCode = "";
	  String reportLabel = "";

	  for(int a=0; a <= 1; a++){
		  if(a==1){
			  searchStr = "visible=\"#{";
			  strLength = searchStr.length();
			  //System.out.println(a+" xxxx Render foundStr=="+foundStr);
		  }
		  //find render class
		  for (int i=strLength; i <= data.length(); i++){
	    	dynamicSearchStr = data.substring(i-strLength,i);
	    	if (dynamicSearchStr.equalsIgnoreCase(searchStr)) {
	    		for(int j=i; j <= i+200; j++){
	    			closeSignSearch = data.substring(j,j+1);
	    			if(closeSignSearch.equalsIgnoreCase("}")){
	    				if(data.substring(i,j).contains("bindings.")){}
	    				else{
		    				foundStr = data.substring(i,j);
		    				result[a] = foundStr;
		    				//remove the method and remain with the managed bean name only
		    				for (int k=foundStr.length(); k > 0; k--){
		    					if(foundStr.substring(k-1, k).equalsIgnoreCase(".")){
		    						result[a] = foundStr.substring(0, k-1);
		    					}
		    				}
		    				
		    				System.out.println("Render foundStr=="+foundStr);
	
					    	if (result[a] != null){
						    	try{
						    		beanClass = findBeanClass(result[a],adfconfig);
						    		if (beanClass.equalsIgnoreCase("")||beanClass == null){
						    			beanClass = findBeanClass(result[a],facesconfig);
						    		}
						    	} catch(Exception e){
						    		System.out.println("Reading render bean xml file...");
						    		e.printStackTrace();
						    	}
					    	}
		    				System.out.println("Filename="+filename+" Listener="+foundStr+" BeanClass="+beanClass+" result[a]="+result[a]+" result[0]="+result[0]);
		    				
		    		    	String xmlFile="";
		    		    	if(result[a] != null ){
		    		    		xmlFile = result[a].replace(".class", ".xml");
		    		    	}
		    		    	if(result[a] != result[0]){
			    				csvListItem = new csvList();
			    		    	csvListItem.setJspx(filename);
			    		    	csvListItem.setJava(beanClass);
			    		    	csvListItem.setJavaMethods(foundStr);
			    		    	csvListItem.setJavaMethodUsage("Rendering");
			    		    	csvListItem.setXml(xmlFile);
			    		    	csvListItem.setController(result[a]);
			    		    	csvListItem.setReportCode(reportCode);
			    		    	csvListItem.setReportLabel(reportLabel);
			    		    	csvLst.add(csvListItem);
		    		    	}
	    				}    				
	    				break;
	    			}    			
	    		}
	    		break;
	    	}
		  }
	  }
   
	  
	  return foundStr;
  }//end findRenderClass

  public static String findListenerClass(String filename)  throws Exception{

	  String[] result = new String[2];
	  String searchStr = "Listener=\"#{";	  
	  String data = readFileAsString(filename);
	  int strLength = searchStr.length();
	  String dynamicSearchStr;
	  String closeSignSearch;
	  String foundStr = "";
	  String beanClass = "";
	  csvList csvListItem = null;
	  String reportCode = "";
	  String reportLabel = "";
		

	  for (int i=strLength; i <= data.length(); i++){
    	dynamicSearchStr = data.substring(i-strLength,i);
    	if (dynamicSearchStr.equalsIgnoreCase(searchStr)) {
    		for(int j=i; j <= i+200; j++){
    			closeSignSearch = data.substring(j,j+1);
    			if(closeSignSearch.equalsIgnoreCase("}")){
    				//foundStr = data.substring(i,j);
    				//System.out.println("aaa Listener="+foundStr);
    				if(data.substring(i,j).contains("bindings.")){}
    				else{
	    				foundStr = data.substring(i,j);
	    				result[0] = foundStr;
	    				//System.out.println("Listener="+foundStr);
	    				for (int k=result[0].length(); k >= 0; k--){
	    					if(result[0].substring(k-1, k).equalsIgnoreCase(".")){
	    						result[0] = result[0].substring(0, k-1);
	    						break;
	    					}
	    				}

						if(result[0].contains("ReportEngine")){
	    					//find report id(rptCode)
							for(int k = i; k > i-200; k--){//loop backwards until id= string is found
								if(data.substring(k-4, k).equalsIgnoreCase("id=\"")){
									for(int m = k; m <= k+200; m++){//loop forward until " is found
										if(data.substring(m, m+1).equalsIgnoreCase("\"")){
											reportCode = data.substring(k, m).replace("rpt", "");
											break;
										}
									}									
								    break;
								}
								else if(data.substring(k-1, k).equalsIgnoreCase("<")){
									break;
								}
							}
							
							//find report label
							for(int k = i; k > i-1000; k--){//loop backwards until id= string is found
								if(data.substring(k-6, k).equalsIgnoreCase("text=\"")){
									for(int m = k; m <= k+200; m++){//loop forward until " is found
										if(data.substring(m, m+1).equalsIgnoreCase("\"")){
											reportLabel = data.substring(k, m);
											break;
										}
									}									
								    break;
								}
								else if(data.substring(k-1, k).equalsIgnoreCase("<")){
									break;
								}
							}
		    				//System.out.println("++++++++++ reportCode="+reportCode+" reportLabel="+reportLabel);
	    				}//end ReportEngine if statement
    				
				    	if (result[0] != null){
					    	try{
					    		beanClass = findBeanClass(result[0],adfconfig);
					    		if (beanClass.equalsIgnoreCase("")||beanClass == null){
					    			beanClass = findBeanClass(result[0],facesconfig);
					    		}
					    	} catch(Exception e){
					    		System.out.println("Reading control bean xml file...");
					    		e.printStackTrace();
					    	}
				    	}
	    				//System.out.println("Filename="+filename+" Listener="+foundStr+" BeanClass="+beanClass+" result[0]="+result[0]);
	    				
	    		    	String xmlFile="";
	    		    	if(result[0] != null){
	    		    	xmlFile = result[0].replace(".class", ".xml");}
	    				csvListItem = new csvList();
	    		    	csvListItem.setJspx(filename);
	    		    	csvListItem.setJava(beanClass);
	    		    	csvListItem.setJavaMethods(foundStr);
	    		    	csvListItem.setJavaMethodUsage("actionListener");
	    		    	csvListItem.setXml(xmlFile);
	    		    	csvListItem.setController(result[0]);
	    		    	csvListItem.setReportCode(reportCode);
	    		    	csvListItem.setReportLabel(reportLabel);
	    		    	csvLst.add(csvListItem);

    				}
    				break;
    			}
    		}    		
    	}
	  }
	  
	  return foundStr;
  }//end findListenerClass

  public static void findViewClasses(String filename) throws Exception{
	  
	  String[] result = new String[2];
	  String pageDefname = "";
	  String pageDefFname = filename.replace(".jspx", "PageDef.xml");
	  //System.out.println("xxxxxxxxx pageDefFname="+pageDefFname);	  
	  String searchStr = "ReturnName=";	  
	  
	  int strLength = searchStr.length();
	  String dynamicSearchStr;
	  String closeSignSearch;
	  String startSignSearch;
	  String foundStr = "";
	  int bindStrLen = 0;
	  int closeCnt	= 0;
	  Boolean exists = false;
	  
	  String dao;

	  //get the pageDef file
	  for(int i = pageDefFname.length(); i > 0; i--){
		  if(pageDefFname.substring(i-1, i).equalsIgnoreCase("\\")
				  ||pageDefFname.substring(i-1, i).equalsIgnoreCase("/")){
			  //System.out.println(" FOund pageDefFname="+pageDefFname);
			  break;
		  };
		  //System.out.println(i+" cc pageDefFname="+pageDefFname.substring(i-1, pageDefFname.length()));
		  pageDefname = pageDefFname.substring(i-1, pageDefFname.length());
	  }
	  pageDefFname = pageDefname;
	  //System.out.println(" XX pageDefFname="+pageDefFname);
	  foundFile = "";
	  pageDefFname = searchIt(new File(binaryFiles), pageDefFname);
	  //System.out.println(" yy pageDefFname="+pageDefFname);	
	  
	  if (pageDefFname.equalsIgnoreCase("")){return;}
	
	  	csvList csvListItem = new csvList();
	  	csvListItem.setJspx(filename);
	  	csvListItem.setXml(pageDefFname);
	  	csvLst.add(csvListItem);
	  	
	  String data = readFileAsString(pageDefFname);
	  //System.out.println(data);
	  //System.out.println("dynamicSearchStrXX="+ foundStr+" searchStr="+searchStr);
	  for (int i=strLength; i <= data.length(); i++){
    	dynamicSearchStr = data.substring(i-strLength,i);
    	if (dynamicSearchStr.equalsIgnoreCase(searchStr)) {
    		//System.out.println("dynamicSearchStr="+ foundStr);
    		for(int j=i; j <= i+200; j++){
    			closeSignSearch = data.substring(j,j+1);
    			if(closeSignSearch.equalsIgnoreCase("_")){
    				foundStr = data.substring(i+1,j).replace("data.", "").replace("methodResults.", "");
    				dao = foundStr;
    				//System.out.println("aaaaaa foundStr="+foundStr);
    				for(int l=0; l <= dao.length(); l++){
    					closeSignSearch = dao.substring(l,l+1);
    					if(closeSignSearch.equalsIgnoreCase(".")){
    						dao = dao.substring(0, l);
    					}
    				}
    				result = findDataControlBinds(dao, data);
    				System.out.println("	dao="+dao+"		"+result[0]+ "	"+result[1]+ " "+foundStr );

    		    	String xmlFile="";
    		    	if(result[0] != null){
    		    	xmlFile = result[0].replace(".class", ".xml");}
    				csvListItem = new csvList();
    		    	csvListItem.setJspx(filename);
    		    	csvListItem.setJava(result[0]);
    		    	csvListItem.setJavaMethods(foundStr);
    		    	csvListItem.setJavaMethodUsage("View");
    		    	csvListItem.setXml(xmlFile);
    		    	csvLst.add(csvListItem);

    		    	if(result[1] != null){
    		    	xmlFile = result[1].replace(".class", ".xml");}
    		    	csvListItem = new csvList();
    		    	csvListItem.setJspx(filename);
    		    	csvListItem.setJava(result[1]);
    		    	csvListItem.setJavaMethods(foundStr);
    		    	csvListItem.setJavaMethodUsage("View");
    		    	csvListItem.setXml(xmlFile);
    		    	csvLst.add(csvListItem);
    		    	
    				break;
    			}else{
    				
    			}
    		}
    	}else{
    		//System.out.println("i="+ i + " dynamicSearchStr="+dynamicSearchStr);
    	}
		  
	  }
	  
  }//end findViewClasses


  public static String[] findDataControlBinds(String searchStr, String data){
	  String[] result = new String[2];
	  
	  int strLength = searchStr.length();
	  String dynamicSearchStr;
	  String closeSignSearch;
	  String startSignSearch = "BeanClass=\"";
	  String beanSearch;
	  String foundStr = "";
	  String foundStrTwo = "";
	  int bindStrLen = 0;
	  int bindStrLenTwo = 0;
	  int closeCnt	= 0;
	  Boolean exists = false;

	  String startBindsSearch = "Binds=\"";
	  String bindsSearch = "";
	  String methodSearch = "";
	  
	  for (int i=strLength; i <= data.length(); i++){
	    	dynamicSearchStr = data.substring(i-strLength,i);
	    	
	    	//find the location of the DAO in the page def
	    	if (dynamicSearchStr.equalsIgnoreCase(searchStr)) {
	    		//////////////The location of the DAO has been found/////////////	    		
	    		//System.out.println(i+ "  DAO dynamicSearchStr="+dynamicSearchStr);
	    		
	    		//Now find the full path////////////////////////////////////////
	    		for(int j=i; j <= i+100; j++){
	    			beanSearch = data.substring(j,j+startSignSearch.length());
	    			if(beanSearch.equalsIgnoreCase(startSignSearch)){
	    				bindStrLen = 0;
	    				for(int k=j+startSignSearch.length()+1; k <=j+200; k++ ){
	    					closeSignSearch = data.substring(k-1,k);
    						bindStrLen++;
	    					if(closeSignSearch.equalsIgnoreCase("\"")){
	    						foundStr = data.substring(j+startSignSearch.length(),j+startSignSearch.length()+bindStrLen-1);
		    					//System.out.println("	yyyyy beanSearch="+beanSearch+ " foundStr="+foundStr+ " bindStrLen="+bindStrLen+ "  "+foundStr.length());
		    					result[1] = foundStr+".class";
		    					//remove the last .filename from foundStr and remain with the package path
		    					bindStrLenTwo =0;
		    					for (int m = foundStr.length(); m >= 0; m--){
		    						bindStrLenTwo++;
		    						closeSignSearch = foundStr.substring(m-1,m);
		    						if(closeSignSearch.equalsIgnoreCase(".")){
		    							foundStr = foundStr.substring(0, foundStr.length()-bindStrLenTwo);
		    							//System.out.println("xxxxx foundStr="+foundStr+" bindStrLenTwo="+bindStrLenTwo);
		    							foundStr = foundStr+"."+searchStr+".class";
		    							break;
		    						}
		    					}
	    						break;
	    					}else{
	    					}
	    				}
	    				//System.out.println("	wwwww="+foundStr);
	    				break;
	    			}else{
	    				
	    			}
	    		}
	    		break;
	    	}else{
	    		//System.out.println("i="+ i + " dynamicSearchStr="+dynamicSearchStr);
	    	}
		  
	  }
	  result[0] = foundStr;
	  return result;
  }//end findDataControlBinds
  
  public static int dirLoop=0;
  public static String[] displayJspx(File node){
		//System.out.println(node.getAbsoluteFile());
		if(node.getAbsoluteFile().toString().contains(".jspx")){
			//System.out.println(node.getAbsoluteFile());
			jspxList[jspxCnt] = node.getAbsoluteFile().toString();
			jspxCnt++;
		}
		dirLoop++;
		if(node.isDirectory() && (dirLoop==1)){
			String[] subNote = node.list();
			for(String filename : subNote){
				
				displayJspx(new File(node, filename));
			}
		}
		return jspxList;
	}//end displayIt

	public static Boolean located=false;
	public static String foundFile="";
	public static String searchIt(File node, String searchName){
        
	  if(foundFile.equalsIgnoreCase("")){
		if(node.isDirectory()){
			String[] subNote = node.list();
            for(String search : node.list()){                
                if(searchName.equals(search)){
                    //System.out.println("Located "+ node.getAbsoluteFile()+ separator +search);
                    foundFile = node.getAbsoluteFile().toString() + separator +search;
                    return foundFile;
                }                
            }
            //System.out.println("Mbaya "+ node.getAbsoluteFile());
			for(String filename : subNote){
				searchIt(new File(node, filename), searchName);
			}
		}
	  }
		return foundFile;
		
	}//end searchIt
	
	public static String[] findStoredProcedures(String javaMethods, String javaFname)throws Exception{
		String[] result = new String[20];
		String javaFile = javaFname;

		
		if (javaMethods.length()!=0 && javaFname.length()!=0 ){

			  String searchStr="";
			  for(int i=javaMethods.length(); i > 0; i--){
				  if (javaMethods.substring(i-1, i).equalsIgnoreCase(".")) {
					  searchStr = javaMethods.substring(i, javaMethods.length());
					  //System.out.println(" searchStr="+searchStr);
					  break;
				  }
			  }

			  int strLength = searchStr.length();
			  String dynamicSearchStr;
			  String closeSignSearch;
			  String startSignSearch = "BeanClass=\"";
			  String foundStr = "";
			  String foundStrTwo = "";
			  int bindStrLen = 0;
			  int closeCnt	= 0;
			  Boolean exists = false;
			  
			//System.out.println(" javaFile="+javaFile +" javaFile.length()="+javaFile.length());	
			if(javaFile.substring((javaFile.length()-6), javaFile.length()).equalsIgnoreCase(".class")){
				javaFile = javaFname.replace(".class",".java");
			}else{
				javaFile = javaFname+".java";
			}
			javaFile = javaFile.replace(".",separator);
			javaFile = src + separator + javaFile.replace(separator+"java",".java");
			System.out.println("javaFile="+javaFile);
			
			String data = readFileAsString(javaFile);

			//System.out.println(data);
			//find the usage of stored procesdures
			for(int i=strLength; i <= data.length(); i++){
		    	dynamicSearchStr = data.substring(i-strLength,i);
		    	
		    	//find the location of the DAO in the page def
		    	if (dynamicSearchStr.equalsIgnoreCase(searchStr)) {
		    		//STILL UNDER CONSTRUCTION
		    	}
				
			}
			
		}
		return result;
	}//end findStoredProcedures
	
	public static class csvList {
		private String jspx;
		private String xml;
		private String java;
		private String javaMethods;
		private String controller;
		private String reportCode;
		private String reportLabel;
		private String javaMethodUsage;
		private String storedProcedure;

	    public void setJspx(String jspx) {
	        this.jspx = jspx;
	    }
	    public String getJspx() {
	        return jspx;
	    }

	    public void setXml(String xml) {
	        this.xml = xml;
	    }
	    public String getXml() {
	        return xml;
	    }
	    
	    public void setJava(String java) {
	        this.java = java;
	    }
	    public String getjava() {
	        return java;
	    }
	    
	    public void setJavaMethods(String javaMethods) {
	        this.javaMethods = javaMethods;
	    }
	    public String getJavaMethods() {
	        return javaMethods;
	    }

	    public void setController(String controller) {
	        this.controller = controller;
	    }
	    public String getController() {
	        return controller;
	    }
	    
	    public void setReportCode(String reportCode) {
	        this.reportCode = reportCode;
	    }
	    public String getReportCode() {
	        return reportCode;
	    }
	    
	    public void setReportLabel(String reportLabel) {
	        this.reportLabel = reportLabel;
	    }
	    public String getReportLabel() {
	        return reportLabel;
	    }
	    
	    public void setJavaMethodUsage(String javaMethodUsage) {
	        this.javaMethodUsage = javaMethodUsage;
	    }
	    public String getJavaMethodUsage() {
	        return javaMethodUsage;
	    }
	    
	    public void setStoredProcedure(String storedProcedure) {
	        this.storedProcedure = storedProcedure;
	    }
	    public String getStoredProcedure() {
	        return storedProcedure;
	    }

	}
	
}
