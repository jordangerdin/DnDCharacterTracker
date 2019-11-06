package android.jordang.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.jordang.myapplication.db.CharacterItem;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class NewCharacterFragment extends Fragment {

    interface SaveChangesListener{
        void saveChanges(CharacterItem item);
        void takePicture(View button);
    }

    private static final String TAG = "NEW_CHARACTER_FRAGMENT";
    private static final String ARGS_EDIT = "args_edit_string";

    private SaveChangesListener mSaveChangesListener;

    private EditText mEditName;
    private EditText mEditTags;
    private ImageButton mEditCharacterImage;

    private CharacterItem mItem = new CharacterItem();

    public static NewCharacterFragment newInstance() {
        NewCharacterFragment fragment = new NewCharacterFragment();
        return fragment;
    }

    public static NewCharacterFragment newInstance(CharacterItem item){
        final Bundle args = new Bundle();
        args.putParcelable(ARGS_EDIT, item);
        NewCharacterFragment newCharacterFragment = new NewCharacterFragment();
        newCharacterFragment.setArguments(args);
        return newCharacterFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        if (context instanceof SaveChangesListener) {
            mSaveChangesListener = (SaveChangesListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SaveChangesListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);

        mEditName = view.findViewById(R.id.name_edit_text);
        mEditTags = view.findViewById(R.id.tags_edit_text);

        mEditCharacterImage = view.findViewById(R.id.image_character_button);
        mEditCharacterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mSaveChangesListener.takePicture(mEditCharacterImage);
            }
        });

        Button addItem = view.findViewById(R.id.save_button);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.d(TAG, "Save button has been clicked");
                if (mEditName == null || mEditTags == null){
                    Toast.makeText(NewCharacterFragment.this.getContext(), "Must include a name and tags", Toast.LENGTH_SHORT).show();
                }

                // Setting data to object
                mItem.setText(mEditName.getText().toString());
                mItem.setTags(mEditTags.getText().toString());
                Date date = new Date();
                mItem.setDateCreated(date);
                mItem.setImagePath("");

                mSaveChangesListener.saveChanges(mItem);
            }
        });


        /*if(getArguments() != null && getArguments().getParcelable(ARGS_EDIT) != null) {
            mItem = getArguments().getParcelable(ARGS_EDIT);
            setCurrentAttributes(mItem);
        }*/

        return view;
    }



    private void setCurrentAttributes(CharacterItem item) {
        mEditName.setText(item.getText());
        mEditTags.setText(item.getTags());
        loadImage();
    }

    public void setImageFilePath(String filePath){
        mItem.setImagePath(filePath);
        loadImage();
    }

    private void loadImage() {

        ImageButton imageButton = mEditCharacterImage;
        String path = mItem.getImagePath();

        if (path != null && !path.isEmpty()) {
            Picasso.get()
                    .load(new File(path))
                    .error(android.R.drawable.stat_notify_error)
                    .fit()
                    .centerCrop()
                    .into(imageButton, new Callback() {
                        @Override
                        public void onSuccess() { Log.d(TAG, "Image laoded"); }
                        @Override
                        public void onError(Exception e) {Log.e(TAG, "error loading image", e); }
                    });
        }
    }
}
