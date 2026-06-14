package com.kavi.pbc.web.common.ui.component.pbc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.email.EmailGroupHeading
import org.jetbrains.compose.resources.painterResource
import pbcwebapp.lib_common_ui.generated.resources.Res
import pbcwebapp.lib_common_ui.generated.resources.icon_checked

@Composable
fun EmailGroupSelection(
    emailHeadings: List<EmailGroupHeading>,
    selectedHeadings: MutableState<MutableList<EmailGroupHeading>>
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .border(1.dp, MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(8.dp))
            .clip( RoundedCornerShape(8.dp)),
    ) {
        Column (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            FlowRow(
                maxItemsInEachRow = 10,
                modifier = Modifier.fillMaxWidth()
            ) {
                emailHeadings.forEach { heading ->
                    var isSelected by remember { mutableStateOf(false) }
                    isSelected = selectedHeadings.value.contains(heading)

                    FilterChip(
                        selected = isSelected,
                        modifier = Modifier.padding(4.dp),
                        label = {
                            Text(
                                text = heading.name,
                                fontFamily = PBCFontFamily,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        onClick = {
                            if (isSelected) {
                                selectedHeadings.value.remove(heading)
                                isSelected = false
                            } else {
                                selectedHeadings.value.add(heading)
                                isSelected = true
                            }
                        },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    painter = painterResource(Res.drawable.icon_checked),
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = "Checked icon",
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        } else {
                            null
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiary
                        )
                    )
                }
            }
        }
    }
}