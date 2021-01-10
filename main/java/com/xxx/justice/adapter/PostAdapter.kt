package com.xxx.justice.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.xxx.justice.R
import com.xxx.justice.model.Post
import com.xxx.justice.view.CommentActivity


class PostAdapter(private val postList: List<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pid: TextView = view.findViewById(R.id.postId)
//        val userImage: ImageView = view.findViewById(R.id.postImage)
        val userName: TextView = view.findViewById(R.id.postName)
        val lastTime: TextView = view.findViewById(R.id.lastTime)
        val postTitle: TextView = view.findViewById(R.id.postTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        val viewHolder=ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(context, CommentActivity::class.java)
            val post = postList[viewHolder.adapterPosition]
            intent.putExtra("pid", post.objectId)
            context.startActivity(intent)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postList[position]
        holder.pid.text = post.objectId
//        holder.userImage.setImageResource(R.mipmap.ic_launcher_round)
        R.drawable.profile
        if (post.isAnonymous){
            holder.userName.text ="匿名用户"
        }
        else {
            holder.userName.text = post.author?.username
        }
        holder.lastTime.text = post.updatedAt
        holder.postTitle.text = post.title
    }
}
