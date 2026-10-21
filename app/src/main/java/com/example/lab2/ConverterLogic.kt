package com.example.lab2


import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Definicja jednostek z dokładnymi przelicznikami na metry.
 */
enum class LengthUnit(val label: String, val symbol: String, val metersFactor: Double) {
    CENTIMETRE("Centimetres", "cm", 0.01),
    KILOMETRE("Kilometres", "km", 1000.0),
    MILE("Miles", "mi", 1609.344),
    LIGHT_YEAR("Light years", "ly", 9_460_730_472_580_800.0),
}

object ConverterLogic {

    /**
     * Bezpieczna konwersja tekstu na wartość w metrach.
     * Zamienia polski przecinek na kropkę, aby obsłużyć klawiatury regionalne.
     */
    fun parseInput(input: String): Double? {
        val normalized = input.trim().replace(',', '.')
        if (normalized.isEmpty()) return null
        return normalized.toDoubleOrNull()
    }

    /**
     * Przeliczenie wartości z metrów na zadaną jednostkę docelową.
     */
    fun convert(meters: Double, targetUnit: LengthUnit): Double {
        return meters / targetUnit.metersFactor
    }

    /**
     * Formatowanie wyniku:
     * - Bardzo małe liczby (< 0.001) lub olbrzymie (>= 1_000_000) przechodzą na notację wykładniczą.
     * - Standardowe liczby otrzymują separator tysięcy oraz do 4 miejsc po przecinku.
     */
    fun formatValue(value: Double): String {
        val absValue = Math.abs(value)
        if (absValue == 0.0) return "0"

        // Używamy symboli z kropką jako separatorem dziesiętnym i przecinkiem/spacją jako grupującym
        val symbols = DecimalFormatSymbols(Locale.US)

        return if (absValue < 1e-3 || absValue >= 1e6) {
            val scientificFormat = DecimalFormat("0.####E0", symbols)
            scientificFormat.format(value).replace("E", " × 10^")
        } else {
            val standardFormat = DecimalFormat("#,##0.####", symbols)
            standardFormat.format(value)
        }
    }
}