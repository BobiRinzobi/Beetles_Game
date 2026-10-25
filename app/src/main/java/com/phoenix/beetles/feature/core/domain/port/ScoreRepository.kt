package com.phoenix.beetles.feature.core.domain.port


interface ScoreRepository {
    fun loadBest(): Int
    fun saveBest(userId: String, score: Int)
    fun save(userId: String, score: Int)
    fun loadRecentScores(userId: String, limit: Int = 5): List<Int>
}