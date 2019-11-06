package android.jordang.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ButtonFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "BUTTON_FRAGMENT";

    public interface ButtonClickedListener{
        void buttonClicked(View button);
    }

    private ButtonClickedListener mButtonClickedListener;

    ImageButton mPictureButton;
    ImageButton mCharacterButton;
    ImageButton mNoteButton;

    public static ButtonFragment newInstance() {
        return new ButtonFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);

        if(context instanceof ButtonClickedListener){
            mButtonClickedListener = (ButtonClickedListener) context;
            Log.d(TAG, "On attach configured listener " + mButtonClickedListener);
        } else {
            throw new RuntimeException(context.toString() + "must implement ButtonClickedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button, container, false);

        mPictureButton = view.findViewById(R.id.add_picture_button);
        mPictureButton.setOnClickListener(this);

        mCharacterButton = view.findViewById(R.id.add_character_button);
        mCharacterButton.setOnClickListener(this);

        mNoteButton = view.findViewById(R.id.add_notes_button);
        mNoteButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View button){
        mButtonClickedListener.buttonClicked(button);
    }
}
