package com.umar.warunggo.data.repository

import com.umar.warunggo.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

/**
 * Transaction Repository
 * Handles transaction data and simulates API calls
 */
object TransactionRepository {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _paymentStatus = MutableStateFlow<Map<String, PaymentStatus>>(emptyMap())
    val paymentStatus: StateFlow<Map<String, PaymentStatus>> = _paymentStatus.asStateFlow()

    /**
     * Generate unique transaction ID
     * Format: TRX + YYYYMMDDHHMMSS + Random
     */
    fun generateTransactionId(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale("id", "ID"))
        val timestamp = dateFormat.format(Date())
        val random = (1000..9999).random()
        return "TRX$timestamp$random"
    }

    /**
     * Submit transaction with API simulation
     * Simulates network delay and response
     */
    suspend fun submitTransaction(transaction: Transaction): Result<Transaction> {
        return try {
            // Simulate API delay
            delay(2000)

            // Random success/failure (90% success rate for simulation)
            val isSuccess = (1..10).random() <= 9

            if (isSuccess) {
                // Add transaction to list
                val currentTransactions = _transactions.value.toMutableList()
                currentTransactions.add(0, transaction)
                _transactions.value = currentTransactions

                // Set initial payment status based on payment method
                val initialStatus = when (transaction.paymentMethod) {
                    PaymentMethod.QRIS -> PaymentStatus.PENDING
                    PaymentMethod.TRANSFER -> PaymentStatus.PENDING
                    PaymentMethod.E_WALLET -> PaymentStatus.PENDING
                    else -> PaymentStatus.SUCCESS
                }
                
                updatePaymentStatus(transaction.id, initialStatus)

                Result.success(transaction)
            } else {
                Result.failure(Exception("Gagal memproses transaksi. Silakan coba lagi."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Terjadi kesalahan: ${e.message}"))
        }
    }

    /**
     * Check payment status (for QRIS, Transfer, E-Wallet)
     * Simulates payment status polling
     */
    suspend fun checkPaymentStatus(transactionId: String): PaymentStatus {
        // Simulate API delay
        delay(1000)

        val currentStatus = _paymentStatus.value[transactionId] ?: PaymentStatus.PENDING

        // If already final status, return as is
        if (currentStatus == PaymentStatus.SUCCESS || 
            currentStatus == PaymentStatus.FAILED || 
            currentStatus == PaymentStatus.CANCELLED) {
            return currentStatus
        }

        // Simulate status change (30% chance to become SUCCESS)
        val newStatus = if ((1..10).random() <= 3) {
            PaymentStatus.SUCCESS
        } else {
            PaymentStatus.PENDING
        }

        updatePaymentStatus(transactionId, newStatus)
        return newStatus
    }

    /**
     * Update payment status
     */
    private fun updatePaymentStatus(transactionId: String, status: PaymentStatus) {
        val currentStatuses = _paymentStatus.value.toMutableMap()
        currentStatuses[transactionId] = status
        _paymentStatus.value = currentStatuses
    }

    /**
     * Cancel payment
     */
    suspend fun cancelPayment(transactionId: String): Result<Unit> {
        return try {
            delay(500)
            updatePaymentStatus(transactionId, PaymentStatus.CANCELLED)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all transactions
     */
    fun getAllTransactions(): List<Transaction> {
        return _transactions.value
    }

    /**
     * Get transaction by ID
     */
    fun getTransactionById(id: String): Transaction? {
        return _transactions.value.find { it.id == id }
    }

    /**
     * Get transactions by date range
     */
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): List<Transaction> {
        return _transactions.value.filter { 
            it.transactionDate in startDate..endDate 
        }
    }

    /**
     * Get today's transactions
     */
    fun getTodayTransactions(): List<Transaction> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = calendar.timeInMillis

        return getTransactionsByDateRange(startOfDay, endOfDay)
    }

    /**
     * Calculate total sales for today
     */
    fun getTodayTotalSales(): Double {
        return getTodayTransactions()
            .filter { _paymentStatus.value[it.id] == PaymentStatus.SUCCESS }
            .sumOf { it.total }
    }

    /**
     * Calculate total transactions count for today
     */
    fun getTodayTransactionCount(): Int {
        return getTodayTransactions()
            .filter { _paymentStatus.value[it.id] == PaymentStatus.SUCCESS }
            .size
    }

    /**
     * Clear all transactions (for testing)
     */
    fun clearAllTransactions() {
        _transactions.value = emptyList()
        _paymentStatus.value = emptyMap()
    }
}
