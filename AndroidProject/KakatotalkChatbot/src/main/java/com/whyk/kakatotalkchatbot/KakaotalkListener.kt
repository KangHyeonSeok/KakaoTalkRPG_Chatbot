package com.whyk.kakatotalkchatbot

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log


@SuppressLint("OverrideAbstract")
class KakaotalkListener() : NotificationListenerService() {
    companion object {
        var execContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ChatRPG", "onStartCommand")
        return START_STICKY
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        Log.d("ChatRPG", "onNoti")
        Log.d("ChatRPG", sbn!!.packageName)

        if (sbn!!.packageName == "com.kakao.talk") {
            val wExt = Notification.WearableExtender(
                sbn.notification
            )
            for (act in wExt.actions) if (act.remoteInputs != null && act.remoteInputs.size > 0) if (act.title.toString()
                    .toLowerCase().contains("reply") ||
                act.title.toString().toLowerCase().contains("Reply") ||
                act.title.toString().toLowerCase().contains("답장")
            ) {
                execContext = applicationContext
                callResponder(
                    sbn.notification.extras.getString(Notification.EXTRA_SUMMARY_TEXT),
                    sbn.notification.extras.getString(
                        Notification.EXTRA_TITLE
                    ),
                    sbn.notification.extras["android.text"],
                    act
                )
            }
        }
    }

    private fun callResponder(
        room: String?,
        sender: String?,
        msg: Any?,
        session: Notification.Action
    ) {
        var room: String? = room
        val isGroupChat: Boolean
        var _msg: String = msg as String
        if (room == null) {
            room = sender
            isGroupChat = false
        } else {
            isGroupChat = true
        }

        Log.d("ChatRPG", room + " / " + isGroupChat + " / " + sender + " / " + msg)
        reply("Test!!", session)
    }

    fun reply(value: String?, session: Notification.Action) {
        if (session == null) return
        val sendIntent = Intent()
        val msg = Bundle()

        for (inputable in session.getRemoteInputs()) {
            msg.putCharSequence(inputable.resultKey, value)
            Log.d("ChatRPG", inputable.resultKey)
        }

        RemoteInput.addResultsToIntent(session.getRemoteInputs(), sendIntent, msg)

        try {
            session.actionIntent.send(execContext, 0, sendIntent)
        } catch (e: PendingIntent.CanceledException) {
        }
    }
}