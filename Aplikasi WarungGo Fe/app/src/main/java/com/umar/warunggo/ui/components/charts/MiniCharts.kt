package com.umar.warunggo.ui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.warunggo.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

/**
 * Mini Sales Chart Component
 * Grafik garis sederhana untuk menampilkan tren penjualan 7 hari terakhir
 * 
 * @param data List of values untuk 7 hari terakhir
 * @param labels List of labels (hari)
 */
@Composable
fun MiniSalesChart(
    data: List<Long>,
    labels: List<String>,
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
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tren Penjualan 7 Hari",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                
                Icon(
                    imageVector = Icons.Filled.ShowChart,
                    contentDescription = "Chart",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Chart
            LineChart(
                data = data,
                labels = labels,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Stats summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ChartStat(
                    label = "Min",
                    value = formatCompactRupiah(data.minOrNull() ?: 0L),
                    color = DangerRed
                )
                ChartStat(
                    label = "Rata-rata",
                    value = formatCompactRupiah(data.average().toLong()),
                    color = TextSecondary
                )
                ChartStat(
                    label = "Max",
                    value = formatCompactRupiah(data.maxOrNull() ?: 0L),
                    color = SuccessGreen
                )
            }
        }
    }
}

/**
 * Line Chart Component using Canvas
 */
@Composable
private fun LineChart(
    data: List<Long>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    val primaryColor = PrimaryBlue
    
    Canvas(modifier = modifier) {
        if (data.isEmpty() || data.size < 2) return@Canvas
        
        val width = size.width
        val height = size.height
        val spacing = width / (data.size - 1)
        
        val maxValue = data.maxOrNull() ?: 1L
        val minValue = data.minOrNull() ?: 0L
        val range = (maxValue - minValue).toFloat()
        
        // Normalize data points
        val points = data.mapIndexed { index, value ->
            val x = index * spacing
            val normalizedValue = if (range > 0) {
                ((value - minValue) / range)
            } else {
                0.5f
            }
            val y = height - (normalizedValue * height * 0.8f) - (height * 0.1f)
            Offset(x, y)
        }
        
        // Draw gradient fill
        val path = Path().apply {
            moveTo(points.first().x, height)
            points.forEach { point ->
                lineTo(point.x, point.y)
            }
            lineTo(points.last().x, height)
            close()
        }
        
        val gradient = Brush.verticalGradient(
            colors = listOf(
                primaryColor.copy(alpha = 0.3f),
                Color.Transparent
            ),
            startY = 0f,
            endY = height
        )
        
        drawPath(
            path = path,
            brush = gradient
        )
        
        // Draw line
        val linePath = Path().apply {
            moveTo(points.first().x, points.first().y)
            points.forEach { point ->
                lineTo(point.x, point.y)
            }
        }
        
        drawPath(
            path = linePath,
            color = primaryColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        
        // Draw points
        points.forEach { point ->
            drawCircle(
                color = primaryColor,
                radius = 4.dp.toPx(),
                center = point
            )
            drawCircle(
                color = Color.White,
                radius = 2.dp.toPx(),
                center = point
            )
        }
    }
}

/**
 * Chart Stat Component
 */
@Composable
private fun ChartStat(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextSecondary
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

/**
 * Format rupiah compact (K for thousands, M for millions)
 */
private fun formatCompactRupiah(value: Long): String {
    return when {
        value >= 1_000_000 -> String.format("%.1fM", value / 1_000_000.0)
        value >= 1_000 -> String.format("%.0fK", value / 1_000.0)
        else -> value.toString()
    }
}

/**
 * Simple Bar Chart Component (alternative)
 */
@Composable
fun SimpleBarChart(
    data: List<Pair<String, Long>>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Statistik",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            val maxValue = data.maxOfOrNull { it.second } ?: 1L
            
            data.forEach { (label, value) ->
                BarItem(
                    label = label,
                    value = value,
                    maxValue = maxValue
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun BarItem(
    label: String,
    value: Long,
    maxValue: Long
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextSecondary
            )
            Text(
                text = formatCompactRupiah(value),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        val fraction = if (maxValue > 0) value.toFloat() / maxValue.toFloat() else 0f
        
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = PrimaryBlue,
            trackColor = CardDark,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun MiniSalesChartPreview() {
    WarungGoTheme {
        MiniSalesChart(
            data = listOf(1_200_000, 1_500_000, 1_800_000, 2_100_000, 1_900_000, 2_300_000, 2_500_000),
            labels = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min"),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun SimpleBarChartPreview() {
    WarungGoTheme {
        SimpleBarChart(
            data = listOf(
                "Penjualan" to 2_500_000,
                "Pengeluaran" to 800_000,
                "Laba" to 1_700_000
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
