package com.kavi.pbc.web.pbc.container.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.component.AppLinkButton
import com.kavi.pbc.web.parent.navigation.DashboardPath
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.lib_pbc_container.generated.resources.Res
import pbcwebapp.lib_pbc_container.generated.resources.container_label_about_us
import pbcwebapp.lib_pbc_container.generated.resources.container_label_contact_us

@Composable
fun AdditionalActionComponent(navController: NavController) {
    Row (
        horizontalArrangement = Arrangement.End
    ) {
        AppLinkButton(
            label = stringResource(Res.string.container_label_contact_us),
            color = MaterialTheme.colorScheme.onPrimary
        ) {
            navController.navigate(DashboardPath.ContactUs)
        }

        Spacer(modifier = Modifier.width(12.dp))

        AppLinkButton(
            label = stringResource(Res.string.container_label_about_us),
            color = MaterialTheme.colorScheme.onPrimary
        ) {
            navController.navigate(DashboardPath.AboutUs)
        }
    }
}