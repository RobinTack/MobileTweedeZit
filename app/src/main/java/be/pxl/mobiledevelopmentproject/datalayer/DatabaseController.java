package be.pxl.mobiledevelopmentproject.datalayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.EditText;

public class DatabaseController extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private IngredientAdapter mAdapter;
    private ConstraintLayout constraintLayoutIng;
    private DatabaseHelper databaseHelper;
    private EditText mEditTextName;
    private Context mContext;
    private LayoutInflater inflater;


    public DatabaseController(Context context) {
        mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
        mDatabase = databaseHelper.getWritableDatabase();
        mAdapter = new IngredientAdapter(mContext, getAllItems());

    }

    public void addItem(String item){
        //mEditTextName = constraintLayoutIng.findViewById(R.id.edittext_name);
        //String name = mEditTextName.getText().toString();
        databaseHelper.insertDataInIngredients(item);
        mAdapter.swapCursor(getAllItems());

    }

    public void removeItem(long id){
        mDatabase.delete(IngredientContract.IngredientEntry.TABLE_NAME,
                IngredientContract.IngredientEntry._ID + "=" + id,
                null);
        mAdapter.swapCursor(getAllItems());
    }

    public Cursor getAllItems(){

        return mDatabase.query(
                IngredientContract.IngredientEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                IngredientContract.IngredientEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    public SQLiteDatabase getmDatabase() {
        return mDatabase;
    }

    public IngredientAdapter getmAdapter() {
        return mAdapter;
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}
