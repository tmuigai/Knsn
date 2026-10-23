package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.KanisaniViewModel
import com.example.ui.theme.*

@Composable
fun GivingScreen(
    viewModel: KanisaniViewModel,
    modifier: Modifier = Modifier
) {
    val transactions by viewModel.transactions.collectAsState()
    val isOffline by viewModel.isOfflineMode.collectAsState()
    val churches by viewModel.churches.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val currentChurch = churches.find { it.id == currentUser.churchId } ?: churches.firstOrNull()

    val totalGiven = transactions
        .filter { it.status == PaymentStatus.COMPLETED }
        .sumOf { it.amountKes }

    val queuedCount = transactions.count { it.status == PaymentStatus.QUEUED_OFFLINE }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("giving_screen_scroll"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {
        // Summary Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("giving_summary_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Given (2026)",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "KES ${String.format("%,.0f", totalGiven)}",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = MpesaGreen
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MpesaGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Payments,
                                contentDescription = null,
                                tint = MpesaGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.openGivingModal(currentChurch) },
                        colors = ButtonDefaults.buttonColors(containerColor = MpesaGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("give_now_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.SendToMobile, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isOffline) "Give Now (Queue Offline)" else "Give via M-Pesa STK Push",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        // Offline Queue Warning & Sync Action
        if (queuedCount > 0) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = LightGold,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().testTag("offline_giving_queue_alert")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = GoldenAmber,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "$queuedCount Payment(s) Queued Offline",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF78350F)
                                )
                                Text(
                                    text = "Ready to push to Safaricom network",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF78350F)
                                )
                            }
                        }
                        Button(
                            onClick = { viewModel.syncOfflineData() },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldenAmber),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Sync All")
                        }
                    }
                }
            }
        }

        // Categories Guide
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Biblical Giving Channels",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GivingCategoryChip(
                    title = "Tithe (10%)",
                    desc = "Storehouse provision",
                    modifier = Modifier.weight(1f)
                )
                GivingCategoryChip(
                    title = "Sadaka",
                    desc = "General worship",
                    modifier = Modifier.weight(1f)
                )
                GivingCategoryChip(
                    title = "Ujenzi",
                    desc = "Sanctuary project",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Transactions History
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Contribution Ledger & Receipts",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${transactions.size} records",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(transactions) { tx ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        // Show receipt
                    }
                    .testTag("tx_item_${tx.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (tx.status == PaymentStatus.COMPLETED) MpesaGreen.copy(alpha = 0.15f) else LightGold
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (tx.status == PaymentStatus.COMPLETED) Icons.Default.CheckCircle else Icons.Default.Schedule,
                                contentDescription = null,
                                tint = if (tx.status == PaymentStatus.COMPLETED) MpesaGreen else GoldenAmber,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = tx.givingType.name.replace("_", " "),
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${tx.churchName} • ${tx.timestamp}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Code: ${tx.mpesaReceipt}",
                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                                color = RegalBlue
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "KES ${String.format("%,.0f", tx.amountKes)}",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = if (tx.status == PaymentStatus.COMPLETED) MpesaGreen else GoldenAmber
                            )
                        )
                        Surface(
                            color = if (tx.status == PaymentStatus.COMPLETED) MpesaLight else LightGold,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (tx.status == PaymentStatus.COMPLETED) "CONFIRMED" else "QUEUED",
                                color = if (tx.status == PaymentStatus.COMPLETED) MpesaGreen else Color(0xFF78350F),
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
}

@Composable
fun GivingCategoryChip(
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
