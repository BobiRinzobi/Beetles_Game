package com.phoenix.beetles.feature.gold.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.phoenix.beetles.R
import com.phoenix.beetles.feature.gold.data.GoldRepositoryImpl
import com.phoenix.beetles.feature.gold.domain.model.GoldPrice
import com.phoenix.beetles.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class GoldWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        val repository = GoldRepositoryImpl(context.applicationContext)
        updateWidgets(context, appWidgetManager, appWidgetIds, repository.getCachedGoldPrice())

        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                val freshPrice = repository.refreshGoldPrice()
                updateWidgets(context, appWidgetManager, appWidgetIds, freshPrice)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun updateWidgets(
        context: Context,
        manager: AppWidgetManager,
        ids: IntArray,
        price: GoldPrice?,
    ) {
        ids.forEach { widgetId ->
            val views = RemoteViews(context.packageName, R.layout.widget_gold_ball)
            views.setTextViewText(R.id.gold_widget_price, price?.let(::formatPrice) ?: "Нет данных")
            views.setTextViewText(
                R.id.gold_widget_date,
                price?.let { "ЦБ · ${it.date}" } ?: "Курс золота",
            )
            val openApp = PendingIntent.getActivity(
                context,
                4102,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            views.setOnClickPendingIntent(R.id.gold_widget_root, openApp)
            manager.updateAppWidget(widgetId, views)
        }
    }

    private fun formatPrice(price: GoldPrice): String =
        NumberFormat.getNumberInstance(Locale("ru", "RU")).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }.format(price.pricePerGram) + " Р/г"

    companion object {
        fun requestUpdate(context: Context) {
            val intent = Intent(context, GoldWidget::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    AppWidgetManager.getInstance(context).getAppWidgetIds(
                        ComponentName(context, GoldWidget::class.java)
                    ),
                )
            }
            context.sendBroadcast(intent)
        }
    }
}
