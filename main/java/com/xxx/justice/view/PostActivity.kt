package com.xxx.justice.view

import android.app.Activity
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.xxx.justice.R
import com.xxx.justice.UTIL
import com.xxx.justice.adapter.PostAdapter
import com.xxx.justice.model.Post
import kotlinx.android.synthetic.main.activity_post.*


class PostActivity : Activity() {

    val postList = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val explode: Transition =
            TransitionInflater.from(this).inflateTransition(R.transition.explode)
        window.exitTransition = explode
        window.reenterTransition = explode
        setContentView(R.layout.activity_post)
        init()
        exitUniversity.setOnClickListener {
            finish()
        }
    }

    private fun init() {
        val university = intent.getStringExtra("university")
        UniversityName.text = university
        val mainQuery: BmobQuery<Post> = BmobQuery<Post>()
        mainQuery.addWhereEqualTo("university", university)
        mainQuery.include("author")
        mainQuery.findObjects(object : FindListener<Post?>() {
            override fun done(
                `object`: List<Post?>,
                e: BmobException?
            ) {
                if (e == null) {
                    if (`object`.isEmpty())
                        UTIL.toast(this@PostActivity, "暂时没有帖子")
                    else
                        postList.addAll(`object` as Collection<Post>)
                }
                recyclerView.layoutManager = LinearLayoutManager(this@PostActivity)
                recyclerView.adapter = PostAdapter(postList)
            }
        })
    }
}