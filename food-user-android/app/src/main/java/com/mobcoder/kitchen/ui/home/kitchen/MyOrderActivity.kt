package com.mobcoder.kitchen.ui.home.kitchen

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.base.BaseActivity
import com.mobcoder.kitchen.callback.CallbackType
import com.mobcoder.kitchen.callback.RootCallback
import com.mobcoder.kitchen.extension.gone
import com.mobcoder.kitchen.extension.visible
import com.mobcoder.kitchen.model.food.Order
import com.mobcoder.kitchen.ui.home.order.OrderDetailActivity
import com.mobcoder.kitchen.utils.AppConstant
import com.mobcoder.kitchen.utils.AppUtil
import com.mobcoder.kitchen.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_follow.*
import kotlinx.android.synthetic.main.include_recycler_swipe.view.*


class MyOrderActivity : BaseActivity(), RootCallback<Order>,
    SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: AuthViewModel by viewModels()
    private var myOrderAdapter: MyOrderAdapter? = null

    override fun layoutRes(): Int {
        return R.layout.activity_follow
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        includeSwp.swp.setOnRefreshListener(this)
        setHeader("My Orders")
        setAdapter()
        setObservables()
        includeSwp.tvNoData.text = "No order created from User."
    }

    private fun setAdapter() {
        myOrderAdapter = MyOrderAdapter()
        includeSwp.recyclerView.setHasFixedSize(true) //every item of the RecyclerView has a fix size
        includeSwp.recyclerView.adapter = myOrderAdapter
        myOrderAdapter?.setRootCallback(this as RootCallback<Order>)
    }

    override fun onResume() {
        super.onResume()
        if (AppUtil.isConnection()) {
            showProgressBar()
            viewModel.getUseOrders()
        }
    }

    override fun onRefresh() {
        if (AppUtil.isConnection()) {
            viewModel.getUseOrders()
        }
    }

    private fun setObservables() {
        viewModel.userOrderListSuccess.observe(this) { data ->
            includeSwp.swp.isRefreshing = false
            hideProgressBar()
            AppUtil.showToast(data.message)

            if (data.orderList == null || data.orderList?.size == 0) {
                includeSwp.tvNoData.visible()
            } else {
                includeSwp.tvNoData.gone()
            }

            myOrderAdapter?.setData(data.orderList)
        }

        viewModel.orderCancelSuccess.observe(this) { data ->
            hideProgressBar()
            includeSwp.swp.isRefreshing = false
            AppUtil.showToast(data.message)
            showProgressBar()
            onRefresh()
        }

        viewModel.error.observe(this) { errors ->
            hideProgressBar()
            includeSwp.swp.isRefreshing = false
            AppUtil.showToast(errors.errorMessage)
        }
    }

    override fun onRootCallback(index: Int, data: Order, type: CallbackType, view: View) {

        when (type) {

            CallbackType.ORDER_CANCEL -> {
                if (AppUtil.isConnection()) {
                    showProgressBar()
                    viewModel.orderCancelAPI(data?.orderId)
                }
            }

            CallbackType.ORDER_DETAIL -> {
                if (!TextUtils.isEmpty(data.orderId)) {
                    val bundle = Bundle()
                    bundle.putString(AppConstant.BK.ODER_ID, data.orderId)
                    launchActivity(OrderDetailActivity::class.java, bundle)
                }
            }
        }
    }
}