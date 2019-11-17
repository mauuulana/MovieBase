package com.maul.favoritemoviebase.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TVShowResponse {
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private ArrayList<TVShow> tvs = null;

    public ArrayList<TVShow> getTvs() {
        return tvs;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
