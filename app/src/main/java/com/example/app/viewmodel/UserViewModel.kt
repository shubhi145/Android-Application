package com.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app.model.PostModel
import com.example.app.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class UserViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    private val postRef = db.getReference("posts")
    private val userRef = db.getReference("users")

    private val _post = MutableLiveData<List<PostModel>>(mutableListOf())
    val post: LiveData<List<PostModel>> = _post

    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> = _user

    private val _followerList = MutableLiveData(
        listOf<String>()
    )
    val followerList: LiveData<List<String>> = _followerList

    private val _followingList = MutableLiveData(listOf<String>())
    val followingList: LiveData<List<String>> = _followingList

    fun fetchUser(uid: String) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _user.postValue(user!!)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun fetchPosts(uid: String) {
        postRef.orderByChild("userId").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val postList = snapshot.children.mapNotNull {
                        it.getValue(PostModel::class.java)
                    }
                    _post.postValue(postList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }

    val firestoredb = Firebase.firestore
    fun followuser(userId: String, currentUserId: String) {

        val ref = firestoredb.collection("following").document(currentUserId)
        val followerref = firestoredb.collection("followers").document(userId)

        ref.update("followingIds", FieldValue.arrayUnion(userId))
        followerref.update("followerIds", FieldValue.arrayUnion(currentUserId))
    }

    fun getFollowers(userId: String) {

        firestoredb.collection("followers").document(userId).addSnapshotListener{
            value,error ->

            val followerIds = value?.get("followerIds") as? List<String>  ?: listOf()
            _followerList.postValue(followerIds)
        }

    }

    fun getFollowing(userId: String) {


        firestoredb.collection("following").document(userId).addSnapshotListener{
                value,error ->

            val followingIds = value?.get("followingIds") as? List<String>  ?: listOf()
            _followingList.postValue(followingIds)
        }

    }
}
