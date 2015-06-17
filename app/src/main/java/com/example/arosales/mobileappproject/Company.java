package com.example.arosales.mobileappproject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aRosales on 28/05/2015.
 */
public class Company implements Serializable{
    private String id;
    private String name;
    private String location;
    private String address;
    private String industry;
    private String description;
    private int company_size;
    private String website;
    private String clients;


    public String getClients() {
        return clients;
    }

    public void setClients(String clients) {
        this.clients = clients;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCompany_size() {
        return company_size;
    }

    public void setCompany_size(int company_size) {
        this.company_size = company_size;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}

