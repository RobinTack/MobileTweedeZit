package be.pxl.mobiledevelopmentproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import be.pxl.mobiledevelopmentproject.datalayer.DatabaseController;
import be.pxl.mobiledevelopmentproject.datalayer.IngredientContract;
import be.pxl.mobiledevelopmentproject.dummy.DummyContent;

public class RecipeListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private DatabaseController database;
    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        Button showRecipesButton = findViewById(R.id.showRecipesButton);
        showRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRecipeSearchQuery();

            }
        });
        recipes = new ArrayList<>();

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
        }

    }

    public void makeRecipeSearchQuery() {
        database = new DatabaseController(this);
        Cursor ingredientCursor = database.getAllItems();
        StringBuilder baseUrl = new StringBuilder("https://api.edamam.com/search?q=");
        while (ingredientCursor.moveToNext()) {
            String name = ingredientCursor.getString(ingredientCursor.getColumnIndex(IngredientContract.IngredientEntry.COLUMN_NAME));
            baseUrl.append(name + "%2C");
        }
        baseUrl.setLength(baseUrl.length() - 3);
        baseUrl.append("&app_id=670fa570&app_key=35dea8300b3ecfd511213bc8313576fc&from=0&to=10&ingr=15");

        try {
            new RecipeSearchTask().execute(new URL(baseUrl.toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public static class RecipeSearchTask extends AsyncTask<URL, String, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL recipeUrl = urls[0];
            String recipeResult = null;
            try {
                recipeResult = NetworkUtils.getResponseFromHttpUrl(recipeUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return recipeResult;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")) {
                try {
                    getRecipe(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private void getRecipe(String JSONString) throws JSONException {
            List<String> ingredients = new ArrayList<>();
            List<Recipe> recipesToAdd = new ArrayList<>();
            JSONObject response = new JSONObject(JSONString);
            System.out.println(response);
            JSONArray hits = response.getJSONArray("hits");
            JSONObject recipe;
            for (int i = 0; i < 10; i++) {
                JSONObject hit = hits.getJSONObject(i);
                recipe = hit.getJSONObject("recipe");
                Recipe recipeToAdd = new Recipe();
                recipeToAdd.setRecipeName(recipe.getString("label"));
                JSONArray ingredientLines = recipe.getJSONArray("ingredientLines");
                for (int j = 0; j < ingredientLines.length(); j++) {

                    String ingredient = ingredientLines.getString(j);
                    ingredients.add(ingredient);
                }

                recipeToAdd.setIngredients(ingredients);
                recipesToAdd.add(recipeToAdd);
                System.out.println(recipeToAdd.getRecipeName() + " " + recipeToAdd.getIngredients());
            }
            RecipeListActivity activity = new RecipeListActivity();
            activity.setRecipes(recipesToAdd);

        }

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, recipes, mTwoPane));
    }



    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private final List<Recipe> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = (Recipe) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, recipe.ingredients.toString());
                    RecipeDetailFragment fragment = new RecipeDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, recipe.ingredients.toString());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeListActivity parent,
                                      List<Recipe> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            System.out.println("CALLING ONBINDVIEWHOLDER ---------------------------------------------------------------------------------------------------");
            holder.mRecipeNameView.setText(mValues.get(position).recipeName);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mRecipeNameView;

            ViewHolder(View view) {
                super(view);
                mRecipeNameView = (TextView) view.findViewById(R.id.id_text);
            }
        }
    }
}
