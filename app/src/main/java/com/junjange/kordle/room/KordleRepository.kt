package com.junjange.kordle.room

import android.app.Application
import androidx.lifecycle.LiveData

class KordleRepository(application : Application) {


    // Room Dao
    private val kordleDao = KordleDatabase.getInstance(application)!!.kordleDao()

    // Use Room
    fun roomGetAllKordle(): LiveData<List<KordleEntity>> {
        return kordleDao.getAll()
    }
//    suspend fun roomInsertKordle(kordleEntity: KordleEntity) {
//        kordleDao.insert(kordleEntity)
//    }

    // singleton pattern
    companion object {
        private var instance: KordleRepository? = null

        fun getInstance(application : Application): KordleRepository? {
            if (instance == null) instance = KordleRepository(application)
            return instance
        }
    }



}