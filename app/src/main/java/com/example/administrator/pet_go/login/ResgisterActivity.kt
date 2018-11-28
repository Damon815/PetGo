package com.example.administrator.pet_go.login


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
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
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.example.administrator.pet_go.Main.MainActivity
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.Util.DataUtil
import com.example.administrator.pet_go.Util.ImageUtils
import com.example.administrator.pet_go.Util.PictureUploadUtils
import com.example.administrator.pet_go.verify.EmailVerify
import com.example.administrator.pet_go.verify.PasswordVerify
import kotlinx.android.synthetic.main.activity_resgister.*
import org.jetbrains.anko.*
import java.net.URL
import java.net.URLEncoder
import java.util.*


class ResgisterActivity : AppCompatActivity() {
    private val host = "${DataUtil.host}/register"
    private  val IMAGE_REQUEST_CODE = 0x22

    private val ablumCode = 0x22
    private val cameraCode = 0x33
    private var year = -1
    private var month = -1
    private var day = -1
    private lateinit var header_url:String
    private  var sex = ""
    private var user_id = ""
    private var password = ""
    private lateinit var time:String


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

    private fun getFromAblum() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, ablumCode)


    }

    private fun backgroupAlapha(bgalpha: Float){
        val attributes = this.window.attributes
        attributes.alpha = bgalpha
        this.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        this.window.attributes = attributes
    }
    private fun getFromCamera() {

        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)//调用android自带的照相机
        val photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, cameraCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resgister)
        val instance = Calendar.getInstance()
        year = instance.get(Calendar.YEAR)
        month = instance.get(Calendar.MONTH)
        day = instance.get(Calendar.DAY_OF_MONTH)
        header_url = "http://veng-photo.oss-cn-beijing.aliyuncs.com/18-11-24/52249892.jpg"
        //头像上传
        sv_header.setOnClickListener {

            showPopwindow()

        }

        rg_sex.setOnCheckedChangeListener { group, checkedId ->
            sex = if (checkedId == R.id.rb_male) "男" else "女"

        }

        et_date.setOnClickListener {
            val listener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val date = Date()
                val current = date.year
                time = "$year-$month-$day"
                et_date.text= time
                time = ""
                Log.d("aksdjflaskdjf++++", "$year $month $day")
            }
            val dialog = DatePickerDialog(this@ResgisterActivity, DatePickerDialog.THEME_HOLO_LIGHT, listener, year, month,day)
            dialog.show()
        }

        //注册
        btn_register.setOnClickListener {
            val email = et_email.text.toString()
            val password1 = et_password1.text.toString()
            val password2 = et_password2.text.toString()
            val name  = et_name.text.toString()
            val verify = EmailVerify()
            val state = verify.verify(email)
            if (state == EmailVerify.empty){
                toast("请输入邮箱!")
            }else if (state == EmailVerify.fail){
                toast("请输入正确的邮箱格式!")
            }else{
                //格式正确
                user_id = email

                val verify = PasswordVerify()
                val verify1 = verify.verify(password1 + "," + password2)
                if (verify1 == PasswordVerify.empty){
                    toast("密码不能为空!")

                }else if (verify1 == PasswordVerify.fail){
                    toast("两次密码不一致!")
                }else {
                    //密码一致
                    password = password1
                    if (name.isEmpty()){
                        toast("昵称不能为空!")
                    }else {

                        if (sex.isEmpty()){
                            toast("请选择性别!")
                        }else{

                            if (year<0||month<0||day<0){
                                toast("请选择出生年月！")
                            }else{
                                Toast.makeText(getContext(),"正在注册",Toast.LENGTH_SHORT).show()
                                async {
                                    val name_ = URLEncoder.encode(name,"utf-8")
                                    val sex_ = URLEncoder.encode(sex,"utf-8")
                                    val url = "$host?user_id=$user_id&password=$password&name=$name_&sex=$sex_&date=$time&head_url=$header_url"
                                    Log.d("asdjfkasjd",url)
                                    val response = URL(url).readText()
                                    uiThread {
                                        if (response.toLowerCase().equals("true")){
                                            //注册成功
                                            val intent = Intent()
                                            intent.setClass(this@ResgisterActivity,MainActivity::class.java)
                                            startActivity(intent)
                                            toast("")
                                        }else{
                                            toast("该账号已被注册!")
                                        }
                                    }
                                }


                            }
                        }
                    }
                }
            }

        }

    }






    fun  getContext():Context{
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (data == null){
            return
        }

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
                        val compreePath = ImageUtils.compressImage(path)
                        val bitmap = BitmapFactory.decodeFile(compreePath)
                        async {
                            val upload = PictureUploadUtils(getContext())
                            val url = upload.uploadPortrait(compreePath)
                            uiThread {
                                header_url = url
                                sv_header.setBitmap(bitmap)
//                                Glide.with(this@ResgisterActivity).load(url).transform(GlideCircleTransform(this@ResgisterActivity)).into(sv_header)
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
                                header_url = url
                                sv_header.setBitmap(bitmapOption)
//                                Glide.with(this@ResgisterActivity).load(url).transform(GlideCircleTransform(this@ResgisterActivity)).into(sv_header)
                            }
                        }

                    }
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
}
