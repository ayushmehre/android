package com.retindode.screens;

import android.content.Context;

import java.text.SimpleDateFormat;

public class Data {

    private Context context;
    private static Data data;
//    public static SimpleDateFormat dbDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    public static SimpleDateFormat userDateFormat = new SimpleDateFormat("dd MMM yyyy");
    public static SimpleDateFormat userDateTimeFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a");


    private Data(Context context) {
        this.context = context;
    }

    public static Data getInstance(Context c) {
        if (data == null) {
            data = new Data(c);
        }
        return data;
    }



}
