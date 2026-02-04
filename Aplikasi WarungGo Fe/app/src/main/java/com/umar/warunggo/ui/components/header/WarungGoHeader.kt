package com.umar.warunggo.ui.components.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.warunggo.ui.theme.PrimaryBlue
import com.umar.warunggo.ui.theme.WarungGoTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

/**
 * Global Header Component - WarungGo
 * Reusable header untuk semua halaman dengan notifikasi dan profil
 * 
 * @param title Judul header (nama aplikasi/toko)
 * @param subtitle Optional subtitle (tanggal, info, dll)
 * @param onNotificationClick Callback untuk klik notifikasi
 * @param onProfileClick Callback untuk klik profil
 * @param notificationBadgeCount Jumlah notifikasi unread
 * @param contextContent Optional slot untuk konten kontekstual (search bar, filter, dll)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarungGoHeader(
    title: String,
    subtitle: String? = null,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    notificationBadgeCount: Int = 0,
    modifier: Modifier = Modifier,
    contextContent: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp)
    ) {
        // Global Header Section
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PrimaryBlue
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Title & Subtitle
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    subtitle?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
                
                // Right: Actions (Notification & Profile)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Notification Button with Badge
                    BadgedBox(
                        badge = {
                            if (notificationBadgeCount > 0) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.offset(x = (-4).dp, y = 4.dp)
                                ) {
                                    Text(
                                        text = if (notificationBadgeCount > 99) "99+" else notificationBadgeCount.toString(),
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = onNotificationClick) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Notifikasi",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    
                    // Profile Button
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "Profil",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
        
        // Context Header Section (Dynamic per page)
        contextContent?.let {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    it()
                }
            }
        }
    }
}

/**
 * Simple Header variant tanpa context content
 */
@Composable
fun SimpleWarungGoHeader(
    title: String,
    subtitle: String? = null,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    notificationBadgeCount: Int = 0,
    modifier: Modifier = Modifier
) {
    WarungGoHeader(
        title = title,
        subtitle = subtitle,
        onNotificationClick = onNotificationClick,
        onProfileClick = onProfileClick,
        notificationBadgeCount = notificationBadgeCount,
        contextContent = null,
        modifier = modifier
    )
}

/**
 * Page Header untuk halaman lain (Produk, Transaksi, Laporan, Profil)
 * Menampilkan "WarungGo [Nama Halaman]" dengan tanggal real-time
 * 
 * @param pageName Nama halaman (Produk, Transaksi, Laporan, Profil)
 * @param showActions Tampilkan ikon notifikasi dan profil (default: true)
 * @param onNotificationClick Callback untuk klik notifikasi
 * @param onProfileClick Callback untuk klik profil
 * @param notificationBadgeCount Jumlah notifikasi unread
 */
@Composable
fun PageHeader(
    pageName: String,
    showActions: Boolean = true,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    notificationBadgeCount: Int = 0,
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("in", "ID")) }
    val currentDate = remember {
        mutableStateOf(dateFormat.format(Date()))
    }
    
    // Update tanggal setiap detik (real-time)
    LaunchedEffect(Unit) {
        while (true) {
            currentDate.value = dateFormat.format(Date())
            delay(1000L) // Update every second
        }
    }
    
    if (showActions) {
        SimpleWarungGoHeader(
            title = "WarungGo $pageName",
            subtitle = currentDate.value,
            onNotificationClick = onNotificationClick,
            onProfileClick = onProfileClick,
            notificationBadgeCount = notificationBadgeCount,
            modifier = modifier
        )
    } else {
        // Simple header tanpa ikon (untuk halaman Profil)
        SimplePageHeader(
            title = "WarungGo $pageName",
            subtitle = currentDate.value,
            modifier = modifier
        )
    }
}

/**
 * Simple Page Header - Hanya judul dan tanggal, tanpa ikon
 */
@Composable
fun SimplePageHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp),
        color = PrimaryBlue
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WarungGoHeaderPreview() {
    WarungGoTheme {
        WarungGoHeader(
            title = "WarungGo",
            subtitle = "Rabu, 04 Februari 2026",
            notificationBadgeCount = 5,
            contextContent = {
                // Example: Search bar context
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Cari produk...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SimpleWarungGoHeaderPreview() {
    WarungGoTheme {
        SimpleWarungGoHeader(
            title = "WarungGo",
            subtitle = "Rabu, 04 Februari 2026",
            notificationBadgeCount = 3
        )
    }
}
