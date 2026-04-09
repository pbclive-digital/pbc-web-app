package com.kavi.pbc.web.event.ui.selected.action

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.TitleWithAction
import com.kavi.pbc.web.common.ui.component.TitleWithBackNav
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.event.ui.common.EventPotluckItemUI
import com.kavi.pbc.web.event.ui.common.PotluckItemContributorListUI
import com.kavi.pbc.web.event.ui.selected.SelectedEventViewModel
import com.kavi.pbc.web.network.session.Session
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_view
import pbcwebapp.ui_event.generated.resources.event_label_contribute_potluck
import pbcwebapp.ui_event.generated.resources.event_phrase_contribute_potluck
import pbcwebapp.ui_event.generated.resources.event_phrase_potluck_contribution_data

private enum class PotluckViewMode {
    CONTRIBUTING_MODE, CONTRIBUTION_VIEW_MODE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PotluckSheetUI(sheetState: SheetState, showSheet: MutableState<Boolean>,
                   viewModel: SelectedEventViewModel, isPhoneScreen: Boolean
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current
    var viewMode by remember { mutableStateOf(PotluckViewMode.CONTRIBUTING_MODE) }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            showSheet.value = false
        },
        containerColor = MaterialTheme.colorScheme.background,
        scrimColor = themeAdditionalColors.shadow.copy(alpha = .5f)
    ) {
        Box (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 20.dp, end = 20.dp, bottom = 40.dp)
                .fillMaxWidth()
        ) {
            if (Session.isLogIn()) {
                Column {
                    if (isPhoneScreen) {
                        Text(
                            text = stringResource(Res.string.event_label_contribute_potluck),
                            fontFamily = PBCFontFamily,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    } else {
                        when(viewMode) {
                            PotluckViewMode.CONTRIBUTING_MODE -> {
                                TitleWithAction(
                                    titleText = stringResource(Res.string.event_label_contribute_potluck),
                                    textSize = 24,
                                    actionPainter = painterResource(Res.drawable.event_icon_view),
                                    actionPainterSize = 28.dp,
                                    isIcon = true
                                ) {
                                    viewMode = PotluckViewMode.CONTRIBUTION_VIEW_MODE
                                }
                            }
                            PotluckViewMode.CONTRIBUTION_VIEW_MODE -> {
                                TitleWithBackNav(
                                    titleText = stringResource(Res.string.event_label_contribute_potluck),
                                    textSize = 24,
                                    backIconSize = 24.dp
                                ) {
                                    viewMode = PotluckViewMode.CONTRIBUTING_MODE
                                }
                            }
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(2.dp),
                        thickness = 2.dp
                    )

                    when(viewMode) {
                        PotluckViewMode.CONTRIBUTING_MODE -> {
                            // Contributing Mode
                            ShowContributingMode(viewModel = viewModel)
                        }
                        PotluckViewMode.CONTRIBUTION_VIEW_MODE -> {
                            // Show current contributions mode
                            ShowCurrentContributionsModel(viewModel = viewModel)
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun ShowContributingMode(viewModel: SelectedEventViewModel) {
    val eventPotluckData by viewModel.eventPotluckData.collectAsState()
    val potluckItemCount = eventPotluckData.potluckItemList.size

    val lazyColumHeight = if (potluckItemCount <= 3) {
        400.dp
    } else if(potluckItemCount in 4..6) {
        500.dp
    } else {
        600.dp
    }

    Column {
        Text(
            text = stringResource(Res.string.event_phrase_contribute_potluck),
            fontFamily = PBCFontFamily,
            fontSize = 16.sp,
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        LazyColumn (
            modifier = Modifier
                .padding(top = 12.dp)
                .height(lazyColumHeight)
        ) {
            items(eventPotluckData.potluckItemList) { potluckItem ->
                EventPotluckItemUI(
                    modifier = Modifier.padding(bottom = 8.dp),
                    viewModel = viewModel,
                    potluckItem = potluckItem,
                    currentUserContributions = viewModel
                        .checkedCurrentUserContribution(potluckItem = potluckItem)
                )
            }
        }
    }
}

@Composable
private fun ShowCurrentContributionsModel(viewModel: SelectedEventViewModel) {
    val eventPotluckData by viewModel.eventPotluckData.collectAsState()

    val potluckItemCount = eventPotluckData.potluckItemList.size

    val lazyColumHeight = if (potluckItemCount <= 3) {
        400.dp
    } else if(potluckItemCount in 4..6) {
        500.dp
    } else {
        600.dp
    }

    Column {
        Text(
            text = stringResource(Res.string.event_phrase_potluck_contribution_data),
            fontFamily = PBCFontFamily,
            fontSize = 16.sp,
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        LazyColumn (
            modifier = Modifier
                .padding(top = 12.dp)
                .height(lazyColumHeight)
        ) {
            items(eventPotluckData.potluckItemList) { potluckItem ->
                PotluckItemContributorListUI(
                    modifier = Modifier.padding(bottom = 8.dp),
                    potluckItem = potluckItem
                )
            }
        }
    }
}