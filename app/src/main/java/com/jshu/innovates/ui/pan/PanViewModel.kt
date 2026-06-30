package com.jshu.innovates.ui.pan

import androidx.lifecycle.ViewModel
import com.jshu.innovates.domain.BirthdateValidator
import com.jshu.innovates.domain.PanValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class PanUiState(
    val panNumber: String = "",
    val day: String = "",
    val month: String = "",
    val year: String = "",
    val isNextEnabled: Boolean = false,
    val isPanValid: Boolean = true,
    val isDayValid: Boolean = true,
    val isMonthValid: Boolean = true,
    val isYearValid: Boolean = true
)

@HiltViewModel
class PanViewModel @Inject constructor(
    private val panValidator: PanValidator,
    private val birthdateValidator: BirthdateValidator
) : ViewModel() {

    private val _uiState = MutableStateFlow(PanUiState())
    val uiState: StateFlow<PanUiState> = _uiState.asStateFlow()

    fun onPanChange(pan: String) {
        val cleanPan = pan.uppercase().take(10)
        _uiState.update {
            it.copy(
                panNumber = cleanPan,
                isPanValid = cleanPan.isEmpty() || panValidator.isValid(cleanPan)
            )
        }
        validate()
    }

    fun onDayChange(day: String) {
        if (day.length <= 2 && day.all { it.isDigit() }) {
            val d = day.toIntOrNull()
            _uiState.update {
                it.copy(
                    day = day,
                    isDayValid = day.isEmpty() || (d != null && d in 1..31)
                )
            }
            validate()
        }
    }

    fun onMonthChange(month: String) {
        if (month.length <= 2 && month.all { it.isDigit() }) {
            val m = month.toIntOrNull()
            _uiState.update {
                it.copy(
                    month = month,
                    isMonthValid = month.isEmpty() || (m != null && m in 1..12)
                )
            }
            validate()
        }
    }

    fun onYearChange(year: String) {
        if (year.length <= 4 && year.all { it.isDigit() }) {
            val y = year.toIntOrNull()
            val currentYear = java.time.LocalDate.now().year
            _uiState.update {
                it.copy(
                    year = year,
                    isYearValid = year.isEmpty() || (year.length < 4 || (y != null && y in 1900..currentYear))
                )
            }
            validate()
        }
    }

    private fun validate() {
        val state = _uiState.value
        val isPanValid = panValidator.isValid(state.panNumber)
        val isDobValid = birthdateValidator.isValid(state.day, state.month, state.year)

        // Detailed DOB validation for UI feedback
        val d = state.day.toIntOrNull()
        val m = state.month.toIntOrNull()
        val y = state.year.toIntOrNull()
        val currentYear = java.time.LocalDate.now().year

        val isDayInRange = d != null && d in 1..31
        val isMonthInRange = m != null && m in 1..12
        val isYearInRange = y != null && y in 1900..currentYear

        // If all fields are complete but LocalDate throws (e.g. Feb 31), mark fields invalid
        val isRealDate = if (state.day.isNotEmpty() && state.month.isNotEmpty() && state.year.length == 4) {
            isDobValid
        } else {
            true // Don't show error until full date is typed
        }

        _uiState.update {
            it.copy(
                isNextEnabled = isPanValid && isDobValid,
                isDayValid = state.day.isEmpty() || (isDayInRange && isRealDate),
                isMonthValid = state.month.isEmpty() || (isMonthInRange && isRealDate),
                isYearValid = state.year.isEmpty() || (state.year.length < 4 || (isYearInRange && isRealDate))
            )
        }
    }
}
