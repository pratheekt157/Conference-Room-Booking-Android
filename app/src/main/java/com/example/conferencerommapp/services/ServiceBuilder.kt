package com.example.conferencerommapp

import android.annotation.SuppressLint
import com.example.conferencerommapp.utils.Constants
import com.example.conferencerommapp.services.ConferenceService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.Gson



@SuppressLint("StaticFieldLeak")
object ServiceBuilder {

    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    // private lateinit var mContext: Context
    private val okHttp: OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(logger)
        .connectTimeout(900, TimeUnit.SECONDS)
        .readTimeout(900, TimeUnit.SECONDS)
        .addNetworkInterceptor { chain -> chain.proceed(chain.request().newBuilder().addHeader("Connection", "close").build()) }
    var gson = GsonBuilder()
            .setLenient()
            .create()

    private val builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(Constants.IP_ADDRESS)
        .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttp.build())
    private val retrofit: Retrofit = builder.build()

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }

    fun getObject(): ConferenceService {
        return buildService(ConferenceService::class.java)
    }

    /**
     * .addInterceptor {
    it.proceed(
    it.request().newBuilder()
    .addHeader("Token", getTokenFromSharedPreference())
    .addHeader("UserId", getUserIdFromSharedPreference()).build()
    )

    }
     */
}
