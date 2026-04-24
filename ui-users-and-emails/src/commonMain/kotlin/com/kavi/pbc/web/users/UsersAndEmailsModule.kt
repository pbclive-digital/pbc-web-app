package com.kavi.pbc.web.users

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.kavi.pbc.web.parent.contract.model.UsersAndEmailsContract
import com.kavi.pbc.web.users.ui.emails.manage.EmailGroupManageUI
import com.kavi.pbc.web.users.ui.users.manage.UserManageUI

class UsersAndEmailsModule: UsersAndEmailsContract {

    @Composable
    override fun GetUserManageUI(navController: NavController) {
        UserManageUI(navController = navController)
    }

    @Composable
    override fun GetEmailGroupManageUI(navController: NavController) {
        EmailGroupManageUI(navController = navController)
    }
}