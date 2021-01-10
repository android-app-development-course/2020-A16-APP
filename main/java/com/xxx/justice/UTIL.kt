package com.xxx.justice

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Environment
import android.widget.Toast
import com.xxx.justice.model.User
import java.io.*
import java.util.regex.Matcher
import java.util.regex.Pattern


object UTIL {
    var user: User? = null
    val university =
        arrayOf("华南师范大学", "中山大学", "暨南大学", "华南理工大学", "广东工业大学")
    val sex =
        arrayOf("男", "女", "未知")

    fun toast(context: Context?, toast: String) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }


    /**
     * 判断电话号码是否有效
     *
     * @param phoneNumber
     * @return true 有效 / false 无效
     */
    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        var isValid = false
        val expression =
            "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))"
        val inputStr: CharSequence = phoneNumber
        val pattern: Pattern = Pattern.compile(expression)
        val matcher: Matcher = pattern.matcher(inputStr)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }

    /**
     * 判断邮箱地址是否有效
     *
     * @param email
     * @return true 有效 / false 无效
     */
    fun isEmailValid(email: String): Boolean {
        val regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"
        return email.matches(regex.toRegex())
    }

    // 判断网络是否连接
    fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager
                .activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }
}