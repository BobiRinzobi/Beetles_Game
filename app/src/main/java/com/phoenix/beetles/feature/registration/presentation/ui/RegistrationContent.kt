package com.phoenix.beetles.feature.registration.presentation.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import com.phoenix.beetles.R
import com.phoenix.beetles.feature.registration.domain.entity.Cources
import com.phoenix.beetles.feature.registration.domain.entity.Gender
import com.phoenix.beetles.feature.registration.domain.entity.User
import com.phoenix.beetles.feature.registration.domain.entity.Zodiac

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationContent(
    modifier: Modifier = Modifier
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

    var userResult by remember { mutableStateOf<User?>(null) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("ОК")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Регистрация", style = MaterialTheme.typography.headlineLarge)

        TextField(
            value = fio,
            onValueChange = { fio = it },
            label = { Text("ФИО") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text(text = "Пол:", style = MaterialTheme.typography.headlineSmall)
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

        Text(text = "Курс:", style = MaterialTheme.typography.headlineSmall)
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

        Text(text = "Уровень сложности игры: ${difficulty.toInt()}", style = MaterialTheme.typography.headlineSmall)
        Slider(
            value = difficulty,
            onValueChange = { difficulty = it },
            valueRange = 1f..5f,
            steps = 3
        )

        Text(text = "Дата рождения:", style = MaterialTheme.typography.headlineSmall)
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

        Text(text = "Знак зодиака: ${zodiacCalc.title}", style = MaterialTheme.typography.headlineSmall)
        Image(
            painter = painterResource(id = zodiacCalc.toDrawableRes()),
            contentDescription = zodiacCalc.toString(),
            modifier = Modifier
                .size(96.dp)
                .padding(top = 8.dp)
        )

        Button(
            onClick = {
                userResult = User(
                    fio = fio.ifBlank { "Не указано" },
                    gender = gender,
                    course = course,
                    difficulty = difficulty.toInt(),
                    data = dateString,
                    zodiac = zodiacCalc
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрировать игрока")
        }

        userResult?.let { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Карточка игрока", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = """
                            ФИО: ${user.fio}
                            Пол: ${user.gender.title}
                            Курс: ${user.course.title}
                            Сложность: ${user.difficulty}
                            Дата рождения: ${user.data}
                            Знак зодиака: ${user.zodiac.title}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Image(
                        painter = painterResource(id = user.zodiac.toDrawableRes()),
                        contentDescription = user.zodiac.toString(),
                        modifier = Modifier
                            .size(96.dp)
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}