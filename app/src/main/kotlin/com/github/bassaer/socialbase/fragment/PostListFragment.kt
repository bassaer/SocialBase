package com.github.bassaer.socialbase.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.github.bassaer.socialbase.PostDetailActivity
import com.github.bassaer.socialbase.R
import com.github.bassaer.socialbase.model.Post
import com.github.bassaer.socialbase.view.PostViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_all_posts.*

/**
 * base fragment class
 * Created by nakayama on 2017/10/01.
 */
abstract class PostListFragment: Fragment(), View.OnClickListener {
    private val TAG = "PostListFragment"
    private val database = FirebaseDatabase.getInstance().reference
    //private val recyclerAdapter = PostRecyclerAdapter(getQuery(database), this, getUid,  database)
    private var recyclerAdapter: FirebaseRecyclerAdapter<Post, PostViewHolder>? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater?.inflate(R.layout.fragment_all_posts, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messagesList.setHasFixedSize(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Set up Layout manager, reverse layout
        val manager = LinearLayoutManager(activity)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        messagesList.layoutManager = manager

        val postsQuery = getQuery(this.database)

        recyclerAdapter = object : FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post::class.java, R.layout.item_post, PostViewHolder::class.java, postsQuery
        ) {
            override fun populateViewHolder(viewHolder: PostViewHolder, model: Post, position: Int) {
                val postRef = getRef(position)
                val postKey = postRef.key
                viewHolder.itemView.setOnClickListener {
                    val intent = Intent(activity, PostDetailActivity::class.java)
                    intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey)
                    startActivity(intent)
                }

                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_star_24dp)
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_star_border_24dp)
                }

                viewHolder.bindToPost(model, View.OnClickListener {
                    val globalPostRef = database.child("posts").child(postRef.key)
                    val userPostRef = database.child("user-posts").child(model.uid).child(postRef.key)

                    onStarClicked(globalPostRef)
                    onStarClicked(userPostRef)
                })
            }
        }

        this.recyclerView?.adapter = recyclerAdapter

    }

    private fun onStarClicked(postReference: DatabaseReference) {
        postReference.runTransaction(object : Transaction.Handler {

            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val post = mutableData.getValue(Post::class.java) ?: return Transaction.success(mutableData)
                val uid = getUid() ?: return Transaction.success(mutableData)

                if (post.stars.containsKey(uid)) {
                    post.starCount -= 1
                    post.stars.remove(uid)
                } else {
                    post.starCount += 1
                    post.stars.put(uid, true)
                }
                mutableData.value = post
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError?, flag: Boolean, dataSnapshot: DataSnapshot?) {
                Log.d(TAG, "postTransaction:onComplete:" + databaseError)
            }

        })
    }
    

    fun getUid():String? = FirebaseAuth.getInstance().currentUser?.uid

    abstract fun getQuery(databaseReference: DatabaseReference): Query

}