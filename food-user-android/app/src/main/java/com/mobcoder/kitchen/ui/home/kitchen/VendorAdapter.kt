package com.mobcoder.kitchen.ui.home.kitchen

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.callback.CallbackType
import com.mobcoder.kitchen.callback.RootCallback
import com.mobcoder.kitchen.model.Vendor
import com.mobcoder.kitchen.utils.GlideUtils
import kotlinx.android.synthetic.main.adapter_vendor.view.*

class VendorAdapter() :

    RecyclerView.Adapter<VendorAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {


        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_vendor, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val data = vendors?.get(position)
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return vendors?.size ?: 0
    }

    private var rootCallback: RootCallback<Any>? = null

    fun setRootCallback(rootCallback: RootCallback<Any>) {
        this.rootCallback = rootCallback
    }

    private var vendors: MutableList<Vendor>? = null

    fun setData(products: MutableList<Vendor>?) {
        this.vendors = products
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: View) :

        RecyclerView.ViewHolder(binding.rootView) {

        fun bind(data: Vendor?, position: Int) {

            data?.let { v ->

                if (v.isSelected) {
                    binding.linearVL.setBackgroundResource(R.drawable.bg_80_rect_fill)
                } else {
                    binding.linearVL.setBackgroundResource(R.drawable.bg_80_rect_strok)
                }

                if(!TextUtils.isEmpty(v.image)){
                    GlideUtils.loadImageFullWidth(binding.ivVendor, v.image)
                }else{
                    GlideUtils.loadImageFullWidth(binding.ivVendor, R.drawable.ic_vendor_image)
                }

                binding.tvVendorName.text = v.name

                binding.linearVL.setOnClickListener {

                    vendors?.forEach { v1 ->
                        v1.isSelected = false
                    }
                    v.isSelected = true
                    notifyDataSetChanged()
                }

                binding.btnOrderVendor.setOnClickListener {
                    rootCallback?.onRootCallback(position, data, CallbackType.ORDER_VENDOR, it)
                }
            }
        }
    }
}