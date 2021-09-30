package com.mobcoder.kitchen.base

import com.mobcoder.kitchen.model.Media


interface IBottomSheetClickListener {
    fun onBottomSheetItemClicked(
        position: Int,
        type: BottomSheetType?,
        data: Media?
    )
}