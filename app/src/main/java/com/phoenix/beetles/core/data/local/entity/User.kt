package com.phoenix.beetles.core.data.local.entity

import com.phoenix.beetles.feature.registration.domain.entity.Cources
import com.phoenix.beetles.feature.registration.domain.entity.Gender
import com.phoenix.beetles.feature.registration.domain.entity.Zodiac

data class User(
    val id : String,
    val fio : String?,
    val gender : Gender,
    val course : Cources,
    val difficulty : Int?,
    val data : String?,
    val zodiac : Zodiac,
)
