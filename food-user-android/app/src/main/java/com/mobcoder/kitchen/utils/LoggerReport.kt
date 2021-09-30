package com.mobcoder.kitchen.utils

import android.os.Environment
import com.mobcoder.kitchen.base.App
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object LoggerReport {
    private fun getFileSizeMegaBytes(file: File?): Long {
        return file?.length()?:0
    }


    fun addLoggerFile(data: String?) {
        var fos: FileOutputStream? = null
        var dos: DataOutputStream? = null
        if (myExternalFile == null) {
            createFile()
        }
        try {
            if (data != null) {
                val df: DateFormat =
                    SimpleDateFormat("dd/MMM HH:mm:ss", Locale.getDefault())
                val timeS = df.format(System.currentTimeMillis())
                fos = FileOutputStream(myExternalFile, true)
                dos = DataOutputStream(fos)
                val d = "$timeS -> $data\n"
                dos.writeBytes(d)
            }
        } catch (e: Exception) {
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (ignored: IOException) {
                }
            }
            if (dos != null) {
                try {
                    dos.close()
                } catch (ignored: IOException) {
                }
            }
        }
    }

    var myExternalFile: File? = null
    private fun createFile(): File? {
        if (App.INSTANCE != null) {
            val filename = "logger.txt"
            val filepath = "LoggerFile"
            val state = Environment.getExternalStorageState()
            val baseDir: String
            baseDir = if (Environment.MEDIA_MOUNTED == state) {
                val baseDirFile: File? = App.INSTANCE.getExternalFilesDir(filepath)
                if (baseDirFile == null) {
                    App.INSTANCE.filesDir.absolutePath
                } else {
                    baseDirFile.absolutePath
                }
            } else {
                App.INSTANCE.filesDir.absolutePath
            }
            myExternalFile = File(baseDir, filename)
        }
        return myExternalFile
    }

    private val isExternalStorageReadOnly: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState
        }

    private val isExternalStorageAvailable: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == extStorageState
        }

    fun checkAndDeleteLoggerFile() {
        val fileSize = getFileSizeMegaBytes(createFile())
        // 2MB file deletion
        if (fileSize >= 20 * 1024 * 1024) {
            if (myExternalFile != null) {
                myExternalFile!!.delete()
            }
        }
    }
}