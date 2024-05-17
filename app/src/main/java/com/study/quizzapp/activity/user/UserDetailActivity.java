package com.study.quizzapp.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.study.quizzapp.R;
import com.study.quizzapp.api.ResultApi;
import com.study.quizzapp.model.ResultTest;
import com.study.quizzapp.model.Test;
import com.study.quizzapp.retrofit.RetrofitService;
import com.study.quizzapp.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailActivity extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    private int count_test;
    private ArrayList<Test> result = new ArrayList<>();
    private ResultApi restMethod_Result;
    private List<ResultTest> listResultTest;
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
        String temp = "Điểm trong bài thi này là: " + getIntent().getStringExtra("Marks");
        textView5.setText(temp);
        countTest();

    }

    private void countTest() {
        restMethod_Result = RetrofitService.getRetrofit().create(ResultApi.class);
        restMethod_Result.getAllResultByUserId(SharedPrefManager.getInstance(this).getUser().getId()).enqueue(new Callback<List<ResultTest>>() {
            @Override
            public void onResponse(@NonNull Call<List<ResultTest>> call, @NonNull Response<List<ResultTest>> response) {
                result.clear();
                listResultTest = response.body();
                List<Test> listTest = new ArrayList<>();
                HashSet<Long> setTest_id = new HashSet<>();
                assert listResultTest != null;
                for(ResultTest resultTest : listResultTest) {
                    if(setTest_id.add(resultTest.getTest().getId())) {
                        listTest.add(resultTest.getTest());
                    }
                }
                result.addAll(listTest);
                Log.e("The read success: " ,"su"+result.size());
                count_test = result.size();
                String temp = "Số bài đã làm trong app: " + String.valueOf(result.size());
                textView6.setText(temp);
            }
            @Override
            public void onFailure(@NonNull Call<List<ResultTest>> call, @NonNull Throwable throwable) {
                Log.e("The read failed: ", Objects.requireNonNull(throwable.getMessage()));
            }
        });
        Log.d("Sai nua thi chiu", String.valueOf(result.size()));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}