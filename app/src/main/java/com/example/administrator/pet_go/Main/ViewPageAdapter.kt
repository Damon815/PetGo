package com.example.administrator.pet_go.Main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Administrator on 2018/11/24/024.
 */
class ViewPageAdapter (private val fm: FragmentManager,private val list: List<Fragment>): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {

        return list.size
    }

}