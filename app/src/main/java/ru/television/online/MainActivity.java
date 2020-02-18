package ru.television.online;



import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.mobileup.channelone.tv1player.VitrinaTVPlayerApplication;


public class MainActivity extends AppCompatActivity {



   AsyncTask bs=null;
    Bundle savedInstanceState;
    int page=0;
    ProgressBar pb;
    Group group;

    Adapterr ad;
    boolean folder;
    ViewPager pager;
    LinearLayout ll;
    TextView info,pusto;
    Handler h;
    MainActivity act;
    Button rec;
    Thread th1;
    boolean error;
    int pos;
    EditText edit_m3u;
    ImageView but_m3u,save_m3u,regim;
    ImageView home,fold,search;

    String groupid;
   // LinearLayoutCompat lin_fav;
    int i=0;
    RecyclerView fav;

    LinearLayout fav_l;
    MainActivity activ;


    private final int SPLASH_DISPLAY_LENGTH = 5000;



    FrameLayout fl0;

    List<Channel> chs;

  public   void noti()
    {
        DBI dbi=new DBI(this);

        dbi.getWritableDatabase();

        chs=dbi.qDB();

        ad=new Adapterr(this,chs,fav_l);

        fav.setAdapter(ad);
    }

    ListChannel l;

    RecyclerView listch,fav_list;

    static ArrayList<Channel> list;

    static ArrayList<Channel> templist;

    static ArrayList<String> cht;

    static boolean sav;
    App app;
    boolean reg_n=false;


    void saveList(ArrayList<Channel> ch)
    {
        DBI db=new DBI(this);
        db.getWritableDatabase();
        db.dDbt("");
        for (Channel k:ch) {
            db.iDbt(k.getName(),k.getIdd(),k.getFile(),k.getFile2(),k.getFile3(),"",k.getSelect(),k.getImage());
        }
    }

    ArrayList<Channel> getList()
    {
        DBI db=new DBI(this);
        db.getWritableDatabase();
       return db.qDBt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState==null) PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("hom", true).apply();

        if ( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("reg", false))
        {
            this.setTheme(R.style.Theme_AppCompat_NoActionBar);
        }
        else
        {
            this.setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar);
        }

        setContentView(R.layout.activity_main);

        activ=this;

        regim=(ImageView)findViewById(R.id.regim);



        regim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("reg", false))
                {

                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("reg",false).apply();
                }
                else
                {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("reg", true).apply();
                }

                Intent i=new Intent(activ,ActivitySplash.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                activ.finish();
            }
        });



        pb=(ProgressBar)findViewById(R.id.pb);

        fav_l=(LinearLayout)findViewById(R.id.fav_l);

        save_m3u=(ImageView)findViewById(R.id.save_m3u);

        save_m3u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listM3Ushow();
            }
        });

        search=(ImageView)findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          int z = 0;
                                          templist = new ArrayList<>();

                                          templist.clear();
                                          if (templist.size() == 0) {
                                              for (Channel ch : list) {
                                                  templist.add(ch);
                                              }
                                          }
                                          ArrayList<Channel> res = new ArrayList<>();
                                          String st = edit_m3u.getText().toString().trim();
                                          st = st.toLowerCase();
                                          for (int i = 0; i < list.size(); i++) {
                                              String st1 = list.get(i).getName().toLowerCase().trim();
                                              if (st1.contains(st)) {
                                                  res.add(list.get(i));
                                                  z++;
                                              }
                                          }
                                          if (z != 0) {
                                              ((Adaptera) listch.getAdapter()).channels = res;
                                              list = res;
                                          } else {
                                              list = templist;
                                              ((Adaptera) listch.getAdapter()).channels = list;
                                          }


                                          listch.getAdapter().notifyDataSetChanged();

                                          new AsyncTask<Void, Void, Void>() {

                                              @Override
                                              protected Void doInBackground(Void... voids) {

                                                  if (list == null) return null;

                                                  cht.clear();

                                                  for (Channel c : list) {

                                                      cht.add(getCurrTV2(getBaseContext(), c.getIdd()));

                                                  }
                                                  return null;
                                              }

                                              @Override
                                              protected void onPostExecute(Void aVoid) {
                                                  super.onPostExecute(aVoid);
                                                  Log.v("erty","posr");
                                                      ((Adaptera) listch.getAdapter()).cht = cht;
                                                      listch.getAdapter().notifyDataSetChanged();

                                              }
                                          }.execute();
                                      }
                                  });


        home=(ImageView)findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("fold", false).apply();

                list=helper2.parseXmlChannel(activ,"1");

                PreferenceManager.getDefaultSharedPreferences(activ).edit().putString("url", "").apply();

                PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("hom", true).apply();

                new AsyncTask<Void,Void,Void>()
                {

                    @Override
                    protected Void doInBackground(Void... voids) {

                        if (list==null) return null ;

                        for (Channel c:list)
                        {

                            cht.add( getCurrTV2(getBaseContext(),c.getIdd()));

                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);


                        if (listch!=null && activ!=null && list!=null && cht!=null && fav_l!=null) {
                            listch.setAdapter(new Adaptera(activ, list, cht, fav_l));
                            activ.saveList(list);
                        }

                    }
                }.execute();

            }
        });

        listch=(RecyclerView)findViewById(R.id.list_channels);

        pusto=(TextView)findViewById(R.id.pusto);

        edit_m3u=(EditText)findViewById(R.id.edit_m3u);

        listch.setLayoutManager(new LinearLayoutManager(this));

        list=new ArrayList<>();

        pusto.setVisibility(View.GONE);


      if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("hom", true)) {

          list = helper2.parseXmlChannel(this, "1");

         DBI db=new DBI(this);
         db.getWritableDatabase();
         for (Channel k:list) {
             db.iDbt(k.getName(),k.getIdd(),k.getFile(),k.getFile2(),k.getFile3(),"",k.getSelect(),k.getImage());
         }

      }

        cht=new ArrayList<>();

        getProg();

        listch.setAdapter(new Adaptera(this,list,cht,fav_l));

        LinearLayoutManager sglm=new LinearLayoutManager(this);

        fav=(RecyclerView)findViewById(R.id.fav_list);

        fav.setLayoutManager(sglm);

        DBI dbi=new DBI(this);

        dbi.getWritableDatabase();

        chs=dbi.qDB();

        ad=new Adapterr(this,chs,fav_l);

        fav.setAdapter(ad);

        act=this;

        this.savedInstanceState=savedInstanceState;

        if ( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("reg", false))
        {
            regim.setImageDrawable(getResources().getDrawable(R.drawable.den));
            search.setImageDrawable(getResources().getDrawable(R.drawable.icsearchn));
            save_m3u.setImageDrawable(getResources().getDrawable(R.drawable.progn));
            home.setImageDrawable(getResources().getDrawable(R.drawable.homen));
        }
        else
        {
            regim.setImageDrawable(getResources().getDrawable(R.drawable.nit));
            search.setImageDrawable(getResources().getDrawable(R.drawable.icsearch));
            save_m3u.setImageDrawable(getResources().getDrawable(R.drawable.prog));
            home.setImageDrawable(getResources().getDrawable(R.drawable.home));
        }

    }

    void getProg()
    {
        new AsyncTask<Void,Void,Void>()
        {

            @Override
            protected Void doInBackground(Void... voids) {

                if (list==null) return null ;

                for (Channel c:list)
                {

                    cht.add( getCurrTV2(getBaseContext(),c.getIdd()));

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                PreferenceManager.getDefaultSharedPreferences(act).edit().putBoolean("hom", true).apply();
                if (listch!=null && activ!=null && list!=null && cht!=null && fav_l!=null) {
                    listch.setAdapter(new Adaptera(activ, list, cht, fav_l));
                    activ.saveList(list);
                }

            }
        }.execute();

    }

    void openUrlPlaylist(String url)
    {
        PreferenceManager.getDefaultSharedPreferences(act).edit().putInt("index", 0).apply();
        PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("fold", false).apply();
        ParcerM3U p=new ParcerM3U();
        if (url.length()!=0) {
            sav=true;
            p.dnlFromUrl(activ, url,false);
            PreferenceManager.getDefaultSharedPreferences(act).edit().putString("url", url).apply();
            PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("fold",false).apply();
            PreferenceManager.getDefaultSharedPreferences(activ).edit().putString("recent",url).apply();
        }


    }

    void openFilePlaylist()
    {


      DLG dlg=new DLG();
      dlg.show(getSupportFragmentManager(),"dlg");
       // pb.setVisibility(View.VISIBLE);
       // PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("fold", true).apply();
      //  Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
      //  intent.setType("*/*");
      //  intent.addCategory(Intent.CATEGORY_OPENABLE);
      //  startActivityForResult(Intent.createChooser(intent, "Файл плэйлиста..."), 4);
    }

    final String FILM="http://yasnstudio.moscow/kino.m3u";
    final String POZN="http://yasnstudio.moscow/pozn.m3u";
    final String REGION="http://yasnstudio.moscow/region.m3u";
    final String SPORT="http://yasnstudio.moscow/sport.m3u";


   static ArrayList<M3U> arm3u=new ArrayList<>();

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);


        if (requestCode == 4) {
            Uri treeUri;
            if (responseCode == Activity.RESULT_OK) {
                // Get Uri from Storage Access Framework.
               treeUri = intent.getData();
                Log.v("qsqs", treeUri + "");
                // Persist URI - this is required for verification of writability.
                if (treeUri != null) {
                    pb.setVisibility(View.VISIBLE);

                    ParcerM3U p=new ParcerM3U();

                        uri=treeUri.toString();
                        p.dnlFromUrl(activ,treeUri.toString(),true);

                        sav=true;
                    PreferenceManager.getDefaultSharedPreferences(act).edit().putString("url", uri).apply();
                    PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("fold",true).apply();

                }

            }
        }
    }
    String uri="";
    AlertDialog adf=null;
    void dialogSave()
    {
         adf=new AlertDialog.Builder(this)
                .setTitle("Сохранить плэйлист?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setName();
                        cancelll(adf);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    void cancelll(AlertDialog adf)
    {
        adf.cancel();
    }
ProgressBar pbb;
    AlertDialog adz=null;
  void  listM3Ushow()
  {

     final DBI db=new DBI(this);
      db.getWritableDatabase();
      arm3u=new ArrayList<M3U>();
      arm3u.clear();
      arm3u=db.qDBz();

     View z= getLayoutInflater().inflate(R.layout.listm3u,null);

     pbb=(ProgressBar)z.findViewById(R.id.pbb);
     TextView film=z.findViewById(R.id.film);
     film.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             PreferenceManager.getDefaultSharedPreferences(activ).edit().putString("url", FILM).apply();
             PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("fold",false).apply();
             PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("hom",false).apply();
             ParcerM3U p=new ParcerM3U();
             pbb.setVisibility(View.VISIBLE);
                 p.dnlFromUrl(activ, FILM,false);
              adz.cancel();

         }
     });
      TextView pozn=z.findViewById(R.id.pozn);
      pozn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              PreferenceManager.getDefaultSharedPreferences(activ).edit().putString("url", POZN).apply();
              PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("fold",false).apply();
              PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("hom",false).apply();
              ParcerM3U p=new ParcerM3U();
              pbb.setVisibility(View.VISIBLE);
              p.dnlFromUrl(activ, POZN,false);
              adz.cancel();

          }
      });
      TextView region=z.findViewById(R.id.region);
      region.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              PreferenceManager.getDefaultSharedPreferences(activ).edit().putString("url", REGION).apply();
              PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("fold",false).apply();
              PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("hom",false).apply();
              ParcerM3U p=new ParcerM3U();
              pbb.setVisibility(View.VISIBLE);
              p.dnlFromUrl(activ, REGION,false);
              adz.cancel();

          }
      });
      TextView sport=z.findViewById(R.id.sport);
      sport.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              PreferenceManager.getDefaultSharedPreferences(activ).edit().putString("url", SPORT).apply();
              PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("fold",false).apply();
              PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("hom",false).apply();
              ParcerM3U p=new ParcerM3U(); pbb.setVisibility(View.VISIBLE);
              p.dnlFromUrl(activ, SPORT,false);
              adz.cancel();
          }
      });

      RecyclerView listm3u=z.findViewById(R.id.list_m3u);


     listm3u.setLayoutManager(new LinearLayoutManager(this));
     listm3u.setAdapter(new RecyclerView.Adapter<M3UHolder>() {
         @NonNull
         @Override
         public M3UHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View v= LayoutInflater.from(act).inflate(R.layout.item_m3u,parent,false);
             return new M3UHolder(v);
         }

         @Override
         public void onBindViewHolder(@NonNull M3UHolder holder, int position) {

             holder.item3u.setText(arm3u.get(position).n);
             holder.pos=position;
             holder.item3u.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("hom", false).apply();
                     PreferenceManager.getDefaultSharedPreferences(act).edit().putInt("index", 0).apply();
                    // ParcerM3U p=new ParcerM3U();
                     if ( holder.item3u.length()!=0) {
                         pbb.setVisibility(View.VISIBLE);
                       //  p.dnlFromUrl(activ,  arm3u.get(holder.pos).name,arm3u.get(holder.pos).uri.equals("folder"));
                         DBI db=new DBI(activ);
                         db.getWritableDatabase();
                         list=db.qDBx("table"+arm3u.get(holder.pos).id);
                         ((Adaptera) activ.listch.getAdapter()).channels = list;
                         ( activ.listch.getAdapter()).notifyDataSetChanged();
                         activ.saveList(list);

                        // PreferenceManager.getDefaultSharedPreferences(act).edit().putString("url", arm3u.get(holder.pos).name).apply();
                        // PreferenceManager.getDefaultSharedPreferences(activ).edit().putBoolean("fold",arm3u.get(holder.pos).uri.equals("folder")).apply();
                         adz.cancel();
                     }

                 }
             });
             holder.delm3u.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     db.dDbz(arm3u.get(holder.pos).id);
                     arm3u.clear();
                     arm3u=db.qDBz();
                     nott();
                 }
             });

         }

        void nott()
        {
            notifyDataSetChanged();
        }

         @Override
         public int getItemCount() {
             return arm3u.size();
         }
     });
      ImageView del3u=z.findViewById(R.id.del3u);
     adz=new AlertDialog.Builder(this)
              .setView(z)
              .setPositiveButton("Файл...", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      pb.setVisibility(View.VISIBLE);
                      PreferenceManager.getDefaultSharedPreferences(act).edit().putBoolean("hom", false).apply();
                      openFilePlaylist();
                  }
              })
              .setNegativeButton("URL...", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      pb.setVisibility(View.VISIBLE);
                      PreferenceManager.getDefaultSharedPreferences(act).edit().putBoolean("hom", false).apply();
                      setURL();
                  }
              })
             .setNeutralButton("Закрыть", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     adz.cancel();
                 }
             })
              .show();

  };

  String name_pl;
    AlertDialog adv;
  void setName()
  {
      DBI db=new DBI(this);
      db.getWritableDatabase();

      View z= getLayoutInflater().inflate(R.layout.name,null);
      EditText n=z.findViewById(R.id.n);
      adv=new AlertDialog.Builder(this)
              .setView(z)
              .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      String id=Calendar.getInstance().getTimeInMillis()+"";
                      name_pl=n.getText().toString();
                      if ( PreferenceManager.getDefaultSharedPreferences(activ).getBoolean("fold", false)) {
                         if (!uri.equals(""))
                         {
                             db.iDbz(id, uri, "folder",name_pl);
                         db.createTableM3U("table"+id);
                         for (Channel c:list) {
                             db.iDbx("table"+id, c.getName(),c.getIdd(),c.getFile(),c.getFile2(),c.getFile3(),"",c.getSelect(),c.getImage());
                         }
                         }
                         else
                         {
                             Toast.makeText(activ,"Не выбран файл плэйлиста",Toast.LENGTH_LONG).show();
                            canc();
                         }
                      }
                      else
                      {
                          String uri= PreferenceManager.getDefaultSharedPreferences(activ).getString("recent","");
                          db.iDbz(id, uri, "",name_pl);
                      }
                  }
              })
              .show();
  }


    void setURL()
    {
        View z= getLayoutInflater().inflate(R.layout.nurl,null);
        EditText n=z.findViewById(R.id.nur);
        adv=new AlertDialog.Builder(this)
                .setView(z)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    openUrlPlaylist(n.getText().toString());

                    }

                })
                .show();
    }



  void canc()
  {
      adv.cancel();
  }

    List<Channel> getChannel()
    {
        list=new ArrayList();


        //  стс

        Channel sts=new Channel();

        sts.setIdd("79");
        sts.setName("СТС");
        sts.setUrlImage("https://player.mediavitrina.ru/ctc_ext/sdk_android/5d713004/sdk.json");
        sts.setId("ctc");
        list.add(sts);

        //  sts love

        Channel stslove=new Channel();

        stslove.setIdd("-1");
        stslove.setName("СТС love");
        stslove.setUrlImage("");
        stslove.setId("sts_love");
        list.add(stslove);

        // domashniy

        Channel dom=new Channel();

        dom.setIdd("304");
        dom.setName("Домашний");
        dom.setUrlImage("");
        dom.setId("domashniy");
        list.add(dom);


        //russia

        Channel ros=new Channel();

        ros.setIdd("317");
        ros.setName("Россия-1");
        ros.setUrlImage("");
        ros.setId("rossiya_1_");
        list.add(ros);

        //russia24

        Channel ros24=new Channel();

        ros24.setIdd("1683");
        ros24.setName("Россия-24");
        ros24.setUrlImage("");
        ros24.setId("rossiya_24");
        list.add(ros24);

        //russia-k

        Channel rosk=new Channel();

        rosk.setIdd("-1");
        rosk.setName("Россия К");
        rosk.setUrlImage("");
        rosk.setId("russiyk");
        list.add(rosk);

        //channel5

        Channel ch5=new Channel();

        ch5.setIdd("504");
        ch5.setName("5 канал");
        ch5.setUrlImage("");
        ch5.setId("pyatyy_kanal");
        list.add(ch5);


        //channel 78

        Channel ch78=new Channel();

        ch78.setIdd("-1");
        ch78.setName("78 канал");
        ch78.setUrlImage("");
        ch78.setId("78");
        list.add(ch78);

        //ren tv

        Channel ren=new Channel();

        ren.setIdd("18");
        ren.setName("Рен ТВ");
        ren.setUrlImage("");
        ren.setId("ren");
        list.add(ren);

        //Che


        Channel che=new Channel();

        che.setIdd("-1");
        che.setName("Че");
        che.setUrlImage("");
        che.setId("che");
        list.add(che);


        //Spas

        Channel spas=new Channel();

        spas.setIdd("447");
        spas.setName("Спас");
        spas.setUrlImage("");
        spas.setId("spas");
        list.add(spas);

        //Mir

        Channel mir=new Channel();

        mir.setIdd("726");
        mir.setName("Мир");
        mir.setUrlImage("");
        mir.setId("mir");
        list.add(mir);

        return list;

    }


    void startPlayer()
    {
        Intent intent=new Intent(this,PlayerActivity.class);
        intent.putExtra("pos",pos);
        intent.putExtra("groupid",groupid);
        act.startActivity(intent);
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

    String getCurrProg(Context ctx, String idd)
    {
        String text="";
        List<ItemProgram> ar=new ArrayList();
        Log.v("wwqq",idd);

        try {
            ar=helper2.parseXmlprogtv(ctx,idd);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.v("wwqq",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("wwqq",e.getMessage());
        }

        Log.v("wwqq",ar.size()+"");


        for (ItemProgram i:ar)
        {


            Calendar calstart=Calendar.getInstance();

            Calendar calstop=Calendar.getInstance();

            calstop.setTimeInMillis(i.stop_millisec);

            calstart.setTimeInMillis(i.start_millisec);

            String start=calstart.get(Calendar.HOUR_OF_DAY)+":"+(calstart.get(Calendar.MINUTE)<10?"0"+calstart.get(Calendar.MINUTE):calstart.get(Calendar.MINUTE));

            String stop=calstop.get(Calendar.HOUR_OF_DAY)+":"+(calstop.get(Calendar.MINUTE)<10?"0"+calstop.get(Calendar.MINUTE):calstop.get(Calendar.MINUTE));



            text =start+" - "+stop+"    "+i.text;


        }

        return text ;

    }


    @Override
    protected void onResume() {

      pb.setVisibility(View.GONE);

      listch.setEnabled(true);

        ((Adaptera) listch.getAdapter()).click=false;

          if (!PreferenceManager.getDefaultSharedPreferences(activ).getBoolean("hom", false)) {

              list = getList();

              ((Adaptera) listch.getAdapter()).channels = list;

              listch.getAdapter().notifyDataSetChanged();
          }

        super.onResume();
    }

    String  getCurrTV2(Context ctx, String idd)

    {
        String text="";
        List<ItemProgram> ar=new ArrayList();
        Log.v("wwqq",idd);

        try {
            ar=helper2.parseXmlprogtv2(ctx,idd);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.v("wwqq",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("wwqq",e.getMessage());
        }

        Log.v("wwqq",ar.size()+"");


        for (ItemProgram i:ar)
        {


            Calendar calstart=Calendar.getInstance();

            Calendar calstop=Calendar.getInstance();

            calstop.setTimeInMillis(i.stop_millisec);

            calstart.setTimeInMillis(i.start_millisec);

            String start=calstart.get(Calendar.HOUR_OF_DAY)+":"+(calstart.get(Calendar.MINUTE)<10?"0"+calstart.get(Calendar.MINUTE):calstart.get(Calendar.MINUTE));

            String stop=calstop.get(Calendar.HOUR_OF_DAY)+":"+(calstop.get(Calendar.MINUTE)<10?"0"+calstop.get(Calendar.MINUTE):calstop.get(Calendar.MINUTE));



                text =start+" - "+stop+"    "+i.text;


        }

        return text ;

    }








    private static long back_pressed;

    @Override
    public void onBackPressed() {  //обработка кнопки "назад" на телефоне
        //в противном случае спрашиваем, хочет ли пользователь выйти и выходим в случае необходимости


            if (back_pressed + 2000 > System.currentTimeMillis()) {

                PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("rek",0).apply();
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
                super.onBackPressed();
            } else {
                Toast.makeText(getBaseContext(), "Нажмите еще раз, чтобы выйти",
                        Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();



    }



}

class Adapterr extends RecyclerView.Adapter<GroupViewHolderr>
{
    MainActivity act;
    Group group;
    List<Channel> channels=new ArrayList();

    FrameLayout fl0;


    public   Adapterr (MainActivity act,List<Channel> ls,LinearLayout ll)
    {
        this.act=act;
        this.channels=ls;
        if (ls.size()==0) ll.setVisibility(View.GONE);

    }

    @NonNull
    @Override
    public GroupViewHolderr onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.v("dfd","createviewholder");
        View v= LayoutInflater.from(act).inflate(R.layout.item_lchannel_fav,viewGroup,false);
        return new GroupViewHolderr(v);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    boolean click=false;

    @Override
    public void onBindViewHolder(@NonNull final GroupViewHolderr channelViewHolder, int i) {



        channelViewHolder.pos = i;

        channelViewHolder.item_text_channel.setText(channels.get(i).getName());

        channelViewHolder.item_text_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(act, PlayerActivity.class);
                    intent.putExtra("pos", channelViewHolder.pos);
                    intent.putExtra("groupid", "fav");
                    act.startActivity(intent);

                    Log.v("qqws", "click");


            }
        });

        channelViewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DBI dbi = new DBI(act);

                dbi.getWritableDatabase();

                dbi.dDb(channels.get(channelViewHolder.pos).getName());

                act.noti();

            }
        });




    }




    @Override
    public int getItemCount()

    {
        Log.v("dfd","count "+channels.size()+"");

        return  channels.size();
    }
}

class GroupViewHolderr extends RecyclerView.ViewHolder
{



    TextView item_text_channel;
    ImageView del;

    int pos;

    public GroupViewHolderr(@NonNull View itemView) {

        super(itemView);

        Log.v("dfd","groupviewholder");


        item_text_channel=itemView.findViewById(R.id.item_text_channel_fav);
        del=itemView.findViewById(R.id.del);

    }
}


class Adaptera extends RecyclerView.Adapter<GroupViewHoldera>
{
    MainActivity act;
    List<Channel> channels=new ArrayList();
    ArrayList<String> cht;
    LinearLayout f;



    public   Adaptera (MainActivity act,List<Channel> ls,ArrayList<String> cht,LinearLayout f)
    {
       this.f=f;
        this.cht=MainActivity.cht;
        this.channels=MainActivity.list;
        this.act=act;


    }

    @NonNull
    @Override
    public GroupViewHoldera onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.v("dfd","createviewholder");
        View v= LayoutInflater.from(act).inflate(R.layout.item_lchannel,viewGroup,false);
        return new GroupViewHoldera(v);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    String t="";

    boolean click=false;

    @Override
    public void onBindViewHolder(@NonNull final GroupViewHoldera channelViewHolder, int i) {



        channelViewHolder.cur_progr.setText("Нет данных...");

        channelViewHolder.pos = i;

        //

        if (channels.get(i).getImage()==null && PreferenceManager.getDefaultSharedPreferences(act).getBoolean("hom", false)) {

            if (cht != null && channelViewHolder != null && cht.size() > channelViewHolder.pos)
                channelViewHolder.cur_progr.setText(cht.get(channelViewHolder.pos));



            InputStream imageStream = null;
            try {
                // get input stream
                imageStream = act.getAssets().open(channels.get(i).getId() + ".png");
                // load image as Drawable
                Drawable drawable = Drawable.createFromStream(imageStream, null);
                // set image to ImageView
                channelViewHolder.icon.setImageDrawable(drawable);
            } catch (IOException ex) {
                channelViewHolder.icon.setImageDrawable(act.getResources().getDrawable(R.drawable.logo));
            }
        }
        else if (channels.get(i).getImage()!=null)
        {
            Picasso.get().load(Uri.parse(channels.get(i).getImage())).error(R.drawable.logo).into(channelViewHolder.icon);
            //channelViewHolder.icon.setImageURI(Uri.parse(channels.get(i).getImage()));
        }
        else
        {
            channelViewHolder.icon.setImageDrawable(act.getResources().getDrawable(R.drawable.logo));
        }

        channelViewHolder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!click) {

                    act.pb.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(act, PlayerActivity.class);
                    if (cht!=null && channelViewHolder.pos<cht.size()) {
                        intent.putExtra("curr", cht.get(channelViewHolder.pos));
                    }
                    else
                    {
                        intent.putExtra("curr", "");
                    }
                    intent.putExtra("pos", channelViewHolder.pos);
                    intent.putExtra("groupid", "1");
                    act.startActivity(intent);
                    Log.v("qqws", "click");
                    click=true;
                }

                PreferenceManager.getDefaultSharedPreferences(act).edit().putInt("index", channelViewHolder.pos).apply();
            }
        });

        channelViewHolder.item_text_channel.setText(channels.get(i).getName());

        channelViewHolder.item_text_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!click) {
                    act.pb.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(act, PlayerActivity.class);
                    if (cht!=null && channelViewHolder.pos<cht.size()) {
                        intent.putExtra("curr", cht.get(channelViewHolder.pos));
                    }
                    else
                    {
                        intent.putExtra("curr", "");
                    }
                    intent.putExtra("pos", channelViewHolder.pos);
                    intent.putExtra("groupid", "1");
                    act.startActivity(intent);
                    Log.v("qqws", "click");
                    click=true;
                    PreferenceManager.getDefaultSharedPreferences(act).edit().putInt("index", channelViewHolder.pos).apply();
                }

            }
        });

        channelViewHolder.cur_progr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!click) {
                    Intent intent = new Intent(act, PlayerActivity.class);
                    if (cht!=null && channelViewHolder.pos<cht.size()) {
                        intent.putExtra("curr", cht.get(channelViewHolder.pos));
                    }
                    else
                    {
                        intent.putExtra("curr", "");
                    }
                    intent.putExtra("pos", channelViewHolder.pos);
                    intent.putExtra("groupid", "1");
                    act.startActivity(intent);
                    Log.v("qqws", "click");
                    click=true;
                }

            }
        });

        channelViewHolder.programma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              MainActivity.showProgrammTV(act, channels.get(channelViewHolder.pos).getIdd());
            }
        });


        channelViewHolder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                f.setVisibility(View.VISIBLE);
                DBI dbi = new DBI(act);

                dbi.getWritableDatabase();

                List<Channel> chll = dbi.qDB();

                for (Channel ch : chll) {
                    if (ch.getName().equals(channels.get(channelViewHolder.pos).getName())) {
                        Toast.makeText(act, "Этот канал уже добавлен", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                dbi.iDb(channels.get(channelViewHolder.pos).getName(), channels.get(channelViewHolder.pos).getIdd(),
                        channels.get(channelViewHolder.pos).getFile(), channels.get(channelViewHolder.pos).getFile2(), channels.get(channelViewHolder.pos).getFile3(), channels.get(channelViewHolder.pos).getId(),
                        channels.get(channelViewHolder.pos).getSelect(),channels.get(channelViewHolder.pos).getImage());

                act.noti();





            }
        });


    }






    @Override
    public int getItemCount()

    {
       if (channels==null) return 0;

        return channels.size();
    }
}

class GroupViewHoldera extends RecyclerView.ViewHolder
{


    ImageView programma,favorite,icon;
    TextView item_text_channel,cur_progr;

    int pos;

    public GroupViewHoldera(@NonNull View itemView) {

        super(itemView);

        Log.v("dfd","groupviewholder");

        programma=itemView.findViewById(R.id.programma);
        favorite=itemView.findViewById(R.id.favorite);
        item_text_channel=itemView.findViewById(R.id.item_text_channel);
        cur_progr=itemView.findViewById(R.id.cur_progr);
        icon= itemView.findViewById(R.id.icon);
    }



}

class M3UHolder extends RecyclerView.ViewHolder {


    ImageView delm3u;
    TextView item3u;
    int pos;

    public M3UHolder(@NonNull View itemView) {

        super(itemView);

        Log.v("dfd", "groupviewholder");

        delm3u = itemView.findViewById(R.id.del3u);
        item3u = itemView.findViewById(R.id.item3u);
    }
}