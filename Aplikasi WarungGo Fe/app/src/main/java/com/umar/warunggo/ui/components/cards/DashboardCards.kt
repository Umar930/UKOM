package com.umar.warunggo.ui.components.cards

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.warunggo.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

/**
 * Summary KPI Card Component
 * Kartu untuk menampilkan metrik penting (Penjualan, Pengeluaran, Laba, dll)
 * 
 * Features:
 * - Format rupiah otomatis
 * - Change indicator (▲ +12% / ▼ -5%)
 * - Color coding (hijau = naik, merah = turun)
 * - Icon indicator
 * 
 * @param title Judul kartu (e.g., "Penjualan Hari Ini")
 * @param value Nilai nominal (dalam rupiah)
 * @param changePercent Persentase perubahan (positif = naik, negatif = turun)
 * @param icon Icon untuk kartu
 * @param iconColor Warna icon
 */
@Composable
fun SummaryKpiCard(
    title: String,
    value: Long,
    changePercent: Double? = null,
    icon: ImageVector = Icons.Filled.TrendingUp,
    iconColor: Color = PrimaryBlue,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: Icon + Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            // Value
            Text(
                text = formatRupiah(value),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            // Change Indicator (optional)
            changePercent?.let { percent ->
                ChangeIndicator(
                    changePercent = percent,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Change Indicator Component
 * Menampilkan perubahan persentase dengan ikon dan warna
 */
@Composable
fun ChangeIndicator(
    changePercent: Double,
    modifier: Modifier = Modifier
) {
    val isPositive = changePercent >= 0
    val color = if (isPositive) SuccessGreen else DangerRed
    val icon = if (isPositive) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown
    val arrow = if (isPositive) "▲" else "▼"
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = if (isPositive) "Naik" else "Turun",
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        
        Spacer(modifier = Modifier.width(4.dp))
        
        Text(
            text = "$arrow ${String.format("%.1f", kotlin.math.abs(changePercent))}% dibanding kemarin",
            fontSize = 12.sp,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Count Card Component
 * Untuk menampilkan jumlah/count (transaksi, produk, dll)
 */
@Composable
fun CountCard(
    title: String,
    count: Int,
    icon: ImageVector = Icons.Filled.Receipt,
    iconColor: Color = InfoBlue,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Text(
                text = count.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
    }
}

/**
 * Action Card Component
 * Untuk quick actions (Tambah Transaksi, Tambah Produk, dll)
 */
@Composable
fun ActionCard(
    title: String,
    icon: ImageVector,
    iconColor: Color = PrimaryBlue,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = iconColor.copy(alpha = 0.15f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                maxLines = 2
            )
        }
    }
}

/**
 * Helper function to format rupiah
 */
private fun formatRupiah(value: Long): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    return numberFormat.format(value).replace("Rp", "Rp ").replace(",00", "")
}

// Previews
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun SummaryKpiCardPreview() {
    WarungGoTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummaryKpiCard(
                title = "Penjualan Hari Ini",
                value = 2450000,
                changePercent = 12.5,
                icon = Icons.Filled.TrendingUp,
                iconColor = SuccessGreen
            )
            
            SummaryKpiCard(
                title = "Pengeluaran",
                value = 850000,
                changePercent = -5.2,
                icon = Icons.Filled.TrendingDown,
                iconColor = DangerRed
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun CountCardPreview() {
    WarungGoTheme {
        CountCard(
            title = "Jumlah Transaksi",
            count = 45,
            icon = Icons.Filled.Receipt,
            iconColor = InfoBlue,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ActionCardPreview() {
    WarungGoTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCard(
                title = "Tambah Transaksi",
                icon = Icons.Filled.Add,
                iconColor = PrimaryBlue,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            
            ActionCard(
                title = "Tambah Produk",
                icon = Icons.Filled.Inventory,
                iconColor = SuccessGreen,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}
