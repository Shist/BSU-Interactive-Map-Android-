package com.example.data.repository.mappers

import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.iconItem.IconItemEntityDB
import com.example.domain.IconItem

class IconItemDBMapper {

    fun fromDBToDomain(item: IconItemEntityDB?) : IconItem? {
        return if (item == null) {
            null
        } else {
            IconItem(item.id,
                item.subdivision,
                item.logoPath)
        }
    }

    fun fromDomainToDB(item: IconItem?, structuralObjectItemId: String?) : IconItemEntityDB? {
        return if (item == null) {
            null
        } else {
            IconItemEntityDB(item.id,
                structuralObjectItemId,
                item.subdivision,
                item.logoPath)
        }
    }

}