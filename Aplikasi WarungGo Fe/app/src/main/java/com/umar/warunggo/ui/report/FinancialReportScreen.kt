package com.umar.warunggo.ui.report

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umar.warunggo.data.model.*
import com.umar.warunggo.ui.components.*
import com.umar.warunggo.ui.components.header.PageHeader

/**
 * Financial Report Screen
 * Complete financial dashboard with KPIs, charts, and insights
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialReportScreen(
    viewModel: FinancialReportViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Header dengan tanggal real-time (tanpa ikon notif/profil)
        PageHeader(pageName = "Laporan", showActions = false)
        
        // Action buttons row (Filter & Export)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.showFilterDialog() }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filter")
            }
            IconButton(onClick = { viewModel.showExportDialog() }) {
                Icon(Icons.Default.Share, contentDescription = "Export")
            }
        }
        
        // Content
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    LoadingState()
                }
                uiState.errorMessage != null -> {
                    ErrorState(
                        message = uiState.errorMessage!!,
                        onRetry = { viewModel.loadReport() }
                    )
                }
                else -> {
                    ReportContent(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }
            }
        }
        
        // Filter Dialog with Period Selector
        if (uiState.showFilterDialog) {
            FilterPeriodDialog(
                selectedPeriod = uiState.selectedPeriod,
                onPeriodSelected = { 
                    viewModel.selectPeriod(it)
                    viewModel.hideFilterDialog()
                },
                onCustomClick = { 
                    viewModel.hideFilterDialog()
                    viewModel.showDatePicker() 
                },
                onDismiss = { viewModel.hideFilterDialog() }
            )
        }
        
        // Export Dialog
        if (uiState.showExportDialog) {
            ExportDialog(
                onDismiss = { viewModel.hideExportDialog() },
                onExportPdf = { viewModel.exportToPdf() },
                onExportExcel = { viewModel.exportToExcel() },
                onShare = { viewModel.shareReport() }
            )
        }
        
        // Date Picker Dialog
        if (uiState.showDatePicker) {
            DateRangePickerDialog(
                onDismiss = { viewModel.hideDatePicker() },
                onConfirm = { startDate, endDate ->
                    viewModel.setCustomDateRange(DateRange(startDate, endDate))
                }
            )
        }
    } // End Column
}

/**
 * Main report content
 */
@Composable
private fun ReportContent(
    uiState: FinancialReportUiState,
    viewModel: FinancialReportViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Summary KPI Cards (langsung tanpa Period Selector)
        item {
            AnimatedVisibility(visible = uiState.summary != null) {
                SummaryKpiSection(summary = uiState.summary!!)
            }
        }
        
        // Chart Section
        item {
            AnimatedVisibility(visible = uiState.chartData.isNotEmpty()) {
                ChartSection(
                    chartData = uiState.chartData,
                    selectedType = uiState.selectedChartType,
                    onTypeSelected = { viewModel.selectChartType(it) }
                )
            }
        }
        
        // Expense Breakdown
        item {
            AnimatedVisibility(visible = uiState.expenseCategories.isNotEmpty()) {
                ExpenseBreakdownSection(categories = uiState.expenseCategories)
            }
        }
        
        // Best Sellers
        item {
            AnimatedVisibility(visible = uiState.bestSellerProducts.isNotEmpty()) {
                BestSellersSection(products = uiState.bestSellerProducts)
            }
        }
        
        // Transaction History
        item {
            AnimatedVisibility(visible = uiState.recentTransactions.isNotEmpty()) {
                TransactionHistorySection(transactions = uiState.recentTransactions)
            }
        }
    }
}

/**
 * Filter Period Dialog - Pilihan periode filter
 */
@Composable
private fun FilterPeriodDialog(
    selectedPeriod: ReportPeriod,
    onPeriodSelected: (ReportPeriod) -> Unit,
    onCustomClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                text = "Pilih Periode",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterPeriodOption(
                    label = "Hari Ini",
                    selected = selectedPeriod == ReportPeriod.TODAY,
                    onClick = { onPeriodSelected(ReportPeriod.TODAY) }
                )
                FilterPeriodOption(
                    label = "Mingguan",
                    selected = selectedPeriod == ReportPeriod.WEEKLY,
                    onClick = { onPeriodSelected(ReportPeriod.WEEKLY) }
                )
                FilterPeriodOption(
                    label = "Bulanan",
                    selected = selectedPeriod == ReportPeriod.MONTHLY,
                    onClick = { onPeriodSelected(ReportPeriod.MONTHLY) }
                )
                FilterPeriodOption(
                    label = "Custom",
                    selected = selectedPeriod == ReportPeriod.CUSTOM,
                    onClick = onCustomClick
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}

/**
 * Filter Period Option Item
 */
@Composable
private fun FilterPeriodOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer 
                else MaterialTheme.colorScheme.surface,
        tonalElevation = if (selected) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer 
                        else MaterialTheme.colorScheme.onSurface
            )
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Summary KPI Section
 */
@Composable
private fun SummaryKpiSection(summary: FinancialSummary) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Ringkasan",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KpiCard(
                title = "Total Pendapatan",
                value = summary.getFormattedRevenue(),
                changePercentage = summary.revenueChange,
                icon = {
                    Icon(
                        Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                accentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            
            KpiCard(
                title = "Total Pengeluaran",
                value = summary.getFormattedExpense(),
                changePercentage = summary.expenseChange,
                icon = {
                    Icon(
                        Icons.Default.TrendingDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                accentColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KpiCard(
                title = "Laba Bersih",
                value = summary.getFormattedProfit(),
                changePercentage = summary.profitChange,
                icon = {
                    Icon(
                        Icons.Default.Wallet,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                },
                accentColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
            
            KpiCard(
                title = "Total Transaksi",
                value = "${summary.totalTransactions}",
                changePercentage = summary.transactionChange,
                icon = {
                    Icon(
                        Icons.Default.Receipt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                },
                accentColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Chart Section
 */
@Composable
private fun ChartSection(
    chartData: List<ChartDataPoint>,
    selectedType: ChartType,
    onTypeSelected: (ChartType) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Grafik Keuangan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = selectedType == ChartType.REVENUE,
                    onClick = { onTypeSelected(ChartType.REVENUE) },
                    label = { Text("Pendapatan") }
                )
                FilterChip(
                    selected = selectedType == ChartType.EXPENSE,
                    onClick = { onTypeSelected(ChartType.EXPENSE) },
                    label = { Text("Pengeluaran") }
                )
                FilterChip(
                    selected = selectedType == ChartType.PROFIT,
                    onClick = { onTypeSelected(ChartType.PROFIT) },
                    label = { Text("Laba") }
                )
            }
        }
        
        FinancialChart(
            data = chartData,
            selectedType = selectedType
        )
    }
}

/**
 * Expense Breakdown Section
 */
@Composable
private fun ExpenseBreakdownSection(categories: List<ExpenseCategory>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Rincian Pengeluaran",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            categories.forEach { category ->
                ExpenseCategoryItem(
                    name = category.name,
                    amount = category.getFormattedAmount(),
                    percentage = category.percentage
                )
            }
        }
    }
}

/**
 * Best Sellers Section
 */
@Composable
private fun BestSellersSection(products: List<BestSellerProduct>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Produk Terlaris",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(products) { product ->
                BestSellerCard(product = product)
            }
        }
    }
}

/**
 * Transaction History Section
 */
@Composable
private fun TransactionHistorySection(transactions: List<TransactionItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transaksi Terakhir",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                TextButton(onClick = { /* Navigate to full list */ }) {
                    Text("Lihat Semua")
                }
            }
            
            transactions.forEach { transaction ->
                TransactionHistoryItem(
                    invoiceId = transaction.invoiceId,
                    date = transaction.getFormattedDate(),
                    amount = transaction.getFormattedAmount(),
                    status = transaction.status
                )
                
                if (transaction != transactions.last()) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

/**
 * Loading State
 */
@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Error State
 */
@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = onRetry) {
                Text("Coba Lagi")
            }
        }
    }
}

/**
 * Export Dialog
 */
@Composable
private fun ExportDialog(
    onDismiss: () -> Unit,
    onExportPdf: () -> Unit,
    onExportExcel: () -> Unit,
    onShare: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Export Laporan") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(
                    onClick = onExportPdf,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export ke PDF")
                    }
                }
                
                TextButton(
                    onClick = onExportExcel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.TableChart, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export ke Excel")
                    }
                }
                
                TextButton(
                    onClick = onShare,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Bagikan")
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}

/**
 * Date Range Picker Dialog (Placeholder)
 */
@Composable
private fun DateRangePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Long, Long) -> Unit
) {
    val currentTime = System.currentTimeMillis()
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pilih Periode") },
        text = {
            Column {
                Text("Custom date picker akan ditambahkan di sini")
                Text(
                    text = "Sementara menggunakan 7 hari terakhir",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val endDate = currentTime
                val startDate = currentTime - (7 * 24 * 60 * 60 * 1000)
                onConfirm(startDate, endDate)
            }) {
                Text("Terapkan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
