package com.kreasirumahaplikasi.mahasiswakreasi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kreasirumahaplikasi.mahasiswakreasi.data.model.ClickableModel

@Entity(tableName = "Student")
data class Student(
    @PrimaryKey(autoGenerate = true) @SerializedName("id") var id : Int = 0,
    @SerializedName("name") var name: String = "",
    @SerializedName("nim") var nim: String = ""): ClickableModel()