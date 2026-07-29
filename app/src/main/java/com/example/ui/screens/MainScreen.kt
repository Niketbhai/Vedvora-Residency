package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ui.components.BookingDialog
import com.example.ui.components.CrisisAlertDialog
import com.example.ui.components.GatePassDialog
import com.example.ui.components.PaymentDialog
import com.example.ui.components.PreAuthorizeVisitorDialog
import com.example.ui.components.VedvoraBottomBar
import com.example.ui.components.VedvoraTab
import com.example.ui.components.VedvoraTopBar
import com.example.viewmodel.VedvoraViewModel

@Composable
fun MainScreen(
    viewModel: VedvoraViewModel,
    onSignOut: () -> Unit
) {
    var currentTab by remember { mutableStateOf<String>(VedvoraTab.Home.route) }
    val snackbarHostState = remember { SnackbarHostState() }

    val userToastMessage by viewModel.userToastMessage.collectAsState()
    val isGatePassOpen by viewModel.isGatePassDialogOpen.collectAsState()
    val isPreAuthorizeOpen by viewModel.isPreAuthorizeVisitorOpen.collectAsState()
    val isPaymentOpen by viewModel.isPaymentDialogOpen.collectAsState()
    val isBookingOpen by viewModel.isBookingDialogOpen.collectAsState()
    val selectedService by viewModel.selectedBookingService.collectAsState()
    val isCrisisAlertOpen by viewModel.isCrisisAlertOpen.collectAsState()

    LaunchedEffect(userToastMessage) {
        userToastMessage?.let { msg ->
            snackbarHostState.showSnackbar(message = msg, duration = SnackbarDuration.Short)
            viewModel.clearToast()
        }
    }

    Scaffold(
        topBar = {
            VedvoraTopBar(
                showSearch = (currentTab == VedvoraTab.Guests.route || currentTab == VedvoraTab.VIP.route),
                onSearchClick = { viewModel.showToast("Search active") },
                onNotificationClick = { viewModel.showToast("3 New Building Announcements") },
                onProfileClick = { currentTab = VedvoraTab.Account.route }
            )
        },
        bottomBar = {
            VedvoraBottomBar(
                currentRoute = currentTab,
                onTabSelected = { route -> currentTab = route }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_screen")
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                VedvoraTab.Home.route -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToVisitors = { currentTab = VedvoraTab.Guests.route },
                        onNavigateToBilling = { currentTab = VedvoraTab.Billing.route },
                        onNavigateToAmenities = { currentTab = VedvoraTab.Booking.route }
                    )
                }
                VedvoraTab.VIP.route -> {
                    VisitorsScreen(viewModel = viewModel)
                }
                VedvoraTab.Booking.route -> {
                    AmenitiesScreen(viewModel = viewModel)
                }
                VedvoraTab.Guests.route -> {
                    VisitorsScreen(viewModel = viewModel)
                }
                VedvoraTab.Billing.route -> {
                    BillingScreen(viewModel = viewModel)
                }
                VedvoraTab.Account.route -> {
                    ProfileScreen(
                        viewModel = viewModel,
                        onSignOut = onSignOut
                    )
                }
                else -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToVisitors = { currentTab = VedvoraTab.Guests.route },
                        onNavigateToBilling = { currentTab = VedvoraTab.Billing.route },
                        onNavigateToAmenities = { currentTab = VedvoraTab.Booking.route }
                    )
                }
            }
        }

        // Active Dialogs
        if (isGatePassOpen) {
            GatePassDialog(
                onDismiss = { viewModel.isGatePassDialogOpen.value = false },
                onShare = {
                    viewModel.isGatePassDialogOpen.value = false
                    viewModel.showToast("Digital Gate Pass shared to clipboard")
                }
            )
        }

        if (isPreAuthorizeOpen) {
            PreAuthorizeVisitorDialog(
                onDismiss = { viewModel.isPreAuthorizeVisitorOpen.value = false },
                onSubmit = { name, category, subtitle, valet, lounge, time ->
                    viewModel.submitNewVisitor(name, category, subtitle, valet, lounge, time)
                }
            )
        }

        if (isPaymentOpen) {
            PaymentDialog(
                amount = 450.00,
                onDismiss = { viewModel.isPaymentDialogOpen.value = false },
                onConfirmPayment = { viewModel.payAllDues() }
            )
        }

        if (isBookingOpen) {
            BookingDialog(
                serviceName = selectedService,
                onDismiss = { viewModel.isBookingDialogOpen.value = false },
                onConfirm = { slot, notes ->
                    viewModel.confirmBooking(slot, notes)
                }
            )
        }

        if (isCrisisAlertOpen) {
            CrisisAlertDialog(
                onDismiss = { viewModel.isCrisisAlertOpen.value = false },
                onDispatch = { notes ->
                    viewModel.triggerCrisisAlert(notes)
                }
            )
        }
    }
}
