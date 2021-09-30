package com.mobcoder.kitchen.ui.home.wallet

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.base.BaseActivity
import com.mobcoder.kitchen.callback.RootCallback
import com.mobcoder.kitchen.extension.gone
import com.mobcoder.kitchen.extension.visible
import com.mobcoder.kitchen.model.Transaction
import com.mobcoder.kitchen.model.food.Order
import com.mobcoder.kitchen.ui.home.order.OrderDetailActivity
import com.mobcoder.kitchen.utils.AppConstant
import com.mobcoder.kitchen.utils.AppUtil
import com.mobcoder.kitchen.utils.DateUtils
import kotlinx.android.synthetic.main.adapter_transaction.view.*

class MyTransactionAdapter(var activity: BaseActivity) :
    RecyclerView.Adapter<MyTransactionAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {


        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_transaction, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val data = transactions?.get(position)
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return transactions?.size ?: 0
    }

    private var rootCallback: RootCallback<Order>? = null

    fun setRootCallback(rootCallback: RootCallback<Order>) {
        this.rootCallback = rootCallback
    }

    private var transactions: MutableList<Transaction>? = null

    fun setData(transactions: MutableList<Transaction>?) {
        this.transactions = transactions
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: View) :
        RecyclerView.ViewHolder(binding.rootView) {
        fun bind(data: Transaction?, position: Int) {

            /*   "amountType": 2,
               "transactionType": 3,
               "status": 1,*/

            data?.let {

                if (!TextUtils.isEmpty(data.orderId)) {
                    binding.linearPayStatus.visible()
                } else {
                    binding.linearPayStatus.gone()
                }

                binding.liCW.visible()
                binding.tvDesODID.gone()

                when (data.transactionType) {
                    // W CW
                    AppConstant.TRANSACTIONS.DAILY_COUPON_CREDIT -> {
                        binding.tvDesOD.text = "Coupon added by Mobcoder."
                    }
                    AppConstant.TRANSACTIONS.AMOUNT_CREDIT_BY_ADMIN -> {
                        binding.tvDesOD.text = "Wallet recharged."
                        binding.liCW.gone()
                    }
                    AppConstant.TRANSACTIONS.ORDER_CREATE_AMOUNT_DEBIT -> {
                        binding.tvDesOD.text = "Order placed."
                        binding.tvDesODID.text = "OrderId: ${data.orderId}"
                        binding.tvDesODID.visible()
                    }
                    AppConstant.TRANSACTIONS.DAILY_COUPON_DEBIT -> {
                        binding.tvDesOD.text = "Coupon expired."
                    }
                    AppConstant.TRANSACTIONS.AMOUNT_DEBIT_BY_ADMIN -> {
                        binding.tvDesOD.text = "Amount withdraw."
                    }
                    AppConstant.TRANSACTIONS.ORDER_CANCEL_AMOUNT_CREDIT -> {
                        binding.tvDesOD.text = "Order refund amount."
                        binding.tvDesODID.text = "OrderId: ${data.orderId}"
                        binding.tvDesODID.visible()
                    }
                    AppConstant.TRANSACTIONS.VENDOR_ORDER_CREDIT -> {
                        binding.tvDesOD.text = "Amount credited by Order."
                        binding.tvDesODID.text = "OrderId: ${data.orderId}"
                        binding.tvDesODID.visible()
                    }
                    else -> {
                    }
                }

                when (data.amountType) {
                    AppConstant.AMOUNT_TYPE.CREDIT -> {
                        binding.tvAmountWOD.setTextColor(AppUtil.getColor(R.color.color_green))
                        binding.tvAmountCWOD.setTextColor(AppUtil.getColor(R.color.color_green))

                        if (data.walletAmount == 0.0f) {
                            binding.tvAmountWOD.text = "${AppUtil.getRupee(data.walletAmount)}"
                        } else {
                            binding.tvAmountWOD.text = "+${AppUtil.getRupee(data.walletAmount)}"
                        }

                        if (data.couponWalletAmount == 0.0f) {
                            binding.tvAmountCWOD.text =
                                "${AppUtil.getRupee(data.couponWalletAmount)}"
                        } else {
                            binding.tvAmountCWOD.text =
                                "+${AppUtil.getRupee(data.couponWalletAmount)}"
                        }
                    }
                    AppConstant.AMOUNT_TYPE.DEBIT -> {
                        binding.tvAmountWOD.setTextColor(AppUtil.getColor(R.color.red))
                        binding.tvAmountCWOD.setTextColor(AppUtil.getColor(R.color.red))
                        if (data.walletAmount == 0.0f) {
                            binding.tvAmountWOD.text = "${AppUtil.getRupee(data.walletAmount)}"
                        } else {
                            binding.tvAmountWOD.text = "-${AppUtil.getRupee(data.walletAmount)}"
                        }

                        if (data.couponWalletAmount == 0.0f) {
                            binding.tvAmountCWOD.text =
                                "${AppUtil.getRupee(data.couponWalletAmount)}"
                        } else {
                            binding.tvAmountCWOD.text =
                                "-${AppUtil.getRupee(data.couponWalletAmount)}"
                        }
                    }
                    else -> {
                    }
                }

                when (data.status) {
                    AppConstant.STATUS_PAY.INITIATED -> {
                        binding.tvPaymentStatus.text = "Initiated"
                    }
                    AppConstant.STATUS_PAY.COMPLETE -> {
                        binding.tvPaymentStatus.text = "Completed"
                    }
                    AppConstant.STATUS_PAY.FAILED -> {
                        binding.tvPaymentStatus.text = "Failed"
                    }
                    else -> {
                    }
                }

                binding.tvDateOD.text =
                    DateUtils.setDateFormat(data.created, AppConstant.TIME_FORMAT)

                binding.linearTD.setOnClickListener {
                    if (!TextUtils.isEmpty(data.orderId)) {
                        val bundle = Bundle()
                        bundle.putString(AppConstant.BK.ODER_ID, data.orderId)
                        bundle.putInt(AppConstant.BK.ODER_FROM, 1)
                        activity.launchActivity(OrderDetailActivity::class.java, bundle)
                    }
                }
            }
        }
    }
}