package com.apseducation.studentinformation.bean;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Survey_bean implements Serializable {
    public static final String client_id = "client_id";
    public static final String key_first_name = "first_name";
    public static final String key_father_name = "father_name";
    public static final String key_mobile_no = "mobile_no";
    public static final String key_date_of_birth = "date_of_birth";
    public static final String key_dist = "office_mob_no";
    public static final String key_email_id = "email_id";
    public static final String key_address = "address_";
    public static final String key_pin_code = "pin_code";
    public static final String key_tehsil = "tehsil";
    public static final String key_state = "state";
    public static final String key_height = "height";
    public static final String key_blood = "blood";
    public static final String key_board = "board";
    public static final String key_department = "department";
    public static final String key_yearPass = "year_pass";
    public static final String key_university = "university";
    public static final String key_weight = "weight";
    public static final String key_specialize = "specialize";
    public static final String key_other = "other";
    public static final String key_boardTenth = "boardTen";
    public static final String key_board_Twelve = "twelve";
    public static final String key_yearTenth = "year_ten";
    public static final String key_year_Twelve = "year_twelve";
    public static final String key_yearGrad = "year_graduation";
    public static final String key_subjectTenth = "sub_ten";
    public static final String key_subject_Twelve = "sub_twelve";
    public static final String key_percentageTenth = "ten_percent";
    public static final String key_percentage_Twelve = "twelve_percent";
    public static final String key_percentageGrad = "graduation_percent";
    public static final String key_subjectGrad = "sub_grad";
    public static final String key_boardGrad = "board_grad";
    public static final String key_service = "service";
    public static final String key_force = "force";
    public static final String key_college = "college";
    public static final String key_image = "image";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date date;
    private String Client_id;
    private String first_name;
    private String father_name;
    private String mobile_no;
    private String tehsil;
    private String state;
    private String dist;
    private String email;
    private String address;
    private String pin_code;
    private String height;
    private String blood;
    private String board;
    private String department;
    private String yearPass;
    private String university, boardTen, boardTwelve, yearTen, yearTwelve, subjectTen, subjectTwelve, percentageTen, percentageTwelve, boardGraduation, yeargraduation, subjectGraduation, percentageGraduation;
    private String weight, specialize, force, service, other, college;
    private String image;

    protected Survey_bean(Parcel in) {
        Client_id = in.readString();
        first_name = in.readString();
        father_name = in.readString();
        mobile_no = in.readString();
        dist = in.readString();
        email = in.readString();
        address = in.readString();
        pin_code = in.readString();
    }

    public Survey_bean() {

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYearPass() {
        return yearPass;
    }

    public void setYearPass(String yearPass) {
        this.yearPass = yearPass;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getTehsil() {
        return tehsil;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
    }

    public String getClient_id() {
        return Client_id;
    }

    public void setClient_id(String client_id) {
        Client_id = client_id;
    }

    public String getBoardTen() {
        return boardTen;
    }

    public void setBoardTen(String boardTen) {
        this.boardTen = boardTen;
    }

    public String getBoardTwelve() {
        return boardTwelve;
    }

    public void setBoardTwelve(String boardTwelve) {
        this.boardTwelve = boardTwelve;
    }

    public String getYearTen() {
        return yearTen;
    }

    public void setYearTen(String yearTen) {
        this.yearTen = yearTen;
    }

    public String getYearTwelve() {
        return yearTwelve;
    }

    public void setYearTwelve(String yearTwelve) {
        this.yearTwelve = yearTwelve;
    }

    public String getSubjectTen() {
        return subjectTen;
    }

    public void setSubjectTen(String subjectTen) {
        this.subjectTen = subjectTen;
    }

    public String getSubjectTwelve() {
        return subjectTwelve;
    }

    public void setSubjectTwelve(String subjectTwelve) {
        this.subjectTwelve = subjectTwelve;
    }

    public String getPercentageTen() {
        return percentageTen;
    }

    public void setPercentageTen(String percentageTen) {
        this.percentageTen = percentageTen;
    }

    public String getPercentageTwelve() {
        return percentageTwelve;
    }

    public void setPercentageTwelve(String percentageTwelve) {
        this.percentageTwelve = percentageTwelve;
    }

    public String getBoardGraduation() {
        return boardGraduation;
    }

    public void setBoardGraduation(String boardGraduation) {
        this.boardGraduation = boardGraduation;
    }

    public String getYeargraduation() {
        return yeargraduation;
    }

    public void setYeargraduation(String yeargraduation) {
        this.yeargraduation = yeargraduation;
    }

    public String getSubjectGraduation() {
        return subjectGraduation;
    }

    public void setSubjectGraduation(String subjectGraduation) {
        this.subjectGraduation = subjectGraduation;
    }

    public String getPercentageGraduation() {
        return percentageGraduation;
    }

    public void setPercentageGraduation(String percentageGraduation) {
        this.percentageGraduation = percentageGraduation;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getSpecialize() {
        return specialize;
    }

    public void setSpecialize(String specialize) {
        this.specialize = specialize;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    @Override
    public String toString() {
        return "College = " + college + "\n\n" +
                "Name = " + first_name + "\n\n" +
                "Father Name = " + father_name + "\n\n" +
                "Mobile No. = " + mobile_no + "\n\n" +
                "DOB = " + format.format(date) + "\n\n" +
                "Email = " + email + "\n\n" +
                "Height = " + height + "\n\n" +
                "Blood Group = " + blood + "\n\n" +
                "Weight = " + weight + "\n\n" +
                "Village = " + address + "\n\n" +
                "District = " + dist + "\n\n" +
                "PinCode = " + pin_code + "\n\n" +
                "Tehsil = " + tehsil + "\n\n" +
                "State = " + state + "\n\n" +
                "Courses = " + department + "\n\n" +
                "Tenth Board = " + boardTen + "\n\n" +
                "Tenth Passed Year = " + yearTen + "\n\n" +
                "Twelve Board = " + boardTwelve + "\n\n" +
                "Twelve Passed Year = " + yearTwelve + "\n\n" +
                "Graduation University = " + boardGraduation + "\n\n" +
                "Graduation Passed Year = " + yeargraduation + "\n\n" +
                "Graduation Course = " + subjectGraduation + "\n\n" +
                "Coaching = " + specialize + "\n\n" +
                "Force = " + force + "\n\n" +
                "Government Member = " + service + "\n\n" +
                "Other = " + other + "\n\n"
                ;
    }

}
