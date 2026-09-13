package com.phoenix.beetles.feature.registration.domain.entity

data class User(
    val fio : String?,
    val gender : Gender,
    val course : Cources,
    val difficulty : Int?,
    val data : String?,
    val zodiac : Zodiac,
)
