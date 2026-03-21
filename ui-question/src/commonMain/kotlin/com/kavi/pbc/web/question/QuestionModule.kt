package com.kavi.pbc.web.question

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.kavi.pbc.web.parent.contract.model.QuestionContract
import com.kavi.pbc.web.question.ui.list.QuestionListUI

class QuestionModule: QuestionContract {
    @Composable
    override fun GetQuestionList(navController: NavController) {
        QuestionListUI(navController = navController)
    }
}