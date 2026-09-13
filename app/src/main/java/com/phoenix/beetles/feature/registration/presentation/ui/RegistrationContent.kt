package com.phoenix.beetles.feature.registration.presentation.ui

import android.widget.EditText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.phoenix.beetles.feature.registration.domain.entity.Cources
import com.phoenix.beetles.feature.registration.domain.entity.User

@Composable
fun registrationContent() {
    var text by rememberSaveable { mutableStateOf("") }
    val fio : String
    var gender : Boolean = true
    val cource : Cources
    val dif : Int
    val data : String // преобразовывать в строку
    val zodiac : String  // смотреть по типу картинки
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TextField(
            value = text,
            onValueChange = {text = it}
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            RadioButton(
                onClick = { gender = !gender },
                selected = TODO(),
                enabled = TODO(),
                colors = TODO(),
                interactionSource = TODO(),
            )
            Text(
                text = if ( gender) "Мужчина" else "Женщина"
            );
        }

    }
}