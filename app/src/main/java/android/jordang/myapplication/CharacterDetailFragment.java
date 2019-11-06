package android.jordang.myapplication;

import android.content.Context;
import android.jordang.myapplication.db.CharacterItem;
import android.jordang.myapplication.db.CharacterViewModel;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class CharacterDetailFragment extends Fragment {

    TextView mName;
    TextView mTags;
    ImageButton mImage;
    String mFilePath;

    CharacterItem mItem;

    private CharacterViewModel mCharacterViewModel;
    private List<CharacterItem> mItems;

    private static final String TAG = "CHARACTER ITEM FRAGMENT";

    private static final String ARGS_DETAIL = "DETAIL ARGUMENTS";

    public static CharacterDetailFragment newInstance(){
        CharacterDetailFragment fragment = new CharacterDetailFragment();
        return fragment;
    }

    public static CharacterDetailFragment newInstance(CharacterItem item) {
        final Bundle args = new Bundle();
        args.putParcelable(ARGS_DETAIL, item);
        CharacterDetailFragment fragment = new CharacterDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        mCharacterViewModel = ViewModelProviders.of(this).get(CharacterViewModel.class);

        final Observer<List<CharacterItem>> characterListObserver = new Observer<List<CharacterItem>>() {
            @Override
            public void onChanged(List<CharacterItem> items) {

            }
        };

        mCharacterViewModel.getAllItems().observe(this, characterListObserver);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);

        mName = view.findViewById(R.id.name_edit_text);
        mTags = view.findViewById(R.id.tags_edit_text);
        mImage = view.findViewById(R.id.image_character_button);

        setItem(mItem);
        return view;
    }


    public void setItem(CharacterItem item){
        this.mItem = item;
        setAttributes(mItem);
    }

    public void setAttributes(CharacterItem item){
        if (item == null){

        }
        mName.setText(item.getText());
        mTags.setText(item.getTags());
        loadImage();

    }

    public CharacterItem getCurrentItem(){
        return mItem;
    }

    public void loadImage() {
        ImageButton imageButton = mImage;
        String path = mItem.getImagePath();

        if (path != null && !path.isEmpty()){
            Picasso.get()
                    .load(new File(path))
                    .error(android.R.drawable.stat_notify_error)
                    .fit()
                    .centerCrop()
                    .into(imageButton, new Callback(){
                        @Override
                        public void onSuccess(){
                            Log.d(TAG, "Image loaded");
                        }
                        @Override
                        public void onError (Exception e){
                            Log.e(TAG, "Error loading image ", e);
                        }
                    });
        }
    }
}
