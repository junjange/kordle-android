package com.junjange.kordle.ui.view.dialog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.junjange.kordle.R
import com.junjange.kordle.databinding.ActivityExplanationDialogBinding
import com.junjange.kordle.databinding.ActivitySetDialogBinding

class SetDialog : AppCompatActivity() {
    private lateinit var binding: ActivitySetDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_dialog)
        setContentView(binding.root)
        binding.dialog = this
        binding.lifecycleOwner = this



        setSupportActionBar(binding.mainToolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게

        binding.clear.setOnClickListener {
            finish()
        }

        // 풀이 방법
       binding.explanation.setOnClickListener {
            startActivity(Intent(this, ExplanationDialog::class.java))


        }

        // 통계 보기
        binding.statistics.setOnClickListener {
            startActivity(Intent(this, StatisticsDialog::class.java))

        }

        // 평가 하기기
        binding.evaluation.setOnClickListener {
            Toast.makeText(this, "서비스 준비중입니다.", Toast.LENGTH_SHORT).show()


        }


    }
}