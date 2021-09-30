package com.mobcoder.kitchen.ui.home.kitchen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.callback.CallbackType
import com.mobcoder.kitchen.callback.RootCallback
import com.mobcoder.kitchen.extension.invisible
import com.mobcoder.kitchen.extension.visible
import com.mobcoder.kitchen.model.food.Food
import com.mobcoder.kitchen.utils.AppUtil
import com.mobcoder.kitchen.utils.GlideUtils
import kotlinx.android.synthetic.main.adapter_kitchen.view.*

class KitchenAdapter() :

    RecyclerView.Adapter<KitchenAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {


        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_kitchen, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val data = products?.get(position)
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return products?.size ?: 0
    }

    private var rootCallback: RootCallback<Any>? = null

    fun setRootCallback(rootCallback: RootCallback<Any>) {
        this.rootCallback = rootCallback
    }

    private var products: MutableList<Food>? = null
    fun setData(products: MutableList<Food>?) {
        this.products = products
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: View) :
        RecyclerView.ViewHolder(binding.rootView) {
        fun bind(data: Food?, position: Int) {

            data?.let {
                if (data.quantity == 0) {
                    binding.tvFoodCount.text = "0"
                } else {
                    binding.tvFoodCount.text = "${data.quantity}"
                }

                if (data.images != null && data.images?.size ?: 0 > 0) {
                    GlideUtils.loadImageFullWidth(binding.ivFood, data.images?.get(0))
                }

                binding.tvFoodPrice.text = AppUtil.getRupee(data.price)
                binding.tvDisplayPrice.text = AppUtil.getRupee(data.total)
                binding.tvFoodName.text = data.name

                if (data.total == 0.0f) {
                    binding.tvDisplayPrice.invisible()
                } else {
                    binding.tvDisplayPrice.visible()
                }

                binding.linearMinus.setOnClickListener {
                    if (data.quantity == 0) {
                        return@setOnClickListener
                    }
                    data.quantity = data.quantity - 1
                    data.total = data.price * data.quantity
                    notifyDataSetChanged()
                    rootCallback?.onRootCallback(position, data, CallbackType.CART_ADD, it)
                }

                binding.linearPlus.setOnClickListener {
                    if (data.quantity == 99) {
                        return@setOnClickListener
                    }
                    data.quantity = data.quantity + 1

                    data.total = data.price * data.quantity

                    notifyDataSetChanged()
                    rootCallback?.onRootCallback(position, data, CallbackType.CART_ADD, it)
                }
            }
        }
    }
}