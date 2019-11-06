package android.jordang.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.jordang.myapplication.db.CharacterItem;
import android.jordang.myapplication.db.CharacterViewModel;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class CharacterListActivity extends AppCompatActivity implements
        ButtonFragment.ButtonClickedListener,
        ItemListFragment.ListItemSelectedListener,
        NewCharacterFragment.SaveChangesListener {

    private static final String BUNDLE_KEY_CHARACTER_ITEMS = "CHARACTER ITEM ARRAY LIST";

    private static final String TAG_BUTTON_FRAG = "BUTTON FRAGMENT";
    private static final String TAG_LIST_FRAG = "LIST FRAGMENT";
    private static final String TAG_DETAIL = "DETAIL FRAGMENT";
    private static final String TAG_EDIT = "EDIT FRAGMENT";

    private static final String TAG = "MAIN ACTIVITY";

    private CharacterViewModel characterViewModel;
    private ArrayList<CharacterItem> mCharacterItems;
    private String mCurrentImagePath;

    private static int REQUEST_CODE_TAKE_PICTURE = 0;

    CharacterItem mItem;

    private EditText mSearchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create ViewModel, associate with this Activity

        characterViewModel = new CharacterViewModel(getApplication());



        if (savedInstanceState == null) {
            Log.d(TAG, "onCreate has no instance state. Setting up ArrayList");
        }

        ButtonFragment buttonFragment = ButtonFragment.newInstance();
        ItemListFragment itemListFragment = ItemListFragment.newInstance(mCharacterItems);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.button_container, buttonFragment, TAG_BUTTON_FRAG);
        ft.add(R.id.item_list_view_container, itemListFragment, TAG_LIST_FRAG);

        ft.commit();
    }

    @Override
    public void buttonClicked(View button) {
        int buttonID = button.getId();
        // TODO add variations of NewCharacterFragment for picture/character/notes
        switch(buttonID){
            case R.id.add_picture_button:
            case R.id.add_character_button:
            case R.id.add_notes_button:
                callNewCharacterFragment();
                break;
        }
    }

    private void callNewCharacterFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        NewCharacterFragment newCharacterFragment = NewCharacterFragment.newInstance();

        ft.replace(android.R.id.content, newCharacterFragment, TAG_EDIT);

        ft.addToBackStack(TAG_EDIT);

        ft.commit();
    }

    @Override
    public void itemSelected(CharacterItem selected) {
        Log.d(TAG, "Notified that this item was selected " + selected);
        try {
            FragmentManager fm = getSupportFragmentManager();
            CharacterDetailFragment detailFragment = (CharacterDetailFragment) fm.findFragmentByTag(TAG_DETAIL);
            mItem = selected;
            detailFragment.setItem(mItem);
        } catch (NullPointerException npe) {
            mItem = selected;
        }

    }

    @Override
    public void saveChanges(CharacterItem item) {
        FragmentManager fm = getSupportFragmentManager();
        ItemListFragment itemListFragment = (ItemListFragment) fm.findFragmentByTag(TAG_LIST_FRAG);
        itemListFragment.updateList(item);

        FragmentTransaction ft = fm.beginTransaction();
        NewCharacterFragment newCharacterFragment = (NewCharacterFragment) fm.findFragmentByTag(TAG_EDIT);
        if (newCharacterFragment != null) {
            ft.remove(newCharacterFragment);
        }

        ft.commit();
    }

    public void takePicture(View button) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try{
                File imageFile = createImageFile();
                if (imageFile != null) {
                    Uri imageURI = FileProvider.getUriForFile(this, "android.jordang.myapplication.fileprovider", imageFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                    startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PICTURE);
                } else {
                    Log.e(TAG, "Image file is null");
                }
            } catch (IOException e) {
                Log.e(TAG, "Error creating image file " + e);
            }
        }
    }

    private File createImageFile() throws IOException {
        //Create unique filename with timestamp
        String imageFileName = "CHARACTERSHEET_" + new Date().getTime();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void requestSaveImageToMediaStore() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            saveImage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveImage();
        } else {
            Toast.makeText(this, "Images will NOT be saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveImage(){
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), mCurrentImagePath, "Character", "CharacterImage");
            try {
                // TODO stuff
            } catch (NullPointerException npe) {
                Log.d(TAG, "EditPetFragment is null");
            }
        } catch (IOException e) {
            Log.e(TAG, "Image file not found", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult for request code " + requestCode + " and current path " + mCurrentImagePath);
            requestSaveImageToMediaStore();
        } else if (resultCode == RESULT_CANCELED) {
            mCurrentImagePath = "";
        }
    }
}
