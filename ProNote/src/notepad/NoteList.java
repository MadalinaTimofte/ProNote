package notepad;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.pronote.R;

public class NoteList extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private SimpleCursorAdapter dataAdapter;
	
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
	
    private static final int DELETE_ID = Menu.FIRST;
	private NotesDbAdapter mDbHelper;
   private ArrayList<NoteItem> notes;
    private NoteClassAdapter aa;
    private static final String TAG="Records";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notelist);
	//	mDbHelper = new NotesDbAdapter(this);
	//	mDbHelper.open();
		
		notes = new ArrayList<NoteItem>();
	//	Log.i(TAG,"notes"+ (notes).length());
		int resID = R.layout.notes_row;
	
		aa = new NoteClassAdapter(this,resID,notes);
		setListAdapter(aa);
	//	
		 getLoaderManager().initLoader(0, null, this);
	//	fillData();				
		//registerForContextMenu(getListView());
		Button addnote = (Button)findViewById(R.id.addnotebutton);
		addnote.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				createNote();
				}
		});
		}
	
	 
//	@Override
	//public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.notelist_menu, menu);
	//	return true;		
	//}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	            return super.onOptionsItemSelected(item);
	        }
	    
	
	private void createNote() {
		Intent i = new Intent(this, NoteEdit.class);
		Bundle bundle = new Bundle();
		 bundle.putString("mode", "add");
		 i.putExtras(bundle);
        startActivityForResult(i, ACTIVITY_CREATE);    	
    }
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        NoteItem noteItem = notes.get(position);
    //    ListItem cursor = (ListItem) l.getItemAtPosition(position);
     //   long  row_id = notes.getColumnIndexNotesDbAdapter.KEY_ROWID);
        Intent i = new Intent(this, NoteEdit.class);
        Bundle bundle = new Bundle();
        bundle.putString("mode", "update");
        bundle.putLong(NotesDbAdapter.KEY_ROWID, id);
        i.putExtras(bundle);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

	//private void fillData() {
     // The desired columns to be bound
   //     String[] columns = new String[] {
    //      NotesDbAdapter.KEY_TITLE,
    //      NotesDbAdapter.KEY_DATE
    //    };
       
        // the XML defined views which the data will be bound to
     //   int[] to = new int[] { 
     //     R.id.text1,
     //     R.id.date,
     //   };
       
        // create an adapter from the SimpleCursorAdapter
   //     dataAdapter = new SimpleCursorAdapter(
    //      this, 
    //      R.layout.notes_row, 
    //      null, 
    //      columns, 
    //      to,
     //     0);
        
   //     setListAdapter(dataAdapter);
        //Ensures a loader is initialized and active.
  //      getLoaderManager().initLoader(0, null, this);
        
 //   }
	
//	public void onNewItemAdded(String title, String date) {
	//    ContentResolver cr = getContentResolver();
	    
	//    ContentValues values = new ContentValues();
	//    values.put(NotesDbAdapter.KEY_TITLE, title);
	//    values.put(NotesDbAdapter.KEY_DATE, date);
	//    
	//    cr.insert(NotesDbAdapter.CONTENT_URI, values);
	 //   getLoaderManager().restartLoader(0, null, this);
	//  }
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
               // mDbHelper.deleteNote(info.id);
                
                Uri uri = Uri.parse(NotesDbAdapter.CONTENT_URI + "/" + info.id);
                getContentResolver().delete(uri, null, null);
                
            //    fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
      //  fillData();        
    }   
    
    
    protected void onResume() {
		  super.onResume();
		  //Starts a new or restarts an existing Loader in this manager
		  getLoaderManager().restartLoader(0, null, this);
		 }
	
	
	 // This is called when a new Loader needs to be created.
	 @Override
	 public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	  String[] projection = { 
	    NotesDbAdapter.KEY_ROWID,
	    NotesDbAdapter.KEY_TITLE, 
	    NotesDbAdapter.KEY_BODY, 
	    NotesDbAdapter.KEY_DATE};
	  CursorLoader cursorLoader = new CursorLoader(this,
	    NotesDbAdapter.CONTENT_URI, projection, null, null, null);
	  return cursorLoader;
	 }
	
	 @Override
	 public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	 
	   // dataAdapter.swapCursor(cursor);
	 int keyTaskIndex = cursor.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE);
//	  int date = cursor.getColumnIndexOrThrow(NotesDbAdapter.KEY_DATE);  
	    notes.clear();
	 
	    while (cursor.moveToNext()) {
		      NoteItem newItem = new NoteItem(cursor.getString(keyTaskIndex));
		     notes.add(newItem);
		     int length = notes.size();
				Log.e(TAG,"Records"+ length);
		   }
	    aa.notifyDataSetChanged();
		  }
		 
	 
	 
	 @Override
	 public void onLoaderReset(Loader<Cursor> loader) {
	  // This is called when the last Cursor provided to onLoadFinished()
	  // above is about to be closed.  We need to make sure we are no
	  // longer using it.
	//  dataAdapter.swapCursor(null);
	 }
    
}
