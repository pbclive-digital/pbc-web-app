package com.kavi.pbc.web.question.ui.sheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.component.AppIconButton
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.Title
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.question.Question
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.question.ui.common.AnswerCommentItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_question.generated.resources.Res
import pbcwebapp.ui_question.generated.resources.question_icon_send
import pbcwebapp.ui_question.generated.resources.question_label_answers
import pbcwebapp.ui_question.generated.resources.question_label_your_answer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionSelectedBottomSheetUI(
    sheetState: SheetState,
    showSheet: MutableState<Boolean>,
    selectedQuestion: Question
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    val newAnswerComment = remember { mutableStateOf(TextFieldValue("")) }

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
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Title(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp),
                    titleText = "Q: ${selectedQuestion.title}",
                    textSize = 32
                )

                Text(
                    text = selectedQuestion.content,
                    fontFamily = PBCFontFamily,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 12.dp, end = 12.dp)
                        .fillMaxWidth()
                )

                Row (
                    modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "by ${selectedQuestion.author.firstName} ${selectedQuestion.author.lastName}",
                        fontFamily = PBCFontFamily,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Justify,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .border(
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.tertiary
                                ),
                                shape = CircleShape
                            )
                    ) {
                        AsyncImage(
                            model = selectedQuestion.author.profilePicUrl,
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(5.dp)
                                .clip(CircleShape)
                        )
                    }
                }

                Text(
                    text = stringResource(Res.string.question_label_answers),
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 8.dp)
                        .fillMaxWidth()
                )

                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    selectedQuestion.answerList.forEach { answerComment ->
                        AnswerCommentItem(answerComment = answerComment)
                    }

                    if (Session.isLogIn()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppOutlineTextField(
                                modifier = Modifier
                                    .weight(1f),
                                headingText = stringResource(Res.string.question_label_your_answer),
                                contentText = newAnswerComment,
                                onValueChange = { newValue ->
                                    newAnswerComment.value = newValue
                                }
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            AppIconButton(
                                modifier = Modifier.padding(top = 8.dp),
                                icon = painterResource(Res.drawable.question_icon_send),
                                buttonSize = 50.dp
                            ) {

                            }
                        }
                    }
                }
            }
        }
    }
}