package com.phoenix.beetles.feature.registration.domain.entity

data class User(
    val fio : String?,
    val gender : Boolean?, // или лучше сделать enum?
    val cource : Cources?,
    val dif : Int?,
    val data : String?, // преобразовывать в строку
    val zodiac : String?,  // смотреть по типу картинки
)
