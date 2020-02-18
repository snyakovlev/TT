package ru.television.online;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBI  extends SQLiteOpenHelper implements BaseColumns {

        Context context;


        static String nameMainTable = "favorite";
        static final String tab_param1 = "name";
        static final String tab_param2 = "idd";
        static final String tab_param3 = "file";
        static final String tab_param4 = "file2";
        static final String tab_param5 = "file3";
        static final String tab_param6 = "logo";
        static final String tab_param7 = "sel";
    static final String tab_param8 = "image";

    static String nameMainTablet = "channels";
    static final String tab_param1t = "name";
    static final String tab_param2t = "idd";
    static final String tab_param3t = "file";
    static final String tab_param4t = "file2";
    static final String tab_param5t = "file3";
    static final String tab_param6t = "logo";
    static final String tab_param7t= "sel";
    static final String tab_param8t= "image";

    static String nameMainTableZ = "m3u";
    static final String tab_param1z = "id";
    static final String tab_param2z = "name";
    static final String tab_param3z = "uri";
    static final String tab_param4z = "n";


        static final String CREATE_TABLE = "CREATE TABLE " + nameMainTable + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + tab_param1 + " TEXT," + tab_param2 + " TEXT," + tab_param3 + " TEXT," + tab_param4 + " TEXT,"+ tab_param5 + " TEXT,"+ tab_param6 + " TEXT,"+ tab_param7 + " TEXT,"+ tab_param8 + " TEXT)";
    static final String CREATE_TABLET = "CREATE TABLE " + nameMainTablet + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + tab_param1t + " TEXT," + tab_param2t + " TEXT," + tab_param3t + " TEXT," + tab_param4t + " TEXT,"+ tab_param5t + " TEXT,"+ tab_param6t + " TEXT,"+ tab_param7t + " TEXT,"+ tab_param8t + " TEXT)";
    static final String CREATE_TABLEZ = "CREATE TABLE " + nameMainTableZ + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + tab_param1z + " TEXT," + tab_param2z + " TEXT," + tab_param3z + " TEXT,"+ tab_param4z + " TEXT)";

        public DBI(Context context) {

            super(context, "favorite.db", null, 17);

            this.context = context;

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
            sqLiteDatabase.execSQL(CREATE_TABLEZ);
            sqLiteDatabase.execSQL(CREATE_TABLET);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+nameMainTable);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+nameMainTableZ);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+nameMainTablet);
            sqLiteDatabase.execSQL(CREATE_TABLE);
            sqLiteDatabase.execSQL(CREATE_TABLEZ);
            sqLiteDatabase.execSQL(CREATE_TABLET);
        }

        void createTableM3U(String name_table)
        {
            SQLiteDatabase sql=this.getWritableDatabase();
            String CREATE_TABLETX = "CREATE TABLE " + name_table + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + tab_param1t + " TEXT," + tab_param2t + " TEXT," + tab_param3t + " TEXT," + tab_param4t + " TEXT,"+ tab_param5t + " TEXT,"+ tab_param6t + " TEXT,"+ tab_param7t + " TEXT,"+ tab_param8t + " TEXT)";
           sql.execSQL(CREATE_TABLETX);
        }

        List<Channel> qDB()
        {

            List<Channel> channels=new ArrayList<>();

            SQLiteDatabase sql=this.getWritableDatabase();

            Cursor c= sql.query(nameMainTable,null,null,null,null,null,null);

            int indexcolumn1 = c.getColumnIndex("name");

            int indexcolumn2 = c.getColumnIndex("idd");

            int indexcolumn3 = c.getColumnIndex("file");
            int indexcolumn4 = c.getColumnIndex("file2");
            int indexcolumn5 = c.getColumnIndex("file3");

            int indexcolumn6 = c.getColumnIndex("logo");
            int indexcolumn7 = c.getColumnIndex("sel");
            int indexcolumn8 = c.getColumnIndex("image");


            while (c.moveToNext()) {
                Channel ch = new Channel();

                ch.setName(c.getString(indexcolumn1));

                ch.setIdd(c.getString(indexcolumn2));

                ch.setUrlImage(c.getString(indexcolumn3));

                ch.setUrlImage2(c.getString(indexcolumn4));

                ch.setUrlImage3(c.getString(indexcolumn5));

                ch.setId(c.getString(indexcolumn6));

                ch.setSelect(c.getString(indexcolumn7));

                ch.setImage(c.getString(indexcolumn8));


                channels.add(ch);

            }

            sql.close();

            return channels;

        }

    ArrayList<Channel> qDBx(String name_table)
    {

       ArrayList<Channel> channels=new ArrayList<>();

        SQLiteDatabase sql=this.getWritableDatabase();

        Cursor c= sql.query(name_table,null,null,null,null,null,null);

        int indexcolumn1 = c.getColumnIndex("name");

        int indexcolumn2 = c.getColumnIndex("idd");

        int indexcolumn3 = c.getColumnIndex("file");
        int indexcolumn4 = c.getColumnIndex("file2");
        int indexcolumn5 = c.getColumnIndex("file3");

        int indexcolumn6 = c.getColumnIndex("logo");
        int indexcolumn7 = c.getColumnIndex("sel");
        int indexcolumn8 = c.getColumnIndex("image");


        while (c.moveToNext()) {
            Channel ch = new Channel();

            ch.setName(c.getString(indexcolumn1));

            ch.setIdd(c.getString(indexcolumn2));

            ch.setUrlImage(c.getString(indexcolumn3));

            ch.setUrlImage2(c.getString(indexcolumn4));

            ch.setUrlImage3(c.getString(indexcolumn5));

            ch.setId(c.getString(indexcolumn6));

            ch.setSelect(c.getString(indexcolumn7));

            ch.setImage(c.getString(indexcolumn8));


            channels.add(ch);

        }

        sql.close();

        return channels;

    }

    ArrayList<Channel> qDBt()
    {

       ArrayList<Channel> channels=new ArrayList<>();

        SQLiteDatabase sql=this.getWritableDatabase();

        Cursor c= sql.query(nameMainTablet,null,null,null,null,null,null);

        int indexcolumn1 = c.getColumnIndex("name");

        int indexcolumn2 = c.getColumnIndex("idd");

        int indexcolumn3 = c.getColumnIndex("file");
        int indexcolumn4 = c.getColumnIndex("file2");
        int indexcolumn5 = c.getColumnIndex("file3");

        int indexcolumn6 = c.getColumnIndex("logo");
        int indexcolumn7 = c.getColumnIndex("sel");
        int indexcolumn8 = c.getColumnIndex("image");


        while (c.moveToNext()) {
            Channel ch = new Channel();

            ch.setName(c.getString(indexcolumn1));

            ch.setIdd(c.getString(indexcolumn2));

            ch.setUrlImage(c.getString(indexcolumn3));

            ch.setUrlImage2(c.getString(indexcolumn4));

            ch.setUrlImage3(c.getString(indexcolumn5));

            ch.setId(c.getString(indexcolumn6));

            ch.setSelect(c.getString(indexcolumn7));

            ch.setImage(c.getString(indexcolumn8));


            channels.add(ch);

        }

        sql.close();

        return channels;

    }


   ArrayList<M3U> qDBz()
    {

        ArrayList<M3U> channels=new ArrayList<>();

        SQLiteDatabase sql=this.getWritableDatabase();

        Cursor c= sql.query(nameMainTableZ,null,null,null,null,null,null);

        int indexcolumn1 = c.getColumnIndex("id");

        int indexcolumn2 = c.getColumnIndex("name");

        int indexcolumn3 = c.getColumnIndex("uri");

        int indexcolumn4 = c.getColumnIndex("n");





        while (c.moveToNext()) {
            M3U ch = new M3U();

            ch.id=c.getString(indexcolumn1);

            ch.name=c.getString(indexcolumn2);

            ch.uri=c.getString(indexcolumn3);

            ch.n=c.getString(indexcolumn4);


            channels.add(ch);

        }

        sql.close();

        return channels;

    }



    void iDb(String name,String idd,String file,String file2,String file3,String logo,String sel,String image)
        {
            SQLiteDatabase sql=this.getWritableDatabase();

            ContentValues cv=new ContentValues();

            cv.put("name",name);

            cv.put("idd",idd);

            cv.put("file",file);

            cv.put("file2",file2);

            cv.put("file3",file3);

            cv.put("logo",logo);

            cv.put("sel",sel);

            cv.put("image",image);

            sql.insert(nameMainTable, null, cv);

            sql.close();
        }

    void iDbx(String name_table,String name,String idd,String file,String file2,String file3,String logo,String sel,String image)
    {
        SQLiteDatabase sql=this.getWritableDatabase();

        ContentValues cv=new ContentValues();

        cv.put("name",name);

        cv.put("idd",idd);

        cv.put("file",file);

        cv.put("file2",file2);

        cv.put("file3",file3);

        cv.put("logo",logo);

        cv.put("sel",sel);

        cv.put("image",image);

        sql.insert(name_table, null, cv);

        sql.close();
    }


    void iDbt(String name,String idd,String file,String file2,String file3,String logo,String sel,String image)
    {


        SQLiteDatabase sql=this.getWritableDatabase();

        ContentValues cv=new ContentValues();

        cv.put("name",name);

        cv.put("idd",idd);

        cv.put("file",file);

        cv.put("file2",file2);

        cv.put("file3",file3);

        cv.put("logo",logo);

        cv.put("sel",sel);

        cv.put("image",image);

        sql.insert(nameMainTablet, null, cv);

        sql.close();
    }

    void iDbz(String id,String name,String uri,String n)
    {
        SQLiteDatabase sql=this.getWritableDatabase();

        ContentValues cv=new ContentValues();

        cv.put("id",id);

        cv.put("name",name);

        cv.put("uri",uri);

        cv.put("n",n);



        sql.insert(nameMainTableZ, null, cv);

        sql.close();
    }

    void uDb(String name,String idd,String file,String file2,String file3,String logo,String sel,String image)
    {
        SQLiteDatabase sql=this.getWritableDatabase();

        ContentValues cv=new ContentValues();

        cv.put("idd",idd);

        cv.put("file",file);

        cv.put("file2",file2);

        cv.put("file3",file3);

        cv.put("logo",logo);

        cv.put("sel",sel);

        cv.put("image",image);

        sql.update(nameMainTable,cv,"name=?", new String[]{name});

        sql.close();
    }

    void uDbt(String name,String idd,String file,String file2,String file3,String logo,String sel)
    {
        SQLiteDatabase sql=this.getWritableDatabase();

        ContentValues cv=new ContentValues();

        cv.put("idd",idd);

        cv.put("file",file);

        cv.put("file2",file2);

        cv.put("file3",file3);

        cv.put("logo",logo);

        cv.put("sel",sel);

        sql.update(nameMainTablet,cv,"name=?", new String[]{name});

        sql.close();
    }


    void uDbz(String id,String name,String uri)
    {
        SQLiteDatabase sql=this.getWritableDatabase();

        ContentValues cv=new ContentValues();

        cv.put("id",id);

        cv.put("name",name);

        cv.put("uri",uri);



        sql.update(nameMainTableZ,cv,"id=?", new String[]{name});

        sql.close();
    }

       void dDb(String name)
       {
           SQLiteDatabase sql = this.getWritableDatabase();

           sql.delete(nameMainTable, "name=?", new String[]{name});

           sql.close();
       }

    void dDbt(String name_table)
    {
        SQLiteDatabase sql = this.getWritableDatabase();

        sql.delete(nameMainTablet, null, null);

        sql.close();
    }

    void dDbx(String name_table)
    {
        SQLiteDatabase sql = this.getWritableDatabase();

        sql.execSQL("DROP TABLE IF EXISTS "+name_table);
    }

    void dDbz(String id)
    {
        SQLiteDatabase sql = this.getWritableDatabase();

        sql.delete(nameMainTableZ, "id=?", new String[]{id});

        dDbx("table"+id);

        sql.close();
    }
    }

    class M3U
    {
        String id;
        String name;
        String uri;
        String n;
    }