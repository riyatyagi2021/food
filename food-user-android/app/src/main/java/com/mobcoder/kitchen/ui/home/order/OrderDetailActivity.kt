package com.mobcoder.kitchen.ui.home.order

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.base.BaseActivity
import com.mobcoder.kitchen.model.food.Food
import com.mobcoder.kitchen.ui.home.kitchen.FoodInnerAdapter
import com.mobcoder.kitchen.utils.AppConstant
import com.mobcoder.kitchen.utils.AppUtil
import com.mobcoder.kitchen.utils.DateUtils
import com.mobcoder.kitchen.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_order_detail.*


class OrderDetailActivity : BaseActivity() {

    private var adapetr: FoodInnerAdapter? = null
    private var orderFrom: Int = 0
    private val viewModel: AuthViewModel by viewModels()

    override fun layoutRes(): Int {
        return R.layout.activity_order_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHeader("Order Detail")
        setObservables()
        setData()
    }

    private val foodOrder: MutableList<Food> = ArrayList()
    private fun setData() {

        val bundle = intent?.extras
        val orderId: String? = bundle?.getString(AppConstant.BK.ODER_ID)
        orderFrom = bundle?.getInt(AppConstant.BK.ODER_FROM) ?: 0
        adapetr = FoodInnerAdapter()
        rvOrderInnerOD.setHasFixedSize(true) //every item of the RecyclerView has a fix size
        rvOrderInnerOD.adapter = adapetr

        if (AppUtil.isConnection()) {
            showProgressBar()
            viewModel.getOrderDetail(orderId!!)
        }
    }

    private fun setObservables() {

        viewModel.orderDetailSuccess.observe(this) { data ->
            AppUtil.showToast(data?.message)
            hideProgressBar()
            data.orderDetail?.let {

                if (orderFrom == 1) {
                    tvDl?.text = "Delivered on"
                } else {
                    tvDl?.text = "Request pending"
                }

                tvOrderIdOD.text = it.orderId
                tvUserNameOD.text =
                    AppUtil.getFullName(it.employeeName?.firstName, it.employeeName?.lastName)

                tvPhoneNumberOD.text = it.employeePhone

                tvItemOD.text = AppUtil.getItems(it.foods?.size ?: 0)

                tvTotalAmountOD.text = AppUtil.getRupee(it.totalPrice)
                tvDateOD.text =
                    DateUtils.setDateFormat(it.created, AppConstant.TIME_FORMAT)


                when (it.status) {

                    AppConstant.STATUS.PENDING -> {
                        tvStatusRequestOD.text = "Pending"
                        tvStatusRequestOD.setTextColor(AppUtil.getColor(R.color.color_82848e))
                    }

                    AppConstant.STATUS.ACCEPTED -> {
                        tvStatusRequestOD.text = "Accepted"
                        tvStatusRequestOD.setTextColor(AppUtil.getColor(R.color.color_fd9700))
                    }

                    AppConstant.STATUS.COMPLETED -> {
                        tvStatusRequestOD.text = "Completed"
                        tvStatusRequestOD.setTextColor(AppUtil.getColor(R.color.color_398710))
                    }

                    AppConstant.STATUS.CANCEL_BY_VENDOR -> {
                        tvStatusRequestOD.text = "Canceled by You"
                        tvStatusRequestOD.setTextColor(AppUtil.getColor(R.color.color_6C1B03))
                    }

                    AppConstant.STATUS.CANCEL_BY_USER -> {
                        tvStatusRequestOD.text = "Canceled by User"
                        tvStatusRequestOD.setTextColor(AppUtil.getColor(R.color.color_6C1B03))
                    }


                }

                val adapetr =
                    FoodInnerAdapter()
                rvOrderInnerOD.setHasFixedSize(true) //every item of the RecyclerView has a fix size
                rvOrderInnerOD.adapter = adapetr
                adapetr.setData(it.foods)

            }

        }
        viewModel.error.observe(this) { errors ->
            hideProgressBar()
            AppUtil.showToast(errors.errorMessage)

        }
    }
}