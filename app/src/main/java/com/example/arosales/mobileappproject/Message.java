package com.example.arosales.mobileappproject;

import com.parse.ParseUser;

import java.io.Serializable;

/**
 * Created by aRosales on 28/05/2015.
 */
public class Message implements Serializable {
    private ParseUser from;
    private String subject;
    private String message;

    public ParseUser getFrom() {
        return from;
    }

    public void setFrom(ParseUser from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}