package com.kavi.pbc.web.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.lib_common_ui.generated.resources.Res
import pbcwebapp.lib_common_ui.generated.resources.common_label_loading

@Composable
fun AppFullScreenLoader(
    isWithBackground: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isWithBackground) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun AppLoadingDialog(
    showLoadingDialog: MutableState<Boolean>,
    loadingMessage: String = stringResource(Res.string.common_label_loading)
) {
    AppBasicDialog(
        modifier = Modifier
            .height(250.dp)
            .width(250.dp),
        showDialog = showLoadingDialog.value,
        onDismissRequest = {
            // Nothing
        }
    ) {
        Column (
            modifier = Modifier
                .height(250.dp)
                .width(250.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                text = loadingMessage,
                fontFamily = PBCFontFamily,
                fontSize = 22.sp
            )
        }
    }
}