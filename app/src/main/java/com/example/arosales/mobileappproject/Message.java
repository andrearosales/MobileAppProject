package com.example.arosales.mobileappproject;

import java.io.Serializable;

/**
 * Created by aRosales on 28/05/2015.
 */
public class Message implements Serializable {
    private String subject;
    private String message;

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