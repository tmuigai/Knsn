package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.KanisaniViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    viewModel: KanisaniViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val churches by viewModel.churches.collectAsState()
    val attendance by viewModel.attendance.collectAsState()
    val isOffline by viewModel.isOfflineMode.collectAsState()

    val currentChurch = churches.find { it.id == currentUser.churchId } ?: churches.firstOrNull()
    var selectedService by remember { mutableStateOf(currentChurch?.services?.firstOrNull()) }

    var checkInMethodTab by remember { mutableStateOf(0) } // 0: QR Scan, 1: Phone Check-in, 2: Guest / Manual
    var phoneInput by remember { mutableStateOf("") }
    var guestNameInput by remember { mutableStateOf("") }
    var showSuccessToast by remember { mutableStateOf(false) }
    var lastCheckedMember by remember { mutableStateOf("") }

    val unsyncedCount = attendance.count { !it.isSyncedOffline }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("attendance_screen_scroll"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {
        // Attendance Overview & Counter
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("attendance_counter_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Live Service Attendance",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${attendance.size} Checked In",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = RegalBlue
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(RegalBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.HowToReg,
                                contentDescription = null,
                                tint = RegalBlue,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Current Service: ${selectedService?.name ?: "Main Worship"} (${selectedService?.time ?: "Today"})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Offline notice
        if (unsyncedCount > 0) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = LightGold,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CloudOff, contentDescription = null, tint = GoldenAmber)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$unsyncedCount check-in(s) stored locally on device",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF78350F)
                            )
                        }
                        TextButton(onClick = { viewModel.syncOfflineData() }) {
                            Text("Sync", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Success banner
        item {
            AnimatedVisibility(visible = showSuccessToast) {
                Surface(
                    color = MpesaLight,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MpesaGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Checked In Successfully: $lastCheckedMember",
                            color = MpesaGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Check-in Method selector
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Select Check-In Method",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            TabRow(
                selectedTabIndex = checkInMethodTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = RegalBlue
            ) {
                Tab(
                    selected = checkInMethodTab == 0,
                    onClick = { checkInMethodTab = 0 },
                    text = { Text("QR Scanner", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_checkin_qr")
                )
                Tab(
                    selected = checkInMethodTab == 1,
                    onClick = { checkInMethodTab = 1 },
                    text = { Text("Phone No.", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_checkin_phone")
                )
                Tab(
                    selected = checkInMethodTab == 2,
                    onClick = { checkInMethodTab = 2 },
                    text = { Text("Guest / Manual", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tab_checkin_manual")
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    when (checkInMethodTab) {
                        0 -> {
                            // QR Scanner
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF1E293B)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.QrCode2,
                                        contentDescription = "QR Code",
                                        tint = WarmGold,
                                        modifier = Modifier.size(80.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Scan sanctuary entrance QR code",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        viewModel.recordAttendance(
                                            serviceId = selectedService?.id ?: "srv_main",
                                            serviceName = selectedService?.name ?: "Main Service",
                                            memberName = currentUser.name,
                                            phone = currentUser.phone,
                                            method = CheckInMethod.QR_SCAN
                                        )
                                        lastCheckedMember = currentUser.name
                                        showSuccessToast = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = RegalBlue),
                                    modifier = Modifier.fillMaxWidth().testTag("scan_qr_simulate_button")
                                ) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Simulate Quick QR Check-In")
                                }
                            }
                        }

                        1 -> {
                            // Phone Number Entry
                            Column {
                                OutlinedTextField(
                                    value = phoneInput,
                                    onValueChange = { phoneInput = it },
                                    label = { Text("Member Phone Number") },
                                    placeholder = { Text("e.g. 0722 123 456") },
                                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.fillMaxWidth().testTag("attendance_phone_input"),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        val p = phoneInput.ifBlank { "+254 712 345 678" }
                                        viewModel.recordAttendance(
                                            serviceId = selectedService?.id ?: "srv_main",
                                            serviceName = selectedService?.name ?: "Main Service",
                                            memberName = "Member ($p)",
                                            phone = p,
                                            method = CheckInMethod.PHONE_NUMBER
                                        )
                                        lastCheckedMember = p
                                        showSuccessToast = true
                                        phoneInput = ""
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = RegalBlue),
                                    modifier = Modifier.fillMaxWidth().testTag("submit_phone_checkin_button")
                                ) {
                                    Text("Check In by Phone")
                                }
                            }
                        }

                        2 -> {
                            // Guest / Manual
                            Column {
                                OutlinedTextField(
                                    value = guestNameInput,
                                    onValueChange = { guestNameInput = it },
                                    label = { Text("Visitor / Member Full Name") },
                                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                    modifier = Modifier.fillMaxWidth().testTag("attendance_guest_name"),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = phoneInput,
                                    onValueChange = { phoneInput = it },
                                    label = { Text("Phone Number") },
                                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.fillMaxWidth().testTag("attendance_guest_phone"),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        val name = guestNameInput.ifBlank { "First-time Visitor" }
                                        val phone = phoneInput.ifBlank { "Unlisted" }
                                        viewModel.recordAttendance(
                                            serviceId = selectedService?.id ?: "srv_main",
                                            serviceName = selectedService?.name ?: "Main Service",
                                            memberName = name,
                                            phone = phone,
                                            method = CheckInMethod.MANUAL
                                        )
                                        lastCheckedMember = name
                                        showSuccessToast = true
                                        guestNameInput = ""
                                        phoneInput = ""
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = RegalBlue),
                                    modifier = Modifier.fillMaxWidth().testTag("submit_manual_checkin_button")
                                ) {
                                    Text("Register & Check In")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Attendance Roster List
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Today's Check-In Roster",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(attendance) { record ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(RegalBlue.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (record.checkInMethod) {
                                    CheckInMethod.QR_SCAN -> Icons.Default.QrCode
                                    CheckInMethod.PHONE_NUMBER -> Icons.Default.Phone
                                    CheckInMethod.MANUAL -> Icons.Default.EditNote
                                },
                                contentDescription = null,
                                tint = RegalBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = record.memberName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${record.serviceName} • ${record.timestamp}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        color = if (record.isSyncedOffline) MpesaLight else LightGold,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (record.isSyncedOffline) "SYNCED" else "OFFLINE",
                            color = if (record.isSyncedOffline) MpesaGreen else Color(0xFF78350F),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
