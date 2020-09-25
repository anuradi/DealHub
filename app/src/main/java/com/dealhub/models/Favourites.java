package com.dealhub.models;

public class Favourites {
    String shopname;
    int offerid;
    boolean result;

    public Favourites() {
    }

    public Favourites(String shopname, int offerid, boolean result) {
        this.shopname = shopname;
        this.offerid = offerid;
        this.result = result;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public int getOfferid() {
        return offerid;
    }

    public void setOfferid(int offerid) {
        this.offerid = offerid;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
