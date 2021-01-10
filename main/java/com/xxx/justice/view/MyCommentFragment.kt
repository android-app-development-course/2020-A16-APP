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
import com.xxx.justice.adapter.MyCommentAdapter
import com.xxx.justice.model.Comment
import kotlinx.android.synthetic.main.fragment_mycomment.*

class MyCommentFragment : Fragment() {

    val commentList = ArrayList<Comment>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mycomment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update()
    }

    private fun update() {
        commentList.clear()
        val mainQuery: BmobQuery<Comment> = BmobQuery<Comment>()
        mainQuery.addWhereEqualTo("author", UTIL.user?.objectId)
        mainQuery.findObjects(object : FindListener<Comment?>() {
            override fun done(
                `object`: List<Comment?>,
                e: BmobException?
            ) {
                if (e == null) {
                    commentList.addAll(`object` as Collection<Comment>)
                }
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = MyCommentAdapter(commentList)
            }
        })
    }
}