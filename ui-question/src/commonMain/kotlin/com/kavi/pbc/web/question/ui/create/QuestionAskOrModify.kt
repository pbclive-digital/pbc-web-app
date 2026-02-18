package com.kavi.pbc.web.question.ui.create

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kavi.pbc.web.common.ui.component.AppBasicDialog
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppOutlineMultiLineTextField
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.TitleWithAction
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.data.question.PrivacyStatus
import com.kavi.pbc.web.data.question.Question
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_question.generated.resources.Res
import pbcwebapp.ui_question.generated.resources.question_icon_x
import pbcwebapp.ui_question.generated.resources.question_label_create
import pbcwebapp.ui_question.generated.resources.question_label_create_new_question
import pbcwebapp.ui_question.generated.resources.question_label_modify
import pbcwebapp.ui_question.generated.resources.question_label_question_content
import pbcwebapp.ui_question.generated.resources.question_label_question_privacy
import pbcwebapp.ui_question.generated.resources.question_label_question_title
import pbcwebapp.ui_question.generated.resources.question_phrase_question_privacy

@Composable
fun QuestionAskOrModifyDialog(
    showDialog: MutableState<Boolean>,
    modifyQuestion: Question? = null,
    onCancel: () -> Unit
) {
    AppBasicDialog(
        modifier = Modifier.fillMaxSize(),
        showDialog = showDialog.value,
        onDismissRequest = {
            onCancel.invoke()
        }
    ) {
        QuestionAskOrModifyUI(modifyQuestion = modifyQuestion, onCancel = onCancel)
    }
}

@Composable
private fun QuestionAskOrModifyUI(
    modifyQuestion: Question?,
    onCancel: () -> Unit
) {

    val viewModel: QuestionAskOrModifyViewModel = viewModel { QuestionAskOrModifyViewModel() }
    var isModify by remember { mutableStateOf(false) }

    modifyQuestion?.let {
        isModify = true
        viewModel.setModifyingQuestion(question = it)
    }

    val askOrModifyQuestion by viewModel.askOrModifyQuestion.collectAsState()

    val askQuestionTitle = remember { mutableStateOf(TextFieldValue(askOrModifyQuestion?.title ?: "")) }
    val askQuestionContent = remember { mutableStateOf(TextFieldValue(askOrModifyQuestion?.content ?: "")) }
    var isPrivateQuestion by remember { mutableStateOf(
        askOrModifyQuestion?.privacy == PrivacyStatus.PRIVATE
    ) }

    BoxWithConstraints(
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = this.maxWidth
        val screenType = UIUtil.screenType(maxWidth = maxWidth)

        Column (
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when(screenType) {
                ScreenType.PHONE -> {
                    TitleWithAction(
                        titleText = stringResource(Res.string.question_label_create_new_question),
                        textSize = 40,
                        actionPainter = painterResource(Res.drawable.question_icon_x),
                        actionPainterSize = 30.dp,
                        isIcon = true,
                    ) {
                        viewModel.clearQuestion()
                        onCancel.invoke()
                    }
                }
                else -> {
                    TitleWithAction(
                        titleText = stringResource(Res.string.question_label_create_new_question),
                        actionPainter = painterResource(Res.drawable.question_icon_x),
                        actionPainterSize = 40.dp,
                        isIcon = true,
                    ) {
                        viewModel.clearQuestion()
                        onCancel.invoke()
                    }
                }
            }

            AppOutlineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                headingText = stringResource(Res.string.question_label_question_title),
                contentText = askQuestionTitle,
                onValueChange = { newValue ->
                    askQuestionTitle.value = newValue
                    viewModel.updateQuestionTitle(askQuestionTitle.value.text)
                }
            )

            AppOutlineMultiLineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .height(250.dp),
                headingText = stringResource(Res.string.question_label_question_content),
                contentText = askQuestionContent,
                maxLines = 20,
                onValueChange = { newValue ->
                    askQuestionContent.value = newValue
                    viewModel.updateQuestionContent(askQuestionContent.value.text)
                }
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 4.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = stringResource(Res.string.question_label_question_privacy),
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.weight(1f))

                Switch(
                    checked = isPrivateQuestion, // The current state of the switch
                    onCheckedChange = { newState ->
                        isPrivateQuestion = newState // Update the state when the user interacts with the switch
                        viewModel.updatePrivacyStatus(isPrivateQuestion)
                    }
                )
            }

            Text(
                text = stringResource(Res.string.question_phrase_question_privacy),
                fontFamily = PBCFontFamily,
                fontSize = 12.sp,
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxWidth()
            )

            AppFilledButton(
                label = if (isModify) stringResource(Res.string.question_label_modify)
                else stringResource(Res.string.question_label_create),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                viewModel.createOrModifyQuestion(isModify = isModify)
            }
        }
    }
}