package ru.television.online;

import android.content.Intent;

import androidx.leanback.widget.Presenter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GridItemPresenter extends Presenter {

    private final TvActivity2 act;
    TextView tcurr;

    public GridItemPresenter(TvActivity2 mainFragment) {
        this.act = mainFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v= LayoutInflater.from(act).inflate(R.layout.currtv,parent,false);


        tcurr=v.findViewById(R.id.tcurr);



        return new ViewHolder(v);
    }

    boolean click;

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object it) {


        Chan item=(Chan) it;
        tcurr.setText(item.curr);

        tcurr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!click) {

                    Intent intent = new Intent(act, PlayerActivityTV.class);
                    intent.putExtra("curr",item.curr);
                    intent.putExtra("pos",item.pos);
                    intent.putExtra("groupid", "1");
                    act.startActivity(intent);
                    Log.v("qqws", "click");
                    click=true;
                }

            }
        });

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

}

