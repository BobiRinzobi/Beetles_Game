package com.phoenix.beetles.feature.core.domain.entity

class Score {
    var value: Int = 0

    var misses: Int = 0

    fun add(points: Int){
        value += points
    }

    fun miss(penalty: Int){
        value = (value - penalty)
        misses++
    }

    fun reset(){
        value = 0
        misses = 0
    }
}