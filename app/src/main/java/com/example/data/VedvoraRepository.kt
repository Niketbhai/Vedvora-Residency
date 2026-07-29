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
                    amount = 15000.00,
                    dueDateStr = "Oct 31, 2026",
                    status = "Paid",
                    iconType = "membership"
                )
            )
            dao.insertInvoice(
                InvoiceEntity(
                    title = "Private Lounge & Concierge Access",
                    amount = 12000.00,
                    dueDateStr = "Sep 15, 2026",
                    status = "Paid",
                    iconType = "lounge"
                )
            )
            dao.insertInvoice(
                InvoiceEntity(
                    title = "Luxury Amenities & Maintenance Fee",
                    amount = 45000.00,
                    dueDateStr = "Nov 05, 2026",
                    status = "Pending",
                    iconType = "amenity"
                )
            )
        }

        if (scheduledVisitors.first().isEmpty()) {
            val indianVipList = listOf(
                VisitorEntity(name = "Aarav Singhania", category = "Executive", subtitle = "Mercedes S-Class • MH-01-AV-0001", status = "Pending", dateStr = "Today", timeStr = "08:00 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Deepika Padukone", category = "VIP Guest", subtitle = "Maybach S680 • MH-02-DP-8888", status = "Pending", dateStr = "Today", timeStr = "06:30 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Vikramaditya Birla", category = "Executive", subtitle = "Rolls Royce Phantom • MH-01-VB-0007", status = "Pending", dateStr = "Today", timeStr = "07:00 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Ananya Sharma & Family", category = "VIP Guest", subtitle = "4 Guests • Tower A Escort (KA-05-AS-1204)", status = "Pending", dateStr = "Today", timeStr = "05:15 PM", valetRequested = false, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Rajesh Kumar", category = "Executive", subtitle = "BMW 7 Series • DL-01-RK-9999", status = "Pending", dateStr = "Today", timeStr = "04:30 PM", valetRequested = true, loungeAccess = false, isHistory = false),
                VisitorEntity(name = "Priya Nair", category = "VIP Guest", subtitle = "Audi A8L • KL-07-PN-5555", status = "Pending", dateStr = "Today", timeStr = "08:45 PM", valetRequested = false, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Siddharth Malhotra", category = "VIP Guest", subtitle = "Porsche Cayenne • MH-02-SM-7777", status = "Pending", dateStr = "Today", timeStr = "09:00 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Natarajan Chandrasekaran", category = "Executive", subtitle = "Jaguar I-Pace • MH-01-NC-1000", status = "Pending", dateStr = "Today", timeStr = "03:15 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Rohan Verma", category = "Executive", subtitle = "Tesla Model S • DL-03-RV-4040", status = "Pending", dateStr = "Today", timeStr = "02:30 PM", valetRequested = true, loungeAccess = false, isHistory = false),
                VisitorEntity(name = "Kavita Reddy", category = "VIP Guest", subtitle = "Range Rover Velar • TS-09-KR-2020", status = "Pending", dateStr = "Today", timeStr = "06:00 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Sunil Bharti", category = "Executive", subtitle = "Bentley Flying Spur • DL-01-SB-0001", status = "Pending", dateStr = "Today", timeStr = "07:30 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Nandan Nilekani", category = "VIP Guest", subtitle = "Volvo XC90 • KA-01-NN-1111", status = "Pending", dateStr = "Today", timeStr = "05:45 PM", valetRequested = false, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Anand Mahindra", category = "VIP Guest", subtitle = "Mahindra XUV700 Special • MH-04-AM-0001", status = "Pending", dateStr = "Today", timeStr = "08:15 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Kiran Mazumdar", category = "Executive", subtitle = "Lexus LS500h • KA-03-KM-9000", status = "Pending", dateStr = "Today", timeStr = "04:00 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Ratan Tata Estate Escort", category = "VIP Guest", subtitle = "Tata Avinya Concept • MH-01-RT-1937", status = "Pending", dateStr = "Today", timeStr = "07:15 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Aravind Srinivas", category = "Executive", subtitle = "Mercedes EQS • KA-05-AI-2026", status = "Pending", dateStr = "Today", timeStr = "06:45 PM", valetRequested = false, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Sanjeev Kapoor", category = "VIP Guest", subtitle = "Culinary Master • BMW X7 (MH-02-SK-1001)", status = "Pending", dateStr = "Today", timeStr = "01:00 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Rahul Dravid", category = "VIP Guest", subtitle = "Toyota Vellfire • KA-01-RD-1996", status = "Pending", dateStr = "Today", timeStr = "05:00 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Mithali Raj", category = "VIP Guest", subtitle = "Hyundai Ioniq 5 • TS-07-MR-2000", status = "Pending", dateStr = "Today", timeStr = "04:15 PM", valetRequested = false, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Neeraj Chopra", category = "VIP Guest", subtitle = "Range Rover Autobiography • HR-26-NC-8758", status = "Pending", dateStr = "Today", timeStr = "08:00 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Aditi Rao Hydari", category = "VIP Guest", subtitle = "Audi Q8 • MH-02-AH-3333", status = "Pending", dateStr = "Today", timeStr = "07:45 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Pankaj Tripathi", category = "VIP Guest", subtitle = "Mahindra Thar Earth • UP-32-PT-1008", status = "Pending", dateStr = "Today", timeStr = "06:15 PM", valetRequested = false, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Sabyasachi Mukherjee", category = "VIP Guest", subtitle = "Jaguar XJ • WB-02-SM-0007", status = "Pending", dateStr = "Today", timeStr = "03:45 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Manish Malhotra", category = "VIP Guest", subtitle = "Mercedes Maybach GLS600 • MH-02-MM-9999", status = "Pending", dateStr = "Today", timeStr = "08:30 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Gaurav Gupta", category = "Executive", subtitle = "Porsche Panamera • DL-01-GG-7000", status = "Pending", dateStr = "Today", timeStr = "05:30 PM", valetRequested = true, loungeAccess = false, isHistory = false),
                VisitorEntity(name = "Tarun Tahiliani", category = "VIP Guest", subtitle = "Land Rover Defender • DL-02-TT-5050", status = "Pending", dateStr = "Today", timeStr = "09:15 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Falguni Nayar", category = "Executive", subtitle = "BMW i7 Luxury • MH-01-FN-1010", status = "Pending", dateStr = "Today", timeStr = "02:00 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Ghazal Alagh", category = "VIP Guest", subtitle = "Mercedes GLE Coupe • HR-26-GA-8000", status = "Pending", dateStr = "Today", timeStr = "04:45 PM", valetRequested = false, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Peyush Bansal", category = "VIP Guest", subtitle = "Audi E-Tron GT • DL-01-PB-2020", status = "Pending", dateStr = "Today", timeStr = "07:20 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Vineeta Singh", category = "Executive", subtitle = "Volvo EX90 • MH-02-VS-1100", status = "Pending", dateStr = "Today", timeStr = "06:10 PM", valetRequested = true, loungeAccess = false, isHistory = false),
                VisitorEntity(name = "Aman Gupta", category = "VIP Guest", subtitle = "BMW M5 • DL-03-AG-1000", status = "Pending", dateStr = "Today", timeStr = "08:00 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Anupam Mittal", category = "VIP Guest", subtitle = "Aston Martin DB11 • MH-01-AM-5000", status = "Pending", dateStr = "Today", timeStr = "09:30 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Deepinder Goyal", category = "Executive", subtitle = "Lamborghini Urus • HR-26-DG-0001", status = "Pending", dateStr = "Today", timeStr = "07:00 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Sriharsha Majety", category = "Executive", subtitle = "Lexus RX500h • KA-01-SM-3030", status = "Pending", dateStr = "Today", timeStr = "03:00 PM", valetRequested = false, loungeAccess = false, isHistory = false),
                VisitorEntity(name = "Nithin Kamath", category = "Executive", subtitle = "Porsche Taycan • KA-05-NK-8000", status = "Pending", dateStr = "Today", timeStr = "05:20 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Nikhil Kamath", category = "Executive", subtitle = "Ferrari Roma • KA-05-NK-9000", status = "Pending", dateStr = "Today", timeStr = "08:40 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Vijay Shekhar Sharma", category = "Executive", subtitle = "BMW iX • UP-16-VS-1978", status = "Pending", dateStr = "Today", timeStr = "04:10 PM", valetRequested = true, loungeAccess = false, isHistory = false),
                VisitorEntity(name = "Bhavish Aggarwal", category = "VIP Guest", subtitle = "Electric Mobility Escort • KA-01-BA-2024", status = "Pending", dateStr = "Today", timeStr = "06:50 PM", valetRequested = false, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Ritesh Agarwal", category = "VIP Guest", subtitle = "Mercedes S-Class Guard • DL-01-RA-100", status = "Pending", dateStr = "Today", timeStr = "07:10 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Kunal Shah", category = "VIP Guest", subtitle = "Porsche 911 GT3 • MH-02-KS-9111", status = "Pending", dateStr = "Today", timeStr = "09:45 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Harsh Mariwala", category = "VIP Guest", subtitle = "Bentley Continental GT • MH-01-HM-2222", status = "Pending", dateStr = "Today", timeStr = "05:10 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Sajjan Jindal", category = "Executive", subtitle = "Rolls Royce Ghost • MH-01-SJ-7000", status = "Pending", dateStr = "Today", timeStr = "06:25 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Kumar Mangalam", category = "VIP Guest", subtitle = "Maybach S580 • MH-01-KM-5555", status = "Pending", dateStr = "Today", timeStr = "07:50 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Gautam Adani Estate Visitor", category = "Executive", subtitle = "Lexus LX600 • GJ-01-GA-0001", status = "Pending", dateStr = "Today", timeStr = "04:40 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Cyrus Poonawalla", category = "VIP Guest", subtitle = "Rolls Royce Cullinan • MH-12-CP-0001", status = "Pending", dateStr = "Today", timeStr = "08:20 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Adar Poonawalla", category = "Executive", subtitle = "Escort Fleet • MH-12-AP-0007", status = "Pending", dateStr = "Today", timeStr = "09:10 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Shiv Nadar", category = "Executive", subtitle = "Mercedes Maybach S650 • TN-01-SN-1000", status = "Pending", dateStr = "Today", timeStr = "03:30 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Radhakishan Damani", category = "VIP Guest", subtitle = "Toyota Land Cruiser LC300 • MH-01-RD-3000", status = "Pending", dateStr = "Today", timeStr = "05:50 PM", valetRequested = false, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Azim Premji", category = "VIP Guest", subtitle = "Volvo S90 • KA-01-AP-1010", status = "Pending", dateStr = "Today", timeStr = "06:05 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Sudha Murty", category = "VIP Guest", subtitle = "Honorary Delegate • KA-01-SM-100", status = "Pending", dateStr = "Today", timeStr = "02:15 PM", valetRequested = false, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Shashi Tharoor", category = "VIP Guest", subtitle = "BMW 5 Series • KL-01-ST-1956", status = "Pending", dateStr = "Today", timeStr = "07:35 PM", valetRequested = true, loungeAccess = true, isHistory = false),
                VisitorEntity(name = "Harsha Bhogle", category = "VIP Guest", subtitle = "Audi Q7 • MH-02-HB-3000", status = "Pending", dateStr = "Today", timeStr = "08:10 PM", valetRequested = false, loungeAccess = true, isHistory = false)
            )

            indianVipList.forEach { dao.insertVisitor(it) }
        }

        if (recentEncounters.first().isEmpty()) {
            dao.insertVisitor(
                VisitorEntity(
                    name = "Ramesh Kumar (HVAC Tech)",
                    category = "Maintenance Specialist",
                    subtitle = "HVAC Air Conditioning Service • 09:15 AM",
                    status = "Secure Exit",
                    dateStr = "Today",
                    timeStr = "09:15 AM",
                    isHistory = true
                )
            )
            dao.insertVisitor(
                VisitorEntity(
                    name = "Zomato / Swiggy Gourmet",
                    category = "Express Delivery",
                    subtitle = "Gourmet Order Delivered • Tower A Lobby • 01:30 PM",
                    status = "Delivered",
                    dateStr = "Today",
                    timeStr = "01:30 PM",
                    isHistory = true
                )
            )
        }

        if (bookings.first().isEmpty()) {
            dao.insertBooking(
                BookingEntity(
                    serviceName = "Deep Housekeeping & Sanitize",
                    subtitle = "Notes: Organic cleaning products for Master Suite",
                    startTimeStr = "Tomorrow at 10:00 AM",
                    status = "Confirmed",
                    attendants = "2 Staff Assigned",
                    category = "Cleaning",
                    specialNotes = "Organic cleaning products for Master Suite"
                )
            )
            dao.insertBooking(
                BookingEntity(
                    serviceName = "Air Conditioner HVAC Servicing",
                    subtitle = "Notes: Check Living Room Cooling Unit",
                    startTimeStr = "Today at 03:00 PM",
                    status = "In Progress",
                    attendants = "Senior Tech Ramesh K.",
                    category = "Maintenance",
                    specialNotes = "Check Living Room Cooling Unit"
                )
            )
            dao.insertBooking(
                BookingEntity(
                    serviceName = "Private Chef Rooftop Dining",
                    subtitle = "Notes: 5-Course Royal Indian & Pan-Asian Menu",
                    startTimeStr = "Oct 30 at 07:30 PM",
                    status = "Pending Concierge",
                    attendants = "Chef Sanjeev Kapoor Desk",
                    category = "Dining",
                    specialNotes = "5-Course Royal Indian & Pan-Asian Menu"
                )
            )
            dao.insertBooking(
                BookingEntity(
                    serviceName = "Penthouse Balcony AC & Glass Polish",
                    subtitle = "Service Completed by Master Maintenance Team",
                    startTimeStr = "Yesterday at 02:00 PM",
                    status = "Completed",
                    attendants = "Vedvora Maintenance Team",
                    category = "Maintenance",
                    specialNotes = "Completed with high polish finish"
                )
            )
        }

        if (notices.first().isEmpty()) {
            dao.insertNotice(
                NoticeEntity(
                    title = "Diwali Resident Gala & Cultural Evening",
                    subtitle = "RSVP required at Concierge Desk",
                    isUrgent = true,
                    isCompleted = false
                )
            )
            dao.insertNotice(
                NoticeEntity(
                    title = "Tower A Elevator Servicing",
                    subtitle = "Completed",
                    isUrgent = false,
                    isCompleted = true
                )
            )
        }

        if (activityLogs.first().isEmpty()) {
            dao.insertActivityLog(
                ActivityLogEntity(
                    title = "Monthly Maintenance Dues Paid (₹25,000)",
                    referenceCode = "Auth #LXV-IND-99201",
                    timeAgoStr = "Today, 9:24 AM",
                    iconType = "payment"
                )
            )
            dao.insertActivityLog(
                ActivityLogEntity(
                    title = "VIP Guest Arrived",
                    referenceCode = "Mr. Vikramaditya Birla (Gate Guard Escort)",
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
                attendants = "Confirmed",
                category = "Amenity"
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

    suspend fun addLifestyleRequest(
        serviceName: String,
        category: String,
        specialNotes: String,
        dateStr: String,
        timeStr: String
    ) {
        val timeSlot = "$dateStr at $timeStr"
        val subtitleStr = if (specialNotes.isNotBlank()) "Notes: $specialNotes" else "Scheduled via Concierge Desk"
        dao.insertBooking(
            BookingEntity(
                serviceName = serviceName,
                subtitle = subtitleStr,
                startTimeStr = timeSlot,
                status = "Pending Concierge",
                attendants = "Concierge Desk",
                category = category,
                specialNotes = specialNotes
            )
        )
        dao.insertActivityLog(
            ActivityLogEntity(
                title = "Request Submitted: $serviceName",
                referenceCode = "Req #${(1000..9999).random()}",
                timeAgoStr = "Just Now",
                iconType = "service"
            )
        )
    }

    suspend fun cancelBooking(id: Long, serviceName: String) {
        dao.deleteBooking(id)
        dao.insertActivityLog(
            ActivityLogEntity(
                title = "Cancelled Request: $serviceName",
                referenceCode = "Ref #${(1000..9999).random()}",
                timeAgoStr = "Just Now",
                iconType = "service"
            )
        )
    }

    suspend fun rescheduleBooking(id: Long, serviceName: String, newDateStr: String, newTimeStr: String) {
        val newTimeSlot = "$newDateStr at $newTimeStr"
        val newSubtitle = "Rescheduled for $newTimeSlot"
        dao.rescheduleBooking(id, newTimeSlot, newSubtitle)
        dao.insertActivityLog(
            ActivityLogEntity(
                title = "Rescheduled: $serviceName",
                referenceCode = "Time: $newTimeSlot",
                timeAgoStr = "Just Now",
                iconType = "service"
            )
        )
    }

    suspend fun recordGatePassScan(passType: String, code: String) {
        dao.insertActivityLog(
            ActivityLogEntity(
                title = "Gate Scan Verified ($passType)",
                referenceCode = "Code: $code • Security Approved",
                timeAgoStr = "Just Now",
                iconType = "guest"
            )
        )
    }

    suspend fun updateBookingStatus(id: Long, serviceName: String, status: String) {
        dao.updateBookingStatus(id, status)
        dao.insertActivityLog(
            ActivityLogEntity(
                title = "Status Changed: $serviceName",
                referenceCode = "New Status: $status",
                timeAgoStr = "Just Now",
                iconType = "service"
            )
        )
    }

    suspend fun rateBooking(id: Long, rating: Int, feedbackText: String, feedbackTags: String) {
        dao.updateBookingRating(id, rating, feedbackText, feedbackTags)
        dao.updateBookingStatus(id, "Completed")
        dao.insertActivityLog(
            ActivityLogEntity(
                title = "Service Rated $rating ★",
                referenceCode = "Feedback Submitted to Concierge",
                timeAgoStr = "Just Now",
                iconType = "service"
            )
        )
    }

    suspend fun postNotice(title: String, subtitle: String, category: String, isUrgent: Boolean) {
        dao.insertNotice(
            NoticeEntity(
                title = title,
                subtitle = subtitle,
                isUrgent = isUrgent,
                isCompleted = false
            )
        )
        dao.insertActivityLog(
            ActivityLogEntity(
                title = "Digital Notice Broadcasted",
                referenceCode = "[$category] $title",
                timeAgoStr = "Just Now",
                iconType = "service"
            )
        )
    }
}
