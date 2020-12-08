package com.iua.agustinpereyra.repository.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.iua.agustinpereyra.repository.database.entities.Cattle

@Dao
interface CattleDAO {
    //TODO: Make them suspend
    @Query("SELECT * FROM cattle")
    fun getAll() : LiveData<List<Cattle>>

    @Query("DELETE FROM cattle")
    fun deleteAll()

    @Update
    fun updateAll(updatedCattle: List<Cattle>)

    @Insert
    fun insert(cattle: Cattle)
}