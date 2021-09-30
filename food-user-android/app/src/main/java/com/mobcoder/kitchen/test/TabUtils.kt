package com.mobcoder.kitchen.test

import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.utils.AppUtil


object TabUtil {
    // for home tab
    @SuppressLint("InflateParams")
    fun setDashBoardSelection(
        activity: AppCompatActivity?,
        adapter: DashBoardAdapter,
        tabLayout: TabLayout,
        tabSelectedIcon: TypedArray,
        tabLabel: Array<String>
    ) {
        val bgView =
            arrayOfNulls<View>(tabLayout.tabCount)

        val tvView = arrayOfNulls<TextView>(tabLayout.tabCount)

        val ivView =
            arrayOfNulls<ImageView>(tabLayout.tabCount)

        val ivTabSelector =
            arrayOfNulls<ImageView>(tabLayout.tabCount)

        val slidingTabStrip =
            tabLayout.getChildAt(0) as ViewGroup


        for (i in 0 until tabLayout.tabCount) {
            val tab = slidingTabStrip.getChildAt(i)
            bgView[i] = tab
            val layoutParams =
                tab.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 1f
            tab.layoutParams = layoutParams

            val customTab = LayoutInflater.from(activity)
                .inflate(R.layout.layout_tab_home, null) as LinearLayout
            val ivIcon =
                customTab.findViewById<ImageView>(R.id.iv_tab_icon)
            val tvName = customTab.findViewById<TextView>(R.id.tv_tab_name)
            ivIcon.setImageResource(tabSelectedIcon.getResourceId(i, -1))
            tvName.text = tabLabel[i]
            tvView[i] = tvName
            ivView[i] = ivIcon


            val param = tab.layoutParams as ViewGroup.MarginLayoutParams

            if (i == 0) {
                tab.setPadding(0, 0, 0, 0)
                ivIcon.setImageResource(tabSelectedIcon.getResourceId(i, -1))
                tvView[i]?.setTextColor(AppUtil.getColor(R.color.color_black))
            } else {
                ivIcon.setImageResource(tabSelectedIcon.getResourceId(i, -1))
            }

            tab.setPadding(0, 0, 0, 0)
            tabLayout.getTabAt(i)?.customView = customTab

        }
    }


}