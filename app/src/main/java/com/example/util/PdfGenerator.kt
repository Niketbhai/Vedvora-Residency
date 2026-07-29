package com.example.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfGenerator {

    fun generateAndSavePaymentReceiptPdf(
        context: Context,
        residentName: String = "Arjun Sharma",
        residentUnit: String = "Tower A, Flat 1204",
        amountPaid: Double = 45000.0,
        transactionId: String = "UPI/2026/0729/982140",
        paymentMode: String = "UPI (Google Pay)",
        purpose: String = "Luxury Amenities & HOA Maintenance Dues"
    ): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points (595 x 842)
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint().apply { isAntiAlias = true }

        // Background
        paint.color = Color.rgb(250, 250, 250)
        canvas.drawRect(0f, 0f, 595f, 842f, paint)

        // Header Background Bar (Vedvora Navy)
        paint.color = Color.rgb(15, 23, 42) // #0F172A
        canvas.drawRect(0f, 0f, 595f, 130f, paint)

        // Gold Accent Line
        paint.color = Color.rgb(212, 175, 55) // Gold
        canvas.drawRect(0f, 126f, 595f, 130f, paint)

        // Title text
        paint.color = Color.rgb(212, 175, 55)
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("VEDVORA LUXURY ESTATE", 40f, 50f, paint)

        paint.color = Color.WHITE
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("OFFICIAL PAYMENT RECEIPT & TAX INVOICE", 40f, 75f, paint)
        canvas.drawText("Tower 1, Vedvora Boulevard, High-Tech City", 40f, 95f, paint)

        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val dateStr = dateFormat.format(Date())

        // Receipt Details Box
        paint.color = Color.WHITE
        canvas.drawRoundRect(40f, 150f, 555f, 310f, 16f, 16f, paint)
        paint.color = Color.rgb(226, 232, 240)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(40f, 150f, 555f, 310f, 16f, 16f, paint)
        paint.style = Paint.Style.FILL

        // Text inside Box
        paint.color = Color.rgb(15, 23, 42)
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("RECEIPT DETAILS", 60f, 180f, paint)

        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = Color.rgb(100, 116, 139)

        canvas.drawText("Receipt No:", 60f, 210f, paint)
        canvas.drawText("Date & Time:", 60f, 235f, paint)
        canvas.drawText("Resident Name:", 60f, 260f, paint)
        canvas.drawText("Resident Unit:", 60f, 285f, paint)

        paint.color = Color.rgb(15, 23, 42)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("VEDV-REC-2026-0729", 200f, 210f, paint)
        canvas.drawText(dateStr, 200f, 235f, paint)
        canvas.drawText(residentName, 200f, 260f, paint)
        canvas.drawText(residentUnit, 200f, 285f, paint)

        // Payment Summary Table
        paint.color = Color.WHITE
        canvas.drawRoundRect(40f, 330f, 555f, 550f, 16f, 16f, paint)
        paint.color = Color.rgb(226, 232, 240)
        paint.style = Paint.Style.STROKE
        canvas.drawRoundRect(40f, 330f, 555f, 550f, 16f, 16f, paint)
        paint.style = Paint.Style.FILL

        // Table Header
        paint.color = Color.rgb(241, 245, 249)
        canvas.drawRoundRect(40f, 330f, 555f, 370f, 16f, 16f, paint)
        paint.color = Color.rgb(15, 23, 42)
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Description", 60f, 355f, paint)
        canvas.drawText("Amount (INR)", 410f, 355f, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = Color.rgb(51, 65, 85)
        canvas.drawText("Monthly HOA & Security Charges", 60f, 400f, paint)
        canvas.drawText("₹32,500.00", 410f, 400f, paint)

        canvas.drawText("Luxury Club & Spa Access Fee", 60f, 430f, paint)
        canvas.drawText("₹8,500.00", 410f, 430f, paint)

        canvas.drawText("Valet & Concierge Premium Pass", 60f, 460f, paint)
        canvas.drawText("₹4,000.00", 410f, 460f, paint)

        // Divider
        paint.color = Color.rgb(226, 232, 240)
        canvas.drawLine(60f, 485f, 535f, 485f, paint)

        // Total
        paint.color = Color.rgb(16, 185, 129) // Success Green
        paint.textSize = 15f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("TOTAL PAID (SUCCESSFUL)", 60f, 520f, paint)
        canvas.drawText(String.format("₹%,.2f", amountPaid), 410f, 520f, paint)

        // Payment Mode & Verification Stamp Box
        paint.color = Color.rgb(240, 253, 244)
        canvas.drawRoundRect(40f, 570f, 555f, 670f, 16f, 16f, paint)
        paint.color = Color.rgb(34, 197, 94)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.5f
        canvas.drawRoundRect(40f, 570f, 555f, 670f, 16f, 16f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.rgb(22, 101, 52)
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("UPI PAYMENT STATUS: SUCCESSFUL ✓", 60f, 600f, paint)

        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Transaction Ref No: $transactionId", 60f, 625f, paint)
        canvas.drawText("Payment Gateway: $paymentMode", 60f, 645f, paint)

        // Footer
        paint.color = Color.rgb(148, 163, 184)
        paint.textSize = 10f
        canvas.drawText("This is an official computer-generated receipt issued by Vedvora Estate Management.", 70f, 780f, paint)
        canvas.drawText("Vedvora Estates Management © 2026. All rights reserved.", 130f, 800f, paint)

        pdfDocument.finishPage(page)

        // Write file
        var file: File? = null
        try {
            val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                ?: context.filesDir
            file = File(downloadsDir, "Vedvora_Payment_Receipt_${System.currentTimeMillis()}.pdf")
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }

        return file
    }
}
