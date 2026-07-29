package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.ui.theme.VedvoraGold
import com.example.ui.theme.VedvoraPrimary
import com.example.ui.theme.VedvoraPrimaryContainer
import com.example.viewmodel.VedvoraViewModel
import java.io.File

@Composable
fun DigitalResidencyCardDialog(
    viewModel: VedvoraViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val residentName by viewModel.residentName.collectAsState()
    val residentUnit by viewModel.residentUnit.collectAsState()
    val residentStatus by viewModel.residentStatus.collectAsState()
    val profilePicPath by viewModel.residentProfilePicPath.collectAsState()

    var isScanned by remember { mutableStateOf(false) }

    val avatarModel = remember(profilePicPath) {
        if (profilePicPath != null && File(profilePicPath!!).exists()) {
            File(profilePicPath!!)
        } else {
            "https://lh3.googleusercontent.com/aida-public/AB6AXuBtof968EME_AT3J1X04VqgMz2xHOsMs_XTnv7Hq48M1nhmsEnAlfrxdbMhFGS_QqNYoN91npY4DXsydgXk8XSuwnIjqwDH0Yro8mQzvzfgNF4_sGxmEKuf-gEgfG4vVJofi_j3eKwH36638MQYQU0xmwU_iN14Xge2TWjaipqNhw_Um7wHMLQrHfe-TzFEw4OfOMNOkSxwvs9_P3FX6sygdnomhDpSHUy7PK6zyHnZRZUu-qArPFWvSFuj5p-8GNEkYeU59Humrcw"
        }
    }

    val resIdCode = remember { "VEDV-RES-8809-2026" }

    // Pulsing scan line animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("digital_residency_card_dialog"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Digital ID Pass Card
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = VedvoraPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(VedvoraGold, Color(0xFFFFF2C2), VedvoraGold, Color(0xFFB8860B))
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(VedvoraGold.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "VEDVORA RESIDENCES",
                                    color = VedvoraGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                )
                                Text(
                                    text = "DIGITAL ACCESS VERIFICATION",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White.copy(alpha = 0.8f))
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Photo & Badge Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Resident Photo
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .border(2.5.dp, VedvoraGold, RoundedCornerShape(20.dp))
                                .clickable { viewModel.isProfilePhotoPickerOpen.value = true }
                                .testTag("card_photo_click"),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = avatarModel,
                                contentDescription = residentName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Quick Change Overlay Icon
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(4.dp)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(VedvoraGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Update Photo", tint = VedvoraPrimary, modifier = Modifier.size(12.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF10B981).copy(alpha = 0.2f))
                                    .border(1.dp, Color(0xFF10B981), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "ACTIVE RESIDENT",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF10B981)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = residentName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = FontFamily.Serif
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Apartment, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = residentUnit,
                                    fontSize = 12.sp,
                                    color = VedvoraGold,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "ID: $resIdCode",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.6f),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // QR / Barcode Gate Verification Block
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, VedvoraGold.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode2,
                                        contentDescription = null,
                                        tint = VedvoraGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Security Verification QR Code",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(VedvoraGold.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.Nfc, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("NFC Ready", fontSize = 10.sp, color = VedvoraGold, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Realistic Security QR Code display
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White)
                                    .padding(10.dp)
                                    .testTag("security_qr_code_canvas"),
                                contentAlignment = Alignment.Center
                            ) {
                                SecurityQrCodeCanvas(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .alpha(alphaAnim),
                                    codeString = resIdCode,
                                    qrColor = VedvoraPrimary,
                                    accentColor = VedvoraGold
                                )

                                if (isScanned) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color(0xE610B981)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("VERIFIED!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Scan at Lobby Turnstile or Display to Security Guard",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated Guard Verification Button & Copy Details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                isScanned = true
                                viewModel.showToast("⚡ Access Pass Verified! Gate Unlocked for $residentName")
                            },
                            modifier = Modifier
                                .weight(1.3f)
                                .height(44.dp)
                                .testTag("simulate_verify_scan_btn"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VedvoraGold)
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = VedvoraPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Simulate Gate Scan", color = VedvoraPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                val text = "Vedvora Residency ID Card\nResident: $residentName\nUnit: $residentUnit\nID: $resIdCode\nStatus: $residentStatus"
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                val clip = ClipData.newPlainText("Vedvora Digital ID", text)
                                clipboard?.setPrimaryClip(clip)
                                viewModel.showToast("Digital ID details copied to clipboard!")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy ID", color = VedvoraGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SecurityQrCodeCanvas(
    modifier: Modifier = Modifier,
    codeString: String = "VEDV-RES-8809-2026",
    qrColor: Color = VedvoraPrimary,
    accentColor: Color = VedvoraGold
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val gridSize = 21 // Standard QR Version 1 matrix (21x21 modules)
        val moduleW = width / gridSize
        val moduleH = height / gridSize

        // White background
        drawRect(color = Color.White)

        fun drawMod(x: Int, y: Int, color: Color = qrColor) {
            drawRect(
                color = color,
                topLeft = Offset(x * moduleW, y * moduleH),
                size = Size(moduleW, moduleH)
            )
        }

        // Standard 7x7 Finder pattern at (startX, startY)
        fun drawFinder(startX: Int, startY: Int) {
            for (r in 0 until 7) {
                for (c in 0 until 7) {
                    val isOuter = r == 0 || r == 6 || c == 0 || c == 6
                    val isInnerBox = r in 2..4 && c in 2..4
                    if (isOuter || isInnerBox) {
                        drawMod(startX + c, startY + r, qrColor)
                    }
                }
            }
        }

        // Draw 3 Finder Patterns
        drawFinder(0, 0)                  // Top-Left
        drawFinder(gridSize - 7, 0)       // Top-Right
        drawFinder(0, gridSize - 7)       // Bottom-Left

        // Timing patterns
        for (i in 7 until gridSize - 7) {
            if (i % 2 == 0) {
                drawMod(i, 6, qrColor)
                drawMod(6, i, qrColor)
            }
        }

        // Alignment Pattern at (14, 14)
        val alignX = 14
        val alignY = 14
        for (r in -2..2) {
            for (c in -2..2) {
                if (r == -2 || r == 2 || c == -2 || c == 2 || (r == 0 && c == 0)) {
                    drawMod(alignX + c, alignY + r, accentColor)
                }
            }
        }

        // Hash-based matrix data pattern
        val seed = codeString.hashCode()
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val inTopLeftFinder = r < 8 && c < 8
                val inTopRightFinder = r < 8 && c >= gridSize - 8
                val inBottomLeftFinder = r >= gridSize - 8 && c < 8
                val inAlignPattern = (r in (alignY - 2)..(alignY + 2)) && (c in (alignX - 2)..(alignX + 2))
                val inCenterLogo = (r in 9..11) && (c in 9..11)

                if (!inTopLeftFinder && !inTopRightFinder && !inBottomLeftFinder && !inAlignPattern && !inCenterLogo) {
                    val valBit = ((seed xor (r * 37 + c * 19 + r * c)) and 0x1) == 1
                    if (valBit) {
                        val color = if ((r + c) % 5 == 0) accentColor else qrColor
                        drawMod(c, r, color)
                    }
                }
            }
        }

        // Center Emblem/Logo Box
        val cStart = 9 * moduleW
        val cSize = 3 * moduleW
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(cStart - moduleW * 0.3f, cStart - moduleH * 0.3f),
            size = Size(cSize + moduleW * 0.6f, cSize + moduleH * 0.6f),
            cornerRadius = CornerRadius(moduleW, moduleH)
        )
        drawRoundRect(
            color = accentColor,
            topLeft = Offset(cStart, cStart),
            size = Size(cSize, cSize),
            cornerRadius = CornerRadius(moduleW * 0.6f, moduleH * 0.6f)
        )
    }
}
