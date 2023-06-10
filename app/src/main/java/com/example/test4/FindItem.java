package com.example.test4;

public class FindItem {

    String name;
    String date;
    String item;
    String imagepath;

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public FindItem(String name, String date, String item, String imagepath) {
        this.name = name;
        this.date = date;
        this.item = item;
        this.imagepath = imagepath;
    }
}
