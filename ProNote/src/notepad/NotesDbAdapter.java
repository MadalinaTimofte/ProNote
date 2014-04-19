package notepad;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;



public class NotesDbAdapter extends ContentProvider{

	private Context mCtx;
	 // fields for my content provider
	     static final String PROVIDER_NAME = "com.example.note.contentprovider.notesdbadapter";
	     static final String URL = "content://" + PROVIDER_NAME + "/notes";
	     static final Uri CONTENT_URI = Uri.parse(URL);

	     //fields for database
    public static final String KEY_TITLE = "title";
    public static final String KEY_DATE = "date";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";

    //integer values used in content URI
    
    public static final int NOTES = 1;
    public static final int NOTES_ID = 2;
    
    //projection map for a query
  //  private static HashMap<String,String> Notes;
    
    //map content uri patterns to the integer values that were set above
    
    static final UriMatcher uriMatcher;
           static{
              uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
              uriMatcher.addURI(PROVIDER_NAME, "notes", NOTES);
              uriMatcher.addURI(PROVIDER_NAME, "notes/#", NOTES_ID);
           }
           
    //database declaration
    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mhelper;
    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 2;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
    		  "CREATE TABLE if not exists " + DATABASE_TABLE + " (" +
    		   KEY_ROWID + " integer PRIMARY KEY autoincrement," +
    		   KEY_TITLE + "," +
    		   KEY_BODY + "," +
    		   KEY_DATE + "," +
    		   " UNIQUE (" + KEY_ROWID +"));";
    		 
  


    //class that creates and manages the providers database
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }
    
    public boolean onCreate() {
    	        // TODO Auto-generated method stub
    	        Context context = getContext();
    	        mhelper = new DatabaseHelper(context);
    	        // permissions to be writabl
    	        database =mhelper.getWritableDatabase();
    	        if(database == null)
    	            return false;
    	        else
    	            return true;   
    	    }

    @Override
        public Cursor query(Uri uri, String[] projection, String selection,
                String[] selectionArgs, String sortOrder) {
            // TODO Auto-generated method stub
    	
            
             SQLiteDatabase database = mhelper.getWritableDatabase();
             SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
             // the TABLE_NAME to query on
             queryBuilder.setTables(DATABASE_TABLE);
              switch (uriMatcher.match(uri)) {
              // maps all database column names
              case NOTES:
               //   queryBuilder.setProjectionMap(Notes);
                 break;
              case NOTES_ID:
            	  String rowID = uri.getPathSegments().get(1);//getLastPathSegment
                  queryBuilder.appendWhere( KEY_ROWID + "=" + rowID);
                 break;
              default:
                 throw new IllegalArgumentException("Unknown URI " + uri);
              }
           //   if (sortOrder == null || sortOrder == ""){
                 // No sorting-> sort on names by default
           //      sortOrder = NAME;
           //   }
              Cursor cursor = queryBuilder.query(database, projection, selection,
                      selectionArgs, null, null, sortOrder);
              /**
               * register to watch a content URI for changes
               */
           //   cursor.setNotificationUri(getContext().getContentResolver(), uri);
              return cursor;
        }
    
    @Override
        public Uri insert(Uri uri, ContentValues values) {
            // TODO Auto-generated method stub
    	  SQLiteDatabase database = mhelper.getWritableDatabase();
            long row = database.insert(DATABASE_TABLE, "", values);
            // If record is added successfully
              if(row > 0) {
                 Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
                 getContext().getContentResolver().notifyChange(newUri, null);
                 return newUri;
              }
              throw new SQLException("Fail to add a new record into " + uri);
        }
    
    @Override
        public int update(Uri uri, ContentValues values, String selection,
                String[] selectionArgs) {
            // TODO Auto-generated method stub
    	SQLiteDatabase database = mhelper.getWritableDatabase();
             int count = 0;
              switch (uriMatcher.match(uri)){
              case NOTES:
               //  count = database.update(DATABASE_TABLE, values, selection, selectionArgs);
                 break;
              case NOTES_ID:
                 count = database.update(DATABASE_TABLE, values, KEY_ROWID +
                         " = " + uri.getLastPathSegment() +
                         (!TextUtils.isEmpty(selection) ? " AND (" +
                         selection + ')' : ""), selectionArgs);
                 break;
              default:
                 throw new IllegalArgumentException("Unsupported URI " + uri );
              }
              getContext().getContentResolver().notifyChange(uri, null);
              return count;
        }
    
    @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
            // TODO Auto-generated method stub
    	 SQLiteDatabase database = mhelper.getWritableDatabase();
            int count = 0;
             switch (uriMatcher.match(uri)){
              case NOTES:
                  // delete all the records of the table
                  count = database.delete(DATABASE_TABLE, selection, selectionArgs);
                  break;
              case NOTES_ID:
                  String id = uri.getLastPathSegment(); //gets the id
                  count = database.delete( DATABASE_TABLE, KEY_ROWID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                        selection + ')' : ""), selectionArgs);
                  break;
              default:
                  throw new IllegalArgumentException("Unsupported URI " + uri);
              }
              getContext().getContentResolver().notifyChange(uri, null);
              return count;
        }

    @Override
        public String getType(Uri uri) {
            // TODO Auto-generated method stub
            switch (uriMatcher.match(uri)){
              // Get all friend-birthday record
              case NOTES:
                 return "vnd.android.cursor.dir/vnd.com.example.note.contentprovider.notesdbadapter/notes";
              // Get a particular friend
              case NOTES_ID:
                 return "vnd.android.cursor.item/vnd.com.example.note.contentprovider.notesdbadapter/notes";
              default:
                 throw new IllegalArgumentException("Unsupported URI: " + uri);
            }
        }



    public NotesDbAdapter(){
    	
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public NotesDbAdapter(Context ctx) {
      this.mCtx = ctx;
     
   }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
   public NotesDbAdapter open() throws SQLException {
        mhelper = new DatabaseHelper(mCtx);
        database = mhelper.getWritableDatabase();
       return this;
    }

   public void close() {
        mhelper.close();
    }
   
}