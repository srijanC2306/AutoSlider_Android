package com.example.simphealbusiness.model;

public class shopModel {


    String address, shopName, picture, shopkey;

    public shopModel(String address, String shopName, String picture, String shopkey) {

        this.address = address;
        this.shopName = shopName;
        this.picture = picture;
        this.shopkey = shopkey;
    }


    public shopModel() {
    }

    public String getShopkey() {
        return shopkey;
    }

    public void setShopkey(String shopkey) {
        this.shopkey = shopkey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
