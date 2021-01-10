package com.xxx.justice.view


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.update.BmobUpdateAgent
import com.xxx.justice.R
import com.xxx.justice.UTIL
import com.xxx.justice.UTIL.toast
import com.xxx.justice.model.User
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : Activity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        BmobUpdateAgent.setUpdateOnlyWifi(true)
        BmobUpdateAgent.update(this)
        userInfo
        login.setOnClickListener(this)
        reset.setOnClickListener(this)
        register.setOnClickListener(this)
    }

    private val userInfo: Unit
        get() {
            val sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
            et_username.setText(sp.getString("username", null))
            et_password.setText(sp.getString("password", null))
        }

    private fun saveUserInfo(username: String?, password: String?) {
        val sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.putBoolean("auto", autoLogin.isChecked)
        editor.apply()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login -> {
                val username = et_username.text.toString()
                val password = et_password.text.toString()
                if (!UTIL.isNetworkConnected(this)) {
                    toast(this@LoginActivity, "网络繁忙，请稍后重试")
                } else if (username == "" || password == "") {
                    toast(this@LoginActivity, "请输入账号和密码")
                } else {
                    val user = User()
                    user.username = username
                    user.setPassword(password)
                    user.login(object : SaveListener<Any?>() {
                        override fun done(p0: Any?, e: BmobException?) {
                            when {
                                e == null -> {
                                    toast(this@LoginActivity, "登录成功")
                                    UTIL.user = BmobUser.getCurrentUser(User::class.java)
                                    saveUserInfo(username, password)
                                    val toHome =
                                        Intent(this@LoginActivity, BaseActivity::class.java)
                                    startActivity(toHome)
                                    finish()
                                }
                                e.message == "username or password incorrect." -> {
                                    toast(this@LoginActivity, "帐号或密码错误")
                                }
                                else -> {
                                    toast(this@LoginActivity, "登录失败：${e.message}")
                                }
                            }
                        }
                    })
                }
            }
            R.id.reset -> {
                val toResetPsdActivity =
                    Intent(this@LoginActivity, ResetActivity::class.java)
                startActivity(toResetPsdActivity)
            }
            R.id.register -> {
                val toReg = Intent(
                    this@LoginActivity,
                    RegisterActivity::class.java
                )
                startActivity(toReg)
            }
        }
    }

}