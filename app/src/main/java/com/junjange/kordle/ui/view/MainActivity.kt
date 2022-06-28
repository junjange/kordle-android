package com.junjange.kordle.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.junjange.kordle.R
import com.junjange.kordle.databinding.ActivityMainBinding
import com.junjange.kordle.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val boxText = arrayOf(arrayOf(binding.box0Text,  binding.box1Text,  binding.box2Text,  binding.box3Text,  binding.box4Text,  binding.box5Text),
            arrayOf(binding.box6Text,  binding.box7Text,  binding.box8Text,  binding.box9Text,  binding.box10Text,  binding.box11Text),
            arrayOf(binding.box12Text,  binding.box13Text,  binding.box14Text,  binding.box15Text,  binding.box16Text,  binding.box17Text),
            arrayOf(binding.box18Text,  binding.box19Text,  binding.box20Text,  binding.box21Text,  binding.box22Text,  binding.box23Text),
            arrayOf(binding.box24Text,  binding.box25Text,  binding.box26Text,  binding.box27Text,  binding.box28Text,  binding.box29Text),
            arrayOf(binding.box30Text,  binding.box31Text,  binding.box32Text,  binding.box33Text,  binding.box34Text,  binding.box35Text))



        val line = Array(6){ i -> false}
        var currentLine : Int = 0
        var currentBox : Int = 0

        viewModel.keyboardText.observe(this, {

            viewModel.keyboardText.value?.let { it ->

                if(!line[currentLine] && currentBox < 6){
                    boxText[currentLine][currentBox].text = it

                    currentBox++

                }else{
                    Toast.makeText(this@MainActivity,"6개의 자음과 모음으로 단어를 확인하세요!", Toast.LENGTH_SHORT).show()
                }

            }

        })

//        binding.qButton.setOnClickListener {
//            Log.d("ttt", currentLine.toString())
//            if(!line[currentLine] && currentBox < 6){
//                boxText[currentLine][currentBox].text = "ㅂ"
//                currentBox++
//
//            }else{
//                Toast.makeText(this@MainActivity,"6개의 자음과 모음으로 단어를 확인하세요!", Toast.LENGTH_SHORT).show()
//
//            }
//        }

        binding.deleteButton.setOnClickListener{
            if(currentBox in 1..6){
                boxText[currentLine][currentBox-1].text = ""
                currentBox--

            }

        }

        binding.submitButton.setOnClickListener {
            if(currentBox < 6){
                Toast.makeText(this@MainActivity,"6개의 자음과 모음으로 단어를 입력하세요!", Toast.LENGTH_SHORT).show()


            }else{
                line[currentLine] = true
                currentLine++
                currentBox = 0
            }

        }



    }


}