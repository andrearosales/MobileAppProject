package com.example.arosales.mobileappproject;

import com.parse.ParseFile;

import java.io.Serializable;

/**
 * Created by ricardogarcia on 12/06/15.
 */
public class Notice implements Serializable{

    private String id;
    private String title;
    private String category;
    private String description;
    private Student publisher;
    private ParseFile photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Student getPublisher() {
        return publisher;
    }

    public void setPublisher(Student publisher) {
        this.publisher = publisher;
    }

    public ParseFile getPhoto() {
        return photo;
    }

    public void setPhoto(ParseFile photo) {
        this.photo = photo;
    }
}
