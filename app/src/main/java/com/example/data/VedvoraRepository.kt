package com.example.data

import com.example.data.dao.VedvoraDao
import com.example.data.entity.ActivityLogEntity
import com.example.data.entity.BookingEntity
import com.example.data.entity.InvoiceEntity
import com.example.data.entity.NoticeEntity
import com.example.data.entity.VisitorEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class VedvoraRepository(private val dao: VedvoraDao) {

    val scheduledVisitors: Flow<List<VisitorEntity>> = dao.getScheduledVisitors()
    val recentEncounters: Flow<List<VisitorEntity>> = dao.getRecentEncounters()
    val invoices: Flow<List<InvoiceEntity>> = dao.getInvoices()
    val bookings: Flow<List<BookingEntity>> = dao.getBookings()
    val notices: Flow<List<NoticeEntity>> = dao.getNotices()
    val activityLogs: Flow<List<ActivityLogEntity>> = dao.getActivityLogs()

    suspend fun seedInitialDataIfEmpty() {
        if (invoices.first().isEmpty()) {
            dao.insertInvoice(
                InvoiceEntity(
                    title = "Club Membership & Services",
                    amount = 150.00,
                    dueDateStr = "Oct 31, 2023",
                    status = "Paid",
                    iconType = "membership"
                )
            )
            dao.insertInvoice(
                InvoiceEntity(
                    title = "Private Lounge Access",
                    amount = 120.00,
                    dueDateStr = "Sep 15, 2023",
                    status = "Paid",
                    iconType = "lounge"
                )
            )
            dao.insertInvoice(
                InvoiceEntity(
                    title = "Luxury Amenities Fee",
                    amount = 450.00,
                    dueDateStr = "Nov 05, 2023",
                    status = "Pending",
                    iconType = "amenity"
                )
            )
        }

        if (scheduledVisitors.first().isEmpty()) {
            dao.insertVisitor(
                VisitorEntity(
                    name = "Julian Pierce",
                    category = "Executive",
                    subtitle = "Bentley • LUK-001",
                    status = "Pending",
                    avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCWnteFUv3gojXN2JAWN9x1kh-NO9wEIlFsmWPti0aRBLMfVn_DeImmzfd7rw9MZGzAVXmBdfvMsaMJsX2PsSF4M2tpplLZHR_fHd6OHaVKM0DAZbcK3HxZb3Tl2DZADXyzrCO8ghSw6K781535i8XhF4KWCFXATMf9aiViqHihDJprwnTwYWKnYbAONqxsz4O5lQwDMSfHd3Q00KaNs6KB_DGvZzyTT05_mek6HdjUpC7sOUlj5x0LLd4Vghb4GsahEzu4sqM--7Y",
                    dateStr = "Today",
                    timeStr = "8:00 PM",
                    valetRequested = true,
                    loungeAccess = false,
                    isHistory = false
                )
            )
            dao.insertVisitor(
                VisitorEntity(
                    name = "The Sterling Family",
                    category = "VIP Guest",
                    subtitle = "4 Persons • Apt 1204",
                    status = "Pending",
                    avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDgMUL-xtrhctUEMLeEHOrmFETbUc0DZeHeoLDCZh7agnOCuUASPpNSKCJJmUk9NRC0NFSH3ByFU_Jf3sYo-XSU6Zm_lMEJGIkPBsjlE5LooDPAKVSfi6zbO1QIgYwGTdjcp86Ua4R0a92PS3kw7i89Ob97w7BRROGzMceN9n-y42l-ReHdovQBTOAzJO4FPMugE60-L1NUV-kIeK9sm4uNbMJN0adHJj_w32Z2X2W9cXX7Mka9Ya5rMGzP8eO1-6RFg2OBnUgc6FI",
                    dateStr = "Today",
                    timeStr = "6:30 PM",
                    valetRequested = false,
                    loungeAccess = true,
                    isHistory = false
                )
            )
        }

        if (recentEncounters.first().isEmpty()) {
            dao.insertVisitor(
                VisitorEntity(
                    name = "Kevin Malone",
                    category = "Maintenance Specialist",
                    subtitle = "Maintenance Specialist • 09:15 AM",
                    status = "Secure Exit",
                    avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCz-wyAwIAUVsRkifMCv3aE4mKsHEm-9teH6thFZwIWg2WbNP68QquCevVdXE8evgyDfMVZErmMHbCYRL-Np1myNmnC7HxhmNCiqVIIXU96Pi1kj524n2q2GuxFNQB1PuK6UD7QGN4YWDqptG1qXDx22g81Thu831qxwhsLWvFDd4uaV9p1lIqOZZrfdLUTXyNlu8kLriDzi1m0ZvulxTvD8v6mDYciJarysvB-H-BTRbY1hzjjsZG0FtvH_2-mQvjNDPfIjtuB5ZQ",
                    dateStr = "Today",
                    timeStr = "09:15 AM",
                    isHistory = true
                )
            )
            dao.insertVisitor(
                VisitorEntity(
                    name = "Parcel Logistics",
                    category = "Signature Service",
                    subtitle = "Signature Service • 04:30 PM",
                    status = "Delivered",
                    avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCSZs8XyWNGAtNHyv4HXTbboQTjM5OEGQcP0LdH0FFRrCCVLUfYPaTMu1EBiTF7NG41wifm-p0RMkVARk796b34blLOfmhBVuWJUH-i5WEHqe29KIj_pkCwTVV3EZtoKC07a5D8fCfoKP2QYl5QJDNJtcqh33gKNeCKZ9Cep7GREiCv4AGJUWWuX7w1JpFEY3gX87TPl2tKSpuz6zN3dherdI1zOj8Us8b0heeD40bxXMigox9d1C-jLK5mF1DhqHv3XR3b-j840WU",
                    dateStr = "Yesterday",
                    timeStr = "04:30 PM",
                    isHistory = true
                )
            )
        }

        if (bookings.first().isEmpty()) {
            dao.insertBooking(
                BookingEntity(
                    serviceName = "Private Cinema",
                    subtitle = "Your booking for 'Inception' starts at 8:00 PM tonight.",
                    startTimeStr = "8:00 PM Tonight",
                    status = "Priority Access",
                    attendants = "2 Ready"
                )
            )
        }

        if (notices.first().isEmpty()) {
            dao.insertNotice(
                NoticeEntity(
                    title = "Annual Resident Gala",
                    subtitle = "RSVP required by Friday",
                    isUrgent = true,
                    isCompleted = false
                )
            )
            dao.insertNotice(
                NoticeEntity(
                    title = "Elevator Maintenance",
                    subtitle = "Completed",
                    isUrgent = false,
                    isCompleted = true
                )
            )
        }

        if (activityLogs.first().isEmpty()) {
            dao.insertActivityLog(
                ActivityLogEntity(
                    title = "Monthly HOA Paid",
                    referenceCode = "Auth #LXV-99201",
                    timeAgoStr = "Today, 9:24 AM",
                    iconType = "payment"
                )
            )
            dao.insertActivityLog(
                ActivityLogEntity(
                    title = "Guest Arrived",
                    referenceCode = "Mr. Henderson (Concierge Ref)",
                    timeAgoStr = "Yesterday, 7:12 PM",
                    iconType = "guest"
                )
            )
            dao.insertActivityLog(
                ActivityLogEntity(
                    title = "Service Fulfilled",
                    referenceCode = "Unit Deep Cleaning #SRV-102",
                    timeAgoStr = "2 days ago",
                    iconType = "service"
                )
            )
        }
    }

    suspend fun addVisitor(
        name: String,
        category: String,
        subtitle: String,
        valet: Boolean,
        lounge: Boolean,
        time: String
    ) {
        val newVisitor = VisitorEntity(
            name = name,
            category = category,
            subtitle = subtitle,
            status = "Pending",
            dateStr = "Today",
            timeStr = time.ifBlank { "07:00 PM" },
            valetRequested = valet,
            loungeAccess = lounge,
            isHistory = false
        )
        dao.insertVisitor(newVisitor)
    }

    suspend fun payAllDues() {
        dao.payAllPendingInvoices()
        dao.insertActivityLog(
            ActivityLogEntity(
                title = "Total Outstanding Dues Paid",
                referenceCode = "Auth #LXV-${System.currentTimeMillis().toString().takeLast(5)}",
                timeAgoStr = "Just Now",
                iconType = "payment"
            )
        )
    }

    suspend fun addBooking(serviceName: String, subtitle: String, timeStr: String) {
        dao.insertBooking(
            BookingEntity(
                serviceName = serviceName,
                subtitle = subtitle,
                startTimeStr = timeStr,
                status = "Confirmed",
                attendants = "Confirmed"
            )
        )
        dao.insertActivityLog(
            ActivityLogEntity(
                title = "$serviceName Reserved",
                referenceCode = "Booking #${(100..999).random()}",
                timeAgoStr = "Just Now",
                iconType = "service"
            )
        )
    }
}
