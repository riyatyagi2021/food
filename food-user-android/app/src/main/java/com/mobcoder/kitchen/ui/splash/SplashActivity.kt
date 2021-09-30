package com.mobcoder.kitchen.ui.splash

import android.content.Intent
import android.os.Bundle
import com.mobcoder.kitchen.BuildConfig
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.base.BaseActivity
import com.mobcoder.kitchen.ui.auth.LoginActivity
import com.mobcoder.kitchen.ui.home.kitchen.KitchenDashboardActivity
import com.mobcoder.kitchen.utils.AppConstant
import com.mobcoder.kitchen.utils.LoggerReport
import com.mobcoder.kitchen.utils.PreferenceKeeper
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {


    override fun layoutRes(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.FLAVOR.contains("prod")) {
            tvVersion?.text = "prod: ${BuildConfig.VERSION_NAME}"
        } else {
            tvVersion?.text = "dev: ${BuildConfig.VERSION_NAME}"
        }
        navigateActivity()
    }

    private fun navigateActivity() {
        GlobalScope.launch { // context of the parent, main runBlocking coroutine
            delay(AppConstant.SPLASH_DELAY)
            gotoScreen()
        }
    }

    private fun gotoScreen() {
        val isLogin = PreferenceKeeper.getInstance().isLogin
        if (isLogin) {
            startActivity(Intent(this, KitchenDashboardActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}