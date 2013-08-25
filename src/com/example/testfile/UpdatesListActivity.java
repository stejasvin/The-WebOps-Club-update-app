package com.example.testfile;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.TimerTask;

/**
 * Created by Tejas on 8/24/13.
 */
public class UpdatesListActivity extends ListActivity implements GestureOverlayView.OnGesturePerformedListener{

    private static final String TAG = "UpdatesListActivity";
    public static final String REFRESH_ACTION = "REFRESH_UPDATES";
    public static final String ROTATE_ACTION = "ROTATE_BUTTON";
    private BroadcastReceiver refreshReceiver;
    ArrayAdapter<Update> adapter;
    ArrayList<Update> mapList;

    ListView list;

    private GestureLibrary gestureLib;

    View selectedView = null;
    int selectedId = -1;
    private BroadcastReceiver rotateReceiver;

    @Override
    protected void onStart() {
        super.onStart();

        refreshReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
//                Bundle extras = intent.getExtras();

                Log.i(TAG, "refreshReceiver triggered");
//                refreshComments();
//                Intent i = getIntent();
//                finish();
//                startActivity(i);

                UpdatesDbHandler updatesDbHandler = new UpdatesDbHandler(UpdatesListActivity.this);
                mapList = updatesDbHandler.getAllUpdates();
                adapter.clear();
                for(Update u : mapList)
                    adapter.add(u);

            }
        };
        registerReceiver(refreshReceiver, new IntentFilter(REFRESH_ACTION));

        rotateReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
//                Bundle extras = intent.getExtras();

                Log.i(TAG, "rotateReceiver triggered");
                findViewById(R.id.update_b_refresh).clearAnimation();

            }
        };
        registerReceiver(rotateReceiver, new IntentFilter(ROTATE_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(refreshReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.list_act);

        GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
        //GestureOverlayView gestureOverlayView = (GestureOverlayView)findViewById((R.id.gestureOverlayView));
        View inflate;

        inflate = getLayoutInflater().inflate(R.layout.list_act, null);
        gestureOverlayView.setGestureColor(Color.TRANSPARENT);
        gestureOverlayView.setGestureVisible(false);
        gestureOverlayView.addView(inflate);
        gestureOverlayView.addOnGesturePerformedListener(this);
        gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gestureLib.load()) {
            finish();
        }
        setContentView(gestureOverlayView);



        UpdatesDbHandler updatesDbHandler = new UpdatesDbHandler(UpdatesListActivity.this);
        mapList =  updatesDbHandler.getAllUpdates();

        //update the ListView in Asynctask
        adapter = new ViewUpdatesListAdapter(this,
                R.layout.single_list_item_view_updates, mapList);
        setListAdapter(adapter);

        final Button refresh = (Button)findViewById(R.id.update_b_refresh);

        //refresh list
        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        refresh.startAnimation(animFadein);
        Intent iService = new Intent(this,UpdateDbService.class);
        startService(iService);
        setRecurringAlarm(this);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refresh list
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.rotate);
                refresh.startAnimation(animFadein);
                startService(new Intent(UpdatesListActivity.this,UpdateDbService.class));
            }
        });
        list = getListView();
//        list.setSelector(R.drawable.list_selector);



        list.setScrollbarFadingEnabled(false);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Update update = mapList.get(position);
                Intent intent = new Intent(UpdatesListActivity.this,DisplayUpdateActivity.class);
                intent.putExtra("webops.LOCAL_ID",update.getLocalId());
                startActivity(intent);

            }
        });



        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.jitter);
                view.startAnimation(animFadein);
//                Thread tSleep = new Thread(new TimerTask() {
//                    @Override
//                    public void run() {
//                        SystemClock.sleep(1000);
//                        view1.clearAnimation();
//
//                    }
//                });

                selectedView = view;
                selectedId = mapList.get(position).getLocalId();
                return true;
            }
        });



//                new AdapterView.OnItemLongClickListener() {
//            @Override
//            public void onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
//                        R.anim.sequential2);
//
////        refresh.setAnimation(animFadein);
//                view.startAnimation(animFadein);
//            }
//        });




    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
        for (Prediction prediction : predictions) {
            if (prediction.score > 1.0) {
                //makeToast("Listener");
                //Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();

                //Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();

//                if(prediction.name.equalsIgnoreCase("left")){
//
//                }
//                if(prediction.name.equalsIgnoreCase("right")){
                    if(selectedView!=null&&
                            selectedId!=-1){
                        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.sequential2);
                        animFadein.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.sequential3);
                                list.startAnimation(animFadein);

                                UpdatesDbHandler updatesDbHandler = new UpdatesDbHandler(UpdatesListActivity.this);
                                updatesDbHandler.deleteUpdate(selectedId+"");

//                                try{
//                                    list.removeViewInLayout(selectedView);
//                                }catch (Exception e){
//                                    e.printStackTrace();
//                                }

                                selectedView.setVisibility(View.VISIBLE);
                                refreshList();

                                selectedView = null;
                                selectedId = -1;

                                Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.fade_in);
                                list.startAnimation(anim2);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        selectedView.startAnimation(animFadein);

                    }
//                }
            }
        }
    }

    private void refreshList() {

//        UpdatesDbHandler updatesDbHandler = new UpdatesDbHandler(UpdatesListActivity.this);
//
//        mapList = updatesDbHandler.getAllUpdates();
////        list.removeAllViewsInLayout();
//
//        adapter.clear();
//        for(Update u : mapList)
//            adapter.add(u);
        finish();
        startActivity(getIntent());
    }


    private void setRecurringAlarm(Context context) {
        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
        updateTime.set(Calendar.HOUR_OF_DAY, 11);
        updateTime.set(Calendar.MINUTE, 45);
        Intent downloader = new Intent(context, AlarmReceiver.class);
        PendingIntent recurringDownload = PendingIntent.getBroadcast(context,
                0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) UpdatesListActivity.this.getSystemService(
                Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, recurringDownload);
    }


}
