package com.example.administrator.pet_go.Me

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.administrator.pet_go.Pet.UploadPetActivity

import com.example.administrator.pet_go.R
import kotlinx.android.synthetic.main.fragment_me.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
class MeFragment(private val activity: Activity) : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(R.layout.fragment_me, container, false)
        val upload_pet = inflate.findViewById<Button>(R.id.btn_upload_pet)
        upload_pet.setOnClickListener {
            val intent = Intent()
            intent.setClass(activity,UploadPetActivity::class.java)
            startActivity(intent)
        }
        return inflate

    }


}// Required empty public constructor
