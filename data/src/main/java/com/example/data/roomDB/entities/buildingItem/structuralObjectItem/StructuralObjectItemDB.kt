package com.example.data.roomDB.entities.buildingItem.structuralObjectItem

import androidx.room.*
import com.example.data.roomDB.entities.buildingItem.structuralObjectItem.iconItem.IconItemEntityDB

data class StructuralObjectItemDB(
    @Embedded
    val structuralItemsEntityDB: StructuralObjectItemEntityDB,

    @ColumnInfo(name = "buildingItemId")
    val buildingItemId: String?,

    @Relation(
        parentColumn = "id",
        entityColumn = "structuralObjectItemId"
    )
    val icon: IconItemEntityDB?,

    )
