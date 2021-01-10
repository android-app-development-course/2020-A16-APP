package com.xxx.justice.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.xxx.justice.R
import com.xxx.justice.UTIL
import com.xxx.justice.adapter.CommentAdapter
import com.xxx.justice.adapter.PostAdapter
import com.xxx.justice.model.Favorite
import com.xxx.justice.model.Post
import kotlinx.android.synthetic.main.fragment_mypost.*

class MyPostFragment : Fragment() {

    val postList = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mypost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = PostAdapter(postList)
        update()
    }

    private fun update() {
        postList.clear()
        val mainQuery: BmobQuery<Post> = BmobQuery<Post>()
        mainQuery.addWhereEqualTo("author", UTIL.user?.objectId)
        mainQuery.include("author")
        mainQuery.findObjects(object : FindListener<Post?>() {
            override fun done(
                `object`: List<Post?>,
                e: BmobException?
            ) {
                if (e == null) {
                    postList.addAll(`object` as Collection<Post>)
                }
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = PostAdapter(postList)
            }
        })
    }
}