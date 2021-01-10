package com.xxx.justice.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xxx.justice.R
import com.xxx.justice.UTIL.user
import kotlinx.android.synthetic.main.fragment_my.*

class MyFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name.text = user?.username
        my.setOnClickListener {
            startActivity(Intent(context, ChangeActivity::class.java))
        }
        link.setOnClickListener {
            val intent = Intent(context, ActionActivity::class.java)
            intent.putExtra("index", 0)
            startActivity(intent)
        }
        post.setOnClickListener {
            val intent = Intent(context, ActionActivity::class.java)
            intent.putExtra("index", 1)
            startActivity(intent)
        }
        re.setOnClickListener {
            val intent = Intent(context, ActionActivity::class.java)
            intent.putExtra("index", 2)
            startActivity(intent)
        }
        quit.setOnClickListener {
            user = null
            startActivity(Intent(context, LoginActivity::class.java))
            (context as Activity).finish()
        }
    }

}