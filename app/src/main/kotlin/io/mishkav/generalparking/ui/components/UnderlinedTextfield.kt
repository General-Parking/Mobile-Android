package io.mishkav.generalparking.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import io.mishkav.generalparking.ui.components.lines.TextfieldUnderLine

@Composable
fun UnderlinedTextfield(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) = Column(
    modifier = Modifier
        .fillMaxWidth()
) {
    ScreenTextfield(
        value = value,
        onValueChange = onValueChange,
        keyboardType = keyboardType,
        label = label,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        modifier = Modifier
            .fillMaxWidth()
    )
    TextfieldUnderLine()
}