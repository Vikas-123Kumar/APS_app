package com.apseducation.studentinformation.profile.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.apseducation.studentinformation.selectpreference.SelectPreferences;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class Student_Login extends AppCompatActivity implements View.OnClickListener {
    private EditText user_name, user_password;
    private TextView not_registered;
    private Button log_in;
    String username, password;
    private ImageView show_hide;
    SharedPreferences SM;
    private static final String KEY_EMPTY = "";
    Context context;
    NetworkConnection networkConnection;
    User_Register_Bean userBean;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__login);
        user_name = findViewById(R.id.user_name);
        context = this;
        show_hide = findViewById(R.id.show_hide_icon_pass);
        log_in = findViewById(R.id.submit);
        user_password = findViewById(R.id.pass_word);
        not_registered = findViewById(R.id.sign_up);
        not_registered.setOnClickListener(this);
        show_hide.setOnClickListener(this);
        log_in.setOnClickListener(this);
        networkConnection = new NetworkConnection(context);
        SM = getSharedPreferences("userrecord", 0);
        Boolean islogin = SM.getBoolean("userlogin", false);
        dataBaseHelper = new DataBaseHelper(this);
        if (islogin) {
            startActivity(new Intent(getApplicationContext(), SelectPreferences.class));

            return;
        }
    }

    public void userLogin() {
        userBean = new User_Register_Bean();
        username = user_name.getText().toString();
        password = user_password.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(Student_Login.this);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            Log.e("error", jsonObject.toString() + "");
            //request, as I am using my own localhost for this tutorial
            // Request a string response from the provided URL.
            String url = "http://192.168.0.103:5000/";
            Log.e("error", "message of url");
            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("value of json2", jsonObject.toString() + "");
                                String responseStatus = response.getString("response");
                                Log.e("response", response + "");
                                Log.e("response", responseStatus + "");

                                if (responseStatus.equals("ok")) {
                                    Toast.makeText(getApplicationContext(), "response value" + responseStatus, Toast.LENGTH_SHORT).show();
                                    Log.e("value of if", jsonObject.toString() + "");
                                    startActivity(new Intent(getApplicationContext(), SelectPreferences.class));
                                } else {
                                    Log.e("value of else", jsonObject.toString() + "");
                                    Toast.makeText(getApplicationContext(), "not register", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.e("error....", e.getMessage());
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


    @Override
    protected void onRestart() {
        user_name.setText("");
        user_password.setText("");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit) {
            if (validateInputs()) {
                if (dataBaseHelper.checkUser(username, password)) {
                    SharedPreferences.Editor edit = SM.edit();
                    edit.putBoolean("userlogin", true);
                    edit.commit();
                    Snackbar.make(v, "LogIn successfully", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), SelectPreferences.class);
                    intent.putExtra("name", username);
                    startActivity(intent);
                } else {
                    Snackbar.make(v, "Username and password does not exist", Snackbar.LENGTH_LONG).show();
                }
            }
        }
        if (v.getId() == R.id.show_hide_icon_pass) {
            if (user_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                show_hide.setImageResource(R.drawable.show_password);
                //Show Password
                user_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                show_hide.setImageResource(R.drawable.hide_password);
                //Hide Password
                user_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
        if (v.getId() == R.id.sign_up) {
            startActivity(new Intent(getApplicationContext(), Student_Registration.class));
        }
    }

    public boolean validateInputs() {                   // check that edittext is empty or not.
        username = user_name.getText().toString().toLowerCase().trim();
        password = user_password.getText().toString().trim();
        if (KEY_EMPTY.equals(username)) {
            user_name.setError("Username cannot be empty");
            user_name.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            user_password.setError("Password cannot be empty");
            user_password.requestFocus();
            return false;
        }

        return true;
    }

}
