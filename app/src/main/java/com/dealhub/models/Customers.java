package com.dealhub.models;

public class Customers {
    String id;
    String fname;
    String lname;
    String username;
    String imageurl;
    String bio;

    public Customers() {
    }

    public Customers(String id, String fname, String lname, String username, String imageurl, String bio) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.username = username;
        this.imageurl = imageurl;
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
