package com.phoenix.beetles.feature.core.domain.port

interface RandomProvider {
    fun nextFloat(): Float
    fun nextFloat(from: Float, until: Float): Float
    fun nextInt(until: Int): Int
}