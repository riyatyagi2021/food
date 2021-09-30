package com.mobcoder.kitchen.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.base.BaseActivity
import com.mobcoder.kitchen.ui.home.kitchen.KitchenDashboardActivity
import com.mobcoder.kitchen.utils.AppUtil
import com.mobcoder.kitchen.utils.PreferenceKeeper
import com.mobcoder.kitchen.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {


    private val viewModel: AuthViewModel by viewModels()

    override fun layoutRes(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setListener()
        setObservables()
    }

    private fun setListener() {

        showPassword()
        btnLogin.setOnClickListener {
            loginAPI()
        }

        etPassword.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginAPI()
                return@setOnEditorActionListener true
            }
            false
        }


        tvForgot.setOnClickListener {
            AppUtil.preventTwoClick(it)
            launchActivity(ForgotActivity::class.java)
        }
    }

    private fun setObservables() {

        viewModel.loginSuccess.observe(this) { data ->

            AppUtil.showToast(data?.message)
            hideProgressBar()

            PreferenceKeeper.getInstance().isLogin = true
            val useData = data?.employeeProfile
            PreferenceKeeper.getInstance().accessToken = useData?.accessToken
            PreferenceKeeper.getInstance().setUser(useData)
            launchActivity(KitchenDashboardActivity::class.java)
            finish()
        }

        viewModel.error.observe(this) { errors ->
            hideProgressBar()
            AppUtil.showToast(errors.errorMessage)
        }
    }

    private fun loginAPI() {
        if (AppUtil.isConnection()) {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (loginEmailAPI(email, password)) {
                showProgressBar()
                hideSoftKeyBoard()
                viewModel.loginAPI(email, password)
            }
        }
    }

    private fun loginEmailAPI(email: String, password: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            AppUtil.showToast("Email cannot be blank.")
            return false
        }
        if (TextUtils.isEmpty(password)) {
            AppUtil.showToast("Password cannot be blank.")
            return false
        }
        return true
    }

    private fun showPassword() {
        ivPasswordHide?.setOnClickListener {
            val t = it.tag.toString().toInt()
            if (t == 0) {
                ivPasswordHide.setImageResource(R.drawable.ic_password_show)
                etPassword.transformationMethod = null
                ivPasswordHide.tag = "1"
            } else {
                ivPasswordHide.setImageResource(R.drawable.ic_password_hide)
                etPassword.transformationMethod = PasswordTransformationMethod()
                ivPasswordHide.tag = "0"
            }
        }
    }
}