package com.junjange.kordle.room

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import java.io.File


@Database(entities = [KordleEntity::class], version = 0, exportSchema = false)
abstract class KordleDatabase : RoomDatabase(){
    abstract fun kordleDao(): KordleDao
    // 데이터 베이스 인스턴스를 싱글톤으로 사용하기 위해 companion object 사용
    companion object {
        private const val DB_NAME = "KordelDatabase"
        private var instance: KordleDatabase? = null

        fun getInstance(application : Application): KordleDatabase? { // singleton pattern
            if (instance == null) {
                synchronized(this){
                    instance = Room.databaseBuilder(application, KordleDatabase::class.java, DB_NAME).build()
                }
            }
            return instance
        }
    }
        // DB 초기 값
//        private var dbCallback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                Executors.newSingleThreadExecutor()
//                    .execute { db.execSQL("insert into pmd values (0, '37.5642135','127.0016985')")}
//            }
//
//        }
    }

