package com.phoenix.beetles.feature.gold.data.remote

import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface GoldApi {
    @GET("scripts/xml_metall.asp")
    suspend fun getMetalPrices(
        @Query("date_req1") fromDate: String,
        @Query("date_req2") toDate: String
    ): ResponseBody

    companion object {
        private const val BASE_URL = "https://www.cbr.ru/"

        fun create(): GoldApi {
            val client = okhttp3.OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("User-Agent", "BeetlesGameAndroid/1.0")
                        .build()
                    chain.proceed(request)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .build()
                .create(GoldApi::class.java)
        }
    }
}
