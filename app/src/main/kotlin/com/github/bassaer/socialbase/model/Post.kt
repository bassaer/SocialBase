package com.github.bassaer.socialbase.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

/**
 * Post data
 * Created by nakayama on 2017/10/01.
 */
@IgnoreExtraProperties
class Post(val uid: String, val author: String,  val title: String, val body: String, val starCount: Int) {
    val stars: Map<String, Boolean> = HashMap()

    @Exclude
    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()

        result.put("uid", this.uid)
        result.put("author", this.author)
        result.put("title", this.title)
        result.put("body", body)
        result.put("starCount", starCount)
        result.put("stars", stars)

        return result
    }
}