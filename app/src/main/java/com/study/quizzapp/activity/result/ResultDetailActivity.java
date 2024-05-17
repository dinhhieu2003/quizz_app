package com.study.quizzapp.activity.result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.study.quizzapp.R;
import com.study.quizzapp.activity.user.UserDetailActivity;
import com.study.quizzapp.api.ResultApi;
import com.study.quizzapp.model.ResultTest;
import com.study.quizzapp.model.Test;
import com.study.quizzapp.retrofit.RetrofitService;
import com.study.quizzapp.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultDetailActivity extends AppCompatActivity {

    private ResultDetailActivity.TestAdapter testAdapter;
    ArrayList<ResultTest> result=new ArrayList<>();
    private String testName;
    private int lastPos = -1;
    private int count_test = 0;
    public boolean isAdmin = false;
    private ResultApi restMethod;
    private Test currentTest;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);

        isAdmin = getIntent().getBooleanExtra("ISAdmin",false);
        currentTest = (Test)getIntent().getExtras().get("TEST");
        testName = currentTest.getName();
        progressBar = findViewById(R.id.progressbar_test);
        progressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFF);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ListView listView = findViewById(R.id.test_listview);
        testAdapter = new ResultDetailActivity.TestAdapter(ResultDetailActivity.this,result);
        listView.setAdapter(testAdapter);
        getSupportActionBar().setTitle(testName);
        getResults();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getResults() {
        // get toan bo result theo test id tu TEST
        restMethod = RetrofitService.getRetrofit().create(ResultApi.class);
        if(isAdmin) {
            restMethod.getAllResultByTestId(currentTest.getId()).enqueue(new Callback<List<ResultTest>>() {
                @Override
                public void onResponse(@NonNull Call<List<ResultTest>> call, @NonNull Response<List<ResultTest>> response) {
                    List<ResultTest> listResult;
                    if(response.body() != null) {
                        Log.d("Get result success", response.message());
                        listResult = response.body();
                        result.addAll(listResult);
                        testAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    } else {
                        Log.d("Get result failed", response.message());
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ResultTest>> call, @NonNull Throwable throwable) {
                    Log.d("Get result failed on Failure", throwable.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            restMethod.getAllResultByTestIdAndUserId(currentTest.getId(), SharedPrefManager.getInstance(this).getUser().getId())
                    .enqueue(new Callback<List<ResultTest>>() {
                        @Override
                        public void onResponse(Call<List<ResultTest>> call, Response<List<ResultTest>> response) {
                            List<ResultTest> listResult;
                            if(response.body() != null) {
                                Log.d("Get result success", response.message());
                                listResult = response.body();
                                result.addAll(listResult);
                                testAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);

                            } else {
                                Log.d("Get result failed", response.message());
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ResultTest>> call, Throwable throwable) {
                            Log.d("Get result failed on Failure", throwable.getMessage());
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }


    class TestAdapter extends ArrayAdapter<ResultTest> {
        private Context mContext;
        ArrayList<ResultTest> dataList;

        public TestAdapter( Context context,ArrayList<ResultTest> list) {
            super(context, 0 , list);
            mContext = context;
            dataList = list;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.test_item,parent,false);
            ((ImageView)listItem.findViewById(R.id.item_imageView))
                    .setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ranking));
            if(dataList.get(position).getUser()!=null)
                ((TextView)listItem.findViewById(R.id.item_textView)).setText(dataList.get(position).getUser().getFname());
            else {
                ((TextView) listItem.findViewById(R.id.item_textView)).setText("Details not added yet");
            }

            Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPos) ? R.anim.up_from_bottom : R.anim.down_from_top);
            (listItem).startAnimation(animation);
            lastPos = position;
            if(isAdmin ) {
                ((TextView) listItem.findViewById(R.id.item_textView)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ResultDetailActivity.this, UserDetailActivity.class);
                        intent.putExtra("USERID", dataList.get(position).getUser().getId());
                        intent.putExtra("USERFNAME", dataList.get(position).getUser().getFname());
                        intent.putExtra("USERAGE", dataList.get(position).getUser().getAge());
                        intent.putExtra("USEREMAIL", dataList.get(position).getUser().getEmail());
                        intent.putExtra("TestNAME", testName);
                        intent.putExtra("Marks", String.valueOf(dataList.get(position).getScore())
                                + "/" + String.valueOf(currentTest.getQuestionList().size()));
                        startActivity(intent);

                    }
                });
            }
            ((Button)listItem.findViewById(R.id.item_button)).setText(String.valueOf(dataList.get(position).getScore())
                    + "/" + String.valueOf(currentTest.getQuestionList().size()));
            return listItem;
        }
    }
}