package com.apseducation.studentinformation.selectpreference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apseducation.studentinformation.R;
import com.apseducation.studentinformation.adapter.CollegeAdapter;
import com.apseducation.studentinformation.NetworkMoniter;
import com.apseducation.studentinformation.api.API_URL;
import com.apseducation.studentinformation.bean.Aps_Bean;
import com.apseducation.studentinformation.connection.NetworkConnection;
import com.apseducation.studentinformation.profile.ui.Student_Login;
import com.apseducation.studentinformation.profile.ui.sqlite.DataBaseHelper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectPreferences extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_WRITE_STORAGE = 112;
    Context context;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    TextView textView, profile_name;
    Aps_Bean apsBean;
    ProgressBar progressBar;
    CollegeAdapter collegeAdapter;
    RecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    ArrayList<Aps_Bean> collegeName;

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_preferences);
        context = this;
        recyclerView = findViewById(R.id.recyclerView);
        profile_name = findViewById(R.id.profile_name);
        collegeName = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(context);
        apsBean = new Aps_Bean();
        progressBar = findViewById(R.id.progresBar);
        setup_nav_toolbar();
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (!NetworkConnection.isConnected(context)) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        //get college names fro server and set on recycler view with radio button
        if (NetworkConnection.isConnected(context)) {
            Log.e("connected", "connected");
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL.collegeName_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //hiding the progressbar after completio
                            Log.e("response",response);
                            try {
                                //getting the whole json object from the response
                                JSONObject obj = new JSONObject(response);
                                //we have the array named hero inside the object
                                //so here we are getting that json array
                                JSONArray jsonArray = obj.getJSONArray("colleges");
                                //now looping through all the elements of the json array
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject Object = jsonArray.getJSONObject(i);
                                    apsBean = new Aps_Bean();
                                    //creating a hero object and giving them the values from json object
                                    apsBean.setCollegeName(Object.getString("collegeName"));
                                    apsBean.setCollege_id(Object.getString("_id"));
                                    collegeName.add(apsBean);
                                    progressBar.setVisibility(View.GONE);
                                    if (!dataBaseHelper.checkCollegename(Object.getString("collegeName"))) {
                                        dataBaseHelper.addCollege(apsBean);
                                    }
                                    //adding the hero to herolist
                                }
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                collegeAdapter = new CollegeAdapter(context, collegeName);
                                recyclerView.setAdapter(collegeAdapter);
                                collegeAdapter.notifyDataSetChanged();

                                //creating custom adapter object

                            } catch (JSONException e) {
                                Log.e("error....", e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                               Log.e("error",error+"");
                            Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
                            //displaying the error in toast if occurrs
                        }
                    });

            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            //adding the string request to request queue
            requestQueue.add(stringRequest);
        } else {
            if (dataBaseHelper != null) {
                dataBaseHelper = new DataBaseHelper(getApplicationContext());
                ArrayList<Aps_Bean> apsList = dataBaseHelper.getCollegeForLocal();
                Log.e("college name", apsList + "");
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                collegeAdapter = new CollegeAdapter(context, apsList);
                recyclerView.setAdapter(collegeAdapter);
                collegeAdapter.notifyDataSetChanged();

            }

        }

        //this is for sync datt to get from sqlite data base when internet is available
        if (NetworkConnection.isConnected(context)) {
            registerReceiver(new NetworkMoniter(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        // save this file into external storage & get  from external storage when you are in internet connection
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            boolean hasPermission = (ContextCompat.checkSelfPermission(getBaseContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            Log.e("permission", hasPermission + "");
            if (!hasPermission) {
                ActivityCompat.requestPermissions(SelectPreferences.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION
                                , Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        REQUEST_WRITE_STORAGE);
                Log.e("permission......", hasPermission + "");

            }
        }

    }

    public void setup_nav_toolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getActionBar() != null) getActionBar();
        DrawerLayout drawer = findViewById(R.id.drawer_app_select);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SelectPreferences.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectPreferences.this);
            alertDialog.setMessage("Do you want to logout?");
            alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences SM = getSharedPreferences("userrecord", 0);
                    SharedPreferences.Editor edit = SM.edit();
                    edit.putBoolean("userlogin", false);
                    edit.commit();
                    Intent intent = new Intent(SelectPreferences.this, Student_Login.class);
                    startActivity(intent);
                    finish();
                }
            });
            alertDialog.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = alertDialog.create();
            dialog.show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_app_select);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            circularImageView.setImageBitmap(photo);
        }
    }

}
