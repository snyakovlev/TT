package ru.television.online;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import android.util.Log;


import java.util.ArrayList;
import java.util.List;


public class TvActivity2 extends FragmentActivity {


    ArrayList<String> cht;
    List<Channel> channels;
    static List<Channel> list;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("atv","tv2_activity_oncreate");
        list=helper2.parseXmlChannel(this,"1");
        setContentView(R.layout.android_tv);






    }







}
