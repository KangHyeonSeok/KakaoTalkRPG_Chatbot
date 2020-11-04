package com.whyk.notilistener;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

@SuppressLint("OverrideAbstract")
public class NotiListener extends NotificationListenerService {

    Context execContext;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if (sbn.getPackageName().compareTo("com.kakao.talk") == 0 ) {
            Notification.WearableExtender wExt = new Notification.WearableExtender(sbn.getNotification());
            for (Notification.Action act : wExt.getActions()) if (act.getRemoteInputs() != null && act.getRemoteInputs().length > 0) if (act.title.toString()
                    .toLowerCase().contains("reply") ||
                    act.title.toString().toLowerCase().contains("Reply") ||
                    act.title.toString().toLowerCase().contains("답장")
            ) {
                execContext = getApplicationContext();
                callResponder(
                        sbn.getNotification().extras.getString(Notification.EXTRA_SUMMARY_TEXT),
                        sbn.getNotification().extras.getString(Notification.EXTRA_TITLE),
                        sbn.getNotification().extras.get("android.text"),
                        act
                );
            }
        }
    }


    private void callResponder(String room, String sender, Object msg, Notification.Action session ) {
        Boolean isGroupChat;
        if (room == null) {
            room = sender;
            isGroupChat = false;
        } else {
            isGroupChat = true;
        }

        Log.d("NotiListener", room + " / " + isGroupChat + " / " + sender + " / " + msg);
        reply("Test!!", session);
    }

    void reply(String value, Notification.Action session) {
        if (session == null) return;
        Intent sendIntent = new Intent();
        Bundle msg = new Bundle();

        for (RemoteInput inputable : session.getRemoteInputs()) {
            msg.putCharSequence(inputable.getResultKey(), value);
        }

        RemoteInput.addResultsToIntent(session.getRemoteInputs(), sendIntent, msg);

        try {
            session.actionIntent.send(execContext, 0, sendIntent);
        } catch (PendingIntent.CanceledException e) {
        }
    }
}
