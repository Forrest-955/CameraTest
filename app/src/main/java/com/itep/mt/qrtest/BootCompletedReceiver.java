package com.itep.mt.qrtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itep.mt.qrtest.MainActivity;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
