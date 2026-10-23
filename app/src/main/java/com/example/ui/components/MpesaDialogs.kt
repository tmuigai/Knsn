package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MpesaStkDialog(
    churches: List<Church>,
    selectedChurch: Church,
    defaultPhone: String,
    isOffline: Boolean,
    onConfirmPayment: (amount: Double, type: GivingType, phone: String, church: Church) -> Unit,
    onDismiss: () -> Unit
) {
    var church by remember { mutableStateOf(selectedChurch) }
    var selectedType by remember { mutableStateOf(GivingType.OFFERING) }
    var amountText by remember { mutableStateOf("1000") }
    var phoneText by remember { mutableStateOf(defaultPhone) }
    var showSimToolkitPinPrompt by remember { mutableStateOf(false) }
    var simPin by remember { mutableStateOf("") }
    var churchDropdownExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("mpesa_giving_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MpesaGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = "M-Pesa",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "M-Pesa Giving Portal",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Daraja STK Push & Low-Data Queue",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Offline Notice if enabled
                if (isOffline) {
                    Surface(
                        color = LightGold,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = GoldenAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Offline Mode: Giving request will be queued locally and processed via STK Push once reconnected.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF78350F)
                            )
                        }
                    }
                }

                // Church Selector
                Text(
                    text = "Select Church & Paybill",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = churchDropdownExpanded,
                    onExpandedChange = { churchDropdownExpanded = !churchDropdownExpanded },
                    modifier = Modifier.fillMaxWidth().testTag("church_selector_dropdown")
                ) {
                    OutlinedTextField(
                        value = "${church.name} (Paybill: ${church.paybillNumber})",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = churchDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = churchDropdownExpanded,
                        onDismissRequest = { churchDropdownExpanded = false }
                    ) {
                        churches.forEach { ch ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(ch.name, fontWeight = FontWeight.Bold)
                                        Text("Paybill ${ch.paybillNumber} • ${ch.branch}", style = MaterialTheme.typography.bodySmall)
                                    }
                                },
                                onClick = {
                                    church = ch
                                    churchDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Giving Type
                Text(
                    text = "Giving Category",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(GivingType.OFFERING, GivingType.TITHE, GivingType.SEED).forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type.name.replace("_", " "), fontSize = 11.sp) },
                            modifier = Modifier.weight(1f).testTag("giving_type_${type.name}")
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(GivingType.BUILDING_FUND, GivingType.SPECIAL_THANKSGIVING).forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type.name.replace("_", " "), fontSize = 11.sp) },
                            modifier = Modifier.weight(1f).testTag("giving_type_${type.name}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Amount Chips
                Text(
                    text = "Amount (KES)",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("200", "500", "1000", "2500", "5000").forEach { preset ->
                        OutlinedButton(
                            onClick = { amountText = preset },
                            modifier = Modifier.weight(1f).padding(0.dp),
                            contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = if (amountText == preset) ButtonDefaults.outlinedButtonColors(
                                containerColor = LightGold
                            ) else ButtonDefaults.outlinedButtonColors()
                        ) {
                            Text(preset, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { char -> char.isDigit() } },
                    label = { Text("Custom Amount (KES)") },
                    leadingIcon = { Text("KES", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("amount_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // M-Pesa Phone Number
                OutlinedTextField(
                    value = phoneText,
                    onValueChange = { phoneText = it },
                    label = { Text("M-Pesa Mobile Number") },
                    leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = MpesaGreen) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth().testTag("phone_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // SIM Toolkit Prompt inside dialog if online
                AnimatedVisibility(visible = showSimToolkitPinPrompt) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .testTag("sim_toolkit_pin_prompt"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "SIM TOOLKIT (M-PESA)",
                                color = MpesaGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Do you want to pay KSH ${amountText.ifEmpty { "0" }} to ${church.accountName} Paybill ${church.paybillNumber}?",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = simPin,
                                onValueChange = { if (it.length <= 4) simPin = it },
                                placeholder = { Text("Enter 4-Digit M-Pesa PIN", color = Color.LightGray) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    val amt = amountText.toDoubleOrNull() ?: 1000.0
                                    onConfirmPayment(amt, selectedType, phoneText, church)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MpesaGreen),
                                modifier = Modifier.fillMaxWidth().testTag("confirm_sim_pin_button")
                            ) {
                                Text("Send PIN & Authorize Payment", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                if (!showSimToolkitPinPrompt) {
                    Button(
                        onClick = {
                            if (isOffline) {
                                val amt = amountText.toDoubleOrNull() ?: 1000.0
                                onConfirmPayment(amt, selectedType, phoneText, church)
                            } else {
                                showSimToolkitPinPrompt = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isOffline) GoldenAmber else MpesaGreen
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("trigger_mpesa_stk_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = if (isOffline) Icons.Default.CloudQueue else Icons.Default.SendToMobile,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isOffline) "Queue Offline Giving" else "Send M-Pesa STK Push",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MpesaReceiptDialog(
    transaction: GivingTransaction,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("mpesa_receipt_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            if (transaction.status == PaymentStatus.COMPLETED) MpesaGreen else GoldenAmber
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (transaction.status == PaymentStatus.COMPLETED) Icons.Default.Check else Icons.Default.Schedule,
                        contentDescription = "Status",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (transaction.status == PaymentStatus.COMPLETED) "Payment Confirmed!" else "Queued in Offline Storage",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (transaction.status == PaymentStatus.COMPLETED)
                        "Official M-Pesa Church Receipt"
                    else
                        "Will automatically push to your SIM card when signal returns",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Receipt Ticket Box
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Transaction Code", style = MaterialTheme.typography.labelSmall)
                            Text(
                                text = transaction.mpesaReceipt,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = RegalBlue
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Giving Category", style = MaterialTheme.typography.labelSmall)
                            Text(
                                transaction.givingType.name.replace("_", " "),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Recipient Church", style = MaterialTheme.typography.labelSmall)
                            Text(
                                transaction.churchName,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Mobile Number", style = MaterialTheme.typography.labelSmall)
                            Text(
                                transaction.phone,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Date & Time", style = MaterialTheme.typography.labelSmall)
                            Text(
                                transaction.timestamp,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Total Given",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "KES ${String.format("%,.0f", transaction.amountKes)}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = MpesaGreen
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().testTag("close_receipt_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Done")
                }
            }
        }
    }
}
