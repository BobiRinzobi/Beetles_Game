package com.phoenix.beetles.feature.gold.domain.repository

import com.phoenix.beetles.feature.gold.domain.model.GoldPrice

interface GoldRepository {
    fun getCachedGoldPrice(): GoldPrice?
    suspend fun refreshGoldPrice(): GoldPrice?
}
