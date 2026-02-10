package com.kavi.pbc.web.appointment.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.appointment.Appointment
import com.kavi.pbc.web.network.session.Session
import org.jetbrains.compose.resources.painterResource
import pbcwebapp.ui_appointment.generated.resources.Res
import pbcwebapp.ui_appointment.generated.resources.appointment_icon_delete
import pbcwebapp.ui_appointment.generated.resources.appointment_icon_edit

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun AppointmentItem(modifier: Modifier = Modifier, appointment: Appointment, onView: () -> Unit,
                    onDelete:() -> Unit, onModify:() -> Unit) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    BoxWithConstraints (
        modifier = modifier.padding(top = 2.dp)
    ) {
        val maxWith = this.maxWidth
        val textLength = (maxWith.value * .65f).dp

        Row (
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
                .shadow(
                    elevation = 8.dp,
                    spotColor = themeAdditionalColors.shadow,
                    shape = RoundedCornerShape(8.dp),
                )
                .background(MaterialTheme.colorScheme.background)
                .clickable {
                    onView.invoke()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                var titleText = ""
                Session.user?.let {
                    if (it.residentMonk) {
                        titleText = "Appointment with ${appointment.user.firstName}"
                    } else {
                        appointment.selectedMonk?.let { monk ->
                            titleText = "Appointment with Bhanthe ${monk.lastName}"
                        }?: run {
                            titleText = "Appointment with PBC"
                        }
                    }
                }

                Row {
                    Text(
                        modifier = Modifier.width(textLength),
                        text = titleText,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = PBCFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    AppointmentStatusTag(appointmentStatus = appointment.appointmentStatus)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "on ${appointment.getFormatDate()} at ${appointment.time}",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = PBCFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.appointment_icon_edit),
                            contentDescription = "Edit Event",
                            tint = themeAdditionalColors.shadow,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(4.dp)
                                .clickable {
                                    onModify.invoke()
                                }
                        )
                        /*if (isDraftEvent) {
                            Icon(
                                painter = painterResource(R.drawable.icon_publish),
                                contentDescription = "Publish Event",
                                tint = MaterialTheme.colorScheme.shadow,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(4.dp)
                                    .clickable {
                                        onPublish?.invoke()
                                    }
                            )
                        }*/
                        Icon(
                            painter = painterResource(Res.drawable.appointment_icon_delete),
                            contentDescription = "Delete Event",
                            tint = themeAdditionalColors.shadow,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(4.dp)
                                .clickable {
                                    onDelete.invoke()
                                }
                        )
                    }
                }
            }
        }
    }
}
