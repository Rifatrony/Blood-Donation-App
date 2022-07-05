package com.rony.blooddonationapp.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY_1 = "user_password";
    String SESSION_KEY_2 = "email";

    String accessToken;
    String userEmail;
    String password;




    public SessionManagement() {
    }

    @SuppressLint("CommitPrefEdits")
    public SessionManagement(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void setLoginSession(SessionModel model) {

        this.userEmail = model.getUserEmail();
        this.password = model.getPassword();

        editor.putString(SESSION_KEY_1, password).commit();
        editor.putString(SESSION_KEY_2, userEmail).commit();

    }

    public SessionModel getSessionModel() {

        password = sharedPreferences.getString(SESSION_KEY_1, "null");
        userEmail = sharedPreferences.getString(SESSION_KEY_2, "null");

        return new SessionModel(userEmail, password);

    }

    public void removeLoginSession() {

        editor.putString(SESSION_KEY_1, "null").commit();
        editor.putString(SESSION_KEY_2, "null").commit();

    }

}
