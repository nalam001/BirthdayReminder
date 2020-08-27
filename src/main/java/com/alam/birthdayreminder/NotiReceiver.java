package com.alam.birthdayreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotiReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, NotifyIntentService.class);
        context.startService(intentService);
    }
}