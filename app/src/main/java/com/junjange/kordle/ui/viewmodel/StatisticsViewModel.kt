package com.junjange.kordle.ui.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.junjange.kordle.room.KordleEntity
import com.junjange.kordle.room.KordleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatisticsViewModel(private val repository: KordleRepository) : ViewModel() {

    val roomKordle: LiveData<List<KordleEntity>> = repository.roomGetAllKordle()


    class Factory(private val application : Application) : ViewModelProvider.Factory { // factory pattern
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StatisticsViewModel(KordleRepository.getInstance(application)!!) as T
        }

    }
}