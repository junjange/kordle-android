package com.junjange.kordle.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.junjange.kordle.data.AnswerCnt

@Entity(tableName = "Kordle")
data class KordleEntity(
    @PrimaryKey(autoGenerate = true)// PrimaryKey 를 자동적으로 생성
    var currentProblems: Boolean,
    var correctAnswerRate : Long,
    var currentWinningStreak : Int,
    var mostWinStreak : Int,
    var allProblemsCnt : Int,
    @Embedded var AnswerCnt : AnswerCnt

)