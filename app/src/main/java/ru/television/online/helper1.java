package ru.television.online;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import 	java.util.zip.GZIPInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import androidx.annotation.NonNull;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;



import org.apache.commons.net.ftp.FTPClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import static java.lang.Thread.sleep;

public class helper1 {

   static String error;

    static void createCompFile(Activity ctx,Complaint ch)
    {

        XmlSerializer s= Xml.newSerializer();
        FileWriter writer= null;

        try {
            File fil = new File(ctx.getFilesDir() + File.separator + "complaint.txt");
            if (!fil.exists()) fil.createNewFile();
            writer = new FileWriter(fil);
            s.setOutput(writer);
            s.startDocument("UTF-8", true);




                s.startTag("", "complaint");
                s.attribute("", "id", ch.id);
                s.attribute("", "text", ch.text);
                s.attribute("", "status", ch.status);
            s.attribute("", "player", ch.player);
            s.attribute("", "su", ch.su);
                s.endTag("","complaint");





            s.endDocument();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);

        }
    }


    public static void ftpDlgPlaylist(final Activity ctx, final String hostAddress, final String log, final String password, final Bundle sis) throws FileNotFoundException {

        error="";

        new AsyncTask<Void,Void,Void>()
        {


            @Override
            protected Void doInBackground(Void... voids) {
                Log.v("qqwwer","doin");
                String remoteFile1 = "/pl/playlist.xml";
                FTPClient fClient = new FTPClient();
                OutputStream outputStream=null;
                try {
                    outputStream = new BufferedOutputStream(new FileOutputStream(ctx.getFilesDir() + File.separator + "playlist.xml"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    error=e.getMessage();
                }
                try {
                    fClient.connect(hostAddress);
                    fClient.enterLocalPassiveMode();
                    fClient.login(log, password);
                    fClient.retrieveFile(remoteFile1, outputStream);
                    fClient.logout();
                    fClient.disconnect();

                    error="Сообщение успешно передано в тех. поддержку.";
                } catch (IOException ex) {
                    System.err.println(ex);

                    error=ex.getMessage();
                }
                return null ;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                super.onPostExecute(aVoid);

                Log.v("qqaass",error);

                if (ctx.getClass().equals(ActivitySplash.class)) {

                    Intent i = new Intent(ctx,MainActivity.class);
                    i.putExtra("error", error);
                    ctx.startActivity(i);
                    ctx.finish();
                }

                if (ctx.getClass().equals(MainActivity.class)) {

                    ((MainActivity)ctx).pager.getAdapter().notifyDataSetChanged();
                    ((MainActivity)ctx).pager.setCurrentItem(((MainActivity)ctx).pos);
                    if (sis!=null)  ((MainActivity)ctx).pager.setCurrentItem(sis.getInt("page"));

                }
            }
        }.execute();



    }

    static String  text="";


    static public void dloadMessage(final MainActivity ctx,final String urll,final  Handler hen) {

        error="";


       Runnable r=new Runnable() {
            @Override
            public void run() {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;


                File file = null;
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "progtv");
                dir.mkdirs();
                try {
                    dir.createNewFile();
                    file = new File(dir, "message.txt");
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    error=e.getMessage();
                }
                Log.v("helperrr", file.getAbsolutePath());

                String filename = file.getAbsolutePath();

                try {
                    java.net.URL url = new URL(urll);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();


                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        error= "Server returned HTTP " + connection.getResponseCode()
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


                        total += count;
                        // publishing the progress....

                        output.write(data, 0, count);
                    }

                } catch (Exception e) {
                    Log.v("helperrr", e.toString());
                    error=e.getMessage();

                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                        error=ignored.getMessage();
                    }

                    if (connection != null)
                        connection.disconnect();
                }


                File ffile = null;

                File f = ctx.getFilesDir();

                f.mkdirs();

                try {
                    f.createNewFile();
                    ffile = new File(f, "message.txt");
                    ffile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    error=e.getMessage();
                }

                try {
                    copy(file, ffile);
                } catch (IOException e) {
                    e.printStackTrace();
                    error=e.getMessage();
                }
                int z=0;
                int t=0;
                List<Message> lsh = parseXmlMessage(ctx, "");
                if (lsh.size()!=0) {
                    while (z == 0) {

                        if (ctx.th1.isInterrupted()) break;

                        if (t == lsh.size()) t = 0;
                        text = lsh.get(t).text;
                        hen.sendEmptyMessage(z);
                        try {
                            sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        t++;
                    }
                }
            }
        };

     Thread th= new Thread(r);

     th.start();

     ctx.th1=th;


    }

    static  void postCompl(final Activity ctx,final String id,final String text,final String status,final  String pl,final String su)
    {


        error="";


        new AsyncTask<Void,Void,Void>()
        {


            @Override
            protected Void doInBackground(Void... voids) {
                URL url = null;
                try {
                    Log.v("wqase",status);

                    url = new URL("http://xn--80aaa0bbiancdg9m.xn--p1ai/controll.php?id="+id+"&text="+text+"&status="+status+"&player="+pl+"&su="+su);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    error=e.getMessage();
                }
                HttpURLConnection connection = null;
                Log.v("qwss",url.toString());
                try {
                    connection = (HttpURLConnection)url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                    error=e.getMessage();
                }
                try {
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        error="Сообщение успешно передано в тех. поддержку.";
                    } else {
                        error="Ошибка передачи данных";
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }

                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(ctx,error,Toast.LENGTH_LONG).show();
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    @NonNull
    static XmlPullParser createXmlPullParserCom(Context ctx) throws IOException, XmlPullParserException {


        XmlPullParser parser = Xml.newPullParser();


        File f=new File(ctx.getFilesDir()+File.separator+"message.txt");
        InputStream inputStream = new FileInputStream(f);
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        return parser;
    }


    static   public List<Message> parseXmlMessage(Context ctx,String gr) {

        Log.v("edfr","parseXmlCopml");

        XmlPullParser parser= null;
        try {
            parser = createXmlPullParserCom(ctx);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        Log.v("edfr","parser "+parser);

        List<Message> channels = new ArrayList<>();


        int eventType = 0;
        try {
            eventType = parser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        Message channel = null;

        try {
            Log.v("edfr","event "+parser.getEventType());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = "";
            Log.v("edfr","в цикле "+eventType);

            if (eventType == XmlPullParser.START_DOCUMENT) {
                try {
                    Log.v("edfr","start document "+parser.getEventType());
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                channels = new ArrayList<>();


            }

            else if (eventType == XmlPullParser.START_TAG) {
                Log.v("edfr","start_tag "+parser.getName());
                name=parser.getName();

                if (name.equals("message")) {
                    channel = new Message();
                    String id = parser.getAttributeValue(null, "id");
                    String text = parser.getAttributeValue(null, "text");
                    String status=parser.getAttributeValue(null, "status");
                    Log.v("qwsx",id+" "+text+" "+status);
                    channel.id=id;
                    channel.text=text;
                    channel.status=status;
                }



            }

            else if (eventType == XmlPullParser.END_TAG) {
                name = parser.getName();

                if (name!=null && name.equalsIgnoreCase("message")) {
                    Log.v("edfr",parser.getName());
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

        return channels;

    }

    static void dlgUrl2File(ActivitySplash ctx, String url) {

        saveUrl(ctx,url);
    }

    static void dlgUrl2File4(TvActivity ctx, String url) {

        saveUrl4(ctx,url);
    }

    static void dlgUrl2File2(ActivitySplash ctx, String url) {

        saveUrl2(ctx,url);
    }

    static void dlgUrl2File3(TvActivity ctx, String url) {

        saveUrl3(ctx,url);
    }

    static void dlgUrl2File5(Activity ctx, String url) {

        saveUrl5(ctx,url);
    }
    static  File createDir()
    {
       String path= Environment.getExternalStorageDirectory() +File.separator+"Kanali_config";

       File f0=new File(path);

       f0.mkdirs();

        try {
            f0.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f0;
    }





    // загузка файла из url
    static void saveUrl(final ActivitySplash ctx,final String urlString) {


        new AsyncTask<String, Integer, String>() {

            private PowerManager.WakeLock mWakeLock;


            @Override
            protected String doInBackground(String... sUrl) {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;


                File file = null;
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "progtv");
                dir.mkdirs();
                try {
                    dir.createNewFile();
                    file = new File(dir, "xmltv.xml.gz");
                   // file=new File(dir,"tvguid.zip");
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("helperrr", file.getAbsolutePath());

                String filename=file.getAbsolutePath();

                try {
                    URL url = new URL(sUrl[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();


                    // expect HTTP 200 OK, so we don't mistakenly save error report
                    // instead of the file
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Server returned HTTP " + connection.getResponseCode()
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
                    Log.v("helperrr",e.toString());
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



                try {
                  file=  unGzip(file,true);
                  //  file=  unZip(file,true);

                } catch (IOException e) {
                    e.printStackTrace();
                }



                File ffile = null;

                File f=ctx.getFilesDir();

                f.mkdirs();

                try {
                    f.createNewFile();
                    ffile = new File(f, "xmltv.xml");
                  //  ffile=new File(f,"tvguid.xml");
                    ffile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    copy(file,ffile);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String s) {
               ctx.x2=true;
               ctx.go();
                super.onPostExecute(s);
            }
        }.execute(urlString);
    }


    static void saveUrl4(final TvActivity ctx,final String urlString) {


        new AsyncTask<String, Integer, String>() {

            private PowerManager.WakeLock mWakeLock;


            @Override
            protected String doInBackground(String... sUrl) {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;


                File file = null;
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "progtv");
                dir.mkdirs();
                try {
                    dir.createNewFile();
                    file = new File(dir, "xmltv.xml.gz");
                    // file=new File(dir,"tvguid.zip");
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("helperrr", file.getAbsolutePath());

                String filename=file.getAbsolutePath();

                try {
                    URL url = new URL(sUrl[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();


                    // expect HTTP 200 OK, so we don't mistakenly save error report
                    // instead of the file
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Server returned HTTP " + connection.getResponseCode()
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
                    Log.v("helperrr",e.toString());
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



                try {
                    file=  unGzip(file,true);
                    //  file=  unZip(file,true);

                } catch (IOException e) {
                    e.printStackTrace();
                }



                File ffile = null;

                File f=ctx.getFilesDir();

                f.mkdirs();

                try {
                    f.createNewFile();
                    ffile = new File(f, "xmltv.xml");
                    //  ffile=new File(f,"tvguid.xml");
                    ffile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    copy(file,ffile);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                super.onPostExecute(s);
                ctx.x2=true;
                ctx.go();
            }
        }.execute(urlString);
    }

    static void saveUrl2(final ActivitySplash ctx,final String urlString) {


        new AsyncTask<String, Integer, String>() {

            private PowerManager.WakeLock mWakeLock;


            @Override
            protected String doInBackground(String... sUrl) {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;


                File file = null;
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "progtv");
                dir.mkdirs();
                try {
                    dir.createNewFile();
                    file = new File(dir, "xmltv2.xml");
                    // file=new File(dir,"tvguid.zip");
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("helperrr2", file.getAbsolutePath());

                String filename=file.getAbsolutePath();

                try {
                    URL url = new URL(sUrl[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();


                    // expect HTTP 200 OK, so we don't mistakenly save error report
                    // instead of the file
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Server returned HTTP " + connection.getResponseCode()
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
                    Log.v("helperrr2",e.toString());
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

                File f=ctx.getFilesDir();

                f.mkdirs();

                try {
                    f.createNewFile();
                    ffile = new File(f, "xmltv2.xml");
                    //  ffile=new File(f,"tvguid.xml");
                    ffile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    copy(file,ffile);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               ctx.x3=true;
               ctx.go();

            }
        }.execute(urlString);
    }

    static void saveUrl5(final Activity ctx,final String urlString) {


        new AsyncTask<String, Integer, String>() {

            private PowerManager.WakeLock mWakeLock;


            @Override
            protected String doInBackground(String... sUrl) {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;


                File file = null;
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "progtv");
                dir.mkdirs();
                try {
                    dir.createNewFile();
                    file = new File(dir, "xmltv2.xml");
                    // file=new File(dir,"tvguid.zip");
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("helperrr2", file.getAbsolutePath());

                String filename=file.getAbsolutePath();

                try {
                    URL url = new URL(sUrl[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();


                    // expect HTTP 200 OK, so we don't mistakenly save error report
                    // instead of the file
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Server returned HTTP " + connection.getResponseCode()
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
                    Log.v("helperrr2",e.toString());
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

                File f=ctx.getFilesDir();

                f.mkdirs();

                try {
                    f.createNewFile();
                    ffile = new File(f, "xmltv2.xml");
                    //  ffile=new File(f,"tvguid.xml");
                    ffile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    copy(file,ffile);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


            }
        }.execute(urlString);
    }

    static void saveUrl3(final TvActivity ctx,final String urlString) {


        new AsyncTask<String, Integer, String>() {

            private PowerManager.WakeLock mWakeLock;


            @Override
            protected String doInBackground(String... sUrl) {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;


                File file = null;
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "progtv");
                dir.mkdirs();
                try {
                    dir.createNewFile();
                    file = new File(dir, "xmltv2.xml");
                    // file=new File(dir,"tvguid.zip");
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("helperrr2", file.getAbsolutePath());

                String filename=file.getAbsolutePath();

                try {
                    URL url = new URL(sUrl[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();


                    // expect HTTP 200 OK, so we don't mistakenly save error report
                    // instead of the file
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Server returned HTTP " + connection.getResponseCode()
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
                    Log.v("helperrr2",e.toString());
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

                File f=ctx.getFilesDir();

                f.mkdirs();

                try {
                    f.createNewFile();
                    ffile = new File(f, "xmltv2.xml");
                    //  ffile=new File(f,"tvguid.xml");
                    ffile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    copy(file,ffile);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                ctx.x3=true;
                ctx.go();

            }
        }.execute(urlString);
    }


    public static File unZip(File infile, boolean deleteGzipfileOnSuccess) throws IOException {

        Log.v("helperrr","unzip");

        ZipInputStream gin = new ZipInputStream( new BufferedInputStream(new FileInputStream(infile)));
        FileOutputStream fos = null;
        try {
            File outFile = new File(infile.getParent(), infile.getName().replaceAll("\\.zip$", ""));
            fos = new FileOutputStream(outFile);
            byte[] buf = new byte[100000];
            int len;
            while ((len = gin.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fos.close();
            if (deleteGzipfileOnSuccess) {
                infile.delete();
            }
            Log.v("qwert",outFile.length()+"");
            return outFile;
        } finally {
            if (gin != null) {
                gin.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }




    public static File unGzip(File infile, boolean deleteGzipfileOnSuccess) throws IOException {

        Log.v("helperrr","unzip");

        GZIPInputStream gin = new GZIPInputStream(new FileInputStream(infile));
        FileOutputStream fos = null;
        try {
            File outFile = new File(infile.getParent(), infile.getName().replaceAll("\\.gz$", ""));
            fos = new FileOutputStream(outFile);
            byte[] buf = new byte[100000];
            int len;
            while ((len = gin.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fos.close();
            if (deleteGzipfileOnSuccess) {
                infile.delete();
            }
            return outFile;
        } finally {
            if (gin != null) {
                gin.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
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
}