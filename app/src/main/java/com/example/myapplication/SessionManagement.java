package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String shared_pref_name = "session";
    String session_key = "session_user";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(User user){
        int d = user.getId();
        editor.putInt(session_key,d).commit();
    }

    public int getSession(){
        return sharedPreferences.getInt(session_key,-1);
    }

    public void removeSession(){
        int d = -1;
        editor.putInt(session_key,d).commit();
    }
}
