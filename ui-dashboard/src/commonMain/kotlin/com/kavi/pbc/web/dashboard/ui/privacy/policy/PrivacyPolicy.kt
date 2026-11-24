package com.kavi.pbc.web.dashboard.ui.privacy.policy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kavi.pbc.web.common.ui.component.AppFullScreenLoader
import com.kavi.pbc.web.common.ui.component.Title
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.dashboard.data.model.PolicyUiState
import com.kavi.pbc.web.dashboard.ui.common.PrivacyPolicyItem
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_dashboard.generated.resources.Res
import pbcwebapp.ui_dashboard.generated.resources.dashboard_label_privacy_policy
import pbcwebapp.ui_dashboard.generated.resources.dashboard_label_privacy_policy_last_updated
import pbcwebapp.ui_dashboard.generated.resources.dashboard_phrase_privacy_policy
import pbcwebapp.ui_dashboard.generated.resources.dashboard_phrase_privacy_policy_failure

@Composable
fun PrivacyPolicyUI() {

    val viewModel: PrivacyPolicyViewModel = viewModel { PrivacyPolicyViewModel() }

    val policyFetchStatus by viewModel.policyFetchStatus.collectAsState()
    val privacyPolicy by viewModel.privacyPolicy.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPrivacyPolicy()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = this.maxWidth

        when(policyFetchStatus) {
            PolicyUiState.NONE -> {}
            PolicyUiState.PENDING -> {
                AppFullScreenLoader(isWithBackground = false)
            }
            PolicyUiState.FAILURE -> {
                EmptyPolicyList()
            }
            PolicyUiState.SUCCESS -> {
                when (UIUtil.screenType(maxWidth)) {
                    ScreenType.PHONE -> {
                        LazyColumn {
                            item {
                                PolicyHeading(
                                    modifier = Modifier.padding(8.dp),
                                    lastUpdatedOn = privacyPolicy.lastUpdated
                                )
                            }
                            itemsIndexed(privacyPolicy.policyItemList) { index, policyItem ->
                                PrivacyPolicyItem(
                                    index = (index + 1), policyItem = policyItem
                                )
                                if (index < privacyPolicy.policyItemList.lastIndex) {
                                    Spacer(modifier = Modifier.height(8.dp).fillMaxWidth())
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                    ScreenType.TABLET, ScreenType.COMPUTER -> {
                        Column {
                            Spacer(modifier = Modifier.padding(12.dp))

                            PolicyHeading(lastUpdatedOn = privacyPolicy.lastUpdated)

                            Spacer(modifier = Modifier.padding(12.dp))

                            LazyColumn {
                                itemsIndexed(privacyPolicy.policyItemList) { index, policyItem ->
                                    PrivacyPolicyItem(
                                        index = (index + 1), policyItem = policyItem
                                    )
                                    if (index < privacyPolicy.policyItemList.lastIndex) {
                                        Spacer(modifier = Modifier.height(8.dp).fillMaxWidth())
                                    }
                                }
                                item {
                                    Spacer(modifier = Modifier.padding(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PolicyHeading(modifier: Modifier = Modifier, lastUpdatedOn: String) {
    Column (
        modifier = modifier
            .fillMaxWidth()
    ) {

        Title(
            titleText = stringResource(Res.string.dashboard_label_privacy_policy)
        )

        Text(
            text = "${stringResource(Res.string.dashboard_label_privacy_policy_last_updated)}: $lastUpdatedOn",
            fontFamily = PBCFontFamily,
            fontSize = 14.sp,
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
        )

        Text(
            text = stringResource(Res.string.dashboard_phrase_privacy_policy),
            fontFamily = PBCFontFamily,
            fontSize = 16.sp,
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun EmptyPolicyList() {
    Box (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = 100.dp, start = 16.dp, end = 16.dp, bottom = 30.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.dashboard_phrase_privacy_policy_failure),
            textAlign = TextAlign.Center,
        )
    }
}