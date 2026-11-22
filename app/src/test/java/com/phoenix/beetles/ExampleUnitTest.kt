
package com.phoenix.beetles

import com.phoenix.beetles.core.data.local.entity.UserEntity
import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.*
import com.phoenix.beetles.feature.core.domain.port.*
import com.phoenix.beetles.feature.core.domain.repository.GameRepository
import com.phoenix.beetles.feature.core.domain.usecase.*
import com.phoenix.beetles.feature.core.presentation.presenter.GameViewModel
import com.phoenix.beetles.feature.gold.domain.model.GoldPrice
import com.phoenix.beetles.feature.gold.domain.repository.GoldRepository
import com.phoenix.beetles.feature.gold.domain.usecase.SpawnGoldenBugUseCase
import com.phoenix.beetles.feature.settings.domain.entity.Settings
import com.phoenix.beetles.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

import java.util.UUID


@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun gameEnd10Miss() {
        val fixture = GameFixture()

        repeat(9) {
            fixture.viewModel.onTap(Position(100f, 100f))
        }

        fixture.viewModel.onTick(0f)

        assertFalse(fixture.viewModel.isGameFinished)

        fixture.viewModel.onTap(Position(100f, 100f))
        fixture.viewModel.onTick(0f)

        assertTrue(fixture.viewModel.isGameFinished)
    }


    @Test
    fun difficultyNotUp4Sec() {
        val fixture = GameFixture()

        val initialSettings =
            fixture.settingsRepository.getCurrentSettings()

        fixture.viewModel.onTick(4f)

        assertEquals(
            initialSettings,
            fixture.settingsRepository.getCurrentSettings()
        )
    }


    @Test
    fun difficultyUp5Sec() {
        val fixture = GameFixture()

        fixture.viewModel.onTick(5f)

        val settings =
            fixture.settingsRepository.getCurrentSettings()

        assertEquals(1.5f, settings.gameSpeed, 0.001f)
        assertEquals(15, settings.maxBeetlesCount)
        assertEquals(30, settings.boostSpawn)
    }

    @Test
    fun difficultyMaxValue() {
        val maxSettings = Settings(
            gameSpeed = 12.0f,
            maxBeetlesCount = 20,
            boostSpawn = 30
        )

        val fixture = GameFixture(maxSettings)

        fixture.viewModel.onTick(5f)

        val settings =
            fixture.settingsRepository.getCurrentSettings()

        assertEquals(12.0f, settings.gameSpeed, 0.001f)
        assertEquals(20, settings.maxBeetlesCount)
        assertEquals(30, settings.boostSpawn)
    }

    private class GameFixture(
        initialSettings: Settings = Settings()
    ) {

        val world = GameWorld(Bounds(0f, 0f))

        private val config = GameConfig.DEFAULT

        val settingsRepository =
            FakeSettingsRepository(initialSettings)

        private val gameRepository = FakeGameRepository()
        private val scoreRepository = FakeScoreRepository()
        private val difficultyRepository = FakeDifficultyRepository()
        private val goldRepository = FakeGoldRepository()
        private val sfxPlayer = FakeSfxPlayer()

        private val randomProvider = object : RandomProvider {
            override fun nextFloat() = 0.5f

            override fun nextFloat(from: Float, until: Float) =
                from + (until - from) / 2f

            override fun nextInt(until: Int) = 0
        }

        private val spawnBugs = SpawnBugsUseCase(
            world,
            config,
            randomProvider
        ) { BugId(UUID.randomUUID()) }

        private val spawnBonus = SpawnBonusUseCase(
            world,
            config,
            randomProvider
        )

        private val updateWorld = UpdateWorldUseCase(
            world,
            config
        )

        private val spawnGoldenBug = SpawnGoldenBugUseCase(
            world,
            config,
            randomProvider
        ) { BugId(UUID.randomUUID()) }

        val viewModel = GameViewModel(
            world = world,
            baseGameConfig = config,
            handleTap = HandleTapUseCase(world, config),
            spawnBugs = spawnBugs,
            updateWorld = updateWorld,
            spawnBonus = spawnBonus,
            spawnGoldenBug = spawnGoldenBug,
            goldPriceRepository = goldRepository,
            restartGame = RestartGameUseCase(
                world,
                spawnBugs,
                spawnBonus
            ),
            scoreRepository = scoreRepository,
            difficultyRepository = difficultyRepository,
            saveUserRecordUseCase = SaveUserRecordUseCase(
                gameRepository
            ),
            sfxPlayer = sfxPlayer,
            gameRepository = gameRepository,
            settingsRepository = settingsRepository
        )
    }

    private class FakeSettingsRepository(
        initialSettings: Settings
    ) : SettingsRepository {

        private var currentSettings = initialSettings

        override fun getSettings(): Flow<Settings> =
            flowOf(currentSettings)

        override fun getCurrentSettings(): Settings =
            currentSettings

        override fun updateSettings(settings: Settings) {
            currentSettings = settings
        }
    }


    private class FakeGameRepository : GameRepository {

        override suspend fun getActiveUser(): UserEntity? = null

        override suspend fun updateUser(user: UserEntity) {}

        override fun getUser(id: String): Flow<UserEntity?> =
            flowOf(null)
    }


    private class FakeScoreRepository : ScoreRepository {

        override fun loadBest() = 0

        override fun saveBest(userId: String, score: Int) {}

        override fun save(userId: String, score: Int) {}

        override fun loadRecentScores(
            userId: String,
            limit: Int
        ): List<Int> = emptyList()
    }


    private class FakeDifficultyRepository : DifficultyRepository {

        private val level = MutableStateFlow(1)

        override fun getLevel() = level.value

        override fun observeLevel(): StateFlow<Int> = level

        override fun saveLevel(level: Int) {
            this.level.value = level
        }
    }


    private class FakeGoldRepository : GoldRepository {

        override fun getCachedGoldPrice(): GoldPrice? = null

        override suspend fun refreshGoldPrice(): GoldPrice? = null
    }


    private class FakeSfxPlayer : SfxPlayer {

        override fun startBugScream() {}

        override fun stopBugScream() {}
    }
}
