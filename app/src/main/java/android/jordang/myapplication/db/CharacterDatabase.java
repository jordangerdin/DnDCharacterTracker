package android.jordang.myapplication.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {CharacterItem.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class CharacterDatabase  extends RoomDatabase {

    private static volatile CharacterDatabase INSTANCE;

    public abstract CharacterDAO characterDAO(); // Abstract method

    static CharacterDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CharacterDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CharacterDatabase.class, "Character").build();
                }
            }
        }
        return INSTANCE;
    }

}
