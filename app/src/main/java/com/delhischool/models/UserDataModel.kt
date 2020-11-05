package com.delhischool.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "userData")
data class UserDataModel(
        @PrimaryKey
        var userId: String,
        var userName: String,
        var className: String,
        var classSection: String,
        var isTeacher: Boolean,
        @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
        var image: ByteArray
) : Parcelable