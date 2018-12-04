package com.digitalsigma.hobrasul.localData;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Me on 10/9/2015.
 */
public class Database_Handler extends SQLiteOpenHelper{


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "NewsUpdate6";

    // Contacts table name
    public static final String TABLE_NAME = "local_data";

    // Contacts Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NUMBER = "number";


    public static final String KEY_DURATION = "duration";


    public Database_Handler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating favorites table..
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                +KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_NUMBER+ " TEXT)";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public boolean newsNoUpdate(String newRow)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();

         cv.put(Database_Handler.KEY_NUMBER, newRow);



       long result= db.insert(Database_Handler.TABLE_NAME,null,cv);
        if (result== -1)
        {
            return false;
        }
        else {
            return true;
        }



    }
}
