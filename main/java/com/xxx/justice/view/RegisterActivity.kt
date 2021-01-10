package com.xxx.justice.view


import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.xxx.justice.R
import com.xxx.justice.UTIL
import com.xxx.justice.UTIL.toast
import com.xxx.justice.model.User
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : Activity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val sexAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, UTIL.sex)
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        re_sex.adapter = sexAdapter
        val universityAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, UTIL.university)
        universityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        re_university.adapter = universityAdapter
        re_register.setOnClickListener(this)
        re_cancel.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.re_cancel -> {
                finish()
            }
            R.id.re_register -> {
                val username = re_username.text.toString()
                val password = re_password.text.toString()
                val confirm = re_confirm.text.toString()
                val phone = telephone!!.text.toString()
                if (!UTIL.isNetworkConnected(this)) {
                    toast(this@RegisterActivity, "网络繁忙，请稍后重试")
                } else if (username == "" || password == "" || confirm == "" || phone == "") {
                    toast(this@RegisterActivity, "不填完整,无法注册账号")
                } else if (confirm != password) {
                    toast(this@RegisterActivity, "两次密码输入不一致")
                } else if (!UTIL.isPhoneNumberValid(phone)) {
                    toast(this@RegisterActivity, "请输入正确的手机号码")
                } else {
                    val user = User()
                    user.username = username
                    user.setPassword(password)
                    user.age = re_age.text.toString().toInt()
                    user.role = "普通用户"
                    user.sex = re_sex.selectedItem.toString()
                    user.university = re_university.selectedItem.toString()
                    user.mobilePhoneNumber = phone
                    user.signUp(object :
                        SaveListener<User>() {
                        override fun done(
                            user: User?,
                            e: BmobException?
                        ) {
                            when {
                                e == null -> {
                                    toast(this@RegisterActivity, "注册成功")
                                }
                                e.message!!.endsWith("already taken.") -> {
                                    toast(this@RegisterActivity, "该用户名已被使用")
                                }
                                else -> {
                                    toast(this@RegisterActivity, "注册失败：" + e.message)
                                }
                            }
                        }
                    })
                }
            }
        }
    }
}