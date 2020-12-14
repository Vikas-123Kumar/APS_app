package com.apseducation.studentinformation.profile.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apseducation.studentinformation.R;
import com.apseducation.studentinformation.bean.User_Register_Bean;
import com.apseducation.studentinformation.connection.NetworkConnection;
import com.apseducation.studentinformation.profile.ui.sqlite.DataBaseHelper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Student_Registration extends AppCompatActivity implements View.OnClickListener {
    private EditText user_name_edit, password_edit, confirm_password_edit, email_address_edit, mobile_no_edit, designation_edit;
    private Button registeration;
    private ImageView show_pass_eye_icon, show_conf_pass_eye_icon;
    String username, password, email, ConfirmPassword, mobile_no, designation;
    Context context;
    View snackbar;
    NetworkConnection networkConnection;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
    TextView backToLogin;
    DataBaseHelper dataBaseHelper;
    User_Register_Bean user_register_bean;
    private static final String KEY_EMPTY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_student__registeration);
        user_name_edit = findViewById(R.id.user_name_register);
        password_edit = findViewById(R.id.pass_word_register);
        registeration = findViewById(R.id.register_button);
        confirm_password_edit = findViewById(R.id.conf_pass_word_register);
        email_address_edit = findViewById(R.id.email_address);
        mobile_no_edit = findViewById(R.id.mobile_no);
        backToLogin = findViewById(R.id.back_login);
        networkConnection = new NetworkConnection(context);
        designation_edit = findViewById(R.id.designation);
        backToLogin.setOnClickListener(this);
        show_pass_eye_icon = findViewById(R.id.show_pass_icon);
        show_conf_pass_eye_icon = findViewById(R.id.show_confirm_pass_icon);
        show_pass_eye_icon.setOnClickListener(this);
        show_conf_pass_eye_icon.setOnClickListener(this);
        registeration.setOnClickListener(this);
        user_register_bean = new User_Register_Bean();
        dataBaseHelper = new DataBaseHelper(this);

    }


    private boolean isValid_Mail(String email) {
        String EMAIL_STRING = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return Pattern.compile(EMAIL_STRING).matcher(email).matches();
        // "^[A-Za-z][A-Za-z0-9]*([._-]?[A-Za-z0-9]+)@[A-Za-z].[A-Za-z]{0,3}?.[A-Za-z]{0,2}$";
    }

    private boolean validateInputs() {
// check that editText is empty or not.
        username = user_name_edit.getText().toString().toLowerCase().trim();
        password = password_edit.getText().toString().trim();
        ConfirmPassword = confirm_password_edit.getText().toString().trim();
        email = email_address_edit.getText().toString().trim();
        mobile_no = mobile_no_edit.getText().toString().trim();
        designation = designation_edit.getText().toString();
        user_register_bean.setUser_full_name(username);
        user_register_bean.setUser_email(email);
        user_register_bean.setUser_mob(mobile_no);
        user_register_bean.setUser_pass(password);
        if (KEY_EMPTY.equals(username)) {
            user_name_edit.setError("Username can't be empty");
            user_name_edit.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            password_edit.setError("Password can't be empty");
            password_edit.requestFocus();
            return false;
        }
        if (!password.equals(ConfirmPassword)) {
            confirm_password_edit.setError("Confirm password doesn't match");
            confirm_password_edit.requestFocus();
            return false;
        }
        if (!isValid_Mail(email)) {
            email_address_edit.setError("Enter valid email");
            email_address_edit.requestFocus();
            return false;
        }
        if (mobile_no_edit.length() < 10) {
            mobile_no_edit.setError("Mobile no should be 10 digit");
            mobile_no_edit.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(designation)) {
            designation_edit.setError("Designaton can't be empty");
            designation_edit.requestFocus();
            return false;
        }
//        if (!isConnected()) {
//            return false;
//        }

        return true;
    }

    @Override
    public void onClick(View v) {
        snackbar = v;
        if (v.getId() == R.id.register_button) {
            if (validateInputs()) {
                //userRegister();
                addLocal();
            }
        } else if (v.getId() == R.id.show_pass_icon) {
            if (password_edit.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                show_pass_eye_icon.setImageResource(R.drawable.show_password);                //Show Password
                password_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                show_pass_eye_icon.setImageResource(R.drawable.hide_password);
                //Hide Password
                password_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        } else if (v.getId() == R.id.show_confirm_pass_icon) {
            if (confirm_password_edit.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                show_conf_pass_eye_icon.setImageResource(R.drawable.show_password);
                //set password
                confirm_password_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                show_conf_pass_eye_icon.setImageResource(R.drawable.hide_password);
                confirm_password_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
        if (v.getId() == R.id.back_login) {
            startActivity(new Intent(Student_Registration.this, Student_Login.class));
            finish();
        }
    }

    public void addLocal() {
        if (!dataBaseHelper.checkUser(email)) {
            Log.e("dataBase", dataBaseHelper.getDatabaseName() + "");
            user_register_bean.setUser_full_name(username);
            user_register_bean.setUser_pass(password);
            user_register_bean.setUser_email(email);
            user_register_bean.setUser_mob(mobile_no);
            user_register_bean.setUser_designation(designation);
            dataBaseHelper.addUser(user_register_bean);
            Log.e("databasehelper", dataBaseHelper.toString());
            Snackbar.make(snackbar, "User created successfully! Please Login ", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(snackbar, "User already exit with same Email", Snackbar.LENGTH_LONG).show();
        }
    }

    public void userRegister() {
        RequestQueue requestQueue = Volley.newRequestQueue(Student_Registration.this);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("email", email);
            jsonObject.put("designation", designation);
            jsonObject.put("mobile_no", mobile_no);
            jsonObject.put("name", "employname");
            Log.e("error", jsonObject.toString() + "");

            //request, as I am using my own localhost for this tutorial
            // Request a string response from the provided URL.
            String url = "http://192.168.0.103:5000/employee-register";
            Log.e("error", "message of url");

            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("value of json2", response + "");
                                String responseStatus = response.getString("response");
                                if (responseStatus.equals("User Inserted")) {
                                    Log.e("value of if", "" + responseStatus);
                                    Toast.makeText(getApplicationContext(), "response value" + responseStatus, Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("value of else", responseStatus + "");
                                    Toast.makeText(getApplicationContext(), "response value" + responseStatus, Toast.LENGTH_SHORT).show();

                                }

                            } catch (Exception e) {
                                Log.e("error", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("message1234", jsonObject.toString() + "");
                            Log.e("error", error.getMessage() + "");
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            // Add the request to the RequestQueue.
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            Log.e("error jsonexception", e.getMessage());
        }
    }
}
