package com.kavi.pbc.web.dashboard.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.dashboard.data.model.TabItem
import com.kavi.pbc.web.dashboard.ui.event.EventsUI
import com.kavi.pbc.web.dashboard.ui.news.NewsUI
import com.kavi.pbc.web.dashboard.ui.appointment.AppointmentUI
import com.kavi.pbc.web.dashboard.ui.question.QuestionUI
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.parent.util.ScreenType
import com.kavi.pbc.web.parent.util.UIUtil
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_dashboard.generated.resources.Res
import pbcwebapp.ui_dashboard.generated.resources.icon_appointment
import pbcwebapp.ui_dashboard.generated.resources.icon_ask_question
import pbcwebapp.ui_dashboard.generated.resources.icon_event
import pbcwebapp.ui_dashboard.generated.resources.icon_news
import pbcwebapp.ui_dashboard.generated.resources.image_dhamma_chakra
import pbcwebapp.ui_dashboard.generated.resources.image_pbc
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_pbc

@Composable
fun DashboardUI(navController: NavController) {
    val tabItemList = listOf(
        /*TabItem(name = "Home", icon = Res.drawable.icon_lotus),*/ // TODO - Keep this for future use
        TabItem(name = "Events", icon = Res.drawable.icon_event),
        TabItem(name = "News", icon = Res.drawable.icon_news),
        TabItem(name = "Appointments", icon = Res.drawable.icon_appointment),
        TabItem(name = "Questions", icon = Res.drawable.icon_ask_question)
    )
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState()),
    ) {
        val maxWidth = this.maxWidth

        val sidePadding = when (UIUtil.screenType(maxWidth)) {
            ScreenType.PHONE -> 8.dp
            ScreenType.TABLET, ScreenType.COMPUTER -> {
                (maxWidth.value * .1).dp
            }
        }

        Column {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))

                    Row (
                        modifier = Modifier.padding(start = sidePadding, end = sidePadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box (
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(90.dp),
                                painter = painterResource(Res.drawable.image_pbc),
                                contentDescription = "PBC image with name"
                            )
                        }

                        Column (
                            modifier = Modifier.padding(start = 20.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(Res.string.label_dashboard_pbc),
                                fontFamily = PBCFontFamily,
                                fontSize = 32.sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Box (
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(
                                    border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.tertiary),
                                    shape = CircleShape
                                )
                                .clickable {
                                    //profileAction.invoke()
                                    ContractServiceLocator.locate(AuthContract::class).signInWithFirebaseGoogle()
                                }
                        ) {
                            AsyncImage(
                                model = "https://res.cloudinary.com/demo/image/upload/v1312461204/sample.jpg",
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(Res.drawable.image_dhamma_chakra),
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(5.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
            }
        }

        Column (
            modifier = Modifier
                .padding(top = 160.dp, start = sidePadding, end = sidePadding)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .weight(.85f)
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp), spotColor = MaterialTheme.colorScheme.scrim)
                ) {
                    tabItemList.forEachIndexed { index, tabItem ->
                        NavigationBarItem(
                            modifier = Modifier
                                .padding(4.dp),
                            colors = navigationBarColors(),
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            label = { Text(tabItem.name) },
                            icon = {
                                Icon(
                                    painterResource(tabItem.icon),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .width(45.dp)
                                        .height(45.dp)
                                        .padding(8.dp),
                                )
                            }
                        )
                    }
                }
            }

            TabContent(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .fillMaxSize(),
                navController = navController
            )
        }
    }
}

@Composable
fun TabContent(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    when (selectedTabIndex) {
        /*0 -> HomeUI(navController = navController)*/ // TODO - Keep this for future
        0 -> EventsUI(navController = navController)
        1 -> NewsUI(navController = navController)
        2 -> AppointmentUI()
        3 -> QuestionUI()
    }
}

@Composable
fun navigationBarColors(): NavigationBarItemColors {
    return NavigationBarItemColors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.secondary,
        unselectedTextColor = MaterialTheme.colorScheme.secondary,
        selectedIndicatorColor = MaterialTheme.colorScheme.tertiary,
        disabledIconColor = Color.Gray,
        disabledTextColor = Color.Gray,
    )
}