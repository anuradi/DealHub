package com.dealhub.models;

public class ShopNotifications {
    String id;
    String description;
    String imgurl;

    public ShopNotifications() {

    }

    public ShopNotifications(String id, String description, String imgurl) {
        this.id = id;
        this.description = description;
        this.imgurl = imgurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
