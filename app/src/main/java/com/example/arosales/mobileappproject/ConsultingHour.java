package com.example.arosales.mobileappproject;

import java.io.Serializable;

/**
 * Created by ricardogarcia on 12/06/15.
 */
public class ConsultingHour{

    private String id;
    private Course course;
    private String time;
    private String place;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
