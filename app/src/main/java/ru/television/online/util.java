package ru.television.online;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class util {
    private static final String TAG = "VLCConfig";

    public static final int AOUT_AUDIOTRACK = 0;
    public static final int AOUT_OPENSLES = 1;

    public static final int VOUT_ANDROID_SURFACE = 0;
    public static final int VOUT_OPEGLES2 = 1;
    public static final int VOUT_ANDROID_WINDOW = 2;

    public static final int HW_ACCELERATION_AUTOMATIC = -1;
    public static final int HW_ACCELERATION_DISABLED = 0;
    public static final int HW_ACCELERATION_DECODING = 1;
    public static final int HW_ACCELERATION_FULL = 2;

    public final static int MEDIA_NO_VIDEO = 0x01;
    public final static int MEDIA_NO_HWACCEL = 0x02;
    public final static int MEDIA_PAUSED = 0x4;

    private static final String DEFAULT_CODEC_LIST = "mediacodec_ndk,mediacodec_jni,iomx,all";

    private static float[] sEqualizer = null;
    private static boolean sHdmiAudioEnabled = false;

    static public void dloadVLCOptions(final Activity ctx, String url) {




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
                    file = new File(dir, "optionsvlc.txt");
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();

                }
                Log.v("helperrr", file.getAbsolutePath());

                String filename = file.getAbsolutePath();

                try {
                    URL url = new URL(sUrl[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();


                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {

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
                    ffile = new File(f, "optionsvlc.txt");
                    ffile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();

                }

                try {
                    copy(file, ffile);
                } catch (IOException e) {
                    e.printStackTrace();

                }



                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.v("qawe","post");



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


    public static ArrayList<String> getLibOptions(Context ctx) {

        File f=new File(ctx.getFilesDir()+File.separator+"optionsvlc.txt");

        ArrayList<String> options = new ArrayList<>();

        FileReader fr = null;
        try {
            fr = new FileReader(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

            BufferedReader reader = new BufferedReader(fr);

        if (f.length()<5) return options;

         String line = null;
        try {
            line = reader.readLine();

           if (!line.equals("")) options.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
            while (line != null) {

        try {
                line = reader.readLine();
            options.add(line);

            } catch (IOException e) {
                e.printStackTrace();
            } }




      //   int   networkCaching = 60000;




        /* XXX: why can't the default be fine ? #7792 */
      //  if (networkCaching > 0)
      //      options.add("--network-caching=" + networkCaching);
      //  options.add("--aout=opensles");
      //  options.add("--vout=androidwindow");
      //  options.add("RV32");
      //  options.add("h264");
     // options.add("--no-ts-trust-pcr");// -- This will Disable the Trust in-stream PCR option
     //   options.add("--audio-time-stretch");
     //   options.add("--ts-seek-percent");// -- Enables Seek based on percentage
      //  options.add("-vvv");

        return options;
    }



    // Equalizer
    public static synchronized float[] getEqualizer() {
        return sEqualizer;
    }

    public static synchronized void setEqualizer(float[] equalizer) {
        sEqualizer = equalizer;
    }

    public static synchronized void setAudioHdmiEnabled(boolean enabled) {
        sHdmiAudioEnabled = enabled;
    }

    public static synchronized boolean isAudioHdmiEnabled() {
        return sHdmiAudioEnabled;
    }

}
