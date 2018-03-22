package com.reshmi.james.popularmovies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by reshmijames on 3/21/18.
 */

public class Review {

    @SerializedName("author")
    String author;
    @SerializedName("id")
    String id;
    @SerializedName("content")
    String content;
    @SerializedName("url")
    String url;

    public Review(String author, String id, String content, String url) {
        this.author = author;
        this.id = id;
        this.content = content;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
