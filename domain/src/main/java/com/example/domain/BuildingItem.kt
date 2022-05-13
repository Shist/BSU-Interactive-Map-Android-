package com.example.domain

import android.os.Parcel
import android.os.Parcelable

data class BuildingItem(

    val id: String,

    val structuralObjects: List<StructuralObjectItem?>?,

    val inventoryUsrreNumber: String?,

    val name: String?,

    val isModern: Boolean?,

    val address: AddressItem?,

    val type: String?,

    val markerPath: String?

)  : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createTypedArrayList(StructuralObjectItem),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readParcelable(AddressItem::class.java.classLoader),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeTypedList(structuralObjects)
        parcel.writeString(inventoryUsrreNumber)
        parcel.writeString(name)
        parcel.writeValue(isModern)
        parcel.writeParcelable(address, flags)
        parcel.writeString(type)
        parcel.writeString(markerPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BuildingItem> {
        override fun createFromParcel(parcel: Parcel): BuildingItem {
            return BuildingItem(parcel)
        }

        override fun newArray(size: Int): Array<BuildingItem?> {
            return arrayOfNulls(size)
        }
    }
}

// This is domain data class. Actually this is copy of corresponding BuildingItemDB, but we
// need it in order to hide the database implementation logic. It is considered good practice
// to have separate classes for the database and classes for the domain.
