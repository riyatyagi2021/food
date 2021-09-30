package com.mobcoder.kitchen.ui.home.kitchen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.callback.CallbackType
import com.mobcoder.kitchen.callback.RootCallback
import com.mobcoder.kitchen.extension.gone
import com.mobcoder.kitchen.extension.visible
import com.mobcoder.kitchen.model.food.Order
import com.mobcoder.kitchen.utils.AppConstant
import com.mobcoder.kitchen.utils.AppUtil
import com.mobcoder.kitchen.utils.DateUtils
import kotlinx.android.synthetic.main.adapter_order.view.*

class MyOrderAdapter() :
    RecyclerView.Adapter<MyOrderAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {


        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_order, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val data = orders?.get(position)
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return orders?.size ?: 0
    }

    private var rootCallback: RootCallback<Order>? = null

    fun setRootCallback(rootCallback: RootCallback<Order>) {
        this.rootCallback = rootCallback
    }

    private var orders: MutableList<Order>? = null

    fun setData(order: MutableList<Order>?) {
        this.orders = order
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: View) :
        RecyclerView.ViewHolder(binding.rootView) {
        fun bind(data: Order?, position: Int) {

            data?.let {

                binding.tvVendorNameO?.text = it.vendorDetail?.name
                binding.tvVendorPhoneO?.text = it.vendorDetail?.phoneNo

                binding.tvItemOrder.text = AppUtil.getItems(data.foods?.size ?: 0)
                binding.tvTotalPriceOrder.text = AppUtil.getRupee(data.totalPrice)
                binding.tvDateOrder.text =
                    DateUtils.setDateFormat(data.created, AppConstant.TIME_FORMAT)


                when (data.status) {

                    AppConstant.STATUS.PENDING -> {
                        binding.btnCancelOrder.visible()
                        binding.tvStatusOrder.text = "Pending"
                        binding.tvStatusOrder.setTextColor(AppUtil.getColor(R.color.color_82848e))
                    }

                    AppConstant.STATUS.ACCEPTED -> {
                        binding.btnCancelOrder.gone()
                        binding.tvStatusOrder.text = "Accepted"
                        binding.tvStatusOrder.setTextColor(AppUtil.getColor(R.color.color_fd9700))
                    }

                    AppConstant.STATUS.COMPLETED -> {
                        binding.btnCancelOrder.gone()
                        binding.tvStatusOrder.text = "Completed"
                        binding.tvStatusOrder.setTextColor(AppUtil.getColor(R.color.color_398710))
                    }

                    AppConstant.STATUS.CANCEL_BY_USER -> {
                        binding.btnCancelOrder.gone()
                        binding.tvStatusOrder.text = "Canceled by You"
                        binding.tvStatusOrder.setTextColor(AppUtil.getColor(R.color.color_6C1B03))
                    }

                    AppConstant.STATUS.CANCEL_BY_VENDOR -> {
                        binding.btnCancelOrder.gone()
                        binding.tvStatusOrder.text = "Canceled by Vendor"
                        binding.tvStatusOrder.setTextColor(AppUtil.getColor(R.color.color_6C1B03))
                    }

                }

                val adapetr =
                    FoodInnerAdapter()
                binding.rvOrderInner.setHasFixedSize(true) //every item of the RecyclerView has a fix size
                binding.rvOrderInner.adapter = adapetr
                adapetr.setData(data.foods)

                binding.btnCancelOrder.setOnClickListener {
                    rootCallback?.onRootCallback(position, data, CallbackType.ORDER_CANCEL, it)
                }

                binding.mainLayout.setOnClickListener {
                    rootCallback?.onRootCallback(position, data, CallbackType.ORDER_DETAIL, it)
                }
            }
        }
    }
}