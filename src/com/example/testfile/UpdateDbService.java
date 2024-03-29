package com.example.testfile;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Tejas on 8/24/13.
 */
public class UpdateDbService extends IntentService {
    private static final String TAG = "UpdateDbService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UpdateDbService() {
        super("UpdateDbService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Update Bp and Hr Service Started");

//        while(sDefaultHttpClient==null){
//            sDefaultHttpClient = SessionCookieManager.getClient(
//                    SessionCookieManager.MY_UPDATE_BP_HR_CK,"serv");
//            if(sDefaultHttpClient==null)
//                SystemClock.sleep(10000);
//        }


//        EcgDatabaseHandler ecgDatabaseHandler = new EcgDatabaseHandler(MyUpdateBpHrService.this);

        SharedPreferences mPrefs = getSharedPreferences(
                "webops.SHARED_PREF", 0);
        String lastServerId = mPrefs.getString("com.dhilcare.app.LAST_ID", "0");
        if (updateDb(lastServerId)) {
            sendBroadcast(new Intent(UpdatesListActivity.REFRESH_ACTION));
            generateNotification(UpdateDbService.this,"Updates from webops!");
        }
        ;

    }

    private boolean updateDb(String lastServerId) {
        // Building Parameters
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("id", lastServerId));
        boolean flag = false;
        DefaultHttpClient sDefaultHttpClient = new DefaultHttpClient();

        try {

            String url_update = "http://www.thewebopsclub.org/main/updates/";
            URL url = new URL(url_update + lastServerId);
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 1000);
            HttpConnectionParams.setSoTimeout(httpParameters, 1000);

//            HttpPost httppost = new HttpPost(url.toString());
            HttpGet httppost = new HttpGet(url.toString());
//            httppost.setEntity(new UrlEncodedFormEntity(params));
            httppost.setParams(httpParameters);
            HttpResponse response = sDefaultHttpClient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            InputStream is = null;
            is = httpEntity.getContent();

            String resp = isParse(is);
            Log.i(TAG, resp);
            JSONArray jsonArray = new JSONArray(resp);

            if(addMessages(jsonArray))
                flag = true;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            // makeToastUI("Connection TimeOut");
        }
//        Log.i(Utilities.CONTROL_TAG,"Update Bp and Hr: sucess = 0 exception");
        sendBroadcast(new Intent(UpdatesListActivity.ROTATE_ACTION));
        return flag;
    }

    private boolean addMessages(JSONArray jsonArray) {

        boolean flag = false;
        UpdatesDbHandler updatesDbHandler = new UpdatesDbHandler(UpdateDbService.this);
        SharedPreferences preferences = getSharedPreferences("webops.SHARED_PREF", 0);
        SharedPreferences.Editor mEditor = preferences.edit();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Update update = new Update();
                update.setServerId(jsonArray.getJSONObject(i).getString("id"));
                update.setTitle(jsonArray.getJSONObject(i).getString("title"));
                update.setMessage(jsonArray.getJSONObject(i).getString("message"));
                update.setLocalTime(jsonArray.getJSONObject(i).getString("timestamp"));

                updatesDbHandler.addUpdate(update);

                mEditor.putString("com.dhilcare.app.LAST_ID", update.getServerId()).commit();
                flag = true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * Parses the <code>inputStream</code> into a string
     *
     * @param is
     * @return
     */
    public static String isParse(InputStream is) {
        String s = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                sb.append(line + "\n");

            is.close();

            s = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // return JSON String
        return s;
    }

    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        String currentTime = (c.getTime()).toString();
        return processTimeFormat(currentTime);
    }

    public static void generateNotification(Context context, String message) {
//        Log.i(TAG, "Generating notification + count: " + count);

        int icon = R.drawable.ic_launcher;

        long when = System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(icon, message, when);

        String title = context.getString(R.string.app_name);

        Intent notificationIntent = null;

        notificationIntent = new Intent(context, UpdatesListActivity.class);

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
    }

    public static String processTimeFormat(String input) {
        if (input != null) {
            SimpleDateFormat dateFormat = null;
            Date date = null;

            SimpleDateFormat dateFormatOutput = new SimpleDateFormat(
                    "EEE MMM dd HH:mm");

            try {

//				dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

                date = dateFormat.parse(input);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                try {

                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

                    date = dateFormat.parse(input);
                } catch (ParseException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();

                }
            }
            if (date != null) {
                Log.i("TIME-DATE", date.toString());

                String output = dateFormatOutput.format(date);

                Log.i("TIME-DATE", output);

                // output = output.replace(":", "-");
                return output;
            }
        }

        return null;
    }


}
