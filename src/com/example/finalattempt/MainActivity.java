package com.example.finalattempt;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	//public ArrayList<String> uploaded = new ArrayList<String>();
	ListView lv;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	            StrictMode.setThreadPolicy(policy);
	          }
		 
		Button syncrUP = (Button)findViewById(R.id.data_up);
		
        syncrUP.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				
				
				        	FTPservice FTP = new FTPservice();
							synchrUp(FTP);
							synchrUpDel(FTP);
							sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
			}
		});
        Button syncrDOWN = (Button)findViewById(R.id.data_down);
		
        syncrDOWN.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
				
				
				        	FTPservice FTP = new FTPservice();
							synchrDown(FTP);
							synchrDownDel(FTP);
							sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
			}
		});		
	}	
			
    public String getPath(String Title) {
    	String path = null;
    	String[] img = { MediaStore.Images.Media.DATA,
    		MediaStore.Images.Media.TITLE};
    	String mSelectionClause = null;
    	String[] mSelectionArgs = {null};	
    	
    	String Search = Title;
    	if	(!("".equals(Search))) {
    		mSelectionClause = MediaStore.Images.Media.TITLE + " = ?";
    		mSelectionArgs [0] = Search;
    	}
    	else{
    		mSelectionArgs = null;
    	}
        
    	Cursor mCursor;
    	mCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, img , mSelectionClause, mSelectionArgs, null);
    	int indexDATA = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
    	if (mCursor != null) {
            while (mCursor.moveToNext()) {
            	String newTitle = null;
            	newTitle = mCursor.getString(indexDATA);
            	path = newTitle;
            }
    	}
    	return (path);
    }
    
    
    public String getSize(String Title) {
    	String size = null;
    	String[] img = { MediaStore.Images.Media.SIZE,
    		MediaStore.Images.Media.TITLE};
    	String mSelectionClause = null;
    	String[] mSelectionArgs = {null};	
    	
    	String Search = Title;
    	if	(!("".equals(Search))) {
    		mSelectionClause = MediaStore.Images.Media.TITLE + " = ?";
    		mSelectionArgs [0] = Search;
    	}
    	else{
    		mSelectionArgs = null;
    	}
        
    	Cursor mCursor;
    	mCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, img , mSelectionClause, mSelectionArgs, null);
    	int indexDATA = mCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
    	if (mCursor != null) {
            while (mCursor.moveToNext()) {
            	String newTitle = null;
            	newTitle = mCursor.getString(indexDATA);
            	size = newTitle;
            }
    	}
    	return (size);
    }
    
    
    public ArrayList<String> getHost() {
    	ArrayList<String> Daty = new ArrayList<String>();
    	String[] img =  {MediaStore.Images.Media.TITLE};
    	Cursor mCursor;
    	mCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, img , null, null, null);
    	int indexTITLE = mCursor.getColumnIndex(MediaStore.Images.Media.TITLE);
    	if (mCursor != null) {
            while (mCursor.moveToNext()) {
            	String newTitle = null;
            	newTitle = mCursor.getString(indexTITLE);//+".jpg";
            	Daty.add(newTitle);
            }
    	}
            	
    	return(Daty);
    }
    
    public ArrayList<String> getServer() {
    	ArrayList<String> Daty = new ArrayList<String>();
    	FTPservice servTitle = new FTPservice();
    	Daty = servTitle.getList();
    	return (Daty);
    }
    
    
    
    public ArrayList<String> synchrUp(FTPservice upload) {
    	ArrayList<String> hostList = new ArrayList<String>();
    	ArrayList<String> servList = new ArrayList<String>();
    	ArrayList<String> uploadedList = new ArrayList<String>();
    	
    	String string = ".jpg";
    	hostList = getHost();
    	servList = getServer();
    	ListIterator it = hostList.listIterator();
    	while (it.hasNext()){
    		String temp = it.next().toString();

    		if(!(servList.contains(temp.concat(string)))){
    			
    			uploadedList.add(temp);

    			File up = new File(getPath(temp));
    			
    			boolean att = upload.uploadFile(up);
    			if (att)
    				Toast.makeText(getBaseContext(),  temp.concat(string)+ " has been uploaded", Toast.LENGTH_SHORT).show();
    			else
    				Toast.makeText(getBaseContext(),  temp.concat(string)+ " has NOT been uploaded", Toast.LENGTH_SHORT).show();
    		}
    		else {
    			if(!(Long.parseLong(getSize(temp)) == upload.getSize(temp.concat(string)))) {
    			
    				upload.deleteFile(temp.concat(string));
    				uploadedList.add(temp);
           			File up = new File(getPath(temp));
        			
        			boolean att = upload.uploadFile(up);
        			if (att)
        				Toast.makeText(getBaseContext(),  temp.concat(string)+ " has been updated", Toast.LENGTH_SHORT).show();
        			else
        				Toast.makeText(getBaseContext(),  temp.concat(string)+ " has NOT been updated", Toast.LENGTH_SHORT).show();
    			}
     		}
    	}
    	return(uploadedList);
    }
    
    public ArrayList<String> synchrUpDel(FTPservice upload) {
    	ArrayList<String> hostList = new ArrayList<String>();
    	ArrayList<String> servList = new ArrayList<String>();
    	ArrayList<String> uploadedListDel = new ArrayList<String>();
    	
    	hostList = getHost();
    	String string = ".jpg";
    	servList = getServer();
    	ListIterator it = servList.listIterator();
    	while (it.hasNext()){
    		String temp = it.next().toString();
    		if(!(hostList.contains(temp.replace(string, "")))){     //.replace(".png","")))){
    			//temp = temp.concat(string);
    			uploadedListDel.add(temp);
    			boolean att = upload.deleteFile(temp);
    			if (att)
    				Toast.makeText(getBaseContext(),  temp+ " has been deleted (serv)", Toast.LENGTH_SHORT).show();
    			else
    				Toast.makeText(getBaseContext(),  temp+ " has NOT been deleted (serv)", Toast.LENGTH_SHORT).show();
    		}

    	}
    	return(uploadedListDel	);
    }
    
    public ArrayList<String> synchrDown(FTPservice downL) {
    	ArrayList<String> hostList = new ArrayList<String>();
    	ArrayList<String> servList = new ArrayList<String>();
    	ArrayList<String> downloadedList = new ArrayList<String>();
    	
    	String string= ".jpg";
    	hostList = getHost();
    	servList = getServer();
    	ListIterator it = servList.listIterator();
    	while (it.hasNext()){
    		String temp = it.next().toString();
    		if(!(hostList.contains(temp.replace(string, "")))){
    			downloadedList.add(temp);
    			File down = photoStore(temp);//.replace(string, ""));//.replace(string, ""));
    			//FTPservice upload = new FTPservice();
    			boolean att= downL.downloadFile(temp, down);
    			if (att)
    				Toast.makeText(getBaseContext(),  temp+ " has been downloaded", Toast.LENGTH_SHORT).show();
    			else
    				Toast.makeText(getBaseContext(),  temp+ " has FAILED downloading", Toast.LENGTH_SHORT).show();
    		}
    
    		else {
    			if(!(Long.parseLong(getSize(temp.replace(string, ""))) == downL.getSize(temp))) {
    				File toDel = new File(getPath(temp.replace(string, "")));
        			toDel.delete();
        			downloadedList.add(temp);
        			File down = photoStore(temp);//.replace(string, ""));//.replace(string, ""));
        			//FTPservice upload = new FTPservice();
        			boolean att= downL.downloadFile(temp, down);
        			if (att)
        				Toast.makeText(getBaseContext(),  temp+ " has been updated", Toast.LENGTH_SHORT).show();
        			else
        				Toast.makeText(getBaseContext(),  temp+ " has FAILED updated", Toast.LENGTH_SHORT).show();
        	  			
    			}
    		    				
    			}
    	}
    	return(downloadedList);
    }
    public ArrayList<String> synchrDownDel(FTPservice downL) {
    	ArrayList<String> hostList = new ArrayList<String>();
    	ArrayList<String> servList = new ArrayList<String>();
    	ArrayList<String> downDeletedList = new ArrayList<String>();
    	
    	String string= ".jpg";
    	hostList = getHost();
    	servList = getServer();
    	ListIterator it = hostList.listIterator();
    	while (it.hasNext()){
    		String temp = it.next().toString();
    		if(!(servList.contains(temp.concat(string)))){
    			downDeletedList.add(temp.concat(string));
    			File toDel = new File(getPath(temp));
    			boolean del = toDel.delete();
    			if (del) {
    				Toast.makeText(getBaseContext(),  temp.concat(string)+ " has been deleted", Toast.LENGTH_SHORT).show();
    			}
    			else
    				Toast.makeText(getBaseContext(),  temp.concat(string)+ " has FAILED being deleted", Toast.LENGTH_SHORT).show();
    			//.replace(string, ""));
    			//FTPservice upload = new FTPservice();
    			
    			
    		}
    	}
    	return(downDeletedList);
    }
    
    public File photoStore(String albumName) {
        // Get the directory for the user's public pictures directory. 
    	File ext = Environment.getExternalStorageDirectory();
    	File target = new File(ext+"/Pictures/", albumName);
    	
    	/* File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName); */
        if (target == null)/*(!file.mkdirs())*/ {
            Log.e("error:", "Directory not created");
        }
        
        return target;
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	
}
	
	
	
	
	
/*************REFERENCE CODE FOR FURTHER UTILIZATION AND DEVELOPMENT****************************/	
	
	
	
//	startService(new Intent(MainActivity.this, UpService.class));
			//	finish();
				
				
				
				
				//Intent dBase = new Intent(MainActivity.this, DataB.class);
				//startActivity(dBase);
				
				// TODO Auto-generated method stub
				
			
 /*       ListIterator it = uploaded.listIterator();
   	 while(it.hasNext()) {
      
       Toast.makeText(getBaseContext(),  it.next().toString(), Toast.LENGTH_SHORT).show();
      }
*/
       // FTPservice a = new FTPservice();
        //File b = new File("/storage/emulated/0/Contacts001.csv");
        //a.uploadFile(b);*/
      //  lv = (ListView)findViewById(R.id.lv1);
        
    //	ArrayList<String> nDaty = new ArrayList<String>();
		//PhotoProvider a = new PhotoProvider();
		
		//}
		/* Checks if external storage is available for read and write */
		
		//File exStor = new File("bsls");
		//if (isExternalStorageWritable() && isExternalStorageReadable()) {
		//	getAlbumStorageDir(getBaseContext(), "bsls");
			
		//}
		
			
		
		
		
		
		
	//	ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
        //        this, 
         //       android.R.layout.simple_list_item_1,
        //        uploaded );

       //lv.setAdapter(arrayAdapter);
        
       
/*       
	

    @SuppressWarnings("null")
	@SuppressLint("SimpleDateFormat")
	public ArrayList<String> get_some_info() {
    
    	ArrayList<String> Daty = new ArrayList<String>();
    	String[] img = { MediaStore.Images.Media._ID,
    		MediaStore.Images.Media.DATA,
    		MediaStore.Images.Media.DISPLAY_NAME,
    		//MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
    		MediaStore.Images.Media.TITLE,
    		MediaStore.Images.Media.DATE_MODIFIED,
    		MediaStore.Images.Media.DATE_ADDED,
    		MediaStore.Images.Media.DATE_TAKEN
    	//	MediaStore.Images.Media.HEIGHT,
    	//	MediaStore.Images.Media.WIDTH,
    		};
    	String mSelectionClause = null;
    	String[] mSelectionArgs = {null};	
    	
    	String Search = "";
    	if	(!("".equals(Search))) {
    		mSelectionClause = MediaStore.Images.Media.TITLE + " = ?";
    		mSelectionArgs [0] = Search;
    	}
    	else{
    		mSelectionArgs = null;
    	}
    	
    
  
  
    Cursor mCursor;
    mCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, img , mSelectionClause, mSelectionArgs, null);
	
    int indexTITLE = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
    int indexDATE_M = mCursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
     // Only executes if the cursor is valid. The User Dictionary Provider returns null if
     // an internal error occurs. Other providers may throw an Exception instead of returning null.
     

    if (mCursor != null) {
        
        //  Moves to the next row in the cursor. Before the first movement in the cursor, the
         // "row pointer" is -1, and if you try to retrieve data at that position you will get an
         // exception.
         
        while (mCursor.moveToNext()) {

            // Gets the value from the column.
        	
        	String newTitle = null;
            String newDate_M = null;
        	newTitle = mCursor.getString(indexTITLE);
            newDate_M = mCursor.getString(indexDATE_M);
            long l = Long.parseLong(newDate_M);
            Date time=new Date(l*1000);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            newDate_M = sdf.format(time);
            //@SuppressWarnings("deprecation")
            //Date date = new Date(newWord);
            //newWord = new SimpleDateFormat("dd:MM:yyyy", Locale.GERMAN).format(date);
            
            
            
            //String path = null;
            //path = mCursor.getString(index);
            //DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            //newWord = dateFormat.format(date);
            Daty.add(newTitle + " " + newDate_M);		 

            
            
            
            
            // Insert code here to process the retrieved word.

            //...

            // end of while loop
        }
    } 

        // Insert code here to report an error if the cursor is null or the provider threw an exception.
    //mCurs(long)(newWord)or.close(); 
    return(Daty);
   }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
*/