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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.event.ui.common.EventPotluckItemUI
import com.kavi.pbc.web.event.ui.selected.SelectedEventViewModel
import com.kavi.pbc.web.network.session.Session
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_label_contribute_potluck
import pbcwebapp.ui_event.generated.resources.event_phrase_contribute_potluck

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PotluckSheetUI(sheetState: SheetState, showSheet: MutableState<Boolean>,
                   viewModel: SelectedEventViewModel
) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    val eventPotluckData by viewModel.eventPotluckData.collectAsState()

    val potluckItemCount = eventPotluckData.potluckItemList.size

    val lazyColumHeight = if (potluckItemCount <= 3) {
        400.dp
    } else if(potluckItemCount in 4..6) {
        500.dp
    } else {
        600.dp
    }

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
                    Text(
                        text = stringResource(Res.string.event_label_contribute_potluck),
                        fontFamily = PBCFontFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(2.dp),
                        thickness = 2.dp
                    )

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
        }
    }
}