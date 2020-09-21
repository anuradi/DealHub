package com.dealhub.models;

public class MyOffers {
    int offerid;
    String shopname;
    String offerdescription;
    String offerprice;
    String offerdiscount;
    String offerimageurl;
    String expdate;
    String status;
    int likes;
    String shoplogourl;

    public MyOffers() {
    }

    public MyOffers(int offerid, String shopname, String offerdescription, String offerprice, String offerdiscount, String offerimageurl, String expdate, String status, int likes, String shoplogourl) {
        this.offerid = offerid;
        this.shopname = shopname;
        this.offerdescription = offerdescription;
        this.offerprice = offerprice;
        this.offerdiscount = offerdiscount;
        this.offerimageurl = offerimageurl;
        this.expdate = expdate;
        this.status = status;
        this.likes = likes;
        this.shoplogourl = shoplogourl;
    }

    public int getOfferid() {
        return offerid;
    }

    public void setOfferid(int offerid) {
        this.offerid = offerid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getOfferdescription() {
        return offerdescription;
    }

    public void setOfferdescription(String offerdescription) {
        this.offerdescription = offerdescription;
    }

    public String getOfferprice() {
        return offerprice;
    }

    public void setOfferprice(String offerprice) {
        this.offerprice = offerprice;
    }

    public String getOfferdiscount() {
        return offerdiscount;
    }

    public void setOfferdiscount(String offerdiscount) {
        this.offerdiscount = offerdiscount;
    }

    public String getOfferimageurl() {
        return offerimageurl;
    }

    public void setOfferimageurl(String offerimageurl) {
        this.offerimageurl = offerimageurl;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getShoplogourl() {
        return shoplogourl;
    }

    public void setShoplogourl(String shoplogourl) {
        this.shoplogourl = shoplogourl;
    }
}
