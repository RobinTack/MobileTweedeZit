package be.pxl.mobiledevelopmentproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import be.pxl.mobiledevelopmentproject.datalayer.DatabaseHelper;

public class MainActivity extends AppCompatActivity implements IActionBar {

    private ConstraintLayout constraintLayout;
    DatabaseHelper helper;
    private SQLiteDatabase mDb;


    /**
     * Clear any existing layout, add the current fragment
     * to the back stack, and load the new fragment
     * @param fragment fragment to load
     */

    public void loadFragment(Fragment fragment){
        //Clear any existing layout
        constraintLayout.removeAllViews();


        //Load the new fragment to the layout
        getFragmentManager().beginTransaction()
                .addToBackStack(null) // Go to the previous fragment when clicking back button
                .replace(R.id.constraintLayout, fragment)
                .commit();

    }


    @Override
    public void onBackPressed() {
        //clear any existing layouts before popping the stack
        if (constraintLayout != null){
            constraintLayout.removeAllViews();
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
        setContentView(R.layout.home_view);

        helper = new DatabaseHelper(this);
        this.mDb = helper.getWritableDatabase();
       /* boolean insert = helper.insertDataInIngredients("Tomato");
        if (insert==true){
            Toast.makeText(getApplicationContext(), "Successfully inserted", Toast.LENGTH_SHORT).show();
        }
*/
        //attach the layout to a handle
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        //load the fragment into the layout handle
        getFragmentManager().beginTransaction()
                .replace(R.id.constraintLayout, new MainActivityFragment())
                .commit();


        getSupportActionBar().hide();


        Button startCookingButton = (Button)findViewById(R.id.startCookingButton);

        Button findMarketButton = (Button)findViewById(R.id.findSupermarketButton);

        startCookingButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, IngredientsViewActivity.class));
            }
        });

        findMarketButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, GoogleSuperMarketActivity.class));
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
}