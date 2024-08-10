package com.example.myprofile.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "contacts")
data class Contact(
    val avatar: String? = null,
    var name: String? = null,
    var career: String? = null,
    var address: String? = null,
    @PrimaryKey val id: Long = 0
) : Serializable