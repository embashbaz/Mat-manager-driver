package com.example.matatumanageruser.data


import com.example.matatumanageruser.utils.Constant
import com.example.matatumanageruser.utils.OperationStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class FirebaseRepository @Inject constructor(
    private val mFirestore: FirebaseFirestore,
    private val mAuth: FirebaseAuth,
   private val uId: String?
): MainRepository {



}