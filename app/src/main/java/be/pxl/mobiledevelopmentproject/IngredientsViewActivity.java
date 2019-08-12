package be.pxl.mobiledevelopmentproject;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import be.pxl.mobiledevelopmentproject.datalayer.DatabaseController;
import be.pxl.mobiledevelopmentproject.datalayer.DatabaseHelper;
import be.pxl.mobiledevelopmentproject.datalayer.IngredientAdapter;

public class IngredientsViewActivity extends AppCompatActivity implements IActionBar {
    private SQLiteDatabase mDatabase;
    private IngredientAdapter mAdapter;
    private ConstraintLayout constraintLayoutIng;
    private DatabaseHelper databaseHelper;
    public  EditText mEditTextName;
    private DatabaseController databaseController;

/*
    /**
     * Clear any existing layout, add the current fragment
     * to the back stack, and load the new fragment
     * @param fragment fragment to load
     */
/*
    public void loadFragment(Fragment fragment){
        //Clear any existing layout
        constraintLayoutIng.removeAllViews();


        //Load the new fragment to the layout
        getFragmentManager().beginTransaction()
                .addToBackStack(null) // Go to the previous fragment when clicking back button
                .replace(R.id.constraintLayoutIng, fragment)
                .commit();

    }
*/
    @Override
    public void onBackPressed() {
        //clear any existing layouts before popping the stack
        if (constraintLayoutIng != null){
            constraintLayoutIng.removeAllViews();
        }

        //create fragment manager to load previous fragment
        FragmentManager fragmentManager = getFragmentManager();

        //if there are fragments left in the stack, load the previous fragment
        // this may be needed when calling addToBackStack(null) to load fragments
        if (fragmentManager.getBackStackEntryCount() >1){
            fragmentManager.popBackStack();
            return;
        }

        //exit app if there are no more fragments
        super.onBackPressed();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_view);

        //databaseHelper = new DatabaseHelper(this);
        //mDatabase = databaseHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseController = new DatabaseController(this);
        //mAdapter = new IngredientAdapter(this, getAllItems());
        recyclerView.setAdapter(databaseController.getmAdapter());


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
               databaseController.removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);


        //attach the layout to a handle
        constraintLayoutIng = (ConstraintLayout) findViewById(R.id.constraintLayoutIng);

        //load the fragment into the layout handle
        getFragmentManager().beginTransaction()
                .replace(R.id.constraintLayoutIng, new IngredientsViewActivityFragment())
                .commit();

        toolbar();

        mEditTextName = findViewById(R.id.edittext_name);

        Button buttonAdd = findViewById(R.id.button_add);
        Button findRecipeButton = findViewById(R.id.findRecipeButton);


        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String ingredient = mEditTextName.getText().toString();
                databaseController.addItem(ingredient);
                mEditTextName.getText().clear();
            }
        });


        findRecipeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(IngredientsViewActivity.this, RecipeListActivity.class));
            }
        });


    }

    public void toolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Welcome");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    /*
    private Cursor getAllItems(){
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
*/
}