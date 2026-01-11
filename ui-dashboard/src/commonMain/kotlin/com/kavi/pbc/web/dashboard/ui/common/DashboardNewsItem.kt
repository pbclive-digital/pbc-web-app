package com.kavi.pbc.web.dashboard.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppLinkButton
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.news.News
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_dashboard.generated.resources.Res
import pbcwebapp.ui_dashboard.generated.resources.label_news_read_more

@Composable
fun DashboardNewsItem(modifier: Modifier = Modifier, news: News, onReadMore: () -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = news.title,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = PBCFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = news.content,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = PBCFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Row (modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "by ${news.author.name}",
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
            )
            Spacer(modifier = Modifier.weight(1f))
            AppLinkButton(
                label = stringResource(Res.string.label_news_read_more),
                color = MaterialTheme.colorScheme.secondary,
            ) {
                onReadMore.invoke()
            }
        }
    }
}