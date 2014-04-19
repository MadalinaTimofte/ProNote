package com.example.pronote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import camera.Camera_Activity;
import audioRecord.Record_Activity;

public class MainActivity extends Activity{
	
	  private ImageButton noteButton;
	  private ImageButton cameraButton;
	  private ImageButton recordButton;

	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main_layout);
	        
	        noteButton = (ImageButton) findViewById(R.id.note);
	        cameraButton = (ImageButton) findViewById(R.id.camera);
	        recordButton = (ImageButton) findViewById(R.id.record);
	        
	        noteButton.setOnClickListener(new View.OnClickListener() {
	        	 
	            @Override
	            public void onClick(View v) {
	            	
	            }
	        });
	        
	        cameraButton.setOnClickListener(new View.OnClickListener() {
	        	 
	            @Override
	            public void onClick(View v) {
	            	
	            	Intent i = new Intent(MainActivity.this, Camera_Activity.class);
	            	startActivity(i);
	            }
	        });
	        
	        recordButton.setOnClickListener(new View.OnClickListener() {
	        	 
	            @Override
	            public void onClick(View v) {
	            	Intent i = new Intent(MainActivity.this, Record_Activity.class);
	            	startActivity(i);
	            }
	        });

}

}