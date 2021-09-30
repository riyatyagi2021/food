package com.mobcoder.kitchen.ui.home

import android.os.Bundle
import com.mobcoder.kitchen.BuildConfig
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.base.BaseActivity
import kotlinx.android.synthetic.main.activity_about_us.*


class AboutUsActivity : BaseActivity() {

    override fun layoutRes(): Int {
        return R.layout.activity_about_us
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHeader("About Us")
        tvVersionName?.text = "Version Name : ${BuildConfig.VERSION_NAME}"
        tvVersionCode?.text = "Version Code : ${BuildConfig.VERSION_CODE}"
    }
}