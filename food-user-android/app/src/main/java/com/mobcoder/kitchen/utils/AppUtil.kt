package com.mobcoder.kitchen.utils


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.base.App
import com.mobcoder.kitchen.base.App.Companion.INSTANCE
import com.mobcoder.kitchen.model.Media
import com.mobcoder.kitchen.model.api.user.UserProfile
import com.mobcoder.kitchen.ui.home.kitchen.MyOrderActivity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object AppUtil {


    /** Method to Get user data from constant if not present then find from preference @return user object **/
    fun getUser(): UserProfile? {
        var user = AppConstant.USER
        if (user?.firstName == null) {
            user = PreferenceKeeper.getInstance().getUser()
            AppConstant.USER = user
        }
        return user
    }


    fun showToast(message: String?) {
        Toast.makeText(INSTANCE, message, Toast.LENGTH_SHORT).show()
    }

    /** Method for displaying snackbar message**/
    fun showSnackBar(view: View, message: String) {
        val snackbar: Snackbar = Snackbar.make(
            view, message,
            Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }

    /** Method for preventing multiple Clicks **/
    fun preventTwoClick(view: View) {
        view.setEnabled(false)
        view.postDelayed(Runnable { view.setEnabled(true) }, 800)
    }


    fun isConnection(): Boolean {
        return isConnection(true)
    }

    fun isConnection(notShowMsg: Boolean): Boolean {
        val connectivityManager = App.INSTANCE
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo =
            Objects.requireNonNull(connectivityManager)
                .activeNetworkInfo
        val isNetwork =
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        if (!isNetwork && !notShowMsg) {
            showToast(
                App.INSTANCE.getResources()
                    .getString(R.string.msg_network_connection)
            )
        }
        return isNetwork
    }

    /** Method to Get deviceId **/
    fun getDeviceId(): String? {
        return Settings.Secure.getString(
            INSTANCE.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    /**Method for getting fileType**/
    fun getFileType(url: String?): String? {
        val path = Uri.fromFile(File(url))
        var extension = MimeTypeMap.getFileExtensionFromUrl(path.toString())
        extension = extension.toLowerCase()
        if (extension.contains(".")) {
            extension = extension.substring(extension.lastIndexOf("."))
        }
        return extension
    }

    /** method to save bitmap image to Local Storage **/
    @Synchronized
    fun saveBitmap(bitmap: Bitmap, storagePath: String?): File? {
        val file_path =
            Environment.getExternalStorageDirectory().absolutePath + "/livePoint" + File.separator + "crop"
        val dir = File(file_path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val format = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        val file = File(dir, format + "_." + "png")
        val fOut: FileOutputStream
        try {
            fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: Exception) {
        }
        return file

    }

    fun getMediaList(b: Boolean): List<Media> {
        val storageBottomList: MutableList<Media> =
            ArrayList()

        val s0 = Media()
        s0.name = "Camera"
        s0.id = 0
        storageBottomList.add(s0)

        val s1 = Media()
        s1.name = "Gallery"
        s1.id = 1
        storageBottomList.add(s1)


        return storageBottomList

    }

    fun getAppDirectory(): File? {
        // External sdcard file location
        val mediaStorageDir = File(
            Environment.getExternalStorageDirectory(),
            INSTANCE.getString(R.string.app_name)
        )
        // Create storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        return mediaStorageDir
    }


    fun getColor(color: Int): Int {
        return ContextCompat.getColor(INSTANCE, color)
    }


    fun deviceId(): String {
        return Settings.Secure.getString(
            INSTANCE.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun setBgView(layout: View, drawableId: Int) {
        layout.background = ContextCompat.getDrawable(
            INSTANCE,
            drawableId
        )
    }

    fun isValidEmail(email: String?): Boolean {
        val EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"
        val pattern = Pattern.compile(EMAIL_REGEX)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun getRupee(price: Float): String {
        return "\u20B9${price}"
    }

    fun getItems(item: Int): String {
        if (item == 1) {
            return "${item} Item"
        }
        return "${item} Items"
    }

    fun getDeviceOS(): String {
        var codeName = "UNKNOWN"
        try {
            val fields = Build.VERSION_CODES::class.java.fields

            fields.filter { it.getInt(Build.VERSION_CODES::class) == Build.VERSION.SDK_INT }
                .forEach { codeName = it.name }
        } catch (e: Exception) {

        }
        return codeName
    }

    fun getMimeType(url: String?): String {
        val path = Uri.fromFile(File(url))
        val type_map = MimeTypeMap.getSingleton()
        var extension = MimeTypeMap.getFileExtensionFromUrl(path.toString())
        extension = extension.toLowerCase(Locale.ROOT)
        if (extension.contains(".")) {
            extension = extension.substring(extension.lastIndexOf("."))
        }
        return type_map.getMimeTypeFromExtension(extension).toString()
    }

    fun getFullName(firstName: String?, lastName: String?): String {
        var name1 = "NA"
        var name2: String? = ""
        if (!TextUtils.isEmpty(firstName)) {
            name1 = "$firstName "
        }
        if (!TextUtils.isEmpty(lastName)) {
            name2 = lastName
        }
        return name1 + "" + name2
    }

    fun setBgTint(view: View?, color: Int) {
        view?.let {
            ViewCompat.setBackgroundTintList(
                it,
                ColorStateList.valueOf(color)
            )
        }
    }

    fun setTint(view: AppCompatImageView?, color: Int) {
        view?.setColorFilter(color)
    }

    fun showNotification(
        msg: String?,
        title: String
    ) {
        val notificationManager = App.INSTANCE
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_TWO_ID,
                INSTANCE.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(channel)
        }
        val builder =
            NotificationCompat.Builder(
                App.INSTANCE, CHANNEL_TWO_ID
            )

        val bundle: Bundle = Bundle()
        bundle.putString("id", "" + "");
        bundle.putString("notification_type", "")

        val backggroundIntent = Intent(App.INSTANCE, MyOrderActivity::class.java)
        backggroundIntent.putExtras(bundle)
        backggroundIntent.setFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        )

        val pendingIntent =
            PendingIntent.getActivity(INSTANCE, 0, backggroundIntent, PendingIntent.FLAG_ONE_SHOT)

        val alarmSound: Uri =
            Uri.parse("android.resource://" + INSTANCE.packageName + "/" + R.raw.alert_vendor_1);

        builder.setSmallIcon(R.mipmap.ic_launcher_kitchen)
            .setContentText(msg)
            .setContentTitle(title)
            .setColor(INSTANCE.getColor(R.color.colorPrimary))
            .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher_kitchen)
            .setTicker(
                App.INSTANCE.resources.getString(R.string.app_name)
            )
            .setSound(alarmSound)
            .setVibrate(longArrayOf(1000, 1000))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setLights(Color.RED, 3000, 3000).priority = Notification.PRIORITY_HIGH

        val notification: Notification? = builder.build()
        notificationManager.notify(1, notification)
    }

    const val CHANNEL_TWO_ID = "my_chanel"

}