package com.apseducation.studentinformation.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class User_Register_Bean implements Parcelable {

    private int user_id;
    private String user_full_name;
    private String user_email;
    private String user_mob;
    private String user_pass;

    public String getUser_designation() {
        return user_designation;
    }

    public void setUser_designation(String user_designation) {
        this.user_designation = user_designation;
    }

    private String user_designation;



    public User_Register_Bean() {

    }

    public User_Register_Bean(int user_id, String user_full_name, String user_email, String user_mob, String user_pass) {
        this.user_id = user_id;
        this.user_full_name = user_full_name;
        this.user_email = user_email;
        this.user_mob = user_mob;
        this.user_pass = user_pass;

    }

    protected User_Register_Bean(Parcel in) {

        user_full_name = in.readString();
        user_email = in.readString();
        user_mob = in.readString();
        user_pass = in.readString();

    }

    public static final Creator<User_Register_Bean> CREATOR = new Creator<User_Register_Bean>() {
        @Override
        public User_Register_Bean createFromParcel(Parcel in) {
            return new User_Register_Bean(in);
        }

        @Override
        public User_Register_Bean[] newArray(int size) {
            return new User_Register_Bean[size];
        }
    };


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public void setUser_full_name(String user_full_name) {
        this.user_full_name = user_full_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_mob() {
        return user_mob;
    }

    public void setUser_mob(String user_mob) {
        this.user_mob = user_mob;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(String.valueOf(user_id));
        dest.writeString(user_full_name);
        dest.writeString(user_email);
        dest.writeString(user_mob);
        dest.writeString(user_pass);

    }

    @Override
    public String toString() {
        return "User_Register_Bean{" +
                "user_id='" + user_id + '\'' +
                ", user_full_name='" + user_full_name + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_mob='" + user_mob + '\'' +
                ", user_pass='" + user_pass + '\'' +

                '}';
    }
}
