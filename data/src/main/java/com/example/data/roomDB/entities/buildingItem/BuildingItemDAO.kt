package com.example.data.roomDB.entities.buildingItem

import androidx.room.*
import com.example.data.roomDB.entities.buildingItem.adressItem.AddressItemEntityDB
import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.StructuralObjectItemEntityDB
import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.iconItem.IconItemEntityDB
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildingItemDAO {

    // This operations are needed for entities. We'll use it in more difficult queries
    // =================================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBuildingItemEntityDB(buildingItemEntityDB: BuildingItemEntityDB): Long
    @Update
    fun updateBuildingItemEntityDB(buildingItemEntityDB: BuildingItemEntityDB)
    @Delete
    fun deleteBuildingItemEntityDB(buildingItemEntityDB: BuildingItemEntityDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStructuralObjectItemEntityDB(structuralItemsEntityDB: StructuralObjectItemEntityDB): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStructuralObjectItemEntitiesListDB(items: List<StructuralObjectItemEntityDB>): List<Long> {
        val resultValue = emptyList<Long>().toMutableList()
        for (soi: StructuralObjectItemEntityDB in items) {
            resultValue += insertStructuralObjectItemEntityDB(soi)
        }
        return resultValue.toList()
    }
    @Update
    fun updateStructuralObjectItemEntityDB(structuralItemsEntityDB: StructuralObjectItemEntityDB)
    @Update
    fun updateStructuralObjectItemEntitiesListDB(items: List<StructuralObjectItemEntityDB>) {
        for (soi: StructuralObjectItemEntityDB in items) {
            updateStructuralObjectItemEntityDB(soi)
        }
    }
    @Delete
    fun deleteStructuralObjectItemEntityDB(structuralItemsEntityDB: StructuralObjectItemEntityDB)
    @Delete
    fun deleteStructuralObjectItemEntitiesListDB(items: List<StructuralObjectItemEntityDB>) {
        for (soi: StructuralObjectItemEntityDB in items) {
            deleteStructuralObjectItemEntityDB(soi)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIconItemEntityDB(icon: IconItemEntityDB?): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIconItemEntitiesListDB(items: List<IconItemEntityDB>): List<Long> {
        val resultValue = emptyList<Long>().toMutableList()
        for (ii: IconItemEntityDB in items) {
            resultValue += insertIconItemEntityDB(ii)
        }
        return resultValue.toList()
    }
    @Update
    fun updateIconItemEntityDB(icon: IconItemEntityDB?)
    @Update
    fun updateIconItemEntitiesListDB(items: List<IconItemEntityDB>) {
        for (ii: IconItemEntityDB in items) {
            updateIconItemEntityDB(ii)
        }
    }
    @Delete
    fun deleteIconItemEntityDB(icon: IconItemEntityDB?)
    @Delete
    fun deleteIconItemEntitiesListDB(items: List<IconItemEntityDB>) {
        for (ii: IconItemEntityDB in items) {
            deleteIconItemEntityDB(ii)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddressItemEntityDB(address: AddressItemEntityDB?): Long
    @Update
    fun updateAddressItemEntityDB(address: AddressItemEntityDB?)
    @Delete
    fun deleteAddressItemEntityDB(address: AddressItemEntityDB?)
    // =================================

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneBuildingItem(item: BuildingItemDB): Long {
        val resultValue = insertBuildingItemEntityDB(item.buildingItemEntityDB)
        insertStructuralObjectItemEntitiesListDB(item.structuralObjectEntities)
        insertIconItemEntitiesListDB(item.iconEntities)
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
        updateStructuralObjectItemEntitiesListDB(item.structuralObjectEntities)
        updateIconItemEntitiesListDB(item.iconEntities)
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
            deleteStructuralObjectItemEntitiesListDB(item.structuralObjectEntities)
            deleteIconItemEntitiesListDB(item.iconEntities)
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