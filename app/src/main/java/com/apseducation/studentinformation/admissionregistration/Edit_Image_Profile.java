package com.apseducation.studentinformation.admissionregistration;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.apseducation.studentinformation.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

public class Edit_Image_Profile extends AppCompatActivity {
    private Context context;
    private ImageView full_prof_view;
    Bitmap bitmap;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__image__profile);
        context = this;
        init_view();
        setupToolbar();
    }

    private void init_view() {
        full_prof_view = findViewById(R.id.full_profile_image);
        final File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/APSEducation/documents/" + "profile");
        String fileName = System.currentTimeMillis() + ".jpg";

        File f = new File(path, fileName);
        imagePath = f.getAbsolutePath();
        bitmap = BitmapFactory.decodeFile(imagePath);
        full_prof_view.setImageBitmap(bitmap);
        if (f.exists()) {
            Picasso.get()
                    .load(f)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .fit()
                    .into(full_prof_view);
            Log.e("filepath", f + "");
        } else if (!f.exists()) {
            Picasso.get()
                    .load(R.drawable.profile)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .fit()
                    .into(full_prof_view);
            Log.e("file in else", f + "");
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
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
//        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
