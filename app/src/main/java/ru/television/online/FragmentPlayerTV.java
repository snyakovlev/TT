package ru.television.online;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.mobileup.channelone.tv1player.player.VitrinaTVPlayer;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerTV#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerTV extends Fragment implements PlaybackPreparer {



    PlayerView eplayerll;
    Uri uri,uri2,uri3;
    String name;
    String idd;
    String select;
    boolean err=false;
    SimpleExoPlayer player;
   static TextView serror;
    ProgressBar pb;
    TextView curr,pot;

    Context context;


    VitrinaTVPlayer vtvp;



    public FragmentPlayerTV() {
        // Required empty public constructor
    }


    public static FragmentPlayerTV newInstance(String name, String url, String url2, String url3, String idd, String select) {
        FragmentPlayerTV fragment = new FragmentPlayerTV();

        Bundle args=new Bundle();

        args.putString("url",url);
        args.putString("url2",url2);
        args.putString("url3",url3);
        args.putString("idd",idd);
        args.putString("name",name);
        args.putString("select",select);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

         setRetainInstance(true);
        setHasOptionsMenu(true);
        context=getActivity().getApplicationContext();



        uri=Uri.parse(getArguments().getString("url"));
        uri2=Uri.parse(getArguments().getString("url2"));
        uri3=Uri.parse(getArguments().getString("url3"));
        idd=getArguments().getString("idd");
        name=getArguments().getString("name");
        select=getArguments().getString("select");
        su=select;

    }



    int regim=0;
    private DefaultTrackSelector trackSelector;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View root=getLayoutInflater().inflate(R.layout.fragment_player,null);

      curr=root.findViewById(R.id.curr);




      act=(PlayerActivityTV)getActivity();
      pot=root.findViewById(R.id.pot);

      pot.setText("EXO#"+select);

      pot.setVisibility(View.GONE);

      curr.setTypeface(Typeface.DEFAULT_BOLD);

        initProg();

        Log.v("aqws","createfragment");

        eplayerll=root.findViewById(R.id.eplayerll);



        serror=root.findViewById(R.id.error);

        pb=root.findViewById(R.id.pb);

       // initPlayer();




        ImageButton m=root.findViewById(R.id.exo_m);

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (regim) {
                    case 0: {
                        Log.v("qaww","0");
                        eplayerll.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    }
                    break;

                    case 1: {
                        Log.v("qaww","1");
                        eplayerll.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                    }
                    break;

                    case 3: {
                        Log.v("qaww","2");
                        eplayerll.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                    }
                    break;
                }

                regim++;

                if (regim==4) regim=0;

            }
        });




        return root;
    }

    int errors=0;



   PlayerActivityTV act;



    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initPlayer();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initPlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        eplayerll.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    long playbackPosition;
    int currentWindow;
    boolean playWhenReady;



    private void releasePlayer() {
        if (player != null) {

            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            try {
                if (ms!=null && !err)  ms.releaseSource(null);
            }
            catch (NullPointerException e)
            {
                return;
            }
            player.clearVideoSurface();
            player.release();
            player = null;
            this.onDestroy();
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    void initPlayer()
    {
        serror.setVisibility(View.GONE);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();



        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();


        //LoadControl loadControl = new DefaultLoadControl( new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE));

        player = ExoPlayerFactory.newSimpleInstance(getActivity(),trackSelector, loadControl);

        eplayerll.setPlayer(player);

        player.seekTo(currentWindow, playbackPosition);



       pre();


    }




    void pre()
    {
        ms=buildMediaSource(uri,"");

       if(ms!=null) {player.prepare(ms, true, false);}


       player.setRepeatMode(Player.REPEAT_MODE_ALL);

        player.addListener(new ExoPlayer.EventListener() {



            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

                if (isLoading) {
                    pb.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady && playbackState == Player.STATE_READY) {
                    err=false;
                }
                else
                {
                    err=true;
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                err=true;


                su=select;
                serror.setVisibility(View.GONE);








            }


        });

      player.setPlayWhenReady(true);

    }



    MediaSource ms=null;

   void initProg()
    {
        curr.setText(name);
    }





static void showProgrammTV(Context ctx,String idd)
{
    String text="";
    List<ItemProgram> ar=new ArrayList();

    try {
        ar=helper2.parseXmlprogtv(ctx,idd);
    } catch (XmlPullParserException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }


    for (ItemProgram i:ar)
    {
        Log.v("awe",i.stop_millisec+"  "+System.currentTimeMillis()+"  " +(System.currentTimeMillis()+(1000*60*60*24)));

        Calendar cal=Calendar.getInstance();

        cal.setTimeInMillis(i.stop_millisec);

        Log.v("aaww",cal.get(Calendar.DAY_OF_MONTH)+"."+cal.get(Calendar.MONTH)+"."+cal.get(Calendar.YEAR)+"   "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));

        if (i.stop_millisec>=System.currentTimeMillis() && i.stop_millisec<System.currentTimeMillis()+(1000*60*60*24)) {
            text = text + i.start_time + "-" + i.stop_time + "  " + i.text + "\n\n";
        }
    }

    ScrollView sw=new ScrollView(ctx);

    sw.setFillViewport(true);

    LinearLayout ll=new LinearLayout(ctx);

    sw.addView(ll);



    TextView v=new TextView(ctx);

    ll.addView(v);

    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    lp.setMargins(16,16,16,16);


    v.setLayoutParams(lp);
    v.setText(text);
    AlertDialog ad=new AlertDialog.Builder(ctx)
            .setTitle("")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            })
            .setView(sw)
            .show();
}

    @Override
    public    void onDestroy() {
        super.onDestroy();
    }

    String su="0";

 void sendComplaint()
{
 LinearLayout ll=new LinearLayout(getActivity());
 ViewGroup.MarginLayoutParams lp=new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
 lp.setMargins(8,8,8,8);
  final   EditText v=new EditText(getActivity());
  v.setLayoutParams(lp);
  ll.addView(v);

    AlertDialog ad=new AlertDialog.Builder(getActivity())
            .setView(ll)
            .setTitle("Сообщение в тех. поддержку")
            .setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.v("qqwwer","send");

                    Complaint compl=new Complaint();

                    compl.id=name;

                    compl.text=v.getText().toString();

                    compl.status=serror.getText().toString();

                    compl.player="EXO";

                    compl.su=su+"";

                    if (compl.status.equals("")) compl.status="noerror";

                    helper1.createCompFile(getActivity(),compl);

                    helper1.postCompl(getActivity(),compl.id,compl.text,compl.status,compl.player,compl.su);

                }
            })
            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            })
            .show();


}


    @Override
    public void preparePlayback() {
        player.retry();
    }

    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {

        final DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(context, "rf.kanali.kanali"), bandwidthMeterA);


            @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
            switch (type) {
                case C.TYPE_DASH:
                    return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                case C.TYPE_SS:
                    return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                case C.TYPE_HLS:
                    return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

                default:
                    throw new IllegalStateException("Unsupported type: " + type);
            }

    }


}
