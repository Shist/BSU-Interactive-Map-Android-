package com.example.data.repository.mappers

import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.iconItem.IconItemEntityDB
import com.example.domain.IconItem

// This mapper converts a database entity to a domain entity
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

}