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
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.ui.KanisaniViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    viewModel: KanisaniViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val denominations by viewModel.denominations.collectAsState()
    val churches by viewModel.churches.collectAsState()
    val members by viewModel.members.collectAsState()
    val transactions by viewModel.transactions.collectAsState()

    var adminTab by remember { mutableStateOf(0) } // 0: Financials & Analytics, 1: Member Directory, 2: Denominations & Churches
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var showAddChurchDialog by remember { mutableStateOf(false) }
    var showAddDenomDialog by remember { mutableStateOf(false) }
    var memberSearchQuery by remember { mutableStateOf("") }

    val totalRevenue = transactions.filter { it.status == PaymentStatus.COMPLETED }.sumOf { it.amountKes }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("admin_panel_scroll"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {
        // Role Permission Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_role_badge_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(currentUser.role.badgeColorHex).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (currentUser.role) {
                                UserRole.SUPER_ADMIN -> Icons.Default.AdminPanelSettings
                                UserRole.DENOMINATION_ADMIN -> Icons.Default.AccountBalance
                                UserRole.CHURCH_ADMIN -> Icons.Default.Shield
                                UserRole.LEADER -> Icons.Default.SupervisedUserCircle
                                UserRole.MEMBER -> Icons.Default.Person
                            },
                            contentDescription = null,
                            tint = Color(currentUser.role.badgeColorHex),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = currentUser.role.label,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = when (currentUser.role) {
                                UserRole.SUPER_ADMIN -> "God Mode: Global multi-denomination governance & audit"
                                UserRole.DENOMINATION_ADMIN -> "Denomination oversight, doctrine, and church audits"
                                UserRole.CHURCH_ADMIN -> "Parish leadership, membership rolls, and services"
                                UserRole.LEADER -> "Ministry leader dashboard and attendance tracking"
                                UserRole.MEMBER -> "Member view (Switch roles via top bar to manage church)"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Tab Row
        item {
            Spacer(modifier = Modifier.height(14.dp))
            TabRow(
                selectedTabIndex = adminTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = RegalBlue
            ) {
                Tab(
                    selected = adminTab == 0,
                    onClick = { adminTab = 0 },
                    text = { Text("Financials", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Insights, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("admin_tab_financials")
                )
                Tab(
                    selected = adminTab == 1,
                    onClick = { adminTab = 1 },
                    text = { Text("Members", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("admin_tab_members")
                )
                Tab(
                    selected = adminTab == 2,
                    onClick = { adminTab = 2 },
                    text = { Text("Churches", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Church, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("admin_tab_churches")
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        when (adminTab) {
            0 -> {
                // Analytics & Financial Audit
                item {
                    Text(
                        text = "Global Giving & Audit Overview",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Total Cleared", style = MaterialTheme.typography.labelSmall)
                                Text(
                                    "KES ${String.format("%,.0f", totalRevenue)}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = MpesaGreen
                                    )
                                )
                                Text("M-Pesa STK verified", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Total Congregants", style = MaterialTheme.typography.labelSmall)
                                Text(
                                    "${members.size} Enrolled",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = RegalBlue
                                    )
                                )
                                Text("Across ${churches.size} branches", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Branch Collection Performance",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(churches) { ch ->
                    val branchTotal = transactions
                        .filter { it.churchId == ch.id && it.status == PaymentStatus.COMPLETED }
                        .sumOf { it.amountKes }

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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(ch.name, fontWeight = FontWeight.Bold)
                                Text("Paybill: ${ch.paybillNumber} • ${ch.branch}", style = MaterialTheme.typography.bodySmall)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "KES ${String.format("%,.0f", branchTotal)}",
                                    fontWeight = FontWeight.Bold,
                                    color = MpesaGreen
                                )
                                Text("${ch.memberCount} members", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            1 -> {
                // Member Directory
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Member Roll (${members.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Button(
                            onClick = { showAddMemberDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = RegalBlue),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("admin_register_member_button")
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Register Member", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = memberSearchQuery,
                        onValueChange = { memberSearchQuery = it },
                        placeholder = { Text("Search name, phone, or member #...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("member_search_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                val filteredMembers = members.filter { mem ->
                    memberSearchQuery.isBlank() ||
                            mem.name.contains(memberSearchQuery, ignoreCase = true) ||
                            mem.phone.contains(memberSearchQuery, ignoreCase = true) ||
                            mem.membershipNumber.contains(memberSearchQuery, ignoreCase = true)
                }

                items(filteredMembers) { mem ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("member_item_${mem.id}"),
                        shape = RoundedCornerShape(12.dp),
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
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(mem.role.badgeColorHex).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = Color(mem.role.badgeColorHex)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(mem.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                    Text("${mem.phone} • ${mem.churchName}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("ID: ${mem.membershipNumber} • ${mem.baptismStatus}", style = MaterialTheme.typography.labelSmall, color = GoldenAmber)
                                }
                            }

                            Surface(
                                color = Color(mem.role.badgeColorHex).copy(alpha = 0.12f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = mem.role.name.replace("_", " "),
                                    color = Color(mem.role.badgeColorHex),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            2 -> {
                // Denominations & Churches Management
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Denominations Ecosystem",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Button(
                            onClick = { showAddDenomDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("add_denomination_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Denom", fontSize = 11.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(denominations) { den ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(den.name, fontWeight = FontWeight.Bold)
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        den.code,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(den.doctrineSummary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Churches & Branches (${churches.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Button(
                            onClick = { showAddChurchDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = RegalBlue),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("add_church_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Church", fontSize = 11.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(churches) { ch ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(ch.name, fontWeight = FontWeight.Bold)
                                Text("Paybill: ${ch.paybillNumber}", fontWeight = FontWeight.Bold, color = MpesaGreen)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${ch.branch} • ${ch.city} • ${ch.address}", style = MaterialTheme.typography.bodySmall)
                            Text("Phone: ${ch.phone}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }

    if (showAddMemberDialog) {
        AddMemberDialog(
            churches = churches,
            onDismiss = { showAddMemberDialog = false },
            onAdd = { name, phone, email, gender, chId, baptism, role ->
                viewModel.registerMember(name, phone, email, gender, chId, baptism, role)
                showAddMemberDialog = false
            }
        )
    }

    if (showAddChurchDialog) {
        AddChurchDialog(
            denominations = denominations,
            onDismiss = { showAddChurchDialog = false },
            onAdd = { denomId, name, branch, city, address, phone, paybill ->
                viewModel.addChurch(denomId, name, branch, city, address, phone, paybill)
                showAddChurchDialog = false
            }
        )
    }

    if (showAddDenomDialog) {
        AddDenominationDialog(
            onDismiss = { showAddDenomDialog = false },
            onAdd = { name, code, doctrine ->
                viewModel.addDenomination(name, code, doctrine)
                showAddDenomDialog = false
            }
        )
    }
}

@Composable
fun AddMemberDialog(
    churches: List<Church>,
    onDismiss: () -> Unit,
    onAdd: (name: String, phone: String, email: String, gender: String, churchId: String, baptism: String, role: UserRole) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+254 7") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Female") }
    var baptism by remember { mutableStateOf("Baptized by Immersion") }
    var selectedChurchId by remember { mutableStateOf(churches.firstOrNull()?.id ?: "") }
    var selectedRole by remember { mutableStateOf(UserRole.MEMBER) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp).testTag("add_member_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Register Church Member", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email Address") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onAdd(name.ifBlank { "New Member" }, phone, email, gender, selectedChurchId, baptism, selectedRole)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RegalBlue)
                    ) {
                        Text("Register")
                    }
                }
            }
        }
    }
}

@Composable
fun AddChurchDialog(
    denominations: List<Denomination>,
    onDismiss: () -> Unit,
    onAdd: (denomId: String, name: String, branch: String, city: String, address: String, phone: String, paybill: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Nairobi") }
    var paybill by remember { mutableStateOf("554433") }
    var denomId by remember { mutableStateOf(denominations.firstOrNull()?.id ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp).testTag("add_church_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Add New Church / Branch", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Church Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = branch, onValueChange = { branch = it }, label = { Text("Branch / Parish") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = paybill, onValueChange = { paybill = it }, label = { Text("M-Pesa Paybill Number") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onAdd(denomId, name.ifBlank { "New Sanctuary" }, branch.ifBlank { "Central" }, city, "Main Road", "+254 700 000 000", paybill)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RegalBlue)
                    ) {
                        Text("Save Church")
                    }
                }
            }
        }
    }
}

@Composable
fun AddDenominationDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, code: String, doctrine: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var doctrine by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp).testTag("add_denom_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Create Denomination", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Denomination Name (e.g. Baptist, Lutheran)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Code (e.g. BCK)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = doctrine, onValueChange = { doctrine = it }, label = { Text("Doctrine / Liturgical Notes") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onAdd(name.ifBlank { "Independent Fellowship" }, code.ifBlank { "IND" }, doctrine.ifBlank { "Apostolic doctrine" })
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        Text("Save Denomination")
                    }
                }
            }
        }
    }
}
