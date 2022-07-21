package com.junjange.kordle.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Kordle")
data class KordleEntity(
    @PrimaryKey(autoGenerate = true)// PrimaryKey 를 자동적으로 생성
    var id : Int,
    var words: String,

)