package com.iua.agustinpereyra.controller

import android.app.Application
import android.content.Context
import android.text.Editable
import com.iua.agustinpereyra.repository.UsersRepository
import com.iua.agustinpereyra.repository.database.entities.Users
import java.lang.Error

class AccountManager(application: Application) {

    private val usersRepository = UsersRepository(application)
    private val preferenceUtils = PreferenceUtils(application)

    companion object {
        fun isPasswordValid(text: Editable?) : Boolean {
            // Check if password is bigger than 8
            return text != null && text.length >= 8
        }
    }

    /**
     * checkAndLog checks if the user data corresponds to a user
     * in the database. If it does, it saves the user as the current
     * user and returns true, else it returns false.
     */
    suspend fun checkAndLog(email: String, passwd: String) : Boolean {
        // Check if user is found in database
        // TODO: Encrypt password
        val dbUser = usersRepository.getUser(email, passwd)
        if (dbUser != null) {
            // Save current userId
            preferenceUtils.saveCurrentUser(dbUser.id)

            return true
        }
        return false
    }

    /**
     * Returns true if password could be changed, false otherwise
     */
    suspend fun changePasswd(oldPasswd: String, newPasswd: String, confPasswd: String) : Boolean {
        val loggedUserId = preferenceUtils.getCurrentUser()
        // TODO: Encrypt passwords
        if (loggedUserId != null) {
            // Get the user's data
            val loggedUser = usersRepository.getUserByIdNotObservable(loggedUserId)
            if (loggedUser.passwd == oldPasswd) {
                if (newPasswd == confPasswd) {
                    usersRepository.updateUserPassword(loggedUserId, newPasswd)
                    return true
                }
            }
            return false
        } else {
            throw Error("Impossible to change password of current user. There is not a logged user!")
        }
    }

    /**
     * changeUsername returns true if it was able to change the username, false otherwise
     */
    suspend fun changeUsername(newUsername: String): Boolean {
        val loggedUserId = preferenceUtils.getCurrentUser()
        if (loggedUserId != null) {
            return usersRepository.updateUserName(loggedUserId, newUsername)
        }
        return false
    }



    /**
     * registerUser does what is necessary to the user data before saving it
     * in the database, and then saves it in database. If the user was successfully
     * registered, it returns true, otherwise it returns false.
     */
    suspend fun registerUser(email: String, username: String, password: String) : Boolean {
        // TODO: Encrypt password
        val newUser = Users(0, email, username, password)
        usersRepository.insertUser(newUser)
        // Get User Id
        val savedUser = usersRepository.getUser(email, password)
        return if (savedUser != null) {
            preferenceUtils.saveCurrentUser(savedUser.id)
            true
        } else {
            // Something went wrong
            false
        }
    }
}