package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.entity.InvoiceEntity
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
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points
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
        canvas.drawText(purpose, 60f, 400f, paint)
        canvas.drawText(String.format("₹%,.2f", amountPaid * 0.72), 410f, 400f, paint)

        canvas.drawText("Luxury Club & Service Dues", 60f, 430f, paint)
        canvas.drawText(String.format("₹%,.2f", amountPaid * 0.18), 410f, 430f, paint)

        canvas.drawText("Concierge & Maintenance Pass", 60f, 460f, paint)
        canvas.drawText(String.format("₹%,.2f", amountPaid * 0.10), 410f, 460f, paint)

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

    fun generatePaymentHistoryStatementPdf(
        context: Context,
        residentName: String,
        residentUnit: String,
        invoices: List<InvoiceEntity>
    ): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint().apply { isAntiAlias = true }

        // Background
        paint.color = Color.rgb(250, 250, 250)
        canvas.drawRect(0f, 0f, 595f, 842f, paint)

        // Header (Vedvora Gold/Navy Theme)
        paint.color = Color.rgb(15, 23, 42)
        canvas.drawRect(0f, 0f, 595f, 130f, paint)

        paint.color = Color.rgb(212, 175, 55)
        canvas.drawRect(0f, 126f, 595f, 130f, paint)

        paint.color = Color.rgb(212, 175, 55)
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("VEDVORA ESTATE MANAGEMENT", 40f, 50f, paint)

        paint.color = Color.WHITE
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("STATEMENT OF PAYMENT HISTORY & DUES", 40f, 75f, paint)
        canvas.drawText("High-Tech City, Vedvora Boulevard", 40f, 95f, paint)

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val currentDateStr = dateFormat.format(Date())

        // Resident Info Box
        paint.color = Color.WHITE
        canvas.drawRoundRect(40f, 145f, 555f, 235f, 12f, 12f, paint)
        paint.color = Color.rgb(226, 232, 240)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(40f, 145f, 555f, 235f, 12f, 12f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.rgb(15, 23, 42)
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Resident Name:", 60f, 170f, paint)
        canvas.drawText("Unit / Flat:", 60f, 195f, paint)
        canvas.drawText("Statement Date:", 60f, 220f, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText(residentName, 170f, 170f, paint)
        canvas.drawText(residentUnit, 170f, 195f, paint)
        canvas.drawText(currentDateStr, 170f, 220f, paint)

        // Statement Table Header
        var startY = 255f
        paint.color = Color.rgb(241, 245, 249)
        canvas.drawRoundRect(40f, startY, 555f, startY + 30f, 8f, 8f, paint)

        paint.color = Color.rgb(15, 23, 42)
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Description / Service", 55f, startY + 20f, paint)
        canvas.drawText("Due Date", 290f, startY + 20f, paint)
        canvas.drawText("Status", 400f, startY + 20f, paint)
        canvas.drawText("Amount (INR)", 470f, startY + 20f, paint)

        startY += 35f
        var totalPaid = 0.0
        var totalPending = 0.0

        invoices.forEachIndexed { index, item ->
            val rowY = startY + (index * 32f)
            if (rowY < 720f) {
                if (index % 2 == 1) {
                    paint.color = Color.rgb(248, 250, 252)
                    canvas.drawRect(40f, rowY - 12f, 555f, rowY + 18f, paint)
                }

                paint.color = Color.rgb(30, 41, 59)
                paint.textSize = 11f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

                val shortTitle = if (item.title.length > 28) item.title.take(26) + ".." else item.title
                canvas.drawText(shortTitle, 55f, rowY, paint)
                canvas.drawText(item.dueDateStr, 290f, rowY, paint)

                if (item.status == "Paid") {
                    totalPaid += item.amount
                    paint.color = Color.rgb(22, 101, 52)
                    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    canvas.drawText("Paid ✓", 400f, rowY, paint)
                } else {
                    totalPending += item.amount
                    paint.color = Color.rgb(220, 38, 38)
                    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    canvas.drawText("Pending", 400f, rowY, paint)
                }

                paint.color = Color.rgb(15, 23, 42)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText(String.format("₹%,.2f", item.amount), 470f, rowY, paint)
            }
        }

        val summaryY = startY + (invoices.size * 32f).coerceAtMost(420f) + 20f

        // Summary Divider Line
        paint.color = Color.rgb(212, 175, 55)
        paint.strokeWidth = 1.5f
        canvas.drawLine(40f, summaryY, 555f, summaryY, paint)

        // Summary Totals
        paint.color = Color.rgb(15, 23, 42)
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("TOTAL CLEARED / PAID:", 60f, summaryY + 28f, paint)

        paint.color = Color.rgb(22, 101, 52)
        canvas.drawText(String.format("₹%,.2f", totalPaid), 470f, summaryY + 28f, paint)

        if (totalPending > 0) {
            paint.color = Color.rgb(15, 23, 42)
            canvas.drawText("TOTAL OUTSTANDING / PENDING:", 60f, summaryY + 50f, paint)
            paint.color = Color.rgb(220, 38, 38)
            canvas.drawText(String.format("₹%,.2f", totalPending), 470f, summaryY + 50f, paint)
        }

        // Verification Seal Box
        val sealY = summaryY + 80f
        paint.color = Color.rgb(240, 253, 244)
        canvas.drawRoundRect(40f, sealY, 555f, sealY + 65f, 12f, 12f, paint)
        paint.color = Color.rgb(34, 197, 94)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(40f, sealY, 555f, sealY + 65f, 12f, 12f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.rgb(22, 101, 52)
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("VERIFIED BY VEDVORA TREASURY LEDGER", 60f, sealY + 25f, paint)

        paint.color = Color.rgb(71, 85, 105)
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Document Ref: VEDV-STMT-${System.currentTimeMillis().toString().takeLast(6)} • Digital Sign Verified", 60f, sealY + 45f, paint)

        // Footer
        paint.color = Color.rgb(148, 163, 184)
        paint.textSize = 9f
        canvas.drawText("This is an official statement generated by Vedvora Estate Management for resident accounting.", 65f, 800f, paint)

        pdfDocument.finishPage(page)

        var file: File? = null
        try {
            val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                ?: context.filesDir
            file = File(downloadsDir, "Vedvora_Payment_Statement_${System.currentTimeMillis()}.pdf")
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

    fun openOrSharePdf(context: Context, pdfFile: File) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                pdfFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                // Fallback to chooser / share
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Download & Open PDF Receipt").apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "PDF saved to Downloads: ${pdfFile.name}", Toast.LENGTH_LONG).show()
        }
    }
}

