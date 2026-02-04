package com.umar.warunggo.data.model

import java.time.LocalDate

/**
 * Summary Report Models
 */

/**
 * Main summary report data
 */
data class SummaryReport(
    val revenue: Double,
    val transactionCount: Int,
    val averageTransaction: Double,
    val estimatedProfit: Double?,
    val revenueTrend: List<Float>,
    val topProducts: List<TopProduct>,
    val lowStockProducts: List<LowStockProduct>,
    val comparison: ComparisonData?
)

/**
 * Top selling product
 */
data class TopProduct(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val qtySold: Int,
    val revenueContributionPercent: Float
)

/**
 * Low stock product alert
 */
data class LowStockProduct(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val remainingStock: Int,
    val threshold: Int
)

/**
 * Comparison data with previous period
 */
data class ComparisonData(
    val revenueChangePercent: Float?,
    val transactionChangePercent: Float?
)

/**
 * Period type for filtering
 */
enum class PeriodType(val displayName: String) {
    TODAY("Hari Ini"),
    THIS_WEEK("Minggu Ini"),
    THIS_MONTH("Bulan Ini"),
    CUSTOM("Kustom")
}

/**
 * Custom date range for summary report
 */
data class SummaryDateRange(
    val startDate: Long,
    val endDate: Long
) {
    fun getDisplayText(): String {
        val dateFormat = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale("id", "ID"))
        val start = dateFormat.format(java.util.Date(startDate))
        val end = dateFormat.format(java.util.Date(endDate))
        return "$start - $end"
    }
}
