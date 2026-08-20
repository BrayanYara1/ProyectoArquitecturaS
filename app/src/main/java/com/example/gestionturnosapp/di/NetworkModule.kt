package com.example.gestionturnosapp.di

import android.content.Context
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * URL de producción en Render.
     */
    private const val BASE_URL = "https://saludactiva-backend.onrender.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(userManager: UserManager): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request()
                android.util.Log.d("NetworkDebug", "Llamando a: ${request.url}")
                
                val builder = request.newBuilder()
                userManager.token?.let {
                    builder.addHeader("Authorization", "Bearer $it")
                }

                var response = chain.proceed(builder.build())
                var tryCount = 0
                val maxLimit = 2

                // REINTENTO AUTOMÁTICO PARA ERRORES DE GATEWAY O SOBRECARGA (Render)
                while (!response.isSuccessful && 
                    (response.code == 500 || response.code == 502 || response.code == 503 || response.code == 504) && 
                    tryCount < maxLimit) {
                    
                    android.util.Log.w("NetworkRetry", "Reintentando... (${tryCount + 1}) por código: ${response.code}")
                    tryCount++
                    response.close()
                    // Pequeña espera antes de reintentar
                    Thread.sleep(1000 * tryCount.toLong())
                    response = chain.proceed(builder.build())
                }
                response
            }
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
