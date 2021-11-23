package com.example.data.roomDB.entities.buildingItem.structuralObjectItem

import com.example.data.model.StructuralObjectItemJson
import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.iconItem.IconItemJsonMapper

class StructuralObjectItemJsonMapper {

    fun fromJsonToRoomDB(itemJson: StructuralObjectItemJson?) : StructuralObjectItemDB?
    {
        if (itemJson == null) {
            return null
        }
        else {
            val categoryName: String? = if (itemJson.category == null) {
                null
            } else {
                itemJson.category!!.name
            }

            return StructuralObjectItemDB(StructuralObjectItemEntityDB(itemJson.id!!,
                    itemJson.subdivision,
                    itemJson.description,
                    itemJson.website,
                    itemJson.buildingId,
                    categoryName),
                itemJson.buildingId,
                IconItemJsonMapper().fromJsonToRoomDB(itemJson.icon, itemJson.id))
        }
    }

}
