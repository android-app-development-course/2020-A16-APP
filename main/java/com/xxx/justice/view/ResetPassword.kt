package com.xxx.justice.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.xxx.justice.R
import com.xxx.justice.UTIL
import com.xxx.justice.model.User
import kotlinx.android.synthetic.main.activity_reset_password.*


class ResetPassword : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        yesTochange.setOnClickListener {
            val pass = pass.text.toString()
            if (pass == passagain.text.toString()) {
                //修改密码
                val user = User()
                user.setPassword(pass)
                user.update(intent.getStringExtra("objectID"), object : UpdateListener() {
                    override fun done(p0: BmobException?) {
                        if (p0 == null) {
                            UTIL.toast(this@ResetPassword, "密码修改成功")
                            //返回登录
                            val intent = Intent(baseContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else
                            UTIL.toast(this@ResetPassword, "密码修改失败")
                    }
                })
            } else {
                UTIL.toast(this@ResetPassword, "两次的输入不一致")
            }
        }
    }
}