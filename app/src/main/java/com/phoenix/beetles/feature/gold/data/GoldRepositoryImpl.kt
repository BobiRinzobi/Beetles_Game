package com.phoenix.beetles.feature.gold.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.phoenix.beetles.feature.gold.data.remote.GoldApi
import com.phoenix.beetles.feature.gold.data.remote.GoldParser
import com.phoenix.beetles.feature.gold.domain.model.GoldPrice
import com.phoenix.beetles.feature.gold.domain.repository.GoldRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class GoldRepositoryImpl(context: Context) : GoldRepository {
    private val preferences: SharedPreferences = context.applicationContext
        .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val api: GoldApi = GoldApi.create()

    override fun getCachedGoldPrice(): GoldPrice? {
        val price = preferences.getString(KEY_PRICE, null)?.toDoubleOrNull() ?: return null
        val date = preferences.getString(KEY_DATE, null) ?: return null
        if (price <= 0.0) return null
        return GoldPrice(pricePerGram = price, date = date)
    }

    override suspend fun refreshGoldPrice(): GoldPrice? = withContext(Dispatchers.IO) {
        try {
            val today = Calendar.getInstance()
            val endDate = DATE_QUERY_FORMAT.format(today.time)
            today.add(Calendar.DAY_OF_YEAR, -30)
            val startDate = DATE_QUERY_FORMAT.format(today.time)

            val parsed = api.getMetalPrices(startDate, endDate).use { body ->
                GoldParser.parse(body.byteStream())
            } ?: run {
            Log.e(
                "GoldPriceRepository",
                "XML получен, но запись золота не найдена"
            )
            return@withContext getCachedGoldPrice()
        }

            preferences.edit()
                .putString(KEY_PRICE, parsed.pricePerGram.toString())
                .putString(KEY_DATE, parsed.date)
                .apply()
            parsed
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (e: Exception) {
            Log.e(
                "GoldPriceRepository",
                "Не удалось загрузить курс золота ЦБ",
                e
            )
            getCachedGoldPrice()
        }
    }

    private companion object {
        const val PREFERENCES_NAME = "cbr_gold_price"
        const val KEY_PRICE = "price_per_gram"
        const val KEY_DATE = "date"
        val DATE_QUERY_FORMAT = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    }
}
