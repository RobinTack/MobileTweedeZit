package be.pxl.mobiledevelopmentproject.datalayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.pxl.mobiledevelopmentproject.datalayer.IngredientContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "whatscookin.db";
    public static final String tableName = "ingredients";
    public static final int DATABASE_VERSION = 1;
    private SQLiteDatabase mDb;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_INGREDIENTLIST_TABLE ="CREATE TABLE " +
                IngredientEntry.TABLE_NAME + " (" +
                IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                IngredientEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        //db.execSQL("CREATE TABLE "+tableName+ " (Id Integer PRIMARY KEY AUTOINCREMENT, INGREDIENTTEXT TEXT)");
        db.execSQL(SQL_CREATE_INGREDIENTLIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
        onCreate(db);
    }


    public boolean insertDataInIngredients(String ingredient){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", ingredient);
        long result = database.insert(tableName,null,contentValues);
        if (result==-1){
            return false;
        }
        else{
            return true;
        }
    }


}
