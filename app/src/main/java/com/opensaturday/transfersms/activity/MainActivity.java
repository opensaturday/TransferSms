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
import com.opensaturday.transfersms.database.SmsTransferDb;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private boolean doubleBackToExitPressedOnce = false;


    private Button buttonOnOff;
    private Button buttonAddPhoneNum;
    private EditText editTextPhoneNum;
    private ListView listViewPhoneNum;
    private PhoneNumberAdapter adapter;
    private static List<BroadcastReceiver> receivers = new ArrayList<BroadcastReceiver>();

    private ArrayList phoneArray = new ArrayList();
    private boolean isStateOn = false;
    SmsReceiver smsReceiver = new SmsReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLayout();
        setFunction();

        smsReceiver = new SmsReceiver();
    }

    private void setLayout() {
        buttonOnOff = (Button)findViewById(R.id.btn_onoff);
        buttonAddPhoneNum = (Button)findViewById(R.id.btn_add);
        editTextPhoneNum = (EditText)findViewById(R.id.edit_add_number);
        listViewPhoneNum = (ListView)findViewById(R.id.listview_number);

        buttonOnOff.setOnClickListener(this);
        buttonAddPhoneNum.setOnClickListener(this);
    }

    private void setFunction() {
        SmsTransferDb db = new SmsTransferDb(getApplicationContext());
        Cursor c = db.getSelectAll();

        while(c.moveToNext()) {
            phoneArray.add(c.getString(1));
        }

        c.close();
        db.clearDb();

        adapter = new PhoneNumberAdapter(this, android.R.layout.simple_list_item_1, phoneArray);
        listViewPhoneNum.setAdapter(adapter);
        listViewPhoneNum.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if(!editTextPhoneNum.getText().toString().isEmpty()) {
                    adapter.add(editTextPhoneNum.getText().toString());
                    adapter.notifyDataSetChanged();
                    editTextPhoneNum.setText("");
                }
                break;
            case R.id.btn_onoff:
                if(!isStateOn) {
                    isStateOn = true;
                    buttonOnOff.setText(getString(R.string.on));
                    IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                    intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
                    registerReceiver(smsReceiver, intentFilter);
                    receivers.add(smsReceiver);
                } else {
                    isStateOn = false;
                    buttonOnOff .setText(getString(R.string.off));
                    unregisterReceiver(smsReceiver);
                    receivers.remove(smsReceiver);
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
        if(isReceiverRegistered(smsReceiver)) {
            unregisterReceiver(smsReceiver);
            receivers.remove(smsReceiver);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adapter.removeItem(i);
        adapter.notifyDataSetChanged();
    }
}
