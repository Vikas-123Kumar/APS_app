package com.apseducation.studentinformation.event;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.apseducation.studentinformation.R;
import com.apseducation.studentinformation.api.API_URL;
import com.apseducation.studentinformation.bean.Aps_Bean;
import com.apseducation.studentinformation.bean.EventBean;
import com.apseducation.studentinformation.connection.NetworkConnection;
import com.apseducation.studentinformation.profile.ui.sqlite.DataBaseHelper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class Event extends AppCompatActivity implements View.OnClickListener {
    String name, fatherName, contact, address, eventId;
    View snack_view;
    ProgressBar pbar;
    EventBean eventBean;
    DataBaseHelper dataBaseHelper;
    private EditText edit_name, edit_father, edit_contact, edit_address;
    private Button submit;
    private Spinner event_date, event_name, event_venue;
    private Context context;
    ArrayList<EventBean> apsEventList = new ArrayList<>();
    private ArrayAdapter eventNameAdapter, eventDateAdapter, eventVenueAdapter;
    private ArrayList eventNameList, eventVenueList, eventDateList, eventIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        context = this;
        init();
        dataBaseHelper = new DataBaseHelper(context);
        eventBean = new EventBean();
        getEvent();
        setupToolbar();
        event_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int getid = parent.getSelectedItemPosition();
                eventId = (String) eventIdList.get(getid);
                Log.e("id", eventId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void init() {
        edit_name = findViewById(R.id.edit_event_name);
        edit_contact = findViewById(R.id.edit_event_contact);
        edit_address = findViewById(R.id.address_event);
        submit = findViewById(R.id.eventSubmit);
        edit_father = findViewById(R.id.edit_event_father);
        event_name = findViewById(R.id.edit_eventname);
        event_venue = findViewById(R.id.edit_eventaddress);
        event_date = findViewById(R.id.edit_event_date);
        pbar = findViewById(R.id.progresBarevent);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        snack_view = v;
        if (v.getId() == R.id.eventSubmit) {
            if (checkValidation()) {
                if (NetworkConnection.isConnected(context)) {
                    submitEvent();
                } else {
                    submitEventOnLocal();
                }
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

    public void getEvent() {
        if (NetworkConnection.isConnected(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL.event_get,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //hiding the progressbar after completio
                            try {
                                Log.e("event name", response + "");
                                JSONArray myListsAll = new JSONArray(response);
                                eventNameList = new ArrayList();
                                eventDateList = new ArrayList();
                                eventVenueList = new ArrayList();
                                eventIdList = new ArrayList();
                                eventBean = new EventBean();
                                for (int i = 0; i < myListsAll.length(); i++) {
                                    JSONObject jsonobject = (JSONObject) myListsAll.get(i);
                                    String eventName = jsonobject.optString("name");
                                    String eventvenue = jsonobject.optString("venue");
                                    String eventdate = jsonobject.optString("date");
                                    String eventId = jsonobject.optString("id");
                                    eventBean.setEventId(eventId);
                                    eventBean.setEventName(eventName);
                                    eventBean.setEventVenue(eventvenue);
                                    eventBean.setEventDate(eventdate);
                                    if (!dataBaseHelper.checkevent(eventId)) {
                                        dataBaseHelper.insert_eventDetail(eventBean);
                                    }
                                    eventNameList.add(eventName);
                                    eventVenueList.add(eventvenue);
                                    eventDateList.add(eventdate);
                                    eventIdList.add(eventId);

                                }
                                addEventName();
                                addEventDate();
                                addEventVenue();
                                //getting the whole json object from the response
                                //now looping through all the elements of the json array

                            } catch (Exception e) {
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
            if (dataBaseHelper != null) {
                dataBaseHelper = new DataBaseHelper(getApplicationContext());
                apsEventList = dataBaseHelper.getEventDetail();
                eventNameList = new ArrayList();
                eventDateList = new ArrayList();
                eventVenueList = new ArrayList();
                eventIdList = new ArrayList();
                for (EventBean eventBean : apsEventList) {
                    Log.e("eventid", eventBean.getEventId());

                    getEventLocal(eventBean);
                }
            }
            addEventName();
            addEventDate();
            addEventVenue();
        }
    }

    public void getEventLocal(EventBean eventBean) {
        String eventName = eventBean.getEventName();
        String eventDate = eventBean.getEventDate();
        String eventVenue = eventBean.getEventVenue();
        String eventId = eventBean.getEventId();
        eventNameList.add(eventName);
        eventVenueList.add(eventVenue);
        eventDateList.add(eventDate);
        eventIdList.add(eventId);


    }

    public void addEventName() {
        eventNameAdapter = new ArrayAdapter(this, R.layout.spinner_item, eventNameList);
        eventNameAdapter.setDropDownViewResource(R.layout.spinner_item);
        event_name.setAdapter(eventNameAdapter);
        Log.e("name of event", eventNameList + "");

    }

    public void addEventDate() {
        eventDateAdapter = new ArrayAdapter(this, R.layout.spinner_item, eventDateList);
        eventDateAdapter.setDropDownViewResource(R.layout.spinner_item);
        event_date.setAdapter(eventDateAdapter);
    }

    public void addEventVenue() {
        eventVenueAdapter = new ArrayAdapter(this, R.layout.spinner_item, eventVenueList);
        eventVenueAdapter.setDropDownViewResource(R.layout.spinner_item);
        event_venue.setAdapter(eventVenueAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public boolean checkValidation() {
        name = edit_name.getText().toString();
        contact = edit_contact.getText().toString();
        address = edit_address.getText().toString();
        fatherName = edit_father.getText().toString();
        if (name.length() < 5) {
            edit_name.setError("Username should be minimun 5 characters");
            edit_name.requestFocus();
            Snackbar.make(snack_view, "Username should be minimun 5 characters", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!isValid_Phone(contact)) {
            edit_contact.setError("Please enter valid phone no.");
            edit_contact.requestFocus();
            Snackbar.make(snack_view, "Please enter valid phone no.", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (address.isEmpty()) {
            edit_address.setError("Address can not be blank");
            edit_address.requestFocus();
            Snackbar.make(snack_view, "Address can not be blank", Snackbar.LENGTH_LONG).show();
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
        // "^[A-Za-z][A-Za-z0-9]*([._-]?[A-Za-z0-9]+)@[A-Za-z].[A-Za-z]{0,3}?.[A-Za-z]{0,2}$";
    }


    public void submitEvent() {
        pbar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(Event.this);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("fatherName", fatherName);
            jsonObject.put("mobile", contact);
            jsonObject.put("address", address);
            jsonObject.put("id", eventId);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, API_URL.event_url_create, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("value of json2", response + "");
                                String responseStatus = response.getString("msg");
                                if (responseStatus.equals("Success")) {
                                    Log.e("value of if", "" + responseStatus);
                                    Toast.makeText(getApplicationContext(), responseStatus, Toast.LENGTH_SHORT).show();
                                    pbar.setVisibility(View.GONE);
                                } else {
                                    pbar.setVisibility(View.GONE);
                                    Log.e("value of else", responseStatus + "");
                                    Toast.makeText(getApplicationContext(), responseStatus, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                pbar.setVisibility(View.GONE);
                                Log.e("error.....", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pbar.setVisibility(View.GONE);
                            Log.e("error", error.getMessage() + "");
                        }
                    });
            // Add the request to the RequestQueue.
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }

    public void submitEventOnLocal() {
        eventBean.setNameE(name);
        eventBean.setContactE(contact);
        eventBean.setAddressE(address);
        eventBean.setFatherName(fatherName);
        eventBean.setEventId(eventId);
        dataBaseHelper.insert_event(eventBean);
        Snackbar.make(snack_view, "Event successfully", Snackbar.LENGTH_LONG).show();
    }
}