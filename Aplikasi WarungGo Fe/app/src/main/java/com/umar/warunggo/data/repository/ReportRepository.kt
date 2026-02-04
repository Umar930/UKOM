package com.umar.warunggo.data.repository

import com.umar.warunggo.data.model.*
import kotlinx.coroutines.delay
import java.time.LocalDate
import kotlin.random.Random

/**
 * Report Repository
 * Simulates API calls for business summary report
 */
object ReportRepository {

    /**
     * Get summary report for specified period
     */
    suspend fun getSummary(
        period: PeriodType,
        customRange: SummaryDateRange? = null
    ): Result<SummaryReport> {
        return try {
            // Simulate network delay
            delay(1500)

            // Random failure simulation (10% chance)
            if (Random.nextInt(100) < 10) {
                throw Exception("Gagal memuat data. Periksa koneksi internet Anda.")
            }

            val report = generateDummyReport(period)
            Result.success(report)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generate dummy report data
     */
    private fun generateDummyReport(period: PeriodType): SummaryReport {
        val baseRevenue = when (period) {
            PeriodType.TODAY -> Random.nextDouble(500_000.0, 2_000_000.0)
            PeriodType.THIS_WEEK -> Random.nextDouble(3_000_000.0, 10_000_000.0)
            PeriodType.THIS_MONTH -> Random.nextDouble(12_000_000.0, 40_000_000.0)
            PeriodType.CUSTOM -> Random.nextDouble(5_000_000.0, 20_000_000.0)
        }

        val transactionCount = when (period) {
            PeriodType.TODAY -> Random.nextInt(10, 50)
            PeriodType.THIS_WEEK -> Random.nextInt(50, 200)
            PeriodType.THIS_MONTH -> Random.nextInt(200, 800)
            PeriodType.CUSTOM -> Random.nextInt(100, 400)
        }

        val averageTransaction = baseRevenue / transactionCount
        val estimatedProfit = baseRevenue * Random.nextDouble(0.2, 0.35)

        // Generate trend data (7-14 points)
        val trendPoints = List(if (period == PeriodType.TODAY) 7 else 14) {
            Random.nextFloat() * 100f
        }

        // Top products
        val topProducts = listOf(
            TopProduct(
                id = "1",
                name = "Indomie Goreng",
                imageUrl = null,
                qtySold = Random.nextInt(50, 200),
                revenueContributionPercent = Random.nextFloat() * 15f + 20f
            ),
            TopProduct(
                id = "2",
                name = "Aqua 600ml",
                imageUrl = null,
                qtySold = Random.nextInt(40, 150),
                revenueContributionPercent = Random.nextFloat() * 10f + 15f
            ),
            TopProduct(
                id = "3",
                name = "Beras Premium 5kg",
                imageUrl = null,
                qtySold = Random.nextInt(20, 80),
                revenueContributionPercent = Random.nextFloat() * 8f + 12f
            ),
            TopProduct(
                id = "4",
                name = "Minyak Goreng 2L",
                imageUrl = null,
                qtySold = Random.nextInt(15, 60),
                revenueContributionPercent = Random.nextFloat() * 6f + 8f
            ),
            TopProduct(
                id = "5",
                name = "Gula Pasir 1kg",
                imageUrl = null,
                qtySold = Random.nextInt(10, 50),
                revenueContributionPercent = Random.nextFloat() * 5f + 5f
            )
        ).sortedByDescending { it.revenueContributionPercent }

        // Low stock products
        val lowStockProducts = listOf(
            LowStockProduct(
                id = "101",
                name = "Sabun Mandi",
                imageUrl = null,
                remainingStock = Random.nextInt(1, 5),
                threshold = 5
            ),
            LowStockProduct(
                id = "102",
                name = "Shampo Sachet",
                imageUrl = null,
                remainingStock = Random.nextInt(2, 8),
                threshold = 10
            ),
            LowStockProduct(
                id = "103",
                name = "Pasta Gigi",
                imageUrl = null,
                remainingStock = Random.nextInt(1, 4),
                threshold = 5
            ),
            LowStockProduct(
                id = "104",
                name = "Deterjen",
                imageUrl = null,
                remainingStock = Random.nextInt(3, 7),
                threshold = 10
            )
        ).filter { it.remainingStock <= it.threshold }

        // Comparison data
        val comparison = ComparisonData(
            revenueChangePercent = Random.nextFloat() * 40f - 20f, // -20% to +20%
            transactionChangePercent = Random.nextFloat() * 30f - 15f // -15% to +15%
        )

        return SummaryReport(
            revenue = baseRevenue,
            transactionCount = transactionCount,
            averageTransaction = averageTransaction,
            estimatedProfit = estimatedProfit,
            revenueTrend = trendPoints,
            topProducts = topProducts,
            lowStockProducts = lowStockProducts,
            comparison = comparison
        )
    }
}
