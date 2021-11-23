package com.example.data.repository.mappers

import com.example.data.roomDB.entities.buildingItem.BuildingItemDB
import com.example.domain.BuildingItem
import com.example.domain.StructuralObjectItem

class BuildingItemDBMapper {

    fun fromDBToDomain(item: BuildingItemDB?) : BuildingItem? {
        if (item == null) {
            return null
        }
        else {
            val structuralObjects: List<StructuralObjectItem?>? =
                if (item.structuralObjectEntities == null) {
                    null
                } else {
                    item.structuralObjectEntities
                        .map { StructuralObjectItemDBMapper().fromDBToDomain(it) }
                }

            return BuildingItem(item.buildingItemEntityDB.id,
                structuralObjects,
                item.buildingItemEntityDB.inventoryUsrreNumber,
                item.buildingItemEntityDB.name,
                item.buildingItemEntityDB.isModern,
                AddressItemDBMapper().fromDBToDomain(item.address),
                item.buildingItemEntityDB.type,
                item.buildingItemEntityDB.markerPath)
        }
    }

}