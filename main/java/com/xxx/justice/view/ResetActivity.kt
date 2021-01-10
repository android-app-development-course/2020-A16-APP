package com.xxx.justice.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import com.xxx.justice.R
import com.xxx.justice.UTIL
import com.xxx.justice.UTIL.toast
import com.xxx.justice.model.User
import kotlinx.android.synthetic.main.activity_reset.*


class ResetActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)
        yes.setOnClickListener {
            if (tips.visibility == View.GONE) {
                tips.visibility = View.VISIBLE
                verificationCode.visibility = View.VISIBLE
                account.isEnabled = false
            } else {
                //获取验证码
                val verifycode = verificationCode.text.toString()
                if (!UTIL.isPhoneNumberValid(verifycode)) {
                    toast(baseContext, "请输入正确的手机号")
                    return@setOnClickListener
                }
                //获取账号
                val accountnumber = account.text.toString()
                //查询账户的手机号码
                val bmobQuery: BmobQuery<User> = BmobQuery<User>()
                var condition = false
                bmobQuery.doSQLQuery("select * from _User where username='$accountnumber'", object :
                    cn.bmob.v3.listener.SQLQueryListener<User>() {
                    override fun done(p0: BmobQueryResult<User>?, p1: BmobException?) {
                        val abc = "2"
                        if (p0 != null && p1 == null && p0.results != null && p0.results.size == 1 &&
                            p0.results[0].mobilePhoneNumber == verifycode
                        )
                            condition = true

                        //验证成功，跳转界面重设密码
                        if (condition) {
                            val intent = Intent(this@ResetActivity, ResetPassword::class.java)
                            intent.putExtra("objectID", p0?.results?.get(0)?.objectId);
                            startActivity(intent)
                            finish()
                        }
                        //验证失败，提示
                        else {
                            toast(baseContext, "验证失败")
                            tips.visibility = View.GONE
                            verificationCode.visibility = View.GONE
                            account.isEnabled = true
                        }
                    }
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //隐藏验证码
        tips.visibility = View.GONE
        verificationCode.visibility = View.GONE
        account.isEnabled = true
    }
}