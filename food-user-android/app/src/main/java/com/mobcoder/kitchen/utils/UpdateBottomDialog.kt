package com.mobcoder.kitchen.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.callback.CallbackType
import com.mobcoder.kitchen.callback.RootCallback
import kotlinx.android.synthetic.main.bottom_dialog_update.view.*

class UpdateBottomDialog :
    BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.CustomBottomSheetDialogTheme
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return LayoutInflater.from(activity)
            .inflate(R.layout.bottom_dialog_update, null, false)
    }

    override fun onViewCreated(
        view1: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view1, savedInstanceState)

        view1.linearEdit.setOnClickListener {
            rootCallback?.onRootCallback(0, data!!, CallbackType.EDIT_FOOD, it)
            dismiss()
        }
        view1.linearDelete.setOnClickListener {
            rootCallback?.onRootCallback(1, data!!, CallbackType.DELETE_FOOD, it)
            dismiss()
        }
        view1.ivCrossD.setOnClickListener { dismiss() }
    }

    private var rootCallback: RootCallback<Any>? = null
    private var data: Any? = null

    fun setRootCallback(rootCallback: RootCallback<Any>, data: Any) {
        this.rootCallback = rootCallback
        this.data = data
    }
}