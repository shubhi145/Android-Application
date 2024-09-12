package com.example.app.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app.model.UserModel
import com.example.app.utils.Share
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID

class AuthViewModel : ViewModel() {
    val auth = Firebase.auth
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: MutableLiveData<FirebaseUser?> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: android.content.Context) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser)
                getData(auth.currentUser!!.uid, context)
            } else {
                _error.postValue(it.exception!!.message)
            }
        }
    }

    private fun getData(uid: String, context: android.content.Context) {




        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                Share.storeData(
                    userData!!.name,
                    userData!!.email,
                    userData!!.bio,
                    userData!!.username,
                    userData!!.imageUrl,
                    context
                )
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun Register(
        email: String,
        password: String,
        name: String,
        bio: String,
        username: String,
        imageUri: Uri,
        context: android.content.Context
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser)
                saveImage(email, name, password, username, imageUri, auth.currentUser?.uid, context)
            } else {
                _error.postValue("Unknown error")
            }
        }
    }

    private fun saveImage(
        email: String,
        name: String,
        password: String,
        username: String,
        imageUri: Uri,
        uid: String?,
        context: android.content.Context
    ) {

        val firestoredb = Firebase.firestore
        val followerRef = firestoredb.collection("followers").document(uid!!)
        val  followingRef = firestoredb.collection("following").document(uid!!)

        followingRef.set("followingIds" to listOf<String>())
        followerRef.set("followerIds" to listOf<String>())

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveDatabase(email, name, password, "", username, it.toString(), uid, context)
            }
        }
    }

    private fun saveDatabase(
        email: String,
        name: String,
        password: String,
        bio: String,
        username: String,
        toString: String,
        uid: String?,
        context: android.content.Context
    ) {
        val UserData = UserModel(email, name, password, bio, username, toString, uid!!)

        userRef.child(uid!!).setValue(UserData).addOnSuccessListener {
            Share.storeData(name, email, password, username, toString, context)
        }.addOnFailureListener {

        }
    }

    fun logout() {
        auth.signOut()
        _firebaseUser.postValue(null)
    }


}