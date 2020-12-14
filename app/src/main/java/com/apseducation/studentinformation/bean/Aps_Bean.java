package com.apseducation.studentinformation.bean;

public class Aps_Bean {
    public static final String key_college_id = "college_id";
    public static final String key_college_names = "college_name";
    public static final String key_course_id = "course_id";
    public static final String key_course_name = "course_name";
    public static final String key_course_price="course_price";
    String collegeName;
    String courses;
    int coursePrice;
    String college_id;
    String course_id;

    public Aps_Bean() {

    }

    public String getCollege_id() {
        return college_id;
    }

    public void setCollege_id(String college_id) {
        this.college_id = college_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public int getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(int coursePrice) {
        this.coursePrice = coursePrice;
    }

    public String getCourses() {
        return courses;
    }

    public void setCourses(String courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Aps_Bean{" +
                "collegeName='" + collegeName + '\'' +
                ", courses='" + courses + '\'' +
                ", coursePrice=" + coursePrice +
                ", college_id='" + college_id + '\'' +
                ", course_id='" + course_id + '\'' +
                '}';
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
}
