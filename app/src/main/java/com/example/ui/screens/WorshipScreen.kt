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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.KanisaniViewModel
import com.example.ui.theme.*

@Composable
fun WorshipScreen(
    viewModel: KanisaniViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Hymns, 1: Prayer Book, 2: Sermons
    var hymnSearchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var showFavoritesOnly by remember { mutableStateOf(false) }

    val hymns by viewModel.hymns.collectAsState()
    val prayers by viewModel.prayers.collectAsState()
    val sermons by viewModel.sermons.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("worship_screen")
    ) {
        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = RegalBlue
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Hymns (Nyimbo)", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                icon = { Icon(Icons.Default.LibraryMusic, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_hymns")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Prayer Book (Sala)", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                icon = { Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_prayers")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Sermons", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                icon = { Icon(Icons.Default.Headphones, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_sermons")
            )
        }

        when (selectedTab) {
            0 -> {
                // Hymns Content
                Column(modifier = Modifier.fillMaxSize()) {
                    // Search & Filters
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                            OutlinedTextField(
                                value = hymnSearchQuery,
                                onValueChange = { hymnSearchQuery = it },
                                placeholder = { Text("Search number, title, or Swahili lyrics...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                trailingIcon = {
                                    if (hymnSearchQuery.isNotEmpty()) {
                                        IconButton(onClick = { hymnSearchQuery = "" }) {
                                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("hymn_search_input")
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilterChip(
                                    selected = showFavoritesOnly,
                                    onClick = { showFavoritesOnly = !showFavoritesOnly },
                                    label = { Text("Favorites", fontSize = 11.sp) },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Favorite,
                                            contentDescription = null,
                                            tint = if (showFavoritesOnly) Color.Red else Color.Gray,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    },
                                    modifier = Modifier.testTag("filter_favorites")
                                )

                                listOf("All", "Praise & Grace", "Prayer & Fellowship", "Praise & Worship").forEach { cat ->
                                    FilterChip(
                                        selected = selectedCategory == cat && !showFavoritesOnly,
                                        onClick = {
                                            selectedCategory = cat
                                            showFavoritesOnly = false
                                        },
                                        label = { Text(cat, fontSize = 11.sp) }
                                    )
                                }
                            }
                        }
                    }

                    val filteredHymns = hymns.filter { hymn ->
                        val matchesSearch = hymnSearchQuery.isBlank() ||
                                hymn.number.toString().contains(hymnSearchQuery) ||
                                hymn.titleEn.contains(hymnSearchQuery, ignoreCase = true) ||
                                hymn.titleSw.contains(hymnSearchQuery, ignoreCase = true) ||
                                hymn.versesSw.any { it.contains(hymnSearchQuery, ignoreCase = true) } ||
                                hymn.versesEn.any { it.contains(hymnSearchQuery, ignoreCase = true) }

                        val matchesCategory = selectedCategory == "All" || hymn.category == selectedCategory
                        val matchesFavorite = !showFavoritesOnly || hymn.isFavorite

                        matchesSearch && matchesCategory && matchesFavorite
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        items(filteredHymns) { hymn ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { viewModel.selectHymn(hymn) }
                                    .testTag("hymn_item_${hymn.number}"),
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
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            color = RegalBlue.copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "#${hymn.number}",
                                                color = RegalBlue,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = hymn.titleSw,
                                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = hymn.titleEn,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "${hymn.category} • Key: ${hymn.keySignature}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = GoldenAmber
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { viewModel.toggleHymnFavorite(hymn.id) },
                                        modifier = Modifier.testTag("fav_button_${hymn.id}")
                                    ) {
                                        Icon(
                                            imageVector = if (hymn.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Favorite",
                                            tint = if (hymn.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Prayers Content
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    item {
                        Surface(
                            color = LightGold,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = GoldenAmber
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        "Digital Prayer Library (Fully Offline)",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF78350F)
                                    )
                                    Text(
                                        "Liturgy, Morning Collects, Rosary & Family Benedictions",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF78350F)
                                    )
                                }
                            }
                        }
                    }

                    items(prayers) { prayer ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .testTag("prayer_card_${prayer.id}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = GoldenAmber.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = prayer.category,
                                            color = GoldenAmber,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = prayer.scriptureRef,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = prayer.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = prayer.text,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
            }

            2 -> {
                // Sermons Content
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(sermons) { sermon ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable { viewModel.selectSermon(sermon) }
                                .testTag("sermon_item_${sermon.id}"),
                            shape = RoundedCornerShape(14.dp),
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
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(RegalBlue.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Headphones,
                                            contentDescription = null,
                                            tint = RegalBlue,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = sermon.title,
                                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "${sermon.preacher} • ${sermon.date}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "${sermon.durationMinutes} min • ${sermon.scripture}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GoldenAmber
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = { viewModel.toggleSermonDownload(sermon.id) },
                                    modifier = Modifier.testTag("download_sermon_${sermon.id}")
                                ) {
                                    Icon(
                                        imageVector = if (sermon.isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
                                        contentDescription = "Download Status",
                                        tint = if (sermon.isDownloaded) MpesaGreen else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
