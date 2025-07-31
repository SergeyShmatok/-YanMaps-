package ru.netology.yandexmaps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.yandexmaps.repository.PointRepositoryFun
import ru.netology.yandexmaps.room.db.entity.PointEntity
import javax.inject.Inject


@HiltViewModel
class PointViewModel @Inject constructor(
    private val repository: PointRepositoryFun,
) : ViewModel() {

    private var _pointsData = repository.getPoints


    val pointsData
        get() = _pointsData

//--------------------------------------------------------------------------------------------------

    fun removePoint(latitude: Double, longitude: Double) = viewModelScope.launch {
        repository.removePoint(latitude, longitude)
    }

//--------------------------------------------------------------------------------------------------

    fun save(point: PointEntity) = viewModelScope.launch {

        repository.save(point)

    }

}

//------------------------------------ End