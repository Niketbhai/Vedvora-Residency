package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.VedvoraRepository
import com.example.data.entity.ActivityLogEntity
import com.example.data.entity.BookingEntity
import com.example.data.entity.InvoiceEntity
import com.example.data.entity.NoticeEntity
import com.example.data.entity.VisitorEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    // User session
    val residentName = MutableStateFlow("Johnathan Doe")
    val residentUnit = MutableStateFlow("Emerald Heights • Tower C, Penthouse 1204")
    val residentStatus = MutableStateFlow("Platinum Member")

    // Snackbar / Toast feedback
    val userToastMessage = MutableStateFlow<String?>(null)

    init {
        val database = AppDatabase.getDatabase(application)
        repository = VedvoraRepository(database.vedvoraDao())
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
        }
    }

    fun triggerCrisisAlert(details: String) {
        viewModelScope.launch {
            isCrisisAlertOpen.value = false
            showToast("Crisis Alert Dispatched to Vedvora Security & Concierge Desk!")
        }
    }

    fun rsvpToGala() {
        showToast("RSVP Confirmed for Annual Resident Gala!")
    }

    fun showToast(msg: String) {
        userToastMessage.value = msg
    }

    fun clearToast() {
        userToastMessage.value = null
    }
}
