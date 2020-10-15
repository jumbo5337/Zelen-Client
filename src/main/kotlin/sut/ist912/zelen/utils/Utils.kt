package sut.ist912.zelen.utils

import javafx.beans.binding.BooleanBinding
import javafx.beans.property.StringProperty
import javafx.scene.control.TextFormatter
import javafx.scene.control.TreeItem
import javafx.util.StringConverter
import sut.ist912.zelen.rest.LoginClient
import tornadofx.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.UnaryOperator
import java.util.regex.Pattern


private val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")
private val feePercent = 0.015
private val validEditingState = Pattern.compile("(([1-9][0-9]*)|0)?(\\.[0-9]*)?")

fun Double.format(digits: Int = 2) = "%.${digits}f".format(this)

fun jwt() = "Bearer ${LoginClient.jwtToken}"

fun String?.isValidAmount(): Boolean {
    return if (this.isNullOrBlank()) {
        false
    } else {
        if (this.isDouble()) {
            this.toDouble() > 0
        } else {
            false
        }
    }
}

fun Date.toZString() = dateFormat.format(this)!!

fun textFormatter(): TextFormatter<Double> {

    val filter = UnaryOperator<TextFormatter.Change> { c: TextFormatter.Change ->
        val text = c.controlNewText
        if (validEditingState.matcher(text).matches()) {
            return@UnaryOperator c
        } else {
            return@UnaryOperator null
        }
    }
    val converter: StringConverter<Double> = object : StringConverter<Double>() {
        override fun fromString(s: String): Double {
            return if (s.isEmpty() || "-" == s || "." == s || "-." == s) {
                0.0
            } else {
                java.lang.Double.valueOf(s)
            }
        }

        override fun toString(d: Double): String {
            return d.toString()
        }
    }
    return TextFormatter(converter, 0.0, filter)
}

fun Double.calcFee(): Double = this + this * feePercent