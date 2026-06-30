package com.jshu.innovates.domain

import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeParseException

class PanValidator {
    /**
     * Validates Indian PAN card number.
     * Format: 5 Letters, 4 Digits, 1 Letter (e.g., ABCDE1234F)
     */
    fun isValid(pan: String): Boolean {
        val regex = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        return regex.matches(pan.uppercase())
    }
}

class BirthdateValidator {
    /**
     * Validates birthdate strings for Day, Month, and Year.
     * 100% accurate calendar validation including leap years and days-in-month.
     */
    fun isValid(day: String, month: String, year: String): Boolean {
        // 1. Strict format check
        if (year.length != 4) return false

        // 2. Safe numeric parsing (returns false if inputs contain letters/symbols)
        val d = day.toIntOrNull() ?: return false
        val m = month.toIntOrNull() ?: return false
        val y = year.toIntOrNull() ?: return false

        return try {
            // 3. Perfect Calendar Check:
            // LocalDate.of throws a DateTimeException if the date is impossible
            // (e.g., Feb 29 on non-leap years, April 31, or Month 13).
            val birthDate = LocalDate.of(y, m, d)

            // 4. Logical Constraints:
            val today = LocalDate.now()
            val isNotFuture = !birthDate.isAfter(today)
            val isRealistic = y >= 1900 // Avoids nonsensical years like 0001

            isNotFuture && isRealistic
        } catch (e: DateTimeException) {
            // Catches any invalid date combination
            false
        }
    }
}