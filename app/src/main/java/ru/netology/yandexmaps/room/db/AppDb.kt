package ru.netology.yandexmaps.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.netology.yandexmaps.room.db.dao.PointDao
import ru.netology.yandexmaps.room.db.entity.PointEntity


@Database(entities = [PointEntity::class], version = 3, exportSchema = false)
// При изменении таблицы (добавление новых колонок)
// нужно изменять версию и устанавливать миграцию (**)
// либо заходить в приложение и всё чистить. Иначе получим exception при запуске.
abstract class AppDb : RoomDatabase() {
    abstract fun pointDao(): PointDao

}