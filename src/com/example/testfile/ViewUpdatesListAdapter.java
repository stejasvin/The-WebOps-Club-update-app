package com.example.testfile;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class is a customized array adapter used to display the List of
 * messages and their details in a list.
 *
 * @author stejasvin
 * @since v1.0
 */

public class ViewUpdatesListAdapter extends ArrayAdapter {

    private static final String TAG_FROM_DOC_NAME = "from_doctor_name";

    ArrayList<Update> mapList;
    /**
     * Context
     */
    private Context context;

    public ViewUpdatesListAdapter(Context context, int textViewResourceId, ArrayList<Update> mapList) {
        super(context, textViewResourceId, mapList);
        this.context = context;
        this.mapList = mapList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.single_list_item_view_updates, parent, false); // inflate view from xml file
        }

        TextView tvTitle = (TextView) row.findViewById(R.id.list_title);
        TextView tvMessage = (TextView) row.findViewById(R.id.list_message);
        TextView tvTime = (TextView) row.findViewById(R.id.list_time);
        LinearLayout tvLayout = (LinearLayout) row.findViewById(R.id.list_layout);


//        String sName = null;
//        if(mapList.get(position).get("name1")!="null"){
//            sName=mapList.get(position).get("name1");
//        }
//        if(mapList.get(position).get("name2")!="null"){
//            sName+=","+mapList.get(position).get("name2");
//        }
//        if(mapList.get(position).get("name3")!="null"){
//            sName+=","+mapList.get(position).get("name3")+"..";
//        }
//        if(sName!=null)
//        name.setText(sName);

        String message = mapList.get(position).getMessage();
        String title = mapList.get(position).getTitle();
//        int localId = mapList.get(position).getLocalId();
//        String serverId = mapList.get(position).getServerId();
        String localTime = mapList.get(position).getLocalTime();

        tvTitle.setText(title);
        tvTime.setText(localTime);
        tvMessage.setText(message);

//        if (messageStatus != null &&
//                messageStatus.equals("old") ||
//                fromDocId.equals("1")
//                ) {
//
//            message.setTextColor(Color.BLACK);
//            name.setTextColor(Color.BLACK);
//            ecgid.setTextColor(Color.BLACK);
//            layout.setBackgroundColor(Color.GRAY);
//        } else {
//
//            layout.setBackgroundColor(Color.WHITE);
//            message.setTextColor(Color.BLACK);
//            name.setTextColor(Color.BLACK);
//            ecgid.setTextColor(Color.BLACK);
//        }
        //TODO uncomment this after php script is finished
        //time.setText(mapList.get(position).get("time"));
//        time.setText("10:10 12/12/12");

        return row;
    }

}