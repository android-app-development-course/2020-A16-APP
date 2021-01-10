package com.xxx.justice.model

import cn.bmob.v3.BmobObject

class Favorite : BmobObject() {
    var authorID: String? = null
    var post: Post? = null
}