package com.dealhub.models;

public class MyShops {
    String shopname;
    String address;
    String shopbrno;
    String email;
    String contactno;
    String ownernic;
    String shopimageurl;
    String brurl;
    String nicurl;
    String logourl;
    String status;
    int shopid;

    public MyShops() {

    }

    public MyShops(String shopname, String address, String shopbrno, String email, String contactno, String ownernic, String shopimageurl, String brurl, String nicurl, String logourl, String status, int shopid) {
        this.shopname = shopname;
        this.address = address;
        this.shopbrno = shopbrno;
        this.email = email;
        this.contactno = contactno;
        this.ownernic = ownernic;
        this.shopimageurl = shopimageurl;
        this.brurl = brurl;
        this.nicurl = nicurl;
        this.logourl = logourl;
        this.status = status;
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopbrno() {
        return shopbrno;
    }

    public void setShopbrno(String shopbrno) {
        this.shopbrno = shopbrno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getOwnernic() {
        return ownernic;
    }

    public void setOwnernic(String ownernic) {
        this.ownernic = ownernic;
    }

    public String getShopimageurl() {
        return shopimageurl;
    }

    public void setShopimageurl(String shopimageurl) {
        this.shopimageurl = shopimageurl;
    }

    public String getBrurl() {
        return brurl;
    }

    public void setBrurl(String brurl) {
        this.brurl = brurl;
    }

    public String getNicurl() {
        return nicurl;
    }

    public void setNicurl(String nicurl) {
        this.nicurl = nicurl;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }
}

