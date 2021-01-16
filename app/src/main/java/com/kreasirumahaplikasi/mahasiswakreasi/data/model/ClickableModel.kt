package com.kreasirumahaplikasi.mahasiswakreasi.data.model

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

open class ClickableModel(@Ignore @SerializedName("isClicked") var isClicked: Boolean = false) {
}