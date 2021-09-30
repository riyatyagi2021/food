package com.mobcoder.kitchen.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobcoder.kitchen.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //setProfileNotificationFragment()

        setTab()
    }

    private fun setTab(){

        val imgSelected = resources.obtainTypedArray(R.array.home_icon_selected)

        val tabSelection = resources.getStringArray(R.array.tab_label)


        val chatFragment =  ChatFragment()
        val statusFragment =  StatusFragment()
        val callFragment =  CallFragment()

        val adapter = DashBoardAdapter(supportFragmentManager)
        adapter.addFragment(chatFragment)
        adapter.addFragment(statusFragment)
        adapter.addFragment(callFragment)

        viewPagerHome.adapter = adapter
        viewPagerHome.currentItem = 0
        viewPagerHome.offscreenPageLimit = 3
        tabHome.setupWithViewPager(viewPagerHome)



        TabUtil.setDashBoardSelection(
            this,
            adapter,
            tabHome,
            imgSelected,
            tabSelection
        )

    }

   /* private fun setProfileNotificationFragment() {

        val ftProfile = supportFragmentManager.beginTransaction()
        ftProfile.add(R.id.flProfile, ProfileFragment())
        ftProfile.commit()

        val ftNotification = supportFragmentManager.beginTransaction()
        ftNotification.add(R.id.flNotification, NotificationFragment())
        ftNotification.commit()

    }*/
}