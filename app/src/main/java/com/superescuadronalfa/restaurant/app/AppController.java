package com.superescuadronalfa.restaurant.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.superescuadronalfa.restaurant.activities.SettingsActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


        // Initialize settings
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String serverName = sharedPref.getString(SettingsActivity.KEY_SERVER_NAME, "local");
        AppConfig.SERVER_ADRESS = serverName;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack() {
                @Override
                protected HttpURLConnection createConnection(URL url) throws IOException {
                    final HttpURLConnection httpURLConnection = super.createConnection(url);
                    // httpURLConnection.setChunkedStreamingMode(0);   // Force no retry for HTTP POST. https://stackoverflow.com/a/31125618/1402846
                    return httpURLConnection;
                }
            });
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        if (AppConfig.USE_CONNECTOR)
            throw new RuntimeException("No se puede usar cuando tienes el conector activado");
        /*
        req.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                */
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {

        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}