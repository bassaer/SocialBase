package com.github.bassaer.socialbase.model

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Comment data
 * Created by nakayama on 2017/10/01.
 */
@IgnoreExtraProperties
class Commnet(uid: String, author: String, text: String)