package ru.netology.yandexmaps.repository.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.yandexmaps.repository.PointRepository
import ru.netology.yandexmaps.repository.PointRepositoryFun
import ru.netology.yandexmaps.room.db.dao.PointDao
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindsPostRepository(repository: PointRepository): PointRepositoryFun

}





