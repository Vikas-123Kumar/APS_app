package com.apseducation.studentinformation.selectpreference;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.apseducation.studentinformation.R;
import com.apseducation.studentinformation.admissionregistration.SurveyForm;
import com.apseducation.studentinformation.event.Event;
import com.apseducation.studentinformation.registration.Registration;

public class SelectSecondPreference extends AppCompatActivity {
    private RadioButton radioButton1, radioButton2, eventRadio;
    private RadioGroup radioGroup;
    TextView textView;
    String string;
    String radioValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_second_preference);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        eventRadio = findViewById(R.id.eventButton);
        textView = findViewById(R.id.text_view);
        string = getIntent().getStringExtra("college name");
        textView.setText(string);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioButton1.isChecked()) {
                    radioValue = radioButton1.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), SurveyForm.class);
                    intent.putExtra("form name", radioValue);
                    intent.putExtra("college", string);
                    startActivity(intent);
                } else if (radioButton2.isChecked()) {
                    radioValue = radioButton2.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), Registration.class);
                    intent.putExtra("form name", radioValue);
                    intent.putExtra("college", string);
                    startActivity(intent);
                }
                else if (eventRadio.isChecked()) {
                    radioValue = eventRadio.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), Event.class);
                    intent.putExtra("form name", radioValue);
                    intent.putExtra("college", string);
                    startActivity(intent);
                }
            }
        });
        setupToolbar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        radioGroup.clearCheck();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        ActionBar action_Bar = getSupportActionBar();
        if (action_Bar != null) {
            action_Bar.setDisplayHomeAsUpEnabled(true);
            action_Bar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
