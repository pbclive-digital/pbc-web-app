package com.kavi.pbc.web.question.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppFullScreenLoader
import com.kavi.pbc.web.common.ui.component.AppIconButton
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.Title
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.question.Question
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.parent.extention.copy
import com.kavi.pbc.web.question.data.model.AddAnswerStatus
import com.kavi.pbc.web.question.data.model.QuestionListUiState
import com.kavi.pbc.web.question.ui.common.AnswerCommentItem
import com.kavi.pbc.web.question.ui.common.QuestionItem
import com.kavi.pbc.web.question.ui.common.SelectedQuestion
import com.kavi.pbc.web.question.ui.sheet.QuestionSelectedBottomSheetUI
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_question.generated.resources.Res
import pbcwebapp.ui_question.generated.resources.question_icon_send
import pbcwebapp.ui_question.generated.resources.question_label_answers
import pbcwebapp.ui_question.generated.resources.question_label_create_question
import pbcwebapp.ui_question.generated.resources.question_label_open_question
import pbcwebapp.ui_question.generated.resources.question_label_open_question_empty
import pbcwebapp.ui_question.generated.resources.question_label_personal_question
import pbcwebapp.ui_question.generated.resources.question_label_personal_question_empty
import pbcwebapp.ui_question.generated.resources.question_label_your_answer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenQuestinList(navController: NavController) {

    val viewModel: QuestionListViewModel = viewModel { QuestionListViewModel() }

    val openQuestionListUiState by viewModel.openQuestionListUiState.collectAsState()

    val selectedQuestion = remember { mutableStateOf(Question()) }

    val selectedQuestionSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val showQuestionSheet = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchOpenQuestionList()
        viewModel.fetchPersonalQuestionList()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth

        val screenType = UIUtil.screenType(maxWidth = maxWidth)

        when(openQuestionListUiState) {
            QuestionListUiState.NONE -> {}
            QuestionListUiState.EMPTY -> {
                EmptyQuestionList(
                    emptyMessage = stringResource(Res.string.question_label_open_question_empty)
                )
            }
            QuestionListUiState.PENDING -> {
                AppFullScreenLoader(isWithBackground = false)
            }
            QuestionListUiState.FAILURE -> {}
            QuestionListUiState.SUCCESS -> {
                Row {
                    when(screenType) {
                        ScreenType.PHONE -> {
                            Column(
                                modifier = Modifier
                                    .height(maxHeight)
                                    .padding(top = 10.dp)
                            ) {
                                QuestionListPager(
                                    screenWidth = maxWidth,
                                    selectedQuestion = selectedQuestion,
                                    viewModel = viewModel,
                                    onQuestionSelect = { question ->
                                        selectedQuestion.value = question
                                        showQuestionSheet.value = true
                                    }
                                )
                            }
                        }
                        else -> {
                            Column (
                                modifier = Modifier
                                    .weight(.35f)
                                    .padding(top = 20.dp, start = 15.dp)
                            ) {
                                QuestionListPager(
                                    screenWidth = (maxWidth.value * (.35)).dp,
                                    selectedQuestion = selectedQuestion,
                                    viewModel = viewModel,
                                    onQuestionSelect = { question ->
                                        selectedQuestion.value = question
                                    }
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(.65f)
                                    .padding(top = 10.dp, start = 15.dp)
                            ) {
                                SelectedQuestion(selectedQuestion = selectedQuestion, viewModel = viewModel)
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
                selectedQuestion = selectedQuestion,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun QuestionListPager(
    screenWidth: Dp,
    selectedQuestion: MutableState<Question>,
    viewModel: QuestionListViewModel,
    onQuestionSelect: (question: Question) -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    var selectedPagerIndex by rememberSaveable { mutableIntStateOf(0) }
    val state = rememberPagerState { 2 }

    LaunchedEffect(selectedPagerIndex) {
        state.animateScrollToPage(selectedPagerIndex)
    }

    Column {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        selectedPagerIndex = 0
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.question_label_open_question),
                    fontFamily = PBCFontFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        selectedPagerIndex = 1
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.question_label_personal_question),
                    fontFamily = PBCFontFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Row {
            repeat(state.pageCount) { iteration ->
                val color = if (state.currentPage == iteration)
                    themeAdditionalColors.quaternary else MaterialTheme.colorScheme.surface

                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(5.dp)
                        .width(screenWidth / 2)
                        .background(color)
                )
            }
        }

        HorizontalPager(
            state = state,
            modifier = Modifier
                .padding(top = 10.dp),
            contentPadding = PaddingValues(horizontal = 0.dp),
            snapPosition = SnapPosition.Center
        ) { page ->
            when (page) {
                0 -> OpenQuestionListComponent(
                    modifier = Modifier,
                    selectedQuestion = selectedQuestion,
                    pageIndex = selectedPagerIndex,
                    viewModel = viewModel,
                    onQuestionSelect = onQuestionSelect
                )
                1 -> PersonalQuestionList(
                    modifier = Modifier,
                    viewModel = viewModel,
                    onQuestionSelect = onQuestionSelect
                )
            }
        }
    }
}

@Composable
private fun OpenQuestionListComponent(
    modifier: Modifier,
    selectedQuestion: MutableState<Question>,
    pageIndex: Int,
    viewModel: QuestionListViewModel,
    onQuestionSelect: (question: Question) -> Unit
) {

    val openQuestionList by viewModel.openQuestionList.collectAsState()

    selectedQuestion.value = openQuestionList[0]
    Column(
        modifier = modifier
            .padding(top = 10.dp, end = 15.dp)
    ) {
        LazyColumn {
            items(openQuestionList) { question ->
                QuestionItem(
                    question = question, onClick = {
                        onQuestionSelect.invoke(question)
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

@Composable
private fun PersonalQuestionList(
    modifier: Modifier,
    viewModel: QuestionListViewModel,
    onQuestionSelect: (question: Question) -> Unit
) {
    val personalQuestionUiState by viewModel.personalQuestionListUiState.collectAsState()
    val personalQuestionList by viewModel.personalQuestionList.collectAsState()

    Column(
        modifier = modifier
            .padding(top = 10.dp, end = 15.dp)
    ) {
        AppFilledButton(label = stringResource(Res.string.question_label_create_question)) {

        }

        when (personalQuestionUiState) {
            QuestionListUiState.NONE, QuestionListUiState.FAILURE -> {}
            QuestionListUiState.EMPTY -> {
                EmptyPersonalQuestionList(
                    emptyMessage = stringResource(Res.string.question_label_personal_question_empty)
                )
            }

            QuestionListUiState.PENDING -> {}
            QuestionListUiState.SUCCESS -> {
                LazyColumn {
                    items(personalQuestionList) { question ->
                        QuestionItem(
                            question = question, onClick = {
                                onQuestionSelect.invoke(question)
                            }
                        )
                    }
                }
            }
        }
    }
}

/*@Composable
private fun SelectedQuestion(selectedQuestion: MutableState<Question>, viewModel: QuestionListViewModel) {

    viewModel.setSelectedQuestion(selectedQuestion.value)
    val newAnswerComment = remember { mutableStateOf(TextFieldValue("")) }

    val questionAnswerList by viewModel.answerCommentList.collectAsState()
    val addAnswerStatus by viewModel.addAnswerStatus.collectAsState()

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(40.dp)
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
}*/

@Composable
fun EmptyQuestionList(emptyMessage: String) {
    Box (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 30.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = emptyMessage,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun EmptyPersonalQuestionList(emptyMessage: String) {
    Box (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 30.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = emptyMessage,
            textAlign = TextAlign.Center,
        )
    }
}