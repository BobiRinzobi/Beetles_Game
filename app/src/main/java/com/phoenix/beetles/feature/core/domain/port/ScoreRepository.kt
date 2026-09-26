package com.phoenix.beetles.feature.core.domain.port

interface ScoreRepository {
    fun loadBest(): Int
    fun saveBest(score: Int)
}