package com.example.testfile;

/**
 * Created by Tejas on 8/24/13.
 */
public class Update {

    String message,title,s_id, l_timestamp;
    int l_id;

    public Update(){
        this.message = "N/A";
        this.title   = "N/A";
        this.l_id      = -1;
        this.s_id      = "-1";
        this.l_timestamp      = "-1";
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }


    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getLocalId(){
        return this.l_id;
    }

    public void setLocalId(int l_id){
        this.l_id = l_id;
    }

    public String getServerId(){
        return this.s_id;
    }

    public void setServerId(String s_id){
        this.s_id = s_id;
    }

    public String getLocalTime(){
        return this.l_timestamp;
    }

    public void setLocalTime(String l_timestamp){
        this.l_timestamp = l_timestamp;
    }




}
