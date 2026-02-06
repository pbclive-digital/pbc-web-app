package com.kavi.pbc.web.event.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.potluck.EventPotluckItem
import com.kavi.pbc.web.event.data.model.EventActionUiState
import com.kavi.pbc.web.event.ui.selected.SelectedEventViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_add_item
import pbcwebapp.ui_event.generated.resources.event_icon_remove_item
import pbcwebapp.ui_event.generated.resources.event_label_user_contribution_count

@Composable
fun EventPotluckItemUI(modifier: Modifier = Modifier,
                       viewModel: SelectedEventViewModel,
                       potluckItem: EventPotluckItem,
                       currentUserContributions: Int) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    var isLoading by remember { mutableStateOf(false) }
    val potluckItemRegUnRegUiState by viewModel.potluckItemRegUnRegUiState.collectAsState()

    if (potluckItemRegUnRegUiState != EventActionUiState.PENDING) {
        isLoading = false
    }

    Row (
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
            .clip( RoundedCornerShape(8.dp))
            .shadow(elevation = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp)
                        .weight(.8f),
                    text = potluckItem.itemName,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = PBCFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier.weight(.2f),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isLoading) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (potluckItem.availableCount > potluckItem.contributorList.size) {
                                Image(
                                    painter = painterResource(Res.drawable.event_icon_add_item),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clickable {
                                            isLoading = true
                                            viewModel.signUpForPotluckItem(potluckItem)
                                        }
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            if (potluckItem.contributorList.isNotEmpty()) {
                                Image(
                                    painter = painterResource(Res.drawable.event_icon_remove_item),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clickable {
                                            isLoading = true
                                            viewModel.signOutFromPotluckItem(potluckItem)
                                        }
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .width(35.dp)
                                .height(35.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            Row (
                modifier = Modifier
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { potluckItem.completionProgress() },
                    color = themeAdditionalColors.quaternary,
                    trackColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .weight(.85f)
                        .padding(start = 4.dp, end = 8.dp)
                )

                Text(
                    modifier = Modifier
                        .weight(.15f),
                    text = "${potluckItem.contributorList.size}/${potluckItem.availableCount}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = PBCFontFamily,
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (currentUserContributions != 0) {
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                        .fillMaxWidth(),
                    text = stringResource(Res.string.event_label_user_contribution_count)
                        .replace("%s", potluckItem.itemName)
                        .replace("%d", currentUserContributions.toString()),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = PBCFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}