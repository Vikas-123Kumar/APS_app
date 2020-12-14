package com.apseducation.studentinformation.profile.ui.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.apseducation.studentinformation.bean.Aps_Bean;
import com.apseducation.studentinformation.bean.EventBean;
import com.apseducation.studentinformation.bean.Registration_Bean;
import com.apseducation.studentinformation.bean.Survey_bean;
import com.apseducation.studentinformation.bean.User_Register_Bean;
import com.apseducation.studentinformation.profile.ui.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.apseducation.studentinformation.bean.EventBean.key_address_e;
import static com.apseducation.studentinformation.bean.EventBean.key_contact_e;
import static com.apseducation.studentinformation.bean.EventBean.key_email_e;
import static com.apseducation.studentinformation.bean.EventBean.key_event_date;
import static com.apseducation.studentinformation.bean.EventBean.key_event_id;
import static com.apseducation.studentinformation.bean.EventBean.key_event_name;
import static com.apseducation.studentinformation.bean.EventBean.key_event_venue;
import static com.apseducation.studentinformation.bean.EventBean.key_fathername;
import static com.apseducation.studentinformation.bean.EventBean.key_id_e;
import static com.apseducation.studentinformation.bean.EventBean.key_id_event;
import static com.apseducation.studentinformation.bean.EventBean.key_name_e;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_address_name;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_blood_register;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_boardAspiring;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_boardGraduation;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_boardTen;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_boardTwelve;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_category;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_client_Id;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_college_name;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_date_birth;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_defence;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_dist_name;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_email;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_father;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_father_occupation;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_height_register;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_identification;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_image_grad;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_image_identity;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_image_profile;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_image_tenth;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_image_twelve;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_maritalSpinner;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_mobile;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_name;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_percentageGraduation;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_percentageTen;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_percentageTwelve;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_pin;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_state_register;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_subjectAspiring;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_subjectGraduation;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_subjectTen;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_subjectTwelve;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_weight_reg;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_whatsapp;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_yearAspiring;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_yearGraduation;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_yearTen;
import static com.apseducation.studentinformation.bean.Registration_Bean.key_yearTwelve;
import static com.apseducation.studentinformation.bean.Survey_bean.client_id;
import static com.apseducation.studentinformation.bean.Survey_bean.key_address;
import static com.apseducation.studentinformation.bean.Survey_bean.key_blood;
import static com.apseducation.studentinformation.bean.Survey_bean.key_boardGrad;
import static com.apseducation.studentinformation.bean.Survey_bean.key_boardTenth;
import static com.apseducation.studentinformation.bean.Survey_bean.key_board_Twelve;
import static com.apseducation.studentinformation.bean.Survey_bean.key_college;
import static com.apseducation.studentinformation.bean.Survey_bean.key_date_of_birth;
import static com.apseducation.studentinformation.bean.Survey_bean.key_department;
import static com.apseducation.studentinformation.bean.Survey_bean.key_dist;
import static com.apseducation.studentinformation.bean.Survey_bean.key_email_id;
import static com.apseducation.studentinformation.bean.Survey_bean.key_father_name;
import static com.apseducation.studentinformation.bean.Survey_bean.key_first_name;
import static com.apseducation.studentinformation.bean.Survey_bean.key_force;
import static com.apseducation.studentinformation.bean.Survey_bean.key_height;
import static com.apseducation.studentinformation.bean.Survey_bean.key_image;
import static com.apseducation.studentinformation.bean.Survey_bean.key_mobile_no;
import static com.apseducation.studentinformation.bean.Survey_bean.key_other;
import static com.apseducation.studentinformation.bean.Survey_bean.key_pin_code;
import static com.apseducation.studentinformation.bean.Survey_bean.key_service;
import static com.apseducation.studentinformation.bean.Survey_bean.key_specialize;
import static com.apseducation.studentinformation.bean.Survey_bean.key_state;
import static com.apseducation.studentinformation.bean.Survey_bean.key_subjectGrad;
import static com.apseducation.studentinformation.bean.Survey_bean.key_tehsil;
import static com.apseducation.studentinformation.bean.Survey_bean.key_weight;
import static com.apseducation.studentinformation.bean.Survey_bean.key_yearGrad;
import static com.apseducation.studentinformation.bean.Survey_bean.key_yearTenth;
import static com.apseducation.studentinformation.bean.Survey_bean.key_year_Twelve;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "User_Manager.db";
    private static final String TABLE_REGISTRATION = "registeration";
    public static final String TABLE_USER_REGISTER = "table_user_register";
    private static final String TABLE_SURVEY = "table_user_survey";
    public static final String TABLE_EMAIL_SURVEY = "email_survey";
    public static final String TABLE_EMAIL_REGISTRATION = "email_register";
    public static final String TABLE_COLLEGE_NAME = "college_name";
    public static final String TABLE_COURSE_NAME = "course_name";
    public static final String TABLE_EVENT_REGISTER = "event_table_register";
    public static final String TABLE_EVENT = "event";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_USER_PASSWORD = "user_password";
    public static final String COLUMN_USER_MOBILE = "user_mobile";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_DESIGNATION = "designation";
    private DataBaseHelper context = this;
    // drop table sql query
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER_REGISTER;
    private String DROP_SERVAY_TABLE = "DROP TABLE IF EXISTS " + TABLE_SURVEY;
    private String DROP_REGISTERATION_TABLE = "DROP TABLE IF EXISTS " + TABLE_REGISTRATION;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        create_init(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void create_init(SQLiteDatabase db) {
        // create table in sql query
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER_REGISTER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_DESIGNATION + " TEXT," + COLUMN_USER_MOBILE + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
        String CREATE_SURVEY_TABLE = "CREATE TABLE " + TABLE_SURVEY + "("
                + client_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + key_first_name + " TEXT," + key_father_name + " TEXT," + key_date_of_birth + " DATE," + key_address + " TEXT,"
                + key_mobile_no + " TEXT," + key_dist + " TEXT," + key_college + " TEXT," + key_pin_code + " TEXT," + key_tehsil + " TEXT," + key_blood + " TEXT," + key_height + " TEXT," + key_state + " TEXT," + key_email_id + " TEXT,"
                + key_department + " TEXT," + key_force + " TEXT," + key_service + " TEXT," + key_specialize + " TEXT," + key_boardTenth + " TEXT," + key_board_Twelve + " TEXT,"
                + key_boardGraduation + " TEXT," + key_yearTenth + " TEXT," + key_year_Twelve + " TEXT," + key_subjectGrad + " TEXT," + key_yearGraduation + " TEXT," + key_other + " TEXT," + key_weight + " TEXT," + key_image + " TEXT" + ")";
        db.execSQL(CREATE_SURVEY_TABLE);
        String CREATE_REGISTRATION_TABLE = "CREATE TABLE " + TABLE_REGISTRATION + "("
                + key_client_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," + key_name + " TEXT," + key_father + " TEXT," + key_mobile + " TEXT," + key_whatsapp + " TEXT," + key_address_name + " TEXT," + key_dist_name + " TEXT," + key_pin + " TEXT," + key_father_occupation + " TEXT,"
                + key_date_birth + " TEXT," + key_email + " TEXT," + key_college_name + " TEXT," + key_state_register + " TEXT," + key_height_register + " TEXT," + key_percentageGraduation + " TEXT," + key_percentageTwelve + " TEXT," + key_percentageTen + " TEXT," + key_boardGraduation + " TEXT,"
                + key_boardTwelve + " TEXT," + key_boardTen + " TEXT," + key_yearGraduation + " TEXT," + key_yearTwelve + " TEXT," + key_yearTen + " TEXT," + key_defence + " TEXT," + key_subjectGraduation + " TEXT,"
                + key_subjectTwelve + " TEXT," + key_subjectTen + " TEXT," + key_weight_reg + " TEXT," + key_image_profile + " TEXT," + key_maritalSpinner + " TEXT,"
                + key_boardAspiring + " TEXT," + key_subjectAspiring + " TEXT," + key_yearAspiring + " TEXT," + key_identification + " TEXT," + key_image_identity + " TEXT,"
                + key_image_tenth + " TEXT," + key_image_twelve + " TEXT," + key_image_grad + " TEXT," + key_blood_register + " TEXT," + key_category + " TEXT" + ")";
        db.execSQL(CREATE_REGISTRATION_TABLE);
        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENT_REGISTER + "("
                + key_id_e + " INTEGER PRIMARY KEY AUTOINCREMENT," + key_name_e + " TEXT," + key_fathername + " TEXT," + key_address_e + " TEXT," + key_contact_e + " TEXT,"
                + key_event_id + " TEXT" + ")";

        db.execSQL(CREATE_EVENT_TABLE);
        String CREATE_EVENT = "CREATE TABLE " + TABLE_EVENT + "("
                + key_id_event + " INTEGER PRIMARY KEY AUTOINCREMENT," + key_event_id + " TEXT," + key_event_name + " TEXT," + key_event_venue + " TEXT," + key_event_date + " TEXT" + ")";
        db.execSQL(CREATE_EVENT);
        String CREATE_EMAIL_SURVEY = "CREATE TABLE " + TABLE_EMAIL_SURVEY + "(" + key_email_id + " TEXT," + key_college + " TEXT" + ")";
        db.execSQL(CREATE_EMAIL_SURVEY);
        String create_college_table = "CREATE TABLE " + TABLE_COLLEGE_NAME + "(" + Aps_Bean.key_college_id + " TEXT," + Aps_Bean.key_college_names + " TEXT" + ")";
        db.execSQL(create_college_table);
        String create_course_table = "CREATE TABLE " + TABLE_COURSE_NAME + "(" + Aps_Bean.key_course_id + " TEXT," + Aps_Bean.key_course_name + " TEXT," + Aps_Bean.key_course_price + " TEXT" + ")";
        db.execSQL(create_course_table);

        String CREATE_EMAIL_REGISTRATION = "CREATE TABLE " + TABLE_EMAIL_REGISTRATION + "(" + key_email + " TEXT," + key_college_name + " TEXT" + ")";
//        db.execSQL(CREATE_EMAIL_REGISTRATION);
    }

    public void addUser(User_Register_Bean user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, user.getUser_full_name());
        contentValues.put(COLUMN_USER_EMAIL, user.getUser_email());
        contentValues.put(COLUMN_USER_PASSWORD, user.getUser_pass());
        contentValues.put(COLUMN_USER_MOBILE, user.getUser_mob());
        contentValues.put(COLUMN_DESIGNATION, user.getUser_designation());
        // insert row
        sqLiteDatabase.insert(TABLE_USER_REGISTER, null, contentValues);
        sqLiteDatabase.close();
    }

    public void addCollege(Aps_Bean aps_bean) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Aps_Bean.key_college_id, aps_bean.getCollege_id());
        contentValues.put(Aps_Bean.key_college_names, aps_bean.getCollegeName());
        sqLiteDatabase.insert(TABLE_COLLEGE_NAME, null, contentValues);
        sqLiteDatabase.close();
        Log.e("aps", aps_bean.getCollegeName());
    }

    public void addCourse(Aps_Bean aps_bean) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Aps_Bean.key_course_id, aps_bean.getCourse_id());
        contentValues.put(Aps_Bean.key_course_name, aps_bean.getCourses());
        contentValues.put(Aps_Bean.key_course_price, aps_bean.getCoursePrice());
        sqLiteDatabase.insert(TABLE_COURSE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public void addSurveyEmail(Survey_bean survey_bean) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(key_email_id, survey_bean.getEmail());
        contentValues.put(key_college, survey_bean.getCollege());
        sqLiteDatabase.insert(TABLE_EMAIL_SURVEY, null, contentValues);
        sqLiteDatabase.close();

    }

    public ArrayList<Aps_Bean> getCollegeForLocal() {
        String str_reg = "SELECT * FROM " + TABLE_COLLEGE_NAME;
        ArrayList<Aps_Bean> apsList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(str_reg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Aps_Bean aps_bean = new Aps_Bean();
                    aps_bean.setCollege_id(cursor.getString(cursor.getColumnIndex(Aps_Bean.key_college_id)));
                    aps_bean.setCollegeName(cursor.getString(cursor.getColumnIndex(Aps_Bean.key_college_names)));
                    apsList.add(aps_bean);
                } while (cursor.moveToNext());
            }
        }
        return apsList;
    }

    public ArrayList<Aps_Bean> getCourseForLocal() {
        String str_reg = "SELECT * FROM " + TABLE_COURSE_NAME;
        ArrayList<Aps_Bean> apsList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(str_reg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Aps_Bean aps_bean = new Aps_Bean();
                    aps_bean.setCourse_id(cursor.getString(cursor.getColumnIndex(Aps_Bean.key_course_id)));
                    aps_bean.setCourses(cursor.getString(cursor.getColumnIndex(Aps_Bean.key_course_name)));
                    aps_bean.setCoursePrice(cursor.getInt(cursor.getColumnIndex(Aps_Bean.key_course_price)));
                    apsList.add(aps_bean);
                } while (cursor.moveToNext());
            }
        }
        return apsList;
    }

    public void insert_eventDetail(EventBean event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues event_value = new ContentValues();
        event_value.put(key_event_id, event.getEventId());
        event_value.put(key_event_venue, event.getEventVenue());
        event_value.put(key_event_date, event.getEventDate());
        event_value.put(key_event_name, event.getEventName());
        try {
            sqLiteDatabase.insert(TABLE_EVENT, null, event_value);
            sqLiteDatabase.close();
            Log.e("Event Created", "" + event);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public ArrayList<EventBean> getEventDetail() {
        ArrayList<EventBean> arrayEvent = new ArrayList<>();
        String str_reg = "SELECT * FROM " + TABLE_EVENT;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(str_reg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    EventBean eventBean = new EventBean();
                    eventBean.setIdEvent(cursor.getString(cursor.getColumnIndex(key_id_event)));
                    eventBean.setEventName(cursor.getString(cursor.getColumnIndex(key_event_name)));
                    eventBean.setEventDate(cursor.getString(cursor.getColumnIndex(key_event_date)));
                    eventBean.setEventVenue(cursor.getString(cursor.getColumnIndex(key_event_venue)));
                    eventBean.setEventId(cursor.getString(cursor.getColumnIndex(key_event_id)));
                    arrayEvent.add(eventBean);
                } while (cursor.moveToNext());
            }
        }
        return arrayEvent;
    }

    public void insert_event(EventBean event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues event_value = new ContentValues();
        event_value.put(key_name_e, event.getNameE());
        event_value.put(key_fathername, event.getFatherName());
        event_value.put(key_contact_e, event.getContactE());
        event_value.put(key_address_e, event.getAddressE());
        event_value.put(key_event_id, event.getEventId());
        try {
            sqLiteDatabase.insert(TABLE_EVENT_REGISTER, null, event_value);
            sqLiteDatabase.close();
            Log.e("Event Created", "" + event);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public ArrayList<EventBean> getEvent() {
        ArrayList<EventBean> arrayEvent = new ArrayList<>();
        String str_reg = "SELECT * FROM " + TABLE_EVENT_REGISTER;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(str_reg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    EventBean eventBean = new EventBean();
                    eventBean.setIdE(cursor.getString(cursor.getColumnIndex(key_id_e)));
                    eventBean.setNameE(cursor.getString(cursor.getColumnIndex(key_name_e)));
                    eventBean.setContactE(cursor.getString(cursor.getColumnIndex(key_contact_e)));
                    eventBean.setAddressE(cursor.getString(cursor.getColumnIndex(key_address_e)));
                    eventBean.setFatherName(cursor.getString(cursor.getColumnIndex(key_fathername)));
                    eventBean.setEventId(cursor.getString(cursor.getColumnIndex(key_event_id)));
                    arrayEvent.add(eventBean);
                } while (cursor.moveToNext());
            }
        }
        return arrayEvent;
    }

    public void insertSurvey(Survey_bean survey_bean) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues survey_value = new ContentValues();
        survey_value.put(key_first_name, survey_bean.getFirst_name());
        survey_value.put(key_father_name, survey_bean.getFather_name());
        survey_value.put(key_date_of_birth, dateFormat.format(survey_bean.getDate()));
        survey_value.put(key_address, survey_bean.getAddress());
        survey_value.put(key_email_id, survey_bean.getEmail());
        survey_value.put(key_mobile_no, survey_bean.getMobile_no());
        survey_value.put(key_dist, survey_bean.getDist());
        survey_value.put(key_pin_code, survey_bean.getPin_code());
        survey_value.put(key_height, survey_bean.getHeight());
        survey_value.put(key_blood, survey_bean.getBlood());
        survey_value.put(key_state, survey_bean.getState());
        survey_value.put(key_tehsil, survey_bean.getTehsil());
        survey_value.put(key_yearGrad, survey_bean.getYeargraduation());
        survey_value.put(key_year_Twelve, survey_bean.getYearTwelve());
        survey_value.put(key_yearTenth, survey_bean.getYearTen());
        survey_value.put(key_boardGrad, survey_bean.getBoardGraduation());
        survey_value.put(key_board_Twelve, survey_bean.getBoardTwelve());
        survey_value.put(key_boardTenth, survey_bean.getBoardTen());
        survey_value.put(key_subjectGrad, survey_bean.getSubjectGraduation());
        survey_value.put(key_department, survey_bean.getDepartment());
        survey_value.put(key_weight, survey_bean.getWeight());
        survey_value.put(key_specialize, survey_bean.getSpecialize());
        survey_value.put(key_other, survey_bean.getOther());
        survey_value.put(key_force, survey_bean.getForce());
        survey_value.put(key_service, survey_bean.getService());
        survey_value.put(key_college, survey_bean.getCollege());
        survey_value.put(key_image, survey_bean.getImage());
        try {
            sqLiteDatabase.insert(TABLE_SURVEY, null, survey_value);
            sqLiteDatabase.close();
            Log.e("User Created", "" + survey_bean);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void setDate(Cursor cursor, Survey_bean employeeBean) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex(key_date_of_birth)));
            Log.e("date", "" + date);

        } catch (ParseException e) {
            Log.e("date conversion error", "Could not string to date during fetch");
            e.printStackTrace();
        }
        employeeBean.setDate(date);
    }

    private void setDateRegister(Cursor cursor, Registration_Bean employeeBean) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex(key_date_birth)));
            Log.e("date", "" + date);

        } catch (ParseException e) {
            Log.e("date conversion error", "Could not string to date during fetch");
            e.printStackTrace();
        }
        employeeBean.setDate_of_birth(date);
    }

    public void insert_registeration(Registration_Bean registration_bean) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues registration_value = new ContentValues();
        registration_value.put(key_name, registration_bean.getFirst_name());
        registration_value.put(key_father, registration_bean.getFather_name());
        registration_value.put(key_email, registration_bean.getEmail());
        registration_value.put(key_mobile, registration_bean.getMobile_no());
        registration_value.put(key_whatsapp, registration_bean.getWhatsappNo());
        registration_value.put(key_address_name, registration_bean.getAddress());
        registration_value.put(key_date_birth, dateFormat.format(registration_bean.getDate_of_birth()));
        registration_value.put(key_dist_name, registration_bean.getDist_register());
        registration_value.put(key_pin, registration_bean.getPin_code());
        registration_value.put(key_father_occupation, registration_bean.getFather_occupation());
        registration_value.put(key_height_register, registration_bean.getHeight_register());
        registration_value.put(key_state_register, registration_bean.getState_register());
        registration_value.put(key_percentageGraduation, registration_bean.getPercentageGraduation());
        registration_value.put(key_percentageTwelve, registration_bean.getPercentageTwelve());
        registration_value.put(key_percentageTen, registration_bean.getPercentageTen());
        registration_value.put(key_subjectGraduation, registration_bean.getSubjectGraduation());
        registration_value.put(key_subjectTwelve, registration_bean.getSubjectTwelve());
        registration_value.put(key_subjectTen, registration_bean.getSubjectTen());
        registration_value.put(key_yearGraduation, registration_bean.getYeargraduation());
        registration_value.put(key_yearTwelve, registration_bean.getYearTwelve());
        registration_value.put(key_yearTen, registration_bean.getYearTen());
        registration_value.put(key_boardGraduation, registration_bean.getBoardGraduation());
        registration_value.put(key_boardTwelve, registration_bean.getBoardTwelve());
        registration_value.put(key_boardTen, registration_bean.getBoardTen());
        registration_value.put(key_defence, registration_bean.getDefence());
//        registration_value.put(key_PayMent_Status, registration_bean.getPaymentStatus());
        registration_value.put(key_maritalSpinner, registration_bean.getSpinnerMarital());
        registration_value.put(key_identification, registration_bean.getIdentification());
        registration_value.put(key_category, registration_bean.getSpinnerCategory());
        registration_value.put(key_weight_reg, registration_bean.getWeight());
//        registration_value.put(key_coursePrice, registration_bean.getPrice());
        registration_value.put(key_college_name, registration_bean.getCollege());
        registration_value.put(key_boardAspiring, registration_bean.getBoardAspiring());
        registration_value.put(key_subjectAspiring, registration_bean.getSubjectAspiring());
        registration_value.put(key_yearAspiring, registration_bean.getYearAspiring());
        registration_value.put(key_image_profile, registration_bean.getImage());
        registration_value.put(key_image_identity, registration_bean.getImageIdentity());
        registration_value.put(key_image_tenth, registration_bean.getImageTenth());
        registration_value.put(key_image_twelve, registration_bean.getImageTwelve());
        registration_value.put(key_image_grad, registration_bean.getImageGrad());
        registration_value.put(key_blood_register, registration_bean.getBlood());
        //registeration_value.put();
        try {
            long value = sqLiteDatabase.insert(TABLE_REGISTRATION, null, registration_value);
            sqLiteDatabase.close();
            if (value > 0) {
                Log.e("table registeration", registration_bean + "");
                Log.e("User Created", "" + registration_value);
            }
        } catch (Exception ex) {
            Log.e("exception", ex.getMessage());

        }
    }

    public Registration_Bean getNewRegistrationByEmail(String keyword) {
        String str_reg = "SELECT * FROM " + TABLE_REGISTRATION + " where " + key_email + "='" + keyword + "'";
        Registration_Bean registration_bean;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(str_reg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    registration_bean = new Registration_Bean();
                    registration_bean.setClient_id(cursor.getString(cursor.getColumnIndex(key_client_Id)));
                    registration_bean.setFirst_name(cursor.getString(cursor.getColumnIndex(key_name)));
                    registration_bean.setFather_name(cursor.getString(cursor.getColumnIndex(key_father)));
                    registration_bean.setMobile_no(cursor.getString(cursor.getColumnIndex(key_mobile)));
                    registration_bean.setWhatsappNo(cursor.getString(cursor.getColumnIndex(key_whatsapp)));
                    registration_bean.setAddress(cursor.getString(cursor.getColumnIndex(key_address_name)));
                    registration_bean.setDist_register(cursor.getString(cursor.getColumnIndex(key_dist_name)));
                    setDateRegister(cursor, registration_bean);
                    registration_bean.setFather_occupation(cursor.getString(cursor.getColumnIndex(key_father_occupation)));
                    registration_bean.setEmail(cursor.getString(cursor.getColumnIndex(key_email)));
                    registration_bean.setPin_code(cursor.getString(cursor.getColumnIndex(key_pin)));
                    registration_bean.setHeight_register(cursor.getString(cursor.getColumnIndex(key_height_register)));
                    registration_bean.setState_register(cursor.getString(cursor.getColumnIndex(key_state_register)));
                    registration_bean.setPercentageGraduation(cursor.getString(cursor.getColumnIndex(key_percentageGraduation)));
                    registration_bean.setPercentageTwelve(cursor.getString(cursor.getColumnIndex(key_percentageTwelve)));
                    registration_bean.setPercentageTen(cursor.getString(cursor.getColumnIndex(key_percentageTen)));
                    registration_bean.setBoardGraduation(cursor.getString(cursor.getColumnIndex(key_boardGraduation)));
                    registration_bean.setYeargraduation(cursor.getString(cursor.getColumnIndex(key_yearGraduation)));
                    registration_bean.setYearTwelve(cursor.getString(cursor.getColumnIndex(key_yearTwelve)));
                    registration_bean.setYearTen(cursor.getString(cursor.getColumnIndex(key_yearTen)));
                    registration_bean.setBoardTwelve(cursor.getString(cursor.getColumnIndex(key_boardTwelve)));
                    registration_bean.setBoardTen(cursor.getString(cursor.getColumnIndex(key_boardTen)));
                    registration_bean.setDefence(cursor.getString(cursor.getColumnIndex(key_defence)));
                    registration_bean.setSpinnerMarital(cursor.getString(cursor.getColumnIndex(key_maritalSpinner)));
                    registration_bean.setSubjectGraduation(cursor.getString(cursor.getColumnIndex(key_subjectGraduation)));
                    registration_bean.setSubjectTwelve(cursor.getString(cursor.getColumnIndex(key_subjectTwelve)));
                    registration_bean.setSubjectTen(cursor.getString(cursor.getColumnIndex(key_subjectTen)));
                    registration_bean.setCollege(cursor.getString(cursor.getColumnIndex(key_college_name)));
                    registration_bean.setSpinnerCategory(cursor.getString(cursor.getColumnIndex(key_category)));
                    registration_bean.setWeight(cursor.getString(cursor.getColumnIndex(key_weight_reg)));
//                    registration_bean.setPaymentStatus(cursor.getString(cursor.getColumnIndex(key_PayMent_Status)));
                    registration_bean.setIdentification(cursor.getString(cursor.getColumnIndex(key_identification)));
                    registration_bean.setImage(cursor.getString(cursor.getColumnIndex(key_image_profile)));
                    registration_bean.setImageGrad(cursor.getString(cursor.getColumnIndex(key_image_grad)));
                    registration_bean.setImageIdentity(cursor.getString(cursor.getColumnIndex(key_image_identity)));
                    registration_bean.setImageTenth(cursor.getString(cursor.getColumnIndex(key_image_tenth)));
                    registration_bean.setImageTwelve(cursor.getString(cursor.getColumnIndex(key_image_twelve)));
//                    registration_bean.setPrice(cursor.getString(cursor.getColumnIndex(key_coursePrice)));
                    registration_bean.setYearAspiring(cursor.getString(cursor.getColumnIndex(key_yearAspiring)));
                    registration_bean.setBoardAspiring(cursor.getString(cursor.getColumnIndex(key_boardAspiring)));
                    registration_bean.setSubjectAspiring(cursor.getString(cursor.getColumnIndex(key_subjectAspiring)));
                    registration_bean.setBlood(cursor.getString(cursor.getColumnIndex(key_blood_register)));

                    if (registration_bean.getEmail().equals(keyword)) {
                        return registration_bean;
                    }
                } while (cursor.moveToNext());
            }
        }
        return new Registration_Bean();
    }

    public ArrayList<Registration_Bean> getNewRegistration() {
        ArrayList<Registration_Bean> registration_list = new ArrayList<>();
        String str_reg = "SELECT * FROM " + TABLE_REGISTRATION;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(str_reg, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Registration_Bean registration_bean = new Registration_Bean();
                    registration_bean.setClient_id(cursor.getString(cursor.getColumnIndex(key_client_Id)));
                    registration_bean.setFirst_name(cursor.getString(cursor.getColumnIndex(key_name)));
                    registration_bean.setFather_name(cursor.getString(cursor.getColumnIndex(key_father)));
                    registration_bean.setMobile_no(cursor.getString(cursor.getColumnIndex(key_mobile)));
                    registration_bean.setWhatsappNo(cursor.getString(cursor.getColumnIndex(key_whatsapp)));
                    registration_bean.setAddress(cursor.getString(cursor.getColumnIndex(key_address_name)));
                    registration_bean.setDist_register(cursor.getString(cursor.getColumnIndex(key_dist_name)));
                    setDateRegister(cursor, registration_bean);
                    registration_bean.setFather_occupation(cursor.getString(cursor.getColumnIndex(key_father_occupation)));
                    registration_bean.setEmail(cursor.getString(cursor.getColumnIndex(key_email)));
                    registration_bean.setPin_code(cursor.getString(cursor.getColumnIndex(key_pin)));
                    registration_bean.setHeight_register(cursor.getString(cursor.getColumnIndex(key_height_register)));
                    registration_bean.setState_register(cursor.getString(cursor.getColumnIndex(key_state_register)));
                    registration_bean.setPercentageGraduation(cursor.getString(cursor.getColumnIndex(key_percentageGraduation)));
                    registration_bean.setPercentageTwelve(cursor.getString(cursor.getColumnIndex(key_percentageTwelve)));
                    registration_bean.setPercentageTen(cursor.getString(cursor.getColumnIndex(key_percentageTen)));
                    registration_bean.setBoardGraduation(cursor.getString(cursor.getColumnIndex(key_boardGraduation)));
                    registration_bean.setYeargraduation(cursor.getString(cursor.getColumnIndex(key_yearGraduation)));
                    registration_bean.setYearTwelve(cursor.getString(cursor.getColumnIndex(key_yearTwelve)));
                    registration_bean.setYearTen(cursor.getString(cursor.getColumnIndex(key_yearTen)));
                    registration_bean.setBoardTwelve(cursor.getString(cursor.getColumnIndex(key_boardTwelve)));
                    registration_bean.setBoardTen(cursor.getString(cursor.getColumnIndex(key_boardTen)));
                    registration_bean.setDefence(cursor.getString(cursor.getColumnIndex(key_defence)));
                    registration_bean.setSpinnerMarital(cursor.getString(cursor.getColumnIndex(key_maritalSpinner)));
                    registration_bean.setSubjectGraduation(cursor.getString(cursor.getColumnIndex(key_subjectGraduation)));
                    registration_bean.setSubjectTwelve(cursor.getString(cursor.getColumnIndex(key_subjectTwelve)));
                    registration_bean.setSubjectTen(cursor.getString(cursor.getColumnIndex(key_subjectTen)));
                    registration_bean.setCollege(cursor.getString(cursor.getColumnIndex(key_college_name)));
                    registration_bean.setSpinnerCategory(cursor.getString(cursor.getColumnIndex(key_category)));
                    registration_bean.setWeight(cursor.getString(cursor.getColumnIndex(key_weight_reg)));
//                    registration_bean.setPaymentStatus(cursor.getString(cursor.getColumnIndex(key_PayMent_Status)));
                    registration_bean.setIdentification(cursor.getString(cursor.getColumnIndex(key_identification)));
                    registration_bean.setImage(cursor.getString(cursor.getColumnIndex(key_image_profile)));
                    registration_bean.setImageGrad(cursor.getString(cursor.getColumnIndex(key_image_grad)));
                    registration_bean.setImageIdentity(cursor.getString(cursor.getColumnIndex(key_image_identity)));
                    registration_bean.setImageTenth(cursor.getString(cursor.getColumnIndex(key_image_tenth)));
                    registration_bean.setImageTwelve(cursor.getString(cursor.getColumnIndex(key_image_twelve)));
//                    registration_bean.setPrice(cursor.getString(cursor.getColumnIndex(key_coursePrice)));
                    registration_bean.setYearAspiring(cursor.getString(cursor.getColumnIndex(key_yearAspiring)));
                    registration_bean.setBoardAspiring(cursor.getString(cursor.getColumnIndex(key_boardAspiring)));
                    registration_bean.setSubjectAspiring(cursor.getString(cursor.getColumnIndex(key_subjectAspiring)));
                    registration_bean.setBlood(cursor.getString(cursor.getColumnIndex(key_blood_register)));
                    registration_list.add(registration_bean);

                } while (cursor.moveToNext());
            }
        }
        return registration_list;
    }


    public void deleteRegistration(Registration_Bean registration_bean) {
        Log.e("delete", "deleted");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_REGISTRATION, key_client_Id + " = ?",
                new String[]{String.valueOf(registration_bean.getClient_id())});
        sqLiteDatabase.close();
    }

    public void deleteEvent(EventBean eventBean) {
        Log.e("delete", "deleted");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_EVENT_REGISTER, key_id_e + " = ?",
                new String[]{String.valueOf(eventBean.getIdE())});
        sqLiteDatabase.close();
    }

    public void deleteSurvey(Survey_bean survey_bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SURVEY, client_id + " = ?",
                new String[]{String.valueOf(survey_bean.getClient_id())});
        db.close();
    }

    public Survey_bean getNewSurveyByEmail(String email) {
        String st = "SELECT * FROM " + TABLE_SURVEY + " where " + key_email_id + "='" + email + "'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Survey_bean survey_bean;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    survey_bean = new Survey_bean();
                    survey_bean.setFirst_name(cursor.getString(cursor.getColumnIndex(key_first_name)));
                    survey_bean.setFather_name(cursor.getString(cursor.getColumnIndex(key_father_name)));
                    survey_bean.setClient_id(cursor.getString(cursor.getColumnIndex(client_id)));
                    survey_bean.setEmail(cursor.getString(cursor.getColumnIndex(key_email_id)));
                    setDate(cursor, survey_bean);
                    survey_bean.setAddress(cursor.getString(cursor.getColumnIndex(key_address)));
                    survey_bean.setMobile_no(cursor.getString(cursor.getColumnIndex(key_mobile_no)));
                    survey_bean.setPin_code(cursor.getString(cursor.getColumnIndex(key_pin_code)));
                    survey_bean.setBlood(cursor.getString(cursor.getColumnIndex(key_blood)));
                    survey_bean.setHeight(cursor.getString(cursor.getColumnIndex(key_height)));
                    survey_bean.setTehsil(cursor.getString(cursor.getColumnIndex(key_tehsil)));
                    survey_bean.setState(cursor.getString(cursor.getColumnIndex(key_state)));
                    survey_bean.setBoardTen(cursor.getString(cursor.getColumnIndex(key_boardTenth)));
                    survey_bean.setBoardTwelve(cursor.getString(cursor.getColumnIndex(key_board_Twelve)));
                    survey_bean.setYearTwelve(cursor.getString(cursor.getColumnIndex(key_year_Twelve)));
                    survey_bean.setYearTen(cursor.getString(cursor.getColumnIndex(key_yearTenth)));
                    survey_bean.setBoardGraduation(cursor.getString(cursor.getColumnIndex(key_boardGrad)));
                    survey_bean.setYeargraduation(cursor.getString(cursor.getColumnIndex(key_yearGrad)));
                    survey_bean.setSubjectGraduation(cursor.getString(cursor.getColumnIndex(key_subjectGrad)));
                    survey_bean.setDepartment(cursor.getString(cursor.getColumnIndex(key_department)));
                    survey_bean.setWeight(cursor.getString(cursor.getColumnIndex(key_weight)));
                    survey_bean.setDist(cursor.getString(cursor.getColumnIndex(key_dist)));
                    survey_bean.setOther(cursor.getString(cursor.getColumnIndex(key_other)));
                    survey_bean.setService(cursor.getString(cursor.getColumnIndex(key_service)));
                    survey_bean.setSpecialize(cursor.getString(cursor.getColumnIndex(key_specialize)));
                    survey_bean.setForce(cursor.getString(cursor.getColumnIndex(key_force)));
                    survey_bean.setCollege(cursor.getString(cursor.getColumnIndex(key_college)));
                    survey_bean.setImage(cursor.getString(cursor.getColumnIndex(key_image)));
                    if (survey_bean.getEmail().equals(email)) {
                        return survey_bean;
                    }
                    break;

                } while (cursor.moveToNext());

            }

        }
        return new Survey_bean();
    }


    public ArrayList<Survey_bean> getNewSurvey() {
        ArrayList<Survey_bean> surveyArrayList = new ArrayList<>();
        String st = "SELECT * FROM " + TABLE_SURVEY;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Survey_bean survey_bean;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    survey_bean = new Survey_bean();
                    survey_bean.setFirst_name(cursor.getString(cursor.getColumnIndex(key_first_name)));
                    survey_bean.setFather_name(cursor.getString(cursor.getColumnIndex(key_father_name)));
                    survey_bean.setClient_id(cursor.getString(cursor.getColumnIndex(client_id)));
                    survey_bean.setEmail(cursor.getString(cursor.getColumnIndex(key_email_id)));
                    setDate(cursor, survey_bean);
                    survey_bean.setAddress(cursor.getString(cursor.getColumnIndex(key_address)));
                    survey_bean.setMobile_no(cursor.getString(cursor.getColumnIndex(key_mobile_no)));
                    survey_bean.setPin_code(cursor.getString(cursor.getColumnIndex(key_pin_code)));
                    survey_bean.setBlood(cursor.getString(cursor.getColumnIndex(key_blood)));
                    survey_bean.setHeight(cursor.getString(cursor.getColumnIndex(key_height)));
                    survey_bean.setTehsil(cursor.getString(cursor.getColumnIndex(key_tehsil)));
                    survey_bean.setState(cursor.getString(cursor.getColumnIndex(key_state)));
                    survey_bean.setBoardTen(cursor.getString(cursor.getColumnIndex(key_boardTenth)));
                    survey_bean.setBoardTwelve(cursor.getString(cursor.getColumnIndex(key_board_Twelve)));
                    survey_bean.setYearTwelve(cursor.getString(cursor.getColumnIndex(key_year_Twelve)));
                    survey_bean.setYearTen(cursor.getString(cursor.getColumnIndex(key_yearTenth)));
                    survey_bean.setBoardGraduation(cursor.getString(cursor.getColumnIndex(key_boardGrad)));
                    survey_bean.setYeargraduation(cursor.getString(cursor.getColumnIndex(key_yearGrad)));
                    survey_bean.setSubjectGraduation(cursor.getString(cursor.getColumnIndex(key_subjectGrad)));
                    survey_bean.setDepartment(cursor.getString(cursor.getColumnIndex(key_department)));
                    survey_bean.setWeight(cursor.getString(cursor.getColumnIndex(key_weight)));
                    survey_bean.setDist(cursor.getString(cursor.getColumnIndex(key_dist)));
                    survey_bean.setOther(cursor.getString(cursor.getColumnIndex(key_other)));
                    survey_bean.setService(cursor.getString(cursor.getColumnIndex(key_service)));
                    survey_bean.setSpecialize(cursor.getString(cursor.getColumnIndex(key_specialize)));
                    survey_bean.setForce(cursor.getString(cursor.getColumnIndex(key_force)));
                    survey_bean.setCollege(cursor.getString(cursor.getColumnIndex(key_college)));
                    survey_bean.setImage(cursor.getString(cursor.getColumnIndex(key_image)));
                    surveyArrayList.add(survey_bean);
                } while (cursor.moveToNext());
            }
        }
        return surveyArrayList;
    }


    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User_Register_Bean> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_MOBILE

        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User_Register_Bean> userList = new ArrayList<User_Register_Bean>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER_REGISTER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User_Register_Bean user = new User_Register_Bean();
                user.setUser_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setUser_full_name(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setUser_email(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setUser_pass(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                user.setUser_mob(cursor.getString(cursor.getColumnIndex(COLUMN_USER_MOBILE)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    public Cursor getUnsyncedNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_USER_REGISTER + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_MOBILE, user.getMobile_no());

        // updating row
        db.update(TABLE_USER_REGISTER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User_Register_Bean user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER_REGISTER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getUser_id())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'abc@gmail.com';
         */
        Cursor cursor = db.query(TABLE_USER_REGISTER, //Table to query

                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkUser_servay(String email) {

        // array of columns to fetch
        String[] columns = {
                client_id
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = key_email_id + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'abc@gmail.com';
         */
        Cursor cursor = db.query(TABLE_SURVEY, //Table to query

                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);
        Log.e("table", cursor.toString() + "");//The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkevent(String id) {

        // array of columns to fetch
        String[] columns = {
                key_id_event
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = key_event_id + " = ?";

        // selection argument
        String[] selectionArgs = {id};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'abc@gmail.com';
         */
        Cursor cursor = db.query(TABLE_EVENT, //Table to query

                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);
        Log.e("table", cursor.toString() + "");//The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkCollegename(String college) {

        // array of columns to fetch
        String[] columns = {
                Aps_Bean.key_college_id
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = Aps_Bean.key_college_names + " = ?";
        // selection argument
        String[] selectionArgs = {college};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'abc@gmail.com';
         */
        Cursor cursor = db.query(TABLE_COLLEGE_NAME, //Table to query

                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);
        Log.e("table", cursor.toString() + "");//The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkcoursename(String college) {

        // array of columns to fetch
        String[] columns = {
                Aps_Bean.key_course_id
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = Aps_Bean.key_course_name + " = ?";

        // selection argument
        String[] selectionArgs = {college};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'abc@gmail.com';
         */
        Cursor cursor = db.query(TABLE_COURSE_NAME, //Table to query

                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);
        Log.e("table", cursor.toString() + "");//The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkSurvey(String email) {

        // array of columns to fetch
        String[] columns = {
                client_id
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = key_email_id + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'abc@gmail.com';
         */
        Cursor cursor = db.query(TABLE_SURVEY, //Table to query

                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);
        Log.e("table", cursor.toString() + "");//The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkUser_registeration(String email) {

        // array of columns to fetch
        String[] columns = {
                key_client_Id
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = key_email + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'abc@gmail.com';
         */
        Cursor cursor = db.query(TABLE_REGISTRATION, //Table to query

                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);
        Log.e("table", cursor.toString() + "");//The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param name
     * @param password
     * @return true/false
     */
    public boolean checkUser(String name, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {name, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'abc@gmail.com' AND user_password = '12345';
         */
        Cursor cursor = db.query(TABLE_USER_REGISTER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public void composeJSONfromSQLite() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM users where udpateStatus = '" + "no" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {

            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", cursor.getString(0));
                map.put("userName", cursor.getString(1));
                wordList.add(map);
            } while (cursor.moveToNext());

        }
        database.close();
//        Gson gson = new GsonBuilder().create();
//        //Use GSON to serialize Array List to JSON
//        return gson.toJson(wordList);

    }


    public String getSyncStatus() {

        String msg = null;

        if (this.dbSyncCount() == 0) {

            msg = "SQLite and Remote MySQL DBs are in Sync!";

        } else {

            msg = "DB Sync neededn";

        }

        return msg;

    }

    public int dbSyncCount() {

        int count = 0;

        String selectQuery = "SELECT  * FROM users where udpateStatus = '" + "no" + "'";

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        count = cursor.getCount();

        database.close();

        return count;

    }

    public void updateSyncStatus(String id, String status) {

        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "Update users set udpateStatus = '" + status + "' where userId=" + "'" + id + "'";

        Log.d("query", updateQuery);

        database.execSQL(updateQuery);

        database.close();

    }
}
