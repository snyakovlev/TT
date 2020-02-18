package ru.television.online;

import java.util.ArrayList;

public class ListChannel {

    ArrayList<Channel> channel;

    private static  ListChannel ourInstance;

    public static void initInstance()
    {
        ourInstance = new ListChannel();
    }

    public static ListChannel getInstance() {
        return ourInstance;
    }

    private ListChannel() {
    }


    public void setListChannel(ArrayList<Channel> ch)
    {
        channel=ch;
    }

    public ArrayList<Channel> getListChannel()
    {
        return channel;
    }
}
