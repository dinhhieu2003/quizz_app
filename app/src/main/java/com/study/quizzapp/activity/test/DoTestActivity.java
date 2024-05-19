package com.study.quizzapp.activity.test;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.study.quizzapp.R;
import com.study.quizzapp.api.AuthApi;
import com.study.quizzapp.api.ResultApi;
import com.study.quizzapp.model.Question;
import com.study.quizzapp.model.ResultTest;
import com.study.quizzapp.model.Test;
import com.study.quizzapp.model.User;
import com.study.quizzapp.retrofit.RetrofitService;
import com.study.quizzapp.service.NotificationService;
import com.study.quizzapp.sharedpref.SharedPrefManager;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoTestActivity extends AppCompatActivity {
    private ArrayList<Question> questions = new ArrayList<>();
    private String []answers;
    private Toolbar toolbar;
    private DiscreteScrollView scrollView;
    private LinearLayout indexLayout;
    private GridView quesGrid;
    private ArrayList<String> list;
    private ArrayList<String> arrayList;
    private int flag_controller = 1;
    private long timer;// =((Test) getIntent().getExtras().get("Questions")).getTime()*60*1000;
    private popGridAdapter popGrid;
    private Button next,prev;
    private TextView textView;
    private String TESTNAME;
    private RadioGroup group;
    private int countPaused = 0;
    private ResultApi restMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_do_test);

        List<Question> listQuestion = ((Test) getIntent().getExtras().get("Questions")).getQuestionList();
        for(Question ques: listQuestion) {
            Log.d("Do Test", ques.getQuestion());
        }
        Log.d("Size test", String.valueOf(listQuestion.size()));
        questions.addAll(listQuestion);
        TESTNAME = (String) getIntent().getExtras().get("TESTNAME");
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFF);
        answers=new String[questions.size()];
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFF);
        scrollView = findViewById(R.id.discrete);
        final QuestionAdapter questionAdapter=new QuestionAdapter(questions);
        scrollView.setAdapter(questionAdapter);

        next=findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scrollView.getCurrentItem()==questions.size()-1){
                    showPopUp();
                }else {
                    setNextPrevButton(scrollView.getCurrentItem() + 1);
                    scrollView.smoothScrollToPosition(scrollView.getCurrentItem() + 1);
                }
            }
        });

        prev=findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scrollView.getCurrentItem()!=0){
                    setNextPrevButton(scrollView.getCurrentItem()-1);
                    scrollView.smoothScrollToPosition(scrollView.getCurrentItem()-1);
                }
            }
        });

        setNextPrevButton(scrollView.getCurrentItem());
        indexLayout=findViewById(R.id.index_layout);
        indexLayout.setAlpha(.5f);
        quesGrid = findViewById(R.id.pop_grid);
        popGrid = new popGridAdapter(DoTestActivity.this);
        quesGrid.setAdapter(popGrid);
        quesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                scrollView.smoothScrollToPosition(i+1);
                slideUp(indexLayout);
            }
        });
        scrollView.addScrollListener(new DiscreteScrollView.ScrollListener<RecyclerView.ViewHolder>() {
            @Override
            public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {
                setNextPrevButton(newPosition);
            }
        });

        timer=((Test) getIntent().getExtras().get("Questions")).getTime()*60*1000;

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showPopUp();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);
    }
    void showPopUp(){
        AlertDialog.Builder builder=new AlertDialog.Builder(DoTestActivity.this);
        builder.setMessage("Bạn có chắc muốn nộp bài?");
        builder.setPositiveButton("Chắc", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                submit();
                dialogStart();
            }
        });

        builder.setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();

    }

    /*submit result to database**/
    void submit(){
        flag_controller = 0;
        int score = 0;
        list = new ArrayList<>();
        arrayList = new ArrayList<>();
        for(int i = 0; i < answers.length; i++){
            if(answers[i] != null && answers[i].equals(questions.get(i).getAnswer())){
                score++;
            }
            String temp = (answers[i]!=null) ? answers[i]+") ":"null) ";

            list.add("Bạn chọn ("+ temp + "Đáp án đúng là("+ questions.get(i).getAnswer()+")");
            arrayList.add(questions.get(i).getQuestion());
        }

        try {
            ResultTest resultTest = new ResultTest();
            resultTest.setScore(score);
            User user = SharedPrefManager.getInstance(this).getUser();
            Test test = (Test) getIntent().getExtras().get("Questions");
            resultTest.setUser(user);
            resultTest.setTest(test);
            restMethod = RetrofitService.getRetrofit().create(ResultApi.class);
            restMethod.addResultTest(resultTest).enqueue(new Callback<ResultTest>() {
                @Override
                public void onResponse(Call<ResultTest> call, Response<ResultTest> response) {
                    Log.d("Submit success", response.body().getTest().getName());
                    Toasty.success(DoTestActivity.this,"Bài của bạn đã nộp thành công rồi",
                            Toasty.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<ResultTest> call, Throwable throwable) {
                    Toasty.error(DoTestActivity.this,"Bài của bạn đã nộp thất bại rồi",
                            Toasty.LENGTH_SHORT).show();
                    Log.d("Submit failed", throwable.getMessage());
                }
            });
        }catch (Exception e){
            Log.e("Result Update Failed " ,e.getMessage());
        }
    }

    void dialogStart() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(DoTestActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher_round);
        builderSingle.setTitle(TESTNAME+" Đáp án");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (DoTestActivity.this, android.R.layout.select_dialog_singlechoice);
        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>
                (DoTestActivity.this,android.R.layout.select_dialog_singlechoice);

        for(String y : arrayList) {
            arrayAdapter1.add(y);
        }
        for(String x: list){
            arrayAdapter.add(x);
        }

        builderSingle.setCancelable(false);
        builderSingle.setNegativeButton("Xác nhận!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(DoTestActivity.this);
                builderInner.setMessage(strName);
                builderInner.setCancelable(false);
                builderInner.setTitle("Đáp án mà bạn lựa chọn là: ");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
//                        finish();
                        builderSingle.show();
//                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(countPaused<=2 && countPaused >=0 && flag_controller == 1)
            startService(new Intent(DoTestActivity.this, NotificationService.class));
        countPaused++;
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(DoTestActivity.this, NotificationService.class));
        if(countPaused>2) {
            Toasty.success(DoTestActivity.this,"Cảm ơn! Bài của bạn đã được nộp.",
                    Toasty.LENGTH_SHORT).show();
            countPaused = -1000;
            Log.d("On resume", "On");
//            submit();
            dialogStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        stopService(new Intent(DoTestActivity.this, NotificationService.class));
    }

    void setNextPrevButton(int pos){
        if(pos==0){
//            prev.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            prev.setText("");
        }else {
//            prev.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            prev.setText("Câu trước");
        }
        if(pos==questions.size()-1){
            next.setText("Nộp bài");
//            next.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        }else {
            next.setText("Câu tiếp");
//            next.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.do_test_menu, menu);
        final MenuItem  counter = menu.findItem(R.id.counter);

        new CountDownTimer(timer, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                long hr= TimeUnit.MILLISECONDS.toHours(millis),mn=(TimeUnit.MILLISECONDS.toMinutes(millis)-
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))),
                        sc=TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));


                String  hms =format(hr)+":"+format(mn)+":"+format(sc) ;
                counter.setTitle(hms);
                timer = millis;
            }
            String format(long n){
                if(n<10)
                    return "0"+n;
                else return ""+n;
            }

            public void onFinish() {
                submit();
                dialogStart();
            }
        }.start();

        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.submit){
            showPopUp();

            return true;
        }else if(id==R.id.info){
            togglePopUp();
        }
        return super.onOptionsItemSelected(item);
    }


    void togglePopUp(){
        if(indexLayout.getVisibility()==View.GONE){
            slideDown(indexLayout);
        }else slideUp(indexLayout);
    }

    class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

        private int itemHeight;
        private ArrayList<Question> data;

        QuestionAdapter(ArrayList<Question> data) {
            this.data = data;
        }


        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            Activity context = (Activity) recyclerView.getContext();
            Point windowDimensions = new Point();
            context.getWindowManager().getDefaultDisplay().getSize(windowDimensions);
            itemHeight = Math.round(windowDimensions.y * 0.6f);
        }

        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.frag_question, parent, false);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    itemHeight);
            v.setLayoutParams(params);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.questionText.setText(data.get(position).getQuestion());
            holder.r1.setText(data.get(position).getOpt_A());
            holder.r2.setText(data.get(position).getOpt_B());
            holder.r3.setText(data.get(position).getOpt_C());
            holder.r4.setText(data.get(position).getOpt_D());
            holder.r5.setText("Xóa lựa chọn");

            holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    final int selectedId = holder.radioGroup.getCheckedRadioButtonId();
                    if(i==R.id.radioButton){
                        answers[position]="A";
                    }  else if(i==R.id.radioButton2){
                        answers[position]="B";
                    }else if(i==R.id.radioButton3){
                        answers[position]="C";
                    }else if(i==R.id.radioButton4){
                        answers[position]="D";
                    }
                    else if(i==R.id.radioButton5) {
                        holder.radioGroup.clearCheck();
                        answers[position] = null;
                    }
                    popGrid.notifyDataSetChanged();
                }
            });


            if(answers[position]==null) {
                holder.radioGroup.clearCheck();
            }else if(answers[position].equals("A")) {
                holder.radioGroup.check(R.id.radioButton);
            }else if(answers[position].equals("B")) {
                holder.radioGroup.check(R.id.radioButton2);
            }else if(answers[position].equals("C")) {
                holder.radioGroup.check(R.id.radioButton3);
            }else if(answers[position].equals("D")) {
                holder.radioGroup.check(R.id.radioButton4);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private View overlay;
            private  TextView questionText;
            private RadioGroup radioGroup;
            private RadioButton r1,r2,r3,r4,r5;

            ViewHolder(View itemView) {
                super(itemView);
                questionText =  itemView.findViewById(R.id.questionTextView);
                radioGroup=itemView.findViewById(R.id.radioGroup);
                r1=itemView.findViewById(R.id.radioButton);
                r2=itemView.findViewById(R.id.radioButton2);
                r3=itemView.findViewById(R.id.radioButton3);
                r4=itemView.findViewById(R.id.radioButton4);
                r5 = itemView.findViewById(R.id.radioButton5);
            }

            public void setOverlayColor(@ColorInt int color) {
                overlay.setBackgroundColor(color);
            }

            public void unCheck() {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                if(radioGroup.getCheckedRadioButtonId() == R.id.radioButton) {
                    r1.setChecked(true);
                }
                else if(radioGroup.getCheckedRadioButtonId() == R.id.radioButton2) {
                    r2.setChecked(true);
                }
                else if(radioGroup.getCheckedRadioButtonId() == R.id.radioButton3) {
                    r3.setChecked(true);
                }
                else if(radioGroup.getCheckedRadioButtonId() == R.id.radioButton4) {
                    r4.setChecked(true);
                }
                else if(radioGroup.getCheckedRadioButtonId() ==R.id.radioButton5) {
                    r5.setChecked(true);
                }
            }
        }
    }

    class popGridAdapter extends BaseAdapter {
        Context mContext;
        popGridAdapter(Context context){
            mContext=context;
        }
        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getCount() {
            return questions.size();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View convertView;
            if(view==null){
                convertView=new Button(mContext);
            }else convertView=view;
            if(answers[i]==null)
                (convertView).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            else
                (convertView).setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

            ((Button)convertView).setText(""+(i+1));

            (convertView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setNextPrevButton(i);
                    scrollView.smoothScrollToPosition(i);
                }
            });
            return convertView;
        }
    }

    public void slideUp(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                -view.getHeight());                // toYDelta
        animate.setDuration(500);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -view.getHeight(),                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        view.startAnimation(animate);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}