package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.KanisaniViewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RegalBlue
import com.example.ui.theme.WarmGold

class MainActivity : ComponentActivity() {

    private val viewModel: KanisaniViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                KanisaniApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun KanisaniApp(viewModel: KanisaniViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isOffline by viewModel.isOfflineMode.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val attendance by viewModel.attendance.collectAsState()
    val members by viewModel.members.collectAsState()
    val churches by viewModel.churches.collectAsState()

    val selectedHymn by viewModel.selectedHymn.collectAsState()
    val selectedSermon by viewModel.selectedSermon.collectAsState()
    val showRoleSwitcher by viewModel.showRoleSwitcher.collectAsState()
    val showMpesaStkModal by viewModel.showMpesaStkModal.collectAsState()
    val activeGivingChurch by viewModel.activeGivingChurch.collectAsState()
    val lastCompletedTx by viewModel.lastCompletedTx.collectAsState()

    val pendingSyncCount = transactions.count { it.isOfflineQueued } + attendance.count { !it.isSyncedOffline }

    val fontSizeScale by viewModel.fontSizeScale.collectAsState()

    // Intercept back button to navigate through stack
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            KanisaniTopBar(
                currentRole = currentUser.role,
                isOffline = isOffline,
                pendingSyncCount = pendingSyncCount,
                fontSizeScale = fontSizeScale,
                onCycleFontSize = { viewModel.cycleFontSize() },
                onToggleOffline = { viewModel.toggleOfflineMode() },
                onOpenRoleSwitcher = { viewModel.toggleRoleSwitcher(true) },
                onSyncClick = { viewModel.syncOfflineData() }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bottom_nav_bar")
            ) {
                val navItems = listOf(
                    Triple(AppScreen.HOME, "Home", Icons.Default.Home),
                    Triple(AppScreen.GIVING, "Giving", Icons.Default.Payments),
                    Triple(AppScreen.WORSHIP, "Worship", Icons.Default.LibraryMusic),
                    Triple(AppScreen.ATTENDANCE, "Check-In", Icons.Default.QrCodeScanner),
                    Triple(AppScreen.ADMIN, "CMS", Icons.Default.AdminPanelSettings)
                )

                navItems.forEach { (screen, label, icon) ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.navigateTo(screen) },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("nav_item_${screen.name}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                AppScreen.GIVING -> GivingScreen(viewModel = viewModel)
                AppScreen.WORSHIP -> WorshipScreen(viewModel = viewModel)
                AppScreen.ATTENDANCE -> AttendanceScreen(viewModel = viewModel)
                AppScreen.GROUPS -> GroupsScreen(viewModel = viewModel)
                AppScreen.MARKETPLACE -> MarketplaceScreen(viewModel = viewModel)
                AppScreen.ADMIN -> AdminPanelScreen(viewModel = viewModel)
                AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
            }
        }
    }

    // Role Switcher Dialog
    if (showRoleSwitcher) {
        RoleSwitcherDialog(
            currentRole = currentUser.role,
            members = members,
            onSelectRole = { role -> viewModel.selectRole(role) },
            onSelectUser = { member -> viewModel.switchUser(member) },
            onDismiss = { viewModel.toggleRoleSwitcher(false) }
        )
    }

    // M-Pesa STK Push Dialog
    if (showMpesaStkModal) {
        val givingChurch = activeGivingChurch ?: churches.firstOrNull() ?: com.example.data.local.SampleData.churches.first()
        MpesaStkDialog(
            churches = churches,
            selectedChurch = givingChurch,
            defaultPhone = currentUser.phone,
            isOffline = isOffline,
            onConfirmPayment = { amt, type, phone, church ->
                viewModel.processMpesaGiving(amt, type, phone, church)
            },
            onDismiss = { viewModel.closeGivingModal() }
        )
    }

    // M-Pesa Receipt Dialog
    lastCompletedTx?.let { tx ->
        MpesaReceiptDialog(
            transaction = tx,
            onDismiss = { viewModel.clearReceipt() }
        )
    }

    // Hymn Detail Reader Dialog
    selectedHymn?.let { hymn ->
        HymnDetailDialog(
            hymn = hymn,
            onToggleFavorite = { viewModel.toggleHymnFavorite(hymn.id) },
            onDismiss = { viewModel.selectHymn(null) }
        )
    }

    // Sermon Detail Dialog
    selectedSermon?.let { sermon ->
        SermonDetailDialog(
            sermon = sermon,
            onToggleDownload = { viewModel.toggleSermonDownload(sermon.id) },
            onDismiss = { viewModel.selectSermon(null) }
        )
    }
}
