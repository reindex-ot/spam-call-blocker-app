package com.addev.listaspam.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dangerous_phones")
data class DangerousPhone(
    @PrimaryKey val number: String
)
