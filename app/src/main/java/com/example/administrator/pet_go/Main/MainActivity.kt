package com.example.administrator.pet_go.Main

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.administrator.pet_go.Comment.MomentFragment
import com.example.administrator.pet_go.Explore.HomePageFragment
import com.example.administrator.pet_go.Me.MeFragment
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.Util.DataUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var menuItem: MenuItem? = null
    private lateinit var viewpage:ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //检测有没有消息
        async {
            val sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
            val user_id = sharedPreferences.getString("user_id", "")
            val host = "${DataUtil.host}/chatList"
            val url = "$host?uid=$user_id"
            val response = URL(url).readText()
            uiThread {
                if (response.equals("false")){//没有消息

                }else{//有消息

                }
            }
        }

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
                    menuItem?.isChecked = false
                }else{
                    bottomNavigationView.menu.getItem(0).isChecked = false
                }
                menuItem = bottomNavigationView.menu.getItem(position)
                menuItem?.isChecked = true
            }
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }



        })
        val list = arrayListOf<Fragment>()
        val fragment_explore = HomePageFragment(this)
        val fragment = MomentFragment( this)
        val fragment_me = MeFragment(this)
        list.add(fragment_explore)
        list.add(fragment)
        list.add(fragment_me)
        vp_pages.adapter = ViewPageAdapter(supportFragmentManager,list)


    }
}
