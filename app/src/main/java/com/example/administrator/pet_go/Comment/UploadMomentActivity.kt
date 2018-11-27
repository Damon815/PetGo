package com.example.administrator.pet_go.Comment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.administrator.pet_go.Main.MainActivity
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.Util.DataUtil
import com.example.administrator.pet_go.Util.ImageUtils
import com.example.administrator.pet_go.Util.PictureUploadUtils
import kotlinx.android.synthetic.main.activity_upload_moment.*
import kotlinx.android.synthetic.main.fragment_item.*
import org.jetbrains.anko.async
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.net.URL
import java.net.URLEncoder

class UploadMomentActivity : AppCompatActivity() {
    private  val IMAGE_REQUEST_CODE = 0x22
    private val host = "${DataUtil.host}/uploadmoment"
    private var upload_picture = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_moment)

        //上传图片
        iv_upload_picture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
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
                IMAGE_REQUEST_CODE//这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
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
            }
            super.onActivityResult(requestCode, resultCode, data)
        }else{
            return
        }

    }
}
