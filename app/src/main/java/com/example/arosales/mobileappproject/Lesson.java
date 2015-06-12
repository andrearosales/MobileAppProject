package com.example.arosales.mobileappproject;


import java.util.Calendar;
import java.util.Date;

/**
 * Created by ricardogarcia on 12/03/15.
 */
public class Lesson {

    private String id;
    private Course course;
    private Date startTime;
    private Date endTime;
    private String room;



    public Lesson(Course course, Date startTime, Date endTime, String room) {
        this.course = course;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }


    public int getDay() {

        Calendar c = Calendar.getInstance();
        c.setTime(this.startTime);

        return c.get(Calendar.DAY_OF_WEEK);

    }

}



