package com.whyk.chatrpg

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

//key : AIzaSyAaYfeUuYuanVSsbsUHZZeHxSUqM4cdHOQ
//sheetid : 1k9cSK7iOqHOD1RzzYR0sW8iM0wz14Vk6Z4Jko5LimOQ
interface ApiInterface {
    //@GET("{SheetId}/values/{Range}?key={API Key}")
    @GET("1k9cSK7iOqHOD1RzzYR0sW8iM0wz14Vk6Z4Jko5LimOQ/values/Place!A1:B11?key=AIzaSyAaYfeUuYuanVSsbsUHZZeHxSUqM4cdHOQ")
    fun getPlace() : Call<SheetReturn>
}

internal object APIClient {
    lateinit var retrofit: Retrofit

    val client: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(2,TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build()
            retrofit = Retrofit.Builder()
                .baseUrl("https://sheets.googleapis.com/v4/spreadsheets/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit
        }
}