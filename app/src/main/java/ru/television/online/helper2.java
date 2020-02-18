package ru.television.online;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import androidx.annotation.NonNull;
import android.util.Log;
import android.util.Xml;



import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class helper2 {

    static String er="";

 static public void dloadPlayList2(final ActivitySplash ctx, String url,final Bundle sis) {




       new AsyncTask<String, Integer, String>() {

           private PowerManager.WakeLock mWakeLock;


           @Override
           protected String doInBackground(String... sUrl) {
               er="";
               InputStream input = null;
               OutputStream output = null;
               HttpURLConnection connection = null;


               File file = null;
               File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "tt");
               dir.mkdirs();
               try {
                   dir.createNewFile();
                   file = new File(dir, "playlist.xml");
                   file.createNewFile();
               } catch (IOException e) {
                   e.printStackTrace();
                   er=e.getMessage();
               }
               Log.v("helperrr", file.getAbsolutePath());

               String filename = file.getAbsolutePath();

               try {
                   URL url = new URL(sUrl[0]);
                   connection = (HttpURLConnection) url.openConnection();
                   connection.connect();


                   if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                       return er= "Server returned HTTP " + connection.getResponseCode()
                               + " " + connection.getResponseMessage();
                   }

                   // this will be useful to display download percentage
                   // might be -1: server did not report the length
                   int fileLength = connection.getContentLength();

                   // download the file
                   input = connection.getInputStream();
                   output = new FileOutputStream(filename);

                   byte data[] = new byte[4096];
                   long total = 0;
                   int count;
                   while ((count = input.read(data)) != -1) {
                       // allow canceling with back button
                       if (isCancelled()) {
                           input.close();
                           return null;
                       }
                       total += count;
                       // publishing the progress....
                       if (fileLength > 0) // only if total length is known
                           publishProgress((int) (total * 100 / fileLength));
                       output.write(data, 0, count);
                   }
               } catch (Exception e) {
                   Log.v("helperrr", e.toString());
                   er=e.getMessage();
                   return e.toString();
               } finally {
                   try {
                       if (output != null)
                           output.close();
                       if (input != null)
                           input.close();
                   } catch (IOException ignored) {
                   }

                   if (connection != null)
                       connection.disconnect();
               }


               File ffile = null;

               File f = ctx.getFilesDir();

               f.mkdirs();

               try {
                   f.createNewFile();
                   ffile = new File(f, "playlist.xml");
                   ffile.createNewFile();
               } catch (IOException e) {
                   e.printStackTrace();
                   er=e.getMessage();
               }

               try {
                   copy(file, ffile);
               } catch (IOException e) {
                   e.printStackTrace();
                   er=e.getMessage();
               }



               return null;
           }

           @Override
           protected void onPostExecute(String s) {
               super.onPostExecute(s);
              ctx.x1=true;
              ctx.go();
               Log.v("qawe","post");


           }
       }.execute(url);
   }


    static public void dloadPlayList(final TvActivity ctx, String url,final Bundle sis) {




        new AsyncTask<String, Integer, String>() {

            private PowerManager.WakeLock mWakeLock;


            @Override
            protected String doInBackground(String... sUrl) {
                er="";
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;


                File file = null;
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "tt");
                dir.mkdirs();
                try {
                    dir.createNewFile();
                    file = new File(dir, "playlist.xml");
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    er=e.getMessage();
                }
                Log.v("helperrr", file.getAbsolutePath());

                String filename = file.getAbsolutePath();

                try {
                    URL url = new URL(sUrl[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();


                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return er= "Server returned HTTP " + connection.getResponseCode()
                                + " " + connection.getResponseMessage();
                    }

                    // this will be useful to display download percentage
                    // might be -1: server did not report the length
                    int fileLength = connection.getContentLength();

                    // download the file
                    input = connection.getInputStream();
                    output = new FileOutputStream(filename);

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        // allow canceling with back button
                        if (isCancelled()) {
                            input.close();
                            return null;
                        }
                        total += count;
                        // publishing the progress....
                        if (fileLength > 0) // only if total length is known
                            publishProgress((int) (total * 100 / fileLength));
                        output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    Log.v("helperrr", e.toString());
                    er=e.getMessage();
                    return e.toString();
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }

                    if (connection != null)
                        connection.disconnect();
                }


                File ffile = null;

                File f = ctx.getFilesDir();

                f.mkdirs();

                try {
                    f.createNewFile();
                    ffile = new File(f, "playlist.xml");
                    ffile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    er=e.getMessage();
                }

                try {
                    copy(file, ffile);
                } catch (IOException e) {
                    e.printStackTrace();
                    er=e.getMessage();
                }



                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                ctx.x1=true;
                Log.v("uiol","post "+er);
                ctx.go();



            }
        }.execute(url);
    }



    public static void copy(File src, File dst) throws IOException {

        Log.v("helperrr","copy");

        Log.v("helperrr" , src.length()+"");

        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

                Log.v("helperr",e.toString());
            }

            finally {
                out.close();
            }
        } finally {
            in.close();
        }



        Log.v("helperrr" , dst.length()+"");
    }

    @NonNull
   static   XmlPullParser createXmlPullParserChannel(Context ctx) throws IOException, XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();


        File f=new File(ctx.getFilesDir()+File.separator+"playlist.xml");
        InputStream inputStream = new FileInputStream(f);
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        return parser;
    }

    @NonNull
    static XmlPullParser createXmlPullParserProgtv(Context ctx) throws IOException, XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();

        // получаем доступ к xml файлу
        File f=new File(ctx.getFilesDir()+File.separator+"xmltv.xml");
        InputStream inputStream = new FileInputStream(f);
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        return parser;
    }

    @NonNull
    static XmlPullParser createXmlPullParserProgtv2(Context ctx) throws IOException, XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();

        // получаем доступ к xml файлу
        File f=new File(ctx.getFilesDir()+File.separator+"xmltv2.xml");
        InputStream inputStream = new FileInputStream(f);
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        return parser;
    }

    static public Group getFavorite(Context ctx)
    {
        Group fav=new Group();

        DBI dbi=new DBI(ctx);

        dbi.getWritableDatabase();

        List<Channel> lf=dbi.qDB();

        if (lf.size()==0)

       {
           fav=null;
       }
       else
       {
             fav.name="Мои каналы";
             fav.id="0";
       }
        return fav;
    }

    static  public List<Group> parseXMLGroup(Context ctx)
    {



        XmlPullParser parser= null;
        try {
            parser = createXmlPullParserChannel(ctx);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        if (parser==null) return null ;

        Log.v("par1000","parser "+parser);

        List<Group> groups = new ArrayList<>();





        int eventType = 0;
        try {
            eventType = parser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        Group group = null;


        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = "";


            if (eventType == XmlPullParser.START_DOCUMENT) {

                groups= new ArrayList<>();
               // if (getFavorite(ctx)!=null)
               // {
               //     Log.v("qwed",getFavorite(ctx).name);
               //     groups.add(getFavorite(ctx));
              //  }

            }

            else if (eventType == XmlPullParser.START_TAG) {
                Log.v("par1000","start_tag "+parser.getName());

                name = parser.getName();
                if (name.equals("group")) {
                    group = new Group();
                    String id = parser.getAttributeValue(null, "id");;
                    String attrname=parser.getAttributeValue(null, "name");


                    group.id=id;
                   group.name=attrname;


                }



            }

            else if (eventType == XmlPullParser.END_TAG) {
                name = parser.getName();

                if (name!=null && name.equalsIgnoreCase("group") && group != null) {
                    Log.v("qwed",group.name);
                    groups.add(group);//добавляем группу
                }
            }


            try {
                eventType = parser.next();
            } catch (IOException e) {

                e.printStackTrace();
                Log.v("par1000","error "+e.getMessage());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Log.v("par1000","error "+e.getMessage());
            }

        }

        Log.v("qwed",groups.size()+"");
        return groups;

    }


  static   public ArrayList<Channel> parseXmlChannel(Context ctx,String gr) {

      Log.v("par1000","parseXmlChannel");

      String on="0";

      XmlPullParser parser= null;
      try {
          parser = createXmlPullParserChannel(ctx);
      } catch (IOException e) {
          e.printStackTrace();
      } catch (XmlPullParserException e) {
          e.printStackTrace();
      }

      Log.v("par1000","parser "+parser);

        ArrayList<Channel> channels = new ArrayList<>();
        String group="";

      int eventType = 0;
      try {
          eventType = parser.getEventType();
      } catch (XmlPullParserException e) {
          e.printStackTrace();
      }
      Channel channel = null;

      try {
          Log.v("par1000","event "+parser.getEventType());
      } catch (XmlPullParserException e) {
          e.printStackTrace();
      }

      while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = "";
            Log.v("par1000","в цикле "+eventType);

            if (eventType == XmlPullParser.START_DOCUMENT) {
                try {
                    Log.v("par1000","start document "+parser.getEventType());
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                channels = new ArrayList<>();


            }

            else if (eventType == XmlPullParser.START_TAG) {
                Log.v("par1000","start_tag "+parser.getName());

                name = parser.getName();
                if (name.equals("group")) group=parser.getAttributeValue(null, "id");
                if (name.equals("channel")) {
                    channel = new Channel();
                    String id = parser.getAttributeValue(null, "logo");
                    String idd = parser.getAttributeValue(null, "idd");
                    String attrname=parser.getAttributeValue(null, "name");
                    String file1=parser.getAttributeValue(null, "file");
                    String file2=parser.getAttributeValue(null, "file2");
                    String file3=parser.getAttributeValue(null, "file3");
                    String select=parser.getAttributeValue(null, "select");
                    on=parser.getAttributeValue(null, "on");
                    if (on==null)
                    {
                        on="1";
                    }
                    channel.setGroup(group);
                    channel.setId(id);
                    channel.setName(attrname);
                    channel.setIdd(idd);
                    channel.setSelect(select);
                    if (select==null) channel.setUrlImage(file1);
                    channel.setUrlImage(file1);
                    channel.setUrlImage2(file2);
                    channel.setUrlImage3(file3);

                }



            }

            else if (eventType == XmlPullParser.END_TAG) {
                name = parser.getName();

                if (name!=null && name.equalsIgnoreCase("channel") && channel != null && group.equals(gr) && on.equals("1")  ) {
                    Log.v("par1001",parser.getName());
                    channels.add(channel);
                }
            }


          try {
              eventType = parser.next();
          } catch (IOException e) {

              e.printStackTrace();
              Log.v("par1000","error "+e.getMessage());
          } catch (XmlPullParserException e) {
              e.printStackTrace();
              Log.v("par1000","error "+e.getMessage());
          }
          Log.v("par1000","конец "+eventType);
        }

      DBI dbi = new DBI(ctx);

      dbi.getWritableDatabase();

      for (Channel ch0:channels) {

          for (Channel ch : dbi.qDB()) {

              if (ch.getName().equals(ch0.getName()))
              {
                  dbi.uDb(ch0.getName(),ch0.getIdd(),ch0.getFile(),ch0.getFile2(),ch0.getFile3(),ch0.getId(),ch0.getSelect(),ch0.getImage());
              }
          }
      }



        return channels;

    }

  static   public List<ItemProgram> parseXmlprogtv(Context ctx,String id_cahnnel) throws XmlPullParserException, IOException {


    XmlPullParser parser= createXmlPullParserProgtv(ctx);
        List<ItemProgram> ips=new ArrayList<>();

        ItemProgram ip=null;
        boolean title=false;
        boolean desc=false;



        int eventType = parser.getEventType();

         String id="";

        while (eventType != XmlPullParser.END_DOCUMENT) {




            if (eventType == XmlPullParser.START_DOCUMENT) {

            }

            else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("programme")) {


                id=parser.getAttributeValue(null,"channel");

                if (id.equals(id_cahnnel)) {
                    Log.v("ressss","programme start "+id);
                    ip=new ItemProgram();



                        ip.start_time = time_string(parser.getAttributeValue(null, "start"));
                        ip.stop_time = time_string(parser.getAttributeValue(null, "stop"));
                        ip.start_millisec=time_mill(parser.getAttributeValue(null, "start"));
                        ip.stop_millisec=time_mill(parser.getAttributeValue(null, "stop"));
                        ip.desc=parser.getAttributeValue(null,"desc");


                }

            }
            else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("title") && id.equals(id_cahnnel))
            {
                Log.v("ressss","title start ");
                title=true;
            }

            else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("desc") && id.equals(id_cahnnel))
            {
                Log.v("ressss","title end ");
                desc=false;
            }

            else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("desc") && id.equals(id_cahnnel))
            {
                Log.v("ressss","title start ");
                desc=true;
            }

            else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("title") && id.equals(id_cahnnel))
            {
                Log.v("ressss","title end ");
                title=false;
            }

            else if (eventType==XmlPullParser.TEXT && title && id.equals(id_cahnnel))
            {
                Log.v("ressss","text ");
                    ip.text = parser.getText();
                    Log.v("ressss","text "+ parser.getText());

            }

            else if (eventType==XmlPullParser.TEXT && desc && id.equals(id_cahnnel))
            {
                Log.v("ressss","text ");
                ip.desc = parser.getText();
                Log.v("ressss","text "+ parser.getText());

            }

            else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("programme") && id.equals(id_cahnnel))
            {
                Log.v("ressss","progamme end ");
                ips.add(ip);
                id="";
            }

            eventType = parser.next();
        }



        return  ips;


    }


    static   public ArrayList<Chans> parseXmlprogtv3(Context ctx) throws XmlPullParserException, IOException {

        Log.v("iop","parser_start");
        XmlPullParser parser= createXmlPullParserProgtv(ctx);
        ArrayList<Chans> ips=new ArrayList<>();

        boolean dn=false;
       boolean ic=false;
        Chans ch=null;

        int eventType = parser.getEventType();

        String id="";

        while (eventType != XmlPullParser.END_DOCUMENT) {


            if (eventType == XmlPullParser.START_DOCUMENT) {
                Log.v("iop","start_document");

            }

            else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("programme"))
            {
                break;
            }
             else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("channel")) {
                Log.v("iop", "channel");

               ch  = new Chans();
               ch.id=parser.getAttributeValue(0);
            }
             else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("icon")) {
                    ic=true;
                  ch.icon=parser.getAttributeValue(0);
                    Log.v("iop","channel_icon="+ch.icon);
                }
                else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("icon")) {
                    ic=false;
                }

                else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("display-name")) {

                    dn=true;
                }

                else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("display-name")) {
                    dn=false;
                }

                else if (eventType==XmlPullParser.TEXT && dn)
                {

                    ch.name = parser.getText();
                    Log.v("iop","channel dis_n"+ch.name);
                }


                else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("channel")) {

                    Log.v("iop","ch="+ch.name);
                    ips.add(ch);

                }
            eventType = parser.next();

        }


        Log.v("iop","ips_size="+ips.size());
        return  ips;


    }



    static   public List<ItemProgram> parseXmlprogtv2(Context ctx,String id_cahnnel) throws XmlPullParserException, IOException {


        XmlPullParser parser= createXmlPullParserProgtv2(ctx);
        List<ItemProgram> ips=new ArrayList<>();

        ItemProgram ip=null;
        boolean title=false;
        boolean desc=false;



        int eventType = parser.getEventType();

        String id="";

        while (eventType != XmlPullParser.END_DOCUMENT) {




            if (eventType == XmlPullParser.START_DOCUMENT) {

            }

            else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("programme")) {


                id=parser.getAttributeValue(null,"channel");

                if (id.equals(id_cahnnel)) {
                    Log.v("ressss","programme start "+id);
                    ip=new ItemProgram();



                    ip.start_time = time_string(parser.getAttributeValue(null, "start"));
                    ip.stop_time = time_string(parser.getAttributeValue(null, "stop"));
                    ip.start_millisec=time_mill(parser.getAttributeValue(null, "start"));
                    ip.stop_millisec=time_mill(parser.getAttributeValue(null, "stop"));
                    ip.desc=parser.getAttributeValue(null,"desc");


                }

            }
            else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("title") && id.equals(id_cahnnel))
            {
                Log.v("ressss","title start ");
                title=true;
            }

            else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("desc") && id.equals(id_cahnnel))
            {
                Log.v("ressss","title end ");
                desc=false;
            }

            else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("desc") && id.equals(id_cahnnel))
            {
                Log.v("ressss","title start ");
                desc=true;
            }

            else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("title") && id.equals(id_cahnnel))
            {
                Log.v("ressss","title end ");
                title=false;
            }

            else if (eventType==XmlPullParser.TEXT && title && id.equals(id_cahnnel))
            {
                Log.v("ressss","text ");
                ip.text = parser.getText();
                Log.v("ressss","text "+ parser.getText());

            }

            else if (eventType==XmlPullParser.TEXT && desc && id.equals(id_cahnnel))
            {
                Log.v("ressss","text ");
                ip.desc = parser.getText();
                Log.v("ressss","text "+ parser.getText());

            }

            else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("programme") && id.equals(id_cahnnel))
            {
                Log.v("ressss","progamme end ");
                ips.add(ip);
                id="";
            }

            eventType = parser.next();
        }



        return  ips;


    }

    static   public List<ItemProgram> parseXmlprogtvv(Context ctx,ArrayList<String> id_cahnnel) throws XmlPullParserException, IOException {


        XmlPullParser parser= createXmlPullParserProgtv(ctx);
        List<ItemProgram> ips=new ArrayList<>();

        ItemProgram ip=null;
        boolean title=false;
        boolean desc=false;



        int eventType = parser.getEventType();

        String id="";

        while (eventType != XmlPullParser.END_DOCUMENT) {




            if (eventType == XmlPullParser.START_DOCUMENT) {

            }

            else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("programme")) {


                id=parser.getAttributeValue(null,"channel");

                if (id.equals(id_cahnnel)) {
                    Log.v("ressss","programme start "+id);
                    ip=new ItemProgram();



                    ip.start_time = time_string(parser.getAttributeValue(null, "start"));
                    ip.stop_time = time_string(parser.getAttributeValue(null, "stop"));
                    ip.start_millisec=time_mill(parser.getAttributeValue(null, "start"));
                    ip.stop_millisec=time_mill(parser.getAttributeValue(null, "stop"));
                    ip.desc=parser.getAttributeValue(null,"desc");


                }

            }
            else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("title") && id.equals(id_cahnnel))
            {
                Log.v("ressss","title start ");
                title=true;
            }

            else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("desc") && id.equals(id_cahnnel))
            {
                Log.v("ressss","title end ");
                desc=false;
            }

            else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("desc") && id.equals(id_cahnnel))
            {
                Log.v("ressss","title start ");
                desc=true;
            }

            else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("title") && id.equals(id_cahnnel))
            {
                Log.v("ressss","title end ");
                title=false;
            }

            else if (eventType==XmlPullParser.TEXT && title && id.equals(id_cahnnel))
            {
                Log.v("ressss","text ");
                ip.text = parser.getText();
                Log.v("ressss","text "+ parser.getText());

            }

            else if (eventType==XmlPullParser.TEXT && desc && id.equals(id_cahnnel))
            {
                Log.v("ressss","text ");
                ip.desc = parser.getText();
                Log.v("ressss","text "+ parser.getText());

            }

            else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("programme") && id.equals(id_cahnnel))
            {
                Log.v("ressss","progamme end ");
                ips.add(ip);
                id="";
            }

            eventType = parser.next();
        }



        return  ips;


    }


    static long time_mill(String t)
    {
        long res=0;

        int year=Integer.parseInt(t.substring(0,4));

        int mes=Integer.parseInt(t.substring(4,6));

        int day=Integer.parseInt(t.substring(6,8));

        int hour=Integer.parseInt(t.substring(8,10));

        int min=Integer.parseInt(t.substring(10,12));

        int sec=Integer.parseInt(t.substring(12,14));

        Calendar cal=Calendar.getInstance();

        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,mes-1);
        cal.set(Calendar.DAY_OF_MONTH,day);
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,min);
        cal.set(Calendar.SECOND,sec);

        res=cal.getTimeInMillis();

        return res;
    }

    static String time_string(String t)
    {
        String res="";

        String hour=t.substring(8,10);

        String min=t.substring(10,12);

        res=hour+":"+min;

        return res;
    }



  static   public String parseIcon(Context ctx,String id_cahnnel) throws XmlPullParserException, IOException {

        XmlPullParser parser=createXmlPullParserProgtv(ctx);

        int eventType = parser.getEventType();
        Channel channel = null;


        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = "";
            String id = "";


            if (eventType == XmlPullParser.START_DOCUMENT) {

            } else if (eventType == XmlPullParser.START_TAG) {
                name = parser.getName();
                id = parser.getAttributeValue(null, "channel");

                if (name.equals("channel") && parser.getAttributeValue(null, "id").equals(id_cahnnel)) {
                    parser.next();
                    parser.next();

                    return parser.getAttributeValue(null, "src");

                }
            }
            eventType = parser.next();
        }

        return null;
    }



}

class Group
{
    String name;
    String id;
}
 class Chans
 {
     String id;
     String name;
     String icon;
 }