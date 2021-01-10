package com.xxx.justice.view

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.xxx.justice.R
import com.xxx.justice.UTIL
import com.xxx.justice.UTIL.university
import com.xxx.justice.model.Post
import kotlinx.android.synthetic.main.activity_edit.*


class EditActivity : Activity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, university)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.adapter = adapter
        back.setOnClickListener(this)
        send.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.send -> {
                val post = Post()
                post.author = UTIL.user
                post.title = newTitle.text.toString()
                post.content = newContent.text.toString()
                post.university = spinner.selectedItem as String?
                post.isAnonymous = isAnonymous.isChecked
                post.save(object : SaveListener<String>() {
                    override fun done(objectId: String?, e: BmobException?) {
                        if (e == null) {
                            UTIL.toast(this@EditActivity, "发表成功")
                            finish()
                        } else {
                            UTIL.toast(this@EditActivity, "发表失败：" + e.message)
                        }
                    }
                })
            }
            R.id.back -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("退出此次编辑？")
            setCancelable(false)
            setPositiveButton("退出") { dialog, which ->
                super.onBackPressed()
                finish()
            }
            setNegativeButton("取消") { dialog, which ->
            }
            show()
        }
    }
}