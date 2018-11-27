package com.example.administrator.pet_go.Explore

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.Util.DataUtil
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.net.URL
import java.net.URLEncoder

@SuppressLint("ValidFragment")
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ExploreFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ExploreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExploreFragment (val context: Activity): Fragment() {

    lateinit var address:String
    lateinit var address_show: TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inflate = inflater.inflate(R.layout.fragment_explore, container, false)
        address_show = inflate.findViewById(R.id.tv_address)
        getLocation()
        if (ContextCompat.checkSelfPermission(activity?.applicationContext!!,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity!!, arrayOf("${Manifest.permission.ACCESS_COARSE_LOCATION}"),2)
        }

        return inflate
    }

    lateinit var mlocationClient: AMapLocationClient
    var i = 1
    private fun getLocation() {

        mlocationClient = AMapLocationClient(activity?.applicationContext)
        val mlocationClientOption = AMapLocationClientOption()
        mlocationClientOption.isLocationCacheEnable = true
        mlocationClientOption.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        mlocationClient.setLocationOption(mlocationClientOption)
        mlocationClient.startLocation()
        mlocationClient.setLocationListener(object : AMapLocationListener {
            override fun onLocationChanged(aMapLocation: AMapLocation?) {
                if (aMapLocation != null){
                    if (aMapLocation.errorCode == 0){
                        address = aMapLocation.address
                        val country = aMapLocation.country
                        val province = aMapLocation.province
                        val city = aMapLocation.city
                        i+=1
                        address_show.text = "您的位置是${i} :$country-$province-$city 详细地址：$address"
                        if (!country.isEmpty()){
                            mlocationClient.stopLocation()
                            async {

                                val preferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE)
                                val user_id = preferences.getString("user_id","")
                                val host = "${DataUtil.host}/address"
                                val province_ = URLEncoder.encode(province,"utf-8")
                                val city_ = URLEncoder.encode(city,"utf-8")
                                val url  = "$host?province=$province_&city=$city_&uid=$user_id"
                                val response = URL(url).readText()
                                uiThread {

                                }
                            }
                        }
                    }else{
                        Log.d("amapErrorCode","${aMapLocation.errorCode}")
                    }

                }
            }

        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2){

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mlocationClient.startLocation()
        mlocationClient.onDestroy()
    }


}// Required empty public constructor
