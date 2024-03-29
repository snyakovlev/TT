package ru.television.online;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;


public class TvActivity extends AppCompatActivity {

    public static final int NUMBER_OF_REQUEST = 23401;
    boolean x1,x2,x3;
    AlertDialog ad;

    void  perm() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int canRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int canWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int canWL = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK);

            if (canRead != PackageManager.PERMISSION_GRANTED || canWrite != PackageManager.PERMISSION_GRANTED || canWL != PackageManager.PERMISSION_GRANTED) {

                ad = new AlertDialog.Builder(this)
                        .setTitle("Внимание!")
                        .setMessage("Для корректной работы приложения необходимо Ваше разрешение на работу с файлами." +
                                " В диалоговом окне системы будет соответствующий вопрос в котором будут упомянуты фото и прочие файлы." +
                                "Наше приложение не использует Ваши личные данные в своей работе (фото, видео и т.д.). При работе приложения загружаются только служебные" +
                                "файлы (телепрограмма и плэй-лист) на Ваше устройство.\n\n Спасибо за понимание!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                permm();
                            }
                        })
                        .show();
            }
            else {
                helper2.dloadPlayList(this,"http://yasnstudio.moscow/playlist.xml",null);
                helper1.dlgUrl2File4(this,"http://programtv.ru/xmltv.xml.gz");
                helper1.dlgUrl2File3(this,"http://yasnstudio.moscow/func0.php");
            }
        }
        else {
            helper2.dloadPlayList(this,"http://yasnstudio.moscow/playlist.xml",null);
            helper1.dlgUrl2File4(this,"http://programtv.ru/xmltv.xml.gz");
            helper1.dlgUrl2File3(this,"http://yasnstudio.moscow/func0.php");
        }



    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( ad!=null && ad.isShowing() ){
            ad.cancel();
        }
    }

    void go()
    {
        Log.v("qasw",x1+" "+x2+" "+x3);
        if (x1 && x2 && x3) {
            Intent i = new Intent(this, TvActivity2.class);
            startActivity(i);
            finish();
        }
    }

    void permm()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK}, NUMBER_OF_REQUEST);
        }
    }


    void permiss()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int canRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int canWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int canWL = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK);


            if (canRead != PackageManager.PERMISSION_GRANTED || canWrite != PackageManager.PERMISSION_GRANTED ||canWL != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    //показываем объяснение
                } else {

                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WAKE_LOCK}, NUMBER_OF_REQUEST);
                }
            } else {




            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case NUMBER_OF_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    helper2.dloadPlayList(this,"http://yasnstudio.moscow/playlist.xml",null);
                    helper1.dlgUrl2File4(this,"http://programtv.ru/xmltv.xml.gz");
                    helper1.dlgUrl2File3(this,"http://yasnstudio.moscow/func0.php");


                } else {
                    Toast.makeText(this,"К сожалению, без этого разрешения, приложение работать не может",Toast.LENGTH_LONG).show();

                }
                return;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MobileAds.initialize(this);
        Log.v("atv","tv_activity_oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //perm();

prov();

    }

    void prov()
    {
        if (isNetworkOnline())
        {perm();}
        else
        {
            alert();
        }
    }


    void alert()
    {
        ad= new AlertDialog.Builder(this)
                .setMessage("Нет подключения к Интернет")
                .setPositiveButton("Попробовать ещё раз", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ad.cancel();
                        prov();

                    }
                })
                .setCancelable(false)
                .create();
        ad.show();

    }


    public boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }




}
