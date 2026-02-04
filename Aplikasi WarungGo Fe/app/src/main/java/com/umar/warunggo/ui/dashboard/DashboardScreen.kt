package com.umar.warunggo.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.warunggo.ui.components.cards.*
import com.umar.warunggo.ui.components.charts.MiniSalesChart
import com.umar.warunggo.ui.components.header.PageHeader
import com.umar.warunggo.ui.components.lists.*
import com.umar.warunggo.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Dashboard Screen - Modern Dark Mode
 * Halaman utama dengan ringkasan bisnis, grafik tren, aksi cepat,
 * transaksi terakhir, dan peringatan stok menipis
 */
@Composable
fun DashboardScreen(
    onNavigateToTransaction: () -> Unit = {},
    onNavigateToProduct: () -> Unit = {},
    onNavigateToReport: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Dummy data
    val dashboardData = remember { getDummyDashboardData() }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header Global dengan tanggal real-time
        PageHeader(pageName = "Dashboard")
        
        // Content dengan scroll
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp), // Space for bottom nav
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. RINGKASAN HARI INI - 4 KPI Cards
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Ringkasan Hari Ini",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    
                    // Grid 2x2
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SummaryKpiCard(
                                title = "Penjualan",
                                value = dashboardData.totalSales,
                                changePercent = dashboardData.salesChangePercent,
                                icon = Icons.Filled.TrendingUp,
                                iconColor = SuccessGreen,
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            SummaryKpiCard(
                                title = "Laba Bersih",
                                value = dashboardData.netProfit,
                                changePercent = null,
                                icon = Icons.Filled.Savings,
                                iconColor = InfoBlue,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SummaryKpiCard(
                                title = "Pengeluaran",
                                value = dashboardData.totalExpenses,
                                changePercent = dashboardData.expenseChangePercent,
                                icon = Icons.Filled.Receipt,
                                iconColor = DangerRed,
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            CountCard(
                                title = "Transaksi",
                                count = dashboardData.transactionCount,
                                icon = Icons.Filled.ShoppingCart,
                                iconColor = PrimaryBlue,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            
            // 2. GRAFIK MINI PENJUALAN - 7 Hari Terakhir
            item {
                MiniSalesChart(
                    data = dashboardData.salesTrendData,
                    labels = dashboardData.salesTrendLabels,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            
            // 3. AKSI CEPAT - Quick Actions
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Aksi Cepat",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ActionCard(
                            title = "Tambah Transaksi",
                            icon = Icons.Filled.Add,
                            iconColor = PrimaryBlue,
                            onClick = onNavigateToTransaction,
                            modifier = Modifier.weight(1f)
                        )
                        
                        ActionCard(
                            title = "Tambah Produk",
                            icon = Icons.Filled.Inventory,
                            iconColor = SuccessGreen,
                            onClick = onNavigateToProduct,
                            modifier = Modifier.weight(1f)
                        )
                        
                        ActionCard(
                            title = "Scan Barcode",
                            icon = Icons.Filled.QrCodeScanner,
                            iconColor = WarningYellow,
                            onClick = { /* TODO: Open camera scanner */ },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // 4. TRANSAKSI TERAKHIR
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SectionHeader(
                        title = "Transaksi Terakhir",
                        actionText = "Lihat Semua",
                        onActionClick = onNavigateToReport
                    )
                    
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        dashboardData.recentTransactions.take(5).forEach { transaction ->
                            TransactionItem(
                                productName = transaction.productName,
                                quantity = transaction.quantity,
                                totalPrice = transaction.totalPrice,
                                paymentMethod = transaction.paymentMethod,
                                timestamp = transaction.timestamp,
                                onClick = { /* TODO: Navigate to transaction detail */ }
                            )
                        }
                    }
                }
            }
            
            // 5. STOK MENIPIS - Warning Section
            if (dashboardData.lowStockItems.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Warning Header Card
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = WarningYellow.copy(alpha = 0.15f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "⚠️ Stok Menipis",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarningYellow
                                )
                                
                                TextButton(onClick = onNavigateToProduct) {
                                    Text(
                                        text = "Lihat Semua Stok",
                                        fontSize = 13.sp,
                                        color = WarningYellow
                                    )
                                }
                            }
                        }
                        
                        // Low Stock Items
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            dashboardData.lowStockItems.take(3).forEach { item ->
                                LowStockItem(
                                    productName = item.productName,
                                    currentStock = item.currentStock,
                                    minStock = item.minStock,
                                    onRestockClick = { /* TODO: Navigate to restock */ }
                                )
                            }
                        }
                    }
                }
            }
            
            // 6. STATUS TOKO (Optional Premium Feature)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardDark
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Status Toko",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Atur jam operasional toko Anda",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                        
                        var isOpen by remember { mutableStateOf(true) }
                        
                        Switch(
                            checked = isOpen,
                            onCheckedChange = { isOpen = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SuccessGreen,
                                checkedTrackColor = SuccessGreen.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }
        }
    }
}

// =====================================================
// DATA MODELS
// =====================================================

data class DashboardData(
    val totalSales: Long,
    val salesChangePercent: Double,
    val totalExpenses: Long,
    val expenseChangePercent: Double,
    val netProfit: Long,
    val transactionCount: Int,
    val salesTrendData: List<Long>,
    val salesTrendLabels: List<String>,
    val recentTransactions: List<RecentTransactionData>,
    val lowStockItems: List<LowStockData>
)

data class RecentTransactionData(
    val productName: String,
    val quantity: Int,
    val totalPrice: Long,
    val paymentMethod: String,
    val timestamp: Long
)

data class LowStockData(
    val productName: String,
    val currentStock: Int,
    val minStock: Int
)

// =====================================================
// DUMMY DATA GENERATOR
// =====================================================

fun getDummyDashboardData(): DashboardData {
    val currentTime = System.currentTimeMillis()
    
    return DashboardData(
        totalSales = 2_450_000L,
        salesChangePercent = 12.5,
        totalExpenses = 850_000L,
        expenseChangePercent = -5.2,
        netProfit = 1_600_000L,
        transactionCount = 45,
        salesTrendData = listOf(
            1_200_000L, 1_500_000L, 1_800_000L, 2_100_000L, 
            1_900_000L, 2_300_000L, 2_500_000L
        ),
        salesTrendLabels = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min"),
        recentTransactions = listOf(
            RecentTransactionData(
                productName = "Indomie Goreng Original",
                quantity = 5,
                totalPrice = 15_000L,
                paymentMethod = "QRIS",
                timestamp = currentTime - 600_000 // 10 min ago
            ),
            RecentTransactionData(
                productName = "Aqua Botol 600ml",
                quantity = 3,
                totalPrice = 9_000L,
                paymentMethod = "CASH",
                timestamp = currentTime - 1_800_000 // 30 min ago
            ),
            RecentTransactionData(
                productName = "Teh Pucuk Harum",
                quantity = 2,
                totalPrice = 8_000L,
                paymentMethod = "E-Wallet",
                timestamp = currentTime - 3_600_000 // 1 hour ago
            ),
            RecentTransactionData(
                productName = "Sabun Lifebuoy",
                quantity = 1,
                totalPrice = 5_500L,
                paymentMethod = "CASH",
                timestamp = currentTime - 7_200_000 // 2 hours ago
            ),
            RecentTransactionData(
                productName = "Mie Sedaap Goreng",
                quantity = 10,
                totalPrice = 30_000L,
                paymentMethod = "Transfer",
                timestamp = currentTime - 10_800_000 // 3 hours ago
            )
        ),
        lowStockItems = listOf(
            LowStockData(
                productName = "Indomie Goreng Original",
                currentStock = 5,
                minStock = 20
            ),
            LowStockData(
                productName = "Aqua Botol 600ml",
                currentStock = 8,
                minStock = 30
            ),
            LowStockData(
                productName = "Teh Pucuk Harum",
                currentStock = 3,
                minStock = 15
            )
        )
    )
}

// =====================================================
// PREVIEW
// =====================================================

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF121212)
@Composable
private fun DashboardScreenPreview() {
    WarungGoTheme {
        DashboardScreen()
    }
}
