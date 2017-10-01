package com.github.bassaer.socialbase.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.bassaer.socialbase.R
import com.github.bassaer.socialbase.model.Post

/**
 * ViewHolder
 * Created by nakayama on 2017/10/01.
 */
class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.findViewById(R.id.post_title)
    val authorView: TextView = itemView.findViewById(R.id.post_author)
    val starView: ImageView = itemView.findViewById(R.id.star)
    val numStarsView: TextView = itemView.findViewById(R.id.post_num_stars)
    val bodyView: TextView = itemView.findViewById(R.id.post_body)

    fun bindToPost(post: Post, starClickListener: View.OnClickListener) {
        this.titleView.text = post.title
        this.authorView.text = post.author
        this.numStarsView.text = post.starCount.toString()
        bodyView.text = post.body
        starView.setOnClickListener(starClickListener)
    }
}