package android.jordang.myapplication;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.jordang.myapplication.db.CharacterItem;
import android.jordang.myapplication.db.CharacterViewModel;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends Fragment implements ItemListAdapter.OnListItemClickListener {

    private static final String TAG = "ITEM_LIST_FRAGMENT";
    private static final String ARGS_ITEM_LIST = "items list arguments";

    public interface ListItemSelectedListener {
        void itemSelected(CharacterItem selected);
    }

    private ListItemSelectedListener mItemSelectedListener;

    private RecyclerView mListView;
    private ItemListAdapter mListAdapter;
    private List<CharacterItem> mItems;

    private CharacterViewModel mCharacterViewModel;

    public static ItemListFragment newInstance(ArrayList<CharacterItem> items) {
        final Bundle args = new Bundle();
        args.putParcelableArrayList(ARGS_ITEM_LIST, items);
        final ItemListFragment fragment = new ItemListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {

        Log.d(TAG, "onAttach");
        super.onAttach(context);

        if (context instanceof ListItemSelectedListener) {
            mItemSelectedListener = (ListItemSelectedListener) context;
            Log.d(TAG, "On attach configured listener " + mItemSelectedListener);
        } else {
            throw new RuntimeException(context.toString() + " must implemeent ListItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mListView = view.findViewById(R.id.character_listview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mListView.setLayoutManager(layoutManager);

        mItems = new ArrayList<>();

        if(getArguments() != null) {
            mItems = getArguments().getParcelableArrayList(ARGS_ITEM_LIST);
        }

        mListAdapter = new ItemListAdapter(mItems, this);
        mListView.setAdapter(mListAdapter);
        mListAdapter.notifyDataSetChanged();

        Log.d(TAG, "onCreateView, ArrayList: " + mItems);

        return view;
    }

    @Override
    public void onListItemClick(int itemPosition) {
        CharacterItem item = mItems.get(itemPosition);
        mItemSelectedListener.itemSelected(item);
    }

    @Override
    public void onListLongClick(int itemPosition) {
        Log.d(TAG, "List item LONG clicked: " + itemPosition);

        AlertDialog confirmDeleteDialog = new AlertDialog.Builder(this.getContext())
                .setMessage(getString(R.string.delete_item_message, mItems.get(itemPosition) ))
                .setTitle(getString(R.string.delete_dialog_title))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        confirmDeleteDialog.show();

        return ;
    }

    public void updateList(CharacterItem item) {
        if (mItems.contains(item)) {
            int itemIndex = mItems.indexOf(item);
            mItems.remove(item);
            mCharacterViewModel.update(item);
            mListAdapter.notifyItemRemoved(itemIndex);

            mItems.add(item);
            mListAdapter.notifyItemInserted(mItems.size()-1);

        } else {
            mItems.add(item);
            mCharacterViewModel.insert(item);
            mListAdapter.notifyItemInserted(mItems.size()-1);
        }
    }
}
