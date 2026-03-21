package com.kavi.pbc.web.question.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.parent.navigation.QuestionPath
import com.kavi.pbc.web.pbc.container.PBCPageContainer
import com.kavi.pbc.web.question.ui.list.OpenQuestinList

fun NavGraphBuilder.questionNavGraph(navController: NavHostController) {

    // Retrieve User if user signed-in to the application
    var user: User? = null
    ContractServiceLocator.locate(AuthContract::class).retrieveUser(onSuccess = {
        user = it
    }, onFailure = { /* Do nothing */ })

    navigation(startDestination = QuestionPath.QuestionList.toString(), route = QuestionPath.ROUTE) {
        // Path: questin/question-list-ui
        composable<QuestionPath.QuestionList> {
            PBCPageContainer (user = user) {
                OpenQuestinList(navController = navController)
            }
        }
    }
}