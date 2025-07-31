package ru.netology.yandexmaps.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.yandexmaps.room.db.entity.PointEntity

interface PointRepositoryFun {
    var getPoints: Flow<List<PointEntity>>
    suspend fun save(point: PointEntity)
    suspend fun removePoint(latitude: Double, longitude: Double)
    suspend fun isEmpty(): Boolean

}




