package com.example.arosales.mobileappproject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by aRosales on 28/05/2015.
 */
public class Student implements Serializable{

    private String id;
    private String name;
    private String surname;
    private String location;
    private String industry;
    private Date birthdate;
    private String birthplace;
    private String description;
    private List<String> skills;
    private List<String> languages;
    private List<String> interests;
    private int experienceyears;
    private String degree;
    private String phonenumber;
    private Company current_company;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public int getExperienceyears() {
        return experienceyears;
    }

    public void setExperienceyears(int experienceyears) {
        this.experienceyears = experienceyears;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Company getCurrent_company() {
        return current_company;
    }

    public void setCurrent_company(Company current_company) {
        this.current_company = current_company;
    }
}

