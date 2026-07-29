package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.entity.BookingEntity
import com.example.ui.components.RescheduleRequestDialog
import com.example.ui.components.SubmitLifestyleRequestDialog
import com.example.ui.theme.VedvoraError
import com.example.ui.theme.VedvoraGold
import com.example.ui.theme.VedvoraPrimary
import com.example.ui.theme.VedvoraPrimaryContainer
import com.example.ui.theme.VedvoraSecondary
import com.example.viewmodel.VedvoraViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConciergeDashboardScreen(
    viewModel: VedvoraViewModel
) {
    val bookings by viewModel.bookings.collectAsState()
    val isSubmitDialogOpen by viewModel.isSubmitLifestyleRequestOpen.collectAsState()
    val selectedRescheduleBooking by viewModel.selectedRescheduleBooking.collectAsState()

    var activeFilter by remember { mutableStateOf("All Requests") }

    val filterOptions = listOf(
        "All Requests",
        "Maintenance",
        "Cleaning",
        "Dining",
        "Spa & Wellness",
        "Amenity"
    )

    val filteredBookings = remember(bookings, activeFilter) {
        if (activeFilter == "All Requests") {
            bookings
        } else {
            bookings.filter {
                it.category.contains(activeFilter, ignoreCase = true) ||
                        it.serviceName.contains(activeFilter, ignoreCase = true)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("concierge_dashboard_screen"),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 1000.dp)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(VedvoraGold.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RoomService,
                                contentDescription = null,
                                tint = VedvoraGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Concierge Dashboard",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Active resident requests & upcoming lifestyle bookings",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Button(
                    onClick = { viewModel.isSubmitLifestyleRequestOpen.value = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VedvoraPrimaryContainer),
                    modifier = Modifier.testTag("submit_new_request_top_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = VedvoraGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "NEW REQUEST",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VedvoraGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Chips Row
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                filterOptions.forEach { filterName ->
                    val isSelected = activeFilter == filterName
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) VedvoraGold else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { activeFilter = filterName }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = filterName,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) VedvoraPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Requests List
            if (filteredBookings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.RoomService,
                                contentDescription = null,
                                tint = VedvoraGold,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No Active Requests Found",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "There are currently no active concierge requests in this category.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.isSubmitLifestyleRequestOpen.value = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = VedvoraPrimaryContainer)
                            ) {
                                Text("Submit Lifestyle Request", color = VedvoraGold, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
                ) {
                    items(filteredBookings, key = { it.id }) { booking ->
                        ConciergeRequestCard(
                            booking = booking,
                            onRescheduleClick = {
                                viewModel.selectedRescheduleBooking.value = booking
                            },
                            onCancelClick = {
                                viewModel.cancelBooking(booking.id, booking.serviceName)
                            },
                            onCallConcierge = {
                                viewModel.showToast("Connecting to Concierge Desk Line #001...")
                            }
                        )
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { viewModel.isSubmitLifestyleRequestOpen.value = true },
            containerColor = VedvoraPrimaryContainer,
            contentColor = VedvoraGold,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("submit_request_fab")
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Submit Request")
        }

        // Submit Request Dialog
        if (isSubmitDialogOpen) {
            SubmitLifestyleRequestDialog(
                onDismiss = { viewModel.isSubmitLifestyleRequestOpen.value = false },
                onSubmit = { title, category, notes, date, time ->
                    viewModel.submitLifestyleRequest(title, category, notes, date, time)
                }
            )
        }

        // Reschedule Dialog
        selectedRescheduleBooking?.let { b ->
            RescheduleRequestDialog(
                booking = b,
                onDismiss = { viewModel.selectedRescheduleBooking.value = null },
                onConfirmReschedule = { newDate, newTime ->
                    viewModel.rescheduleBooking(b.id, b.serviceName, newDate, newTime)
                }
            )
        }
    }
}

@Composable
fun ConciergeRequestCard(
    booking: BookingEntity,
    onRescheduleClick: () -> Unit,
    onCancelClick: () -> Unit,
    onCallConcierge: () -> Unit
) {
    val categoryIcon = when (booking.category.lowercase()) {
        "maintenance" -> Icons.Default.Build
        "cleaning" -> Icons.Default.CleaningServices
        "dining" -> Icons.Default.Restaurant
        "spa", "spa & wellness" -> Icons.Default.Spa
        "transportation", "valet ride" -> Icons.Default.DirectionsCar
        else -> Icons.Default.RoomService
    }

    val statusColor = when (booking.status.lowercase()) {
        "confirmed" -> VedvoraSecondary
        "in progress" -> Color(0xFF2196F3)
        else -> VedvoraGold
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, VedvoraGold.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
            .testTag("request_card_${booking.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Category Icon + Title + Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(VedvoraGold.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = categoryIcon,
                            contentDescription = null,
                            tint = VedvoraGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = booking.serviceName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = booking.category.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = VedvoraGold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = booking.status.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Time & Schedule Badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = VedvoraGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = booking.startTimeStr,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = booking.attendants,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (booking.subtitle.isNotBlank() && booking.subtitle != booking.startTimeStr) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Notes,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = booking.subtitle,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Reschedule Button
                OutlinedButton(
                    onClick = onRescheduleClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("reschedule_req_btn_${booking.id}"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = VedvoraGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reschedule", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VedvoraGold)
                }

                // Cancel Button
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("cancel_req_btn_${booking.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = VedvoraError)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        tint = VedvoraError,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cancel", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VedvoraError)
                }

                // Call Desk
                OutlinedButton(
                    onClick = onCallConcierge,
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("call_desk_btn_${booking.id}"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call Desk",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
