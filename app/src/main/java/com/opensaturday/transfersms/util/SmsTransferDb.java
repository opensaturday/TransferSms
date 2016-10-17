package com.opensaturday.transfersms.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Kwontaejin on 2016. 10. 15..
 */

public class SmsTransferDb {

    SQLiteDatabase db;
    SmsTransferDbHelper helper;

    public SmsTransferDb(Context context) {
        helper = new SmsTransferDbHelper(context, "SmsTransfer.db", null, 1);
        db = helper.getWritableDatabase();
    }

    public void insert(String phoneNumber) {
        ContentValues values = new ContentValues();
        values.put("phone_number", phoneNumber);
        db.insert("PhoneNumberTable", null, values);
    }

    public void delete(String phoneNumber) {
        db.delete("PhoneNumberTable", "phone_number=?", new String[]{phoneNumber});
    }

    public Cursor getSelectAll() {
        return db.query("PhoneNumberTable",null,null,null,null,null,null);
    }
}
