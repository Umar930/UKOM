package com.umar.warunggo.ui.transaction.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.umar.warunggo.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Payment method detail dialog with full-screen implementations
 */
@Composable
fun PaymentMethodDetailDialog(
    paymentMethod: PaymentMethod,
    totalAmount: Double,
    onDismiss: () -> Unit,
    onPaymentConfirm: () -> Unit,
    onPaymentAmountChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                PaymentMethodHeader(
                    paymentMethod = paymentMethod,
                    onDismiss = onDismiss
                )

                // Content based on payment method
                when (paymentMethod) {
                    PaymentMethod.CASH -> CashPaymentContent(
                        totalAmount = totalAmount,
                        onPaymentAmountChange = onPaymentAmountChange,
                        onConfirm = onPaymentConfirm
                    )
                    PaymentMethod.QRIS -> QrisPaymentContent(
                        totalAmount = totalAmount,
                        onConfirm = onPaymentConfirm,
                        onCancel = onDismiss
                    )
                    PaymentMethod.TRANSFER -> TransferPaymentContent(
                        totalAmount = totalAmount,
                        onConfirm = onPaymentConfirm
                    )
                    PaymentMethod.E_WALLET -> EWalletPaymentContent(
                        totalAmount = totalAmount,
                        onConfirm = onPaymentConfirm
                    )
                    PaymentMethod.DEBIT_CARD, PaymentMethod.CREDIT_CARD -> CardPaymentContent(
                        totalAmount = totalAmount,
                        paymentMethod = paymentMethod,
                        onConfirm = onPaymentConfirm
                    )
                    PaymentMethod.CREDIT -> CreditPaymentContent(
                        totalAmount = totalAmount,
                        onConfirm = onPaymentConfirm
                    )
                }
            }
        }
    }
}

/**
 * Payment method header
 */
@Composable
private fun PaymentMethodHeader(
    paymentMethod: PaymentMethod,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = when (paymentMethod) {
                    PaymentMethod.CASH -> Icons.Default.Money
                    PaymentMethod.QRIS -> Icons.Default.QrCode2
                    PaymentMethod.TRANSFER -> Icons.Default.AccountBalance
                    PaymentMethod.E_WALLET -> Icons.Default.AccountBalanceWallet
                    PaymentMethod.DEBIT_CARD, PaymentMethod.CREDIT_CARD -> Icons.Default.CreditCard
                    PaymentMethod.CREDIT -> Icons.Default.Receipt
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = paymentMethod.displayName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Tutup",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * Cash payment content with quick amount buttons
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CashPaymentContent(
    totalAmount: Double,
    onPaymentAmountChange: (String) -> Unit,
    onConfirm: () -> Unit
) {
    var paidAmount by remember { mutableStateOf("") }
    val change = (paidAmount.toDoubleOrNull() ?: 0.0) - totalAmount

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Total amount display
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Pembayaran",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = "Rp ${"%,d".format(totalAmount.toLong()).replace(',', '.')}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // Quick amount buttons
        Text(
            text = "Nominal Cepat",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val quickAmounts = listOf(10000, 20000, 50000, 100000)
            quickAmounts.forEach { amount ->
                OutlinedButton(
                    onClick = {
                        paidAmount = amount.toString()
                        onPaymentAmountChange(amount.toString())
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("${amount / 1000}k")
                }
            }
        }

        // Manual input
        OutlinedTextField(
            value = paidAmount,
            onValueChange = { value ->
                if (value.isEmpty() || value.all { it.isDigit() }) {
                    paidAmount = value
                    onPaymentAmountChange(value)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Jumlah Dibayar") },
            placeholder = { Text("Masukkan jumlah") },
            leadingIcon = {
                Icon(Icons.Default.Money, contentDescription = null)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )

        // Change display
        AnimatedVisibility(visible = change >= 0 && paidAmount.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (change > 0) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Kembalian",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Rp ${"%,d".format(change.toLong()).replace(',', '.')}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Confirm button
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = change >= 0 && paidAmount.isNotEmpty()
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Konfirmasi Pembayaran")
        }
    }
}

/**
 * QRIS payment content with QR code simulation
 */
@Composable
private fun QrisPaymentContent(
    totalAmount: Double,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    var paymentStatus by remember { mutableStateOf(PaymentStatus.PENDING) }
    var isChecking by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Amount display
        Text(
            text = "Scan QR Code untuk membayar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "Rp ${"%,d".format(totalAmount.toLong()).replace(',', '.')}",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // QR Code placeholder (simulated)
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.QrCode2,
                contentDescription = "QR Code",
                modifier = Modifier.size(150.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Payment status
        when (paymentStatus) {
            PaymentStatus.PENDING -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Menunggu Pembayaran",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Scan QR code dengan aplikasi pembayaran Anda",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            PaymentStatus.SUCCESS -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Pembayaran Berhasil!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            PaymentStatus.FAILED -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Pembayaran Gagal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Action buttons
        if (paymentStatus == PaymentStatus.PENDING) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Batal")
                }
                Button(
                    onClick = {
                        isChecking = true
                        scope.launch {
                            delay(2000)
                            paymentStatus = PaymentStatus.SUCCESS
                            isChecking = false
                            delay(1000)
                            onConfirm()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isChecking
                ) {
                    if (isChecking) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Cek Status")
                    }
                }
            }
        } else if (paymentStatus == PaymentStatus.SUCCESS) {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Selesai")
            }
        }
    }
}

/**
 * Bank transfer payment content
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransferPaymentContent(
    totalAmount: Double,
    onConfirm: () -> Unit
) {
    var selectedBank by remember { mutableStateOf<BankProvider?>(null) }
    var showBankMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Amount display
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Transfer",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Rp ${"%,d".format(totalAmount.toLong()).replace(',', '.')}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Bank selection
        ExposedDropdownMenuBox(
            expanded = showBankMenu,
            onExpandedChange = { showBankMenu = it }
        ) {
            OutlinedTextField(
                value = selectedBank?.displayName ?: "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                label = { Text("Pilih Bank Tujuan") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = showBankMenu)
                },
                leadingIcon = {
                    Icon(Icons.Default.AccountBalance, contentDescription = null)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            
            ExposedDropdownMenu(
                expanded = showBankMenu,
                onDismissRequest = { showBankMenu = false }
            ) {
                BankProvider.entries.forEach { bank ->
                    DropdownMenuItem(
                        text = { Text(bank.displayName) },
                        onClick = {
                            selectedBank = bank
                            showBankMenu = false
                        }
                    )
                }
            }
        }

        // Bank account info
        AnimatedVisibility(visible = selectedBank != null) {
            selectedBank?.let { bank ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Rekening Tujuan",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Bank",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = bank.displayName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "No. Rekening",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = bank.accountNumber,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Atas Nama",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Warung Berkah Jaya",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Instructions
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Column {
                    Text(
                        text = "Instruksi",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "1. Transfer ke rekening yang tertera\n2. Konfirmasi setelah transfer\n3. Kirim bukti transfer jika diperlukan",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Confirm button
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedBank != null
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Konfirmasi Transfer")
        }
    }
}

/**
 * E-Wallet payment content
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EWalletPaymentContent(
    totalAmount: Double,
    onConfirm: () -> Unit
) {
    var selectedWallet by remember { mutableStateOf<EWalletProvider?>(null) }
    var showWalletMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Amount display
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Pembayaran",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Rp ${"%,d".format(totalAmount.toLong()).replace(',', '.')}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // E-Wallet selection
        ExposedDropdownMenuBox(
            expanded = showWalletMenu,
            onExpandedChange = { showWalletMenu = it }
        ) {
            OutlinedTextField(
                value = selectedWallet?.displayName ?: "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                label = { Text("Pilih E-Wallet") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = showWalletMenu)
                },
                leadingIcon = {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            
            ExposedDropdownMenu(
                expanded = showWalletMenu,
                onDismissRequest = { showWalletMenu = false }
            ) {
                EWalletProvider.entries.forEach { wallet ->
                    DropdownMenuItem(
                        text = { Text(wallet.displayName) },
                        onClick = {
                            selectedWallet = wallet
                            showWalletMenu = false
                        }
                    )
                }
            }
        }

        // Payment instructions
        AnimatedVisibility(visible = selectedWallet != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Buka aplikasi ${selectedWallet?.displayName}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Lakukan pembayaran dengan nominal yang tertera",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Confirm button
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedWallet != null
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Konfirmasi Pembayaran")
        }
    }
}

/**
 * Card payment content (Debit/Credit)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardPaymentContent(
    totalAmount: Double,
    paymentMethod: PaymentMethod,
    onConfirm: () -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Amount display
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Pembayaran",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Rp ${"%,d".format(totalAmount.toLong()).replace(',', '.')}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Card number input
        OutlinedTextField(
            value = cardNumber,
            onValueChange = { value ->
                if (value.length <= 16 && value.all { it.isDigit() }) {
                    cardNumber = value
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nomor Kartu") },
            placeholder = { Text("16 digit nomor kartu") },
            leadingIcon = {
                Icon(Icons.Default.CreditCard, contentDescription = null)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )

        // Card holder name
        OutlinedTextField(
            value = cardHolder,
            onValueChange = { cardHolder = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nama Pemegang Kartu") },
            placeholder = { Text("Sesuai kartu") },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null)
            },
            singleLine = true
        )

        // EDC machine info
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = "Gesek kartu pada mesin EDC atau masukkan data kartu secara manual",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Confirm button
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = cardNumber.length == 16 && cardHolder.isNotEmpty()
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Proses Pembayaran")
        }
    }
}

/**
 * Credit/Hutang payment content
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreditPaymentContent(
    totalAmount: Double,
    onConfirm: () -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Amount display
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Hutang",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = "Rp ${"%,d".format(totalAmount.toLong()).replace(',', '.')}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Customer info
        OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nama Pelanggan") },
            placeholder = { Text("Masukkan nama") },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null)
            },
            singleLine = true
        )

        OutlinedTextField(
            value = customerPhone,
            onValueChange = { value ->
                if (value.all { it.isDigit() }) {
                    customerPhone = value
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("No. Telepon") },
            placeholder = { Text("Masukkan nomor telepon") },
            leadingIcon = {
                Icon(Icons.Default.Phone, contentDescription = null)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Catatan (Opsional)") },
            placeholder = { Text("Keterangan tambahan") },
            leadingIcon = {
                Icon(Icons.Default.Notes, contentDescription = null)
            },
            minLines = 3,
            maxLines = 5
        )

        // Warning card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "Pastikan data pelanggan sudah benar. Transaksi hutang akan dicatat dan perlu dilunasi.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Confirm button
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = customerName.isNotEmpty() && customerPhone.isNotEmpty()
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Catat Hutang")
        }
    }
}
