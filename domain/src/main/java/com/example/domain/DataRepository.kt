package com.example.domain

import kotlinx.coroutines.flow.Flow

interface DataRepository {

    suspend fun loadData()

    //TODO

}