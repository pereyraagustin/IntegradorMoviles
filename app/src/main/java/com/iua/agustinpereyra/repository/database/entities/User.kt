package com.iua.agustinpereyra.repository.database.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User (

    @PrimaryKey
    val email : String,
    @NonNull
    val username : String,
    @NonNull
    val passwd : String,

)
