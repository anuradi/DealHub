package com.dealhub.models;

public class Favourites {
    String shopname;
    String offerid;

    public Favourites() {
    }

    public Favourites(String shopname, String offerid) {
        this.shopname = shopname;
        this.offerid = offerid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid;
    }
}
