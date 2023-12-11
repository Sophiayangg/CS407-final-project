package com.cs407.final_project;
// SharedPreferencesHelper.java
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "Email*";
    private static final String KEY_PASSWORD = "Password*";
    private static final String KEY_REMEMBER_ME = "Remember Me";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveCredentials(String username, String password, boolean rememberMe) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    public String getSavedUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public String getSavedPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public boolean getRememberMeStatus() {
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
    }
}

