package com.example.administrator.pet_go.Pet


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.example.administrator.pet_go.JavaBean.Pet
import com.example.administrator.pet_go.Main.MainActivity
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.Util.DataUtil
import com.example.administrator.pet_go.Util.ImageUtils
import com.example.administrator.pet_go.Util.PictureUploadUtils
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_upload_pet.*
import org.jetbrains.anko.*
import java.io.File
import java.net.URL
import java.net.URLEncoder


class UploadPetActivity : AppCompatActivity() {

    private val host = "${DataUtil.host}/uploadPet"
    private val ablumCode = 0x22
    private val cameraCode = 0x33
    private lateinit var tempFile: File
    private var upload_picture:ArrayList<String> = ArrayList()

    private var types: ArrayList<String> = ArrayList<String>()
    private var varieties: ArrayList<ArrayList<String>> = ArrayList()
    private lateinit var currentImageView: ImageView
    private lateinit var nextImageView: ImageView
    private lateinit var selected_type: String
    private lateinit var selected_variety: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_pet)

        var pet_sex:String = ""

        val gson = Gson()
//        val json = gson.toJson(DataUtil.petData)
        val jsonParse = JsonParser()

        val asJsonArray = jsonParse.parse(DataUtil.petData).asJsonArray

        for (jsonElement in asJsonArray) {
            val asJsonObject = jsonElement.asJsonObject
            val type = asJsonObject.get("type").asString
            types.add(type)
            val variety = asJsonObject.get("variety")
            val v = gson.fromJson<ArrayList<String>>(variety, object : TypeToken<ArrayList<String>>(){}.type)
            varieties.add(v)
        }

        val arrayAdapter = ArrayAdapter<String>(this,R.layout.spinner_item,R.id.text)
        arrayAdapter.addAll(types)
        sp_type.adapter = arrayAdapter

        val arrayAdapter2 = ArrayAdapter<String>(this,R.layout.spinner_item,R.id.text)
        val sp_variety_view = findViewById<Spinner>(R.id.sp_variety)
        arrayAdapter2.addAll(varieties[0])
        sp_variety.adapter = arrayAdapter2
        rg_pet_sex.setOnCheckedChangeListener { group, checkedId ->
             pet_sex = when (checkedId){
                R.id.rb_pet_male -> "公"
                 else -> "母"
            }
        }


        iv_picture1.setOnClickListener {
            currentImageView = iv_picture1
            showPopwindow( )
            nextImageView = iv_picture2
        }

        iv_picture2.setOnClickListener {
            currentImageView = iv_picture2
            showPopwindow( )
            nextImageView = iv_picture3
        }

        iv_picture3.setOnClickListener {
            currentImageView = iv_picture3
            showPopwindow( )
        }

        sp_type.onItemSelectedListener {
            onItemSelected { adapterView, view, i, l ->
                selected_type = types[i]
                val arrayAdapter2 = ArrayAdapter<String>(this@UploadPetActivity,R.layout.spinner_item,R.id.text)
                arrayAdapter2.addAll(varieties[i])
                sp_variety_view.adapter = arrayAdapter2
            }
        }

        sp_variety.onItemSelectedListener {
            onItemSelected { adapterView, view, i, l ->
                selected_variety = types[i]

            }
        }

        //上传
        iv_pet_share.setOnClickListener {
            val sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
            val user_id = sharedPreferences.getString("user_id","")

            val pet_name  = et_pet_name.text.toString()
            val pet_type = selected_type
            val pet_variety = selected_variety
            val pet_age = et_pet_age.text.toString()
            val pet_describe = et_pet_share.text.toString()
            val pet = Pet()
            pet.name =  pet_name
            pet.age = pet_age
            pet.note = pet_describe
            pet.picture = upload_picture
            pet.sex = pet_sex
            pet.type = pet_type
            pet.variety = pet_variety
            pet.uid = user_id

            val gson = Gson()
            val pet_json = gson.toJson(pet)
            Log.d("pet",pet_json)
            val pet_json_ = URLEncoder.encode(pet_json,"utf-8")
            async {

                val url = "$host?pet=$pet_json_"
                val response = URL(url).readText()
                uiThread {
                    val intent = Intent()
                    intent.setClass(this@UploadPetActivity,MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun showPopwindow( ) {
        val popwindow_view = this.layoutInflater.inflate(R.layout.popwindow_upload_pet,null)
        val popwindow = PopupWindow(this)
        popwindow_view.onFocusChange { view, b ->
            if (!b){
                popwindow.dismiss()
            }
        }

        popwindow.isFocusable = true
        popwindow.width = matchParent
        popwindow.height = wrapContent
        popwindow.isOutsideTouchable  = true
        popwindow.contentView = popwindow_view
        popwindow.showAtLocation(popwindow_view, Gravity.BOTTOM, 0, 0)
        backgroupAlapha(0.5f)
        popwindow.setBackgroundDrawable(ColorDrawable(this.resources.getColor(R.color.White)))
        val album = popwindow_view.findViewById<TextView>(R.id.tv_album)
        val camera = popwindow_view.findViewById<TextView>(R.id.tv_camera)
        val cancel = popwindow_view.findViewById<TextView>(R.id.tv_cancel)
        cancel.setOnClickListener { popwindow.dismiss() }
        album.setOnClickListener { getFromAblum();popwindow.dismiss() }
        camera.setOnClickListener { getFromCamera();popwindow.dismiss() }

        popwindow.setOnDismissListener {
            val attributes = window.attributes
            attributes.alpha = 1.0f
            window.attributes = attributes
        }
    }

    private fun backgroupAlapha(bgalpha: Float){
        val attributes = this.window.attributes
        attributes.alpha = bgalpha
        this.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        this.window.attributes = attributes
    }
    private fun getFromCamera() {

        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), 1)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)//调用android自带的照相机
        val photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, cameraCode)

    }



    private fun getFromAblum() {

        ActivityCompat.requestPermissions(this,
                arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, ablumCode)


    }


    private fun getContext():Context{
        return this
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (data!=null){
            //在相册里面选择好相片之后调回到现在的这个activity中
            when (requestCode) {
                ablumCode//这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                -> if (resultCode === Activity.RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        val selectedImage = data.data //获取系统返回的照片的Uri
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null)//从系统表中查询指定Uri对应的照片
                        cursor!!.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val path = cursor.getString(columnIndex) //获取照片路径
                        cursor.close()
                        val compress_path = ImageUtils.compressImage(path)
                        val bitmap = BitmapFactory.decodeFile(compress_path)
                        currentImageView.setImageBitmap(bitmap)
                        nextImageView.visibility = View.VISIBLE
                        async {
                            val upload = PictureUploadUtils(getContext())
                            val url = upload.uploadPortrait(compress_path)
                            uiThread {
                                upload_picture.add(url)
                            }
                        }
                    } catch (e: Exception) {

                        e.printStackTrace()
                    }

                }

                cameraCode -> {

                    if (resultCode == Activity.RESULT_OK){
                        val uri = data.data
                        var photo: Bitmap? = null
                        if (uri != null){
                            photo = BitmapFactory.decodeFile(uri.path)
                        }
                        if (photo == null){
                            val extras = data.extras
                            if (extras != null){
                                photo = extras.get("data") as Bitmap
                            }else{
                                return
                            }
                        }

                        val bitmapOption = ImageUtils.bitmapOption(photo, 5)
                        currentImageView.setImageBitmap(bitmapOption)
                        nextImageView.visibility = View.VISIBLE
                        val path = ImageUtils.saveTempImage(bitmapOption)
                        async {
                            val upload = PictureUploadUtils(getContext())
                            val url = upload.uploadPortrait(path)
                            uiThread {
                                upload_picture.add(url)
                            }
                        }

                    }
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }else{
            return
        }

    }
}
