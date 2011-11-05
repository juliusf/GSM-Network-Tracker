package test.jwf.network.provider;



	
	import android.content.Context;
	import android.database.Cursor;
	import android.database.sqlite.SQLiteDatabase;
	import android.database.sqlite.SQLiteOpenHelper;
	import android.database.sqlite.SQLiteStatement;
	import android.util.Log;
	 
import java.io.File;
	import java.util.ArrayList;
import java.util.List;
	 
	public class DatabaseStorageProvider {
	 
	   private static final String DATABASE_NAME = "GsmNetworkTrack.db";
	   private static final int DATABASE_VERSION = 1;
	   private static final String TABLE_NAME = "data";
	   private File dbFile;
	 
	   private Context context;
	   private SQLiteDatabase db;
	 
	   private SQLiteStatement insertStmt;
	   private static final String INSERT = "insert into "
	       + "data (cellID, type, locationArea,cinr,longitude,latitude,accuracy) values (?,?,?,?,?,?,?)";
	private static final String TAG = "GSMTrack-Database";
	 
	   public  DatabaseStorageProvider(Context context) {
	      this.context = context;
	      SQLiteStatement createStatement;
	      OpenHelper openHelper = new OpenHelper(this.context);
	      dbFile = new File("/sdcard/NetworkTracker.sqlite");
	      db = SQLiteDatabase.openOrCreateDatabase(dbFile, null); 
	      Log.v(TAG,"Database open?: " + db.isOpen() );
	      
	      createStatement = this.db.compileStatement("CREATE TABLE IF NOT EXISTS data (id INTEGER PRIMARY KEY, cellID TEXT, type TEXT, locationArea TEXT, cinr TEXT, longitude TEXT, latitude TEXT, accuracy TEXT)");
	      createStatement.execute();
	   }
	   //id INTEGER PRIMARY KEY, cellId TEXT, type TEXT, LocationArea TEXT, cinr TEXT, Long TEXT, Lat TEXT, acc TEXT)
	   public long insert(String cellId, String type, String LAC, String cinr, String Longi, String Lat, String acc) {
	    insertStmt =  db.compileStatement(INSERT);
		  this.insertStmt.bindString(1, cellId);
	      this.insertStmt.bindString(2, type);
	      this.insertStmt.bindString(3, LAC);
	      this.insertStmt.bindString(4, cinr);
	      this.insertStmt.bindString(5, Longi);
	      this.insertStmt.bindString(6, Lat);
	      this.insertStmt.bindString(7, acc);
	      Log.e(TAG, insertStmt.toString());
	      return this.insertStmt.executeInsert();
	   }
	 
	   public void deleteAll() {
	      this.db.delete(TABLE_NAME, null, null);
	   }
	 
	   public List<String> selectAll() {
	      List<String> list = new ArrayList<String>();
	      Cursor cursor = this.db.query(TABLE_NAME, new String[] { "name" }, 
	        null, null, null, null, "name desc");
	      if (cursor.moveToFirst()) {
	         do {
	            list.add(cursor.getString(0)); 
	         } while (cursor.moveToNext());
	      }
	      if (cursor != null && !cursor.isClosed()) {
	         cursor.close();
	      }
	      return list;
	   }
	 
	   private static class OpenHelper extends SQLiteOpenHelper {
	 
	      OpenHelper(Context context) {
	         super(context, DATABASE_NAME, null, DATABASE_VERSION);
	      }
	 
	      @Override
	      public void onCreate(SQLiteDatabase db) {
	         db.execSQL("CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY, cellId TEXT, type TEXT, LocationArea TEXT, cinr TEXT, Long TEXT, Lat TEXT, acc TEXT)");
	      }
	 
	      @Override
	      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	         Log.w("Example", "Upgrading database, this will drop tables and recreate.");
	         db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	         onCreate(db);
	      }
	   }
	}

