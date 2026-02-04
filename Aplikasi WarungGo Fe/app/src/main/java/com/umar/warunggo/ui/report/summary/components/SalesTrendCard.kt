package com.umar.warunggo.ui.report.summary.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umar.warunggo.ui.theme.WarungGoTheme

/**
 * Sales Trend Card with sparkline chart
 */
@Composable
fun SalesTrendCard(
    trendData: List<Float>,
    periodLabel: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShowChart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "Tren Penjualan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = periodLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }

            // Sparkline Chart
            if (trendData.isNotEmpty()) {
                SparklineChart(
                    data = trendData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada data tren",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                    )
                }
            }

            // Stats summary
            if (trendData.size >= 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TrendStat(
                        label = "Terendah",
                        value = trendData.minOrNull()?.toInt()?.toString() ?: "-"
                    )
                    TrendStat(
                        label = "Rata-rata",
                        value = (trendData.average().toInt()).toString()
                    )
                    TrendStat(
                        label = "Tertinggi",
                        value = trendData.maxOrNull()?.toInt()?.toString() ?: "-"
                    )
                }
            }
        }
    }
}

/**
 * Sparkline chart component
 */
@Composable
private fun SparklineChart(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.onPrimaryContainer
    val gradientColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)

    Canvas(modifier = modifier.padding(vertical = 8.dp)) {
        if (data.size < 2) return@Canvas

        val width = size.width
        val height = size.height
        val maxValue = data.maxOrNull() ?: 1f
        val minValue = data.minOrNull() ?: 0f
        val range = maxValue - minValue

        val stepX = width / (data.size - 1)

        // Draw gradient fill
        val fillPath = Path()
        data.forEachIndexed { index, value ->
            val x = index * stepX
            val normalizedValue = if (range > 0) (value - minValue) / range else 0.5f
            val y = height - (normalizedValue * height * 0.8f) // 80% of height

            if (index == 0) {
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                fillPath.lineTo(x, y)
            }

            if (index == data.size - 1) {
                fillPath.lineTo(x, height)
                fillPath.close()
            }
        }

        drawPath(
            path = fillPath,
            color = gradientColor
        )

        // Draw line
        val linePath = Path()
        data.forEachIndexed { index, value ->
            val x = index * stepX
            val normalizedValue = if (range > 0) (value - minValue) / range else 0.5f
            val y = height - (normalizedValue * height * 0.8f)

            if (index == 0) {
                linePath.moveTo(x, y)
            } else {
                linePath.lineTo(x, y)
            }
        }

        drawPath(
            path = linePath,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx())
        )

        // Draw data points
        data.forEachIndexed { index, value ->
            val x = index * stepX
            val normalizedValue = if (range > 0) (value - minValue) / range else 0.5f
            val y = height - (normalizedValue * height * 0.8f)

            drawCircle(
                color = lineColor,
                radius = 4.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}

/**
 * Trend stat component
 */
@Composable
private fun TrendStat(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SalesTrendCardPreview() {
    WarungGoTheme {
        Surface {
            SalesTrendCard(
                trendData = listOf(45f, 52f, 48f, 65f, 58f, 72f, 68f, 75f, 70f, 82f, 78f, 85f, 90f, 88f),
                periodLabel = "14 hari terakhir",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
