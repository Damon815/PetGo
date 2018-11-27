package com.example.administrator.pet_go.Main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.example.administrator.pet_go.Comment.MomentFragment
import com.example.administrator.pet_go.Explore.ExploreFragment
import com.example.administrator.pet_go.Me.MeFragment
import com.example.administrator.pet_go.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var menuItem: MenuItem
    private lateinit var viewpage:ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpage = vp_pages
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
            item: MenuItem ->
            menuItem = item
            when(item.itemId){
                R.id.navigation_explore ->vp_pages.currentItem = 0
                R.id.navigation_comment ->vp_pages.currentItem = 1
                R.id.navigation_account -> vp_pages.currentItem = 2
            }
            false
        })

        vp_pages.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageSelected(position: Int) {
                if (menuItem != null){
                    menuItem.isChecked = false
                }else{
                    bottomNavigationView.menu.getItem(0).isChecked = false
                }
                menuItem = bottomNavigationView.menu.getItem(position)
                menuItem.isChecked = true
            }
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }



        })
        val list = arrayListOf<Fragment>()
        val fragment_explore = ExploreFragment(this)
        val fragment = MomentFragment( this)
        val fragment_me = MeFragment(this)
        list.add(fragment_explore)
        list.add(fragment)
        list.add(fragment_me)
        vp_pages.adapter = ViewPageAdapter(supportFragmentManager,list)


    }
}
