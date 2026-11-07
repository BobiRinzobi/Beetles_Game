package com.phoenix.beetles

import com.phoenix.beetles.feature.core.domain.usecase.AnalyzeDifficultyUseCase
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Before

class ExampleUnitTest {

    private lateinit var useCase: AnalyzeDifficultyUseCase

    @Before
    fun setUp() {
        useCase = AnalyzeDifficultyUseCase()
    }

    @Test
    fun NoChangeNeedThreeResultTest() {
        assertEquals(0, useCase.execute(emptyList()))
        assertEquals(0, useCase.execute(listOf(100)))
        assertEquals(0, useCase.execute(listOf(100, 100)))
    }

    @Test
    fun UpLevelDifficultyTest() {
        assertEquals(2, useCase.execute(listOf(110, 100, 100)))
    }

    @Test
    fun DownLevelDifficultyTest() {
        assertEquals(-2, useCase.execute(listOf(90, 100, 100))
        )
    }

    @Test
    fun NoChangeNeed10perTest() {
        assertEquals(0, useCase.execute(listOf(109, 100, 100)))
        assertEquals(0, useCase.execute(listOf(91, 100, 100)))
    }

    @Test
    fun AllZeroTest() {
        assertEquals(0, useCase.execute(listOf(0, 0, 0)))
    }

    @Test
    fun LastPositiveTest() {
        assertEquals(2, useCase.execute(listOf(10, 0, 0)))
    }

    @Test
    fun LastNegativeTest() {
        assertEquals(-2, useCase.execute(listOf(-10, 0, 0)))
    }

    @Test
    fun MoreResultTest() {
        assertEquals(2, useCase.execute(listOf(119, 100, 100, 0)))
    }
}