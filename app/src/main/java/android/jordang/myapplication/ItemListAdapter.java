package android.jordang.myapplication;

import android.jordang.myapplication.db.CharacterItem;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {

    private static final String TAG = "ITEM LIST ADAPTER";

    interface OnListItemClickListener {
        void onListItemClick(int position);
        void onListLongClick(int position);
    }

    private static List<CharacterItem> mCharacterItems;

    private OnListItemClickListener listener;

    public ItemListAdapter(List<CharacterItem> items, OnListItemClickListener listener) {
        this.listener = listener;
    }

    void setCharacterItems(List<CharacterItem> items){
        this.mCharacterItems = items;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView text;
        TextView date;
        TextView tags;
        ImageView image;
        String photoPath;

        private OnListItemClickListener listener;

        public ItemViewHolder(@NonNull View itemView, OnListItemClickListener listener) {
            super(itemView);

            this.listener = listener;

            ConstraintLayout layout= (ConstraintLayout) itemView;
            text = layout.findViewById(R.id.character_title);
            date = layout.findViewById(R.id.character_date);
            tags = layout.findViewById(R.id.character_tag);
            image = layout.findViewById(R.id.character_thumbnail);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onListItemClick(getAdapterPosition());
            Log.d(TAG, "Item clicked");
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onListLongClick(getAdapterPosition());
            Log.d(TAG, "Item LONG clicked");
            return true;
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_character_element, parent, false);
        return new ItemViewHolder(layout, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CharacterItem item = mCharacterItems.get(position);
        holder.text.setText(item.getText());
        holder.date.setText(item.getDateCreated().toString());
        holder.tags.setText(item.getTags());
        //holder.image.setImageURI(Uri.fromFile());
    }

    @Override
    public int getItemCount() {
        if (mCharacterItems == null) {
            return 0;
        }
        return mCharacterItems.size();
    }
}
