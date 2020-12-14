package com.apseducation.studentinformation.registration;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.apseducation.studentinformation.bean.Registration_Bean;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private EditText editText_username, father_name_editText, dist_editText, address_editText, contactNo_aditText, emailId_editText, university_graduation, percentage_10th, percentage_12th,
            pincode_editText, height_editText, father_occupation, subject_10th, subject_12th, subject_graduation, percentage_graduation, graduation_year, weight_editText, year_aspiring, board_aspiring,
            subject_aspiring, whatsapp_edittext;
    String username, dateofbirth, fathername, dist, address, contact_no, email, pincode, height, occupation, state, defence, boardTen, boardTwelve, yearTen, yearTwelve, subjectTen,
            subjectTwelve, percentageTen, percentageTwelve, boardGraduation, yeargraduation, subjectGraduation, spinnerMarital, percentageGraduation, category, identification, weight,
            yearAspiring, boardAspiring, subjectAspiring, payment_status, blood, price, college_name, whatsappNo;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    ImageView mark_10th, mark_12, mark_grad, identification_image, email_image;
    TextView editText_date, price_text;
    CircleImageView circleImageView;
    SearchView searchView;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PICK_PDF_REQUEST = 123;
    private static final int GALLERY = 1;
    String isTenthPdf = "false", isTwelvePdf = "false", isGraduationPdf = "false", isIdentityPdf = "false";
    private ListView listViewContact;
    Spinner spinner_marital, board_10th, year_10th, board_12th, year_12th, category_spinner, identification_spinner, state_editText, payment_spinner, blood_editText;
    TextView drop_down;
    Button registration_button;
    String profileImagePath, identityImagePath, tenthImagePath, twelveImagePath, gradImagePath;
    int clickImage;
    NetworkConnection networkConnection;
    String imagePathtoServer = "", markTenToServer = "", markTwelveToServer = "", markGradToServer = "", identityToServer = "";
    CheckBox checkBox;
    String fileNameExtension;
    String[] courseNames;
    int[] coursePrice;
    String studentId = "";
    boolean[] checkedItem;
    boolean[] checkedPrice;
    byte[] tenthFile, twelveFile, gradFile, identityFile;
    Registration_Bean registration_bean;
    ArrayAdapter categoryAdapter, blood_adapter, yearPassOutAdapter, marital_adapter, boardarrayAdapter, identification_adapter, state_adapter, paymentAdapter;
    ArrayList<String> yearPassout, marital_arraylist, boardArray, categoryArray, identification_list;
    ArrayList<Integer> defence_list = new ArrayList<>();
    ArrayList<Integer> price_list = new ArrayList<>();
    View snack_view;
    ProgressBar pbar;
    Bitmap profilePhoto, identifyImage, tenthMark, twelveMark, gradMark, bitmapFromUrl;
    DataBaseHelper dataBase_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        context = this;
        init();
        addPassout();
        addMarital_status();
        addDataBoard();
        addCategory();
        addIdentification();
        addState();
//        payment();
        addBloodGroup();
        networkConnection = new NetworkConnection(context);
        if (!NetworkConnection.isConnected(context)) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        college_name = getIntent().getStringExtra("college");
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
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
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.e("", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = year + "-" + month + "-" + day;
                editText_date.setText(date);

            }
        };
        if (NetworkConnection.isConnected(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL.couseName_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //hiding the progressbar after completio
                            try {
                                //getting the whole json object from the response
                                JSONObject obj = new JSONObject(response);
                                Log.e("course name", obj + "");

                                //we have the array named hero inside the object
                                //so here we are getting that json array
                                JSONArray heroArray = obj.getJSONArray("courses");
                                Log.e("course name", heroArray + "");
                                courseNames = new String[heroArray.length()];
//                                coursePrice = new int[heroArray.length()];
                                //now looping through all the elements of the json array
                                for (int i = 0; i < heroArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject Object = heroArray.getJSONObject(i);
                                    //creating a hero object and giving them the values from json object
                                    Aps_Bean courseName = new Aps_Bean();
                                    courseName.setCourse_id(Object.getString("_id"));
                                    courseName.setCourses(Object.getString("courseName"));
                                    courseName.setCoursePrice(Object.getInt("coursePrice"));
                                    if (!dataBase_register.checkcoursename(Object.getString("courseName"))) {
                                        dataBase_register.addCourse(courseName);
                                    }
                                    courseNames[i] = Object.getString("courseName");
//                                    coursePrice[i] = Object.getInt("coursePrice");
                                    //adding the hero to herolist
                                }
                                checkedItem = new boolean[courseNames.length];
//                                checkedPrice = new boolean[coursePrice.length];
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
            dataBase_register = new DataBaseHelper(context);
            if (dataBase_register != null) {
                ArrayList<Aps_Bean> list = dataBase_register.getCourseForLocal();
                Log.e("list", list.size() + "");
                courseNames = new String[list.size()];
//                coursePrice = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    courseNames[i] = list.get(i).getCourses();
//                    coursePrice[i] = list.get(i).getCoursePrice();
                }
//                checkedPrice = new boolean[coursePrice.length];
                checkedItem = new boolean[courseNames.length];
            }
        }
        setupToolbar();
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


    //find id from sourse file of all fields.
    public void init() {
        registration_button = findViewById(R.id.registeration_button);
        editText_username = findViewById(R.id.edit_text_r_name);
        editText_date = findViewById(R.id.edit_r_date);
        father_name_editText = findViewById(R.id.edit_text_r_father);
        dist_editText = findViewById(R.id.edit_text_r_dist);
        address_editText = findViewById(R.id.edit_text_r_address);
        contactNo_aditText = findViewById(R.id.edit_text_r_contact);
        emailId_editText = findViewById(R.id.edit_text_r_email);
        pincode_editText = findViewById(R.id.edit_text_r_pin);
        height_editText = findViewById(R.id.edit_r_height);
        father_occupation = findViewById(R.id.edit_text_r_occupation);
        state_editText = findViewById(R.id.state_spinner);
        editText_date.setOnClickListener(this);
        editText_date.setInputType(InputType.TYPE_NULL);
        spinner_marital = findViewById(R.id._marital);
        board_10th = findViewById(R.id.board_10th);
        board_12th = findViewById(R.id.board_12th);
        year_10th = findViewById(R.id.year_10th);
        year_12th = findViewById(R.id.year_12th);
        checkBox = findViewById(R.id.checkbox_reg);
        graduation_year = findViewById(R.id.year_graduation);
        category_spinner = findViewById(R.id.category_spinner);
        drop_down = (TextView) findViewById(R.id.dropdown_reg);
        subject_10th = findViewById(R.id.subject_10th);
        subject_12th = findViewById(R.id.subject_12th);
        subject_graduation = findViewById(R.id.subject_graduation);
        university_graduation = findViewById(R.id.board_graduation);
        percentage_10th = findViewById(R.id.percentage_10th);
        percentage_12th = findViewById(R.id.percentage_12th);
        percentage_graduation = findViewById(R.id.percentage_graduation);
        mark_10th = findViewById(R.id.mark_10th);
        mark_12 = findViewById(R.id.mark_12);
        mark_grad = findViewById(R.id.mark_grad);
        email_image = findViewById(R.id.email_image);
        listViewContact = findViewById(R.id.listview_register);
        identification_image = findViewById(R.id.identification_image);
        weight_editText = findViewById(R.id.edit_weight);
        circleImageView = findViewById(R.id.circular_image_register);
        searchView = findViewById(R.id.searchView_register);
//        price_text = findViewById(R.id.price);
        year_aspiring = findViewById(R.id.year_aspiring);
        board_aspiring = findViewById(R.id.board_aspiring);
        subject_aspiring = findViewById(R.id.subject_aspiring);
        pbar = findViewById(R.id.progresBar_reg);
        blood_editText = findViewById(R.id._blood);
//        payment_spinner = findViewById(R.id.payment_spinner);
        whatsapp_edittext = findViewById(R.id.whatsappNo);
        mark_10th.setOnClickListener(this);
        mark_12.setOnClickListener(this);
        checkBox.setOnClickListener(this);
        mark_grad.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        identification_image.setOnClickListener(this);
        registration_button.setOnClickListener(this);
        email_image.setOnClickListener(this);
        identification_spinner = findViewById(R.id.identification_spinner);
        drop_down.setOnClickListener(this);
        dataBase_register = new DataBaseHelper(this);
        registration_bean = new Registration_Bean();

    }


    //get data from fields and store in string and check validation
    public boolean validData() {
        username = editText_username.getText().toString().trim();
        dateofbirth = editText_date.getText().toString().trim();
        fathername = father_name_editText.getText().toString().trim();
        dist = dist_editText.getText().toString().trim();
        address = address_editText.getText().toString().trim();
        contact_no = contactNo_aditText.getText().toString().trim();
        email = emailId_editText.getText().toString().trim();
        pincode = pincode_editText.getText().toString().trim();
        height = height_editText.getText().toString().trim();
        state = state_editText.getSelectedItem().toString().trim();
        occupation = father_occupation.getText().toString().trim();
        defence = drop_down.getText().toString();
        subjectTen = subject_10th.getText().toString();
        subjectTwelve = subject_12th.getText().toString();
        subjectGraduation = subject_graduation.getText().toString();
        boardGraduation = university_graduation.getText().toString();
        percentageTen = percentage_10th.getText().toString();
        percentageTwelve = percentage_12th.getText().toString();
        percentageGraduation = percentage_graduation.getText().toString();
        boardTen = board_10th.getSelectedItem().toString();
        boardTwelve = board_12th.getSelectedItem().toString();
        yearTen = year_10th.getSelectedItem().toString();
        yearTwelve = year_12th.getSelectedItem().toString();
        yeargraduation = graduation_year.getText().toString();
        weight = weight_editText.getText().toString();
        yearAspiring = year_aspiring.getText().toString();
        boardAspiring = board_aspiring.getText().toString();
        subjectAspiring = subject_aspiring.getText().toString();
        category = category_spinner.getSelectedItem().toString();
        spinnerMarital = spinner_marital.getSelectedItem().toString();
        identification = identification_spinner.getSelectedItem().toString();
//        price = price_text.getText().toString();
        blood = blood_editText.getSelectedItem().toString().trim();
        whatsappNo = whatsapp_edittext.getText().toString();
//        payment_status = payment_spinner.getSelectedItem().toString();
        if (username.isEmpty() || username.length() < 4) {
            editText_username.setError("Name should be at least 3 characters!");
            editText_username.requestFocus();
            Snackbar.make(snack_view, "Name should be at least 3 characters!", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (fathername.isEmpty() || fathername.length() < 4) {
            father_name_editText.setError("Father Name is short!");
            father_name_editText.requestFocus();
            Snackbar.make(snack_view, "Father Name is short!", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!isValid_Phone(contact_no)) {
            contactNo_aditText.setError("Please enter valid mobile no.");
            contactNo_aditText.requestFocus();
            Snackbar.make(snack_view, "Please enter valid mobile no.", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (email.isEmpty() || !isValid_Mail(email)) {
            emailId_editText.setError("Please enter valid email id");
            emailId_editText.requestFocus();
            Snackbar.make(snack_view, "Please enter valid email id", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (pincode.isEmpty()) {
            pincode_editText.setError("Please enter valid pin");
            pincode_editText.requestFocus();
            Snackbar.make(snack_view, "Please enter valid pin", Snackbar.LENGTH_LONG).show();
            return false;

        } else if (address.isEmpty()) {
            address_editText.setError("Please enter address");
            address_editText.requestFocus();
            Snackbar.make(snack_view, "Please enter address", Snackbar.LENGTH_LONG).show();
            return false;

        } else if (dist.isEmpty()) {
            dist_editText.setError("Please enter dist");
            dist_editText.requestFocus();
            Snackbar.make(snack_view, "Please enter dist", Snackbar.LENGTH_LONG).show();
            return false;

        } else if (dateofbirth.isEmpty()) {
            editText_date.setError("Please enter date of birth");
            editText_date.requestFocus();
            Snackbar.make(snack_view, "Please enter date of birth", Snackbar.LENGTH_LONG).show();
            return false;

        }
        if (state.equals("State")) {
            Snackbar.make(snack_view, "Please select state", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (defence.equals("Select Course*")) {
            Snackbar.make(snack_view, "Please select course", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (subjectTen.isEmpty()) {
            subject_10th.setError("Please fill tenth subject");
            subject_10th.requestFocus();
            return false;
        }
        if (percentageTen.isEmpty()) {
            percentage_10th.setError("Enter tenth Percentage");
            percentage_10th.requestFocus();
            return false;
        }
        if (boardTen.equals("Select Board")) {
            Toast.makeText(getApplicationContext(), "Please select tenth board", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (yearTen.equals("Select Year")) {
            Snackbar.make(snack_view, "Please select tenth year", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (spinnerMarital.equals("Select Marital Status")) {
            Snackbar.make(snack_view, "Please select marital Status", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isValid_Phone(String phone) {
        String Mobile_STRING = "^[7-9][0-9]{9}$";
        return Pattern.compile(Mobile_STRING).matcher(phone).matches();
    }

    private boolean isValid_Mail(String email) {
        String EMAIL_STRING = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return Pattern.compile(EMAIL_STRING).matcher(email).matches();

    }

    public void payment() {
        List<String> list = new ArrayList<>();
        list.add("Not Paid");
        list.add("Paid");
        paymentAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
        paymentAdapter.setDropDownViewResource(R.layout.spinner_item);
        payment_spinner.setAdapter(paymentAdapter);

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
        boardarrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, boardArray);
        boardarrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        board_10th.setAdapter(boardarrayAdapter);
        board_12th.setAdapter(boardarrayAdapter);
    }

    public void addIdentification() {
        identification_list = new ArrayList<>();
        identification_list.add("Aadhaar Card");
        identification_list.add("Pan Card");
        identification_list.add("Voter Id");
        identification_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, identification_list);
        identification_adapter.setDropDownViewResource(R.layout.spinner_item);
        identification_spinner.setAdapter(identification_adapter);
    }

    public void addCategory() {
        categoryArray = new ArrayList<>();
        categoryArray.add("GEN");
        categoryArray.add("OBC");
        categoryArray.add("SC");
        categoryArray.add("ST");
        categoryAdapter = new ArrayAdapter(this, R.layout.spinner_item, categoryArray);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_item);
        category_spinner.setAdapter(categoryAdapter);
    }

    public void addPassout() {
        yearPassout = new ArrayList<String>();
        yearPassout.add("Select Year");
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2000; i <= thisYear; i++) {
            yearPassout.add(String.valueOf(i));
        }
        yearPassOutAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, yearPassout);
        yearPassOutAdapter.setDropDownViewResource(R.layout.spinner_item);
        //  graduation_year.setAdapter(yearPassOutAdapter);
        year_10th.setAdapter(yearPassOutAdapter);
        year_12th.setAdapter(yearPassOutAdapter);
    }

    public void addMarital_status() {
        marital_arraylist = new ArrayList<>();
        marital_arraylist.add("Select Marital Status");
        marital_arraylist.add("Married");
        marital_arraylist.add("Unmarried");
        marital_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, marital_arraylist);
        marital_adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_marital.setAdapter(marital_adapter);
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


    //save data in sqlite database .
    public void loadDatalocal() {
        if (!dataBase_register.checkUser_registeration(email)) {
            registration_bean = new Registration_Bean();
            registration_bean.setFirst_name(username);
            registration_bean.setFather_name(fathername);
            registration_bean.setMobile_no(contact_no);
            registration_bean.setPin_code(pincode);
            registration_bean.setAddress(address);
            setDateOfBirth(dateofbirth);
            registration_bean.setEmail(email);
            registration_bean.setDist_register(dist);
            registration_bean.setHeight_register(height);
            registration_bean.setFather_occupation(occupation);
            registration_bean.setState_register(state);
            registration_bean.setYeargraduation(yeargraduation);
            registration_bean.setYearTwelve(yearTwelve);
            registration_bean.setYearTen(yearTen);
            registration_bean.setBoardGraduation(boardGraduation);
            registration_bean.setBoardTwelve(boardTwelve);
            registration_bean.setBoardTen(boardTen);
            registration_bean.setSubjectGraduation(subjectGraduation);
            registration_bean.setSubjectTwelve(subjectTwelve);
            registration_bean.setSubjectTen(subjectTen);
            registration_bean.setPercentageGraduation(percentageGraduation);
            registration_bean.setPercentageTwelve(percentageTwelve);
            registration_bean.setPercentageTen(percentageTen);
            registration_bean.setSpinnerMarital(spinnerMarital);
            registration_bean.setDefence(defence);
            registration_bean.setSpinnerCategory(category);
            registration_bean.setIdentification(identification);
            registration_bean.setCollege(college_name);
            registration_bean.setWeight(weight);
//            registration_bean.setPrice(price);
//            registration_bean.setPaymentStatus(payment_status);
            registration_bean.setBlood(blood);
            registration_bean.setWhatsappNo(whatsappNo);
            registration_bean.setImage(profileImagePath);
            registration_bean.setSubjectAspiring(subjectAspiring);
            registration_bean.setBoardAspiring(boardAspiring);
            registration_bean.setYearAspiring(yearAspiring);
            registration_bean.setImageIdentity(identityImagePath);
            registration_bean.setImageTenth(tenthImagePath);
            registration_bean.setImageTwelve(twelveImagePath);
            registration_bean.setImageGrad(gradImagePath);
            dataBase_register.insert_registeration(registration_bean);
            Snackbar.make(snack_view, "Registration successfully", Snackbar.LENGTH_LONG).show();
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


    //Alert dialog for select multiple courses with course prices.
    public void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (courseNames != null) {

            String[] str1 = new String[courseNames.length];
            for (int i = 0; i < courseNames.length; i++) {
                str1[i] = courseNames[i];//+ "\n₹ " + coursePrice[i]
            }
            builder.setMultiChoiceItems(str1, checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if (!isChecked) {
                        try {
                            Log.e("item", which + "");
                            defence_list.remove(new Integer(which));
//                            price_list.remove(new Integer(which));
                        } catch (IndexOutOfBoundsException ex) {
                            Log.e("defence list", ex.getMessage() + "");
                        }
                        Log.e("defence list", defence_list + "");
                    } else {
                        if (!defence_list.contains(which)) {
//                            price_list.add(which);
                            defence_list.add(which);
                        }
                    }

                }
            });
            builder.setCancelable(false);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String item = "";
//                    int sum = 0;
//                    int[] price = new int[coursePrice.length];
//                    for (int i = 0; i < coursePrice.length; i++) {
//                        price[i] = coursePrice[i];
//                    }
                    for (int i = 0; i < defence_list.size(); i++) {
                        item = item + courseNames[defence_list.get(i)];
                        if (i != defence_list.size() - 1) {
                            item = item + ",";
                        }
                    }
//                    for (int j = 0; j < price_list.size(); j++) {
//                        sum = sum + price[price_list.get(j)];
//
//                    }
//                    String str = "₹ " + String.valueOf(sum);
                    if (!item.equals("")) {
                        drop_down.setText(item);
//                        price_text.setText(str);
                    } else {
                        drop_down.setText("Select Course");
//                        price_text.setText("₹ 0");
                    }
                }
            });
            builder.setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (checkedItem != null) {
                        for (int i = 0; i < checkedItem.length; i++) {
                            checkedItem[i] = false;
//                            checkedPrice[i] = false;
                            defence_list.clear();
//                            price_list.clear();
                            drop_down.setText("Select Course");
//                            price_text.setText("₹ 0");
                        }
                    }

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "Network Connection is slow.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDateOfBirth(String dateOFBirth) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dateOFBirth);
        } catch (ParseException e) {
            Log.e("date conversion error", "Could not string to date during fetch");
            e.printStackTrace();
        }
        registration_bean.setDate_of_birth(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void takePhotoFromCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Select File", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Your Documents");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        takePhotoFromCamera();
                        break;
                    case 1:
                        choosePhotoFromGallary();
                        break;
                    case 2:
                        showFileChooser();
                        break;
                }
            }
        });
        builder.show();
    }

    private void selectImageFromProfile(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Your Profile Picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        takePhotoFromCamera();
                        break;
                    case 1:
                        choosePhotoFromGallary();
                        break;
                }
            }
        });
        builder.show();
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

    //change inputstream into byte array
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    //select image and files from internal storage for documents
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("request", requestCode + " result " + resultCode);
        switch (clickImage) {
            case 0:
                if (requestCode == GALLERY && resultCode == RESULT_OK) {
                    checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                    if (data != null) {
                        Uri contentURI = data.getData();
                        try {
                            profilePhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                            profilePhoto = getResizedBitmap(profilePhoto, 400);
                            circleImageView.setImageBitmap(profilePhoto);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            profilePhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageBytes = baos.toByteArray();
                            imagePathtoServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                    checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                    try {
                        profilePhoto = (Bitmap) data.getExtras().get("data");
//                        profilePhoto = getResizedBitmap(profilePhoto, 400);
                        circleImageView.setImageBitmap(profilePhoto);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        profilePhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        imagePathtoServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    } catch (Exception ex) {
                        Log.e("exception", ex.getMessage());
                    }
                }
                break;
            case 1:
                if (requestCode == GALLERY && resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri contentURI = data.getData();
                        try {
                            identityToServer = "";
                            identifyImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                            identifyImage = getResizedBitmap(identifyImage, 400);
                            identification_image.setImageBitmap(identifyImage);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            identifyImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageBytes = baos.toByteArray();
                            identityToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                    try {
                        identityToServer = "";
                        identifyImage = (Bitmap) data.getExtras().get("data");
//                        identifyImage = getResizedBitmap(identifyImage, 400);
                        identification_image.setImageBitmap(identifyImage);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        identifyImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        identityToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    } catch (Exception e) {
                        Log.e("exception1", e.getMessage());
                    }
                } else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    identification_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_cloud_upload_black_24dp));
                    File file = new File(uri.getPath());
                    InputStream iStream = null;
                    try {
                        iStream = getContentResolver().openInputStream(uri);
                        if (file.getName().contains(".pdf")) {
                            identityFile = getBytes(iStream);
                            identityToServer = Base64.encodeToString(identityFile, Base64.DEFAULT);
//                            Log.e("input data", identityToServer + "");
                            isIdentityPdf = "true";
                            Toast.makeText(getApplicationContext(), "File Selected", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select only .pdf file", Toast.LENGTH_LONG).show();
                        }
//                        else {
//                            identityToServer = "";
//                            identifyImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                            identifyImage = getResizedBitmap(identifyImage, 400);
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            identifyImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                            byte[] imageBytes = baos.toByteArray();
//                            identityToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 2:
                if (requestCode == GALLERY && resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri contentURI = data.getData();
                        try {
                            markTenToServer = "";
                            tenthMark = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                            tenthMark = getResizedBitmap(tenthMark, 400);
                            mark_10th.setImageBitmap(tenthMark);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            tenthMark.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageBytes = baos.toByteArray();
                            markTenToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                    try {
                        markTenToServer = "";
                        tenthMark = (Bitmap) data.getExtras().get("data");
//                        tenthMark = getResizedBitmap(tenthMark, 400);
                        mark_10th.setImageBitmap(tenthMark);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        tenthMark.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        markTenToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//                    Log.e("input data", markTenToServer + "");
                    } catch (Exception e) {
                        Log.e("exception2", e.getMessage());
                    }
                } else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    File file = new File(uri.getPath());
                    mark_10th.setImageDrawable(getResources().getDrawable(R.drawable.ic_cloud_upload_black_24dp));
                    InputStream iStream = null;
                    try {
                        iStream = getContentResolver().openInputStream(uri);
                        if (file.getName().contains(".pdf")) {
                            tenthFile = getBytes(iStream);
                            markTenToServer = Base64.encodeToString(tenthFile, Base64.DEFAULT);
//                            Log.e("input data", markTenToServer + "");
                            isTenthPdf = "true";
                            Toast.makeText(getApplicationContext(), "File Selected", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Please select only .pdf file", Toast.LENGTH_LONG).show();

                        }
//                        else {
//                            markTenToServer = "";
//                            tenthMark = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                            tenthMark = getResizedBitmap(tenthMark, 400);
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            tenthMark.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                            byte[] imageBytes = baos.toByteArray();
//                            markTenToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case 3:
                if (requestCode == GALLERY && resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri contentURI = data.getData();
                        try {
                            markTwelveToServer = "";
                            twelveMark = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                            twelveMark = getResizedBitmap(twelveMark, 400);
                            mark_12.setImageBitmap(twelveMark);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            twelveMark.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageBytes = baos.toByteArray();
                            markTwelveToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                    try {
                        markTwelveToServer = "";
                        twelveMark = (Bitmap) data.getExtras().get("data");
//                        twelveMark = getResizedBitmap(twelveMark, 400);
                        mark_12.setImageBitmap(twelveMark);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        twelveMark.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        markTwelveToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    } catch (Exception e) {
                        Log.e("exception3", e.getMessage());
                    }
                } else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    File file = new File(uri.getPath());
                    int file_size = (int) (file.length());
                    Log.e("lenth", file_size + "");
                    mark_12.setImageDrawable(getResources().getDrawable(R.drawable.ic_cloud_upload_black_24dp));
                    InputStream iStream = null;
                    try {
                        iStream = getContentResolver().openInputStream(uri);
                        if (file.getName().contains(".pdf")) {
                            twelveFile = getBytes(iStream);
                            Toast.makeText(getApplicationContext(), "File Selected", Toast.LENGTH_SHORT).show();
                            markTwelveToServer = Base64.encodeToString(twelveFile, Base64.DEFAULT);
                            isTwelvePdf = "true";

                        } else {
                            Toast.makeText(getApplicationContext(), "Please select only .pdf file", Toast.LENGTH_LONG).show();
                        }
//                        else {
//                            markTwelveToServer = "";
//                            twelveMark = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                            twelveMark = getResizedBitmap(twelveMark, 400);
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            twelveMark.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                            byte[] imageBytes = baos.toByteArray();
//                            markTwelveToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 4:
                if (requestCode == GALLERY && resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri contentURI = data.getData();
                        try {
                            markGradToServer = "";
                            gradMark = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                            gradMark = getResizedBitmap(gradMark, 400);
                            mark_grad.setImageBitmap(gradMark);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            gradMark.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageBytes = baos.toByteArray();
                            markGradToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                    try {
                        markGradToServer = "";
                        gradMark = (Bitmap) data.getExtras().get("data");
//                        gradMark = getResizedBitmap(gradMark, 400);
                        mark_grad.setImageBitmap(gradMark);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        gradMark.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        markGradToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    } catch (Exception e) {
                        Log.e("exception4", e.getMessage());
                    }
                } else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    File file = new File(uri.getPath());
                    Log.e("FileName::", uri.getPath());
                    mark_grad.setImageDrawable(getResources().getDrawable(R.drawable.ic_cloud_upload_black_24dp));
                    InputStream iStream = null;
                    try {
                        iStream = getContentResolver().openInputStream(uri);
                        if (file.getName().contains(".pdf")) {
                            gradFile = getBytes(iStream);
                            markGradToServer = Base64.encodeToString(gradFile, Base64.DEFAULT);
//                            Log.e("in if image", markGradToServer);
                            isGraduationPdf = "true";
                            Toast.makeText(getApplicationContext(), "File Selected", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Please select only .pdf file", Toast.LENGTH_LONG).show();

                        }
//                        else {
//                            markGradToServer = "";
//                            gradMark = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                            gradMark = getResizedBitmap(gradMark, 400);
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            gradMark.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                            byte[] imageBytes = baos.toByteArray();
//                            markGradToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
////                            Log.e("not in if image", markGradToServer);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
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


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        String[] mimetypes = {"application/pdf"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }


    //save pdf file in internal storage for sync data to server.
    public String writeFileWithPdf(byte[] file, String pathImage) {
        String fileName = username + ".pdf";
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + college_name + "/Registration/" + email + "/" + pathImage);
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
                fos.write(file);
                fos.flush();
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            if (pathImage.equals("Identification")) {
                identityImagePath = myFile.getAbsolutePath();
            }
            if (pathImage.equals("TenthMarkSheet")) {
                tenthImagePath = myFile.getAbsolutePath();
            }
            if (pathImage.equals("TwelveMarkSheet")) {
                twelveImagePath = myFile.getAbsolutePath();
            }
            if (pathImage.equals("GraduationMarkSheet")) {
                gradImagePath = myFile.getAbsolutePath();
            }
            return myFile.getAbsolutePath();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public void fileOnLocal() {
        if (identityFile != null) {
            saveFile("Identification", identityFile, "image2");
        }
        if (tenthFile != null) {
            saveFile("TenthMarkSheet", tenthFile, "image3");
        }
        if (twelveFile != null) {
            saveFile("TwelveMarkSheet", twelveFile, "image4");
        }
        if (gradFile != null) {
            saveFile("GraduationMarkSheet", gradFile, "image5");
        }
    }

    public void saveFile(String profile, byte[] file, String image1) {
        String image = profile;
        writeFileWithPdf(file, image);
    }


    //save images in internal storage for sync data to server
    public String writeOnFile(Bitmap myBitmap, String pathImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String fileName = username + ".jpeg";
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + college_name + "/Registration/" + email + "/" + pathImage);
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
            if (pathImage.equals("profile")) {
                profileImagePath = myFile.getAbsolutePath();
            }
            if (pathImage.equals("Identification")) {
                identityImagePath = myFile.getAbsolutePath();
            }
            if (pathImage.equals("TenthMarkSheet")) {
                tenthImagePath = myFile.getAbsolutePath();
            }
            if (pathImage.equals("TwelveMarkSheet")) {
                twelveImagePath = myFile.getAbsolutePath();
            }
            if (pathImage.equals("GraduationMarkSheet")) {
                gradImagePath = myFile.getAbsolutePath();
            }
            return myFile.getAbsolutePath();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public void imageOnLocal() {
        if (profilePhoto != null) {
            saveImage("profile", profilePhoto, "image1");
        }
        if (identifyImage != null) {
            saveImage("Identification", identifyImage, "image2");
        }
        if (tenthMark != null) {
            saveImage("TenthMarkSheet", tenthMark, "image3");
        }
        if (twelveMark != null) {
            saveImage("TwelveMarkSheet", twelveMark, "image4");
        }
        if (gradMark != null) {
            saveImage("GraduationMarkSheet", gradMark, "image5");
        }
    }

    private void saveImage(String profile, Bitmap profile_photo, String image1) {
        String image = profile;
        writeOnFile(profile_photo, image);
        Log.e(image1, image);
    }

    @Override
    public void onClick(View v) {
        snack_view = v;
        if (v.getId() == R.id.registeration_button) {
            if (validData()) {
                if (networkConnection.isConnected(context)) {
                    if (studentId.equals("")) {
                        uploadDataServer();
                    } else {
                        updateDataToServer();
                    }
                } else {
                    fileOnLocal();
                    imageOnLocal();
                    loadDatalocal();
                }
            }
        }
        if (v.getId() == R.id.dropdown_reg) {
            alertDialog();
        }
        if (v.getId() == R.id.circular_image_register) {
            clickImage = 0;
            selectImageFromProfile(this);

        }
        if (v.getId() == R.id.mark_10th) {
            clickImage = 2;
            selectImage(Registration.this);

        }
        if (v.getId() == R.id.mark_12) {
            clickImage = 3;
            selectImage(this);

        }
        if (v.getId() == R.id.mark_grad) {
            clickImage = 4;
            selectImage(this);

        }
        if (v.getId() == R.id.identification_image) {
            clickImage = 1;
            selectImage(this);

        }
        if (v.getId() == R.id.checkbox_reg) {
            if (((CheckBox) v).isChecked()) {
            }
        }
        if (v.getId() == R.id.edit_r_date) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    Registration.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
        if (v.getId() == R.id.email_image) {
            String email = emailId_editText.getText().toString();
            if (email != null && email.contains(".com")) {
                if (NetworkConnection.isConnected(context)) {
                    checkMail(email);
                } else if (dataBase_register.checkUser_registeration(email)) {
                    Toast.makeText(getApplicationContext(), "Email Already Exist", Toast.LENGTH_SHORT).show();
                } else {
                    if (dataBase_register.checkSurvey(email)) {
                        Survey_bean survey = dataBase_register.getNewSurveyByEmail(email);
                        setSearchDataFromSurvey(survey);
                    } else {
                        Toast.makeText(getApplicationContext(), "NO Data Found", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }
//        if (v.getId() == R.id.payment_spinner) {
//            startActivity(new Intent(context, PaymentActivity.class));
//        }
    }


    //upload data to server using volley with json object.
    public void uploadDataServer() {
        pbar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("college", college_name);
            jsonObject.put("courses", defence);
//            jsonObject.put("coursePrice", price);
//            jsonObject.put("paid", payment_status);
            jsonObject.put("email", email);
            jsonObject.put("name", username);
            jsonObject.put("fatherName", fathername);
            jsonObject.put("dob", dateofbirth);
            jsonObject.put("fatherOccupation", occupation);
            jsonObject.put("contact", contact_no);
            jsonObject.put("whatsapp", whatsappNo);
            jsonObject.put("height", height);
            jsonObject.put("weight", weight);
            jsonObject.put("bloodGroup", blood);
            jsonObject.put("address", address);
            jsonObject.put("state", state);
            jsonObject.put("district", dist);
            jsonObject.put("pincode", pincode);
            jsonObject.put("category", category);
            jsonObject.put("maritalStatus", spinnerMarital);
            jsonObject.put("tenthBoard", boardTen);
            jsonObject.put("tenthYear", yearTen);
            jsonObject.put("tenthSubject", subjectTen);
            jsonObject.put("tenthPercentage", percentageTen);
            jsonObject.put("twelveBoard", boardTwelve);
            jsonObject.put("twelveYear", yearTwelve);
            jsonObject.put("twelveSubject", subjectTwelve);
            jsonObject.put("twelvePercentage", percentageTwelve);
            jsonObject.put("aspiringYear", yearAspiring);
            jsonObject.put("aspiringBoard", boardAspiring);
            jsonObject.put("aspiringSubject", subjectAspiring);
            jsonObject.put("graduationYear", yeargraduation);
            jsonObject.put("graduationUniversity", boardGraduation);
            jsonObject.put("graduationSubject", subjectGraduation);
            jsonObject.put("graduationPercentage", percentageGraduation);
            jsonObject.put("idType", identification);
            jsonObject.put("isPdfId", isIdentityPdf);
            jsonObject.put("idProofUrl", identityToServer);
            jsonObject.put("isPdf10", isTenthPdf);
            jsonObject.put("tenthMarksheetUrl", markTenToServer);
            jsonObject.put("isPdf12", isTwelvePdf);
            jsonObject.put("twelveMarksheetUrl", markTwelveToServer);
            jsonObject.put("isPdfUniversity", isGraduationPdf);
            jsonObject.put("universityDocumentUrl", markGradToServer);
            jsonObject.put("studentPhotoUrl", imagePathtoServer);
//            Log.e("error", jsonObject.toString() + "");
            //request, as I am using my own localhost for this tutorial
            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, API_URL.Registration_url, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                pbar.setVisibility(View.GONE);
                                Log.e("value of json2", response + "");
                                String responseStatus = response.getString("message");
                                if (responseStatus.equals("user created")) {
                                    Log.e("value of if", "" + responseStatus);
                                    Toast.makeText(getApplicationContext(), responseStatus, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), responseStatus, Toast.LENGTH_SHORT).show();
                                    Log.e("value of else", responseStatus + "");
                                }
                            } catch (Exception e) {

                                Log.e("error", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pbar.setVisibility(View.GONE);
                            if (error.getMessage() == null) {
                                Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
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

    public void updateDataToServer() {
        pbar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", studentId);
            jsonObject.put("college", college_name);
            jsonObject.put("courses", defence);
//            jsonObject.put("coursePrice", price);
//            jsonObject.put("paid", payment_status);
            jsonObject.put("email", email);
            jsonObject.put("name", username);
            jsonObject.put("fatherName", fathername);
            jsonObject.put("dob", dateofbirth);
            jsonObject.put("fatherOccupation", occupation);
            jsonObject.put("contact", contact_no);
            jsonObject.put("whatsapp", whatsappNo);
            jsonObject.put("height", height);
            jsonObject.put("weight", weight);
            jsonObject.put("bloodGroup", blood);
            jsonObject.put("address", address);
            jsonObject.put("state", state);
            jsonObject.put("district", dist);
            jsonObject.put("pincode", pincode);
            jsonObject.put("category", category);
            jsonObject.put("maritalStatus", spinnerMarital);
            jsonObject.put("tenthBoard", boardTen);
            jsonObject.put("tenthYear", yearTen);
            jsonObject.put("tenthSubject", subjectTen);
            jsonObject.put("tenthPercentage", percentageTen);
            jsonObject.put("twelveBoard", boardTwelve);
            jsonObject.put("twelveYear", yearTwelve);
            jsonObject.put("twelveSubject", subjectTwelve);
            jsonObject.put("twelvePercentage", percentageTwelve);
            jsonObject.put("aspiringYear", yearAspiring);
            jsonObject.put("aspiringBoard", boardAspiring);
            jsonObject.put("aspiringSubject", subjectAspiring);
            jsonObject.put("graduationYear", yeargraduation);
            jsonObject.put("graduationUniversity", boardGraduation);
            jsonObject.put("graduationSubject", subjectGraduation);
            jsonObject.put("graduationPercentage", percentageGraduation);
            jsonObject.put("idType", identification);
            jsonObject.put("isPdfId", isIdentityPdf);
            jsonObject.put("idProofUrl", identityToServer);
            jsonObject.put("isPdf10", isTenthPdf);
            jsonObject.put("tenthMarksheetUrl", markTenToServer);
            jsonObject.put("isPdf12", isTwelvePdf);
            jsonObject.put("twelveMarksheetUrl", markTwelveToServer);
            jsonObject.put("isPdfUniversity", isGraduationPdf);
            jsonObject.put("universityDocumentUrl", markGradToServer);
            jsonObject.put("studentPhotoUrl", imagePathtoServer);
//            Log.e("error", jsonObject.toString() + "");
            //request, as I am using my own localhost for this tutorial
            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, API_URL.editRegister, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                pbar.setVisibility(View.GONE);
                                Log.e("value of json2", response + "");
                                String responseStatus = response.getString("message");
                                if (responseStatus.equals("user created")) {
                                    Log.e("value of if", "" + responseStatus);
                                    Toast.makeText(getApplicationContext(), responseStatus, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), responseStatus, Toast.LENGTH_SHORT).show();
                                    Log.e("value of else", responseStatus + "");
                                }
                            } catch (Exception e) {

                                Log.e("error", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pbar.setVisibility(View.GONE);
                            if (error.getMessage() == null) {
                                Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
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

    //check email in registration if exist or not if exist get data
    public void checkMail(final String email) {
        if (NetworkConnection.isConnected(context) && email.contains(".com")) {
            final RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, API_URL.registrationSearch_url + email, null, new Response.Listener<JSONObject>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(JSONObject json) {
                            Log.e("respose1", json + "");
                            try {
                                Log.e("Response: ", json.optString("msg"));
                                if (json.optString("msg").equals("Not Found")) {
                                    checkMailFromSurvey(email);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Email Or Contact No. Already exist", Toast.LENGTH_SHORT).show();
                                    JSONArray array = json.getJSONArray("courses");
                                    for (int i = 0; i < array.length(); i++) {
                                        String str = array.get(i).toString();
                                        Log.e("str", str);
                                        drop_down.setText(str);
                                    }
                                    studentId = json.optString("_id");
                                    state_editText.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, Collections.singletonList(json.optString("state"))));
                                    editText_username.setText(json.optString("name"));
                                    father_name_editText.setText(json.optString("fatherName"));
                                    editText_date.setText(json.optString("dob").substring(0, 10));
                                    subject_graduation.setText(json.optString("graduationSubject"));
                                    graduation_year.setText(json.optString("graduationYear"));
                                    university_graduation.setText(json.optString("graduationUniversity"));
                                    contactNo_aditText.setText(json.optString("contact"));
                                    emailId_editText.setText(json.optString("email"));
                                    height_editText.setText(json.optString("height"));
                                    weight_editText.setText(json.optString("weight"));
                                    address_editText.setText(json.optString("address"));
                                    dist_editText.setText(json.getString("district"));
                                    pincode_editText.setText(json.getString("pincode"));
                                    father_occupation.setText(json.optString("fatherOccupation"));
                                    if (json.optString("whatsapp") != null && !json.optString("whatsapp").equals("null")) {
                                        whatsapp_edittext.setText(json.optString("whatsapp"));
                                    } else {
                                        whatsapp_edittext.setText("");
                                    }

                                    subject_10th.setText(json.optString("tenthSubject"));
                                    subject_12th.setText(json.optString("twelveSubject"));
                                    String category = json.optString("category");
                                    if (categoryArray.contains(category)) {
                                        category_spinner.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, Collections.singletonList(json.optString("category"))));
                                        addCategory();
                                    }
                                    registration_bean.setSpinnerMarital(json.optString("maritalStatus"));
                                    ArrayList mList = new ArrayList();
                                    mList.add(json.optString("maritalStatus"));
                                    if (mList.contains("UNMARRIED")) {
                                        mList.add("MARRIED");
                                    } else {
                                        mList.add("UNMARRIED");
                                    }
                                    spinner_marital.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, mList));
                                    registration_bean.setSubjectTen(json.optString("tenthSubject"));
                                    if (!json.optString("tenthPercentage").equals("null")) {
                                        registration_bean.setPercentageTen(json.optString("tenthPercentage"));
                                        percentage_10th.setText(json.optString("tenthPercentage"));
                                    } else {
                                        registration_bean.setPercentageTen("");
                                        percentage_10th.setText("");
                                    }
                                    if (!json.optString("twelvePercentage").equals("null")) {
                                        percentage_12th.setText(json.optString("twelvePercentage"));
                                    } else {
                                        percentage_12th.setText("");
                                    }
                                    if (json.optString("twelveBoard").equals("SELECT BOARD") || json.optString("twelveBoard").equals("NONE")) {
                                        addDataBoard();
                                    } else {
                                        addGetBoard(json.optString("twelveBoard"));
                                    }
                                    if (json.optString("tenthBoard").equals("SELECT BOARD") || json.optString("tenthBoard").equals("NONE")) {
                                        addDataBoard();
                                    } else {
                                        addGetBoard10(json.optString("tenthBoard"));
                                    }
                                    if (json.optString("bloodGroup").equals("BLOOD GROUP") || json.optString("bloodGroup").equals("NONE")) {
                                        registration_bean.setBlood("");
                                        addBloodGroup();
                                    } else {
                                        registration_bean.setBlood(json.optString("bloodGroup"));
                                        blood_editText.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, Collections.singletonList(json.optString("bloodGroup"))));
                                    }
                                    if (json.optString("tenthYear").equals("Select Year")) {
                                        registration_bean.setYearTen("");
                                    } else if (!json.optString("tenthYear").equals("")) {
                                        registration_bean.setYearTen(json.optString("tenthYear").substring(0, 4));
                                        year_10th.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, Collections.singletonList(json.optString("tenthYear").substring(0, 4))));
                                    }
                                    if (json.optString("twelveYear").equals("Select Year")) {
                                        registration_bean.setYearTwelve("");
//                                        addPassout();
                                    } else if (!json.optString("twelveYear").equals("")) {
                                        year_12th.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, Collections.singletonList(json.optString("twelveYear").substring(0, 4))));
                                    }
                                    if (json.optString("graduationYear").equals("Select Year")) {
                                        registration_bean.setYeargraduation("");
                                    } else if (!json.optString("graduationYear").equals("")) {
                                        registration_bean.setYeargraduation(json.optString("graduationYear").substring(0, 4));
                                    } else {
                                        registration_bean.setYeargraduation(json.optString("graduationYear"));
                                    }

                                    board_aspiring.setText(json.optString("aspiringUniversity"));
                                    year_aspiring.setText(json.optString("aspiringYear"));
                                    subject_aspiring.setText(json.optString("aspiringSubject"));
                                    registration_bean.setBoardGraduation(json.optString("graduationUniversity"));
                                    if (!json.optString("graduationPercentage").equals("null")) {
                                        registration_bean.setPercentageGraduation(json.optString("graduationPercentage"));
                                        percentage_graduation.setText(json.optString("graduationPercentage"));
                                    } else {
                                        registration_bean.setPercentageGraduation("");
                                        percentage_graduation.setText("");
                                    }
                                    registration_bean.setSubjectGraduation(json.optString("graduationSubject"));
                                    registration_bean.setIdentification(json.optString("idType"));
                                    identification_spinner.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, Collections.singletonList(json.optString("idType"))));
                                    addIdentification();
                                    if (json.optString("studentPhotoUrl") != null) {
                                        String url = json.optString("studentPhotoUrl");
                                        Picasso.get()
                                                .load(url)
                                                .into(new Target() {
                                                    @Override
                                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                        /* Save the bitmap or do something with it here */
                                                        //Set it in the ImageView
                                                        circleImageView.setImageBitmap(bitmap);
                                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                        byte[] imageBytes = baos.toByteArray();
                                                        imagePathtoServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                                    }

                                                    @Override
                                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                    }

                                                    @Override
                                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                    }
                                                });
                                    }
                                    if (json.optString("idProofUrl") != null) {
                                        String url = json.optString("idProofUrl");
                                        Log.e("id url", url);
                                        if (!url.contains(".pdf")) {
                                            Picasso.get()
                                                    .load(url)
                                                    .into(new Target() {
                                                        @Override
                                                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                                            /* Save the bitmap or do something with it here */
                                                            //Set it in the ImageView
                                                            identification_image.setImageBitmap(bitmap);
                                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                            byte[] imageBytes = baos.toByteArray();
                                                            identityToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                                        }

                                                        @Override
                                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                        }

                                                        @Override
                                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                        }
                                                    });
                                        } else {
                                            identityToServer=url;
                                        }
                                    }
                                    if (json.optString("universityDocumentUrl") != null && !json.optString("universityDocumentUrl").equals("null")) {
                                        String url = json.optString("universityDocumentUrl");
                                        Log.e("uni url", url);
                                        if (!url.contains(".pdf") && !url.isEmpty()) {
                                            Picasso.get()
                                                    .load(url)
                                                    .into(new Target() {
                                                        @Override
                                                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                                            /* Save the bitmap or do something with it here */
                                                            //Set it in the ImageView
                                                            mark_grad.setImageBitmap(bitmap);
                                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                            byte[] imageBytes = baos.toByteArray();
                                                            markGradToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                                        }

                                                        @Override
                                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                        }

                                                        @Override
                                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                        }
                                                    });
                                        } else {
                                            markGradToServer =url;
                                        }
                                    }
                                    if (json.optString("tenthMarksheetUrl") != null && !json.optString("tenthMarksheetUrl").equals("null")) {
                                        String url = json.optString("tenthMarksheetUrl");
                                        Log.e("10 url", url);
                                        if (!url.contains(".pdf") && !url.isEmpty()) {
                                            Picasso.get()
                                                    .load(url)
                                                    .into(new Target() {
                                                        @Override
                                                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                                            /* Save the bitmap or do something with it here */
                                                            //Set it in the ImageView
                                                            mark_10th.setImageBitmap(bitmap);
                                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                            byte[] imageBytes = baos.toByteArray();
                                                            markTenToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                                        }

                                                        @Override
                                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                        }

                                                        @Override
                                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                        }
                                                    });
                                        } else {
                                            markTenToServer = url;
                                        }
                                    }
                                    if (json.optString("twelveMarksheetUrl") != null && json.optString("twelveMarksheetUrl").equals("null")) {
                                        String url = json.optString("twelveMarksheetUrl");
                                        if (!url.contains(".pdf")) {
                                            Picasso.get()
                                                    .load(url)
                                                    .into(new Target() {
                                                        @Override
                                                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                                            /* Save the bitmap or do something with it here */
                                                            //Set it in the ImageView
                                                            mark_12.setImageBitmap(bitmap);
                                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                            byte[] imageBytes = baos.toByteArray();
                                                            markTwelveToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                                        }

                                                        @Override
                                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                        }

                                                        @Override
                                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                        }
                                                    });
                                        } else {

                                            markTwelveToServer = url;
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
                            // TODO Auto-generated method stub
                        }
                    });
            requestQueue.add(jsObjRequest);
        }
    }

    //check email in survey after check in registration
    public void checkMailFromSurvey(String email) {
        if (NetworkConnection.isConnected(context) && email.contains(".com")) {
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
                                    Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONObject json = obj.getJSONObject("user");
                                    JSONArray jarray = json.getJSONArray("courses");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        String str = jarray.get(i).toString();
                                        Log.e("str", str);
                                        drop_down.setText(str);
                                    }
                                    editText_username.setText(json.get("name").toString());
                                    father_name_editText.setText(json.get("fatherName").toString());
                                    editText_date.setText(json.get("dob").toString().substring(0, 10));
                                    subject_graduation.setText(json.get("graduationCourse").toString());
                                    graduation_year.setText(json.get("graduationYear").toString());
                                    university_graduation.setText(json.get("graduationUniversity").toString());

                                    contactNo_aditText.setText(json.get("contact").toString());
                                    emailId_editText.setText(json.get("email").toString());
                                    height_editText.setText(json.get("height").toString());
                                    weight_editText.setText(json.get("weight").toString());
                                    address_editText.setText(json.get("address").toString());
                                    dist_editText.setText(json.getString("district"));
                                    pincode_editText.setText(json.getString("pincode"));
//                                    university_graduation.setText(json.getString("university"));
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


    //search data in sqlite by email.
    private void searchContact(String keyboard) {
        if (keyboard.contains(".com")) {
            dataBase_register = new DataBaseHelper(getApplicationContext());
            Registration_Bean registrationData = dataBase_register.getNewRegistrationByEmail(keyboard);
            if (registrationData.getFirst_name() != null) {
                Toast.makeText(getApplicationContext(), "Email Already Exist", Toast.LENGTH_SHORT).show();
                Log.e("list value", registrationData.toString());
                setSearchDataInField(registrationData);
                listViewContact.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, Collections.singletonList(registrationData.toString())));
            } else {
                Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
            }
        }

    }


    //set data in fields if data exist in sqlite from registration table
    public void setSearchDataInField(Registration_Bean registrationBean) {
        if (registrationBean != null) {
            editText_username.setText(registrationBean.getFirst_name());
            father_name_editText.setText(registrationBean.getFather_name());
            father_occupation.setText(registrationBean.getFather_occupation());
            emailId_editText.setText(registrationBean.getEmail());
            dist_editText.setText(registrationBean.getDist_register());
            address_editText.setText(registrationBean.getAddress());
            contactNo_aditText.setText(registrationBean.getMobile_no());
            university_graduation.setText(registrationBean.getBoardGraduation());
            drop_down.setText(registrationBean.getDefence());
            spinner_marital.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, Collections.singletonList(registrationBean.getSpinnerMarital())));
            percentage_10th.setText(registrationBean.getPercentageTen());
            state_editText.setAdapter(new ArrayAdapter(this, R.layout.spinner_item, Collections.singletonList(registrationBean.getState_register())));
            percentage_12th.setText(registrationBean.getPercentageTwelve());
            pincode_editText.setText(registrationBean.getPin_code());
            height_editText.setText(registrationBean.getHeight_register());
            subject_10th.setText(registrationBean.getSubjectTen());
            subject_12th.setText(registrationBean.getSubjectTwelve());
//            price_text.setText(registrationBean.getPrice());
            subject_graduation.setText(registrationBean.getSubjectGraduation());
            percentage_graduation.setText(registrationBean.getPercentageGraduation());
            graduation_year.setText(registrationBean.getYeargraduation());
            weight_editText.setText(registrationBean.getWeight());
            editText_date.setText(format.format(registrationBean.getDate_of_birth()));
            year_aspiring.setText(registrationBean.getYearAspiring());
            board_aspiring.setText(registrationBean.getBoardAspiring());
            subject_aspiring.setText(registrationBean.getSubjectAspiring());
        }
    }

    public void setSearchDataFromSurvey(Survey_bean survey) {
        if (survey != null) {
            Log.e("survey", survey + "");
            editText_username.setText(survey.getFirst_name());
            father_name_editText.setText(survey.getFather_name());
            contactNo_aditText.setText(survey.getMobile_no());
            editText_date.setText(format.format(survey.getDate()));
            state_editText.setAdapter(new ArrayAdapter(this, R.layout.spinner_item, Collections.singletonList(survey.getState())));
            height_editText.setText(survey.getHeight());
            weight_editText.setText(survey.getWeight());
            address_editText.setText(survey.getAddress());
            drop_down.setText(survey.getDepartment());
            pincode_editText.setText(survey.getPin_code());
            university_graduation.setText(survey.getUniversity());
            emailId_editText.setText(survey.getEmail());
            dist_editText.setText(survey.getDist());
        }
    }

    public void addGetBoard(String board) {
        ArrayList list = new ArrayList();
        list.add(board);
        if (list.contains("UP BOARD")) {
            list.add("CBSE");
            list.add("ICSE");
            list.add("OTHER");
            board_12th.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list));
        } else if (list.contains("CBSE")) {
            list.add("UP BOARD");
            list.add("ICSE");
            list.add("OTHER");
            board_12th.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list));
        } else if (list.contains("ICSE")) {
            list.add("CBSE");
            list.add("UP BOARD");
            list.add("OTHER");
            board_12th.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list));
        } else {
            list.add("CBSE");
            list.add("UP BOARD");
            list.add("ICSE");
            board_12th.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list));
        }
    }

    public void addGetBoard10(String board) {
        ArrayList list = new ArrayList();
        list.add(board);
        if (list.contains("UP BOARD")) {
            list.add("CBSE");
            list.add("ICSE");
            list.add("OTHER");
            board_10th.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list));
        } else if (list.contains("CBSE")) {
            list.add("UP BOARD");
            list.add("ICSE");
            list.add("OTHER");
            board_10th.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list));
        } else if (list.contains("ICSE")) {
            list.add("CBSE");
            list.add("UP BOARD");
            list.add("OTHER");
            board_10th.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list));
        } else {
            list.add("CBSE");
            list.add("UP BOARD");
            list.add("ICSE");
            board_10th.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list));
        }
    }


    public static String URLReader(URL url1) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = url1.openStream();
            Log.e("exception", url1.toString());
            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }
        } catch (IOException e) {
            Log.e("exception", e.getMessage());
            e.printStackTrace();
            return null;
        }
        return outputStream.toByteArray().toString();
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                InputStream input = new URL(f_url[0]).openStream();
                Log.e("uri", input + "");
//                Uri uri=Uri.parse(f_url[0]);
//                InputStream stream = null;
//                stream = getContentResolver().openInputStream(uri);
                identityFile = getBytes(input);
//                byte[] chunk = new byte[4096];
//                int bytesRead;
//                InputStream stream = url.openStream();
//                while ((bytesRead = stream.read(chunk)) > 0) {
//                    outputStream.write(chunk, 0, bytesRead);
//                }
//                byte[] bytes = outputStream.toByteArray();
//                Log.e("bytes", bytes + "");
                identityToServer = Base64.encodeToString(identityFile, Base64.DEFAULT);
                Log.e("file", identityToServer);
            } catch (IOException e) {
                Log.e("exception", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded

        }

    }
}
// registration_bean.setFirst_name(json.getString("name"));
//         registration_bean.setFather_name(json.getString("fatherName"));
//         registration_bean.setEmail(json.getString("email"));
//         setDateOfBirth(json.getString("dob"));
//         registration_bean.setMobile_no(json.getString("contact"));
//         registration_bean.setWhatsappNo(json.optString("whatsapp"));
//         registration_bean.setFather_occupation(json.optString("fatherOccupation"));
//         registration_bean.setSpinnerCategory(json.optString("category"));
// registration_bean.setWeight(json.optString("weight"));
//         registration_bean.setCollege(json.optString("college"));
//         JSONArray jarray = json.getJSONArray("courses");
//         for (int i = 0; i < jarray.length(); i++) {
//        String str = jarray.get(i).toString();
//        Log.e("str", str);
//        registration_bean.setDefence(str);
//        }
// registration_bean.setBoardAspiring(json.optString("aspiringUniversity"));
//         registration_bean.setYearAspiring(json.optString("aspiringYear"));
//         registration_bean.setSubjectAspiring(json.optString("aspiringSubject"));
// if (!json.optString("aspiringYear").equals("")) {
//         registration_bean.setYearAspiring(json.optString("aspiringYear").substring(0, 4));
//         } else {
//         registration_bean.setYearAspiring(json.optString("aspiringYear"));
//         }