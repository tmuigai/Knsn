package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Hymn
import com.example.data.model.Sermon
import com.example.ui.theme.*

@Composable
fun HymnDetailDialog(
    hymn: Hymn,
    onToggleFavorite: () -> Unit,
    onDismiss: () -> Unit
) {
    var languageSwahili by remember { mutableStateOf(false) }
    var fontScale by remember { mutableStateOf(16f) }
    var isPlayingAudio by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(6.dp)
                .testTag("hymn_detail_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Action Bar
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = RegalBlue,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "#${hymn.number}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                SuggestionChip(
                                    onClick = {},
                                    label = { Text("Key: ${hymn.keySignature}", fontSize = 11.sp) }
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = onToggleFavorite,
                                    modifier = Modifier.testTag("hymn_favorite_toggle")
                                ) {
                                    Icon(
                                        imageVector = if (hymn.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = if (hymn.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = onDismiss) {
                                    Icon(Icons.Default.Close, contentDescription = "Close")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (languageSwahili) hymn.titleSw else hymn.titleEn,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (languageSwahili) hymn.titleEn else hymn.titleSw,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Controls: Language toggle & Font size buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Language Tabs
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(2.dp)
                            ) {
                                TextButton(
                                    onClick = { languageSwahili = false },
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = if (!languageSwahili) RegalBlue else Color.Transparent,
                                        contentColor = if (!languageSwahili) Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("English", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                TextButton(
                                    onClick = { languageSwahili = true },
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = if (languageSwahili) RegalBlue else Color.Transparent,
                                        contentColor = if (languageSwahili) Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Kiswahili", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Font size
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { if (fontScale > 12f) fontScale -= 2f },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text("A-", fontWeight = FontWeight.Bold)
                                }
                                IconButton(
                                    onClick = { if (fontScale < 26f) fontScale += 2f },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text("A+", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Audio Playback Bar
                Surface(
                    color = LightGold,
                    modifier = Modifier.fillMaxWidth().testTag("hymn_audio_bar")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { isPlayingAudio = !isPlayingAudio },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlayingAudio) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                    contentDescription = "Play Hymn Tune",
                                    tint = GoldenAmber
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isPlayingAudio) "Playing Sanctuary Organ Tune..." else "Audio Organ Accompaniment",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = Color(0xFF78350F)
                            )
                        }
                        Text(
                            text = if (isPlayingAudio) "01:24 / 03:40" else "03:40",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF78350F)
                        )
                    }
                }

                // Verses Scroll Area
                val verses = if (languageSwahili) hymn.versesSw else hymn.versesEn
                val chorus = if (languageSwahili) hymn.chorusSw else hymn.chorusEn

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    verses.forEachIndexed { index, verse ->
                        Text(
                            text = "Verse ${index + 1}",
                            fontWeight = FontWeight.Bold,
                            color = RegalBlue,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = verse,
                            fontSize = fontScale.sp,
                            lineHeight = (fontScale * 1.45f).sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        if (chorus != null && index == 0) {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Chorus (Kiitikio)",
                                        fontWeight = FontWeight.Bold,
                                        color = GoldenAmber,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = chorus,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        fontSize = fontScale.sp,
                                        lineHeight = (fontScale * 1.45f).sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SermonDetailDialog(
    sermon: Sermon,
    onToggleDownload: () -> Unit,
    onDismiss: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(6.dp)
                .testTag("sermon_detail_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = RegalBlue.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = sermon.series,
                            color = RegalBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = sermon.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${sermon.preacher} • ${sermon.date}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Scripture reference card
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = GoldenAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scripture: ${sermon.scripture}",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Player Box
                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "Audio Sermon Stream",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    "${sermon.durationMinutes} minutes • High Quality AAC",
                                    color = Color.LightGray,
                                    fontSize = 11.sp
                                )
                            }
                            IconButton(
                                onClick = onToggleDownload,
                                modifier = Modifier.testTag("sermon_download_toggle")
                            ) {
                                Icon(
                                    imageVector = if (sermon.isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
                                    contentDescription = "Download",
                                    tint = if (sermon.isDownloaded) MpesaGreen else Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { if (isPlaying) 0.35f else 0.0f },
                            modifier = Modifier.fillMaxWidth(),
                            color = WarmGold,
                            trackColor = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                if (isPlaying) "16:45" else "00:00",
                                color = Color.LightGray,
                                fontSize = 11.sp
                            )
                            IconButton(
                                onClick = { isPlaying = !isPlaying },
                                modifier = Modifier.size(44.dp).testTag("play_sermon_button")
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.PauseCircleFilled else Icons.Default.PlayCircleFilled,
                                    contentDescription = "Play/Pause",
                                    tint = WarmGold,
                                    modifier = Modifier.size(44.dp)
                                )
                            }
                            Text(
                                "${sermon.durationMinutes}:00",
                                color = Color.LightGray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Sermon Outline & Notes",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = sermon.notesSummary,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
