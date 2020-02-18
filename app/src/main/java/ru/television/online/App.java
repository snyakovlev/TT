package ru.television.online;

import android.app.Application;

import java.util.ArrayList;



public class App extends Application {
    static ArrayList<Channel> list;

    @Override
    public void onCreate() {

        super.onCreate();

    }

    public void initList()
    {
        list=new ArrayList<>();
    }
}
