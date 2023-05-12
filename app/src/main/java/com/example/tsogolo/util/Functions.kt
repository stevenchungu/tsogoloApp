package com.example.tsogolo.util

import kotlin.math.absoluteValue

object Functions {

    fun getMoneyStringShort(amount: Double?): String {
        amount ?: return ""

        return when {
            amount.absoluteValue >= 1000000000 -> "${String.format("%.1f", amount/1000000000)}B"
            amount.absoluteValue >= 1000000 -> "${String.format("%.1f", amount/1000000)}M"
            amount.absoluteValue >= 1000 -> "${String.format("%.1f", amount/1000)}K"
            else -> amount.toInt().toString()
        }
    }


    fun getMoneyString(amount: Double?) : String {
        val currency = "MK"
        amount ?: return "0 $currency"

        val amountStrings = String.format("%.2f", amount).split(".")

        var result = ".${amountStrings[1]}"

        var i = amountStrings[0].lastIndex
        while (i > -1) {
            if (i - 2 > 0) {
                result = ", " + amountStrings[0].substring((i-2)..i) + result
                i -= 3
            } else {
                result = amountStrings[0].substring(0..i) + result
                i = -1
            }
        }

        return "${currency ?: " "}$result"
    }

}