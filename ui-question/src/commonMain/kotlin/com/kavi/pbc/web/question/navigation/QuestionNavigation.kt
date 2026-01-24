package com.kavi.pbc.web.question.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kavi.pbc.web.common.ui.component.PageContainer
import com.kavi.pbc.web.parent.navigation.QuestionPath
import com.kavi.pbc.web.question.ui.open.OpenQuestinList

fun NavGraphBuilder.questionNavGraph(navController: NavHostController) {
    navigation(startDestination = QuestionPath.QUESTION_LIST_UI, route = QuestionPath.ROUTE) {
        composable (route = QuestionPath.QUESTION_LIST_UI) {
            PageContainer {
                OpenQuestinList(navController = navController)
            }
        }
    }
}