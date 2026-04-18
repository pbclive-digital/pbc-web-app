package com.kavi.pbc.web.event.ui.admin.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppIconButton
import com.kavi.pbc.web.common.ui.component.AppTooltipWrap
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_delete
import pbcwebapp.ui_event.generated.resources.event_icon_publish
import pbcwebapp.ui_event.generated.resources.event_label_tip_add_to_agenda

@Composable
fun AgendaItemEdit(
    modifier: Modifier = Modifier,
    onAddItem: (agendaItem: String) -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current
    val inputValue = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth(),
            value = inputValue.value,
            onValueChange = { inputValue.value = it },
            enabled = true,
            readOnly = false,
            textStyle = TextStyle(
                fontFamily = PBCFontFamily
            ),
            label = { Text("HH:MM - Agenda Item") },
            placeholder = { Text("HH:MM - Agenda Item") },
            trailingIcon = {
                AppTooltipWrap(
                    tipLabel = stringResource(Res.string.event_label_tip_add_to_agenda)
                ) {
                    AppIconButton(
                        icon = painterResource(Res.drawable.event_icon_publish),
                        buttonSize = 35.dp
                    ) {
                        onAddItem(inputValue.value.text)
                    }
                }
            },
            isError = false,
            keyboardActions = KeyboardActions(
                onDone = {
                    onAddItem(inputValue.value.text)
                }
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = themeAdditionalColors.quaternary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                cursorColor = MaterialTheme.colorScheme.tertiary,
                focusedLabelColor = themeAdditionalColors.quaternary,
            )
        )
    }
}

@Composable
fun AgendaItem(
    modifier: Modifier = Modifier,
    agendaItem: String,
    onDelete: (agendaItem: String) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        confirmValueChange = {
            when(it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete(agendaItem)
                }
                SwipeToDismissBoxValue.StartToEnd -> return@rememberSwipeToDismissBoxState false
                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        // positional threshold of 25%
        positionalThreshold = { it * .25f }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            DismissBackground(dismissState)
        },
        enableDismissFromStartToEnd = false,
        content = {
            AgendaItemUI(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                agendaItem = agendaItem
            )
        })
}

@Composable
fun AgendaItemUI(
    modifier: Modifier = Modifier,
    agendaItem: String
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp)
                .weight(.85f),
            text = agendaItem,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = PBCFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Color(0xFFFF1744)
        SwipeToDismissBoxValue.StartToEnd -> Color.Transparent
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier)
        androidx.compose.material3.Icon(
            painter = painterResource(Res.drawable.event_icon_delete),
            contentDescription = "delete"
        )
    }
}