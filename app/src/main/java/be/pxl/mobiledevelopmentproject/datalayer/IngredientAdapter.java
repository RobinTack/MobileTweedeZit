package be.pxl.mobiledevelopmentproject.datalayer;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.pxl.mobiledevelopmentproject.R;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private Context mContext;
    private Cursor mCursor;


    public IngredientAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }


    public class IngredientViewHolder extends RecyclerView.ViewHolder{
        public TextView nameText;


        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.textView_name_item);

        }
    }


    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ingredient_item, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex(IngredientContract.IngredientEntry.COLUMN_NAME));
        long id = mCursor.getLong(mCursor.getColumnIndex(IngredientContract.IngredientEntry._ID));


        holder.nameText.setText(name);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if (mCursor!=null){
            mCursor.close();
        }
        mCursor = newCursor;

        if (newCursor != null){
            notifyDataSetChanged();
        }
    }
}
