package ru.television.online;

import androidx.leanback.widget.HeaderItem;

import java.util.ArrayList;

public class HeaderItem2 extends HeaderItem {
    int index;
    String name;
  ArrayList<Channel> channels;


    public HeaderItem2(long id, String name) {
        super(id, name);
    }
}
