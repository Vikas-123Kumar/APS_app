package com.apseducation.studentinformation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.apseducation.studentinformation.adapter.ShowDataAdapter;
import com.apseducation.studentinformation.bean.Registration_Bean;
import com.apseducation.studentinformation.bean.Survey_bean;

public class ShowSearchData extends AppCompatActivity {
    RecyclerView recyclerView;
    private Context context;
    ShowDataAdapter showDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_search_data);
        context = this;
        recyclerView = findViewById(R.id.show_recycler_view);
        Survey_bean survey_bean = (Survey_bean) getIntent().getSerializableExtra("surveyBean");
        Registration_Bean registration_bean = (Registration_Bean) getIntent().getSerializableExtra("registrationBean");
        Log.e("survey bean", survey_bean + "");
        Log.e("registration bean", registration_bean + "");

        if (survey_bean == null && registration_bean != null) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            showDataAdapter = new ShowDataAdapter(registration_bean);
            recyclerView.setAdapter(showDataAdapter);
            showDataAdapter.notifyDataSetChanged();
        } else if (survey_bean != null && registration_bean == null) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            showDataAdapter = new ShowDataAdapter(survey_bean);
            recyclerView.setAdapter(showDataAdapter);
            showDataAdapter.notifyDataSetChanged();
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
