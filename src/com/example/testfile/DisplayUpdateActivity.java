package com.example.testfile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tejas on 8/25/13.
 */
public class DisplayUpdateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_act);

        Intent iThis = getIntent();
        int l_id = iThis.getIntExtra("webops.LOCAL_ID", -1);
        if(l_id == -1){
            Toast.makeText(DisplayUpdateActivity.this,"Message not found",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        UpdatesDbHandler updatesDbHandler = new UpdatesDbHandler(DisplayUpdateActivity.this);
        Update update = updatesDbHandler.getUpdate(l_id);

        if(update == null){
            Toast.makeText(DisplayUpdateActivity.this,"Message not found",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView title = (TextView)findViewById(R.id.disp_title);
        TextView message = (TextView)findViewById(R.id.disp_update);

        title.setText(update.getTitle());
        message.setText(update.getMessage());

    }


}
