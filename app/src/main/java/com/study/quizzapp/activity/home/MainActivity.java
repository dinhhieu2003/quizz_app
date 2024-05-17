package com.study.quizzapp.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.study.quizzapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.study.quizzapp.activity.admin.quiz.create_quiz_main;
import com.study.quizzapp.activity.auth.LoginActivity;
import com.study.quizzapp.activity.result.ResultActivity;
import com.study.quizzapp.activity.result.ResultDetailActivity;
import com.study.quizzapp.activity.test.ViewTestActivity;
import com.study.quizzapp.activity.user.UserDetailActivity;
import com.study.quizzapp.activity.user.UserProfileActivity;
import com.study.quizzapp.api.ResultApi;
import com.study.quizzapp.model.ResultTest;
import com.study.quizzapp.model.Role;
import com.study.quizzapp.model.Test;
import com.study.quizzapp.model.User;
import com.study.quizzapp.retrofit.RetrofitService;
import com.study.quizzapp.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton imageButton;
    private CircleImageView imageView;
    public CircleImageView imageView1;
    private FloatingActionButton floatingActionButton;
    public TextView username;
    private TextView userEmail;
    private TextView username_welcome;
    private boolean isAdmin = false;
    private User user;
    private ArrayList<Test> result = new ArrayList<>();
    private ResultApi restMethod_Result;
    private List<ResultTest> listResultTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        imageButton = findViewById(R.id.userImage2);
        setSupportActionBar(toolbar);
        imageView = findViewById(R.id.card1);
        floatingActionButton = findViewById(R.id.chatHead);

        // init profile of user
        initProfile();

        // fragment for terms and conditions
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment sheetFragment = new BottomSheetDialogFragment();
                sheetFragment.show(getSupportFragmentManager(),sheetFragment.getTag());
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initProfile() {
        user = SharedPrefManager.getInstance(this).getUser();
        NavigationView navigationView =  findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        imageView1 = (navigationView.getHeaderView(0)).findViewById(R.id.imageView);
        username = header.findViewById(R.id.text_user_name);
        userEmail = header.findViewById(R.id.text_email_id);
        isAdmin = user.getRole() == Role.ADMIN;
        setTextOnUser();
        navigationView.setNavigationItemSelectedListener(this);
        username_welcome = findViewById(R.id.text_user_card);
        setWelcomeUser();
        countTest();
    }

    private void countTest() {
        restMethod_Result = RetrofitService.getRetrofit().create(ResultApi.class);
        restMethod_Result.getAllResultByUserId(user.getId()).enqueue(new Callback<List<ResultTest>>() {
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
            }
            @Override
            public void onFailure(@NonNull Call<List<ResultTest>> call, @NonNull Throwable throwable) {
                Log.e("The read failed: ", Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }

    private void setWelcomeUser() {
        String text = "Xin chào " + user.getFname();
        username_welcome.setText(text);
    }

    private void setTextOnUser() {
        username.setText(user.getFname());
        userEmail.setText(user.getEmail());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_test) {
            if (isNetworkAvailable(MainActivity.this)) {
                startActivity(new Intent(MainActivity.this, ViewTestActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            else
                alertNoConnection();
        } else if (id == R.id.nav_result) {
            if ( isNetworkAvailable(MainActivity.this)) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("ISADMIN",isAdmin);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            else
                alertNoConnection();
        } else if (id == R.id.create_test) {
            if (isAdmin && isNetworkAvailable(MainActivity.this)) {
                startActivity(new Intent(MainActivity.this, create_quiz_main.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            else if (isNetworkAvailable(MainActivity.this))
                Toasty.error(getApplicationContext(), "Bạn không phải Admin!", Toasty.LENGTH_SHORT).show();
            else
                alertNoConnection();
        } else if (id == R.id.nav_respass) {
            if (isNetworkAvailable(MainActivity.this)) {
//                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            else
                alertNoConnection();
        } else if (id == R.id.nav_signout) {
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else if (id == R.id.nav_details) {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            intent.putExtra("USERID",user.getId());
            intent.putExtra("USERFNAME", user.getFname());
            intent.putExtra("USERAGE", user.getAge());
            intent.putExtra("USEREMAIL", user.getEmail());
            intent.putExtra("Count", result.size());
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (id == R.id.about_details) {
//            startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (id == R.id.feedback_id) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"Enter your email here"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Your Test or Application Feedback");
            intent.putExtra(Intent.EXTRA_TEXT, "Put your subject here!");
            try {
                startActivity(Intent.createChooser(intent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toasty.error(getApplicationContext(), "There are no email clients installed.", Toasty.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*method to handle network connection**/
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void alertNoConnection() {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.nowifi);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(400, 400));
        builder.show();
    }
}