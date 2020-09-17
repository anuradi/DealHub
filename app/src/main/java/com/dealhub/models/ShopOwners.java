package com.dealhub.models;

public class ShopOwners {
    String id;
    String fname;
    String lname;
    String username;
    String contactno;
    String imageurl;
    String address;

    public ShopOwners() {

    }

    public ShopOwners(String id, String fname, String lname, String username, String contactno, String imageurl, String address) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.username = username;
        this.contactno = contactno;
        this.imageurl = imageurl;
        this.address = address;
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

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
