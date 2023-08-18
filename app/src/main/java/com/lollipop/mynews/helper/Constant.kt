package com.lollipop.mynews.helper

import android.annotation.SuppressLint
import android.content.Context
import com.lollipop.mynews.R
import com.lollipop.mynews.service.model.Category

class Constant {
    object URL {
        const val BASE = "https://newsapi.org"
    }
    object Network {
        const val SERVER_DOWN = 500
        const val MAINTENANCE = 599
        const val REQUEST_UNAUTHORIZED = 401
        const val REQUEST_UNAUTHORIZED_STRING = "401"
        const val REQUEST_NOT_FOUND = 404
        const val BAD_REQUEST = 400
        const val REQUEST_SUCCESS = 200
        const val REQUEST_CREATED = 201
        const val REQUEST_ACCEPTED = 202
        const val REQUEST_NO_CONTENT = 204
        const val PRECONDITION_FAILED = 412
        const val LOGIN_SSO_FAILED = 410
        const val SIGNUP_EMAIL_INVALID = 409
    }

    object IntentParam {
        const val CATEGORY = "category"
        const val SOURCE_ID = "source_id"
        const val SOURCE_NAME = "source_name"
        const val URL = "url"
    }

    object Category {
        @SuppressLint("Recycle")
        fun getData(ctx: Context): List<com.lollipop.mynews.service.model.Category> {
            val items: MutableList<com.lollipop.mynews.service.model.Category> = ArrayList()
            val name_arr = ctx.resources.getStringArray(R.array.category_name)
            val color_arr = ctx.resources.getStringArray(R.array.category_color)
            val drw_arr = ctx.resources.obtainTypedArray(R.array.category_icon)
            for (i in name_arr.indices) {
                val ch = Category(name_arr[i], color_arr[i], drw_arr.getResourceId(i, -1))
                items.add(ch)
            }
            return items
        }
    }
}