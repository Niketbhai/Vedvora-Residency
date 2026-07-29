package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import com.example.ui.theme.VedvoraPrimaryContainer
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.VedvoraError
import com.example.ui.theme.VedvoraGold
import com.example.ui.theme.VedvoraGoldLight
import com.example.ui.theme.VedvoraPrimary
import com.example.ui.theme.VedvoraSecondary
import com.example.viewmodel.VedvoraViewModel

data class LifestyleItem(
    val title: String,
    val subtitle: String,
    val imageUrl: String
)

@Composable
fun HomeScreen(
    viewModel: VedvoraViewModel,
    onNavigateToVisitors: () -> Unit,
    onNavigateToBilling: () -> Unit,
    onNavigateToAmenities: () -> Unit
) {
    val residentName by viewModel.residentName.collectAsState()
    val residentUnit by viewModel.residentUnit.collectAsState()
    val profilePicPath by viewModel.residentProfilePicPath.collectAsState()
    val bookings by viewModel.bookings.collectAsState()
    val notices by viewModel.notices.collectAsState()
    val activityLogs by viewModel.activityLogs.collectAsState()

    val avatarModel = androidx.compose.runtime.remember(profilePicPath) {
        if (profilePicPath != null && java.io.File(profilePicPath!!).exists()) {
            java.io.File(profilePicPath!!)
        } else {
            "https://lh3.googleusercontent.com/aida-public/AB6AXuBtof968EME_AT3J1X04VqgMz2xHOsMs_XTnv7Hq48M1nhmsEnAlfrxdbMhFGS_QqNYoN91npY4DXsydgXk8XSuwnIjqwDH0Yro8mQzvzfgNF4_sGxmEKuf-gEgfG4vVJofi_j3eKwH36638MQYQU0xmwU_iN14Xge2TWjaipqNhw_Um7wHMLQrHfe-TzFEw4OfOMNOkSxwvs9_P3FX6sygdnomhDpSHUy7PK6zyHnZRZUu-qArPFWvSFuj5p-8GNEkYeU59Humrcw"
        }
    }

    val lifestyleItems = listOf(
        LifestyleItem(
            title = "Private Chef",
            subtitle = "Request for dinner",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAOyGYDw7iR9IYVKueM-AyJ_0bABtaX67eplFIJdg9lG4IeXu68UAUT4oSMBbqVLiSDnHxpiUQPA6_fulCRwqEi_AY1hgjGVgG7ciPz3bURi0SzYR9A_6jgWdSy2zv7G26RZ5m4ffW6_-JzMlZ2W9vAncBvCNfGM4uwXLkyHwRu20Bv26dCK7Iz58ZFluvmvaKyeFtwvi1Pp_PZ1FbduzQKNDhhrEPSCH5vc_UePOafDZyQN4vXw3yl3nx2TlCvrfaFmG-0ePpoWZc"
        ),
        LifestyleItem(
            title = "Personal Chauffeur",
            subtitle = "Book airport transfer",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCUBmuPcbyPauA9Ugy5kyewT9VsXuce_SvrkFqrklUCzzysKDdcib3JI3ZIVi72uUReVLrjjAzcIscxVaOC_Zq6H9TUeMZO3WtVA6vmWPTLeP1Eqw6XiqpbHgA8jlNBHL8V997OPHkwtkzGwlRP-B4clIBB_S_KFsIH6BJUPc43QxMDcnxEA5RZCj4hPGKuHub4-tNEbwjQR_9djqE06CCN9kKeDKn2lwUGuuiaFXppDLUxafVS3gkSXpE4ocpSaB7LWfrtNsCZ_FU"
        ),
        LifestyleItem(
            title = "Wellness & Spa",
            subtitle = "Therapy booking",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBXrS6vQ1r9NXYKMaKHJCSfG93LapUCpzIpWQ-mixOEQ20CDuqD_NN4e1w5XWuMCloMsK5QyX_WipOKgYLbuhDYLJVjzCeP0JtmuTeweESFJZlrbqyU2n9aJHm58WxIO0YB8sORxEVNBeM_XTjznvTkwzgeF8BssqCZ1ur1502D5A2fzvipFGb5ytyNRwOpjOxWgiMDcyh65XOxR0HFglUlTTVPdJTC-yF5Jo2gZTlfOJqs6oHOYxz6M-fOnsxbBPtUYK0d9lOfBVk"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen"),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 1000.dp)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            // Welcome Header
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Welcome Home,",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = residentName.split(" ").firstOrNull() ?: "Arjun",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = VedvoraGold,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    // Resident Avatar
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, VedvoraGold, CircleShape)
                            .clickable { viewModel.isProfilePhotoPickerOpen.value = true }
                            .testTag("home_avatar_click"),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = avatarModel,
                            contentDescription = residentName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Apartment,
                        contentDescription = null,
                        tint = VedvoraGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = residentUnit,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.isDigitalIdCardOpen.value = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("home_digital_id_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VedvoraPrimaryContainer)
                    ) {
                        Icon(Icons.Default.Badge, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Digital ID", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = VedvoraGold)
                    }

                    Button(
                        onClick = { viewModel.isSubmitLifestyleRequestOpen.value = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("home_vip_concierge_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VedvoraPrimaryContainer)
                    ) {
                        Icon(Icons.Default.RoomService, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Concierge", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = VedvoraGold)
                    }

                    OutlinedButton(
                        onClick = { viewModel.isGatePassDialogOpen.value = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("home_gate_pass_btn"),
                        shape = RoundedCornerShape(14.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(VedvoraGold, VedvoraGold)))
                    ) {
                        Icon(Icons.Default.QrCode2, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Gate Pass", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = VedvoraGold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Priority Access Card & Curated Lifestyle Section
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, VedvoraGold.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PRIORITY ACCESS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = VedvoraGold,
                            letterSpacing = 1.5.sp
                        )
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = VedvoraGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Private Cinema",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = bookings.firstOrNull()?.subtitle ?: "Your booking for 'Inception' starts at 8:00 PM tonight.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Attendants", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("2 Ready", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VedvoraGold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { viewModel.openBookingFor("Private Cinema") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(VedvoraGold, VedvoraGold)))
                    ) {
                        Text("Modify Booking", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VedvoraGold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Curated Lifestyle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Diamond, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Curated Lifestyle", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
                Text(
                    text = "Explore Collection",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VedvoraGold,
                    modifier = Modifier.clickable { onNavigateToAmenities() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(lifestyleItems) { item ->
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .width(180.dp)
                            .height(160.dp)
                            .clickable { viewModel.openBookingFor(item.title) }
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = item.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, VedvoraPrimary.copy(alpha = 0.9f))
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(12.dp)
                            ) {
                                Text(item.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(item.subtitle, color = VedvoraGoldLight, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Card 1
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToVisitors() }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Today's Visitors", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(VedvoraGold.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(16.dp))
                            }
                        }
                        Text("3", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VedvoraSecondary, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("2 Pre-approved", fontSize = 10.sp, color = VedvoraSecondary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                // Card 2
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToBilling() }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Pending Dues", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(VedvoraError.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = VedvoraError, modifier = Modifier.size(16.dp))
                            }
                        }
                        Text("\$450", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = VedvoraError, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Due in 3 days", fontSize = 10.sp, color = VedvoraError, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Card 3 Active Requests
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Active Maintenance Request", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("1 Active", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text("Maintenance: Leaking faucet in Master Suite", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(VedvoraGold.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ReportProblem, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Modules Section
            Text("Management Modules", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(12.dp))

            // Visitor Management
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToVisitors() }
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SupervisedUserCircle, contentDescription = null, tint = VedvoraGold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Visitor Management", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Real-time log of guests & deliveries", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(VedvoraSecondary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.LocalShipping, contentDescription = null, tint = VedvoraSecondary, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Amazon Delivery", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(VedvoraSecondary.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("ARRIVED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = VedvoraSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Amenities Module
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToAmenities() }
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(VedvoraGold.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Pool, contentDescription = null, tint = VedvoraGold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Amenities", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Book Rooftop Pool, Sky Garden or Fitness Center", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(VedvoraSecondary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sky Gym Available", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VedvoraGold)
                        }
                        Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Official Notices Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(VedvoraGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = VedvoraGold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Official Notices", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    notices.forEach { notice ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                                .background(if (notice.isUrgent) VedvoraGold.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant)
                                .border(
                                    width = 3.dp,
                                    color = if (notice.isUrgent) VedvoraGold else MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(notice.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                                    if (notice.isUrgent) {
                                        Text(
                                            text = "RSVP NOW",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VedvoraGold,
                                            modifier = Modifier.clickable { viewModel.rsvpToGala() }
                                        )
                                    }
                                }
                                Text(notice.subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Elite Events
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Event, contentDescription = null, tint = VedvoraGold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Elite Events", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(14.dp))
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBw-wEr--mkxR2IwdNRedUdCBWonomcg_pGSml8OORiCplN1e9Lavze2BApaBZkIoLb72kChbRJtfBNzjIBZFaczv9GCwm91JIetVu9iPtoG_rV53qRyNVfZASQbeN7_Uqff3zNH9W9whWg6bhwoAxH8b1h_CisoG4mQ1IwWrT3zjneHyG5FDVw0sGbgYgZIgT6GGJGSv7S8nScGdaRGn0BSD-No7kcnerXjpyv9pImuRuVMZmz50phQD9hMXSXzG21mE8LcUHbQPM",
                            contentDescription = "Event",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)))
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Text("OCTOBER 25", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = VedvoraGold, letterSpacing = 1.sp)
                            Text("Grand Wine Tasting Evening", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(VedvoraGold)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("VIP ONLY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = VedvoraPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.showToast("Attendance Confirmed for Grand Wine Tasting!") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VedvoraGold)
                    ) {
                        Text("Confirm Attendance", color = VedvoraPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Activity Feed / Lifestyle Log
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.History, contentDescription = null, tint = VedvoraGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Lifestyle Log", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    activityLogs.forEach { log ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(VedvoraGold.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                val icon = when (log.iconType) {
                                    "payment" -> Icons.Default.Payments
                                    "guest" -> Icons.Default.Login
                                    else -> Icons.Default.CheckCircle
                                }
                                Icon(icon, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(log.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                                Text(log.referenceCode, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(log.timeAgoStr.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = VedvoraGold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { viewModel.showToast("Viewing Full Archive") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Full Archive", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}
