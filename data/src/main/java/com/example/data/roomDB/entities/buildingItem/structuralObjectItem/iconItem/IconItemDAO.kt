package com.example.data.roomDB.entities.buildingItem.structuralObjectItem.iconItem

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IconItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneItem(item: IconItemEntityDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemsList(items: List<IconItemEntityDB>)

    @Update
    suspend fun updateOneItem(item: IconItemEntityDB)

    @Update
    suspend fun updateAllItems(items: List<IconItemEntityDB>)

    @Delete
    suspend fun deleteOneItem(item: IconItemEntityDB)

    @Delete
    suspend fun deleteAllItems(items: List<IconItemEntityDB>)

    @Query("SELECT * FROM icons ORDER BY id")
    fun getAllItems(): Flow<List<IconItemEntityDB>>

    @Query("SELECT * FROM icons WHERE id = :neededId")
    fun getItemById(neededId: String): Flow<IconItemEntityDB>

}