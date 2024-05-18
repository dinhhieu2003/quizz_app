package com.study.quizzapp.activity.admin.quiz;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.study.quizzapp.R;
import com.study.quizzapp.api.QuestionApi;
import com.study.quizzapp.api.TestApi;
import com.study.quizzapp.dto.request.ListQuestionTestDTO;
import com.study.quizzapp.model.Question;
import com.study.quizzapp.model.Test;
import com.study.quizzapp.retrofit.RetrofitService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Custom_quiz extends AppCompatActivity{

    EditText question, aText, bText, cText, dText;
    RadioButton aRadio, bRadio, cRadio, dRadio;

    int currentQuestion = 1;
    int previousQuestion = 1;
    TextView questionNumber;

    ArrayList<Question> ques;
    JSONArray jsonArray;
    String selectedOption = "";

    Button save_button;
    AlertDialog alertDialog;
    String fileName = "file";
    CardView fab,f2,fl;
    private TestApi restMethod;
    private QuestionApi restMethod_Question;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        jsonArray = new JSONArray();
        setContentView(R.layout.activity_create_quizz);
        question = findViewById(R.id.questionView);
        question =  findViewById(R.id.questionView);
        aText =  findViewById(R.id.aText);
        bText =  findViewById(R.id.bText);
        cText =  findViewById(R.id.cText);
        dText =  findViewById(R.id.dText);
        questionNumber =  findViewById(R.id.questionNumber);
        aRadio =  findViewById(R.id.aRadio);

        bRadio =  findViewById(R.id.bRadio);
        cRadio =  findViewById(R.id.cRadio);
        dRadio =  findViewById(R.id.dRadio);

        selectedOption = "";
        currentQuestion = 1;
        setListeners();

        ques = new ArrayList<>();

        alertDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();

        fab = findViewById(R.id.nextfab);
        fl = findViewById(R.id.fab2);//save button
        f2 = findViewById(R.id.pre_card);

        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(previousQuestion>1) {
                    previousQuestion--;
                    setAllData(previousQuestion);
                }
                if(previousQuestion==1)
                    f2.setVisibility(View.INVISIBLE);
                //Question question1 = new Question();
                Toast.makeText(Custom_quiz.this, String.valueOf(previousQuestion), Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(previousQuestion!=currentQuestion) {
                    previousQuestion++;
                    if(previousQuestion!=currentQuestion)
                        setAllData(previousQuestion);
                    else {
                        clearAllData();
                        questionNumber.setText(String.valueOf(currentQuestion));
                    }
                    if(previousQuestion>1)
                        f2.setVisibility(View.VISIBLE);
                }
                boolean cont = getEnteredQuestionsValue();
                if (cont)
                {
                    previousQuestion++;
                    currentQuestion++;
                    Toast.makeText(Custom_quiz.this, "Câu hỏi " + currentQuestion, Toast.LENGTH_SHORT).show();
                    questionNumber.setText(String.valueOf(currentQuestion));
                    clearAllData();
                    f2.setVisibility(View.VISIBLE);
                }
            }
        });

        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ques.size() != 0)
                {
                    // get dialog_custom.xml view
                    LayoutInflater li = LayoutInflater.from(Custom_quiz.this);
                    View promptsView = li.inflate(R.layout.dialog_custom, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            Custom_quiz.this);

                    // set dialog_custom.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    final EditText userTestName =  promptsView
                            .findViewById(R.id.editTextDialogUserInput);
                    final EditText userTime = promptsView.findViewById(R.id.editTextDialogUserInput1);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {

                                            final int time = Integer.parseInt(userTime.getText().toString());
                                            final String testName = userTestName.getText().toString();
                                            Test newTest = new Test();
                                            newTest.setName(testName);
                                            newTest.setTime(time);

                                            // save Test
                                            saveTest(newTest);

                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
                else
                {
                    Toasty.error(getApplicationContext(),
                            "Bạn chưa hoàn thành form", Toasty.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveAllQuestion(ArrayList<Question> questions, Test test) {
        restMethod_Question = RetrofitService.getRetrofit().create(QuestionApi.class);
        ListQuestionTestDTO listQuestionTestDTO = new ListQuestionTestDTO(questions, test);
        restMethod_Question.addAllQuestion(listQuestionTestDTO).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                Log.d("Add Question api call success", response.body().get(0).getQuestion());
                Toasty.normal(Custom_quiz.this, "Thêm câu hỏi thành công").show();
                finish();
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable throwable) {
                Log.d("Add Question api call failed", throwable.getMessage());
                Toasty.error(Custom_quiz.this, throwable.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void saveTest(Test test) {
        Log.d("In save Test", test.getName());
        restMethod = RetrofitService.getRetrofit().create(TestApi.class);
        try {
            restMethod.add(test).enqueue(new Callback<Test>() {
                @Override
                public void onResponse(Call<Test> call, Response<Test> response) {
                    assert response.body() != null;
                    Log.d("Add Test api call success", response.body().getName());
                    Test testsaved = response.body();
                    saveAllQuestion(ques, testsaved);
                }
                @Override
                public void onFailure(Call<Test> call, Throwable throwable) {
                    Log.d("Add Test api call failed", throwable.getMessage());
                    Toasty.error(Custom_quiz.this, throwable.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            });
        } catch (Exception e) {
            Toasty.error(Custom_quiz.this, e.getMessage(), Toast.LENGTH_SHORT, true).show();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final AlertDialog.Builder builder = new AlertDialog.Builder(Custom_quiz.this);
        builder.setMessage("Exit without saving?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void updateData(int position) {
        Question question1 = new Question();
        question1 = ques.get(position-1);
    }

    public void setAllData(int position) {
        clearAllData();
        Question question1 = new Question();
        question1 = ques.get(position-1);
        questionNumber.setText(String.valueOf(question1.getId()));
        question.setText(question1.getQuestion());
        aText.setText(question1.getOpt_A());
        bText.setText(question1.getOpt_B());
        cText.setText(question1.getOpt_C());
        dText.setText(question1.getOpt_D());
        switch (question1.getAnswer()){
            case "A":
                aRadio.setChecked(true);
                break;
            case "B":
                bRadio.setChecked(true);
                break;
            case "C":
                cRadio.setChecked(true);
                break;
            case "D":
                dRadio.setChecked(true);
                break;
        }
    }

    private void clearAllData() {

        aRadio.setChecked(false);
        bRadio.setChecked(false);
        cRadio.setChecked(false);
        dRadio.setChecked(false);
        aText.setText(null);
        bText.setText(null);
        cText.setText(null);
        dText.setText(null);
        question.setText(null);
        selectedOption = "";
    }

    private boolean getEnteredQuestionsValue() {

        boolean cont = false;
        if (TextUtils.isEmpty(question.getText().toString().trim())) {
            question.setError("Hãy điền câu hỏi");
        }
        else if (TextUtils.isEmpty(aText.getText().toString().trim())) {
            aText.setError("Hãy điền đáp án A");
        }
        else if (TextUtils.isEmpty(bText.getText().toString().trim())) {
            bText.setError("Hãy điền đáp án B");
        }
        else if (TextUtils.isEmpty(cText.getText().toString().trim())) {
            cText.setError("Hãy điền đáp án C");
        }
        else if (TextUtils.isEmpty(dText.getText().toString().trim())) {
            dText.setError("Hãy điền đáp án D");
        }
        else if (selectedOption.equals("")) {
            Toast.makeText(this, "Hãy chọn đáp án đúng cho câu hỏi", Toast.LENGTH_SHORT).show();
        }
        else {
            Question quest = new Question();
            quest.setQuestion(question.getText().toString());
            quest.setOpt_A(aText.getText().toString());
            quest.setOpt_B(bText.getText().toString());
            quest.setOpt_C(cText.getText().toString());
            quest.setOpt_D(dText.getText().toString());
            quest.setAnswer(selectedOption);
            ques.add(quest);
            cont = true;
        }
        return cont;
    }

    private void setListeners() {
        aRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "A";
                bRadio.setChecked(false);
                cRadio.setChecked(false);
                dRadio.setChecked(false);
            }
        });
        bRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "B";
                aRadio.setChecked(false);
                cRadio.setChecked(false);
                dRadio.setChecked(false);
            }
        });
        cRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "C";
                bRadio.setChecked(false);
                aRadio.setChecked(false);
                dRadio.setChecked(false);
            }
        });
        dRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "D";
                bRadio.setChecked(false);
                cRadio.setChecked(false);
                aRadio.setChecked(false);
            }
        });

    }

}
