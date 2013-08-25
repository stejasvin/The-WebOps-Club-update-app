package com.example.testfile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Tejas on 8/24/13.
 */
public class SplashActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_act);

        //put animation here

        IniTask asyncIni = new IniTask(this);
        asyncIni.execute();

    }

    /**
     * Deals with delay and intent
     */
    static class IniTask extends AsyncTask<String, String, String> {

        private static final String TAG = "SplashActivity";
        SplashActivity activity = null;

        void detach() {
            activity = null;
        }

        void attach(SplashActivity activity) {
            this.activity = activity;
        }

        public IniTask(SplashActivity activity) {
            // TODO Auto-generated constructor stub
            attach(activity);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {

//			String regId = args[0];

            //Time to display start screen
            SystemClock.sleep(2000);



//			return regId;
            return null;
        }

        @Override
        protected void onPostExecute(String regId) {
            // TODO Auto-generated method stub
            super.onPostExecute(regId);
            Log.i(TAG, "LoginAct started and startact finished");
            Intent i = new Intent(activity,UpdatesListActivity.class);
//				Intent i = new Intent(activity,com.bluetooth.MainActivity.class);
            activity.startActivity(i);
            activity.finish();
//			}

        }
    }

}
