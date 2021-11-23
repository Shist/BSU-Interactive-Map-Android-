package com.example.data.repository

import com.example.data.model.BuildingItemJson
import com.example.data.repository.mappers.AddressItemDBMapper
import com.example.data.repository.mappers.BuildingItemDBMapper
import com.example.data.repository.mappers.IconItemDBMapper
import com.example.data.repository.mappers.StructuralObjectItemDBMapper
import com.example.data.retrofit.MapDataApi
import com.example.data.roomDB.BuildingItemsDatabase
import com.example.data.roomDB.entities.buildingItem.BuildingItemDB
import com.example.data.roomDB.entities.buildingItem.BuildingItemJsonMapper
import com.example.data.roomDB.entities.buildingItem.adressItem.AddressItemJsonMapper
import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.StructuralObjectItemJsonMapper
import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.iconItem.IconItemJsonMapper
import com.example.domain.BuildingItem
import com.example.domain.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataRepositoryImpl(private val buildingItemsDatabase: BuildingItemsDatabase,
                         private val service: MapDataApi,
                         private val buildingItemJsonMapper: BuildingItemJsonMapper,
                         private val buildingItemDBMapper: BuildingItemDBMapper,
                         private val structuralObjectItemJsonMapper: StructuralObjectItemJsonMapper,
                         private val structuralObjectItemDBMapper: StructuralObjectItemDBMapper,
                         private val addressItemJsonMapper: AddressItemJsonMapper,
                         private val addressItemDBMapper: AddressItemDBMapper,
                         private val iconItemJsonMapper: IconItemJsonMapper,
                         private val iconItemDBMapper: IconItemDBMapper
) : DataRepository {

    private fun isItemWithID(itemJson: BuildingItemJson?): Boolean {
        return if (itemJson == null) {
            false
        } else {
            itemJson.id != null
        }
    }

    private fun isItemNotEmpty(item: BuildingItemDB?): Boolean {
        return if (item == null)
            false
        else {
            item.buildingItemEntityDB.inventoryUsrreNumber != null ||
                    item.buildingItemEntityDB.name != null ||
                    item.buildingItemEntityDB.isModern != null ||
                    item.buildingItemEntityDB.type != null ||
                    item.buildingItemEntityDB.markerPath != null ||
                    item.structuralObjectEntities != null ||
                    item.address != null
        }
    }

    override suspend fun loadData() {
        try {
            val items = service.getData()
                ?.filter { isItemWithID(it) } // Clean list from items with null id
                ?.map { BuildingItemJsonMapper().fromJsonToRoomDB(it)!! }
                ?.filter { isItemNotEmpty(it) } // Clean DB from items with empty data
            buildingItemsDatabase.buildingItemsDao().insertItemsList(items!!)
        } catch (e: Throwable) {
            throw NullPointerException("Error: " +
                    "Some BuildingItem (or even whole list) from json is empty!\n" + e.message)
        }
    }

    override fun getItems(): Flow<List<BuildingItem>> {
        try {
            return buildingItemsDatabase.buildingItemsDao().getAllItems().map { list ->
                list.map { BuildingItemDBMapper().fromDBToDomain(it)!! } }
        } catch (e: Throwable) {
            throw NullPointerException("Error while getting building items: " +
            "Some item is nullable!\n" + e.message)
        }
    }

}

