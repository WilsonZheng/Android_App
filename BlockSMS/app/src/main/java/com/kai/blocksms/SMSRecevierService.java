package com.kai.blocksms;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;

public class SMSRecevierService extends Service {

    private SMSReceiver receiver;

    public SMSRecevierService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        receiver = new SMSReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

        filter.setPriority(Integer.MAX_VALUE);

        registerReceiver(receiver, filter);
    }

    public class SMSReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle extras = intent.getExtras();
            if (extras == null){
                return;
            }

            Object[] pdus = (Object[]) extras.get("pdus");

            for (int i = 0; i < pdus.length; i++) {

                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);


                String messageBody = message.getMessageBody();

                String fromAddress = message.getOriginatingAddress();

                BlockDB db = new BlockDB(getApplicationContext());

                boolean isBlocked = db.isNumberBlocked(getResources().getString(R.string.tag_number), fromAddress);

                if  (isBlocked) {

                    HistoryModel model = new HistoryModel(fromAddress, messageBody);
                    db.addHistory(model);
                    abortBroadcast();
                }

                isBlocked = db.isContentBlocked(getResources().getString(R.string.tag_message), messageBody);

                if  (isBlocked) {

                    HistoryModel model = new HistoryModel(fromAddress, messageBody);
                    db.addHistory(model);
                    abortBroadcast();
                }
            }
        }
    }
}
