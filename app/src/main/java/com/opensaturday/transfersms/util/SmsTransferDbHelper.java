package com.opensaturday.transfersms.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kwontaejin on 2016. 10. 15..
 */

public class SmsTransferDbHelper extends SQLiteOpenHelper {

    public SmsTransferDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table PhoneNumberTable (" +
                "_id integer primary key autoincrement, " +
                "phone_number text);";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table if exists PhoneNumberTable";
        sqLiteDatabase.execSQL(sql);

        onCreate(sqLiteDatabase);
    }
}
