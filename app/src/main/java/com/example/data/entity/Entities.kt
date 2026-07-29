package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visitors")
data class VisitorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // Executive, VIP Guest, Maintenance, Delivery
    val subtitle: String, // e.g., "Bentley • LUK-001" or "4 Persons • Apt 1204"
    val status: String, // Pending, Arrived, Secure Exit, Delivered
    val avatarUrl: String? = null,
    val dateStr: String,
    val timeStr: String,
    val valetRequested: Boolean = false,
    val loungeAccess: Boolean = false,
    val isHistory: Boolean = false
)

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Double,
    val dueDateStr: String,
    val status: String, // Paid, Pending
    val iconType: String // membership, lounge, amenity
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val serviceName: String,
    val subtitle: String,
    val startTimeStr: String,
    val status: String,
    val attendants: String = "1 Ready",
    val category: String = "Lifestyle",
    val specialNotes: String = "",
    val rating: Int = 0,
    val feedbackText: String = "",
    val feedbackTags: String = ""
)

@Entity(tableName = "notices")
data class NoticeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subtitle: String,
    val isUrgent: Boolean = false,
    val isCompleted: Boolean = false
)

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val referenceCode: String,
    val timeAgoStr: String,
    val iconType: String // payment, guest, service
)
