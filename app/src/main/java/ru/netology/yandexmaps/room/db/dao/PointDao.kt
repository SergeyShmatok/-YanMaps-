package ru.netology.yandexmaps.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.yandexmaps.room.db.entity.PointEntity

@Dao
interface PointDao {

    @Query("SELECT * FROM PointEntity")
    fun getAll(): Flow<List<PointEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(point: PointEntity)

    @Query("DELETE FROM PointEntity WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun removePoint(latitude: Double, longitude: Double)

    @Query("SELECT COUNT(*) == 0 FROM PointEntity")
    suspend fun isEmpty(): Boolean
}
