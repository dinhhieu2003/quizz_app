package com.study.quizzapp.activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.study.quizzapp.R;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_detail);

        Toolbar toolbar =  findViewById(R.id.toolbartst);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFF);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        textView1 = findViewById(R.id.StudentID);
        textView2 = findViewById(R.id.FnameID);
        textView3 = findViewById(R.id.AgeID);
        textView4 = findViewById(R.id.EmailID);
        textView5 = findViewById(R.id.this_Section);
        textView6 = findViewById(R.id.this_Section_count);

        textView1.setText(String.valueOf(getIntent().getLongExtra("USERID", 0)));
        textView2.setText(getIntent().getStringExtra("USERFNAME"));
        textView3.setText(String.valueOf(getIntent().getIntExtra("USERAGE", 0)));
        textView4.setText(getIntent().getStringExtra("USEREMAIL"));
        String temp = "";
        textView5.setText(temp);
        temp = "Số bài đã làm trong app: " + String.valueOf(getIntent().getIntExtra("Count", 0));
        textView6.setText(temp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}