package com.example.level_up_gamer_app.utils

object Validators {
    fun isValidRUT(rut: String): Boolean {
        val cleanRut = rut.replace(".", "").replace("-", "").uppercase()
        if (cleanRut.length < 2) return false

        val rutNumber = cleanRut.substring(0, cleanRut.length - 1)
        val dv = cleanRut.last()

        if (!rutNumber.all { it.isDigit() }) return false

        return calculateDV(rutNumber) == dv
    }

    private fun calculateDV(rut: String): Char {
        var sum = 0
        var multiplier = 2

        for (i in rut.length - 1 downTo 0) {
            sum += rut[i].toString().toInt() * multiplier
            multiplier = if (multiplier == 7) 2 else multiplier + 1
        }

        val remainder = 11 - (sum % 11)
        return when (remainder) {
            11 -> '0'
            10 -> 'K'
            else -> remainder.toString().first()
        }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}