package com.example.data.repository.mappers

import com.example.data.roomDB.entities.buildingItem.adressItem.AddressItemEntityDB
import com.example.domain.AddressItem

class AddressItemDBMapper {

    fun fromDBToDomain(item: AddressItemEntityDB?) : AddressItem? {
        return if (item == null) {
            null
        } else {
            AddressItem(item.id,
                item.description,
                item.latitude,
                item.longitude)
        }
    }

    fun fromDomainToDB(item: AddressItem?, buildingItemId: String?) : AddressItemEntityDB? {
        return if (item == null) {
            null
        } else {
            AddressItemEntityDB(item.id,
                buildingItemId,
                item.description,
                item.latitude,
                item.longitude)
        }
    }

}