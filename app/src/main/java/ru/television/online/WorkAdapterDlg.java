package ru.television.online;

import android.content.Context;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;


/**
 * Created by Катя on 04.09.2015.
 */
public class WorkAdapterDlg extends RecyclerView.Adapter<WorkAdapterDlg.MyViewHolder2> {


     ArrayList<File> values=new ArrayList<>();

    MainActivity glav;



      class MyViewHolder2  extends RecyclerView.ViewHolder {


        TextView textView1;

       int pos = 0;

        File currsave;

        Context context;

       int i=0;

          boolean error;


        MyViewHolder2(View rowView,Context context) {

            super(rowView);

            textView1 = (TextView) rowView.findViewById(R.id.textView1);

            error=false;

        }

    }

    @Override
    public MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listxmldlg, parent, false);

        MyViewHolder2 vh = new MyViewHolder2(v,this.glav);

        return vh;
    }

    DLG wl;


    public WorkAdapterDlg(Context context, ArrayList<File> values, DLG wl, boolean move) {


        Log.v("dfgbn",values.size()+"");

        this.wl=wl;

        this.glav=(MainActivity) context;

        this.values.clear();

        for (File f:values) {

            File path=f.getAbsoluteFile();

            String ext=getFileExtension(path);

            if (ext.equals("m3u") || ext.equals("m3u8")) {

                this.values.add(f);

            }
        }



    }




    static String getFileExtension(File file) {

        String fileName = file.getName();

        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)

            return fileName.substring(fileName.lastIndexOf(".")+1);

        else return "";
    }


    @Override
    public void onBindViewHolder( MyViewHolder2 vh,  int position) {


        vh.currsave = values.get(position);

        vh.pos = position;

        vh.textView1.setText(values.get(vh.pos).getName());

        vh.textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ParcerM3U p=new ParcerM3U();

                File path=values.get(vh.pos).getAbsoluteFile();

                String AUTHORITY_STRING=BuildConfig.APPLICATION_ID;

                Uri treeUri= FileProvider.getUriForFile(glav, AUTHORITY_STRING, path);

                glav.uri=treeUri.toString();

                String uri=treeUri.toString();

                p.dnlFromUrl(glav,uri,true);

                glav.sav=true;

                PreferenceManager.getDefaultSharedPreferences(glav).edit().putString("url", uri).apply();

                PreferenceManager.getDefaultSharedPreferences(glav).edit().putBoolean("fold",true).apply();

                wl.dismiss();

            }
        });

    }

    @Override
    public int getItemCount() {

        return values.size();
    }


}
