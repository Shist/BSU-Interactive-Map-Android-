package com.example.data.repository.mappers

import com.example.data.roomDB.entities.buildingItem.BuildingItemDB
import com.example.domain.BuildingItem
import com.example.domain.IconItem
import com.example.domain.StructuralObjectItem

class BuildingItemDBMapper {

    fun fromDBToDomain(item: BuildingItemDB?) : BuildingItem? {
        if (item == null) {
            return null
        }
        else {
            val structuralObjects: MutableList<StructuralObjectItem?> =
                emptyList<StructuralObjectItem?>().toMutableList()

            val structuralObjectsEntities = item.structuralObjectEntities
            val iconEntities = item.iconEntities
            for (itemIndex in structuralObjectsEntities.indices)
            {
                val nextItem = StructuralObjectItem(structuralObjectsEntities[itemIndex].id,
                    structuralObjectsEntities[itemIndex].subdivision,
                    structuralObjectsEntities[itemIndex].description,
                    structuralObjectsEntities[itemIndex].website,
                    structuralObjectsEntities[itemIndex].buildingItemId,
                    structuralObjectsEntities[itemIndex].category,
                    IconItem(iconEntities[itemIndex].id,
                        iconEntities[itemIndex].subdivision,
                        iconEntities[itemIndex].logoPath))
                structuralObjects += nextItem
            }
            structuralObjects.toList()

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