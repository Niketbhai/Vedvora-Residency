package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.VedvoraRepository
import com.example.data.entity.ActivityLogEntity
import com.example.data.entity.BookingEntity
import com.example.data.entity.InvoiceEntity
import com.example.data.entity.NoticeEntity
import com.example.data.entity.VisitorEntity
import com.example.util.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class VedvoraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VedvoraRepository

    val visitorSearchQuery = MutableStateFlow("")
    val visitorFilter = MutableStateFlow("All Arrivals") // "All Arrivals", "VIP Lounge", "Valet", "Service"

    // Dialog states
    val isGatePassDialogOpen = MutableStateFlow(false)
    val isPreAuthorizeVisitorOpen = MutableStateFlow(false)
    val isPaymentDialogOpen = MutableStateFlow(false)
    val isBookingDialogOpen = MutableStateFlow(false)
    val selectedBookingService = MutableStateFlow("Private Chef")
    val isCrisisAlertOpen = MutableStateFlow(false)
    val isNotificationsOpen = MutableStateFlow(false)
    val isLoginDialogOpen = MutableStateFlow(false)
    val isProfilePhotoPickerOpen = MutableStateFlow(false)
    val isDigitalIdCardOpen = MutableStateFlow(false)
    val isPostNoticeDialogOpen = MutableStateFlow(false)

    // User session
    val residentName = MutableStateFlow("Arjun Sharma")
    val residentBuilding = MutableStateFlow("Tower A")
    val residentFlat = MutableStateFlow("Penthouse 1204")
    val residentUnit = MutableStateFlow("Tower A • Flat Penthouse 1204")
    val residentStatus = MutableStateFlow("Platinum VIP Member")
    val residentProfilePicPath = MutableStateFlow<String?>(null)

    // Lifestyle Request Dialog state
    val isSubmitLifestyleRequestOpen = MutableStateFlow(false)
    val selectedRescheduleBooking = MutableStateFlow<BookingEntity?> (null)
    val selectedRatingBooking = MutableStateFlow<BookingEntity?>(null)

    fun updateResidentDetails(name: String, building: String, flat: String) {
        val finalName = name.ifBlank { "Arjun Sharma" }.trim()
        val finalBuilding = building.ifBlank { "Tower A" }.trim()
        val finalFlat = flat.ifBlank { "Penthouse 1204" }.trim()

        residentName.value = finalName
        residentBuilding.value = finalBuilding
        residentFlat.value = finalFlat
        residentUnit.value = "$finalBuilding • Flat $finalFlat"
    }

    // Profile Photo Management
    fun updateProfileImageBitmap(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val context = getApplication<Application>()
                val file = File(context.filesDir, "resident_avatar.jpg")
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
                }
                val path = file.absolutePath
                context.getSharedPreferences("vedvora_prefs", Context.MODE_PRIVATE)
                    .edit().putString("profile_pic_path", path).apply()
                residentProfilePicPath.value = path
                showToast("Profile picture updated from camera!")
            } catch (e: Exception) {
                showToast("Failed to save camera photo.")
            }
        }
    }

    fun updateProfileImageUri(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val context = getApplication<Application>()
                val inputStream = context.contentResolver.openInputStream(uri)
                val file = File(context.filesDir, "resident_avatar.jpg")
                val outputStream = FileOutputStream(file)
                inputStream?.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
                val path = file.absolutePath
                context.getSharedPreferences("vedvora_prefs", Context.MODE_PRIVATE)
                    .edit().putString("profile_pic_path", path).apply()
                residentProfilePicPath.value = path
                showToast("Profile picture updated from gallery!")
            } catch (e: Exception) {
                showToast("Failed to load photo from gallery.")
            }
        }
    }

    fun removeProfileImage() {
        val context = getApplication<Application>()
        val file = File(context.filesDir, "resident_avatar.jpg")
        if (file.exists()) file.delete()
        context.getSharedPreferences("vedvora_prefs", Context.MODE_PRIVATE)
            .edit().remove("profile_pic_path").apply()
        residentProfilePicPath.value = null
        showToast("Profile picture restored to default.")
    }

    // Snackbar / Toast feedback
    val userToastMessage = MutableStateFlow<String?>(null)

    init {
        val database = AppDatabase.getDatabase(application)
        repository = VedvoraRepository(database.vedvoraDao())

        // Load saved profile picture
        val savedPath = application.getSharedPreferences("vedvora_prefs", Context.MODE_PRIVATE)
            .getString("profile_pic_path", null)
        if (savedPath != null && File(savedPath).exists()) {
            residentProfilePicPath.value = savedPath
        }

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    val scheduledVisitors: StateFlow<List<VisitorEntity>> = combine(
        repository.scheduledVisitors,
        visitorSearchQuery,
        visitorFilter
    ) { list, query, filter ->
        list.filter { visitor ->
            val matchesQuery = query.isBlank() ||
                    visitor.name.contains(query, ignoreCase = true) ||
                    visitor.subtitle.contains(query, ignoreCase = true) ||
                    visitor.category.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                "VIP Lounge" -> visitor.loungeAccess || visitor.category.contains("VIP", ignoreCase = true)
                "Valet" -> visitor.valetRequested
                "Service" -> visitor.category.contains("Maintenance", ignoreCase = true) || visitor.category.contains("Delivery", ignoreCase = true)
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val recentEncounters: StateFlow<List<VisitorEntity>> = repository.recentEncounters
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val invoices: StateFlow<List<InvoiceEntity>> = repository.invoices
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val bookings: StateFlow<List<BookingEntity>> = repository.bookings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val notices: StateFlow<List<NoticeEntity>> = repository.notices
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val activityLogs: StateFlow<List<ActivityLogEntity>> = repository.activityLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun openBookingFor(serviceName: String) {
        selectedBookingService.value = serviceName
        isBookingDialogOpen.value = true
    }

    fun submitNewVisitor(
        name: String,
        category: String,
        subtitle: String,
        valet: Boolean,
        lounge: Boolean,
        timeStr: String
    ) {
        viewModelScope.launch {
            repository.addVisitor(name, category, subtitle, valet, lounge, timeStr)
            isPreAuthorizeVisitorOpen.value = false
            showToast("Visitor pass created for $name")
        }
    }

    fun payAllDues() {
        viewModelScope.launch {
            repository.payAllDues()
            isPaymentDialogOpen.value = false
            showToast("Payment successful! Outstanding dues cleared.")
        }
    }

    fun updateBookingStatus(bookingId: Long, serviceName: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, serviceName, newStatus)
            NotificationHelper.showServiceStatusNotification(
                context = getApplication(),
                serviceName = serviceName,
                newStatus = newStatus,
                additionalDetails = "Resident Unit: ${residentUnit.value}"
            )
            showToast("Mobile Push Notification Sent: '$serviceName' is now $newStatus")
        }
    }

    fun confirmBooking(timeSlot: String, notes: String) {
        viewModelScope.launch {
            val service = selectedBookingService.value
            repository.addBooking(
                serviceName = service,
                subtitle = "Reserved for $timeSlot ${if (notes.isNotBlank()) "($notes)" else ""}",
                timeStr = timeSlot
            )
            isBookingDialogOpen.value = false
            showToast("Booking confirmed for $service ($timeSlot)")

            // Real-time Push Notification
            NotificationHelper.showServiceStatusNotification(
                context = getApplication(),
                serviceName = service,
                newStatus = "Confirmed",
                additionalDetails = "Time Slot: $timeSlot"
            )

            // Auto-simulate status progress in realtime for testing push alerts
            launch {
                delay(6000)
                val currentBookings = bookings.value
                val booking = currentBookings.firstOrNull { it.serviceName == service }
                val targetId = booking?.id ?: 0L
                repository.updateBookingStatus(targetId, service, "In Progress")
                NotificationHelper.showServiceStatusNotification(
                    context = getApplication(),
                    serviceName = service,
                    newStatus = "In Progress",
                    additionalDetails = "Senior Specialist Assigned • En Route"
                )

                delay(10000)
                repository.updateBookingStatus(targetId, service, "Completed")
                NotificationHelper.showServiceStatusNotification(
                    context = getApplication(),
                    serviceName = service,
                    newStatus = "Completed",
                    additionalDetails = "Service fulfilled successfully. Please rate your experience."
                )
            }
        }
    }

    fun triggerCrisisAlert(details: String) {
        viewModelScope.launch {
            isCrisisAlertOpen.value = false
            NotificationHelper.showServiceStatusNotification(
                context = getApplication(),
                serviceName = "EMERGENCY CRISIS ALERT",
                newStatus = "Dispatched",
                additionalDetails = "Vedvora Armed Response & Patrol Dispatched to ${residentUnit.value}"
            )
            showToast("Crisis Alert Dispatched to Vedvora Security & Concierge Desk!")
        }
    }

    fun submitLifestyleRequest(
        serviceTitle: String,
        category: String,
        notes: String,
        dateStr: String,
        timeStr: String
    ) {
        viewModelScope.launch {
            repository.addLifestyleRequest(serviceTitle, category, notes, dateStr, timeStr)
            isSubmitLifestyleRequestOpen.value = false
            showToast("Lifestyle request '$serviceTitle' dispatched to Concierge Desk!")

            // Real-time Push Notification
            NotificationHelper.showServiceStatusNotification(
                context = getApplication(),
                serviceName = serviceTitle,
                newStatus = "Pending Concierge",
                additionalDetails = "Scheduled for $dateStr at $timeStr"
            )

            // Auto-simulate status progression in real-time
            launch {
                delay(6000)
                val currentBookings = bookings.value
                val booking = currentBookings.firstOrNull { it.serviceName == serviceTitle }
                val targetId = booking?.id ?: 0L
                repository.updateBookingStatus(targetId, serviceTitle, "In Progress")
                NotificationHelper.showServiceStatusNotification(
                    context = getApplication(),
                    serviceName = serviceTitle,
                    newStatus = "In Progress",
                    additionalDetails = "Concierge desk is processing request: $notes"
                )

                delay(10000)
                repository.updateBookingStatus(targetId, serviceTitle, "Completed")
                NotificationHelper.showServiceStatusNotification(
                    context = getApplication(),
                    serviceName = serviceTitle,
                    newStatus = "Completed",
                    additionalDetails = "Request successfully fulfilled."
                )
            }
        }
    }

    fun cancelBooking(bookingId: Long, serviceName: String) {
        viewModelScope.launch {
            repository.cancelBooking(bookingId, serviceName)
            showToast("Request for '$serviceName' cancelled.")
        }
    }

    fun rescheduleBooking(bookingId: Long, serviceName: String, dateStr: String, timeStr: String) {
        viewModelScope.launch {
            repository.rescheduleBooking(bookingId, serviceName, dateStr, timeStr)
            selectedRescheduleBooking.value = null
            showToast("'$serviceName' rescheduled for $dateStr at $timeStr.")
        }
    }

    fun approveGateScan(passType: String, passCode: String, isOriginal: Boolean) {
        viewModelScope.launch {
            repository.recordGatePassScan(passType, passCode)
            isGatePassDialogOpen.value = false
            val passCategoryLabel = if (isOriginal) "Original Resident Master Pass" else "Duplicate Visitor Pass"
            showToast("Gate Access Approved ($passCategoryLabel #$passCode) • Security Officer Verified • Apartment Entry Granted!")
        }
    }

    fun submitServiceRating(bookingId: Long, rating: Int, feedbackText: String, feedbackTags: String) {
        viewModelScope.launch {
            repository.rateBooking(bookingId, rating, feedbackText, feedbackTags)
            selectedRatingBooking.value = null
            showToast("★ $rating-Star Feedback Submitted! Thank you for rating Vedvora Concierge.")
        }
    }

    fun rsvpToGala() {
        showToast("RSVP Confirmed for Annual Resident Gala!")
    }

    fun postNotice(title: String, subtitle: String, category: String, isUrgent: Boolean) {
        viewModelScope.launch {
            repository.postNotice(title, subtitle, category, isUrgent)
            NotificationHelper.showServiceStatusNotification(
                context = getApplication(),
                serviceName = "NOTICE: $title",
                newStatus = if (isUrgent) "EMERGENCY BROADCAST" else category,
                additionalDetails = subtitle
            )
            showToast("Digital Notice Posted & Broadcasted to Residents!")
        }
    }

    fun showToast(msg: String) {
        userToastMessage.value = msg
    }

    fun clearToast() {
        userToastMessage.value = null
    }
}
