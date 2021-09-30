package com.mobcoder.kitchen.ui.home.kitchen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.base.BaseActivity
import com.mobcoder.kitchen.callback.DialogClickListener
import com.mobcoder.kitchen.model.api.user.FoodOrderRequest
import com.mobcoder.kitchen.model.food.Food
import com.mobcoder.kitchen.utils.AppConstant
import com.mobcoder.kitchen.utils.AppUtil
import com.mobcoder.kitchen.utils.DialogUtils
import com.mobcoder.kitchen.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_my_cart.*


class MyCartActivity : BaseActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun layoutRes(): Int {
        return R.layout.activity_my_cart
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHeader("Payment Details")
        setObservables()
        setData()
    }

    private val foodOrder: MutableList<Food> = ArrayList()
    private fun setData() {

        val bunble = intent?.extras
        val totalAmount: Float = bunble?.getFloat(AppConstant.BK.T_PRICE) ?: 0.0f
        val itemCount: Int = bunble?.getInt(AppConstant.BK.ITME) ?: 0
        val foods = bunble?.getSerializable(AppConstant.BK.FOODS) as MutableList<Food>

        tvItemCountDialog.text = AppUtil.getItems(itemCount)
        tvTotalAmountDialog.text = AppUtil.getRupee(totalAmount)


        foods.forEach {
            if (it.total != 0.0f) {
                foodOrder.add(it)
            }
        }

        val adapetr =
            FoodInnerAdapter()
        rvOrderInner1.setHasFixedSize(true) //every item of the RecyclerView has a fix size
        rvOrderInner1.adapter = adapetr
        adapetr.setData(foodOrder)

        btnCancel.setOnClickListener {

            DialogUtils.showAlert(
                this,
                getString(R.string.app_name),
                getString(R.string.cancel_payment),
                "Yes",
                "No",
                object : DialogClickListener<Any> {
                    override fun onClick(isOk: Boolean) {
                        if (isOk) {
                            val i = Intent()
                            val b = Bundle()
                            b.putInt(AppConstant.BK.PAYMENT, 2)
                            i.putExtras(b)
                            setResult(Activity.RESULT_OK, i)
                            finish()
                        }
                    }
                }
            )
        }

        btnPayment.setOnClickListener {

            if (AppUtil.isConnection()) {
                showProgressBar()
                val foodRequest = FoodOrderRequest()
                foodRequest.totalPrice = totalAmount
                foodRequest.foods = foodOrder
                viewModel.paymentBuyAPI(foodRequest)
            }
        }
    }

    private fun setObservables() {

        viewModel.paymentSuccess.observe(this) { data ->
            AppUtil.showToast(data?.message)
            hideProgressBar()
            val i = Intent()
            val b = Bundle()
            b.putInt(AppConstant.BK.PAYMENT, 1)
            i.putExtras(b)
            setResult(Activity.RESULT_OK, i)
            finish()
        }

        viewModel.error.observe(this) { errors ->
            hideProgressBar()

            if (errors?.errorCode == 31) {
                errors.errorMessage?.let {
                    DialogUtils.showAlert(this,
                        "Mob's Kitchen",
                        errors.errorMessage!!,
                        "OK",
                        object : DialogClickListener<Any> {
                            override fun onClick(isOk: Boolean) {
                                if (isOk) {
                                    val i = Intent()
                                    val b = Bundle()
                                    b.putInt(AppConstant.BK.PAYMENT, 2)
                                    i.putExtras(b)
                                    setResult(Activity.RESULT_OK, i)
                                    finish()
                                }
                            }
                        })
                }

            } else {
                AppUtil.showToast(errors.errorMessage)
                val i = Intent()
                val b = Bundle()
                b.putInt(AppConstant.BK.PAYMENT, 2)
                i.putExtras(b)
                setResult(Activity.RESULT_OK, i)
                finish()
            }
        }
    }


}