package com.mobcoder.kitchen.utils

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import com.mobcoder.kitchen.base.BaseActivity
import com.mobcoder.kitchen.callback.DialogClickListener
import org.jsoup.Jsoup

class AppUploadAsyncTask(private val appContext: Application) : AsyncTask<Void, String, String>() {

    private val CURRENT_VERSION = "Current Version"
    private val REFFERER = "http://www.google.com"
    private val USER_AGENT =
        "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
    private val PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id="
    private val EN = "&hl=en"

    private var appUpdateCallback: AppUpdateCallback? = null

    /**
     * Method to set call back
     *
     * @param appUpdateCallback type of AppUpdateCallback
     */
    fun setAppUpdateCallBack(appUpdateCallback: AppUpdateCallback?) {
        this.appUpdateCallback = appUpdateCallback
    }


    override fun doInBackground(vararg voids: Void?): String? {
        var newVersion = ""
        try {
            val document = Jsoup.connect(PLAY_STORE_LINK + appContext.packageName + EN)
                .timeout(30000)
                .userAgent(USER_AGENT)
                .referrer(REFFERER)
                .get()
            if (document != null) {
                val element = document.getElementsContainingOwnText(CURRENT_VERSION)
                for (ele in element) {
                    if (ele.siblingElements() != null) {
                        val subElements = ele.siblingElements()
                        for (subEle in subElements) {
                            newVersion = subEle.text()
                        }
                    }
                }
            }
        } catch (e: Exception) {

        }
        return newVersion
    }


    override fun onPostExecute(onlineVersion: String?) {
        super.onPostExecute(onlineVersion)
        if (onlineVersion != null && !onlineVersion.isEmpty()) {
            if (appUpdateCallback != null) {
                appUpdateCallback!!.onAppUpdated(onlineVersion)
            }
        }
    }

    /**
     * It gives the callback once new app version available on play store
     */
    interface AppUpdateCallback {
        fun onAppUpdated(onlineVersion: String?)
    }


    /**
     * Method is used to compare version of current build and last live build
     *
     * @param onlineVersion  playStore version
     * @param currentVersion local build version
     */
    fun compareVersions(onlineVersion: String, currentVersion: String): Boolean {
        val onlineVer = onlineVersion.replace(".", "").toInt()
        val offlineVer = currentVersion.replace(".", "").toInt()

        println("ZZZZ WW ${onlineVer}  ${offlineVer}")
        return onlineVer > offlineVer
    }

    fun showAppUpdateDialog(activity: BaseActivity) {
        DialogUtils.playStoreDialog(activity,
            object : DialogClickListener<Any> {
                override fun onClick(isOk: Boolean) {
                    if (isOk) {
                        try {
                            activity.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + activity.packageName)
                                )
                            )
                        } catch (ange: Exception) {
                            activity.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(PLAY_STORE_LINK + activity.packageName)
                                )
                            )
                        }
                    }
                }
            }
        )
    }
}