package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanisaniTopBar(
    currentRole: UserRole,
    isOffline: Boolean,
    pendingSyncCount: Int,
    fontSizeScale: Float,
    onCycleFontSize: () -> Unit,
    onToggleOffline: () -> Unit,
    onOpenRoleSwitcher: () -> Unit,
    onSyncClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth().testTag("top_app_bar")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // App Brand
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("app_brand_logo")
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SanctuaryNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Church,
                            contentDescription = "Kanisani Cross",
                            tint = CathedralGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "KANISANI",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.2.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Kanisa Lako Mkononi",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Controls: Font Size Scale, Offline Mode, Role Switcher
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Generational Text Size button (10 to 90 years old)
                    OutlinedButton(
                        onClick = onCycleFontSize,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("cycle_font_size_button")
                    ) {
                        Text(
                            text = when (fontSizeScale) {
                                1.25f -> "A+"
                                1.45f -> "A++"
                                else -> "A"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = CathedralGold
                        )
                    }

                    // Offline / Online pill
                    FilterChip(
                        selected = isOffline,
                        onClick = onToggleOffline,
                        label = {
                            Text(
                                text = if (isOffline) "OFFLINE" else "ONLINE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(if (isOffline) CathedralGold else SafaricomGreen)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LightCathedralGold,
                            selectedLabelColor = OchreGold
                        ),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("offline_mode_toggle")
                    )

                    // Role pill button
                    AssistChip(
                        onClick = onOpenRoleSwitcher,
                        label = {
                            Text(
                                text = when (currentRole) {
                                    UserRole.SUPER_ADMIN -> "God Mode"
                                    UserRole.DENOMINATION_ADMIN -> "Denom"
                                    UserRole.CHURCH_ADMIN -> "Pastor"
                                    UserRole.LEADER -> "Leader"
                                    UserRole.MEMBER -> "Member"
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = when (currentRole) {
                                    UserRole.SUPER_ADMIN -> Icons.Default.AdminPanelSettings
                                    UserRole.DENOMINATION_ADMIN -> Icons.Default.AccountBalance
                                    UserRole.CHURCH_ADMIN -> Icons.Default.Shield
                                    UserRole.LEADER -> Icons.Default.SupervisedUserCircle
                                    UserRole.MEMBER -> Icons.Default.Person
                                },
                                contentDescription = "Role icon",
                                tint = Color(currentRole.badgeColorHex),
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("role_switcher_button")
                    )
                }
            }


            // Sync Banner if items are queued offline
            if (pendingSyncCount > 0) {
                Surface(
                    color = LightGold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSyncClick() }
                        .testTag("pending_sync_banner")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Pending sync",
                                tint = GoldenAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$pendingSyncCount offline items queued for sync",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = Color(0xFF78350F)
                            )
                        }
                        Text(
                            text = "Sync Now",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = RegalBlue
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RoleSwitcherDialog(
    currentRole: UserRole,
    members: List<Member>,
    onSelectRole: (UserRole) -> Unit,
    onSelectUser: (Member) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("role_switcher_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Role & Access Hierarchy",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Switch active perspective to test Kanisani",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                UserRole.values().forEach { role ->
                    val isSelected = currentRole == role
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectRole(role) }
                            .testTag("role_option_${role.name}"),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        tonalElevation = if (isSelected) 2.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(role.badgeColorHex).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (role) {
                                        UserRole.SUPER_ADMIN -> Icons.Default.AdminPanelSettings
                                        UserRole.DENOMINATION_ADMIN -> Icons.Default.AccountBalance
                                        UserRole.CHURCH_ADMIN -> Icons.Default.Shield
                                        UserRole.LEADER -> Icons.Default.SupervisedUserCircle
                                        UserRole.MEMBER -> Icons.Default.Person
                                    },
                                    contentDescription = null,
                                    tint = Color(role.badgeColorHex),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = role.label,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = when (role) {
                                        UserRole.SUPER_ADMIN -> "Full control, all denominations & global audit"
                                        UserRole.DENOMINATION_ADMIN -> "Multi-church oversight, doctrine & reports"
                                        UserRole.CHURCH_ADMIN -> "Local church, members, services & finance"
                                        UserRole.LEADER -> "Ministry groups, check-in & member follow-up"
                                        UserRole.MEMBER -> "Giving, worship, sermon notes & groups"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
