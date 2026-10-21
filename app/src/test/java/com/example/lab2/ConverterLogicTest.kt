package com.example.lab2

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ConverterLogicTest {

    // JUnit 4 wymaga delty przy porównywaniu liczb Double
    private val delta = 1e-9

    @Test
    fun testConversionsFromMeters() {
        val meters = 1500.0

        // cm: 1500 / 0.01 = 150 000
        assertEquals(150_000.0, ConverterLogic.convert(meters, LengthUnit.CENTIMETRE), delta)

        // km: 1500 / 1000 = 1.5
        assertEquals(1.5, ConverterLogic.convert(meters, LengthUnit.KILOMETRE), delta)

        // mi: 1500 / 1609.344 = ~0.932056788
        val expectedMiles = 1500.0 / 1609.344
        assertEquals(expectedMiles, ConverterLogic.convert(meters, LengthUnit.MILE), delta)

        // ly: 1500 / 9 460 730 472 580 800
        val expectedLy = 1500.0 / 9_460_730_472_580_800.0
        assertEquals(expectedLy, ConverterLogic.convert(meters, LengthUnit.LIGHT_YEAR), delta)
    }

    @Test
    fun testInputParsing() {
        // Prawidłowe liczby
        assertEquals(1500.0, ConverterLogic.parseInput("1500")!!, delta)
        assertEquals(1.5, ConverterLogic.parseInput("1.5")!!, delta)

        // Polski przecinek
        assertEquals(1.5, ConverterLogic.parseInput("1,5")!!, delta)

        // Zapis wykładniczy
        assertEquals(100000.0, ConverterLogic.parseInput("1e5")!!, delta)

        // Puste wejście
        assertNull(ConverterLogic.parseInput(""))
        assertNull(ConverterLogic.parseInput("   "))

        // Błędny tekst
        assertNull(ConverterLogic.parseInput("abc"))
        assertNull(ConverterLogic.parseInput("12a.5"))
    }

    @Test
    fun testFormattingThresholds() {
        // Poniżej progu 1e-3 -> notacja wykładnicza
        assertEquals("9.99 × 10^-4", ConverterLogic.formatValue(0.000999))

        // Na progu i powyżej 1e-3 -> standardowy zapis
        assertEquals("0.001", ConverterLogic.formatValue(0.001))

        // Poniżej progu 1e6 -> standardowy zapis z separatorem tysięcy
        assertEquals("150,000", ConverterLogic.formatValue(150000.0))

        // Od progu 1e6 w górę -> notacja wykładnicza
        assertEquals("1 × 10^6", ConverterLogic.formatValue(1_000_000.0))
    }
}