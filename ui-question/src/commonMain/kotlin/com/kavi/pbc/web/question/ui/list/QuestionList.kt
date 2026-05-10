package com.kavi.pbc.web.question.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppFullScreenLoader
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.question.Question
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.question.data.model.QuestionListUiState
import com.kavi.pbc.web.question.ui.common.QuestionItem
import com.kavi.pbc.web.question.ui.common.SelectedQuestion
import com.kavi.pbc.web.question.ui.create.QuestionAskOrModifyDialog
import com.kavi.pbc.web.question.ui.sheet.QuestionSelectedBottomSheetUI
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_question.generated.resources.Res
import pbcwebapp.ui_question.generated.resources.question_label_create_question
import pbcwebapp.ui_question.generated.resources.question_label_no_selected_question
import pbcwebapp.ui_question.generated.resources.question_label_open_question
import pbcwebapp.ui_question.generated.resources.question_label_personal_question
import pbcwebapp.ui_question.generated.resources.question_label_personal_question_empty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionListUI(navController: NavController) {

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

        when (openQuestionListUiState) {
            QuestionListUiState.NONE -> {}
            QuestionListUiState.PENDING -> {
                AppFullScreenLoader(isWithBackground = false)
            }
            else -> {
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
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(
                                modifier = Modifier
                                    .weight(.65f)
                                    .padding(top = 10.dp, start = 15.dp)
                            ) {
                                if (selectedQuestion.value.id.isNullOrEmpty()) {
                                    EmptySelectedQuestion(stringResource(Res.string.question_label_no_selected_question))
                                } else {
                                    SelectedQuestion(
                                        selectedQuestion = selectedQuestion,
                                        viewModel = viewModel
                                    )
                                }
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

    val showCreateQuestionDialog = remember { mutableStateOf(false) }
    val showAuthInviteDialog = remember { mutableStateOf(false) }
    val showSignUpDialog = remember { mutableStateOf(false) }

    Column {
        AppFilledButton(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.question_label_create_question)
        ) {
            if (Session.isLogIn()) {
                showCreateQuestionDialog.value = true
            } else
                showAuthInviteDialog.value = true
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(if (selectedPagerIndex == 0) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surface)
                    .clickable {
                        selectedPagerIndex = 0
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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
                    .height(40.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(if (selectedPagerIndex == 1) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surface)
                    .clickable {
                        selectedPagerIndex = 1
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.question_label_personal_question),
                    fontFamily = PBCFontFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
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

    // Create New question block
    if (showCreateQuestionDialog.value) {
        QuestionAskOrModifyDialog(
            showDialog = showCreateQuestionDialog,
            onCancel = { refreshRequired ->
                showCreateQuestionDialog.value = false

                // Some update happens in questions, therefore refresh-required
                if (refreshRequired) {
                    viewModel.fetchOpenQuestionList(forceFetch = true)
                    viewModel.fetchPersonalQuestionList()
                }
            },
            modifyQuestion = null)
    }

    if (showAuthInviteDialog.value) {
        ContractServiceLocator.locate(AuthContract::class).ProvideCompleteSignInFlow(
            showDialog = showAuthInviteDialog,
        ) {
            showSignUpDialog.value = true
        }
    }

    if (showSignUpDialog.value) {
        ContractServiceLocator.locate(AuthContract::class).ProvideCompleteSignUpFlow(
            showDialog = showSignUpDialog
        )
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

    val questionListUiState by viewModel.openQuestionListUiState.collectAsState()
    val openQuestionList by viewModel.openQuestionList.collectAsState()

    Column(
        modifier = modifier
            .padding(top = 10.dp)
    ) {
        when(questionListUiState) {
            QuestionListUiState.NONE, QuestionListUiState.FAILURE, QuestionListUiState.EMPTY -> {
                EmptyQuestionList(
                    emptyMessage = stringResource(Res.string.question_label_personal_question_empty)
                )
            }
            QuestionListUiState.PENDING -> {}
            QuestionListUiState.SUCCESS -> {
                selectedQuestion.value = openQuestionList[0]
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

    val showCreateQuestionDialog = remember { mutableStateOf(false) }
    var modifyingQuestion: Question? by remember { mutableStateOf(null) }

    Column(
        modifier = modifier
            .padding(top = 10.dp)
    ) {
        when (personalQuestionUiState) {
            QuestionListUiState.NONE, QuestionListUiState.FAILURE, QuestionListUiState.EMPTY -> {
                EmptyQuestionList(
                    emptyMessage = stringResource(Res.string.question_label_personal_question_empty)
                )
            }
            QuestionListUiState.PENDING -> {}
            QuestionListUiState.SUCCESS -> {
                LazyColumn {
                    items(personalQuestionList) { question ->
                        QuestionItem(
                            question = question, isOwnerQuestion = true, onClick = {
                                onQuestionSelect.invoke(question)
                            },
                            onModify = {
                                showCreateQuestionDialog.value = true
                                modifyingQuestion = question
                            },
                            onDelete = {

                            }
                        )
                    }
                }
            }
        }
    }

    // Modify Question Block
    if (showCreateQuestionDialog.value) {
        QuestionAskOrModifyDialog(
            showDialog = showCreateQuestionDialog,
            onCancel = { refreshRequired ->
                showCreateQuestionDialog.value = false

                // Some update happens in questions, therefore refresh-required
                if (refreshRequired) {
                    viewModel.fetchOpenQuestionList(forceFetch = true)
                    viewModel.fetchPersonalQuestionList()
                }
            },
            modifyQuestion = modifyingQuestion)
    }
}

@Composable
fun EmptySelectedQuestion(emptyMessage: String) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        /*modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 30.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background),*/
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
fun EmptyQuestionList(emptyMessage: String) {
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