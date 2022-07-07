package com.junjange.kordle.room

import android.app.Application

class KordleRepository(application : Application) {

    // singleton pattern
    companion object {
        private var instance: KordleRepository? = null

        fun getInstance(application : Application): KordleRepository? {
            if (instance == null) instance = KordleRepository(application)
            return instance
        }
    }



}