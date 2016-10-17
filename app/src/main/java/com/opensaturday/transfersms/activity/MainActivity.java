package com.opensaturday.transfersms.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.opensaturday.transfersms.R;
import com.opensaturday.transfersms.adapter.PhoneNumberAdapter;
import com.opensaturday.transfersms.receiver.SmsReceiver;
import com.opensaturday.transfersms.util.SmsTransferDb;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private boolean mIsOn = false;
    private boolean doubleBackToExitPressedOnce = false;


    private Button mBtnOnOff;
    private Button mBtnAddPhoneNum;
    private EditText mEditPhoneNum;
    private ListView mListViewPhoneNum;
    private PhoneNumberAdapter adapter;
    private static List<BroadcastReceiver> receivers = new ArrayList<BroadcastReceiver>();

    private ArrayList phoneArray = new ArrayList();
    private boolean mIsStateOn = false;
    SmsReceiver mSmsReceiver = new SmsReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLayout();
        setFunction();

        mSmsReceiver = new SmsReceiver();
    }

    private void setLayout() {
        mBtnOnOff = (Button)findViewById(R.id.btn_onoff);
        mBtnAddPhoneNum = (Button)findViewById(R.id.btn_add);
        mEditPhoneNum = (EditText)findViewById(R.id.edit_add_number);
        mListViewPhoneNum = (ListView)findViewById(R.id.listview_number);

        mBtnOnOff.setOnClickListener(this);
        mBtnAddPhoneNum.setOnClickListener(this);
    }

    private void setFunction() {
        SmsTransferDb db = new SmsTransferDb(getApplicationContext());
        Cursor c = db.getSelectAll();

        while(c.moveToNext()) {
            phoneArray.add(c.getString(1));
        }

        adapter = new PhoneNumberAdapter(this, android.R.layout.simple_list_item_1, phoneArray);
        mListViewPhoneNum.setAdapter(adapter);
        mListViewPhoneNum.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if(!mEditPhoneNum.getText().toString().isEmpty()) {
                    adapter.add(mEditPhoneNum.getText().toString());
                    adapter.notifyDataSetChanged();
                    mEditPhoneNum.setText("");
                }
                break;
            case R.id.btn_onoff:
                if(!mIsStateOn) {
                    mIsStateOn = true;
                    mBtnOnOff.setText(getString(R.string.on));
                    IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                    intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
                    registerReceiver(mSmsReceiver, intentFilter);
                    receivers.add(mSmsReceiver);
                } else {
                    mIsStateOn = false;
                    mBtnOnOff.setText(getString(R.string.off));
                    unregisterReceiver(mSmsReceiver);
                    receivers.remove(mSmsReceiver);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public boolean isReceiverRegistered(BroadcastReceiver receiver){
        boolean registered = receivers.contains(receiver);
        return registered;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isReceiverRegistered(mSmsReceiver)) {
            unregisterReceiver(mSmsReceiver);
            receivers.remove(mSmsReceiver);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adapter.removeItem(i);
        adapter.notifyDataSetChanged();
    }
}
