package org.d3if3134.currencycalculator.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3134.currencycalculator.model.CurrencyCode
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://bunnyeee.my.id/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CurrencyApiService {
    @GET("api-currency.json")
    suspend fun getCurrency(): List<CurrencyCode>
}

object CurrencyApi {
    val service: CurrencyApiService by lazy {
        retrofit.create(CurrencyApiService::class.java)
    }
}

enum class ApiStatus { LOADING,  SUCCESS , FAILED}