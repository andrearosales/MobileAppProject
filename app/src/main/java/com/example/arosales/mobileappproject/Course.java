package com.example.arosales.mobileappproject;

import java.util.Date;

/**
 * Created by ricardogarcia on 09/03/15.
 */
public class Course {

    private String id;
    private String name;
    private Teacher teacher;
    private Date startDate;
    private Date endDate;

    public Course(String name, Teacher teacher, Date startDate, Date endDate) {
        this.name = name;
        this.teacher = teacher;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {

        return name;
    }

    public Teacher getTeacher() {

        return teacher;
    }


    public void setName(String name) {

        this.name = name;
    }

    public void setTeacher(Teacher teacher) {

        this.teacher = teacher;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
