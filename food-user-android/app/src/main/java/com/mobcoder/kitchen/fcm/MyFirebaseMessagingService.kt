package com.mobcoder.kitchen.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.utils.AppUtil
import com.mobcoder.kitchen.utils.PreferenceKeeper

/**
 * Show fcm msg
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage)
    }

    override fun onNewToken(token: String) {
        PreferenceKeeper.getInstance().fcmToken = token
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        Log.i("IIIIII", "DATA : $remoteMessage")
        try {
            val params = remoteMessage.data
            openAnnDetailScreen("edewf w", getString(R.string.app_name))
        } catch (e: Throwable) {
            Log.i("IIIIII", "ERROR : " + e.message)
        }
    }

    private fun openAnnDetailScreen(msg: String?, title: String) {
        AppUtil.showNotification(msg, title)
    }
}