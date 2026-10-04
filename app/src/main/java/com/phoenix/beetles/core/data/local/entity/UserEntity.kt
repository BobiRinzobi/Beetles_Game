package com.phoenix.beetles.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id : String,
    val record : Int,
    val fio : String?,
    val gender : Gender,
    val course : Cources,
    val difficulty : Int?,
    val data : String?,
    val zodiac : Zodiac,
    val isActive : Boolean = false
)
