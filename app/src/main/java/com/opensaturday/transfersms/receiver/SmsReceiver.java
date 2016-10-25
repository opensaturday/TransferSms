package com.opensaturday.transfersms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.opensaturday.transfersms.database.SmsTransferDb;

public class SmsReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
			Bundle bundle = intent.getExtras();
			Object messages[] = (Object[])bundle.get("pdus");
			SmsMessage smsMessage[] = new SmsMessage[messages.length];

			for(int i = 0; i < messages.length; i++) {
			    // PDU 포맷으로 되어 있는 메시지를 복원합니다.
			    smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);
			}

			// SMS 발신 번호 확인
			String origNumber = smsMessage[0].getOriginatingAddress();

			// SMS 메시지 확인
			String message = smsMessage[0].getMessageBody().toString();
			Log.d("문자 내용", "발신자 : "+origNumber+", 내용 : " + message);


			SmsManager mSmsManager = SmsManager.getDefault();

			SmsTransferDb db = new SmsTransferDb(context);
			Cursor c = db.getSelectAll();

			while(c.moveToNext()) {
				if(c.getString(1).equals(origNumber)) {
					mSmsManager.sendTextMessage("01001234567", null, message, null, null);
				}
			}
		}
	}
	
}
