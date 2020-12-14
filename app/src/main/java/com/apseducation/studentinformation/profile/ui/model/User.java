package com.apseducation.studentinformation.profile.ui.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;

    public User() {

    }

    public User(int id, String name, String email, String password, String mobile_no) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile_no = mobile_no;
    }

    private String mobile_no;

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
