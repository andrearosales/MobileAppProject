package com.example.arosales.mobileappproject;

/**
 * Created by ricardogarcia on 12/06/15.
 */
public class Notice {

    private String id;
    private String category;
    private String description;
    private Student publisher;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
