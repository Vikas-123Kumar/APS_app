package com.apseducation.studentinformation.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Registration_Bean implements Serializable {
    public static final String key_client_Id = "client_id";
    public static final String key_name = "first_name";
    public static final String key_father = "father_name";
    public static final String key_mobile = "mobile_no";
    public static final String key_whatsapp = "whatsapp_no";
    public static final String key_date_birth = "date_of_birth";
    public static final String key_dist_name = "dist_name";
    public static final String key_email = "email_id";
    public static final String key_address_name = "address_";
    public static final String key_pin = "pin_code";
    public static final String key_father_occupation = "father_occupation";
    public static final String key_height_register = "height";
    public static final String key_state_register = "state";
    public static final String key_defence = "defence";
    public static final String key_boardTen = "boardTen";
    public static final String key_boardTwelve = "twelve";
    public static final String key_yearTen = "year_ten";
    public static final String key_yearTwelve = "year_twelve";
    public static final String key_yearGraduation = "year_graduation";
    public static final String key_subjectTen = "sub_ten";
    public static final String key_subjectTwelve = "sub_twelve";
    public static final String key_percentageTen = "ten_percent";
    public static final String key_percentageTwelve = "twelve_percent";
    public static final String key_percentageGraduation = "graduation_percent";
    public static final String key_subjectGraduation = "sub_grad";
    public static final String key_maritalSpinner = "matitel_spin";
    public static final String key_category = "category";
    public static final String key_boardGraduation = "board_grad";
    public static final String key_identification = "identification";
    public static final String key_college_name = "college";
    public static final String key_weight_reg = "weight";
    public static final String key_image_profile = "image";
    public static final String key_percentAspiring = "percent_aspiring";
    public static final String key_yearAspiring = "year_aspiring";
    public static final String key_subjectAspiring = "subject_aspiring";
    public static final String key_boardAspiring = "board_aspiring";
    public static final String key_image_identity = "imageIdentity";
    public static final String key_image_tenth = "imageTenth";
    public static final String key_image_twelve = "imageTwelve";
    public static final String key_image_aspiring = "imageAspiring";
    public static final String key_image_grad = "imageGrad";
//    public static final String key_coursePrice = "price";
    public static final String key_blood_register = "blood";
//    public static final String key_PayMent_Status = "payment_status";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String Client_id;
    private String first_name;
    private String father_name;
    private String mobile_no;
    private Date date_of_birth;
    private String dist_name;
    private String address;
    private String college;
    private String pin_code;
    private String state_register;
    private String email;
    private String identification;
    private String height_register;
    private String father_occupation;
    private String education_id, defence, boardTen, boardTwelve, yearTen, yearTwelve, subjectTen, subjectTwelve, percentageTen, percentageTwelve, boardGraduation, yeargraduation, subjectGraduation,
            spinnerMarital, percentageGraduation, spinnerCategory, weight, image, yearAspiring, boardAspiring, subjectAspiring, percentageAspiring, imageIdentity, imageTenth, imageTwelve, imageAspiring,
            imageGrad, blood, paymentStatus, price, whatsappNo;

    public Registration_Bean() {
    }


    // setter and getter method of registration bean.
    public static String getKey_name() {
        return key_name;
    }

    public String getImageIdentity() {
        return imageIdentity;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setImageIdentity(String imageIdentity) {
        this.imageIdentity = imageIdentity;
    }

    public String getImageTenth() {
        return imageTenth;
    }

    public void setImageTenth(String imageTenth) {
        this.imageTenth = imageTenth;
    }

    public String getImageTwelve() {
        return imageTwelve;
    }

    public void setImageTwelve(String imageTwelve) {
        this.imageTwelve = imageTwelve;
    }

    public String getImageAspiring() {
        return imageAspiring;
    }


    public String getImageGrad() {
        return imageGrad;
    }

    public void setImageGrad(String imageGrad) {
        this.imageGrad = imageGrad;
    }

    public String getCollege() {
        return college;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getYearAspiring() {
        return yearAspiring;
    }

    public void setYearAspiring(String yearAspiring) {
        this.yearAspiring = yearAspiring;
    }

    public String getBoardAspiring() {
        return boardAspiring;
    }

    public void setBoardAspiring(String boardAspiring) {
        this.boardAspiring = boardAspiring;
    }

    public String getSubjectAspiring() {
        return subjectAspiring;
    }

    public void setSubjectAspiring(String subjectAspiring) {
        this.subjectAspiring = subjectAspiring;
    }

    public String getWhatsappNo() {
        return whatsappNo;
    }

    public void setWhatsappNo(String whatsappNo) {
        this.whatsappNo = whatsappNo;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getPercentageAspiring() {
        return percentageAspiring;
    }

    public void setPercentageAspiring(String percentageAspiring) {
        this.percentageAspiring = percentageAspiring;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getSpinnerCategory() {
        return spinnerCategory;
    }

    public void setSpinnerCategory(String spinnerCategory) {
        this.spinnerCategory = spinnerCategory;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEducation_id() {
        return education_id;
    }

    public void setEducation_id(String education_id) {
        this.education_id = education_id;
    }

    public String getState_register() {
        return state_register;
    }

    public static String getKey_client_Id() {
        return key_client_Id;
    }

    public String getDefence() {
        return defence;
    }

    public void setDefence(String defence) {
        this.defence = defence;
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

    public String getSpinnerMarital() {
        return spinnerMarital;
    }

    public void setSpinnerMarital(String spinnerMarital) {
        this.spinnerMarital = spinnerMarital;
    }

    public String getPercentageGraduation() {
        return percentageGraduation;
    }

    public void setPercentageGraduation(String percentageGraduation) {
        this.percentageGraduation = percentageGraduation;
    }

    public void setState_register(String state) {
        this.state_register = state;
    }

    public String getHeight_register() {
        return height_register;
    }

    public void setHeight_register(String height_register) {
        this.height_register = height_register;
    }

    public String getFather_occupation() {
        return father_occupation;
    }

    public void setFather_occupation(String father_occupation) {
        this.father_occupation = father_occupation;
    }


    public String getClient_id() {
        return Client_id;
    }

    public void setClient_id(String client_id) {
        Client_id = client_id;
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

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getDist_register() {
        return dist_name;
    }

    public void setDist_register(String dist) {
        this.dist_name = dist;
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


    @Override
    public String toString() {
        return "College = " + college + "\n\n" +
                "Name = " + first_name + "\n\n" +
                "Father Name = " + father_name + "\n\n" +
                "Mobile No = " + mobile_no + "\n\n" +
                "Whatsapp No. = " + whatsappNo + "\n\n" +
                "DOB = " + dateFormat.format(date_of_birth) + "\n\n" +
                "Father Occupation= " + father_occupation + "\n\n" +
                "Email = " + email + "\n\n" +
                "Blood Group = " + blood + "\n\n" +
                "Height = " + height_register + "\n\n" +
                "Weight = " + weight + "\n\n" +
                "Village = " + address + "\n\n" +
                "PinCode = " + pin_code + "\n\n" +
                "District = " + dist_name + "\n\n" +
                "State = " + state_register + "\n\n" +
                "Category = " + spinnerCategory + "\n\n" +
                "Marital Status = " + spinnerMarital + "\n\n" +
                "Courses = " + defence + "\n\n" +
                "Tenth Board = " + boardTen + "\n\n" +
                "Tenth Year = " + yearTen + "\n\n" +
                "Tenth Subject = " + subjectTen + "\n\n" +
                "Tenth Percentage = " + percentageTen + "\n\n" +
                "Twelve Board = " + boardTwelve + "\n\n" +
                "Twelve Year = " + yearTwelve + "\n\n" +
                "Twelve Subject = " + subjectTwelve + "\n\n" +
                "Twelve Percentage = " + percentageTwelve + "\n\n" +
                "Graduation University = " + boardGraduation + "\n\n" +
                "Graduation Passed Year = " + yeargraduation + "\n\n" +
                "Graduation Course = " + subjectGraduation + "\n\n" +
                "Graduation Percentage = " + percentageGraduation + "\n\n" +
                "Id Proof =" + identification + "\n\n"
                ;
    }


}
