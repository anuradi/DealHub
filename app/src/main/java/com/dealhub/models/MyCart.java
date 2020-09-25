package com.dealhub.models;

public class MyCart {
    String shopname;
    String shoplogourl;
    int offerid;
    int count;

    public MyCart() {
    }

    public MyCart(String shopname, String shoplogourl, int offerid, int count) {
        this.shopname = shopname;
        this.shoplogourl = shoplogourl;
        this.offerid = offerid;
        this.count = count;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShoplogourl() {
        return shoplogourl;
    }

    public void setShoplogourl(String shoplogourl) {
        this.shoplogourl = shoplogourl;
    }

    public int getOfferid() {
        return offerid;
    }

    public void setOfferid(int offerid) {
        this.offerid = offerid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
