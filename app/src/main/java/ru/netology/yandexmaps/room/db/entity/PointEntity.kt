package ru.netology.yandexmaps.room.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val text: String,
)