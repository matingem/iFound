package com.example.ifound;

public class Users {
    public String fullName;
    public String email;
    public String password;
    public String id;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Users() {
    }

    public Users(String Name, String Email, String password, String id) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.id = id;
    }
    public Users(String Email,String Name) {
        this.fullName = Name;
        this.email = Email;
    }
}
