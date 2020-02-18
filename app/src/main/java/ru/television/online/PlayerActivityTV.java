package ru.television.online;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.leanback.app.PlaybackSupportFragment;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import ru.mobileup.channelone.tv1player.VitrinaSDK;
import ru.mobileup.channelone.tv1player.player.PlayerLiveFragmentBuilder;
import ru.mobileup.channelone.tv1player.player.VideoPlayer;
import ru.mobileup.channelone.tv1player.player.VitrinaTVPlayerFragment;
import ru.mobileup.channelone.tv1player.player.VitrinaTVPlayerListener;
import ru.mobileup.channelone.tv1player.player.VitrinaTvPlayerApi;


public class PlayerActivityTV extends AppCompatActivity {

    protected PowerManager.WakeLock mWakeLock;
    String groupid;
    int pos;
    List<Channel> channels;
    String curr="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        helper1.dlgUrl2File5(this,"http://yasnstudio.moscow/func0.php");
        VitrinaSDK.init(getApplication());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tag:tt");
        this.mWakeLock.acquire();
        pos=getIntent().getIntExtra("pos",0);
        groupid=getIntent().getStringExtra("groupid");
        curr=getIntent().getStringExtra("curr");

        if (helper2.getFavorite(this)!=null && groupid.equals("fav"))
        {

            DBI dbi=new DBI(this);

            dbi.getWritableDatabase();

            channels=dbi.qDB();
        }
        else {

            channels =TvActivity2.list;
        }



        rek=  PreferenceManager.getDefaultSharedPreferences(this).getInt("rek",0);

        rek++;

        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("rek",rek).apply();

        if (rek>=6)
        {
            rek=0;
            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("rek",0).apply();
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-9387716763311379/7261898768");

            mInterstitialAd.loadAd(new AdRequest.Builder().build());



            mInterstitialAd.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mInterstitialAd.show();

                }

                @Override
                public void onAdClosed() {
                    // Load the next interstitial.

                    showEXO();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    showEXO();
                }
            });

        }

        else {
            showEXO();
            Log.d("lllaaa", rek+" noshow");
        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.sd, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.menu.sd)
        {
            FragmentPlayer.showProgrammTV(this,channels.get(pos).getIdd());
        }
        return super.onOptionsItemSelected(item);
    }


   void showEXO()
   {
       this.setTitle(channels.get(pos).getName()+"        сейчас в эфире:   "+curr);
       if (channels.get(pos).getFile2().equals("") && !channels.get(pos).getFile().equals("") ) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fr0, FragmentPlayerTV.newInstance(channels.get(pos).getName(),channels.get(pos).getFile(),
                   channels.get(pos).getFile2(),channels.get(pos).getFile3(),  channels.get(pos).getIdd(),channels.get(pos).getSelect())).commit();

       }
       else if (channels.get(pos).getFile().equals("") && !channels.get(pos).getFile2().equals("")) {
           PlayerLiveFragmentBuilder builder = new PlayerLiveFragmentBuilder(); //создаем билдер
           builder.buildWithRemoteConfig(channels.get(pos).getFile2()); //передаем url конфига
           builder.setCloseFragmentWhenExitPressed(false); //настраиваем “опциональные параметры
           //для формируемого фрагмента

           PlaybackSupportFragment s=new PlaybackSupportFragment();
           builder.addCustomControlsResource(R.layout.controls);
           VitrinaTVPlayerFragment result = builder.getResult(); //получаем результат
           result.setCloseActivityOnRelease(false);//настраиваем опциональные параметры фрагмента
           result.setVitrinaTVPlayerListener(new VitrinaTVPlayerListener() {
               @Override
               public void onPausedAdvertVitrinaTVPlayer() {

               }

               @Override
               public void onInitVitrinaTVPlayer(VitrinaTvPlayerApi vitrinaTvPlayerApi) {

               }

               @Override
               public void onAdvertVitrinaTVPlayer(boolean b) {

               }

               @Override
               public void onErrorVitrinaTVPlayer(String s, VideoPlayer.ErrorCode errorCode, boolean b) {

               }

               @Override
               public void onPlaylistNext() {

               }

               @Override
               public void onSkipNext() {

               }

               @Override
               public void onSkipPrevious() {

               }

               @Override
               public void onMetadataUpdate(com.google.android.pexoplayer2.metadata.Metadata metadata, String s) {

               }


               @Override
               public void onMute(boolean b) {

               }

               @Override
               public void onPlayClick() {

               }

               @Override
               public void onPauseClick() {

               }

               @Override
               public void onStopClick() {

               }

               @Override
               public void onNextClick() {

               }

               @Override
               public void onPreviousClick() {

               }

               @Override
               public void onSeek(int i) {

               }

               @Override
               public void onQualityClick() {

               }
           });



           getSupportFragmentManager().beginTransaction().replace(R.id.fr0, result).commit();
           getSupportFragmentManager().beginTransaction().add(R.id.fr0, s).commit();
       }


       else if (channels.get(pos).getFile2().equals("") && channels.get(pos).getFile2().equals(""))
       {
           getSupportFragmentManager().beginTransaction().replace(R.id.fr0, FragmentPlayerWebTV.newInstance(channels.get(pos).getName(),channels.get(pos).getFile(),
                   channels.get(pos).getFile2(),channels.get(pos).getFile3(),  channels.get(pos).getIdd(),channels.get(pos).getSelect())).commit();

       }

   }



    @Override
    public void onBackPressed() {  //обработка кнопки "назад" на телефоне
        //в противном случае спрашиваем, хочет ли пользователь выйти и выходим в случае необходимости

        int pid = android.os.Process.myPid();

        android.os.Process.killProcess(pid);


        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    static int rek=0;
    InterstitialAd mInterstitialAd;


    @Override
    protected void onResume() {


        super.onResume();
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        if (event.getKeyCode()==KeyEvent.KEYCODE_BACK)
        {
            if (mInterstitialAd.isLoaded())
            {

            }
        }
        return true;
    }

}
