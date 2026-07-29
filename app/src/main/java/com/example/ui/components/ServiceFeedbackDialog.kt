package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.entity.BookingEntity
import com.example.ui.theme.VedvoraGold
import com.example.ui.theme.VedvoraPrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ServiceFeedbackDialog(
    booking: BookingEntity,
    onDismiss: () -> Unit,
    onSubmitFeedback: (rating: Int, comment: String, tags: String) -> Unit
) {
    var rating by remember { mutableIntStateOf(if (booking.rating > 0) booking.rating else 5) }
    var comment by remember { mutableStateOf(booking.feedbackText) }
    val selectedTags = remember { mutableStateListOf<String>() }

    val quickTags = listOf(
        "Punctual & Fast",
        "Polite & Professional",
        "Pristine Quality",
        "Punctual Staff",
        "Exceeded Expectations",
        "Eco-Friendly Supplies"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .padding(16.dp)
                .border(1.dp, VedvoraGold.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .testTag("service_feedback_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RATE CONCIERGE SERVICE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = VedvoraGold,
                            letterSpacing = 1.2.sp
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Service info banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = booking.serviceName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Provided by ${booking.attendants} • ${booking.startTimeStr}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "How was your service experience?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Interactive 5-Star Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    (1..5).forEach { starIndex ->
                        val isFilled = starIndex <= rating
                        Icon(
                            imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Star $starIndex",
                            tint = if (isFilled) VedvoraGold else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { rating = starIndex }
                                .testTag("star_icon_$starIndex")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = when (rating) {
                        5 -> "★ Outstanding • 5/5 Stars"
                        4 -> "★ Great Service • 4/5 Stars"
                        3 -> "★ Satisfactory • 3/5 Stars"
                        2 -> "★ Needs Improvement • 2/5 Stars"
                        else -> "★ Poor Experience • 1/5 Stars"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VedvoraGold
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Quick feedback tags
                Text(
                    text = "QUICK HIGHLIGHTS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    quickTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) VedvoraGold else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) VedvoraPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Text field for custom remarks
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Resident Feedback & Comments", fontSize = 12.sp) },
                    placeholder = { Text("E.g., Very thorough cleaning, staff was exceptionally courteous...", fontSize = 11.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("feedback_comment_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VedvoraGold,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Submit Button
                Button(
                    onClick = {
                        val tagsString = selectedTags.joinToString(", ")
                        onSubmitFeedback(rating, comment.trim(), tagsString)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_service_rating_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VedvoraGold)
                ) {
                    Text(
                        text = "SUBMIT SERVICE RATING",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = VedvoraPrimary,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
