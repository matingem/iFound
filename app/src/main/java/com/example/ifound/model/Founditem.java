package com.example.ifound.model;

public class Founditem
{

    private String title;
    private String discriotion;
    private String email,status;
    private String location;
    private String time;
    private String imageurl,city,key;

    public Founditem() {
    }

    public Founditem(String title, String discriotion, String email, String location, String time, String city,String imageurl,String status,String key) {
        this.title = title;
        this.discriotion = discriotion;
        this.email = email;
        this.location = location;
        this.time = time;
        this.city = city ;
        this.imageurl = imageurl;
        this.status = status;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getDiscriotion() {
        return discriotion;
    }

    public void setDiscriotion(String discriotion) {
        this.discriotion = discriotion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getcity() {
        return city ;
    }

    public void setcity (String city ) {
        this.city  = city ;
    }
}
