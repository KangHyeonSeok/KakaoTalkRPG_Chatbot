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

import com.unity3d.player.UnityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressLint("OverrideAbstract")
public class NotiListener extends NotificationListenerService {

    static Context MainContext;
    static Map<String,Notification.Action> SessionMap = new HashMap<>();
    static String PackageFilter = "com.kakao.talk";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        if (sbn.getPackageName().compareTo(PackageFilter) == 0 ) {
            Notification.WearableExtender wExt = new Notification.WearableExtender(sbn.getNotification());
            for (Notification.Action act : wExt.getActions()) if (act.getRemoteInputs() != null && act.getRemoteInputs().length > 0) if (act.title.toString()
                    .toLowerCase().contains("reply") ||
                    act.title.toString().toLowerCase().contains("Reply") ||
                    act.title.toString().toLowerCase().contains("답장")
            ) {
                MainContext = getApplicationContext();
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

        String uuid = UUID.randomUUID().toString();
        SessionMap.put(uuid, session);
        try {
            UnityPlayer.UnitySendMessage("NotiListener","OnReceive", JsonSerializer.toJson(new ToUnityArguments(uuid, room, isGroupChat, sender, msg.toString())));
        } catch (Exception e) {
            Log.e("NotiListener", e.toString());
            Log.e("NotiListener", "유니티가 실행 중이 아닌듯?");
            reply("유니티가 실행중이 아닌것 같슴다!",session);
        }
        Log.d("NotiListener", room + " / " + isGroupChat + " / " + sender + " / " + msg);
    }

    static void reply(String value, Notification.Action session) {
        if (session == null) return;
        Intent sendIntent = new Intent();
        Bundle msg = new Bundle();

        for (RemoteInput inputable : session.getRemoteInputs()) {
            msg.putCharSequence(inputable.getResultKey(), value);
        }
        RemoteInput.addResultsToIntent(session.getRemoteInputs(), sendIntent, msg);

        try {
            session.actionIntent.send(MainContext, 0, sendIntent);
        } catch (PendingIntent.CanceledException e) {
        }
    }

    public static void ReplyFromUnity(String json) {
        FromUnityArguments arguments = JsonSerializer.fromJson(json,FromUnityArguments.class);
        if(SessionMap.containsKey(arguments.Uuid)) {
            Notification.Action session = SessionMap.get(arguments.Uuid);
            if(session != null )
                reply(arguments.Msg, session);
            SessionMap.remove(arguments.Uuid);
        }
    }

    public static void GoSetting() {
        UnityPlayer.currentActivity.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }

    public static void SetPackageFilter(String filter) {
        PackageFilter = filter;
    }
}

class ToUnityArguments {
    public String Uuid;
    public String Room;
    public boolean IsGroupChat;
    public String Sender;
    public String Msg;

    public ToUnityArguments() {

    }

    public ToUnityArguments(String uuid, String room, boolean isGroup, String sender, String msg) {
        Uuid = uuid;
        Room = room;
        IsGroupChat = isGroup;
        Sender = sender;
        Msg = msg;
    }
}

class FromUnityArguments {
    public String Uuid;
    public String Msg;

    public FromUnityArguments() {

    }

    public FromUnityArguments(String uuid, String msg) {
        Uuid = uuid;
        Msg = msg;
    }
}
