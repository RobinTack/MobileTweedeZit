package be.pxl.mobiledevelopmentproject;

import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import be.pxl.mobiledevelopmentproject.datalayer.DatabaseHelper;
import be.pxl.mobiledevelopmentproject.datalayer.IngredientAdapter;


public class IngredientsViewActivityFragment extends android.app.Fragment {
    private ViewGroup container;
    private LayoutInflater inflater;
    private Button button_add;
    private Button findRecipeButton;
    private SQLiteDatabase mDatabase;
    private ConstraintLayout constraintLayoutIng;
    private DatabaseHelper databaseHelper;
    private EditText mEditTextName;
    private IngredientAdapter mAdapter;


    public IngredientsViewActivityFragment() {
        // Required empty public constructor
    }

/*
    public View initializeUserInterface(){
        View view;

        //if there is already a layout inflated, remove it
        if (container != null){
            container.removeAllViewsInLayout();
        }

        //get screen orientation
        int orientation = getActivity().getResources().getConfiguration().orientation;

        //inflate the appropriate layout based on screen orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            view = inflater.inflate(R.layout.ingredient_view, container, false);
        }
        else{ // orientation == orientation == Configuration.ORIENTATION_LANDSCAPE
            view = inflater.inflate(R.layout.fragment_ingredients_view_activity, container, false);
        }
        //instantiate widgets from the layout
        button_add = view.findViewById(R.id.button_add);
        findRecipeButton = view.findViewById(R.id.findRecipeButton);

        button_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addItem();
            }
        });

        return view;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Instantiate our container and inflater handles
        this.container = container;
        this.inflater = inflater;

        inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.ingredient_view,null);
        EditText editText = (EditText) dialogView.findViewById(R.id.edittext_name);

        //Display desired layout and return the view//
        databaseHelper = new DatabaseHelper(getActivity());
        mDatabase = databaseHelper.getWritableDatabase();
        mAdapter = new IngredientAdapter(getActivity(), getAllItems());
        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(dialogView.getContext()));
        mAdapter = new IngredientAdapter(dialogView.getContext(), getAllItems());
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);


        //attach the layout to a handle
        constraintLayoutIng = (ConstraintLayout) dialogView.findViewById(R.id.constraintLayoutIng);

        //load the fragment into the layout handle
        getFragmentManager().beginTransaction()
                .replace(R.id.constraintLayoutIng, new IngredientsViewActivityFragment())
                .commit();

        return initializeUserInterface();

    }
*/
    /**
     * This is called when the user rotates the device
     * @param newConfig Configuration
     */
/*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        //Create the new Layout
        View view = initializeUserInterface();


        //Display the new layout on the screen
        container.addView(view);

        //Call the default method to cover our bases
        super.onConfigurationChanged(newConfig);
    }
*/
/*
    private void onActivityCreated(){
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mEditTextName = v.findViewById(R.id.edittext_name);
                String name = mEditTextName.getText().toString();

                databaseHelper.insertDataInIngredients(name);

                mEditTextName.getText().clear();


                Intent intent = new Intent(getActivity(), IngredientsViewActivity.class);
                startActivity(intent);

            }
        });

        findRecipeButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

               // Intent intent = new Intent(getActivity());
                //startActivity(intent);
            }
        });
    }
*/
/*
    private void addItem(){

        String name = mEditTextName.getText().toString();
        databaseHelper.insertDataInIngredients(name);
        mAdapter.swapCursor(getAllItems());
        mEditTextName.getText().clear();

    }

    private void removeItem(long id){
        mDatabase.delete(IngredientContract.IngredientEntry.TABLE_NAME,
                IngredientContract.IngredientEntry._ID + "=" + id,
                null);
        mAdapter.swapCursor(getAllItems());
    }


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

