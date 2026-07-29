package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import com.example.ui.theme.VedvoraPrimaryContainer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Deck
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.util.PdfGenerator
import com.example.util.NotificationHelper
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.VedvoraError
import com.example.ui.theme.VedvoraGold
import com.example.ui.theme.VedvoraPrimary

@Composable
fun AuthenticQrCodeView(
    passCode: String,
    isOriginal: Boolean,
    isApproved: Boolean,
    modifier: Modifier = Modifier
) {
    val darkColor = if (isApproved) Color(0xFF10B981) else if (isOriginal) VedvoraGold else Color(0xFFE2E8F0)
    val lightColor = Color(0xFF0F172A)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(lightColor)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSize = 21
            val cellSize = size.width / gridSize

            // Function to check if a cell belongs to 7x7 finder patterns
            fun isFinderPattern(r: Int, c: Int): Boolean {
                if (r in 0..6 && c in 0..6) return true
                if (r in 0..6 && c in (gridSize - 7)..<gridSize) return true
                if (r in (gridSize - 7)..<gridSize && c in 0..6) return true
                return false
            }

            // Draw Finder Pattern
            fun drawFinder(startR: Int, startC: Int) {
                for (r in 0..6) {
                    for (c in 0..6) {
                        val isOuter = r == 0 || r == 6 || c == 0 || c == 6
                        val isInner = r in 2..4 && c in 2..4
                        if (isOuter || isInner) {
                            drawRect(
                                color = darkColor,
                                topLeft = Offset((startC + c) * cellSize, (startR + r) * cellSize),
                                size = Size(cellSize, cellSize)
                            )
                        }
                    }
                }
            }

            drawFinder(0, 0)
            drawFinder(0, gridSize - 7)
            drawFinder(gridSize - 7, 0)

            // Draw timing patterns
            for (i in 7 until gridSize - 7) {
                if (i % 2 == 0) {
                    drawRect(
                        color = darkColor,
                        topLeft = Offset(i * cellSize, 6 * cellSize),
                        size = Size(cellSize, cellSize)
                    )
                    drawRect(
                        color = darkColor,
                        topLeft = Offset(6 * cellSize, i * cellSize),
                        size = Size(cellSize, cellSize)
                    )
                }
            }

            // Draw data modules
            val seed = passCode.hashCode()
            for (r in 0 until gridSize) {
                for (c in 0 until gridSize) {
                    if (isFinderPattern(r, c)) continue
                    if (r == 6 || c == 6) continue
                    if (r in 8..12 && c in 8..12) continue

                    val bit = ((r * 31 + c * 17 + seed) % 3 == 0) || ((r + c) % 2 == 0 && (r * c) % 3 != 0)
                    if (bit) {
                        drawRect(
                            color = darkColor,
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = Size(cellSize, cellSize)
                        )
                    }
                }
            }
        }

        // Center Emblem Badge overlay
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isApproved) Color(0xFF10B981) else if (isOriginal) VedvoraGold else Color(0xFF334155))
                .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isApproved) "VERIFIED" else if (isOriginal) "ORIGINAL" else "DUPLICATE",
                fontSize = 7.sp,
                fontWeight = FontWeight.Black,
                color = if (isApproved) Color.White else if (isOriginal) VedvoraPrimary else Color.White
            )
        }
    }
}

@Composable
fun GatePassDialog(
    residentUnit: String = "Tower A, Flat 1204",
    onDismiss: () -> Unit,
    onShare: () -> Unit,
    onApproveGateScan: (passType: String, passCode: String, isOriginal: Boolean) -> Unit = { _, _, _ -> }
) {
    val context = LocalContext.current
    var isScanning by remember { mutableStateOf(false) }
    var isApprovedByGuard by remember { mutableStateOf(false) }

    val passCode = "VEDV-ORIG-8820-2026"
    val passTitle = "ORIGINAL MASTER RESIDENT PASS"

    val handleSharePass = {
        val shareText = "Vedvora Luxury Residency Pass\nCode: $passCode\nResident Unit: $residentUnit\nStatus: ${if (isApprovedByGuard) "VERIFIED BY GATE GUARD" else "ACTIVE RESIDENT PASS"}\nPresent at Main Gate Scanner."
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("Vedvora Gate Pass", shareText)
        clipboard?.setPrimaryClip(clip)

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Gate Pass")
        context.startActivity(shareIntent)
        onShare()
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .padding(16.dp)
                .border(1.dp, VedvoraGold.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .testTag("gate_pass_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = passTitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VedvoraGold,
                        letterSpacing = 1.2.sp
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scanner View / QR Box
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            2.dp,
                            if (isApprovedByGuard) Color(0xFF10B981) else VedvoraGold,
                            RoundedCornerShape(20.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AuthenticQrCodeView(
                            passCode = passCode,
                            isOriginal = true,
                            isApproved = isApprovedByGuard,
                            modifier = Modifier.size(130.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = passCode,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = if (isApprovedByGuard) "VERIFIED BY GATE GUARD" else "READY FOR SCANNER",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isApprovedByGuard) Color(0xFF10B981) else VedvoraGold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Arjun Sharma",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = residentUnit,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isApprovedByGuard) Color(0xFF10B981).copy(alpha = 0.15f)
                            else VedvoraGold.copy(alpha = 0.15f)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isApprovedByGuard) "Security Officer Vikram Approved • Apartment Unlocked"
                               else "Master Resident Pass • Unlimited Access",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isApprovedByGuard) Color(0xFF10B981) else VedvoraGold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Gate Scanner Action Buttons
                if (!isApprovedByGuard) {
                    Button(
                        onClick = {
                            isScanning = true
                            isApprovedByGuard = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("scan_gate_pass_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VedvoraGold)
                    ) {
                        Icon(Icons.Default.QrCode2, contentDescription = null, tint = VedvoraPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SCAN AT GATE & VERIFY PASS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = VedvoraPrimary
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            onApproveGateScan(
                                "Original Resident",
                                passCode,
                                true
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("approve_gate_entry_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "APPROVE & ENTER APARTMENT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Close", color = MaterialTheme.colorScheme.onSurface)
                    }
                    OutlinedButton(
                        onClick = { handleSharePass() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = VedvoraGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share", color = VedvoraGold, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) VedvoraPrimary else MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = category,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PreAuthorizeVisitorDialog(
    onDismiss: () -> Unit,
    onSubmit: (name: String, category: String, subtitle: String, valet: Boolean, lounge: Boolean, time: String) -> Unit
) {
    var guestName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Executive") }
    var subtitleInput by remember { mutableStateOf("") }
    var timeInput by remember { mutableStateOf("08:00 PM") }
    var valetRequested by remember { mutableStateOf(true) }
    var loungeAccess by remember { mutableStateOf(false) }

    val categories = listOf("Executive", "VIP Guest", "Maintenance")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .padding(16.dp)
                .border(1.dp, VedvoraGold.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .testTag("pre_authorize_visitor_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PRE-AUTHORIZE VISITOR",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = guestName,
                    onValueChange = { guestName = it },
                    label = { Text("Visitor / Guest Name") },
                    placeholder = { Text("e.g. Marcus Vance") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VedvoraGold,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Visitor Category", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (category in categories) {
                        CategoryChip(
                            category = category,
                            isSelected = selectedCategory == category,
                            onClick = { selectedCategory = category }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = subtitleInput,
                    onValueChange = { subtitleInput = it },
                    label = { Text("Vehicle or Access Detail") },
                    placeholder = { Text("e.g. Porsche • ABC-900") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = timeInput,
                    onValueChange = { timeInput = it },
                    label = { Text("Scheduled Arrival Time") },
                    placeholder = { Text("08:00 PM") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Valet Requested", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Switch(
                        checked = valetRequested,
                        onCheckedChange = { valetRequested = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.surface,
                            checkedTrackColor = VedvoraGold
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Deck, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Lounge Access Granted", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Switch(
                        checked = loungeAccess,
                        onCheckedChange = { loungeAccess = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.surface,
                            checkedTrackColor = VedvoraGold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (guestName.isNotBlank()) {
                            onSubmit(
                                guestName,
                                selectedCategory,
                                if (subtitleInput.isBlank()) "$selectedCategory Guest" else subtitleInput,
                                valetRequested,
                                loungeAccess,
                                timeInput
                            )
                        }
                    },
                    enabled = guestName.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VedvoraPrimaryContainer)
                ) {
                    Text("Issue Digital Pass", fontWeight = FontWeight.Bold, color = VedvoraGold)
                }
            }
        }
    }
}

@Composable
fun PaymentDialog(
    amount: Double = 45000.00,
    onDismiss: () -> Unit,
    onConfirmPayment: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var paymentState by remember { mutableStateOf("SELECT") } // "SELECT", "PROCESSING", "SUCCESS"
    var selectedMethod by remember { mutableStateOf("UPI_GPAY") } // "UPI_GPAY", "UPI_PHONEPE", "CARD"
    var upiIdInput by remember { mutableStateOf("arjun@okaxis") }

    val formattedAmount = String.format("%,.2f", amount)
    val transactionRefId = remember { "UPI/2026/0729/${(100000..999999).random()}" }

    val handleDownloadPdf = {
        val methodLabel = when (selectedMethod) {
            "UPI_GPAY" -> "Google Pay UPI"
            "UPI_PHONEPE" -> "PhonePe UPI"
            else -> "Concierge Platinum Card"
        }
        val file = PdfGenerator.generateAndSavePaymentReceiptPdf(
            context = context,
            amountPaid = amount,
            transactionId = transactionRefId,
            paymentMode = methodLabel
        )
        if (file != null && file.exists()) {
            Toast.makeText(context, "📄 Payment Receipt PDF saved to Downloads!\nPath: ${file.name}", Toast.LENGTH_LONG).show()
            NotificationHelper.showServiceStatusNotification(
                context = context,
                serviceName = "Payment Receipt PDF",
                newStatus = "completed",
                additionalDetails = "Saved ${file.name} to Downloads folder"
            )

            try {
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "Share Payment Receipt PDF"))
            } catch (e: Exception) {
                // Fallback share chooser
            }
        } else {
            Toast.makeText(context, "Payment Receipt generated for ₹$formattedAmount", Toast.LENGTH_SHORT).show()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .padding(16.dp)
                .border(1.dp, VedvoraGold.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .testTag("payment_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (paymentState == "SELECT") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "RESIDENTIAL DUES PAYMENT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = VedvoraGold,
                            letterSpacing = 1.2.sp
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(20.dp)
                    ) {
                        Column {
                            Text("Total Amount Due", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                            Text(
                                text = "₹$formattedAmount",
                                color = Color.White,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Luxury Amenities & HOA", color = VedvoraGold, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                Text("Due in 3 days", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Select Payment Method",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // UPI Option 1: Google Pay
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedMethod == "UPI_GPAY") VedvoraGold.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMethod = "UPI_GPAY" }
                            .border(
                                width = if (selectedMethod == "UPI_GPAY") 1.5.dp else 0.dp,
                                color = VedvoraGold,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = (selectedMethod == "UPI_GPAY"),
                                    onClick = { selectedMethod = "UPI_GPAY" },
                                    colors = RadioButtonDefaults.colors(selectedColor = VedvoraGold)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Google Pay UPI", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Instant UPI transfer • arjun@okaxis", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = VedvoraGold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // UPI Option 2: PhonePe / Paytm
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedMethod == "UPI_PHONEPE") VedvoraGold.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMethod = "UPI_PHONEPE" }
                            .border(
                                width = if (selectedMethod == "UPI_PHONEPE") 1.5.dp else 0.dp,
                                color = VedvoraGold,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = (selectedMethod == "UPI_PHONEPE"),
                                    onClick = { selectedMethod = "UPI_PHONEPE" },
                                    colors = RadioButtonDefaults.colors(selectedColor = VedvoraGold)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("PhonePe / Paytm UPI", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Fast UPI pay • arjun@ybl", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Icon(Icons.Default.QrCode2, contentDescription = null, tint = VedvoraGold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Option 3: Concierge Card
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedMethod == "CARD") VedvoraGold.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMethod = "CARD" }
                            .border(
                                width = if (selectedMethod == "CARD") 1.5.dp else 0.dp,
                                color = VedvoraGold,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = (selectedMethod == "CARD"),
                                    onClick = { selectedMethod = "CARD" },
                                    colors = RadioButtonDefaults.colors(selectedColor = VedvoraGold)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Concierge Platinum Card", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Text("•••• •••• •••• 4242", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Icon(Icons.Default.CreditCard, contentDescription = null, tint = VedvoraGold)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            paymentState = "PROCESSING"
                            coroutineScope.launch {
                                delay(1400)
                                paymentState = "SUCCESS"
                                onConfirmPayment()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("pay_now_confirm_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VedvoraGold)
                    ) {
                        Text("PAY NOW  ₹$formattedAmount", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = VedvoraPrimary)
                    }

                } else if (paymentState == "PROCESSING") {
                    Spacer(modifier = Modifier.height(30.dp))
                    CircularProgressIndicator(
                        color = VedvoraGold,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Connecting to UPI Gateway...",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Authenticating ₹$formattedAmount transfer with bank",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(30.dp))

                } else if (paymentState == "SUCCESS") {
                    // UPI Successful Screen / Pop-up
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upi_payment_success_view")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                                .border(4.dp, Color(0xFFD1FAE5), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "₹$formattedAmount",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "UPI Payment Successful",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Transaction Details Card
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Paid To", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("Vedvora Estates HOA Ltd", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("UPI Ref No", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(transactionRefId, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VedvoraGold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Payment Method", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        when (selectedMethod) {
                                            "UPI_GPAY" -> "Google Pay UPI"
                                            "UPI_PHONEPE" -> "PhonePe UPI"
                                            else -> "Platinum Card"
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Status", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("COMPLETED ✓", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Download PDF Receipt Button
                        Button(
                            onClick = { handleDownloadPdf() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("download_pdf_receipt_btn"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("DOWNLOAD RECEIPT (PDF)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val shareText = "Vedvora Payment Receipt\nAmount: ₹$formattedAmount\nUPI Ref: $transactionRefId\nStatus: SUCCESSFUL"
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share Receipt"))
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Share", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VedvoraGold)
                            }

                            Button(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = VedvoraGold)
                            ) {
                                Text("DONE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VedvoraPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.TimeSlotChip(
    slot: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) VedvoraGold else MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = slot,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) VedvoraPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun BookingDialog(
    serviceName: String,
    onDismiss: () -> Unit,
    onConfirm: (timeSlot: String, notes: String) -> Unit
) {
    var selectedSlot by remember { mutableStateOf("08:00 PM") }
    var notesInput by remember { mutableStateOf("") }

    val timeSlots = listOf("06:00 PM", "08:00 PM", "09:30 PM")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .padding(16.dp)
                .border(1.dp, VedvoraGold.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .testTag("booking_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RoomService, contentDescription = null, tint = VedvoraGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RESERVE $serviceName",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Select Preferred Time Slot", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (slot in timeSlots) {
                        TimeSlotChip(
                            slot = slot,
                            isSelected = selectedSlot == slot,
                            onClick = { selectedSlot = slot }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = notesInput,
                    onValueChange = { notesInput = it },
                    label = { Text("Special Requests / Attendants") },
                    placeholder = { Text("e.g. Prepare 2 seats") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onConfirm(selectedSlot, notesInput) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VedvoraGold)
                ) {
                    Text("Confirm VIP Reservation", fontWeight = FontWeight.Bold, color = VedvoraPrimary)
                }
            }
        }
    }
}

@Composable
fun CrisisAlertDialog(
    onDismiss: () -> Unit,
    onDispatch: (details: String) -> Unit
) {
    var emergencyNote by remember { mutableStateOf("Urgent assistance needed at Penthouse 1204") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .padding(16.dp)
                .border(1.dp, VedvoraError.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .testTag("crisis_alert_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(VedvoraError.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Emergency, contentDescription = "Emergency", tint = VedvoraError, modifier = Modifier.size(32.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "CONCIERGE CRISIS ALERT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = VedvoraError,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "Direct high-priority dispatch to Security & Resident Concierge",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = emergencyNote,
                    onValueChange = { emergencyNote = it },
                    label = { Text("Emergency Incident Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onDispatch(emergencyNote) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VedvoraError)
                ) {
                    Text("DISPATCH CONCIERGE NOW", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
