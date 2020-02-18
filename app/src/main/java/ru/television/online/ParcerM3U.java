package ru.television.online;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParcerM3U {

    ChannelList clist;

    MainActivity ctx;

    AsyncTask bs=null;


    void dnlFromUrl(MainActivity ctx,String url,boolean folder)
    {


        this.ctx=ctx;



     new AsyncTask<String, Integer, ArrayList<Channel>>() {


            @Override
            protected ArrayList<Channel> doInBackground(String... sUrl) {


                InputStream input = null;

                if (!folder) {
                    HttpURLConnection connection = null;

                    try {
                        URL url = new URL(sUrl[0]);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.connect();
                        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            Log.v("wsdf", "no ok!");
                            return null;
                        }

                        int fileLength = connection.getContentLength();

                        try {
                            input = connection.getInputStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.v("wsdf", e.getMessage());
                        }

                    } catch (MalformedURLException e) {

                        e.printStackTrace();
                        Log.v("wsdf", e.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.v("wsdf", e.getMessage());
                    }
                }
                else {
                    try {
                        input = ctx.getContentResolver().openInputStream(Uri.parse(sUrl[0]));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                ArrayList<Channel> cl= null;
                try {
                   if (input!=null) cl = M3UParser.parseFile(input);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                ArrayList<Chans> chanss=null;
                try {
                   chanss= helper2.parseXmlprogtv3(ctx);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (Channel c:cl)
                {
                    String res=c.getName().toLowerCase().replaceAll(" ","").replace("!","")
                            .replace("hd","").trim();


                    for (Chans c2:chanss)
                    {
                        String res2=c2.name.toLowerCase().replaceAll(" ","").replace("!","")
                                .replace("hd","").trim();



                        if (res.equals(res2))
                        {
                            c.setImage(c2.icon);
                            c.setIdd(c2.id);
                            break;
                        }
                    }


                }

                return cl;
            }

                @Override
            protected void onPostExecute(ArrayList<Channel> cl) {
                super.onPostExecute(cl);

                ctx.pb.setVisibility(View.GONE);

                if (cl!=null) {

                    MainActivity.list.clear();

                    MainActivity.cht.clear();

                    MainActivity.list = cl;

                    ctx.saveList(cl);

                    PreferenceManager.getDefaultSharedPreferences(ctx).edit().putBoolean("hom", false).apply();

                    if (cl.size() != 0 && ctx.sav) {
                        ctx.dialogSave();
                        ctx.sav = false;
                    }

                    ((Adaptera) ctx.listch.getAdapter()).channels = cl;

                    ((Adaptera) ctx.listch.getAdapter()).notifyDataSetChanged();

                    ctx.listch.scrollToPosition(PreferenceManager.getDefaultSharedPreferences(ctx).getInt("index", 0));

                    ctx.listch.setEnabled(true);

                }
                else
                {
                    Toast.makeText(ctx,"Ошибка загрузки плэйлиста",Toast.LENGTH_LONG).show();

                    PreferenceManager.getDefaultSharedPreferences(ctx).edit().putBoolean("fold", false).apply();

                    MainActivity.list=helper2.parseXmlChannel(ctx,"1");

                    PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("url", "").apply();

                    new AsyncTask<Void,Void,Void>()
                    {

                        @Override
                        protected Void doInBackground(Void... voids) {

                            if (MainActivity.list==null) return null ;

                            for (Channel c:MainActivity.list)
                            {

                                MainActivity.cht.add( ctx.getCurrTV2(ctx.getBaseContext(),c.getIdd()));

                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            if (ctx.listch!=null &&  ctx.list!=null && ctx.cht!=null && ctx.fav_l!=null)  ctx.listch.setAdapter(new Adaptera(ctx,ctx.list,ctx.cht,ctx.fav_l));

                        }
                    }.execute();

                }

            }
        }.execute(url);
        ctx.listch.setEnabled(false);
    }


}





 class M3UParser {

    private static final String EXT_M3U = "#EXTM3U";
    private static final String EXT_INF = "#EXTINF:";
    private static final String EXT_PLAYLIST_NAME = "#PLAYLIST";
    private static final String EXT_LOGO = "tvg-logo";
    private static final String EXT_URL = "http://";

    public static String convertStreamToString(InputStream is) {
        try {
            return new Scanner(is).useDelimiter("\\A").next();
        } catch (NoSuchElementException e) {
            return "";
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static ArrayList<Channel> parseFile(InputStream inputStream) throws FileNotFoundException {

        ArrayList<Channel> playlistItems = new ArrayList<>();
        String stream = convertStreamToString(inputStream);
        Log.v("qawer",stream);
        String linesArray[] = stream.split(EXT_INF);
        for (int i = 0; i < linesArray.length; i++) {
            String currLine = linesArray[i];
            if (currLine.contains(EXT_M3U)) {
                //header of file
                if (currLine.contains(EXT_PLAYLIST_NAME)) {
                    String fileParams = currLine.substring(EXT_M3U.length(), currLine.indexOf(EXT_PLAYLIST_NAME));
                    String playListName = currLine.substring(currLine.indexOf(EXT_PLAYLIST_NAME) + EXT_PLAYLIST_NAME.length()).replace(":", "");

                } else {

                }
            } else {
                Channel playlistItem = new Channel();
                String[] dataArray = currLine.split(",");
                if (dataArray[0].contains(EXT_LOGO)) {
                    String duration = dataArray[0].substring(0, dataArray[0].indexOf(EXT_LOGO)).replace(":", "").replace("\n", "");
                    String icon = dataArray[0].substring(dataArray[0].indexOf(EXT_LOGO) + EXT_LOGO.length()).replace("=", "").replace("\"", "").replace("\n", "");
                   // playlistItem.setItemDuration(duration);
                  //  playlistItem.setItemIcon(icon);
                } else {
                    String duration = dataArray[0].replace(":", "").replace("\n", "");
                   // playlistItem.setItemDuration(duration);
                  //  playlistItem.setItemIcon("");
                }
                try {
                    String url = dataArray[1].substring(dataArray[1].indexOf(EXT_URL)).replace("\n", "").replace("\r", "");
                    String name = dataArray[1].substring(0, dataArray[1].indexOf(EXT_URL)).replace("\n", "");

                    Log.v("qawer",name+"=="+url);
                    playlistItem.setName(name);
                    playlistItem.setUrlImage(url);
                    playlistItem.setIdd("-1");
                    playlistItem.setId("");
                    playlistItem.setUrlImage2("");
                    playlistItem.setUrlImage3("");
                } catch (Exception fdfd) {
                    Log.e("Google", "Error: " + fdfd.fillInStackTrace());
                }
                if (playlistItem!=null && playlistItem.getName()!=null && playlistItem.getName().length()==0)
                {
                    playlistItem.setName("Без названия");
                }
                if (playlistItem!=null && playlistItem.getFile()!=null && playlistItem.getFile().length()>5) {
                    playlistItems.add(playlistItem);
                }
            }
        }

        return playlistItems;
    }
}




 class ChannelList implements Serializable {
    public String name;
    public List<Channel> items;
    public List<String> groups;

    public ChannelList() {
        items = new ArrayList<Channel>();
    }

    public void add(Channel item) {
        items.add(item);
    }
}

