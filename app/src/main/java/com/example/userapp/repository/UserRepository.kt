package com.example.userapp.repository

import androidx.lifecycle.LiveData
import com.example.userapp.data.UserDao
import com.example.userapp.model.User

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }
}