package com.example.finalattempt;

import it.sauronsoftware.ftp4j.FTPClient;
//import it.sauronsoftware.ftp4j.FTPException;
//import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.File;
//import java.io.IOException;
import java.util.ArrayList;



public class FTPservice {
	
	
	FTPClient client;
	public FTPservice(){
		client = new FTPClient();
		
	}
	/*	public void close(){
		try {
			client.disconnect(true);
			client = new FTPClient();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 */
	/*********  work only for Dedicated IP ***********/
    
	final String FTP_HOST= "10.22.114.17";
     
    /*********  FTP USERNAME ***********/
    final String FTP_USER = "1";
     
    /*********  FTP PASSWORD ***********/
    final String FTP_PASS  ="1";
	
  //  static final String PATH = "";
     

     //   File f = new File(PATH);
         
        // Upload sdcard file
      
         
   
     
    public boolean uploadFile(File fileName){
         
         
        boolean ret = false;
          
        try {
             
            client.connect(FTP_HOST, 2121);
            client.login(FTP_USER, FTP_PASS);
            client.setType(FTPClient.TYPE_BINARY);
         //   client.changeDirectory("/");
             
            client.upload(fileName);
           // client.
            client.disconnect(true);   
            ret = true; 
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.disconnect(true);   
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
         return ret;
    }
    
    
    
    public boolean deleteFile(String fileName) {
     
    	
    		 boolean ret = false;
    	        try {
    	             
    	            client.connect(FTP_HOST, 2121);
    	            client.login(FTP_USER, FTP_PASS);
    	            client.setType(FTPClient.TYPE_BINARY);
    	            client.deleteFile(fileName);
    	            client.disconnect(true);
    	            ret = true;
    	}
    	        catch (Exception e) {
    	            e.printStackTrace();
    	            try {
    	                client.disconnect(true);   
    	            } catch (Exception e2) {
    	                e2.printStackTrace();
    	            }
    	        } 
    return ret;		
    	}
     
    
    public boolean downloadFile(String remoteFileName, File localFile){
    	
   		 
    		boolean ret = false;
   	        try {
   	             
   	            client.connect(FTP_HOST, 2121);
   	            client.login(FTP_USER, FTP_PASS);
   	            client.setType(FTPClient.TYPE_BINARY);
   	            client.download(remoteFileName, localFile);
   	            client.disconnect(true);
   	            ret = true;
   	}
   	        catch (Exception e) {
   	            e.printStackTrace();
   	            try {
   	                client.disconnect(true);   
   	            } catch (Exception e2) {
   	                e2.printStackTrace();
   	            }
   	        } 
   		return ret;
   	}
    	
        
    
    public ArrayList<String> getList() {
        
    	
    		
    		 ArrayList<String> a = new ArrayList<String>();
    	        try {
    	             
    	            client.connect(FTP_HOST, 2121);
    	            client.login(FTP_USER, FTP_PASS);
    	            client.setType(FTPClient.TYPE_BINARY);
    	         
    	            for (String str : client.listNames()){
    	        	 a.add(str);
    	            	}
    	            client.disconnect(true);   
    	        }
    	        catch (Exception e) {
    	            e.printStackTrace();
    	            try {
    	                client.disconnect(true);   
    	            } catch (Exception e2) {
    	                e2.printStackTrace();
    	            }
    	        } 
    		return(a);
    	
     
    }

    public long getSize(String fileName) {
        	
		 long a = 0;
		 try {
             
	            client.connect(FTP_HOST, 2121);
	            client.login(FTP_USER, FTP_PASS);
	            client.setType(FTPClient.TYPE_BINARY);
	         
	            a = client.fileSize(fileName);
	            client.disconnect(true);   
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            try {
	                client.disconnect(true);   
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        } 
		return(a);
	
	 
    }
}
