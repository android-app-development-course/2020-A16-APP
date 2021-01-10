package com.xxx.justice.view

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.UpdateListener
import com.xxx.justice.R
import com.xxx.justice.UTIL
import com.xxx.justice.UTIL.toast
import com.xxx.justice.model.User
import kotlinx.android.synthetic.main.activity_change.*


class ChangeActivity : Activity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)
        val sexAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, UTIL.sex)
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        sex.adapter = sexAdapter
        val universityAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, UTIL.university)
        universityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        university.adapter = universityAdapter
        init()
        change.setOnClickListener(this)
        ok.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.change -> {
                if (!UTIL.isPhoneNumberValid(phone.text.toString())) {
                    toast(this@ChangeActivity, "请输入正确的手机号码")
                    return
                }
                val u = User()
                u.age = Integer.parseInt(age.text.toString().trim())
                u.sex = sex.selectedItem.toString()
                u.university = university.selectedItem.toString()
                u.mobilePhoneNumber = phone.text.toString()
                u.update(UTIL.user?.objectId, object : UpdateListener() {
                    override fun done(e: BmobException?) {
                        if (e == null) {
                            toast(this@ChangeActivity, "更新成功:" + u.updatedAt)
                        } else {
                            toast(this@ChangeActivity, "更新失败：" + e.message)
                        }
                    }
                })
            }
            R.id.ok -> {
                finish()
            }
        }
    }

    private fun init() {
        val bmobQuery: BmobQuery<User> = BmobQuery<User>()
        bmobQuery.getObject(UTIL.user?.objectId, object : QueryListener<User>() {
            override fun done(`object`: User, e: BmobException?) {
                if (e == null) {
                    user.text = `object`.username
                    age.setText(`object`.age.toString())
                    sex.setSelection(UTIL.sex.indexOf(`object`.sex))
                    university.setSelection(UTIL.sex.indexOf(`object`.university))
                    phone.setText(`object`.mobilePhoneNumber)
                } else {
                    toast(this@ChangeActivity, "查询失败：" + e.message)
                }
            }
        })
    }
}