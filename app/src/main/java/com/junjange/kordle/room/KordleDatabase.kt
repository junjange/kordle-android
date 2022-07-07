package com.junjange.kordle.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope


@Database(entities = [KordleEntity::class], version = 1, exportSchema = false)
abstract class KordleDatabase : RoomDatabase(){
    abstract fun kordleDao(): KordleDao
    // 데이터 베이스 인스턴스를 싱글톤으로 사용하기 위해 companion object 사용
    companion object {
        @Volatile
        private var INSTANCE: KordleDatabase? = null
        fun getDatabase(
            context: Context,
            scope: CoroutineScope,
        ): KordleDatabase {
            // Room 인스턴스 생성
            // 데이터 베이스가 갱신될 때 기존의 테이블을 버리고 새로 사용하도록 설정
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KordleDatabase::class.java,
                    "kordle"
                )   .fallbackToDestructiveMigration()
//                    .addCallback(dbCallback)
                    .build()
                INSTANCE = instance
                // 만들어지는 DB 인스턴스는 Repository 에서 호출되어 사용
                // return instance
                instance
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

}