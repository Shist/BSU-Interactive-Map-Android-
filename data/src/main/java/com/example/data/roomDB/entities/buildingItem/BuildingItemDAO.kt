package com.example.data.roomDB.entities.buildingItem

import androidx.room.*
import com.example.data.roomDB.entities.buildingItem.adressItem.AddressItemEntityDB
import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.StructuralObjectItemDAO
import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.StructuralObjectItemDB
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildingItemDAO {

    // This operations are needed for entities. We'll use it in more difficult queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBuildingItemEntityDB(buildingItemEntityDB: BuildingItemEntityDB): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStructuralObjectEntities(structuralObjectEntities: List<StructuralObjectItemDB?>?): List<Long>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddressItemEntityDB(address: AddressItemEntityDB?): Long
    @Update
    fun updateBuildingItemEntityDB(buildingItemEntityDB: BuildingItemEntityDB)
    @Update
    fun updateStructuralObjectEntities(structuralObjectEntities: List<StructuralObjectItemDB?>?)
    @Update
    fun updateAddressItemEntityDB(address: AddressItemEntityDB?)
    @Delete
    fun deleteBuildingItemEntityDB(buildingItemEntityDB: BuildingItemEntityDB)
    @Delete
    fun deleteStructuralObjectEntities(structuralObjectEntities: List<StructuralObjectItemDB?>?)
    @Delete
    fun deleteAddressItemEntityDB(address: AddressItemEntityDB?)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneBuildingItem(item: BuildingItemDB): Long {
        val resultValue = insertBuildingItemEntityDB(item.buildingItemEntityDB)
        insertStructuralObjectEntities(item.structuralObjectEntities)
        insertAddressItemEntityDB(item.address)
        return resultValue
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuildingItemsList(items: List<BuildingItemDB>) {
        for (bi: BuildingItemDB in items) {
            insertOneBuildingItem(bi)
        }
    }

    @Transaction
    @Update
    suspend fun updateOneBuildingItem(item: BuildingItemDB) {
        updateBuildingItemEntityDB(item.buildingItemEntityDB)
        updateStructuralObjectEntities(item.structuralObjectEntities)
        updateAddressItemEntityDB(item.address)
    }

    @Transaction
    @Update
    suspend fun updateAllBuildingItems(items: List<BuildingItemDB>) {
        for (bi: BuildingItemDB in items) {
            updateOneBuildingItem(bi)
        }
    }

    @Transaction
    @Delete
    suspend fun deleteOneBuildingItem(item: BuildingItemDB, deleteChildLocations: Boolean) {
        if (deleteChildLocations) {
            deleteStructuralObjectEntities(item.structuralObjectEntities)
            deleteAddressItemEntityDB(item.address)
        }
        deleteBuildingItemEntityDB(item.buildingItemEntityDB)
    }

    @Transaction
    @Delete
    suspend fun deleteAllBuildingItems(items: List<BuildingItemDB>, deleteChildLocations: Boolean) {
        for (bi: BuildingItemDB in items) {
            deleteOneBuildingItem(bi, deleteChildLocations)
        }
    }

    @Transaction
    @Query("SELECT * FROM buildings ORDER BY id")
    fun getAllBuildingItems(): Flow<List<BuildingItemDB>>

    @Transaction
    @Query("SELECT * FROM buildings WHERE id = :neededId")
    fun getBuildingItemById(neededId: String): Flow<BuildingItemDB>

}