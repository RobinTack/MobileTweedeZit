package be.pxl.mobiledevelopmentproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivityFragment extends android.app.Fragment {
    private ViewGroup container;
    private LayoutInflater inflater;
    private EditText editText;
    private TextView textView;
    private Button startCookingButton;
    private Button findSupermarketButton;

    public MainActivityFragment() {
    }

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
            view = inflater.inflate(R.layout.home_view, container, false);
        }
        else{ // orientation == orientation == Configuration.ORIENTATION_LANDSCAPE
            view = inflater.inflate(R.layout.fragment_main_activity, container, false);
        }

        //instantiate widgets from the layout
        startCookingButton = view.findViewById(R.id.startCookingButton);
        findSupermarketButton = view.findViewById(R.id.findSupermarketButton);

        startCookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IngredientsViewActivity.class);
                startActivity(intent);
            }
        });

        findSupermarketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GoogleSuperMarketActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Instantiate our container and inflater handles
        this.container = container;
        this.inflater = inflater;

        //Display desired layout and return the view
        return initializeUserInterface();

    }

    /**
     * This is called when the user rotates the device
     * @param newConfig Configuration
    */

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //Save text that the user has already typed
        //String preserveThisText = editText.getText().toString();

        //Create the new Layout
        View view = initializeUserInterface();

        //Display the text the user has already typed
        //editText.setText(preserveThisText);

        //Display the new layout on the screen
        container.addView(view);

        //Call the default method to cover our bases
        super.onConfigurationChanged(newConfig);
    }
}
