package com.kavi.pbc.web.common.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors

@Composable
fun AppOutlineTextField(
    modifier: Modifier = Modifier,
    headingText: String,
    contentText: MutableState<TextFieldValue>,
    onValueChange: ((newValue: TextFieldValue) -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    OutlinedTextField(
        modifier = modifier
            .padding(1.dp)
            .padding(end = 4.dp),
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedTextColor = themeAdditionalColors.quaternary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
            cursorColor = MaterialTheme.colorScheme.tertiary,
            focusedLabelColor = themeAdditionalColors.quaternary,
        ),
        value = contentText.value,
        maxLines = 1,
        label = { Text(text = headingText) },
        onValueChange = { newValue ->
            onValueChange?.invoke(newValue)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
fun AppOutlineMultiLineTextField(
    modifier: Modifier = Modifier,
    headingText: String,
    contentText: MutableState<TextFieldValue>,
    maxLines: Int = 2,
    onValueChange: ((newValue: TextFieldValue) -> Unit)? = null
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    OutlinedTextField(
        modifier = modifier
            .height(80.dp)
            .padding(1.dp)
            .padding(end = 4.dp),
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedTextColor = themeAdditionalColors.quaternary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
            cursorColor = MaterialTheme.colorScheme.tertiary,
            focusedLabelColor = themeAdditionalColors.quaternary,
        ),
        value = contentText.value,
        maxLines = maxLines,
        label = { Text(text = headingText) },
        onValueChange = { newValue ->
            onValueChange?.invoke(newValue)
        }
    )
}