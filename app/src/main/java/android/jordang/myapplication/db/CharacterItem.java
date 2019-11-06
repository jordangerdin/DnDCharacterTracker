package android.jordang.myapplication.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.DateFormat;
import java.util.Date;

@Entity
public class CharacterItem implements Comparable<CharacterItem>, Parcelable {

    private static DateFormat df = DateFormat.getDateInstance();

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String text;
    private Date dateCreated;
    private String tags;
    private String imagePath;

    public CharacterItem() {

    }

    public CharacterItem(String text, String tags, String imagePath) {
        dateCreated = new Date();
        this.text = text;
        this.tags = tags;
        this.imagePath = imagePath;
    }


    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int describeContents() {
        return 0;
    }

    protected CharacterItem(Parcel in){
        text = in.readString();
        tags = in.readString();
        imagePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(tags);
        dest.writeString(imagePath);
    }

    public int compareTo(CharacterItem item) {
        return this.dateCreated.compareTo(item.dateCreated);
    }
    // TODO fix this
    @Override
    public String toString() {
        return "";
    }

}
