package com.example.testfile;

import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tejas on 8/24/13.
 */
public class UpdatesListActivity extends ListActivity {

    private static final String TAG = "UpdatesListActivity";
    public static final String REFRESH_ACTION = "REFRESH_UPDATES";
    private BroadcastReceiver refreshReceiver;
    ArrayAdapter<Update> adapter;
    ArrayList<Update> mapList;

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
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(refreshReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_act);

        Intent iService = new Intent(this,UpdateDbService.class);
        startService(iService);

        UpdatesDbHandler updatesDbHandler = new UpdatesDbHandler(UpdatesListActivity.this);
        mapList =  updatesDbHandler.getAllUpdates();

        //update the ListView in Asynctask
        adapter = new ViewUpdatesListAdapter(this,
                R.layout.single_list_item_view_updates, mapList);
        setListAdapter(adapter);

        Button refresh = (Button)findViewById(R.id.update_b_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(UpdatesListActivity.this,UpdateDbService.class));
            }
        });
        ListView list = getListView();
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

    }
}
