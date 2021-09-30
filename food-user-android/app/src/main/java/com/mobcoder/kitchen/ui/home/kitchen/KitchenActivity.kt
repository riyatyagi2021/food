package com.mobcoder.kitchen.ui.home.kitchen

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.mobcoder.kitchen.model.Vendor
import com.mobcoder.kitchen.model.food.Food
import com.mobcoder.kitchen.utils.AppConstant
import com.mobcoder.kitchen.utils.AppUtil
import com.mobcoder.kitchen.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_kitchen.*


class KitchenActivity : BaseActivity(), RootCallback<Any>, SwipeRefreshLayout.OnRefreshListener {


    private var foodAdapter: KitchenAdapter? = null
    private val viewModel: AuthViewModel by viewModels()

    var vendorId: String? = null

    private var foods: MutableList<Food>? = null

    override fun layoutRes(): Int {
        return R.layout.activity_kitchen
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        swpKt.setOnRefreshListener(this)
        setHeader("MOB's Menu")
        setAdapter()
        setObservables()


        val bundle = intent?.extras
        val vendor: Vendor? = bundle?.getSerializable(AppConstant.BK.MEDIA_DATA) as Vendor
        vendor?.let { v ->
            vendorId = v.vendorId
            tvNameVendor?.text = v.name

            if (!TextUtils.isEmpty(v.aboutUs)) {
                tvInfoVendor?.visible()
                tvInfoVendor?.text = v.aboutUs
            } else {
                tvInfoVendor?.gone()
            }

            linearCall?.setOnClickListener {
                try {
                    val call: Uri = Uri.parse("tel:${v.phoneNo}")
                    val surf = Intent(Intent.ACTION_DIAL, call)
                    startActivity(surf)
                } catch (e: Exception) {

                }
            }
        }

        if (AppUtil.isConnection()) {
            showProgressBar()
            viewModel.getFoodUser(vendorId)
        }
    }

    override fun onRefresh() {
        if (AppUtil.isConnection()) {
            viewModel.getFoodUser(vendorId)
        }
    }

    private fun setAdapter() {
        foodAdapter = KitchenAdapter()
        rvProductKt.setHasFixedSize(true) //every item of the RecyclerView has a fix size
        rvProductKt.adapter = foodAdapter
        foodAdapter?.setRootCallback(this as RootCallback<Any>)

        linearCartKt.setOnClickListener {
            val bundle = Bundle()
            bundle.putFloat(AppConstant.BK.T_PRICE, totalPrice)
            bundle.putInt(AppConstant.BK.ITME, itemCount)
            bundle.putSerializable(AppConstant.BK.FOODS, foods as java.util.ArrayList<*>?)
            launchActivityForResult(
                MyCartActivity::class.java,
                AppConstant.REQUEST_CODES.REQ_BUY_NOW_CODE,
                bundle,
                it
            )
        }
    }


    private fun setObservables() {


        viewModel.foodUserSuccess.observe(this) { data ->
            hideProgressBar()
            swpKt.isRefreshing = false
            foods = data.foodItemList

            if (foods == null || foods?.size == 0) {
                tvNoDataKt.visible()
                tvNoDataKt.text = "No food available for users"
            } else {
                tvNoDataKt.gone()
            }
            tvItemCountKt.gone()
            linearCartKt.gone()
            foodAdapter?.setData(foods)
        }

        viewModel.error.observe(this) { errors ->
            swpKt.isRefreshing = false
            hideProgressBar()
            AppUtil.showToast(errors.errorMessage)
        }
    }


    var itemCount = 0
    var totalPrice = 0.0f

    override fun onRootCallback(index: Int, data: Any, type: CallbackType, view: View) {

        when (type) {

            CallbackType.CART_ADD -> {

                itemCount = 0
                totalPrice = 0.0f

                foods?.forEach {
                    if (it.total != 0.0f) {
                        itemCount += it.quantity
                        totalPrice += it.total
                    }
                }

                tvItemCountKt.text = AppUtil.getItems(itemCount)

                if (totalPrice != 0.0f) {
                    tvItemCountKt.visible()
                    linearCartKt.visible()
                    tvTotalAmountKt.text = AppUtil.getRupee(totalPrice)
                } else {
                    tvItemCountKt.gone()
                    linearCartKt.gone()
                }
            }

            else -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.REQUEST_CODES.REQ_BUY_NOW_CODE) {
            val b = data?.extras
            val index = b?.getInt(AppConstant.BK.PAYMENT)
            if (resultCode == Activity.RESULT_OK) {
                if (index == 1 || index == 2) { // 1 for payment done, 2 for cancel, null back pressed
                    if (AppUtil.isConnection()) {
                        showProgressBar()
                        viewModel.getFoodUser(vendorId)
                    }
                }
            }
        }
    }
}