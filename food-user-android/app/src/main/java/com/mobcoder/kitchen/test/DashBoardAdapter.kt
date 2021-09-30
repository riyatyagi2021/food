package com.mobcoder.kitchen.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class DashBoardAdapter(manager: FragmentManager?) : FragmentStatePagerAdapter(manager!!) {
    /**
     * Contain all home tabs fragment
     */
    private val baseFragments: MutableList<Fragment> =
        ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        return baseFragments[position]
    }

    fun addFragment(baseFragment: Fragment) {
        baseFragments.add(baseFragment)
    }

    override fun getCount(): Int {
        return baseFragments.size
    }

    fun getCurrentFragment(pos: Int): Fragment {
        return baseFragments[pos]
    }
}