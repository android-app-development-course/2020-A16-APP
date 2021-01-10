package com.xxx.justice.model

import cn.bmob.v3.BmobObject


class Comment : BmobObject() {
    var title: String? = null
    var content: String? = null
    var author: User? = null
    var postID: String? = null
    var floor: Int? = null
    var like: Int? = null
    var isAnonymous: Boolean = false
}