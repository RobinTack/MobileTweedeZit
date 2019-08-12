package be.pxl.mobiledevelopmentproject;

import android.provider.BaseColumns;

public class IngredientContract {

    private IngredientContract(){}

    public static final class IngredientEntry implements BaseColumns{
        public static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_NAME ="name";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
