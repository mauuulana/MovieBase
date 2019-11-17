package com.maul.moviebase.modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Genre implements Parcelable {

    public static final Parcelable.Creator<Genre> CREATOR = new Parcelable.Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel source) {
            return new Genre(source);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };
    @SerializedName("id")
    @Expose
    private int genre_id;
    @SerializedName("name")
    @Expose
    private String genre_name;

    private Genre(Parcel in) {
        this.genre_id = in.readInt();
        this.genre_name = in.readString();
    }

    public int getId() {
        return genre_id;
    }

    public void setId(int id) {
        this.genre_id = id;
    }

    public String getName() {
        return genre_name;
    }

    public void setName(String name) {
        this.genre_name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.genre_id);
        dest.writeString(this.genre_name);
    }
}
