package com.example.administrator.pet_go.Comment

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
import android.view.Gravity
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import com.example.administrator.pet_go.Main.MainActivity
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.Util.DataUtil
import com.example.administrator.pet_go.Util.ImageUtils
import com.example.administrator.pet_go.Util.PictureUploadUtils
import kotlinx.android.synthetic.main.activity_upload_moment.*
import org.jetbrains.anko.*
import java.net.URL
import java.net.URLEncoder

class UploadMomentActivity : AppCompatActivity() {
    private  val IMAGE_REQUEST_CODE = 0x22
    private val host = "${DataUtil.host}/uploadmoment"
    private var upload_picture = ""

    private val ablumCode = 0x22
    private val cameraCode = 0x33
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_moment)

        //上传图片
        iv_upload_picture.setOnClickListener {


            showPopwindow()
        }

        //返回


        //发表
        tv_share.setOnClickListener {
            val content = input_content.text.toString()
            if(content.isEmpty()){
                toast("请分享此刻的感受吧!")
            }else{
                val preferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
                val user_id = preferences.getString("user_id","")
                if (!user_id.isEmpty()&&!upload_picture.isEmpty()){
                    //
                    async {
                        val content_ = URLEncoder.encode(content,"utf-8")
                        val url = "$host?user_id=$user_id&content=$content_&picture=$upload_picture"
                        val response = URL(url).readText()
                        uiThread {
                            if (response.toLowerCase().equals("true")){
                                val intent = Intent()
                                intent.setClass(this@UploadMomentActivity,MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }else{
                    toast("保存的账号为空!")
                }
            }
        }



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
                        val cursor = contentResolver.query(selectedImage,
                                filePathColumn, null, null, null)//从系统表中查询指定Uri对应的照片
                        cursor!!.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val path = cursor.getString(columnIndex) //获取照片路径
                        cursor.close()
                        val compress_path = ImageUtils.compressImage(path)
                        val bitmap = BitmapFactory.decodeFile(compress_path)
                        iv_upload_picture.setImageBitmap(bitmap)
                        async {
                            val upload = PictureUploadUtils(getContext())
                            val url = upload.uploadPortrait(compress_path)
                            uiThread {
                                upload_picture = url
                            }
                        }
                    } catch (e: Exception) {

                        e.printStackTrace()
                    }

                }
                cameraCode->{
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
                        val path = ImageUtils.saveTempImage(bitmapOption)
                        async {
                            val upload = PictureUploadUtils(getContext())
                            val url = upload.uploadPortrait(path)
                            uiThread {
                                upload_picture = url
                                iv_upload_picture.setImageBitmap(bitmapOption)
//                                Glide.with(this@ResgisterActivity).load(url).transform(GlideCircleTransform(this@ResgisterActivity)).into(sv_header)
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
