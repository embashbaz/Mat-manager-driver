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
    override suspend fun loginAdmin(email: String, password: String): OperationStatus<MatatuAdmin> {
      return  try {
           var uId = mAuth.signInWithEmailAndPassword(email, password).await().user!!.uid
          if (uId.isEmpty()){
              OperationStatus.Error("An error occurred")
          }else{
             var admin =  mFirestore.collection(Constant.admincollectionName).document(uId).get().await().toObject(MatatuAdmin::class.java)
              if (admin != null){
                  OperationStatus.Success(admin)
              }else{
                  OperationStatus.Error(("An error occurred"))
              }
          }

        }catch (e: Exception){
          OperationStatus.Error(e.message ?: "An error occurred")
        }

    }

    override suspend fun registerAdmin(
        matatuAdmin: MatatuAdmin,
        password: String
    ): OperationStatus<String> {
        return try {
               val user = mAuth.createUserWithEmailAndPassword(matatuAdmin.saccoEmail, password).await().user
            if(user != null){
                mFirestore.collection(Constant.admincollectionName).document(user.uid).set(matatuAdmin).await()
                OperationStatus.Success("Success")
            }else{
                OperationStatus.Error("An error occurred")
            }
        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun registerDriver(driver: Driver, password: String): OperationStatus<String> {
        return try {
            val user = mAuth.createUserWithEmailAndPassword(driver.driverEmail, password).await().user
            if(user != null){
                mFirestore.collection(Constant.admincollectionName).document(uId!!).collection(Constant.driverCollectionName).document(user.uid).set(driver).await()
                OperationStatus.Success("Success")
            }else{
                OperationStatus.Error("An error occurred")
            }
        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addTrip(trip: MatatuTrip): OperationStatus<String> {
        return try {
            mFirestore.collection(Constant.admincollectionName).document(uId!!).collection(Constant.matatusCollectionName).document(trip.matId).collection(Constant.tripCollectionName).add(trip).await()
            OperationStatus.Success("Success")

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addMatatu(matatu: Matatu): OperationStatus<String> {
        return try {
                mFirestore.collection(Constant.admincollectionName).document(uId!!).collection(Constant.matatusCollectionName).document(matatu.matPlate).set(matatu).await()
                OperationStatus.Success("Success")

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }




}