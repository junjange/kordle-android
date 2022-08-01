package com.junjange.kordle.ui.view.dialog

import android.annotation.SuppressLint
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

        // 공유 하기
        binding.share.setOnClickListener {
            val message = "한글은 위대하다!\n친구들에게 훈들을 공유해요~ ${String(Character.toChars(0x1F917))}\n\n(구글 스토어 주소)"
            share(message)
        }

        // 평가 하기
        binding.evaluation.setOnClickListener {
            Toast.makeText(this, "서비스 준비중입니다.", Toast.LENGTH_SHORT).show()


        }


    }

    @SuppressLint("QueryPermissionsNeeded")
    fun share(content: String) {
        val intent = Intent(Intent.ACTION_SEND) // 공유하는 인텐트 생성
            .apply {
                type = "text/plain" // 데이터 타입 설정
                putExtra(Intent.EXTRA_TEXT, content) // 보낼 내용 설정
            }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intent, "초대 메세지 보내기"))
        } else {
            Toast.makeText(this, "초대 메세지를 전송할 수 없습니다", Toast.LENGTH_LONG).show()
        }
    }
}