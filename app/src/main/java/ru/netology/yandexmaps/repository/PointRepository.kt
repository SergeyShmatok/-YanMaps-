package ru.netology.yandexmaps.repository

import ru.netology.yandexmaps.room.db.dao.PointDao
import ru.netology.yandexmaps.room.db.entity.PointEntity
import javax.inject.Inject


class PointRepository @Inject constructor(
    private val dao: PointDao,
) : PointRepositoryFun {

//--------------------------------------------------------------------------------------------------

    override var getPoints = dao.getAll()

    override suspend fun save(point: PointEntity) {
        dao.insert(point)
    }

    override suspend fun removePoint(latitude: Double, longitude: Double) {
        dao.removePoint(latitude, longitude)
    }

    override suspend fun isEmpty(): Boolean = dao.isEmpty()

}


//------------------------------------ End