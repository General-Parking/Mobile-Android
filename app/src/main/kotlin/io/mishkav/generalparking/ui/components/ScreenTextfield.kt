package io.mishkav.generalparking.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import io.mishkav.generalparking.ui.theme.Typography

@Composable
fun ScreenTextfield(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null
) = TextField(
    value = value,
    onValueChange = onValueChange,
    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    label = label,
    placeholder = placeholder,
    singleLine = true,
    textStyle = Typography.body1,
    colors = TextFieldDefaults.textFieldColors(
        focusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        backgroundColor = Color.Transparent,
    ),
    modifier = modifier
)