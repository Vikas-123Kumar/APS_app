package com.apseducation.studentinformation.admissionregistration;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apseducation.studentinformation.R;
import com.apseducation.studentinformation.ShowSearchData;
import com.apseducation.studentinformation.api.API_URL;
import com.apseducation.studentinformation.bean.Aps_Bean;
import com.apseducation.studentinformation.bean.Survey_bean;
import com.apseducation.studentinformation.connection.NetworkConnection;
import com.apseducation.studentinformation.profile.ui.sqlite.DataBaseHelper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SurveyForm extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button submitButton;
    private int PICK_IMAGE_REQUEST = 10;
    int clickImage;
    ProgressBar pbar;
    Bitmap photo, bitmap;
    NetworkConnection networkConnection;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Context context;
    private EditText editText_username, father_name_edtitext, dist_edittext, address_edittext, contactNo_adtitext, emailId_edittext, pincode_edittext,
            height_editText, tehsil_editText, weight_editText, specialize_editText, subject_graduation, graduation_year,
            university_graduation;
    DataBaseHelper servay_dataBaseHelper;
    private CircleImageView click_profile;
    private CircleImageView circularImageView;
    TextView editText_date;
    String imagePath;
    private static final int STORAGE_PERMISSION_CODE = 101;
    //a broadcast to know weather the data is synced or not
    Spinner blood_editText, family_spinner, service_spinner, other_spinner, state_editText, board_10th, year_10th, board_12th, year_12th;
    ArrayAdapter<String> dataAdapter, boardarrayAdapter, yearPassOutAdapter, blood_adapter, other_adapter, family_adapter, state_adapter;
    ArrayList<String> boardArray, yearPassout;
    View snack_view;
    String[] coursenames;
    String imagePathtoServer = "";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    TextView dropdown;
    ListView department_spinner;
    String username, date, fatherName, dist, address, contact, email, pinCode, height, blood = "", tehsil, state, depatment, weight, specialize, force, service, other, boardTen, boardTwelve, yearTen, yearTwelve,
            boardGraduation, yeargraduation, subjectGraduation;
    boolean[] checkedItem;
    Aps_Bean apsBean;
    SearchView searchView;
    Survey_bean survey_bean;
    DataBaseHelper dataBaseHelper;
    List<Integer> ItemsIntoList = new ArrayList<>();
    private ListView listViewContact;
    String college_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_registeration);
        init();
        context = this;
        circularImageView = findViewById(R.id.circular_image_survey);
        submitButton.setOnClickListener(this);
        blood_editText.setOnItemSelectedListener(this);
        addBloodGroup();
        servay_dataBaseHelper = new DataBaseHelper(getApplicationContext());
        //retrieveDataFromBean();
        family_other();
        family_service();
        addDataBoard();
        addPassout();
        addState();
        apsBean = new Aps_Bean();
        networkConnection = new NetworkConnection(context);
        college_name = getIntent().getStringExtra("college");
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.e("", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = year + "-" + month + "-" + day;
                editText_date.setText(date);
            }
        };
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (NetworkConnection.isConnected(context)) {
                    checkMail(query);
                } else {
                    searchContact(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (NetworkConnection.isConnected(context)) {
                    checkMail(newText);
                } else {
                    searchContact(newText);
                }
                return false;
            }
        });
        survey_bean = new Survey_bean();
        dataBaseHelper = new DataBaseHelper(this);
        setGetCoursesName();
        setupToolbar();
    }

    private void setDateForEmployee(String dateOFBirth) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dateOFBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        survey_bean.setDate(date);
        Log.e("date in set", format.format(date) + "");
    }

    public void setGetCoursesName() {              //this method is used get Courses from server and save in l
        if (NetworkConnection.isConnected(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL.couseName_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //hiding the progressbar after completio
                            try {
                                Log.e("course name", response + "");
                                //getting the whole json object from the response
                                JSONObject obj = new JSONObject(response);
                                Log.e("course name", obj + "");
                                //we have the array named hero inside the object
                                //so here we are getting that json array
                                JSONArray heroArray = obj.getJSONArray("courses");
                                Log.e("course name", heroArray.length() + "");
                                coursenames = new String[heroArray.length()];
                                //now looping through all the elements of the json array
                                for (int i = 0; i < heroArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject Object = heroArray.getJSONObject(i);
                                    dataBaseHelper = new DataBaseHelper(context);
                                    //creating a hero object and giving them the values from json object
                                    Aps_Bean courseName = new Aps_Bean();
                                    courseName.setCourse_id(Object.getString("_id"));
                                    courseName.setCourses(Object.getString("courseName"));
                                    courseName.setCoursePrice(Object.getInt("coursePrice"));

                                    if (!dataBaseHelper.checkcoursename(Object.getString("courseName"))) {
                                        dataBaseHelper.addCourse(courseName);
                                    }
                                    coursenames[i] = Object.getString("courseName");
                                    //adding the hero to herolist

                                }
                                checkedItem = new boolean[coursenames.length];
                                //creating custom adapter object
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displaying the error in toast if occurrs
                            Toast.makeText(context, "Failed to connect", Toast.LENGTH_SHORT).show();
                        }
                    });

            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            //adding the string request to request queue
            requestQueue.add(stringRequest);
        } else {
            dataBaseHelper = new DataBaseHelper(context);
            if (dataBaseHelper != null) {
                ArrayList<Aps_Bean> list = dataBaseHelper.getCourseForLocal();
                Log.e("list", list.size() + "");
                coursenames = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    coursenames[i] = list.get(i).getCourses();
                }
                checkedItem = new boolean[coursenames.length];
            }
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        ActionBar action_Bar = getSupportActionBar();
        if (action_Bar != null) {
            action_Bar.setDisplayHomeAsUpEnabled(true);
            action_Bar.setDisplayShowTitleEnabled(false);
            action_Bar.setTitle("Registration_Bean");
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    // In this find id of all fields from the xml.

    public void init() {
        searchView = findViewById(R.id.search_View);
        submitButton = (Button) findViewById(R.id.addmision_button);
        editText_date = (TextView) findViewById(R.id.edit_date);
        editText_date.setOnClickListener(this);
        editText_date.setInputType(InputType.TYPE_NULL);
        editText_username = findViewById(R.id.edit_text_name);
        father_name_edtitext = findViewById(R.id.edit_text_father);
        dist_edittext = findViewById(R.id.edit_text_dist);
        address_edittext = findViewById(R.id.edit_text_address);
        contactNo_adtitext = findViewById(R.id.edit_text_contact);
        emailId_edittext = findViewById(R.id.edit_text_email);
        pincode_edittext = findViewById(R.id.edit_text_pin);
        height_editText = findViewById(R.id.edit_height);
        blood_editText = findViewById(R.id.edit_blood);
        tehsil_editText = findViewById(R.id.edit_text_tehsil);
        listViewContact = findViewById(R.id.listview);
        state_editText = findViewById(R.id.edit_text_state);
        department_spinner = (ListView) findViewById(R.id.item);
        weight_editText = findViewById(R.id.weight);
        board_10th = findViewById(R.id.board_10);
        board_12th = findViewById(R.id.board_12);
        year_10th = findViewById(R.id.year_10);
        year_12th = findViewById(R.id.year_12);
        dropdown = findViewById(R.id.dropdown);
        graduation_year = findViewById(R.id.year_grad);
        subject_graduation = findViewById(R.id.subject_grad);
        university_graduation = findViewById(R.id.board_grad);
        family_spinner = findViewById(R.id.family_spinner);
        other_spinner = findViewById(R.id.other_spinner);
        service_spinner = findViewById(R.id.service_spinner);
        dropdown.setOnClickListener(this);
        click_profile = findViewById(R.id.click_profile);
        click_profile.setOnClickListener(this);
        pbar = findViewById(R.id.progresBar);
        specialize_editText = findViewById(R.id.specialization);
    }

    //this is used to save data in sqlite data base.

    public void sendDatatoLocal() {
        if (!servay_dataBaseHelper.checkUser_servay(email)) {
            survey_bean.setFirst_name(username);
            survey_bean.setFather_name(fatherName);
            survey_bean.setEmail(email);
            setDateForEmployee(date);
            survey_bean.setMobile_no(contact);
            survey_bean.setPin_code(pinCode);
            survey_bean.setAddress(address);
            survey_bean.setDist(dist);
            survey_bean.setBlood(blood);
            survey_bean.setHeight(height);
            survey_bean.setTehsil(tehsil);
            survey_bean.setState(state);
            survey_bean.setDepartment(depatment);
            survey_bean.setWeight(weight);
            survey_bean.setForce(force);
            survey_bean.setYeargraduation(yeargraduation);
            survey_bean.setYearTen(yearTen);
            survey_bean.setBoardTen(boardTen);
            survey_bean.setYearTwelve(yearTwelve);
            survey_bean.setBoardTwelve(boardTwelve);
            survey_bean.setSubjectGraduation(subjectGraduation);
            survey_bean.setBoardGraduation(boardGraduation);
            survey_bean.setSpecialize(specialize);
            survey_bean.setService(service);
            survey_bean.setOther(other);
            survey_bean.setCollege(college_name);
            survey_bean.setImage(imagePath);
            servay_dataBaseHelper.insertSurvey(survey_bean);
            Snackbar.make(snack_view, "servay successfully", Snackbar.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // finish();
                }
            }, Snackbar.LENGTH_LONG);
        } else {

            Snackbar.make(snack_view, "User already exist with same Email", Snackbar.LENGTH_LONG).show();
        }
    }

    public void addState() {
        String stateName[] = {"State", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
                "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
                "Uttar Pradesh", "Uttarakhand", "West Bengal"};
        List list = new ArrayList();
        for (int i = 0; i < stateName.length; i++) {
            list.add(stateName[i]);
        }
        state_adapter = new ArrayAdapter(this, R.layout.spinner_item, list);
        state_adapter.setDropDownViewResource(R.layout.spinner_item);
        state_editText.setAdapter(state_adapter);
    }

    // search data from sqlite using email.

    private void searchContact(String keyboard) {
        if (keyboard.contains(".com")) {
            dataBaseHelper = new DataBaseHelper(getApplicationContext());
            Survey_bean surveyData = dataBaseHelper.getNewSurveyByEmail(keyboard);
            Log.e("loist value", keyboard + "");
            if (surveyData.getFirst_name() != null) {
                Log.e("list value", surveyData.toString());
                //editText_username = findViewById(R.id.edit_text_name);
                setDataInEditText(surveyData);
                listViewContact.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, Collections.singletonList(surveyData.toString())));
            } else {
                Toast.makeText(getApplicationContext(), "NO data found", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //used for save data on server using Volley with json.

    public void uploadDataServer() {
        pbar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(SurveyForm.this);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("courses", depatment);
            jsonObject.put("college", college_name);
            jsonObject.put("name", username);
            jsonObject.put("fatherName", fatherName);
            jsonObject.put("email", email);
            jsonObject.put("dob", date);
            jsonObject.put("contact", contact);
            jsonObject.put("pincode", pinCode);
            jsonObject.put("district", dist);
            jsonObject.put("bloodGroup", blood);
            jsonObject.put("height", height);
            jsonObject.put("tehsil", tehsil);
            jsonObject.put("address", address);
            jsonObject.put("state", state);
            jsonObject.put("coaching", specialize);
            jsonObject.put("weight", weight);
            jsonObject.put("forceMember", force);
            jsonObject.put("governmentMember", service);
            jsonObject.put("other", other);
            jsonObject.put("studentPhotoUrl", imagePathtoServer);
            jsonObject.put("board10", boardTen);
            jsonObject.put("year10", yearTen);
            jsonObject.put("board12", boardTwelve);
            jsonObject.put("year12", yearTwelve);
            jsonObject.put("graduationYear", yeargraduation);
            jsonObject.put("graduationUniversity", boardGraduation);
            jsonObject.put("graduationCourse", subjectGraduation);
//            Log.e("json for server", jsonObject.toString() + "");
            //request, as I am using my own localhost for this tutorial
            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, API_URL.survey_url, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("value of json2", response + "");
                                Toast.makeText(getApplicationContext(), "save on server", Toast.LENGTH_SHORT).show();
                                String responseStatus = response.getString("message");
                                if (responseStatus.equals("Survey created")) {
                                    Log.e("value of if", "" + responseStatus);
                                    Toast.makeText(getApplicationContext(), responseStatus, Toast.LENGTH_SHORT).show();
                                    pbar.setVisibility(View.GONE);
                                } else {
                                    Log.e("value of else", responseStatus + "");
                                    Toast.makeText(getApplicationContext(), responseStatus, Toast.LENGTH_SHORT).show();
                                    pbar.setVisibility(View.GONE);

                                }

                            } catch (Exception e) {
                                Log.e("error.....", e.getMessage());

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error", error.getMessage() + "");
                            pbar.setVisibility(View.GONE);
                            if (error.getMessage() == null) {
                                Toast.makeText(getApplicationContext(), "Email Or Contact No. Already Exist", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            // Add the request to the RequestQueue.
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            Log.e("error jsonexception", e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        snack_view = v;
        if (v.getId() == R.id.addmision_button) {
            if (validData()) {
                if (NetworkConnection.isConnected(context)) {
                    uploadDataServer();
                } else {
                    if (photo != null) {
                        writeOnFile(photo);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please set profile Image", Toast.LENGTH_SHORT).show();
                    }
                    sendDatatoLocal();
                }
            }
        }
        if (v.getId() == R.id.edit_date) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    SurveyForm.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
        if (v.getId() == R.id.dropdown) {
            alertDialog();
        }
        if (v.getId() == R.id.click_profile) {
//            clickImage = 1;
            selectImage(SurveyForm.this);
        }
    }


    //Select image from interna storage and camera with permission
    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
                        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestcode", requestCode + "");
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode);
                photo = (Bitmap) data.getExtras().get("data");
//                photo = getResizedBitmap(photo, 400);
                circularImageView.setImageBitmap(photo);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imagePathtoServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode);
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                photo = (BitmapFactory.decodeFile(picturePath));
//                photo = getResizedBitmap(photo, 400);
                Log.e("path of image ", picturePath + "");
                circularImageView.setImageBitmap(photo);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imagePathtoServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public void imageInByte() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        imagePathtoServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
        } else {

        }
    }


    //search data from server by email and show in recycler view .
    public void checkMail(String email) {
        if (NetworkConnection.isConnected(context) && email.contains(".com")) {
            pbar.setVisibility(View.VISIBLE);
            Log.e("data ", "in search");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL.surveySearch_url + email,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("response", response);

                            //hiding the progressbar after completio
                            try {
                                JSONObject obj = new JSONObject(response);
                                Log.e("obj", obj + "");
                                if (obj.optString("message").equals("Not found")) {
                                    pbar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                                } else {
                                    pbar.setVisibility(View.GONE);
                                    JSONObject json = obj.getJSONObject("user");
                                    survey_bean.setFirst_name(json.getString("name"));
                                    survey_bean.setFather_name(json.getString("fatherName"));
                                    survey_bean.setEmail(json.getString("email"));
                                    String date = json.getString("dob");
                                    setDateForEmployee(date);
                                    Log.e("date", date);
//                                    survey_bean.setDate(format.parse(date));
                                    survey_bean.setMobile_no(json.getString("contact"));
                                    survey_bean.setPin_code(json.getString("pincode"));
                                    survey_bean.setAddress(json.getString("address"));
                                    survey_bean.setDist(json.getString("district"));
                                    if (json.getString("bloodGroup").equals("BLOOD GROUP")) {
                                        survey_bean.setBlood("");
                                    } else {
                                        survey_bean.setBlood(json.getString("bloodGroup"));
                                    }
                                    survey_bean.setHeight(json.getString("height"));
                                    survey_bean.setTehsil(json.getString("tehsil"));
                                    survey_bean.setState(json.getString("state"));
                                    if (!json.optString("year10").equals("")) {
                                        survey_bean.setYearTen(json.optString("year10").substring(0, 4));
                                    } else {
                                        survey_bean.setYearTen(json.optString("year10"));
                                    }
                                    survey_bean.setBoardTen(json.optString("board10"));
                                    if (!json.optString("year12").equals("")) {
                                        survey_bean.setYearTwelve(json.optString("year12").substring(0, 4));
                                    } else {
                                        survey_bean.setYearTwelve(json.optString("year12"));
                                    }
                                    survey_bean.setBoardTwelve(json.optString("board12"));
                                    if (!json.optString("graduationYear").equals("")) {
                                        survey_bean.setYeargraduation(json.optString("graduationYear").substring(0, 4));
                                    } else {
                                        survey_bean.setYeargraduation(json.optString("graduationYear"));
                                    }
                                    survey_bean.setBoardGraduation(json.optString("graduationUniversity"));
                                    survey_bean.setSubjectGraduation(json.optString("graduationCourse"));
                                    survey_bean.setWeight(json.optString("weight"));
                                    survey_bean.setForce(json.optString("forceMember"));
                                    survey_bean.setSpecialize(json.optString("coaching"));
                                    survey_bean.setService(json.optString("governmentMember"));
                                    survey_bean.setOther(json.optString("other"));
                                    survey_bean.setCollege(json.optString("college"));
                                    JSONArray jarray = json.getJSONArray("courses");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        String str = jarray.get(i).toString();
                                        Log.e("str", str);
                                        survey_bean.setDepartment(str);
                                    }
                                    Intent intent = new Intent(context, ShowSearchData.class);
                                    intent.putExtra("surveyBean", survey_bean);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displaying the error in toast if occurrs
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            //adding the string request to request queue
            requestQueue.add(stringRequest);
        }
    }


    public String writeOnFile(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String fileName = username + ".jpg";
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + college_name + "/survey/" + "profile/" + email);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.e("TAG", "could not create the directories");
                }
            }
            final File myFile = new File(dir, fileName);
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(myFile);
                fos.write(bytes.toByteArray());
                fos.flush();
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            imagePath = myFile.getAbsolutePath();
            bitmap = BitmapFactory.decodeFile(imagePath);
            imageInByte();
            return myFile.getAbsolutePath();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //get data from field in string and check validation.
    public boolean validData() {
        username = editText_username.getText().toString().trim();
        address = address_edittext.getText().toString().trim();
        date = editText_date.getText().toString().trim();
        dist = dist_edittext.getText().toString().trim();
        contact = contactNo_adtitext.getText().toString().trim();
        fatherName = father_name_edtitext.getText().toString().trim();
        pinCode = pincode_edittext.getText().toString().trim();
        height = height_editText.getText().toString().trim();
        blood = blood_editText.getSelectedItem().toString().trim();
        state = state_editText.getSelectedItem().toString().trim();
        tehsil = tehsil_editText.getText().toString().trim();
        depatment = dropdown.getText().toString();
        weight = weight_editText.getText().toString();
        service = service_spinner.getSelectedItem().toString();
        force = family_spinner.getSelectedItem().toString();
        other = other_spinner.getSelectedItem().toString();
        email = emailId_edittext.getText().toString().trim();
        subjectGraduation = subject_graduation.getText().toString();
        boardGraduation = university_graduation.getText().toString();
        boardTen = board_10th.getSelectedItem().toString();
        boardTwelve = board_12th.getSelectedItem().toString();
        yearTen = year_10th.getSelectedItem().toString();
        yearTwelve = year_12th.getSelectedItem().toString();
        yeargraduation = graduation_year.getText().toString();
        specialize = specialize_editText.getText().toString().trim();
        if (username.length() < 5) {
            editText_username.setError("Username should be minimun 5 characters");
            editText_username.requestFocus();
            Snackbar.make(snack_view, "Username should be minimun 5 characters", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (fatherName.length() < 5) {
            father_name_edtitext.setError("Father name should be minimun 5 characters");
            father_name_edtitext.requestFocus();
            Snackbar.make(snack_view, "Father name should be minimun 5 characters", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!isValid_Mail(email)) {
            emailId_edittext.setError("Please enter valid email");
            emailId_edittext.requestFocus();
            return false;
        }
        if (!isValid_Phone(contact)) {
            contactNo_adtitext.setError("Please enter valid phone no.");
            contactNo_adtitext.requestFocus();
            Snackbar.make(snack_view, "Please enter valid phone no.", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (address.isEmpty()) {
            address_edittext.setError("Address can not be blank");
            address_edittext.requestFocus();
            Snackbar.make(snack_view, "Address can not be blank", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (pinCode.isEmpty()) {
            pincode_edittext.setError("Pincode can not be blank");
            pincode_edittext.requestFocus();
            Snackbar.make(snack_view, "Pincode can not be blank", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (dist.isEmpty()) {
            dist_edittext.setError("Dist can not be blank");
            dist_edittext.requestFocus();
            Snackbar.make(snack_view, "Dist can not be blank", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (date.isEmpty()) {
            Snackbar.make(snack_view, "Please enter Data of birth", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (blood.equals("Blood Group")) {
            blood = "";
            return true;
        }
        if (height.isEmpty()) {
            height_editText.setError("Please enter height");
            height_editText.requestFocus();
            Snackbar.make(snack_view, "Please enter height", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (tehsil.isEmpty()) {
            tehsil_editText.setError("Please enter tehsil");
            tehsil_editText.requestFocus();
            return false;
        }
        if (state.equalsIgnoreCase("State")) {
            Snackbar.make(snack_view, "Please enter state", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (yearTen.equals(yearTwelve) || yearTen.compareTo(yearTwelve) > 2) {
            Snackbar.make(snack_view, "Twelve year should be greater than tenth year", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    //Alert Box for multiple select Courses.
    public void alertDialog() {
        addDataDepartment();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMultiChoiceItems(coursenames, checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (!isChecked) {
                    try {
                        Log.e("which", which + "");
                        Log.e("list", ItemsIntoList + "");
                        ItemsIntoList.remove(new Integer(which));
                    } catch (IndexOutOfBoundsException ex) {
                        Log.e("exception", ex.getMessage());
                    }
                } else {
                    if (!ItemsIntoList.contains(which)) {
                        ItemsIntoList.add(which);
                        Log.e("list in else", ItemsIntoList + "");
                    }
                }
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                for (int i = 0; i < ItemsIntoList.size(); i++) {
                    item = item + coursenames[ItemsIntoList.get(i)];
                    if (i != ItemsIntoList.size() - 1) {
                        item = item + ",";
                    }
                }
                if (!item.equals("")) {
                    dropdown.setText(item);
                } else {
                    dropdown.setText("Select Course");
                }
            }
        });
        builder.setNegativeButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkedItem != null) {
                    for (int i = 0; i < checkedItem.length; i++) {
                        checkedItem[i] = false;
                        ItemsIntoList.clear();
                        dropdown.setText("Select Course");
                    }
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private boolean isValid_Phone(String phone) {
        String Mobile_STRING = "^[7-9][0-9]{9}$";
        return Pattern.compile(Mobile_STRING).matcher(phone).matches();
    }

    private boolean isValid_Mail(String email) {
        String EMAIL_STRING = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return Pattern.compile(EMAIL_STRING).matcher(email).matches();
        // "^[A-Za-z][A-Za-z0-9]*([._-]?[A-Za-z0-9]+)@[A-Za-z].[A-Za-z]{0,3}?.[A-Za-z]{0,2}$";
    }

    public void addBloodGroup() {
        String[] blood_str = {"Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        blood_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, blood_str);
        blood_adapter.setDropDownViewResource(R.layout.spinner_item);
        blood_editText.setAdapter(blood_adapter);
    }

    public void addDataBoard() {
        boardArray = new ArrayList<>();
        boardArray.add("Select Board");
        boardArray.add("UP Board");
        boardArray.add("CBSE");
        boardArray.add("ICSE");
        boardArray.add("Other");
        boardarrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, boardArray);
        boardarrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        board_10th.setAdapter(boardarrayAdapter);
        board_12th.setAdapter(boardarrayAdapter);
    }

    public void addDataDepartment() {
        if (coursenames != null) {
            dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, coursenames);
        }
    }

    public void addPassout() {
        yearPassout = new ArrayList<String>();
        yearPassout.add("Select Year");
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2000; i <= thisYear; i++) {
            yearPassout.add(String.valueOf(i));
        }
        yearPassOutAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, yearPassout);
        yearPassOutAdapter.setDropDownViewResource(R.layout.spinner_item);
        year_10th.setAdapter(yearPassOutAdapter);
        year_12th.setAdapter(yearPassOutAdapter);

    }

    public void family_other() {
        List<String> list = new ArrayList<>();
        list.add("Private");
        list.add("Bussiness");
        list.add("Farmer");
        other_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
        other_adapter.setDropDownViewResource(R.layout.spinner_item);
        other_spinner.setAdapter(other_adapter);
    }

    public void family_service() {
        List<String> list = new ArrayList<>();
        list.add("No");
        list.add("Yes");
        family_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
        family_adapter.setDropDownViewResource(R.layout.spinner_item);
        family_spinner.setAdapter(family_adapter);
        service_spinner.setAdapter(family_adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    //set data in field to get from sqlite
    public void setDataInEditText(Survey_bean survey_bean) {
        editText_username.setText(survey_bean.getFirst_name());
        editText_date.setText(format.format(survey_bean.getDate()));
        father_name_edtitext.setText(survey_bean.getFather_name());
        dist_edittext.setText(survey_bean.getDist());
        address_edittext.setText(survey_bean.getAddress());
        contactNo_adtitext.setText(survey_bean.getMobile_no());
        emailId_edittext.setText(survey_bean.getEmail());
        pincode_edittext.setText(survey_bean.getPin_code());
        height_editText.setText(survey_bean.getHeight());
        weight_editText.setText(survey_bean.getWeight());
        tehsil_editText.setText(survey_bean.getTehsil());
        state_editText.setAdapter(new ArrayAdapter(this, R.layout.spinner_item, Collections.singletonList(survey_bean.getState())));
        blood_editText.setAdapter(new ArrayAdapter(this, R.layout.spinner_item, Collections.singletonList(survey_bean.getBlood())));
        dist_edittext.setText(survey_bean.getDist());
        pincode_edittext.setText(survey_bean.getPin_code());
        dropdown.setText(survey_bean.getDepartment());
        String image = survey_bean.getImage();
        Bitmap bitmap = BitmapFactory.decodeFile(image);
        circularImageView.setImageBitmap(bitmap);
        Log.e("date from", survey_bean.getDate() + "");
    }

}

