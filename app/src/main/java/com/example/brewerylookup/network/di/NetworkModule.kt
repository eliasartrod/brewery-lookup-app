package com.example.brewerylookup.network.di

import android.content.Context
import com.example.brewerylookup.network.BreweryLookupDataSource
import com.example.brewerylookup.network.NetworkDataSource
import com.example.inventory.common.AppPreferences
import com.example.brewerylookup.network.resultcall.ResultCallAdapterFactory
import com.example.brewerylookup.network.retrofit.BreweryLookupService
import com.example.brewerylookup.network.retrofit.GoogleMapsService
import com.example.inventory.utils.BaseSchedulerProvider
import com.example.inventory.utils.BreweryLookupRetrofit
import com.example.inventory.utils.GoogleMapsRetrofit
import com.google.gson.Gson
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @BreweryLookupRetrofit
    fun provideRetrofit(
        @ApplicationContext context: Context,
        appPreferences: AppPreferences,
        gson: Gson,
        schedulerProvider: BaseSchedulerProvider
    ): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.openbrewerydb.org")
            .client(client)
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(schedulerProvider.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @GoogleMapsRetrofit
    fun provideGoogleMapsRetrofit(
        @ApplicationContext context: Context,
        appPreferences: AppPreferences,
        gson: Gson,
        schedulerProvider: BaseSchedulerProvider
    ): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com")
            .client(client)
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(schedulerProvider.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideGoogleMapsContext(@ApplicationContext context: Context): GeoApiContext {
        return GeoApiContext.Builder()
            .apiKey("AIzaSyA5e_5QRVEqbL3S0PiN9hnBeY0bPO2JGJQ")
            .build()
    }

    @Provides
    fun provideDirectionsApiRequest(geoApiContext: GeoApiContext): DirectionsApiRequest {
        return DirectionsApi.newRequest(geoApiContext)
    }

    @Provides
    fun providesGoogleMapsService(@GoogleMapsRetrofit retrofit: Retrofit): GoogleMapsService {
        return retrofit.create(GoogleMapsService::class.java)
    }

    @Provides
    fun providesBreweryLookupService(@BreweryLookupRetrofit retrofit: Retrofit): BreweryLookupService {
        return retrofit.create(BreweryLookupService::class.java)
    }

    @Provides
    fun provideBreweryLookupDataSource(
        breweryLookupService: BreweryLookupService,
        googleMapsService: GoogleMapsService
    ): NetworkDataSource {
        return BreweryLookupDataSource(breweryLookupService, googleMapsService)
    }
}