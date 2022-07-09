package com.junjange.kordle.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface KordleDao {
    // 데이터 베이스 불러오기
    @Query("SELECT * from Kordle")
    fun getAll(): LiveData<List<KordleEntity>>

    // 데이터 추가
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//   suspend fun insert(kordleEntity: KordleEntity)

    // 데이터 전체 삭제
    @Query("DELETE FROM Kordle")
    fun deleteAll()

    // 데이터 업데이트
    @Update
    fun update(kordleEntity: KordleEntity)

    // 데이터 삭제
    @Delete
    fun delete(kordleEntity: KordleEntity)


}