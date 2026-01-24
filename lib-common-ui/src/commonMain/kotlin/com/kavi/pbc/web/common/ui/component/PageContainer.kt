package com.kavi.pbc.web.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.lib_common_ui.generated.resources.Res
import pbcwebapp.lib_common_ui.generated.resources.image_common_pbc
import pbcwebapp.lib_common_ui.generated.resources.label_common_pbc_name

@Composable
fun PageContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val maxWidth = this.maxWidth
        val screenType = UIUtil.screenType(maxWidth)

        val sidePadding = when (screenType) {
            ScreenType.PHONE -> 8.dp
            ScreenType.TABLET, ScreenType.COMPUTER -> {
                (maxWidth.value * .1).dp
            }
        }

        Column {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column {
                    Row (
                        modifier = Modifier.padding(start = sidePadding, end = sidePadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box (
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(90.dp),
                                painter = painterResource(Res.drawable.image_common_pbc),
                                contentDescription = "PBC image with name"
                            )
                        }

                        Column (
                            modifier = Modifier.padding(start = 20.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Title(
                                titleText = stringResource(Res.string.label_common_pbc_name),
                                textSize = 32,
                                textColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Box (
                modifier = Modifier.padding(start = sidePadding, end = sidePadding),
                contentAlignment = Alignment.Center,
            ) {
                content.invoke()
            }
        }
    }
}