package com.phoenix.beetles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phoenix.beetles.feature.authors.data.AuthorsRepositoryImpl
import com.phoenix.beetles.feature.authors.presentation.presenter.AuthorsViewModel
import com.phoenix.beetles.feature.authors.presentation.ui.AuthorsScreen
import com.phoenix.beetles.feature.registration.presentation.presenter.RegistrationViewModel
import com.phoenix.beetles.feature.registration.presentation.ui.RegistrationScreen
import com.phoenix.beetles.feature.rules.data.RulesRepositoryImpl
import com.phoenix.beetles.feature.rules.presentation.presenter.RulesViewModel
import com.phoenix.beetles.feature.rules.presentation.ui.RulesScreen
import com.phoenix.beetles.ui.theme.Beetles_GameTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Beetles_GameTheme {

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) {

                    var selectedTab by remember {
                        mutableIntStateOf(0)
                    }

                    val tabs = listOf(
                        "Регистрация",
                        "Правила игры",
                        "Авторы"
                    )

                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        TabRow(
                            selectedTabIndex = selectedTab
                        ) {
                            tabs.forEachIndexed { index, title ->

                                Tab(
                                    selected = selectedTab == index,
                                    onClick = {
                                        selectedTab = index
                                    },
                                    text = {
                                        Text(title)
                                    }
                                )
                            }
                        }

                        when (selectedTab) {

                            0 -> {
                                val viewModel: RegistrationViewModel = viewModel()

                                RegistrationScreen(
                                    registrationViewModel = viewModel
                                )
                            }

                            1 -> {
                                val repository = RulesRepositoryImpl(this@MainActivity)

                                val rulesViewModel = RulesViewModel(repository)

                                RulesScreen(
                                    rules = rulesViewModel.getRules()
                                )
                            }

                            2 -> {
                                val repository = AuthorsRepositoryImpl()

                                val authorsViewModel = AuthorsViewModel(
                                    repository
                                )

                                AuthorsScreen(
                                    authors = authorsViewModel.getAuthors()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}