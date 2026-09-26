package com.phoenix.beetles.feature.core.infrastructure

import com.phoenix.beetles.feature.core.domain.port.RandomProvider
import kotlin.random.Random

class AndroidRandomProvider(
    private val random: Random = Random.Default,
) : RandomProvider {

    override fun nextFloat(): Float = random.nextFloat()

    override fun nextFloat(from: Float, until: Float): Float =
        random.nextFloat() * (until - from) + from

    override fun nextInt(until: Int): Int = random.nextInt(until)
}