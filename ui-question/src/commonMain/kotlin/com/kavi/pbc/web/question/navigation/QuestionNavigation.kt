package com.kavi.pbc.web.question.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kavi.pbc.web.parent.navigation.QuestionPath
import com.kavi.pbc.web.pbc.container.PBCPageContainer
import com.kavi.pbc.web.question.ui.open.OpenQuestinList

fun NavGraphBuilder.questionNavGraph(navController: NavHostController) {
    navigation(startDestination = QuestionPath.QuestionList.toString(), route = QuestionPath.ROUTE) {
        // Path: questin/question-list-ui
        composable<QuestionPath.QuestionList> {
            PBCPageContainer {
                OpenQuestinList(navController = navController)
            }
        }
    }
}