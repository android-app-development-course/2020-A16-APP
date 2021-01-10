package com.xxx.justice.view

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.*
import com.xxx.justice.R
import com.xxx.justice.UTIL
import com.xxx.justice.UTIL.user
import com.xxx.justice.adapter.CommentAdapter
import com.xxx.justice.model.Comment
import com.xxx.justice.model.Favorite
import com.xxx.justice.model.Post
import kotlinx.android.synthetic.main.activity_comment.*


class CommentActivity : Activity(), View.OnClickListener {
    val commentList = ArrayList<Comment>()
    lateinit var postId: String
    lateinit var POST: Post
    lateinit var favId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        init()
        recyclerView.layoutManager = LinearLayoutManager(this@CommentActivity)
        recyclerView.adapter = CommentAdapter(commentList)
        exitComment.setOnClickListener(this)
        fav.setOnClickListener(this)
        commit.setOnClickListener(this)
        newComment.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                isAnonymous.visibility = View.VISIBLE
            } else {
                isAnonymous.visibility = View.GONE
            }
        }
    }

    private fun init() {
        postId = intent.getStringExtra("pid").toString()
        val bmobQuery: BmobQuery<Post> = BmobQuery()
        bmobQuery.include("author")
        bmobQuery.getObject(postId, object : QueryListener<Post>() {
            override fun done(post: Post?, ex: BmobException?) {
                if (ex == null) {
                    if (post?.isAnonymous!!) {
                        authorName.text = "匿名用户"
                    } else {
                        authorName.text = post.author?.username
                    }
                    currentU.text = post.university
                    currentTitle.text = post.title
                    currentTime.text = post.createdAt
                    currentContent.text = post.content
                    POST = post
                    val query: BmobQuery<Favorite> = BmobQuery<Favorite>()
                    query.addWhereEqualTo("authorID", user?.objectId)
                    query.addWhereEqualTo("post", POST)
                    query.findObjects(object : FindListener<Favorite?>() {
                        override fun done(
                            `object`: List<Favorite?>,
                            e: BmobException?
                        ) {
                            if (e == null && `object`.size == 1) {
                                fav.text = "已收藏"
                                favId= `object`[0]!!.objectId
                            }
                        }
                    })
                }
                update()
            }
        })

    }

    private fun update() {
        commentList.clear()
        val mainQuery: BmobQuery<Comment> = BmobQuery<Comment>()
        mainQuery.addWhereEqualTo("postID", postId)
        mainQuery.include("author")
        mainQuery.findObjects(object : FindListener<Comment?>() {
            override fun done(
                `object`: List<Comment?>,
                e: BmobException?
            ) {
                if (e == null) {
                    commentList.addAll(`object` as Collection<Comment>)
                }
                recyclerView.layoutManager = LinearLayoutManager(this@CommentActivity)
                recyclerView.adapter = CommentAdapter(commentList)
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.exitComment -> {
                finish()
            }
            R.id.fav -> {
                if (fav.text == "收藏") {
                    val favorite = Favorite()
                    favorite.post = POST
                    favorite.authorID = user?.objectId
                    favorite.save(object : SaveListener<String>() {
                        override fun done(objectId: String?, e: BmobException?) {
                            if (e == null) {
                                UTIL.toast(this@CommentActivity, "收藏成功")
                                fav.text = "已收藏"
                            } else {
                                UTIL.toast(this@CommentActivity, "收藏失败：" + e.message)
                            }
                        }
                    })
                } else {
                    val favorite = Favorite()
                    favorite.objectId = favId
                    favorite.delete(object : UpdateListener() {
                        override fun done(e: BmobException?) {
                            if (e == null) {
                                UTIL.toast(this@CommentActivity, "取消成功")
                                fav.text = "收藏"
                            } else {
                                UTIL.toast(this@CommentActivity, "取消失败：" + e.message)
                            }
                        }
                    })
                }
            }
            R.id.commit -> {
                var commentCount = 1
                val query: BmobQuery<Comment> = BmobQuery<Comment>()
                query.addWhereEqualTo("postID", postId)
                query.count(Comment::class.java, object : CountListener() {
                    override fun done(count: Int?, e: BmobException?) {
                        if (e == null) {
                            if (count != null) {
                                commentCount += count
                            }
                        } else {
                            commentCount = 1
                        }
                        val comment = Comment()
                        comment.title = currentTitle.text.toString()
                        comment.content = newComment.text.toString()
                        comment.author = user
                        comment.postID = postId
                        comment.floor = commentCount
                        comment.like = 0
                        comment.isAnonymous = isAnonymous.isChecked
                        comment.save(object : SaveListener<String>() {
                            override fun done(objectId: String?, ex: BmobException?) {
                                if (ex == null) {
                                    UTIL.toast(this@CommentActivity, "发表成功")
                                } else {
                                    UTIL.toast(this@CommentActivity, "发表失败：" + ex.message)
                                }
                                update()
                            }
                        })
                    }
                })
            }
        }
    }
}