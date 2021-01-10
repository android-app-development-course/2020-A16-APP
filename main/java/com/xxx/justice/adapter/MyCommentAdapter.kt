package com.xxx.justice.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xxx.justice.R
import com.xxx.justice.model.Comment
import com.xxx.justice.view.CommentActivity
import com.xxx.justice.view.PostActivity

class MyCommentAdapter(private val commentList: List<Comment>) :
    RecyclerView.Adapter<MyCommentAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.myTitle)
        val content: TextView = view.findViewById(R.id.myContent)
        val time: TextView = view.findViewById(R.id.myTime)
//        val floors: TextView = view.findViewById(R.id.floor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mycomment_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(context, CommentActivity::class.java)
            val comment = commentList[viewHolder.adapterPosition]
            intent.putExtra("pid", comment.postID)
            context.startActivity(intent)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = commentList[position]
        holder.title.text = comment.title
        holder.content.text = comment.content
        holder.time.text = comment.createdAt
//        holder.floors.text = comment.floor.toString()
    }
}