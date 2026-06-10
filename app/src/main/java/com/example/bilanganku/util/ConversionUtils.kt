package com.example.bilanganku.util

import android.util.Base64

object ConversionUtils {
    fun convertUniversal(input: String, currentBase: Int, targetBase: Int): String {
        if (input.isEmpty()) return "0"
        return try {
            val decimalValue = input.toLong(currentBase)
            decimalValue.toString(targetBase).uppercase()
        } catch (ignored: Exception) {
            "ERROR"
        }
    }

    fun textToBinary(text: String): String {
        if (text.isEmpty()) return "..."
        val byteData = text.toByteArray()
        return byteData.joinToString(" ") { Integer.toBinaryString(it.toInt()).padStart(8, '0') }
    }

    fun textToHex(text: String): String {
        if (text.isEmpty()) return "..."
        val byteData = text.toByteArray()
        return byteData.joinToString(" ") { String.format("%02X", it) }
    }

    fun textToDecimal(text: String): String {
        if (text.isEmpty()) return "..."
        val byteData = text.toByteArray()
        return byteData.joinToString(" ") { it.toInt().toString() }
    }

    fun textToOctal(text: String): String {
        if (text.isEmpty()) return "..."
        val byteData = text.toByteArray()
        return byteData.joinToString(" ") { Integer.toOctalString(it.toInt()) }
    }

    fun textToBase64(text: String): String {
        if (text.isEmpty()) return "..."
        val byteData = text.toByteArray()
        return Base64.encodeToString(byteData, Base64.NO_WRAP)
    }
}
