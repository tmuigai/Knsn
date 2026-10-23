package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentStatus
import com.example.ui.KanisaniViewModel
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: KanisaniViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val attendance by viewModel.attendance.collectAsState()

    val totalGiven = transactions
        .filter { it.status == PaymentStatus.COMPLETED && it.memberId == currentUser.id }
        .sumOf { it.amountKes }

    val myAttendanceCount = attendance.count { it.memberId == currentUser.id }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen_scroll"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {
        // Digital Church ID Card (Sleek plastic card appearance)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("digital_church_id_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    RegalBlue,
                                    RoyalIndigo,
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(WarmGold),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Church,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        "KANISANI DIGITAL ID",
                                        color = WarmGold,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 11.sp,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        currentUser.churchName,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Surface(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = currentUser.role.label,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = currentUser.name,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = currentUser.phone,
                                    color = Color.LightGray,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Membership No: ${currentUser.membershipNumber}",
                                    color = WarmGold,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            // QR Code badge on ID
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.size(68.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.QrCode2,
                                        contentDescription = "Member QR",
                                        tint = Color.Black,
                                        modifier = Modifier.size(54.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Divider(color = Color.White.copy(alpha = 0.2f))

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Status: ${currentUser.baptismStatus}",
                                color = Color.LightGray,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Enrolled: ${currentUser.joinYear}",
                                color = Color.LightGray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Stats Row
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("My Tithe & Giving", style = MaterialTheme.typography.labelSmall)
                        Text(
                            "KES ${String.format("%,.0f", totalGiven)}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = MpesaGreen
                            )
                        )
                        Text("Verified receipts", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Attendance Count", style = MaterialTheme.typography.labelSmall)
                        Text(
                            "$myAttendanceCount Services",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = RegalBlue
                            )
                        )
                        Text("Sunday & Midweek", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Ministries Joined
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "My Ministries & Departments",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            currentUser.groupsJoined.forEach { groupName ->
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = RegalBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(groupName, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Account Details & Info
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Church Details & Branch",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Church: ${currentUser.churchName}", fontWeight = FontWeight.Bold)
                    Text("Branch: ${currentUser.branch}", style = MaterialTheme.typography.bodySmall)
                    Text("Email: ${currentUser.email}", style = MaterialTheme.typography.bodySmall)
                    Text("Gender: ${currentUser.gender}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.toggleRoleSwitcher(true) },
                        modifier = Modifier.fillMaxWidth().testTag("profile_switch_role_button")
                    ) {
                        Text("Switch Hierarchy Role")
                    }
                }
            }
        }
    }
}
