package com.example.data.roomDB.entities.buildingItem.structuralObjectItem

import androidx.room.*
import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.iconItem.IconItemDAO
import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.iconItem.IconItemEntityDB
import kotlinx.coroutines.flow.Flow

@Dao
interface StructuralObjectItemDAO : IconItemDAO {

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
    suspend fun insertOneStructuralObjectItem(item: StructuralObjectItemDB): Long {
        val resultValue = insertStructuralObjectItemEntityDB(item.structuralItemsEntityDB)
        insertIconItemEntityDB(item.icon)
        return resultValue
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStructuralObjectItemsList(items: List<StructuralObjectItemDB>) {
        for (soi: StructuralObjectItemDB in items) {
            insertOneStructuralObjectItem(soi)
        }
    }

    @Transaction
    @Update
    suspend fun updateOneStructuralObjectItem(item: StructuralObjectItemDB) {
        updateStructuralObjectItemEntityDB(item.structuralItemsEntityDB)
        updateIconItemEntityDB(item.icon)
    }

    @Transaction
    @Update
    suspend fun updateAllStructuralObjectItems(items: List<StructuralObjectItemDB>) {
        for (soi: StructuralObjectItemDB in items) {
            updateOneStructuralObjectItem(soi)
        }
    }

    @Transaction
    @Delete
    suspend fun deleteOneStructuralObjectItem(item: StructuralObjectItemDB, deleteChildLocations: Boolean) {
        if (deleteChildLocations) {
            deleteIconItemEntityDB(item.icon)
        }
        deleteStructuralObjectItemEntityDB(item.structuralItemsEntityDB)
    }

    @Transaction
    @Delete
    suspend fun deleteAllStructuralObjectItems(items: List<StructuralObjectItemDB>, deleteChildLocations: Boolean) {
        for (soi: StructuralObjectItemDB in items) {
            deleteOneStructuralObjectItem(soi, deleteChildLocations)
        }
    }

    @Transaction
    @Query("SELECT * FROM structuralObjects ORDER BY id")
    fun getAllStructuralObjectItems(): Flow<List<StructuralObjectItemDB>>

    @Transaction
    @Query("SELECT * FROM structuralObjects WHERE id = :neededId")
    fun getStructuralObjectItemById(neededId: String): Flow<StructuralObjectItemDB>

}