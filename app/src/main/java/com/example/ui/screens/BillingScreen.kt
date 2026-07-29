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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.VedvoraError
import com.example.ui.theme.VedvoraGold
import com.example.ui.theme.VedvoraPrimary
import com.example.ui.theme.VedvoraPrimaryContainer
import com.example.ui.theme.VedvoraSecondary
import com.example.util.PdfGenerator
import com.example.viewmodel.VedvoraViewModel

@Composable
fun BillingScreen(
    viewModel: VedvoraViewModel
) {
    val context = LocalContext.current
    val invoices by viewModel.invoices.collectAsState()
    val residentName by viewModel.residentName.collectAsState()
    val residentUnit by viewModel.residentUnit.collectAsState()

    val pendingTotal = invoices.filter { it.status == "Pending" }.sumOf { it.amount }
    val displayAmount = if (pendingTotal > 0) pendingTotal else 0.0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("billing_screen"),
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
            // Hero Total Outstanding Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, VedvoraGold.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Decorative icon background
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = VedvoraGold.copy(alpha = 0.08f),
                        modifier = Modifier
                            .size(160.dp)
                            .align(Alignment.TopEnd)
                    )

                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Total Outstanding",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(VedvoraGold, Color(0xFFB4B4B4))
                                        )
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "LUXURY CLUB PLATINUM",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VedvoraPrimary,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "₹${String.format("%,.2f", displayAmount)}",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = (-1).sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    if (displayAmount > 0) {
                                        viewModel.isPaymentDialogOpen.value = true
                                    } else {
                                        viewModel.showToast("No pending dues to pay.")
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("pay_now_btn"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = VedvoraPrimaryContainer)
                            ) {
                                Icon(Icons.Default.Payments, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (displayAmount > 0) "Pay Now" else "Paid", fontWeight = FontWeight.Bold, color = VedvoraGold)
                            }

                            OutlinedButton(
                                onClick = {
                                    val pdf = PdfGenerator.generatePaymentHistoryStatementPdf(
                                        context = context,
                                        residentName = residentName,
                                        residentUnit = residentUnit,
                                        invoices = invoices
                                    )
                                    if (pdf != null) {
                                        viewModel.showToast("Statement PDF Downloaded!")
                                        PdfGenerator.openOrSharePdf(context, pdf)
                                    } else {
                                        viewModel.showToast("Failed to generate PDF statement.")
                                    }
                                },
                                modifier = Modifier
                                    .weight(1.1f)
                                    .height(48.dp)
                                    .testTag("download_statement_pdf_btn"),
                                shape = RoundedCornerShape(12.dp),
                                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(VedvoraGold, VedvoraGold)))
                            ) {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = VedvoraGold, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("PDF Statement", fontWeight = FontWeight.Bold, color = VedvoraGold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Dedicated PDF Download Card Feature Banner
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = VedvoraGold.copy(alpha = 0.12f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val pdf = PdfGenerator.generatePaymentHistoryStatementPdf(
                            context = context,
                            residentName = residentName,
                            residentUnit = residentUnit,
                            invoices = invoices
                        )
                        if (pdf != null) {
                            viewModel.showToast("Statement PDF generated & opened!")
                            PdfGenerator.openOrSharePdf(context, pdf)
                        }
                    }
                    .border(1.dp, VedvoraGold.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(VedvoraGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = "PDF Icon",
                                tint = VedvoraPrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "Download Payment Statement (PDF)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Get complete tax invoice & ledger statement in PDF format",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download",
                        tint = VedvoraGold,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Invoices / Services Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Club Membership & Services",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        val pdf = PdfGenerator.generatePaymentHistoryStatementPdf(
                            context = context,
                            residentName = residentName,
                            residentUnit = residentUnit,
                            invoices = invoices
                        )
                        if (pdf != null) {
                            PdfGenerator.openOrSharePdf(context, pdf)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = VedvoraGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Download All PDF",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = VedvoraGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    invoices.forEachIndexed { index, invoice ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            when (invoice.iconType) {
                                                "membership" -> VedvoraGold.copy(alpha = 0.15f)
                                                "lounge" -> VedvoraSecondary.copy(alpha = 0.15f)
                                                else -> VedvoraError.copy(alpha = 0.15f)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val icon = when (invoice.iconType) {
                                        "membership" -> Icons.Default.Stars
                                        "lounge" -> Icons.Default.Weekend
                                        else -> Icons.Default.Spa
                                    }
                                    val tint = when (invoice.iconType) {
                                        "membership" -> VedvoraGold
                                        "lounge" -> VedvoraSecondary
                                        else -> VedvoraError
                                    }
                                    Icon(icon, contentDescription = null, tint = tint)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(invoice.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Due ${invoice.dueDateStr}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("₹${String.format("%,.2f", invoice.amount)}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    if (invoice.status == "Paid") {
                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(VedvoraSecondary.copy(alpha = 0.15f))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text("Paid", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = VedvoraSecondary)
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(VedvoraError.copy(alpha = 0.15f))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text("Pending", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = VedvoraError)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                // Direct PDF receipt download button for each invoice
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            val pdf = PdfGenerator.generateAndSavePaymentReceiptPdf(
                                                context = context,
                                                residentName = residentName,
                                                residentUnit = residentUnit,
                                                amountPaid = invoice.amount,
                                                transactionId = "UPI/2026/0729/${(100000..999999).random()}",
                                                paymentMode = "UPI (Vedvora Pay)",
                                                purpose = invoice.title
                                            )
                                            if (pdf != null) {
                                                viewModel.showToast("Receipt PDF downloaded for ${invoice.title}!")
                                                PdfGenerator.openOrSharePdf(context, pdf)
                                            }
                                        }
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PictureAsPdf,
                                        contentDescription = "Download Receipt PDF",
                                        tint = VedvoraGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Club Summary Cards
            Text("Club Summary", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("MEMBERSHIP TIER", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(VedvoraGold.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("PLATINUM", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = VedvoraGold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Active since 2021", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("PREFERRED CONCIERGE CARD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CreditCard, contentDescription = null, tint = VedvoraGold)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("**** 4242", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VedvoraGold.copy(alpha = 0.08f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, VedvoraGold.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("PRIVILEGE UPDATE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = VedvoraGold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "\"Your complimentary spa sessions for this quarter have been unlocked.\"",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Atmospheric Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAMFXL2z2irUeMZNhTaFj8bS8dkt5r-4uK0PFWV4UVU8HiHaIdsu2R5PKFNa6CbSaEhkKkkIU4Ynvj6Bu-EJzfHYx6LJ5MmP6h-42pE9qZRo3ea_amU4qpIkbU4JsFOGNjOYd_cULKCmcm35yjUBGvRc4XOjpkU7tgbtwz2T-w-JXFYDo9E_iQhH1e7M40SyvZ9SNzZVGu0eNRxYznEclaiPf4RiYQtWrEjCFvql2JLeCZIMiTcrOUnbWyiBtLWiiLd0vptPyJq9SI",
                    contentDescription = "Atmospheric photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(VedvoraPrimary.copy(alpha = 0.85f), Color.Transparent)
                            )
                        )
                )
                Text(
                    text = "Exclusive Luxury Club experiences at your fingertips.",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(20.dp)
                        .fillMaxWidth(0.7f)
                )
            }
        }
    }
}

