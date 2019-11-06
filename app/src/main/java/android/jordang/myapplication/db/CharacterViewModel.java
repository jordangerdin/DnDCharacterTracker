package android.jordang.myapplication.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CharacterViewModel extends AndroidViewModel {

    private CharacterRepository repository;

    private LiveData<List<CharacterItem>> allItems;

    public CharacterViewModel(@NonNull Application application) {
        super(application);
        repository = new CharacterRepository(application);
        allItems = repository.getAllItems();
    }

    public LiveData<List<CharacterItem>> getAllItems() {
        return allItems;
    }

    public LiveData<List<CharacterItem>> getFilteredItems(String search) {
        return repository.getFilteredItems(search);
    }

    public void insert(CharacterItem item) {
        repository.insert(item);
    }

    public void update(CharacterItem item) {
        repository.update(item);
    }

    public void delete(CharacterItem item) {
        repository.delete(item);
    }
}
