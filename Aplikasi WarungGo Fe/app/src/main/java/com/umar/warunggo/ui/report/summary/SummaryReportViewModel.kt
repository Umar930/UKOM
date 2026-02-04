package com.umar.warunggo.ui.report.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.warunggo.data.model.*
import com.umar.warunggo.data.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Summary Report Screen
 */
class SummaryReportViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SummaryReportUiState>(SummaryReportUiState.Loading)
    val uiState: StateFlow<SummaryReportUiState> = _uiState.asStateFlow()

    private val _selectedPeriod = MutableStateFlow(PeriodType.TODAY)
    val selectedPeriod: StateFlow<PeriodType> = _selectedPeriod.asStateFlow()

    private val _customRange = MutableStateFlow<SummaryDateRange?>(null)
    val customRange: StateFlow<SummaryDateRange?> = _customRange.asStateFlow()

    init {
        loadSummary(PeriodType.TODAY)
    }

    /**
     * Load summary report
     */
    fun loadSummary(period: PeriodType, customRange: SummaryDateRange? = null) {
        viewModelScope.launch {
            _uiState.value = SummaryReportUiState.Loading
            _selectedPeriod.value = period
            _customRange.value = customRange

            val result = ReportRepository.getSummary(period, customRange)

            _uiState.value = result.fold(
                onSuccess = { report ->
                    if (report.transactionCount == 0) {
                        SummaryReportUiState.Empty
                    } else {
                        SummaryReportUiState.Success(report)
                    }
                },
                onFailure = { exception ->
                    SummaryReportUiState.Error(
                        exception.message ?: "Terjadi kesalahan saat memuat data"
                    )
                }
            )
        }
    }

    /**
     * Refresh data
     */
    fun refresh() {
        loadSummary(_selectedPeriod.value, _customRange.value)
    }

    /**
     * Retry loading
     */
    fun retry() {
        loadSummary(_selectedPeriod.value, _customRange.value)
    }

    /**
     * Update selected period
     */
    fun updatePeriod(period: PeriodType) {
        if (period != _selectedPeriod.value) {
            loadSummary(period)
        }
    }

    /**
     * Update custom range
     */
    fun updateCustomRange(range: SummaryDateRange) {
        _customRange.value = range
        loadSummary(PeriodType.CUSTOM, range)
    }
}

/**
 * UI State for Summary Report
 */
sealed class SummaryReportUiState {
    object Loading : SummaryReportUiState()
    data class Success(val data: SummaryReport) : SummaryReportUiState()
    data class Error(val message: String) : SummaryReportUiState()
    object Empty : SummaryReportUiState()
}
