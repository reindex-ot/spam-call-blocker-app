package com.addev.listaspam.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DangerousPhoneDao {
    @Query("SELECT number FROM dangerous_phones")
    fun getAllNumbers(): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM dangerous_phones WHERE number = :number LIMIT 1)")
    fun exists(number: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(phones: List<DangerousPhone>)

    @Query("DELETE FROM dangerous_phones")
    fun deleteAll()
}
