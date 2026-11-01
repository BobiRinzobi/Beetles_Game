package com.phoenix.beetles.feature.registration.presentation.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.phoenix.beetles.R
import com.phoenix.beetles.core.data.local.entity.Cources
import com.phoenix.beetles.core.data.local.entity.Gender
import com.phoenix.beetles.core.data.local.entity.UserEntity
import com.phoenix.beetles.core.data.local.entity.Zodiac
import java.util.Calendar
import java.util.UUID

@DrawableRes
fun Zodiac.toDrawableRes(): Int = when (this) {
    Zodiac.ARIES -> R.drawable.zodiac_aries
    Zodiac.TAURUS -> R.drawable.zodiac_taurus
    Zodiac.GEMINI -> R.drawable.zodiac_gemini
    Zodiac.CANCER -> R.drawable.zodiac_cancer
    Zodiac.LEO -> R.drawable.zodiac_leo
    Zodiac.VIRGO -> R.drawable.zodiac_virgo
    Zodiac.LIBRA -> R.drawable.zodiac_libra
    Zodiac.SCORPIO -> R.drawable.zodiac_scorpio
    Zodiac.SAGITTARIUS -> R.drawable.zodiac_sagittarius
    Zodiac.CAPRICORN -> R.drawable.zodiac_capricorn
    Zodiac.AQUARIUS -> R.drawable.zodiac_aquarius
    Zodiac.PISCES -> R.drawable.zodiac_pisces
}

@Composable
fun RegistrationContent(
    users: List<UserEntity>,
    activeUser: UserEntity?,
    onRegisterUser: (UserEntity) -> Unit,
    onSelectUser: (UserEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Создать") }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Выбрать (${users.size})") }
            )
        }

        when (selectedTabIndex) {
            0 -> CreateUserTab(
                onRegisterUser = { newUser ->
                    onRegisterUser(newUser)
                    selectedTabIndex = 1
                }
            )
            1 -> SelectUserTab(
                users = users,
                activeUser = activeUser,
                onSelectUser = onSelectUser
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateUserTab(
    onRegisterUser: (UserEntity) -> Unit
) {
    var fio by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf(Gender.MAN) }
    var course by rememberSaveable { mutableStateOf(Cources.FIFTH) }
    var isCourseMenuExpanded by remember { mutableStateOf(false) }
    var difficulty by rememberSaveable { mutableFloatStateOf(1f) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    val selectedDateMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
    val calendar = remember(selectedDateMillis) {
        Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
    }
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1
    val year = calendar.get(Calendar.YEAR)
    val dateString = String.format("%02d.%02d.%d", day, month, year)

    val zodiacCalc = remember(day, month) { Zodiac.ZodiacCalculator(day, month) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("ОК") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Регистрация нового игрока", style = MaterialTheme.typography.headlineSmall)

        TextField(
            value = fio,
            onValueChange = { fio = it },
            label = { Text("ФИО") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text(text = "Пол:", style = MaterialTheme.typography.titleMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Gender.entries.forEach { genderChoice ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { gender = genderChoice }
                ) {
                    RadioButton(
                        selected = (gender == genderChoice),
                        onClick = { gender = genderChoice }
                    )
                    Text(text = genderChoice.title)
                }
            }
        }

        Text(text = "Курс:", style = MaterialTheme.typography.titleMedium)
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = course.title,
                onValueChange = {},
                readOnly = true,
                label = { Text("Выберите курс") },
                enabled = false,
                colors = TextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { isCourseMenuExpanded = true }
            )
            DropdownMenu(
                expanded = isCourseMenuExpanded,
                onDismissRequest = { isCourseMenuExpanded = false }
            ) {
                Cources.entries.forEach { courseChoice ->
                    DropdownMenuItem(
                        text = { Text(courseChoice.title) },
                        onClick = {
                            course = courseChoice
                            isCourseMenuExpanded = false
                        }
                    )
                }
            }
        }

        Text(text = "Уровень сложности: ${difficulty.toInt()}", style = MaterialTheme.typography.titleMedium)
        Slider(
            value = difficulty,
            onValueChange = { difficulty = it },
            valueRange = 1f..4f,
            steps = 2
        )

        Text(text = "Дата рождения:", style = MaterialTheme.typography.titleMedium)
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = dateString,
                onValueChange = {},
                readOnly = true,
                label = { Text("Выберите дату") },
                enabled = false
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePicker = true }
            )
        }

        Text(text = "Знак зодиака: ${zodiacCalc.title}", style = MaterialTheme.typography.titleMedium)
        Image(
            painter = painterResource(id = zodiacCalc.toDrawableRes()),
            contentDescription = zodiacCalc.toString(),
            modifier = Modifier.size(80.dp)
        )

        Button(
            onClick = {
                val user = UserEntity(
                    id = UUID.randomUUID().toString(),
                    record = 0,
                    fio = fio.ifBlank { "Не определено" },
                    gender = gender,
                    course = course,
                    difficulty = difficulty.toInt(),
                    data = dateString,
                    zodiac = zodiacCalc,
                    isActive = true
                )
                onRegisterUser(user)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрировать и сохранить")
        }
    }
}

@Composable
private fun SelectUserTab(
    users: List<UserEntity>,
    activeUser: UserEntity?,
    onSelectUser: (UserEntity) -> Unit
) {
    if (users.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Нет зарегистрированных пользователей")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(users) { user ->
                val isActive = user.id == activeUser?.id
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isActive) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                    border = if (isActive) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.fio ?: "Без имени",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Курс: ${user.course.title} | Текущая сложность: ${user.difficulty?.coerceIn(0, 4) ?: 0} | Рекорд: ${user.record}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Зодиак: ${user.zodiac.title}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Button(
                            onClick = { onSelectUser(user) },
                            enabled = !isActive
                        ) {
                            Text(if (isActive) "Активен" else "Активировать")
                        }
                    }
                }
            }
        }
    }
}
