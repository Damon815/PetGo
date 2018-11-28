package com.example.administrator.pet_go.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.administrator.pet_go.Main.MainActivity
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.Util.DataUtil
import com.example.administrator.pet_go.Util.SendMessageUtil
import com.example.administrator.pet_go.verify.EmailVerify
import com.example.administrator.pet_go.verify.verify
import kotlinx.android.synthetic.main.login.*
import org.jetbrains.anko.async
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.net.URL

class LoginActivity : AppCompatActivity() {

    private val login = "${DataUtil.host}/login"//验证登录接口

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val prefernces = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        val user_id = prefernces.getString("user_id","")
        Log.d("user_id",user_id)
        if (!user_id.isEmpty()){

//            toMain()
        }


        tv_register.setOnClickListener {
            //跳转到注册界面
            val intent = Intent()
            intent.setClass(this,ResgisterActivity::class.java)
            startActivity(intent)
        }

        tv_forget.setOnClickListener {
            //忘记密码

//            val data = Intent(Intent.ACTION_SEND)
//            data.type = "text/plain"
//            data.putExtra(Intent.EXTRA_SUBJECT, "邮件标题")
//            data.putExtra(Intent.EXTRA_TEXT, "邮件正文")
//            startActivity(Intent.createChooser(data, "请选择一款浏览器发邮件"))

            SendMessageUtil.sendMessage(this,"123456","18340018761")
//            val intent = Intent()
//            intent.setClass(this,MainActivity::class.java)
//            startActivity(intent)

        }

        btn_login.setOnClickListener {
            val user_id = et_user_id.text.toString()
            val password = et_user_password.text.toString()

            var verify: verify = EmailVerify()
            val verify1 = verify.verify(user_id)
            if (verify1 == EmailVerify.success){
                //格式正确
                if (!password.isEmpty()){
                    //密码不为空 请求登录

                    async {
                        val url = "$login?user_id=$user_id&password=$password"
                        val response = URL(url).readText()
                        uiThread {
                            if (response.toLowerCase().equals("true")){
                                //登录成功
                                val editor = prefernces.edit()
                                editor.putString("user_id",user_id)
                                editor.commit()
                                toMain()
                            }else{
                                //登录失败
                                toast("账号或密码不正确!")
                            }
                        }
                    }

                }else{
                    Toast.makeText(this,"密码不能为空！",Toast.LENGTH_SHORT).show()
                }
            }else if (verify1 == EmailVerify.fail){
                //请输入正确的邮箱
                Toast.makeText(this,"请输入正确的邮箱格式!",Toast.LENGTH_SHORT).show()
            }else if (verify1 == EmailVerify.empty){
                toast("账号不能为空！")
            }
        }
    }

    private fun toMain() {
        val intent = Intent()
        intent.setClass(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }
}
