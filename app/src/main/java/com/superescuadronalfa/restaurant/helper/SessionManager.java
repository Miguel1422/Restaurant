package com.superescuadronalfa.restaurant.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String API_KEY = "apiKey";
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Shared Preferences
    private SharedPreferences pref;
    private Editor editor;
    private Context _context;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void logout() {
        setLogin(false);
        editor.putString(API_KEY, null);
        editor.commit();
    }

    public void login(String key) {
        setLogin(true);
        editor.putString(API_KEY, key);
        editor.commit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getApiKey() {
        if (!isLoggedIn()) {
            throw new RuntimeException("No te has logueado men");
        }
        String api = pref.getString(API_KEY, null);
        if (api == null) {
            throw new RuntimeException("No existe la api");
        }
        return api;
    }
}