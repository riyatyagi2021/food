package com.mobcoder.kitchen.ui.home.wallet

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mobcoder.kitchen.BuildConfig
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.base.BaseActivity
import com.mobcoder.kitchen.extension.gone
import com.mobcoder.kitchen.extension.visible
import com.mobcoder.kitchen.utils.AppUtil
import com.mobcoder.kitchen.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_my_wallet.*


class MyWalletActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var adapetr: MyTransactionAdapter? = null

    private val viewModel: AuthViewModel by viewModels()

    override fun layoutRes(): Int {
        return R.layout.activity_my_wallet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHeader("My Wallet")
        swp.setOnRefreshListener(this)
        setObservables()

        if (AppUtil.isConnection()) {
            showProgressBar()
            viewModel.getMyProfile()
            viewModel.getTransactionAPI()
        }

        adapetr = MyTransactionAdapter(this)
        rvTransaction.setHasFixedSize(true) //every item of the RecyclerView has a fix size
        rvTransaction.adapter = adapetr
    }

    override fun onRefresh() {
        if (AppUtil.isConnection()) {
            viewModel.getMyProfile()
            viewModel.getTransactionAPI()
        }
    }

    private fun setObservables() {

        viewModel.transactionListSuccess.observe(this) { data ->
            AppUtil.showToast(data?.message)
            hideProgressBar()
            swp.isRefreshing = false
            if (data.transactionList == null || data.transactionList?.size == 0) {
                tvNoTrans.visible()
            } else {
                tvNoTrans.gone()
            }
            adapetr?.setData(data.transactionList)
        }

        viewModel.myProfileSuccess.observe(this) { data ->
            val w = data.profileData?.walletBalance
            val c = data.profileData?.couponWalletBalance

            tvWM.text = w?.let { AppUtil.getRupee(w) }
            tvCWM.text = c?.let { AppUtil.getRupee(c) }
        }

        viewModel.error.observe(this) { errors ->
            hideProgressBar()
            swp.isRefreshing = false
            AppUtil.showToast(errors.errorMessage)
        }
    }
}