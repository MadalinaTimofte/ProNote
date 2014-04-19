package notepad;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pronote.R;


public class NoteEdit extends Activity{
	
	public static int numTitle = 1;	
	public static String curDate = "";
	public static String curText = "";	
    private EditText mTitleText;
    private EditText mBodyText;
    private TextView mDateText;
    private Long mRowId;
    private String mode;
    private Cursor note;

    private NotesDbAdapter mDbHelper;
      
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     //   mDbHelper = new NotesDbAdapter(this);
     //   mDbHelper.open();        
        
        setContentView(R.layout.note_edit);
        setTitle(R.string.app_name);

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        mDateText = (TextView) findViewById(R.id.notelist_date);

        long msTime = System.currentTimeMillis();  
        Date curDateTime = new Date(msTime);
 	
        SimpleDateFormat formatter = new SimpleDateFormat("d'/'M'/'y");  
        curDate = formatter.format(curDateTime);        
        
        mDateText.setText(""+curDate);
        

        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mode = extras.getString("mode");
            mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
                                    : null;
        }

        populateFields();
        
       
    }
	
	  public static class LineEditText extends EditText{
			// we need this constructor for LayoutInflater
			public LineEditText(Context context, AttributeSet attrs) {
				super(context, attrs);
					mRect = new Rect();
			        mPaint = new Paint();
			        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			        mPaint.setColor(Color.BLUE);
			}

			private Rect mRect;
		    private Paint mPaint;	    
		    
		    @Override
		    protected void onDraw(Canvas canvas) {
		  
		        int height = getHeight();
		        int line_height = getLineHeight();

		        int count = height / line_height;

		        if (getLineCount() > count)
		            count = getLineCount();

		        Rect r = mRect;
		        Paint paint = mPaint;
		        int baseline = getLineBounds(0, r);

		        for (int i = 0; i < count; i++) {

		            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
		            baseline += getLineHeight();

		        super.onDraw(canvas);
		    }

		}
	  }
	  
	  @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	      //  saveState();
	        outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
	    }
	    
	    @Override
	    protected void onPause() {
	        super.onPause();
	       // saveState();
	    }
	    
	    @Override
	    protected void onResume() {
	        super.onResume();
	        populateFields();
	    }
	    
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.noteedit_menu, menu);
			
			return true;		
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    switch (item.getItemId()) {
		    case R.id.menu_delete:
				if(note != null){
	    			note.close();
	    			note = null;
	    		}
	    		if(mRowId != null){
	    			//mDbHelper.deleteNote(mRowId);
	    			Uri uri = Uri.parse(NotesDbAdapter.CONTENT_URI+"/"+mRowId);
	    			getContentResolver().delete(uri, null, null);
	    		//	mDbHelper.delete(uri,null,null);
	    		}
	    		finish();
		    	
		        return true;
		    case R.id.menu_save:
	    		saveState();
		    	
		    	
	    		finish();	    	
		    default:
		    	return super.onOptionsItemSelected(item);
		    }
		}
	    
	    private void saveState() {
	        String title = mTitleText.getText().toString();
	        String body = mBodyText.getText().toString();
	        String date = mDateText.getText().toString();
	       
	        ContentValues values = new ContentValues();
	    	values.put(NotesDbAdapter.KEY_TITLE, title);
	    	values.put(NotesDbAdapter.KEY_BODY, body);
	    	values.put(NotesDbAdapter.KEY_DATE,date);
	    	
	    	if(mode.trim().equalsIgnoreCase("add")){
	    	getContentResolver().insert(NotesDbAdapter.CONTENT_URI,values);
	    	 	}
	    		  else {
	    			    Uri uri = Uri.parse(NotesDbAdapter.CONTENT_URI + "/" + mRowId);
	    			    getContentResolver().update(uri, values, null, null);
	    			   }	
	    		}
	        
	   
	 private void populateFields() {
	        if (mRowId != null) {
	            String[] projection = { 
	            	    NotesDbAdapter.KEY_ROWID,
	            	    NotesDbAdapter.KEY_TITLE,
	            	    NotesDbAdapter.KEY_BODY,
	            	    NotesDbAdapter.KEY_DATE};
	            	  Uri uri = Uri.parse(NotesDbAdapter.CONTENT_URI + "/" + mRowId);
	            	  Cursor cursor = getContentResolver().query(uri, projection, null, null,
	            	    null);
	            	  if ( cursor.moveToFirst()) {
	            	  // cursor.moveToFirst();
	            	   String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE));
	            	   String body = cursor.getString(cursor.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY));
	            	  String date = cursor.getString(cursor.getColumnIndexOrThrow(NotesDbAdapter.KEY_DATE));
	            	   mTitleText.setText(title);
	            	   mBodyText.setText(body);
	            	   mDateText.setText(date);
	            	  }
	        }
	    }


}
