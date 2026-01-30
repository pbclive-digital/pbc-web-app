package com.kavi.pbc.web.news.ui.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.parent.extention.openUrl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_news.generated.resources.Res
import pbcwebapp.ui_news.generated.resources.icon_news_pbc
import pbcwebapp.ui_news.generated.resources.label_news_reference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSelectedBottomSheetUI(
    sheetState: SheetState,
    showSheet: MutableState<Boolean>,
    selectedNews: News
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

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
            Column (
                modifier = Modifier
                    .fillMaxHeight(.75f)
            ) {
                Text(
                    text = selectedNews.title,
                    fontFamily = PBCFontFamily,
                    fontSize = 36.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Text(
                    text = "on ${selectedNews.getFormatPublishedDate()}",
                    fontFamily = PBCFontFamily,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                Text(
                    text = "by ${selectedNews.author.name}",
                    fontFamily = PBCFontFamily,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(2.dp),
                    thickness = 2.dp
                )

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .shadow(
                                    elevation = 12.dp,
                                    spotColor = themeAdditionalColors.shadow
                                ),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            AsyncImage(
                                model = selectedNews.newsImage,
                                error = painterResource(Res.drawable.icon_news_pbc),
                                contentDescription = null, // decorative image
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .background(color = MaterialTheme.colorScheme.background)
                            )
                        }
                    }

                    Text(
                        text = selectedNews.content,
                        fontFamily = PBCFontFamily,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )

                    selectedNews.facebookLink?.let {
                        Text(
                            text = stringResource(Res.string.label_news_reference),
                            fontFamily = PBCFontFamily,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Justify,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        )

                        Text(
                            text = it,
                            fontFamily = PBCFontFamily,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            textDecoration = TextDecoration.Underline,
                            color = themeAdditionalColors.quaternary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp)
                                .clickable {
                                    openUrl(url = it)
                                }
                        )
                    }
                }
            }
        }
    }
}