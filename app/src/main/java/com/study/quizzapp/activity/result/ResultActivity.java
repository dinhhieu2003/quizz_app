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
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.study.quizzapp.R;
import com.study.quizzapp.api.ResultApi;
import com.study.quizzapp.api.TestApi;
import com.study.quizzapp.model.ResultTest;
import com.study.quizzapp.model.Test;
import com.study.quizzapp.retrofit.RetrofitService;
import com.study.quizzapp.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {
    private ListView listView;
    private ResultActivity.TestAdapter testAdapter;
    private ArrayList<Test> result = new ArrayList<>();
    private List<ResultTest> listResultTest;
    private int lastPos = -1;
    private boolean isAdmin = false;
    private ProgressBar progressBar;
    private TestApi restMethod_Test;
    private ResultApi restMethod_Result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);
        isAdmin = getIntent().getBooleanExtra("ISADMIN",false);
        if(!isAdmin)
            setTitle("Results");
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFF);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressbar_test);
        progressBar.setVisibility(View.VISIBLE);
        listView = findViewById(R.id.test_listview);
        testAdapter = new ResultActivity.TestAdapter(ResultActivity.this, result);
        listView.setAdapter(testAdapter);
        getResults();
    }

    private void getResults() {
        if(isAdmin) {
            restMethod_Test = RetrofitService.getRetrofit().create(TestApi.class);
            restMethod_Test.getAll().enqueue(new Callback<List<Test>>() {
                @Override
                public void onResponse(@NonNull Call<List<Test>> call, @NonNull Response<List<Test>> response) {
                    result.clear();
                    List<Test> listTest = response.body();
                    assert listTest != null;
                    result.addAll(listTest);

                    testAdapter.dataList = result;
                    testAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    Log.e("The read success: " ,"su"+result.size());
                }

                @Override
                public void onFailure(@NonNull Call<List<Test>> call, @NonNull Throwable throwable) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("The read failed: ", Objects.requireNonNull(throwable.getMessage()));
                }
            });
        } else {
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

                    testAdapter.dataList = result;
                    testAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    Log.e("The read success: " ,"su"+result.size());
                }

                @Override
                public void onFailure(@NonNull Call<List<ResultTest>> call, @NonNull Throwable throwable) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("The read failed: ", Objects.requireNonNull(throwable.getMessage()));
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class TestAdapter extends ArrayAdapter<Test> implements Filterable {

        private final Context mContext;
        ArrayList<Test> dataList;

        public TestAdapter( Context context,ArrayList<Test> list) {
            super(context, 0, list);
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
            listItem.findViewById(R.id.item_imageView).setPadding(10,0,0,0);
            ((TextView)listItem.findViewById(R.id.item_textView)).setText(dataList.get(position).getName());
            ((Button)listItem.findViewById(R.id.item_button)).setText("Xem");

            Animation animation = AnimationUtils.loadAnimation(getContext(),
                    (position > lastPos) ? R.anim.up_from_bottom : R.anim.down_from_top);

            (listItem).startAnimation(animation);
            lastPos = position;
            ((Button)listItem.findViewById(R.id.item_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ResultActivity.this, ResultDetailActivity.class);
                    intent.putExtra("TEST",dataList.get(position));
                    intent.putExtra("ISAdmin",isAdmin);
                    startActivity(intent);
                }
            });
            return listItem;
        }
    }
}