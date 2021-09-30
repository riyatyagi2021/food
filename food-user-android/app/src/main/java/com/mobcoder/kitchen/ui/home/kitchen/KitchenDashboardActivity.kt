package com.mobcoder.kitchen.ui.home.kitchen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.observe
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.mobcoder.kitchen.BuildConfig
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.base.App
import com.mobcoder.kitchen.base.BaseActivity
import com.mobcoder.kitchen.callback.CallbackType
import com.mobcoder.kitchen.callback.RootCallback
import com.mobcoder.kitchen.extension.gone
import com.mobcoder.kitchen.extension.visible
import com.mobcoder.kitchen.model.Vendor
import com.mobcoder.kitchen.ui.auth.LoginActivity
import com.mobcoder.kitchen.ui.home.AboutUsActivity
import com.mobcoder.kitchen.ui.home.wallet.MyWalletActivity
import com.mobcoder.kitchen.utils.*
import com.mobcoder.kitchen.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_dashboard_kitchen.*
import kotlinx.android.synthetic.main.nav_header_main.*


class KitchenDashboardActivity : BaseActivity(), RootCallback<Any>,
    SwipeRefreshLayout.OnRefreshListener, AppUploadAsyncTask.AppUpdateCallback {
    private var vendorAdapter: VendorAdapter? = null

    //var RC_REQ: Int = 100

    // Creates instance of the manager.
    private var appUpdateManager: AppUpdateManager? = null

    private val viewModel: AuthViewModel by viewModels()


    override fun layoutRes(): Int {
        return R.layout.activity_dashboard_kitchen
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        swpK.setOnRefreshListener(this)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        pbVendor?.visible()
        setNav()
        setListener()
        setAdapter()
        setObservables()

        setDeviceToken()


        if (AppUtil.isConnection()) {
            if (BuildConfig.APP_STORE == 3) {
                getAppCurrentVersion()
            }
        }
    }

    private fun setAdapter() {
        vendorAdapter = VendorAdapter()
        rvVendorK?.setHasFixedSize(true) //every item of the RecyclerView has a fix size
        rvVendorK?.adapter = vendorAdapter
        vendorAdapter?.setRootCallback(this as RootCallback<Any>)
    }


    override fun onRefresh() {
        if (AppUtil.isConnection()) {
            viewModel.getAllVendorAPI()
            viewModel.getMyProfile()
        }
    }

    override fun onResume() {
        super.onResume()
        if (AppUtil.isConnection()) {
            viewModel.getAllVendorAPI()
            viewModel.getMyProfile()
        }
    }

    private fun setListener() {

        linearRateUs.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }

        ivCrossK.setOnClickListener {
            linearRateUs.gone()
        }

        ivMenuK.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        ivCross?.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
        }

        linearMyOrder?.setOnClickListener {
            launchActivity(MyOrderActivity::class.java)
            drawer.closeDrawer(GravityCompat.START)
        }


        linearMyWallet?.setOnClickListener {
            launchActivity(MyWalletActivity::class.java)
            drawer.closeDrawer(GravityCompat.START)
        }

        linearAboutUs?.setOnClickListener {
            launchActivity(AboutUsActivity::class.java)
            drawer.closeDrawer(GravityCompat.START)
        }

        linearLogout?.setOnClickListener {
            PreferenceKeeper.getInstance().setUser(null)
            PreferenceKeeper.getInstance().accessToken = null
            PreferenceKeeper.getInstance().isLogin = false
            AppConstant.USER = null
            PreferenceKeeper.getInstance().setUser(null)
            launchActivity(LoginActivity::class.java)
            finishAffinity()
        }

    }

    private fun setNav() {
        val drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()


    }


    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setObservables() {

        viewModel.myProfileSuccess.observe(this) { data ->
            tvNameK?.text = "Hello ${data.profileData?.firstName}"

            tvMenuName?.text =
                AppUtil.getFullName(data.profileData?.firstName, data.profileData?.lastName)
            tvMenuEmail?.text = data.profileData?.email
            val c = data.profileData?.couponWalletBalance
            val w = data.profileData?.walletBalance
            tvMenuCW?.text = c?.let { AppUtil.getRupee(c) }
            tvMenuWallet?.text = w?.let { AppUtil.getRupee(w) }
            tvMenuPhone?.text = data.profileData?.phone
        }

        viewModel.vendorListSuccess.observe(this) { data ->
            pbVendor?.gone()
            swpK.isRefreshing = false
            val vendors = data.vendorList
            if (vendors == null || vendors?.size == 0) {
                tvNoData.visible()
                tvNoData.text = "No vendor available yet!"
            } else {
                tvNoData.gone()
            }
            vendorAdapter?.setData(vendors)
        }

        viewModel.error.observe(this) { errors ->
            swpK.isRefreshing = false
            hideProgressBar()
            AppUtil.showToast(errors.errorMessage)
        }
    }


    override fun onRootCallback(index: Int, data: Any, type: CallbackType, view: View) {
        when (type) {
            CallbackType.ORDER_VENDOR -> {
                val bundle = Bundle();
                bundle.putSerializable(AppConstant.BK.MEDIA_DATA, data as Vendor)
                launchActivity(KitchenActivity::class.java, bundle)
            }
            else -> {

            }
        }
    }

    /*  private fun checkUpdate() {
          LoggerReport.addLoggerFile("Kitchen Updates")
          // Returns an intent object that you use to check for an update.
          val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
          // Checks that the platform will allow the specified type of update.
          appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
              LoggerReport.addLoggerFile("Kitchen ${appUpdateInfo?.availableVersionCode()}, ${appUpdateInfo?.updateAvailability()}, ${UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS}")
              if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                  appUpdateManager?.startUpdateFlowForResult(
                      appUpdateInfo, AppUpdateType.IMMEDIATE, this,
                      RC_REQ
                  )
              }
          }
      }*/

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if (data != null) {
             if (requestCode == RC_REQ && resultCode == Activity.RESULT_OK) {
                 AppUtil.showToast("App updating start.")
             }
         }
     }*/

    private fun setDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            if (AppUtil.isConnection()) {
                val token = task.result
                Log.i("TOKEN", ":  $token")
                // viewModel.deviceTokenAPI(token)
                PreferenceKeeper.getInstance().fcmToken = token
            }
        })
    }

    private var appUpdateAsyncTask: AppUploadAsyncTask? = null
    private var currentVersion = ""

    /**
     * method get current version of application
     */
    private fun getAppCurrentVersion() {
        try {
            currentVersion =
                applicationContext.packageManager.getPackageInfo(packageName, 0).versionName
            if (appUpdateAsyncTask == null) {
                appUpdateAsyncTask = AppUploadAsyncTask(App.INSTANCE)
                appUpdateAsyncTask?.setAppUpdateCallBack(this)
                appUpdateAsyncTask?.execute()
            }

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onAppUpdated(onlineVersion: String?) {
        if (BuildConfig.APP_STORE == 3) {
            if (onlineVersion != null && onlineVersion.isNotEmpty() && !TextUtils.isEmpty(currentVersion)) {
                if (appUpdateAsyncTask?.compareVersions(onlineVersion, currentVersion) == true) {
                    appUpdateAsyncTask?.showAppUpdateDialog(this)
                }
            }
        }
    }
}