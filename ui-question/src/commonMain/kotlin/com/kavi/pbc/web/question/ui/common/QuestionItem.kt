package com.kavi.pbc.web.question.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.question.Question
import org.jetbrains.compose.resources.painterResource
import pbcwebapp.ui_question.generated.resources.Res
import pbcwebapp.ui_question.generated.resources.icon_question_delete
import pbcwebapp.ui_question.generated.resources.icon_question_edit

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun QuestionItem(modifier: Modifier = Modifier,
                 question: Question,
                 isOwnerQuestion: Boolean = false,
                 onClick:() -> Unit,
                 onDelete:(() -> Unit)? = null, onModify:(() -> Unit)? = null) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    BoxWithConstraints (
        modifier = modifier.padding(top = 2.dp)
    ) {
        val maxWith = this.maxWidth
        Row (
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
                .shadow(
                    elevation = 8.dp,
                    spotColor = themeAdditionalColors.shadow,
                    shape = RoundedCornerShape(8.dp),
                )
                .background(MaterialTheme.colorScheme.background)
                .clickable {
                    onClick.invoke()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = question.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = PBCFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = question.content,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (isOwnerQuestion) {
                    Row {
                        Text(
                            modifier = Modifier.weight(.75f),
                            text = "${question.answerList.size} Replies",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = PBCFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            modifier = Modifier.weight(.25f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.icon_question_edit),
                                contentDescription = "Edit Event",
                                tint = themeAdditionalColors.shadow,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(4.dp)
                                    .clickable {
                                        onModify?.invoke()
                                    }
                            )

                            Icon(
                                painter = painterResource(Res.drawable.icon_question_delete),
                                contentDescription = "Delete Event",
                                tint = themeAdditionalColors.shadow,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(4.dp)
                                    .clickable {
                                        onDelete?.invoke()
                                    }
                            )
                        }
                    }
                } else {
                    Row {
                        Text(
                            modifier = Modifier.weight(.65f),
                            text = "by ${question.author.firstName} ${question.author.lastName}",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = PBCFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            modifier = Modifier.weight(.35f),
                            text = "${question.answerList.size} Replies",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = PBCFontFamily,
                            textAlign = TextAlign.End,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}