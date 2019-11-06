package android.jordang.myapplication.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CharacterRepository {

    private CharacterDAO characterDAO;

    public CharacterRepository(Application application) {
        CharacterDatabase db = CharacterDatabase.getDatabase(application);
        characterDAO = db.characterDAO();
    }

    public void insert(CharacterItem item) {
        // insert record asynchronously (in the background)
        new InsertCharacterAsync(characterDAO).execute(item);
    }

    static class InsertCharacterAsync extends AsyncTask<CharacterItem, Void, Void> {

        private CharacterDAO characterDAO;

        InsertCharacterAsync(CharacterDAO characterDAO) {
            this.characterDAO = characterDAO;
        }

        @Override
        protected Void doInBackground(CharacterItem... characterItems) {
            characterDAO.insert(characterItems);
            return null;
        }
    }

    public void update(CharacterItem item) {
        new UpdateCharacterAsync(characterDAO).execute(item);
    }

    static class UpdateCharacterAsync extends AsyncTask<CharacterItem, Void, Void> {

        private CharacterDAO characterDAO;

        UpdateCharacterAsync(CharacterDAO characterDAO) {
            this.characterDAO = characterDAO;
        }

        @Override
        protected Void doInBackground(CharacterItem... characterItems) {
            characterDAO.update(characterItems);
            return null;
        }
    }

    public void delete(CharacterItem item) {
        new DeleteCharacterAsync(characterDAO).execute(item);
    }

    static class DeleteCharacterAsync extends AsyncTask<CharacterItem, Void, Void> {

        private CharacterDAO characterDAO;

        DeleteCharacterAsync(CharacterDAO characterDAO) {
            this.characterDAO = characterDAO;
        }

        @Override
        protected Void doInBackground(CharacterItem... characterItems) {
            characterDAO.delete(characterItems);
            return null;
        }
    }

    public LiveData<List<CharacterItem>> getAllItems() {
        return characterDAO.getAllItems();
    }

    public LiveData<List<CharacterItem>> getFilteredItems(String search) {
        return characterDAO.getFilteredItems(search);
    }
}
