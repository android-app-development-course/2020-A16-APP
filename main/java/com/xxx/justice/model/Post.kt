package com.xxx.justice.model

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.datatype.BmobRelation


class Post : BmobObject() {
    var title: String? = null
    var content: String? = null
    var author: User? = null
    var university: String? = null
    var isAnonymous: Boolean = false

    //    val image : BmobFile? = null
//    val likes : BmobRelation? = null
//    val time : LocalDateTime?=null
}