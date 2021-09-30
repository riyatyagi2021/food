package com.mobcoder.kitchen.ui.auth

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.base.BaseActivity
import com.mobcoder.kitchen.utils.AppUtil
import com.mobcoder.kitchen.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_forgot.*

class ForgotActivity : BaseActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun layoutRes(): Int {
        return R.layout.activity_forgot
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnCancelF.setOnClickListener {
            finish()
        }


        btnForgot.setOnClickListener {

            if (AppUtil.isConnection()) {
                val email = etEmailF.text.toString()
                if (TextUtils.isEmpty(email)) {
                    AppUtil.showToast("Email cannot be blank.")
                } else {
                    showProgressBar()
                    viewModel.forgotAPI(email)
                }
            }
        }
        setObservables()
    }

    private fun setObservables() {

        viewModel.forgotSuccess.observe(this) { data ->
            AppUtil.showToast(data?.message)
            hideProgressBar()
            finish()
        }

        viewModel.error.observe(this) { errors ->
            hideProgressBar()
            AppUtil.showToast(errors.errorMessage)
            finish()
        }
    }


}