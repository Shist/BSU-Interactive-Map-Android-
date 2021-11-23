package com.example.data.roomDB.entities.buildingItem.adressItem

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneItem(item: AddressItemEntityDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemsList(items: List<AddressItemEntityDB>)

    @Update
    suspend fun updateOneItem(item: AddressItemEntityDB)

    @Update
    suspend fun updateAllItems(items: List<AddressItemEntityDB>)

    @Delete
    suspend fun deleteOneItem(item: AddressItemEntityDB)

    @Delete
    suspend fun deleteAllItems(items: List<AddressItemEntityDB>)

    @Query("SELECT * FROM addresses ORDER BY id")
    fun getAllItems(): Flow<List<AddressItemEntityDB>>

    @Query("SELECT * FROM addresses WHERE id = :neededId")
    fun getItemById(neededId: String): Flow<AddressItemEntityDB>

}