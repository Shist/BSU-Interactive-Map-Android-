package com.example.data.roomDB.entities.buildingItem

import androidx.room.Embedded
import androidx.room.Relation
import com.example.data.roomDB.entities.buildingItem.adressItem.AddressItemEntityDB
import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.StructuralObjectItemDB

data class BuildingItemDB (

    @Embedded
    val buildingItemEntityDB: BuildingItemEntityDB,

    @Relation(
        parentColumn = "id",
        entityColumn = "buildingItemId"
    )
    val structuralObjectEntities: List<StructuralObjectItemDB?>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "buildingItemId"
    )
    val address: AddressItemEntityDB?,

    )