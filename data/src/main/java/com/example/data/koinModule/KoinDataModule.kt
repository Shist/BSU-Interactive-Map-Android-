package com.example.data.koinModule

import android.content.Context
import android.provider.ContactsContract
import androidx.room.Room
import com.example.data.repository.DataRepositoryImpl
import com.example.data.retrofit.RetrofitClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import com.example.domain.DataRepository

val dataModule = module {

    single<DataRepository> {
        DataRepositoryImpl(

        )
    }

    single { RetrofitClient(client = get()) }

    single {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

}