package com.example.conferencerommapp.common.di

import android.content.Context
import com.example.conferencerommapp.BaseApplication
import com.example.conferencerommapp.Repository.BuildingsRepository
import com.example.conferencerommapp.Repository.ConferenceRoomRepository
import com.example.conferencerommapp.utils.Constants
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.text.SimpleDateFormat
import javax.inject.Singleton

@Singleton
@Module
class AppModule(private val application: BaseApplication) {
    private var objectMapper: ObjectMapper? = null
    private var mHttpLoggingInterceptor: HttpLoggingInterceptor? = null
    private var builder: OkHttpClient.Builder? = null

    /**
     * This method will return the application context
     *
     * @return Application instance mmjpApplication
     */
    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }


    /**
     * This method will return the application instance
     *
     * @return applications instance mmjpApplication
     */

    @Singleton
    @Provides
    fun provideApplication(): BaseApplication {
        return application
    }


    /**
     * this method gives the retrofit instance to the application
     *
     * @return Retrofit object
     */

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        builder = provideOkHttpClient()
        builder!!.addInterceptor(provideHttpLoggingInterceptorInstance())
        return Retrofit.Builder()
            .client(builder!!.build())
            .addConverterFactory(JacksonConverterFactory.create(providesJacksonFactoryInstance()))
            .baseUrl(Constants.IP_ADDRESS)
            .build()

    }


    /**
     * This method returns the httpclient for logging requestURLs
     */

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }


    /**
     * This method returns object mapper instance
     */
    @Singleton
    @Provides
    fun provideObjectMapper(): ObjectMapper {
        return ObjectMapper()
    }

    /**
     * This method returns object mapper instance
     */
    @Singleton
    @Provides
    fun providesJacksonFactoryInstance(): ObjectMapper {
        objectMapper = provideObjectMapper()
        objectMapper!!.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        objectMapper!!.dateFormat = providesSimpleDateFormatInstance()
        return objectMapper!!
    }


    /**
     * This method provides a simpleDateFormat instance
     *
     * @returnRouteSuggestionsRepository
     */
    @Provides
    fun providesSimpleDateFormatInstance(): SimpleDateFormat {
        return SimpleDateFormat()
    }


    /**
     * This method provides Httplogging Interceptor instance
     */

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptorInstance(): HttpLoggingInterceptor {
        mHttpLoggingInterceptor = HttpLoggingInterceptor()
        mHttpLoggingInterceptor!!.level = HttpLoggingInterceptor.Level.BODY
        return mHttpLoggingInterceptor!!
    }

    @Provides
    @Singleton
    fun provideGetBuildingRepository(): BuildingsRepository {
        return BuildingsRepository()
    }

    @Provides
    @Singleton
    fun provideGetConferenceRoomRepository(): ConferenceRoomRepository {
        return ConferenceRoomRepository()
    }
}