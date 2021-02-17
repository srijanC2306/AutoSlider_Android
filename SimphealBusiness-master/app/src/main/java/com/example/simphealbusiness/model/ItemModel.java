package com.example.simphealbusiness.model;

import java.awt.font.NumericShaper;

public class ItemModel {





    private  String name, description, image, price, discount, qty, size, umedID, storeId ;


    public ItemModel(String name, String description,  String image, String price,  String size, String qty, String discount, String umedID, String storeId) {

        this.umedID = umedID;
        this.storeId = storeId;
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.qty = qty;
        this.size = size;
    }

    public String getUmedID() {
        return umedID;
    }

    public void setUmedID(String umedID) {
        this.umedID = umedID;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


}
