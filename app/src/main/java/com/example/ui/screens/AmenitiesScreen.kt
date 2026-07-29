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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Deck
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.VedvoraGold
import com.example.ui.theme.VedvoraPrimary
import com.example.ui.theme.VedvoraPrimaryContainer
import com.example.viewmodel.VedvoraViewModel

data class AmenityCardData(
    val title: String,
    val description: String,
    val availability: String,
    val icon: ImageVector,
    val imageUrl: String
)

@Composable
fun AmenitiesScreen(
    viewModel: VedvoraViewModel
) {
    val amenities = listOf(
        AmenityCardData(
            title = "Rooftop Infinity Pool",
            description = "Heated panoramic infinity pool overlooking Emerald Heights skyline.",
            availability = "Open until 11:00 PM",
            icon = Icons.Default.Pool,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAMFXL2z2irUeMZNhTaFj8bS8dkt5r-4uK0PFWV4UVU8HiHaIdsu2R5PKFNa6CbSaEhkKkkIU4Ynvj6Bu-EJzfHYx6LJ5MmP6h-42pE9qZRo3ea_amU4qpIkbU4JsFOGNjOYd_cULKCmcm35yjUBGvRc4XOjpkU7tgbtwz2T-w-JXFYDo9E_iQhH1e7M40SyvZ9SNzZVGu0eNRxYznEclaiPf4RiYQtWrEjCFvql2JLeCZIMiTcrOUnbWyiBtLWiiLd0vptPyJq9SI"
        ),
        AmenityCardData(
            title = "Private Cinema",
            description = "4K Dolby Atmos private theater for intimate resident viewings.",
            availability = "2 Sessions Available Today",
            icon = Icons.Default.Movie,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBw-wEr--mkxR2IwdNRedUdCBWonomcg_pGSml8OORiCplN1e9Lavze2BApaBZkIoLb72kChbRJtfBNzjIBZFaczv9GCwm91JIetVu9iPtoG_rV53qRyNVfZASQbeN7_Uqff3zNH9W9whWg6bhwoAxH8b1h_CisoG4mQ1IwWrT3zjneHyG5FDVw0sGbgYgZIgT6GGJGSv7S8nScGdaRGn0BSD-No7kcnerXjpyv9pImuRuVMZmz50phQD9hMXSXzG21mE8LcUHbQPM"
        ),
        AmenityCardData(
            title = "Sky Gym & Wellness",
            description = "State-of-the-art TechnoGym equipment and personal training sessions.",
            availability = "24/7 Access for Penthouse",
            icon = Icons.Default.FitnessCenter,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAOyGYDw7iR9IYVKueM-AyJ_0bABtaX67eplFIJdg9lG4IeXu68UAUT4oSMBbqVLiSDnHxpiUQPA6_fulCRwqEi_AY1hgjGVgG7ciPz3bURi0SzYR9A_6jgWdSy2zv7G26RZ5m4ffW6_-JzMlZ2W9vAncBvCNfGM4uwXLkyHwRu20Bv26dCK7Iz58ZFluvmvaKyeFtwvi1Pp_PZ1FbduzQKNDhhrEPSCH5vc_UePOafDZyQN4vXw3yl3nx2TlCvrfaFmG-0ePpoWZc"
        ),
        AmenityCardData(
            title = "Private Chef & Dining",
            description = "Custom gourmet dining prepared in your penthouse suite.",
            availability = "Reserve 2h in Advance",
            icon = Icons.Default.Restaurant,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAOyGYDw7iR9IYVKueM-AyJ_0bABtaX67eplFIJdg9lG4IeXu68UAUT4oSMBbqVLiSDnHxpiUQPA6_fulCRwqEi_AY1hgjGVgG7ciPz3bURi0SzYR9A_6jgWdSy2zv7G26RZ5m4ffW6_-JzMlZ2W9vAncBvCNfGM4uwXLkyHwRu20Bv26dCK7Iz58ZFluvmvaKyeFtwvi1Pp_PZ1FbduzQKNDhhrEPSCH5vc_UePOafDZyQN4vXw3yl3nx2TlCvrfaFmG-0ePpoWZc"
        ),
        AmenityCardData(
            title = "Personal Chauffeur",
            description = "Executive fleet airport transfers and city concierge transport.",
            availability = "Bentley / Maybach Fleet",
            icon = Icons.Default.DirectionsCar,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCUBmuPcbyPauA9Ugy5kyewT9VsXuce_SvrkFqrklUCzzysKDdcib3JI3ZIVi72uUReVLrjjAzcIscxVaOC_Zq6H9TUeMZO3WtVA6vmWPTLeP1Eqw6XiqpbHgA8jlNBHL8V997OPHkwtkzGwlRP-B4clIBB_S_KFsIH6BJUPc43QxMDcnxEA5RZCj4hPGKuHub4-tNEbwjQR_9djqE06CCN9kKeDKn2lwUGuuiaFXppDLUxafVS3gkSXpE4ocpSaB7LWfrtNsCZ_FU"
        ),
        AmenityCardData(
            title = "Wellness & Spa",
            description = "Bespoke aromatherapy massages, sauna sessions, and hydrotherapy.",
            availability = "3 Slots Unlocked",
            icon = Icons.Default.Spa,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBXrS6vQ1r9NXYKMaKHJCSfG93LapUCpzIpWQ-mixOEQ20CDuqD_NN4e1w5XWuMCloMsK5QyX_WipOKgYLbuhDYLJVjzCeP0JtmuTeweESFJZlrbqyU2n9aJHm58WxIO0YB8sORxEVNBeM_XTjznvTkwzgeF8BssqCZ1ur1502D5A2fzvipFGb5ytyNRwOpjOxWgiMDcyh65XOxR0HFglUlTTVPdJTC-yF5Jo2gZTlfOJqs6oHOYxz6M-fOnsxbBPtUYK0d9lOfBVk"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("amenities_screen"),
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
            Text(
                text = "Bespoke Amenities",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Reserve rooftop sanctuaries and exclusive lifestyle privileges",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            amenities.forEach { item ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .border(1.dp, VedvoraGold.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        ) {
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
                                        Brush.verticalGradient(listOf(Color.Transparent, VedvoraPrimary.copy(alpha = 0.85f)))
                                    )
                            )
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.9f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(item.icon, contentDescription = null, tint = VedvoraPrimary, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(item.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(item.availability, color = VedvoraGold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                item.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { viewModel.openBookingFor(item.title) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = VedvoraPrimaryContainer)
                            ) {
                                Text("Reserve ${item.title}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = VedvoraGold)
                            }
                        }
                    }
                }
            }
        }
    }
}
