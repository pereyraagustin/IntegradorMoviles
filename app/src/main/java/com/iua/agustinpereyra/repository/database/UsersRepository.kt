package com.iua.agustinpereyra.repository.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.iua.agustinpereyra.repository.database.dao.UsersDAO
import com.iua.agustinpereyra.repository.database.entities.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersRepository(private val application: Application) {

    private val usersDao: UsersDAO

    init {
        val db = AppDatabase.getDatabase(application)
        usersDao = db.usersDao()
    }

    /**
     * getUserById gets the user asynchronously gets observable data from DB as LiveData<Users>
     */
    suspend fun getUserById(userId: Int): LiveData<Users> = withContext(Dispatchers.IO) {
        val user = usersDao.getUserById(userId)
        user
    }

    /**
     * getUser gets the user from database if password and username match
     * a registry from it. If no user is found, null is returned
     */
    suspend fun getUser(username: String, passwd: String): Users? = withContext(Dispatchers.IO) {
        usersDao.getUser(username, passwd)
    }

    /**
     * insertUsers inserts the asked user asynchronously
     */
    suspend fun insertUser(user: Users) = withContext(Dispatchers.IO) {
        usersDao.insertUser(user)
    }

    /**
     * deleteUser deletes the user asynchronously, and returns the number
     * of deleted rows
     */
    suspend fun deleteUser(user: Users): Int = withContext(Dispatchers.IO) {
        usersDao.deleteUser(user)
    }

    /**
     * updateUser updates the user's passed data (username and/or password)
     */
    suspend fun updateUser(userId: Int, username: String?, password: String?) = withContext(Dispatchers.IO) {
        if (username != null) {
            usersDao.updateUsername(username, userId)
        }
        if (password != null) {
            usersDao.updatePasswd(password, userId)
        }
    }
}