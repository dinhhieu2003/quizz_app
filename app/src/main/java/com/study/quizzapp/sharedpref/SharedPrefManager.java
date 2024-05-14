package com.study.quizzapp.sharedpref;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.study.quizzapp.activity.auth.LoginActivity;
import com.study.quizzapp.model.Role;
import com.study.quizzapp.model.User;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "retrofitapi";
    // User
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_AGE = "keyage";
    private static final String KEY_ID = "keyid";
    private static final String KEY_FNAME = "keyfname";
    private static final String KEY_ROLE = "keyrole";
    // Product
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_ID, user.getId());
        editor.putInt(KEY_AGE, user.getAge());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_FNAME, user.getFname());
        editor.putString(KEY_ROLE, user.getRole().toString());
        editor.apply();
    }

    public void getProduct() {

    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Role role = Role.valueOf(sharedPreferences.getString(KEY_ROLE, null));
        return new User(
                sharedPreferences.getLong(KEY_ID, -1),
                sharedPreferences.getString(KEY_FNAME, null),
                sharedPreferences.getInt(KEY_AGE, -1),
                sharedPreferences.getString(KEY_EMAIL, null),
                "",
                role
        );
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
