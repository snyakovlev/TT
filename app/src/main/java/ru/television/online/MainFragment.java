package ru.television.online;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import android.util.Log;
import android.view.View;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BrowseFragment {
    int index;
    ArrayList<String> cht;
    private ArrayObjectAdapter mRowsAdapter;
    List<Channel> channels;

    public void init() {
        channels=helper2.parseXmlChannel(getActivity(),"1");

        PresenterSelector pr=new PresenterSelector() {
            IconHeaderItemPresenter ic=new IconHeaderItemPresenter((TvActivity2)getActivity());
            @Override
            public Presenter getPresenter(Object item) {
                return ic;
            }
        };
        cht=new ArrayList<>();

        setHeaderPresenterSelector(pr);

        new AsyncTask<Void,Void,Void>()
        {

            @Override
            protected Void doInBackground(Void... voids) {



                for (Channel c:channels)
                {

                    cht.add( getCurrTV2(getActivity(),c.getIdd()));

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                 loadRows();
            }
        }



                .execute();

         setHeadersState(HEADERS_ENABLED);

        setBrandColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.logo));



    }

    private static final String[] HEADERS = new String[]{
            "Featured", "Popular", "Editor's choice"
    };


    public MainFragment() {

        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }




    private void loadRows() {

        Log.v("iioopp","loadrows");
        // адаптер, отвечающий за ListRow (ListRow = заголовок + контент)
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        // класс отвечает за заголовок, в конструкторе указываем что это первый заголовок в списке,
        // и сам заголовок содержит текст "Заголовок 1"
        for (int i = 0; i < channels.size(); i++)
                    {




                        HeaderItem2 headerItem2 = new HeaderItem2(i, "");
                        headerItem2.channels=(ArrayList)channels;
                        headerItem2.index=i;
                     ///   HeaderItem headerItem = new HeaderItem(null);
        // наш класс, отвечающий за заполнение элементов контента
        GridItemPresenter itemPresenter = new GridItemPresenter((TvActivity2)getActivity());
        // адаптер, отвечает за отображение контента в правой части
        ArrayObjectAdapter gridAdapter = new ArrayObjectAdapter(itemPresenter);
        // добавление трех элементов контента

                        Chan ch=new Chan();
                        ch.chans=(ArrayList)channels;
                        ch.pos=i;
                        ch.curr=getCurrTV2(getActivity(),channels.get(i).getIdd());

        gridAdapter.add(0, ch);

        // в адаптер, отвечающий за ListRows, добавляем ListRow.
        // в конструктор передаем класс, отвечающий за заголовок и адаптер, отвечающий за
        // отображение списка контента
        rowsAdapter.add(new ListRow(headerItem2, gridAdapter));
    }

        setAdapter(rowsAdapter);
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


}

 class Chan
{
    int pos;
 ArrayList<Channel> chans;
 String curr;
}