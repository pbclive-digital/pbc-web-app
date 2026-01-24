package com.kavi.pbc.web.question.ui.open

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.component.AppFullScreenLoader
import com.kavi.pbc.web.common.ui.component.AppIconButton
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.Title
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.question.Question
import com.kavi.pbc.web.parent.util.ScreenType
import com.kavi.pbc.web.parent.util.UIUtil
import com.kavi.pbc.web.question.data.model.OpenQuestionListUiState
import com.kavi.pbc.web.question.ui.common.AnswerCommentItem
import com.kavi.pbc.web.question.ui.common.QuestionItem
import com.kavi.pbc.web.question.ui.sheet.QuestionSelectedBottomSheetUI
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_question.generated.resources.Res
import pbcwebapp.ui_question.generated.resources.icon_send
import pbcwebapp.ui_question.generated.resources.label_question_answers
import pbcwebapp.ui_question.generated.resources.label_question_open_question_empty
import pbcwebapp.ui_question.generated.resources.label_question_your_answer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenQuestinList(navController: NavController) {

    val viewModel: OpenQuestionListViewModel = viewModel { OpenQuestionListViewModel() }

    val openQuestionListUiState by viewModel.openQuestionListUiState.collectAsState()
    val openQuestionList by viewModel.openQuestionList.collectAsState()
    val pageIndex by viewModel.pageIndex.collectAsState()

    val selectedQuestion = remember { mutableStateOf(Question()) }

    val selectedQuestionSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val showQuestionSheet = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchOpenQuestionList()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth

        val screenType = UIUtil.screenType(maxWidth = maxWidth)

        when(openQuestionListUiState) {
            OpenQuestionListUiState.NONE -> {}
            OpenQuestionListUiState.EMPTY -> {
                EmptyOpenQuestionList()
            }
            OpenQuestionListUiState.PENDING -> {
                AppFullScreenLoader(isWithBackground = false)
            }
            OpenQuestionListUiState.FAILURE -> {}
            OpenQuestionListUiState.SUCCESS -> {
                Row {
                    when(screenType) {
                        ScreenType.PHONE -> {
                            Column(
                                modifier = Modifier
                                    .height(maxHeight)
                                    .padding(top = 10.dp, end = 15.dp)
                            ) {
                                LazyColumn {
                                    items(openQuestionList) { question ->
                                        QuestionItem(
                                            question = question, onClick = {
                                                selectedQuestion.value = question
                                                showQuestionSheet.value = true
                                            }
                                        )
                                    }
                                    item {
                                        LaunchedEffect(pageIndex) {
                                            viewModel.fetchOpenQuestionList()
                                        }
                                    }
                                }
                            }
                        }
                        else -> {
                            selectedQuestion.value = openQuestionList[0]
                            Column(
                                modifier = Modifier
                                    .weight(.35f)
                                    .height(maxHeight)
                                    .padding(top = 10.dp, end = 15.dp)
                            ) {
                                LazyColumn {
                                    items(openQuestionList) { question ->
                                        QuestionItem(
                                            question = question, onClick = {
                                                selectedQuestion.value = question
                                            }
                                        )
                                    }
                                    item {
                                        LaunchedEffect(pageIndex) {
                                            viewModel.fetchOpenQuestionList()
                                        }
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(.65f)
                                    .padding(top = 10.dp, start = 15.dp)
                            ) {
                                SelectedQuestion(selectedQuestion = selectedQuestion)
                            }
                        }
                    }
                }
            }
        }

        if (showQuestionSheet.value) {
            QuestionSelectedBottomSheetUI(
                sheetState = selectedQuestionSheetState,
                showSheet = showQuestionSheet,
                selectedQuestion = selectedQuestion.value
            )
        }
    }
}

@Composable
private fun SelectedQuestion(selectedQuestion: MutableState<Question>) {

    val newAnswerComment = remember { mutableStateOf(TextFieldValue("")) }

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(40.dp)
    ) {
        Column (
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
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
                text = stringResource(Res.string.label_question_answers),
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
                selectedQuestion.value.answerList.forEach { answerComment ->
                    AnswerCommentItem(answerComment = answerComment)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppOutlineTextField (
                        modifier = Modifier
                            .weight(1f),
                        headingText = stringResource(Res.string.label_question_your_answer),
                        contentText = newAnswerComment,
                        onValueChange = { newValue ->
                            newAnswerComment.value = newValue
                        }
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    AppIconButton(
                        modifier = Modifier.padding(top = 8.dp),
                        icon = painterResource(Res.drawable.icon_send),
                        buttonSize = 50.dp
                    ) {

                    }
                }
            }
        }
    }
}

@Composable
fun EmptyOpenQuestionList() {
    Box (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = 130.dp, start = 16.dp, end = 16.dp, bottom = 30.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.label_question_open_question_empty),
            textAlign = TextAlign.Center,
        )
    }
}