package com.whyk.chatrpg

import com.google.gson.annotations.SerializedName


data class SheetReturn(
    @SerializedName("range")
    var range: String,
    @SerializedName("majorDimension")
    var majorDimension: String,
    @SerializedName("values")
    var vaules:ArrayList<Place>)

data class Place(
    var Name: String,
    var Contents: String
)