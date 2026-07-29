package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.VedvoraError
import com.example.ui.theme.VedvoraGold
import com.example.ui.theme.VedvoraPrimary
import com.example.viewmodel.VedvoraViewModel

@Composable
fun PostNoticeDialog(
    viewModel: VedvoraViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Important Update") } // Important Update, Community Event, Emergency Announcement
    var isUrgent by remember { mutableStateOf(false) }

    val categories = listOf("Important Update", "Community Event", "Emergency Announcement")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, VedvoraGold.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .testTag("post_notice_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Digital Notice Board",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Broadcast management update or event",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = VedvoraGold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Notice Title") },
                    placeholder = { Text("e.g. Annual Rooftop Gala & Fireworks") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("notice_title_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VedvoraGold,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Subtitle/Description Input
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Notice Description / Details") },
                    placeholder = { Text("Enter details, timing, location, or instructions...") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("notice_subtitle_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VedvoraGold,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category Selection
                Text("Category", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VedvoraGold)
                Spacer(modifier = Modifier.height(6.dp))

                categories.forEach { cat ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                category = cat
                                if (cat == "Emergency Announcement") isUrgent = true
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (category == cat),
                            onClick = {
                                category = cat
                                if (cat == "Emergency Announcement") isUrgent = true
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = VedvoraGold)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = when (cat) {
                                "Emergency Announcement" -> Icons.Default.Warning
                                "Community Event" -> Icons.Default.Event
                                else -> Icons.Default.Campaign
                            },
                            contentDescription = null,
                            tint = if (cat == "Emergency Announcement") VedvoraError else VedvoraGold,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = cat,
                            fontSize = 13.sp,
                            fontWeight = if (category == cat) FontWeight.Bold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Urgent Flag
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isUrgent = !isUrgent },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isUrgent,
                        onCheckedChange = { isUrgent = it },
                        colors = CheckboxDefaults.colors(checkedColor = VedvoraError)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mark as High-Priority / Urgent Alert",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isUrgent) VedvoraError else MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Post Button
                Button(
                    onClick = {
                        if (title.isBlank()) {
                            viewModel.showToast("Please enter a notice title.")
                        } else {
                            viewModel.postNotice(
                                title = title,
                                subtitle = if (subtitle.isBlank()) "Official notice broadcasted by Estate Management" else subtitle,
                                category = category,
                                isUrgent = isUrgent
                            )
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_post_notice_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isUrgent) VedvoraError else VedvoraGold)
                ) {
                    Text(
                        text = if (isUrgent) "BROADCAST EMERGENCY NOTICE" else "POST TO NOTICE BOARD",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isUrgent) Color.White else VedvoraPrimary
                    )
                }
            }
        }
    }
}
