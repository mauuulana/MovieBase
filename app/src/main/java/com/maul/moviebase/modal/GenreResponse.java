package com.maul.moviebase.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GenreResponse {

    @SerializedName("genres")
    @Expose
    private ArrayList<Genre> genre;

    public ArrayList<Genre> getGenres() {
        return genre;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genre = genres;
    }
}
