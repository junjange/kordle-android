package com.junjange.kordle.room

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.junjange.kordle.data.AnswerCnt
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


@Database(entities = [KordleEntity::class], version = 16, exportSchema = false)
abstract class KordleDatabase : RoomDatabase(){
    abstract fun kordleDao(): KordleDao
    // 데이터 베이스 인스턴스를 싱글톤으로 사용하기 위해 companion object 사용
    companion object {
        private const val DB_NAME = "KordelDatabase"
        private var instance: KordleDatabase? = null

        fun getInstance(application : Application): KordleDatabase? { // singleton pattern
            if (instance == null) {
                synchronized(this){
                    instance = Room.databaseBuilder(application, KordleDatabase::class.java, DB_NAME).fallbackToDestructiveMigration().addCallback(dbCallback).build()
                }
            }
            return instance
        }
        // DB 초기 값
        private var dbCallback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)




                Executors.newSingleThreadExecutor()
                    .execute { db.execSQL("insert into Kordle values(0, 1658588400000, 0, 0, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0)")}
            }

        }
    }

}

//var currentProblems: Boolean,
//var allProblemsCnt: Int,
//var correctAnswerRate: Double,
//var currentWinningStreak: Int,
//var mostWinStreak: Int,
//@Embedded
//var AnswerCnt: AnswerCnt
