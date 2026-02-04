package com.umar.warunggo.ui.report.summary

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.umar.warunggo.data.model.*
import com.umar.warunggo.ui.report.summary.components.*
import com.umar.warunggo.ui.theme.WarungGoTheme
import kotlinx.coroutines.launch

/**
 * Summary Report Screen - Business snapshot for owners/cashiers
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryReportScreen(
    viewModel: SummaryReportViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToProduct: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedPeriod by viewModel.selectedPeriod.collectAsStateWithLifecycle()
    val customRange by viewModel.customRange.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Laporan Ringkas",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    // Refresh button
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            // Optional: Quick refresh FAB
            if (uiState !is SummaryReportUiState.Loading) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.refresh() },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                    },
                    text = { Text("Refresh") }
                )
            }
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is SummaryReportUiState.Loading -> {
                LoadingState(modifier = Modifier.padding(paddingValues))
            }

            is SummaryReportUiState.Success -> {
                SuccessContent(
                    data = state.data,
                    selectedPeriod = selectedPeriod,
                    customRange = customRange,
                    onPeriodSelected = { viewModel.updatePeriod(it) },
                    onRefresh = { viewModel.refresh() },
                    onViewAllProducts = {
                        // Open web admin URL
                        openWebAdmin(context, "/products")
                    },
                    onViewProduct = onNavigateToProduct,
                    onOpenWebAdmin = {
                        openWebAdmin(context, "/reports/full")
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is SummaryReportUiState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = { viewModel.retry() },
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is SummaryReportUiState.Empty -> {
                EmptyState(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}

/**
 * Success content with pull-to-refresh
 */
@Composable
private fun SuccessContent(
    data: SummaryReport,
    selectedPeriod: PeriodType,
    customRange: SummaryDateRange?,
    onPeriodSelected: (PeriodType) -> Unit,
    onRefresh: () -> Unit,
    onViewAllProducts: () -> Unit,
    onViewProduct: (String) -> Unit,
    onOpenWebAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            isRefreshing = true
            onRefresh()
            // Simulate refresh delay
            scope.launch {
                kotlinx.coroutines.delay(1500)
                isRefreshing = false
            }
        },
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp), // Space for FAB
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Period filter chips
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PeriodFilterChips(
                        selectedPeriod = selectedPeriod,
                        onPeriodSelected = onPeriodSelected
                    )

                    // Custom range display
                    if (selectedPeriod == PeriodType.CUSTOM && customRange != null) {
                        Text(
                            text = customRange.getDisplayText(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            // KPI Summary Grid
            item {
                KpiSummaryGrid(
                    revenue = data.revenue,
                    transactionCount = data.transactionCount,
                    averageTransaction = data.averageTransaction,
                    estimatedProfit = data.estimatedProfit,
                    revenueTrend = data.revenueTrend,
                    comparison = data.comparison
                )
            }

            // Sales Trend Card
            item {
                SalesTrendCard(
                    trendData = data.revenueTrend,
                    periodLabel = getPeriodLabel(selectedPeriod, data.revenueTrend.size),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Top Products Section
            item {
                TopProductsSection(
                    topProducts = data.topProducts,
                    onViewAllClick = onViewAllProducts,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Low Stock Section
            if (data.lowStockProducts.isNotEmpty()) {
                item {
                    LowStockSection(
                        lowStockProducts = data.lowStockProducts,
                        onViewProductClick = onViewProduct,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Open Web Report CTA
            item {
                OpenWebReportCard(
                    onOpenWebClick = onOpenWebAdmin,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

/**
 * Loading state with skeleton shimmer effect
 */
@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Period chips skeleton
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(4) {
                    Surface(
                        modifier = Modifier
                            .width(100.dp)
                            .height(32.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    ) {}
                }
            }
        }

        // KPI cards skeleton
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(2) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        repeat(2) {
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(120.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.medium
                            ) {}
                        }
                    }
                }
            }
        }

        // Trend card skeleton
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            ) {}
        }

        // Loading indicator
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

/**
 * Error state with retry button
 */
@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )

            Text(
                text = "Gagal Memuat Data",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onRetry,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Coba Lagi")
            }
        }
    }
}

/**
 * Empty state when no transactions
 */
@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Assessment,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                modifier = Modifier.size(80.dp)
            )

            Text(
                text = "Belum Ada Transaksi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Mulai transaksi untuk melihat laporan ringkas bisnis Anda",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Get period label for trend chart
 */
private fun getPeriodLabel(period: PeriodType, dataPoints: Int): String {
    return when (period) {
        PeriodType.TODAY -> "$dataPoints jam terakhir"
        PeriodType.THIS_WEEK -> "$dataPoints hari terakhir"
        PeriodType.THIS_MONTH -> "$dataPoints hari terakhir"
        PeriodType.CUSTOM -> "$dataPoints titik data"
    }
}

/**
 * Open web admin (placeholder implementation)
 */
private fun openWebAdmin(context: android.content.Context, path: String) {
    val url = "https://warunggo-admin.web.app$path"
    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
        data = android.net.Uri.parse(url)
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // Handle error - show toast
        android.widget.Toast.makeText(
            context,
            "Tidak dapat membuka browser",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}

// ============================================
// PREVIEWS
// ============================================

@Preview(showBackground = true)
@Composable
private fun SummaryReportScreenSuccessPreview() {
    WarungGoTheme {
        Surface {
            SuccessContent(
                data = SummaryReport(
                    revenue = 5_250_000.0,
                    transactionCount = 143,
                    averageTransaction = 36_713.0,
                    estimatedProfit = 1_575_000.0,
                    revenueTrend = listOf(45f, 52f, 48f, 65f, 58f, 72f, 68f, 75f, 70f, 82f, 78f, 85f, 90f, 88f),
                    topProducts = listOf(
                        TopProduct("1", "Indomie Goreng", null, 156, 28.5f),
                        TopProduct("2", "Aqua 600ml", null, 142, 22.3f),
                        TopProduct("3", "Beras Premium", null, 48, 18.7f)
                    ),
                    lowStockProducts = listOf(
                        LowStockProduct("1", "Sabun Mandi", null, 2, 5),
                        LowStockProduct("2", "Shampo", null, 4, 10)
                    ),
                    comparison = ComparisonData(12.5f, -3.2f)
                ),
                selectedPeriod = PeriodType.TODAY,
                customRange = null,
                onPeriodSelected = {},
                onRefresh = {},
                onViewAllProducts = {},
                onViewProduct = {},
                onOpenWebAdmin = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SummaryReportScreenLoadingPreview() {
    WarungGoTheme {
        Surface {
            LoadingState()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SummaryReportScreenErrorPreview() {
    WarungGoTheme {
        Surface {
            ErrorState(
                message = "Gagal memuat data. Periksa koneksi internet Anda.",
                onRetry = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SummaryReportScreenEmptyPreview() {
    WarungGoTheme {
        Surface {
            EmptyState()
        }
    }
}
