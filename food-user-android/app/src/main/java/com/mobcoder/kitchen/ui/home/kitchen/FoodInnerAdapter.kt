package com.mobcoder.kitchen.ui.home.kitchen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.model.food.Food
import com.mobcoder.kitchen.utils.AppUtil
import com.mobcoder.kitchen.utils.GlideUtils
import kotlinx.android.synthetic.main.adapter_food_inner.view.*

class FoodInnerAdapter() :

    RecyclerView.Adapter<FoodInnerAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {


        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_food_inner, parent, false)
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

    private var products: MutableList<Food>? = null
    fun setData(products: MutableList<Food>?) {
        this.products = products
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: View) :
        RecyclerView.ViewHolder(binding.rootView) {
        fun bind(data: Food?, position: Int) {

            data?.let {

                if (data.images != null && data.images?.size ?: 0 > 0) {
                    GlideUtils.loadImageFullWidth(binding.ivR, data.images?.get(0))
                }

                binding.tvPriceR.text = AppUtil.getRupee(data.price)
                binding.tvNameR.text = data.name
                binding.tvDisplayR.text = AppUtil.getRupee(data.total)
                binding.tvItemR.text = AppUtil.getItems(data.quantity)

            }
        }
    }
}