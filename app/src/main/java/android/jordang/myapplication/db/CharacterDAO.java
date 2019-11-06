package android.jordang.myapplication.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CharacterDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CharacterItem... ci);

    @Update
    void update(CharacterItem... ci);

    @Delete
    void delete(CharacterItem... ci);

    @Query("SELECT * FROM CharacterItem")
    LiveData<List<CharacterItem>> getAllItems();

    @Query("SELECT * FROM CharacterItem WHERE tags LIKE :search")
    LiveData<List<CharacterItem>> getFilteredItems(String search);
}
