package com.dealhub.models;

public class Comments {
    String offerid;
    String comment;
    String profileurl;
    String publisher;
    String publishername;

    public Comments() {
    }

    public Comments(String offerid, String comment, String profileurl, String publisher, String publishername) {
        this.offerid = offerid;
        this.comment = comment;
        this.profileurl = profileurl;
        this.publisher = publisher;
        this.publishername = publishername;
    }

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishername() {
        return publishername;
    }

    public void setPublishername(String publishername) {
        this.publishername = publishername;
    }
}
