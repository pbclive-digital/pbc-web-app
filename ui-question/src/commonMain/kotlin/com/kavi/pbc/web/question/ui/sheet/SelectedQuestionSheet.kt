package com.kavi.pbc.web.question.ui.sheet


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.data.question.Question
import com.kavi.pbc.web.question.ui.common.SelectedQuestion
import com.kavi.pbc.web.question.ui.list.QuestionListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionSelectedBottomSheetUI(
    sheetState: SheetState,
    showSheet: MutableState<Boolean>,
    viewModel: QuestionListViewModel,
    selectedQuestion: MutableState<Question>
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
        SelectedQuestion(selectedQuestion = selectedQuestion, viewModel = viewModel)
    }
}