package com.example.test4;

public class UpdateItem {
    String id;
    String name;
    String date;
    String item;
    String imagepath;


    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }
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

    public UpdateItem(String id, String name, String date, String item, String imagepath) {
        this.name = name;
        this.date = date;
        this.item = item;
        this.imagepath = imagepath;
        this.id = id;
    }
}
