package ru.television.online;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowHeaderPresenter;

import java.io.IOException;
import java.io.InputStream;

public class IconHeaderItemPresenter extends RowHeaderPresenter {

    private float mUnselectedAlpha;
    TvActivity2 act;


    boolean click;

    public  IconHeaderItemPresenter(TvActivity2 ctx)
    {
        act=ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mUnselectedAlpha = parent.getResources()
                .getFraction(R.fraction.lb_browse_header_unselect_alpha, 1, 1);
        View v= LayoutInflater.from(act).inflate(R.layout.item_lchannel2,parent,false);
        ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        v.setLayoutParams(lp);

        return new RowHeaderPresenter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object it) {

       final   HeaderItem2 item=(HeaderItem2) ((ListRow) it).getHeaderItem();

      View root=viewHolder.view;
       root.setFocusable(true);
      final ImageView icon=root.findViewById(R.id.icon);
      final TextView item_text_channel=root.findViewById(R.id.item_text_channel);

        InputStream imageStream = null;
        try {
            // get input stream
            imageStream  = act.getAssets().open(item.channels.get(item.index).getId()+".png");
            // load image as Drawable
            Drawable drawable= Drawable.createFromStream(imageStream, null);
            // set image to ImageView
            icon.setImageDrawable(drawable);
        }
        catch(IOException ex) {
            icon.setImageDrawable(act.getResources().getDrawable(R.drawable.logo));
        }

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!click) {

                    Intent intent = new Intent(act, PlayerActivityTV.class);
                    intent.putExtra("curr",item.channels.get(item.index).getFile());
                    intent.putExtra("pos", item.index);
                    intent.putExtra("groupid", "1");
                    act.startActivity(intent);
                    Log.v("qqws", "click");
                    click=true;
                }
            }
        });

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!click) {

                    Intent intent = new Intent(act, PlayerActivityTV.class);
                    intent.putExtra("curr",item.channels.get(item.index).getFile());
                    intent.putExtra("pos", item.index);
                    intent.putExtra("groupid", "1");
                    act.startActivity(intent);
                    Log.v("qqws", "click");
                    click=true;
                }
            }
        });

        item_text_channel.setText(item.channels.get(item.index).getName());

        item_text_channel.setTextSize(act.getResources().getDimension(R.dimen.text));






    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {


        // no op
    }

    // TODO: This is a temporary fix. Remove me when leanback onCreateViewHolder no longer sets the
    // mUnselectAlpha, and also assumes the xml inflation will return a RowHeaderView.
    @Override
    protected void onSelectLevelChanged(RowHeaderPresenter.ViewHolder holder) {
       // holder.view.setAlpha(mUnselectedAlpha + holder.getSelectLevel() *
      //          (1.0f - mUnselectedAlpha));
    }
}
