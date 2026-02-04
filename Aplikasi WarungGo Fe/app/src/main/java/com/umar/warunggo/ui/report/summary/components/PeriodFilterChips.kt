package com.umar.warunggo.ui.report.summary.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umar.warunggo.data.model.PeriodType
import com.umar.warunggo.ui.theme.WarungGoTheme

/**
 * Period filter chips for quick date selection
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodFilterChips(
    selectedPeriod: PeriodType,
    onPeriodSelected: (PeriodType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(PeriodType.values()) { period ->
            FilterChip(
                selected = period == selectedPeriod,
                onClick = { onPeriodSelected(period) },
                label = { Text(period.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PeriodFilterChipsPreview() {
    WarungGoTheme {
        Surface {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                PeriodFilterChips(
                    selectedPeriod = PeriodType.TODAY,
                    onPeriodSelected = {}
                )
            }
        }
    }
}
