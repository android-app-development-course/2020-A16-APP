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
import com.xxx.justice.adapter.BannerImageAdapter
import com.xxx.justice.adapter.PostAdapter
import com.xxx.justice.model.HotPost
import com.xxx.justice.model.Post
import com.xxx.justice.model.Swiper
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private val hotPostList = ArrayList<Post>()

    private var imageUrls = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwiper()
        initHot()
        hot.layoutManager = LinearLayoutManager(context)
        hot.adapter = PostAdapter(hotPostList)
    }

    private fun initSwiper() {
        val query: BmobQuery<Swiper> = BmobQuery<Swiper>()
        query.findObjects(object : FindListener<Swiper?>() {
            override fun done(
                `object`: List<Swiper?>,
                e: BmobException?
            ) {
                if (e == null) {
                    for (i in `object` as Collection<Swiper>)
                        i.imageURL?.let { imageUrls.add(it) }
                }
                val adapter = BannerImageAdapter(imageUrls)
                banner?.let {
                    it.addBannerLifecycleObserver(this@HomeFragment)
                    it.indicator = CircleIndicator(context)
                    it.setBannerRound(20f)
                    it.adapter = adapter
                }
            }
        })
    }

    private fun initHot() {
        val query: BmobQuery<HotPost> = BmobQuery<HotPost>()
        query.findObjects(object : FindListener<HotPost?>() {
            override fun done(
                `object`: List<HotPost?>,
                e: BmobException?
            ) {
                if (e == null) {
                    if (`object`.isEmpty()) {
                        noHot.visibility = View.VISIBLE
                        hot.visibility = View.GONE
                        return
                    } else {
                        noHot.visibility = View.GONE
                        hot.visibility = View.VISIBLE
                    }
                    var eq: BmobQuery<Post>? = null
                    val queries: MutableList<BmobQuery<Post>> =
                        ArrayList<BmobQuery<Post>>()
                    for (i in `object`) {
                        eq = BmobQuery<Post>()
                        eq.addWhereEqualTo("objectId", i?.postID)
                        queries.add(eq)
                    }
                    val mainQuery: BmobQuery<Post> = BmobQuery<Post>()
                    mainQuery.or(queries)
                    mainQuery.include("author")
                    mainQuery.findObjects(object : FindListener<Post?>() {
                        override fun done(
                            `object`: List<Post?>,
                            e: BmobException?
                        ) {
                            if (e == null) {
                                hotPostList.addAll(`object` as Collection<Post>)
                            } else {
                                UTIL.toast(context, "没有热帖")
                            }
                            hot.layoutManager = LinearLayoutManager(context)
                            hot.adapter = PostAdapter(hotPostList)
                        }
                    })
                }
            }
        })
    }
}