package ru.television.online;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerWebTV#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerWebTV extends Fragment {



    private String url = "http://yasnstudio.moscow/perviy/perviy.html"; //адрес сайта
    private String currentUrl;
    WebView webView; //компонент WebView
    private ProgressBar progressBar;
    private FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Uri uri,uri2;
    String uri3;
    String name;
    String idd;
    String select;
    boolean err=false;
    Context context;




    public FragmentPlayerWebTV() {
        // Required empty public constructor
    }


    public static FragmentPlayerWebTV newInstance(String name, String url, String url2, String url3, String idd, String select) {
        FragmentPlayerWebTV fragment = new FragmentPlayerWebTV();

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
        uri3=getArguments().getString("url3");
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


        View root= inflater.inflate(R.layout.fragment_web, container, false);

        File cach = getActivity().getCacheDir();
        if (cach!=null && cach.exists()) {
            cach.delete();
            Log.v("qqaass", "cach cleared");
        }


        webView=root.findViewById(R.id.webView);
        progressBar=root.findViewById(R.id.progressBar);
        customViewContainer=root.findViewById(R.id.customViewContainer);



        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());
        //включаем JavaScript для просмотра видео
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //загружаем сайт
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webView.loadUrl(uri3);
        progressBar.setProgress(50);


        return root;
    }

    int errors=0;



    public class CustomWebViewClient extends WebViewClient {

        private String url = "http://yasnstudio.moscow/perviy/perviy.html"; //адрес сайта в Punycode
        private boolean isRedirected=false;
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            currentUrl = url;
            if (url.contains(this.url)) { //если страница содержит адрес нашего сайта, остаемся в приложении
                return false;
            } else {
                isRedirected = true;//в противном случае переходим во внешний браузер
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {


            if (!isRedirected) {
                progressBar.setProgress(100);
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }


        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!isRedirected) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }

            isRedirected = false;

        }

        //обработка ошибок
        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (error.getErrorCode() != -15)
                onErrorLoading();
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (errorCode != -15)
                onErrorLoading();
        }
    }

    private void onErrorLoading() { //вывод сообщения об ошибке подключения
        webView.stopLoading();
    }

    private class CustomWebChromeClient extends WebChromeClient {


        //полноэкранное отображение видео
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            webView.setVisibility(View.GONE);
            customViewContainer.setVisibility(View.VISIBLE);
            customViewContainer.addView(view);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            mCustomView = view;
            customViewCallback = callback;
        }

        @Override
        public void onHideCustomView() {

            if (customViewCallback!=null) {
                customViewCallback.onCustomViewHidden();
                customViewContainer.removeView(mCustomView);
                mCustomView = null;
                webView.setVisibility(View.VISIBLE);
                customViewContainer.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //изменение значения progressbar
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
        }
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




}
