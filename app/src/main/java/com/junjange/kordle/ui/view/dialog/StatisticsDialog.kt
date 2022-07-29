package com.junjange.kordle.ui.view.dialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.junjange.kordle.data.AnswerCnt
import com.junjange.kordle.databinding.ActivityStatisticsDialogBinding
import com.junjange.kordle.room.KordleEntity
import com.junjange.kordle.ui.viewmodel.StatisticsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatisticsDialog : AppCompatActivity() {
    private val binding by lazy { ActivityStatisticsDialogBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this, StatisticsViewModel.Factory(application))[StatisticsViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setObserver()

        setSupportActionBar(binding.mainToolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게


        binding.clear.setOnClickListener {

            finish()
        }




    }

    private fun setObserver() {
            viewModel.roomKordle.observe(this@StatisticsDialog, {
                Log.d("ttt", it.toString())
                binding.allProblemsCnt.text = it[0].allProblemsCnt.toString()
                binding.correctAnswerRate.text = it[0].correctAnswerRate.toString() + "%"
                binding.currentWinningStreak.text = it[0].currentWinningStreak.toString()
                binding.mostWinStreak.text = it[0].mostWinStreak.toString()
                binding.AnswerCnt1.text = it[0].AnswerCnt.one.toString()
                binding.AnswerCnt2.text = it[0].AnswerCnt.two.toString()
                binding.AnswerCnt3.text = it[0].AnswerCnt.three.toString()
                binding.AnswerCnt4.text = it[0].AnswerCnt.four.toString()
                binding.AnswerCnt5.text = it[0].AnswerCnt.five.toString()
                binding.AnswerCnt6.text = it[0].AnswerCnt.six.toString()

                (binding.AnswerCnt1.layoutParams as LinearLayout.LayoutParams).weight = it[0].AnswerCnt.one.toFloat()
                (binding.AnswerCnt1Empty.layoutParams as LinearLayout.LayoutParams).weight = (it[0].solveProblemsCnt - it[0].AnswerCnt.one).toFloat()

                (binding.AnswerCnt2.layoutParams as LinearLayout.LayoutParams).weight = it[0].AnswerCnt.two.toFloat()
                (binding.AnswerCnt2Empty.layoutParams as LinearLayout.LayoutParams).weight = (it[0].solveProblemsCnt - it[0].AnswerCnt.two).toFloat()

                (binding.AnswerCnt3.layoutParams as LinearLayout.LayoutParams).weight = it[0].AnswerCnt.three.toFloat()
                (binding.AnswerCnt3Empty.layoutParams as LinearLayout.LayoutParams).weight = (it[0].solveProblemsCnt - it[0].AnswerCnt.three).toFloat()

                (binding.AnswerCnt4.layoutParams as LinearLayout.LayoutParams).weight = it[0].AnswerCnt.four.toFloat()
                (binding.AnswerCnt4Empty.layoutParams as LinearLayout.LayoutParams).weight = (it[0].solveProblemsCnt - it[0].AnswerCnt.four).toFloat()

                (binding.AnswerCnt5.layoutParams as LinearLayout.LayoutParams).weight = it[0].AnswerCnt.five.toFloat()
                (binding.AnswerCnt5Empty.layoutParams as LinearLayout.LayoutParams).weight = (it[0].solveProblemsCnt - it[0].AnswerCnt.five).toFloat()

                (binding.AnswerCnt6.layoutParams as LinearLayout.LayoutParams).weight = it[0].AnswerCnt.six.toFloat()
                (binding.AnswerCnt6Empty.layoutParams as LinearLayout.LayoutParams).weight = (it[0].solveProblemsCnt - it[0].AnswerCnt.six).toFloat()





            })

    }
}