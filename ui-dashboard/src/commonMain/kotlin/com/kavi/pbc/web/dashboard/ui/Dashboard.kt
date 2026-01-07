package com.kavi.pbc.web.dashboard.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kavi.pbc.web.dashboard.data.model.TabItem
import com.kavi.pbc.web.dashboard.ui.event.EventsUI
import com.kavi.pbc.web.dashboard.ui.home.HomeUI
import com.kavi.pbc.web.dashboard.ui.meditation.MeditationUI
import com.kavi.pbc.web.dashboard.ui.temple.TempleUI
import org.jetbrains.compose.resources.painterResource
import pbcwebapp.ui_dashboard.generated.resources.Res
import pbcwebapp.ui_dashboard.generated.resources.icon_event
import pbcwebapp.ui_dashboard.generated.resources.icon_lotus
import pbcwebapp.ui_dashboard.generated.resources.icon_meditation
import pbcwebapp.ui_dashboard.generated.resources.icon_temple
import pbcwebapp.ui_dashboard.generated.resources.image_pbc

@Composable
fun DashboardUI(navController: NavController) {
    val tabItemList = listOf(
        TabItem(name = "Home", icon = Res.drawable.icon_lotus),
        TabItem(name = "Events", icon = Res.drawable.icon_event),
        TabItem(name = "Meditation", icon = Res.drawable.icon_meditation),
        TabItem(name = "Temple", icon = Res.drawable.icon_temple)
    )
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        //contentAlignment = Alignment.Center
    ) {
        Column (
            modifier = Modifier
                .padding(start = 150.dp, end = 150.dp)
        ) {
            Row (
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .weight(.15f)
                        .size(100.dp),
                    painter = painterResource(Res.drawable.image_pbc),
                    contentDescription = "PBC image with name"
                )

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .weight(.85f)
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
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
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun TabContent(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier.Companion
) {
    when (selectedTabIndex) {
        0 -> HomeUI()
        1 -> EventsUI()
        2 -> MeditationUI()
        3 -> TempleUI()
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