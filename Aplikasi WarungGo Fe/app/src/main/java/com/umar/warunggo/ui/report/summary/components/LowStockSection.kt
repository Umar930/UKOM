package com.umar.warunggo.ui.report.summary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umar.warunggo.data.model.LowStockProduct
import com.umar.warunggo.ui.theme.WarungGoTheme

/**
 * Low Stock Section - displays products with low inventory
 */
@Composable
fun LowStockSection(
    lowStockProducts: List<LowStockProduct>,
    onViewProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with warning icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFF6F00),
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "Stok Menipis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    if (lowStockProducts.isNotEmpty()) {
                        Text(
                            text = "${lowStockProducts.size} produk perlu direstock",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Product list
            if (lowStockProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Semua stok aman",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            } else {
                lowStockProducts.forEach { product ->
                    LowStockItem(
                        product = product,
                        onViewClick = { onViewProductClick(product.id) }
                    )
                }
            }
        }
    }
}

/**
 * Low Stock Item
 */
@Composable
private fun LowStockItem(
    product: LowStockProduct,
    onViewClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }

            // Product info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Stock badge
                    Surface(
                        color = if (product.remainingStock <= 2) 
                            Color(0xFFFFCDD2) 
                        else 
                            Color(0xFFFFF9C4),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = if (product.remainingStock <= 2) 
                                    Color(0xFFD32F2F) 
                                else 
                                    Color(0xFFF57C00)
                            )
                            Text(
                                text = "Sisa ${product.remainingStock}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (product.remainingStock <= 2) 
                                    Color(0xFFD32F2F) 
                                else 
                                    Color(0xFFF57C00)
                            )
                        }
                    }

                    Text(
                        text = "Min. ${product.threshold}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // View button
            OutlinedButton(
                onClick = onViewClick,
                modifier = Modifier.height(36.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text(
                    text = "Lihat",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LowStockSectionPreview() {
    WarungGoTheme {
        Surface {
            LowStockSection(
                lowStockProducts = listOf(
                    LowStockProduct(
                        id = "1",
                        name = "Sabun Mandi Lifebuoy",
                        imageUrl = null,
                        remainingStock = 2,
                        threshold = 5
                    ),
                    LowStockProduct(
                        id = "2",
                        name = "Shampo Sachet",
                        imageUrl = null,
                        remainingStock = 4,
                        threshold = 10
                    ),
                    LowStockProduct(
                        id = "3",
                        name = "Pasta Gigi Pepsodent",
                        imageUrl = null,
                        remainingStock = 3,
                        threshold = 5
                    )
                ),
                onViewProductClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LowStockSectionEmptyPreview() {
    WarungGoTheme {
        Surface {
            LowStockSection(
                lowStockProducts = emptyList(),
                onViewProductClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
