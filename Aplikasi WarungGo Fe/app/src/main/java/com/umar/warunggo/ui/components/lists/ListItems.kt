package com.umar.warunggo.ui.components.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.warunggo.ui.theme.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Transaction Item Component
 * Untuk menampilkan item transaksi di list
 */
@Composable
fun TransactionItem(
    productName: String,
    quantity: Int,
    totalPrice: Long,
    paymentMethod: String,
    timestamp: Long,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Product info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = productName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quantity
                    Text(
                        text = "${quantity}x",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    
                    // Separator
                    Text(
                        text = "•",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    
                    // Time
                    Text(
                        text = formatTime(timestamp),
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                
                // Payment method badge
                PaymentMethodBadge(
                    method = paymentMethod,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Right: Total price
            Text(
                text = formatRupiah(totalPrice),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SuccessGreen
            )
        }
    }
}

/**
 * Payment Method Badge Component
 */
@Composable
fun PaymentMethodBadge(
    method: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (method.uppercase()) {
        "QRIS" -> PrimaryBlue.copy(alpha = 0.2f) to PrimaryBlue
        "CASH" -> SuccessGreen.copy(alpha = 0.2f) to SuccessGreen
        "E-WALLET" -> Color(0xFF9C27B0).copy(alpha = 0.2f) to Color(0xFF9C27B0)
        "TRANSFER" -> InfoBlue.copy(alpha = 0.2f) to InfoBlue
        else -> TextSecondary.copy(alpha = 0.2f) to TextSecondary
    }
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor
    ) {
        Text(
            text = method.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

/**
 * Low Stock Item Component
 * Untuk menampilkan produk dengan stok menipis
 */
@Composable
fun LowStockItem(
    productName: String,
    currentStock: Int,
    minStock: Int,
    onRestockClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Product info with warning
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Warning icon
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = WarningYellow.copy(alpha = 0.2f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Warning",
                            tint = WarningYellow,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                // Product details
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = productName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Text(
                        text = "Stok: $currentStock (Min: $minStock)",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Right: Restock button
            TextButton(
                onClick = onRestockClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = PrimaryBlue
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Restock",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Section Header Component
 */
@Composable
fun SectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        if (actionText != null && onActionClick != null) {
            TextButton(onClick = onActionClick) {
                Text(
                    text = actionText,
                    fontSize = 13.sp,
                    color = PrimaryBlue
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// Helper functions
private fun formatRupiah(value: Long): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    return numberFormat.format(value).replace("Rp", "Rp ").replace(",00", "")
}

private fun formatTime(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale("in", "ID"))
    return dateFormat.format(Date(timestamp))
}

// Previews
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun TransactionItemPreview() {
    WarungGoTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TransactionItem(
                productName = "Indomie Goreng Original",
                quantity = 5,
                totalPrice = 15000,
                paymentMethod = "QRIS",
                timestamp = System.currentTimeMillis()
            )
            
            TransactionItem(
                productName = "Aqua Botol 600ml",
                quantity = 3,
                totalPrice = 9000,
                paymentMethod = "CASH",
                timestamp = System.currentTimeMillis() - 3600000
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun LowStockItemPreview() {
    WarungGoTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LowStockItem(
                productName = "Indomie Goreng Original",
                currentStock = 5,
                minStock = 10,
                onRestockClick = {}
            )
            
            LowStockItem(
                productName = "Aqua Botol 600ml (nama panjang sekali untuk testing)",
                currentStock = 2,
                minStock = 15,
                onRestockClick = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun SectionHeaderPreview() {
    WarungGoTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader(
                title = "Transaksi Terakhir",
                actionText = "Lihat Semua",
                onActionClick = {}
            )
            
            SectionHeader(
                title = "Stok Menipis",
                actionText = null,
                onActionClick = null
            )
        }
    }
}
