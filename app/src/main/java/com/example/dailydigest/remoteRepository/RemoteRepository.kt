package com.example.dailydigest.remoteRepository

import com.example.dailydigest.dto.NewsDTO
import domain.models.Results
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
   suspend fun getNews(): Flow<Results<NewsDTO>>
//   fun saveAccessToken(token: String)
//   fun saveUserName(username: String)
//   fun saveUserEmail(email: String)
//   fun saveUserPhoneNumber(phone :String)
//   fun saveUserID(id: Long)
//
//   fun getAccessToken(): Flow<String?>
//   fun getUsername(): Flow<String?>
//   fun getUserEmail(): Flow<String?>
//   fun getUserPhoneNumber(): Flow<String?>
//   fun getUserID(): Flow<Int?>

}