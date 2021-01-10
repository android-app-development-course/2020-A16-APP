package com.xxx.justice.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.xxx.justice.R
import com.xxx.justice.UTIL
import com.xxx.justice.model.User


class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Bmob.initialize(this, APPID)
        setContentView(R.layout.activity_splash)

//		// 使用推送服务时的初始化操作
//		BmobInstallation.getCurrentInstallation(this).save();
//		// 启动推送服务
//		BmobPush.startWork(this, APPID);
        val sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        if (sp.getBoolean("auto", false)) {
            val user = User()
            user.username = sp.getString("username", null)
            user.setPassword(sp.getString("password", null))
            user.login(object : SaveListener<Any?>() {
                override fun done(p0: Any?, e: BmobException?) {
                    if (e == null) {
                        UTIL.user = BmobUser.getCurrentUser(User::class.java)
                        mHandler.sendEmptyMessageDelayed(GO_HOME, 1000)
                    } else {
                        UTIL.toast(this@SplashActivity, "登录失败：${e.message}")
                    }
                }
            })
        } else {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, 1000)
        }
    }

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                GO_HOME -> {
                    val intent = Intent(
                        this@SplashActivity,
                        BaseActivity::class.java
                    )
                    startActivity(intent)
                    UTIL.toast(this@SplashActivity, "登录成功")
                    finish()
                }
                GO_LOGIN -> {
                    val intent = Intent(
                        this@SplashActivity,
                        LoginActivity::class.java
                    )
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    companion object {
        private const val APPID = "20e9ad6f406bc31ab29563c3e78a7149"
        private const val GO_HOME = 100
        private const val GO_LOGIN = 200
    }
}
