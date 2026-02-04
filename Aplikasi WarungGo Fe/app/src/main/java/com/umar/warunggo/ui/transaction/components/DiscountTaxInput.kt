package com.umar.warunggo.ui.transaction.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Discount input card with toggle between nominal and percentage
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountInputCard(
    discountAmount: Double,
    discountPercentage: Double,
    onDiscountAmountChange: (String) -> Unit,
    onDiscountPercentageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPercentage by remember { mutableStateOf(false) }
    var discountInput by remember { mutableStateOf("") }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Discount,
                        contentDescription = "Diskon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Diskon",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Toggle between nominal and percentage
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilterChip(
                        selected = !isPercentage,
                        onClick = { 
                            isPercentage = false
                            discountInput = ""
                        },
                        label = { Text("Rp") }
                    )
                    FilterChip(
                        selected = isPercentage,
                        onClick = { 
                            isPercentage = true
                            discountInput = ""
                        },
                        label = { Text("%") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = discountInput,
                onValueChange = { value ->
                    // Only allow numbers
                    if (value.isEmpty() || value.all { it.isDigit() }) {
                        discountInput = value
                        
                        if (value.isNotEmpty()) {
                            val numValue = value.toDoubleOrNull() ?: 0.0
                            if (isPercentage) {
                                // Validate percentage (0-100)
                                if (numValue <= 100) {
                                    onDiscountPercentageChange(value)
                                }
                            } else {
                                onDiscountAmountChange(value)
                            }
                        } else {
                            // Reset discount
                            if (isPercentage) {
                                onDiscountPercentageChange("0")
                            } else {
                                onDiscountAmountChange("0")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { 
                    Text(if (isPercentage) "Persentase Diskon (%)" else "Nominal Diskon (Rp)") 
                },
                placeholder = { 
                    Text(if (isPercentage) "Masukkan persentase" else "Masukkan nominal") 
                },
                leadingIcon = {
                    Icon(
                        imageVector = if (isPercentage) Icons.Default.Percent else Icons.Default.MoneyOff,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (discountInput.isNotEmpty()) {
                        IconButton(onClick = {
                            discountInput = ""
                            if (isPercentage) {
                                onDiscountPercentageChange("0")
                            } else {
                                onDiscountAmountChange("0")
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Hapus"
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            // Show validation error
            AnimatedVisibility(
                visible = isPercentage && discountInput.isNotEmpty() && 
                         (discountInput.toDoubleOrNull() ?: 0.0) > 100
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Persentase tidak boleh lebih dari 100%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * Tax toggle card with percentage input
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaxInputCard(
    isTaxEnabled: Boolean,
    taxPercentage: Double,
    taxAmount: Double,
    onTaxToggle: (Boolean) -> Unit,
    onTaxPercentageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var taxInput by remember { mutableStateOf(if (taxPercentage > 0) taxPercentage.toInt().toString() else "10") }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = "Pajak",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = "Pajak (PPN)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (isTaxEnabled) {
                            Text(
                                text = "Rp ${"%,d".format(taxAmount.toLong()).replace(',', '.')}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Switch(
                    checked = isTaxEnabled,
                    onCheckedChange = onTaxToggle
                )
            }

            AnimatedVisibility(
                visible = isTaxEnabled,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = taxInput,
                        onValueChange = { value ->
                            // Only allow numbers
                            if (value.isEmpty() || value.all { it.isDigit() }) {
                                taxInput = value
                                
                                if (value.isNotEmpty()) {
                                    val numValue = value.toDoubleOrNull() ?: 10.0
                                    // Validate percentage (0-100)
                                    if (numValue <= 100) {
                                        onTaxPercentageChange(value)
                                    }
                                } else {
                                    onTaxPercentageChange("10")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Persentase Pajak (%)") },
                        placeholder = { Text("Default 10%") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Percent,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )

                    // Show validation error
                    AnimatedVisibility(
                        visible = taxInput.isNotEmpty() && 
                                 (taxInput.toDoubleOrNull() ?: 0.0) > 100
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Persentase tidak boleh lebih dari 100%",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
