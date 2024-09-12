package com.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app.model.PostModel
import com.example.app.model.UserModel
import com.google.firebase.database.*

class HomeViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    private val postRef = db.getReference("posts")

    private val _postanduser = MutableLiveData<List<Pair<PostModel, UserModel>>>()
    val postanduser: LiveData<List<Pair<PostModel, UserModel>>> = _postanduser

    init {
        fetchPostAndUser()
    }

    private fun fetchPostAndUser() {
        postRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Pair<PostModel, UserModel>>()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(PostModel::class.java)
                    post?.let {
                        fetchUserFromPost(it) { user ->
                            result.add(it to user)
                            if (result.size == snapshot.childrenCount.toInt()) {
                                _postanduser.postValue(result)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun fetchUserFromPost(post: PostModel, onResult: (UserModel) -> Unit) {
        db.getReference("users").child(post.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }
}
