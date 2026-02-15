package com.kavi.pbc.web.question.ui.common

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.question.Question
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.parent.extention.copy
import com.kavi.pbc.web.question.data.model.AddAnswerStatus
import com.kavi.pbc.web.question.ui.list.QuestionListViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_question.generated.resources.Res
import pbcwebapp.ui_question.generated.resources.question_icon_send
import pbcwebapp.ui_question.generated.resources.question_label_answers
import pbcwebapp.ui_question.generated.resources.question_label_your_answer

@Composable
fun SelectedQuestion(selectedQuestion: MutableState<Question>, viewModel: QuestionListViewModel) {

    viewModel.setSelectedQuestion(selectedQuestion.value)
    val newAnswerComment = remember { mutableStateOf(TextFieldValue("")) }

    val questionAnswerList by viewModel.answerCommentList.collectAsState()
    val addAnswerStatus by viewModel.addAnswerStatus.collectAsState()

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Column {
            Title(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp),
                titleText = "Q: ${selectedQuestion.value.title}",
            )

            Text(
                text = selectedQuestion.value.content,
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
                    text = "by ${selectedQuestion.value.author.firstName} ${selectedQuestion.value.author.lastName}",
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
                        model = selectedQuestion.value.author.profilePicUrl,
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

            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(questionAnswerList) { answerComment ->
                    AnswerCommentItem(answerComment = answerComment)
                }
                item {
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

                            if (addAnswerStatus == AddAnswerStatus.PENDING) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                AppIconButton(
                                    modifier = Modifier.padding(top = 8.dp),
                                    icon = painterResource(Res.drawable.question_icon_send),
                                    buttonSize = 50.dp
                                ) {
                                    val answer = newAnswerComment.value.text.copy()
                                    newAnswerComment.value = TextFieldValue("")
                                    viewModel.addAnswerCommentToQuestion(answer)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}