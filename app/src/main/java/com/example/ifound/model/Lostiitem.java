package com.example.ifound.model;

public class Lostiitem
{

    private String title;

    private String key,imageurl;

    public Lostiitem() {
    }

    public Lostiitem(String key, String imageurl) {
        this.title = title;
        this.key = key;
        this.imageurl = imageurl;


    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
