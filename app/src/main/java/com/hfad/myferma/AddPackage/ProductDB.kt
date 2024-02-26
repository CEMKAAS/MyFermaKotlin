package com.hfad.myferma.AddPackage


import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import kotlinx.android.parcel.Parcelize

@Parcelize
class ProductDB(var id: Int, var name: String, var disc: Double, var data: String, var price: Int) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }
    companion object {
    }
}