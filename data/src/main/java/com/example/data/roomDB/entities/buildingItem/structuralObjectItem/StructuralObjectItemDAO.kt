package com.example.data.roomDB.entities.buildingItem.structuralObjectItem

import androidx.room.*
import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.iconItem.IconItemEntityDB
import kotlinx.coroutines.flow.Flow

@Dao
interface StructuralObjectItemDAO {

    // This operations are needed for entities. We'll use it in more difficult queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStructuralObjectItemEntityDB(structuralItemsEntityDB: StructuralObjectItemEntityDB): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIconItemEntityDB(icon: IconItemEntityDB?): Long
    @Update
    fun updateStructuralObjectItemEntityDB(structuralItemsEntityDB: StructuralObjectItemEntityDB)
    @Update
    fun updateIconItemEntityDB(icon: IconItemEntityDB?)
    @Delete
    fun deleteStructuralObjectItemEntityDB(structuralItemsEntityDB: StructuralObjectItemEntityDB)
    @Delete
    fun deleteIconItemEntityDB(icon: IconItemEntityDB?)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneItem(item: StructuralObjectItemDB): Long {
        val resultValue = insertStructuralObjectItemEntityDB(item.structuralItemsEntityDB)
        insertIconItemEntityDB(item.icon)
        return resultValue
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemsList(items: List<StructuralObjectItemDB>) {
        for (soi: StructuralObjectItemDB in items) {
            insertOneItem(soi)
        }
    }

    @Transaction
    @Update
    suspend fun updateOneItem(item: StructuralObjectItemDB) {
        updateStructuralObjectItemEntityDB(item.structuralItemsEntityDB)
        updateIconItemEntityDB(item.icon)
    }

    @Transaction
    @Update
    suspend fun updateAllItems(items: List<StructuralObjectItemDB>) {
        for (soi: StructuralObjectItemDB in items) {
            updateOneItem(soi)
        }
    }

    @Transaction
    @Delete
    suspend fun deleteOneItem(item: StructuralObjectItemDB, deleteChildLocations: Boolean) {
        if (deleteChildLocations) {
            deleteIconItemEntityDB(item.icon)
        }
        deleteStructuralObjectItemEntityDB(item.structuralItemsEntityDB)
    }

    @Transaction
    @Delete
    suspend fun deleteAllItems(items: List<StructuralObjectItemDB>, deleteChildLocations: Boolean) {
        for (soi: StructuralObjectItemDB in items) {
            deleteOneItem(soi, deleteChildLocations)
        }
    }

    @Transaction
    @Query("SELECT * FROM structuralObjects ORDER BY id")
    fun getAllItems(): Flow<List<StructuralObjectItemDB>>

    @Transaction
    @Query("SELECT * FROM structuralObjects WHERE id = :neededId")
    fun getItemById(neededId: String): Flow<StructuralObjectItemDB>

}