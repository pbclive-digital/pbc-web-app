package com.kavi.pbc.web.news.ui.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.component.AppBasicDialog
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppOutlineMultiLineTextField
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.ErrorMessageBalloon
import com.kavi.pbc.web.common.ui.component.SuccessMessageBalloon
import com.kavi.pbc.web.common.ui.component.TitleWithAction
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.news.data.model.NewsCreateOrModifyUiState
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.util.toImageBitmap
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_news.generated.resources.Res
import pbcwebapp.ui_news.generated.resources.news_icon_add_image
import pbcwebapp.ui_news.generated.resources.news_icon_close_x
import pbcwebapp.ui_news.generated.resources.news_label_content
import pbcwebapp.ui_news.generated.resources.news_label_create
import pbcwebapp.ui_news.generated.resources.news_label_create_news
import pbcwebapp.ui_news.generated.resources.news_label_headline
import pbcwebapp.ui_news.generated.resources.news_label_link
import pbcwebapp.ui_news.generated.resources.news_label_modify
import pbcwebapp.ui_news.generated.resources.news_label_modify_news
import pbcwebapp.ui_news.generated.resources.news_label_pick_image
import pbcwebapp.ui_news.generated.resources.news_phrase_create_or_modify_empty_fields
import pbcwebapp.ui_news.generated.resources.news_phrase_create_or_modify_failure
import pbcwebapp.ui_news.generated.resources.news_phrase_create_or_modify_success

@Composable
fun CreateOrModifyNewsDialog(
    showDialog: MutableState<Boolean>,
    modifyNews: News? = null,
    onCreateOrModify: () -> Unit,
    onDismiss: () -> Unit
) {
    AppBasicDialog(
        showDialog = showDialog.value,
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        CreateOrModifyNewsContent(
            modifyNews = modifyNews,
            onCreateOrModify = onCreateOrModify,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun CreateOrModifyNewsContent(
    modifyNews: News? = null,
    onCreateOrModify: () -> Unit, onDismiss: () -> Unit) {

    val viewModel: CreateOrModifyNewsViewModel = viewModel { CreateOrModifyNewsViewModel() }
    var isModify by remember { mutableStateOf(false) }

    modifyNews?.let {
        isModify = true
        viewModel.setModifyNews(news = it)
    }

    var anyModifySuccess by remember { mutableStateOf(false) }

    val createOrModifyNews by viewModel.createOrModifyNews.collectAsState()
    val newsCreationOrModifyState by viewModel.newsCreationOrModifyState.collectAsState()

    val newsHeadline = remember { mutableStateOf(TextFieldValue(createOrModifyNews?.title?: "")) }
    val newsContent = remember { mutableStateOf(TextFieldValue(createOrModifyNews?.content?: "")) }
    val facebookLink = remember { mutableStateOf(TextFieldValue(
        createOrModifyNews?.facebookLink ?: run { "" }
    )) }

    val errorBalloonVisibility = remember { mutableStateOf(false) }
    var errorBalloonMessage by remember { mutableStateOf("") }
    val successBalloonVisibility = remember { mutableStateOf(false) }

    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    val scope = rememberCoroutineScope()
    val imagePickerLauncher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        title = "Select Image for NEWS"
    ) { platformFile ->
        // Handle the selected file
        platformFile?.let { imageFile ->
            scope.launch {
                // Read the file and convert it to ImageBitmap
                selectedImage = imageFile.toImageBitmap()
                viewModel.updateNewsImageFile(imageFile)
            }
        }
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 30.dp)
                    .verticalScroll(state = rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TitleWithAction(
                    titleText = if (isModify)
                        stringResource(Res.string.news_label_create_news)
                    else
                        stringResource(Res.string.news_label_modify_news),
                    actionPainter = painterResource(Res.drawable.news_icon_close_x),
                    actionPainterSize = 40.dp,
                    isIcon = true,
                ) {
                    viewModel.clearNews()
                    errorBalloonVisibility.value = false
                    viewModel.revokeCreateOrModifyUiStatus()
                    //onCancel.invoke(anyModifySuccess)
                    onDismiss.invoke()
                }

                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    // Error or Success message balloon
                    ErrorMessageBalloon(
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        showBalloon = errorBalloonVisibility,
                        errorMessage = errorBalloonMessage,
                        onDismiss = {
                            errorBalloonVisibility.value = false
                            viewModel.revokeCreateOrModifyUiStatus()
                        }
                    )
                    SuccessMessageBalloon(
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        showBalloon = successBalloonVisibility,
                        successMessage = stringResource(Res.string.news_phrase_create_or_modify_success),
                        onDismiss = {
                            successBalloonVisibility.value = false
                            viewModel.revokeCreateOrModifyUiStatus()
                        }
                    )
                }

                Row (
                    modifier = Modifier.height(IntrinsicSize.Max)
                ) {
                    Column (
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(.7f)
                    ) {
                        AppOutlineTextField (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            headingText = stringResource(Res.string.news_label_headline).uppercase(),
                            contentText = newsHeadline,
                            onValueChange = { newValue ->
                                newsHeadline.value = newValue
                                viewModel.updateNewsHeadline(newsHeadline.value.text)
                            }
                        )

                        AppOutlineMultiLineTextField (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .height(250.dp),
                            headingText = stringResource(Res.string.news_label_content).uppercase(),
                            contentText = newsContent,
                            maxLines = 20,
                            onValueChange = { newValue ->
                                newsContent.value = newValue
                                viewModel.updateNewsContent(newsContent.value.text)
                            }
                        )

                        AppOutlineTextField (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            headingText = stringResource(Res.string.news_label_link).uppercase(),
                            contentText = facebookLink,
                            onValueChange = { newValue ->
                                facebookLink.value = newValue
                                viewModel.updateNewsLink(facebookLink.value.text)
                            }
                        )
                    }

                    Column (
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(.3f)
                            .fillMaxSize(),
                    ) {
                        Text(
                            text = stringResource(Res.string.news_label_pick_image),
                            fontFamily = PBCFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth()
                        )

                        Box (
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.news_icon_add_image),
                                contentDescription = "Adding news image icon",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clickable {
                                        // Open up image selection from machine
                                        imagePickerLauncher.launch()
                                    }
                            )

                            selectedImage?.let {
                                Box (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        bitmap = it,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .size(250.dp)
                                    )
                                }
                            }?: run {
                                createOrModifyNews?.newsImage?.let {
                                    Box (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            model = it,
                                            contentDescription = "Profile Picture",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(250.dp)
                                                .padding(5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                AppFilledButton(
                    label = if (isModify) stringResource(Res.string.news_label_modify)
                    else stringResource(Res.string.news_label_create),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    viewModel.uploadNewsImageAndCreateOrModifyNews(isModify = isModify)
                }
            }
        }
    }

    when(newsCreationOrModifyState) {
        NewsCreateOrModifyUiState.NONE -> {
            //errorBalloonVisibility.value = false
        }
        NewsCreateOrModifyUiState.PENDING -> {}
        NewsCreateOrModifyUiState.FAILURE -> {
            errorBalloonVisibility.value = true
            errorBalloonMessage = stringResource(Res.string.news_phrase_create_or_modify_failure)
        }
        NewsCreateOrModifyUiState.EMPTY_FIELD -> {
            errorBalloonVisibility.value = true
            errorBalloonMessage = stringResource(Res.string.news_phrase_create_or_modify_empty_fields)
        }
        NewsCreateOrModifyUiState.SUCCESS -> {
            anyModifySuccess = true
            successBalloonVisibility.value = true

            // Clear the question form
            newsHeadline.value = TextFieldValue("")
            newsContent.value = TextFieldValue("")
            facebookLink.value = TextFieldValue("")
            selectedImage = null
        }
    }
}