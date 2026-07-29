package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Pool
import androidx.compose.material.icons.outlined.RoomService
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VedvoraGold
import com.example.ui.theme.VedvoraPrimary

sealed class VedvoraTab(
    val route: String,
    val title: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
) {
    object Home : VedvoraTab("home", "HOME", Icons.Default.Home, Icons.Outlined.Home)
    object Concierge : VedvoraTab("concierge", "REQUESTS", Icons.Default.RoomService, Icons.Outlined.RoomService)
    object VIP : VedvoraTab("vip", "VIP", Icons.Default.Diamond, Icons.Outlined.Diamond)
    object Guests : VedvoraTab("guests", "GUESTS", Icons.Default.People, Icons.Outlined.People)
    object Booking : VedvoraTab("booking", "AMENITIES", Icons.Default.Pool, Icons.Outlined.Pool)
    object Billing : VedvoraTab("billing", "BILLING", Icons.Default.Payments, Icons.Outlined.Payments)
    object Account : VedvoraTab("account", "ACCOUNT", Icons.Default.Person, Icons.Outlined.Person)
}

@Composable
fun VedvoraBottomBar(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        VedvoraTab.Home,
        VedvoraTab.Concierge,
        VedvoraTab.VIP,
        VedvoraTab.Booking,
        VedvoraTab.Billing,
        VedvoraTab.Account
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            .border(
                width = 1.dp,
                color = VedvoraGold.copy(alpha = 0.2f),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 840.dp)
                .height(68.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = currentRoute == tab.route

                val iconColor = if (isSelected) VedvoraGold else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                val textColor = if (isSelected) VedvoraGold else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(tab.route) }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("nav_tab_${tab.route}"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isSelected) tab.activeIcon else tab.inactiveIcon,
                        contentDescription = tab.title,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = tab.title,
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = textColor,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
