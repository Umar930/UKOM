package com.umar.warunggo.ui.report.summary.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umar.warunggo.data.model.ComparisonData
import com.umar.warunggo.ui.theme.WarungGoTheme

/**
 * KPI Summary Grid - displays key metrics in 2-column grid
 */
@Composable
fun KpiSummaryGrid(
    revenue: Double,
    transactionCount: Int,
    averageTransaction: Double,
    estimatedProfit: Double?,
    revenueTrend: List<Float>,
    comparison: ComparisonData?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // First Row: Revenue & Transaction Count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KpiCard(
                title = "Omzet",
                value = formatCurrency(revenue),
                icon = Icons.Default.TrendingUp,
                trendData = revenueTrend,
                changePercent = comparison?.revenueChangePercent,
                modifier = Modifier.weight(1f)
            )

            KpiCard(
                title = "Transaksi",
                value = transactionCount.toString(),
                icon = Icons.Default.Receipt,
                trendData = emptyList(),
                changePercent = comparison?.transactionChangePercent,
                modifier = Modifier.weight(1f)
            )
        }

        // Second Row: Average Transaction & Estimated Profit
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KpiCard(
                title = "Rata-rata",
                value = formatCurrency(averageTransaction),
                icon = Icons.Default.Calculate,
                trendData = emptyList(),
                changePercent = null,
                modifier = Modifier.weight(1f)
            )

            KpiCard(
                title = "Est. Untung",
                value = estimatedProfit?.let { formatCurrency(it) } ?: "-",
                icon = Icons.Default.Savings,
                trendData = emptyList(),
                changePercent = null,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Individual KPI Card
 */
@Composable
private fun KpiCard(
    title: String,
    value: String,
    icon: ImageVector,
    trendData: List<Float>,
    changePercent: Float?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: Icon + Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Value
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Trend sparkline & Change percent
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mini sparkline
                if (trendData.isNotEmpty()) {
                    MiniSparkline(
                        data = trendData,
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Change indicator
                changePercent?.let { percent ->
                    val isPositive = percent >= 0
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = if (isPositive) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = if (isPositive) Color(0xFF4CAF50) else Color(0xFFE57373),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${String.format("%.1f", kotlin.math.abs(percent))}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFE57373),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Mini sparkline chart
 */
@Composable
private fun MiniSparkline(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        if (data.size < 2) return@Canvas

        val width = size.width
        val height = size.height
        val maxValue = data.maxOrNull() ?: 1f
        val minValue = data.minOrNull() ?: 0f
        val range = maxValue - minValue

        val path = Path()
        val stepX = width / (data.size - 1)

        data.forEachIndexed { index, value ->
            val x = index * stepX
            val normalizedValue = if (range > 0) (value - minValue) / range else 0.5f
            val y = height - (normalizedValue * height)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = primaryColor,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
        )
    }
}

/**
 * Format currency to IDR
 */
private fun formatCurrency(amount: Double): String {
    return "Rp ${"%,.0f".format(amount).replace(',', '.')}"
}

@Preview(showBackground = true)
@Composable
private fun KpiSummaryGridPreview() {
    WarungGoTheme {
        Surface {
            KpiSummaryGrid(
                revenue = 5_250_000.0,
                transactionCount = 143,
                averageTransaction = 36_713.0,
                estimatedProfit = 1_575_000.0,
                revenueTrend = listOf(30f, 45f, 38f, 60f, 55f, 70f, 65f),
                comparison = ComparisonData(
                    revenueChangePercent = 12.5f,
                    transactionChangePercent = -3.2f
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KpiCardPreview() {
    WarungGoTheme {
        Surface {
            KpiCard(
                title = "Omzet",
                value = "Rp 5.250.000",
                icon = Icons.Default.TrendingUp,
                trendData = listOf(30f, 45f, 38f, 60f, 55f, 70f, 65f),
                changePercent = 12.5f,
                modifier = Modifier
                    .width(180.dp)
                    .padding(16.dp)
            )
        }
    }
}
