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
import com.xxx.justice.adapter.PostAdapter
import com.xxx.justice.model.Favorite
import com.xxx.justice.model.Post
import kotlinx.android.synthetic.main.activity_comment.*

class MyFavoriteFragment : Fragment() {
    val postList = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_myfavorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = PostAdapter(postList)
        update()
    }

    private fun update() {
        postList.clear()
        val mainQuery: BmobQuery<Favorite> = BmobQuery<Favorite>()
        mainQuery.addWhereEqualTo("authorID", UTIL.user?.objectId)
        mainQuery.include("post")
        mainQuery.include("post.author")
        mainQuery.findObjects(object : FindListener<Favorite?>() {
            override fun done(
                `object`: List<Favorite?>,
                e: BmobException?
            ) {
                if (e == null) {
                    for (favorite in `object`)
                        favorite?.post?.let { postList.add(it) }
                }
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = PostAdapter(postList)
            }
        })
    }
}