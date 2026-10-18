package com.phoenix.beetles.feature.gold.data.remote

import android.util.Xml
import com.phoenix.beetles.feature.gold.domain.model.GoldPrice
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

internal object GoldParser {

    private const val GOLD_CODE = "1"
    fun parse(input: InputStream): GoldPrice? {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(input, null)

        var event = parser.eventType
        var latest: GoldPrice? = null
        var latestDateMillis = Long.MIN_VALUE

        while (event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.START_TAG && parser.name.equals("Record", ignoreCase = true)) {
                val code = parser.getAttributeValue(null, "Code")
                val date = parser.getAttributeValue(null, "Date")
                val (buy, sell) = readRecord(parser)

                if (code == GOLD_CODE && date != null) {
                    val price = when {
                        buy != null && sell != null -> (buy + sell) / 2.0
                        sell != null -> sell
                        else -> buy
                    }
                    val dateMillis = parseDate(date)
                    if (price != null && price > 0 && dateMillis != null && dateMillis >= latestDateMillis) {
                        latest = GoldPrice(pricePerGram = price, date = date)
                        latestDateMillis = dateMillis
                    }
                }
            }
            event = parser.next()
        }
        return latest
    }

    private fun readRecord(parser: XmlPullParser): Pair<Double?, Double?> {
        var buy: Double? = null
        var sell: Double? = null
        var event = parser.next()

        while (!(event == XmlPullParser.END_TAG && parser.name.equals("Record", ignoreCase = true)) &&
            event != XmlPullParser.END_DOCUMENT
        ) {
            if (event == XmlPullParser.START_TAG) {
                when (parser.name.lowercase(Locale.ROOT)) {
                    "buy" -> buy = parseNumber(parser.nextText())
                    "sell" -> sell = parseNumber(parser.nextText())
                }
            }
            event = parser.next()
        }
        return buy to sell
    }

    private fun parseNumber(value: String): Double? =
        value.trim()
            .replace("\u00a0", "")
            .replace(" ", "")
            .replace(',', '.')
            .toDoubleOrNull()

    private fun parseDate(value: String): Long? = try {
        SimpleDateFormat("dd.MM.yyyy", Locale.ROOT).apply { isLenient = false }
            .parse(value)?.time
    } catch (_: ParseException) {
        null
    }

}
