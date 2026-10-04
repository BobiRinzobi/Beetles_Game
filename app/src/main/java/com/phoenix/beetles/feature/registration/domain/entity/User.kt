package com.phoenix.beetles.feature.registration.domain.entity

import com.phoenix.beetles.core.data.local.entity.Cources
import com.phoenix.beetles.core.data.local.entity.Gender
import com.phoenix.beetles.core.data.local.entity.Zodiac

data class User(
    val fio : String?,
    val gender : Gender,
    val course : Cources,
    val difficulty : Int?,
    val data : String?,
    val zodiac : Zodiac,
)
