package com.example.conferencerommapp.services

import android.util.Log
import com.example.conferencerommapp.utils.Constants
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClient {
    companion object {
        private const val TAG = "RestClient"
        private var service: ConferenceService? = null
        private val clientLogin = OkHttpClient.Builder()
        private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        var gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        init {
            try {
                clientLogin.addInterceptor(logger)
                clientLogin.readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                clientLogin.addInterceptor { chain ->
                    var request = chain.request()
                    request = request.newBuilder()
                        .build()
                    chain.proceed(request)
                }
            } catch (e: UnsupportedOperationException) {
                Log.e(TAG, e.toString())
            }
        }
        fun getWebServiceData(): ConferenceService? {
            val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(Constants.IP_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            service = retrofit.create<ConferenceService>(ConferenceService::class.java)
            return service
        }
    }
}
