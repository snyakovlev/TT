package ru.television.online;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DLG#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DLG extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    MainActivity glav;
    WorkAdapterDlg adapter;
    ArrayList<File> files;
public static ArrayList<File> save_for_move=new ArrayList<>();
    double koa;
    int hp;
    int wp;
    int t=0;
    int reglay=0;
    String uuidcurrsheet;



    // TODO: Rename and change types of parameters

    RecyclerView list;


    public DLG() {
        // Required empty public constructor
    }


    public static DLG newInstance() {
        DLG fragment = new DLG();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

  //  @Override
  //  public void onCreate(Bundle savedInstanceState) {
  //      super.onCreate(savedInstanceState);
  //      setRetainInstance(true);

 //       setHasOptionsMenu(true);
 //   }

    TextView tv1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       // getDialog().setTitle("Папка \"Download\" устройства...");



        final View root = inflater.inflate(R.layout.worklistdlg, null);

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());

         files=listFilesWithSubFolders(dir);

         Log.v("qwer",files.size()+"");

        tv1=(TextView)root.findViewById(R.id.textView1);

        tv1.setText(dir.getAbsolutePath());

        list = (RecyclerView) root.findViewById(R.id.worklistlistdlg);

        list.setItemViewCacheSize(0);

        list.setDrawingCacheEnabled(true);

        glav=(MainActivity) getActivity();

        adapter = new WorkAdapterDlg(glav, files,this,false);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());

        list.setLayoutManager(lm);


        list.setAdapter(adapter);

        return root;
    }


    public ArrayList<File> listFilesWithSubFolders(File dir) {
        ArrayList<File> files = new ArrayList<File>();
        for (File file : dir.listFiles()) {
            if (!file.isDirectory())
                files.add(file);
        }
        return files;
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

    }

}
