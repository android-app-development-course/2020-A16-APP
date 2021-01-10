package com.xxx.justice.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xxx.justice.R
import com.xxx.justice.model.Comment

class CommentAdapter(private val commentList: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //        val userImage: ImageView = view.findViewById(R.id.commentImage)
        val userName: TextView = view.findViewById(R.id.userName)
        val postTime: TextView = view.findViewById(R.id.commentTime)
        val postContent: TextView = view.findViewById(R.id.commentContent)
        val floors: TextView = view.findViewById(R.id.floor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = commentList[position]
//        holder.userImage.setImageResource(R.mipmap.ic_launcher_round)
        if (comment.isAnonymous) {
            holder.userName.text = "匿名用户"
        } else {
            holder.userName.text = comment.author?.username
        }
        holder.postTime.text = comment.createdAt
        holder.postContent.text = comment.content
        holder.floors.text = comment.floor.toString()
    }
}