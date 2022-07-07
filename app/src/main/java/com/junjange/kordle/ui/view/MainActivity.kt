package com.junjange.kordle.ui.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.junjange.kordle.R
import com.junjange.kordle.databinding.ActivityMainBinding
import com.junjange.kordle.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this, MainViewModel.Factory(application))[MainViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // 정답 문자 리스트
        val currectText = arrayListOf("\u3147", "\u3163","\u3142", "\u3145", "\u315c","\u3139")
        var target2 = arrayListOf<String>("ㅂ","ㅈ","ㅕ",
            "ㅓ",
            "ㅡ","ㅑ",
            "ㅏ","ㅣ",
            "ㄴ",
            "ㅌ","ㄷ","ㅛ",
            "ㅗ",
            "ㅜ",
            "ㅇ",
            "ㅊ","ㄱ",
            "ㄹ",
            "ㅍ","ㅅ",
            "ㅎ",
            "ㅠ",
            "ㅁ",
            "ㅋ")

        // 키보드 박스
        val box = arrayOf(arrayOf(binding.box0,  binding.box1,  binding.box2,  binding.box3,  binding.box4,  binding.box5),
            arrayOf(binding.box6,  binding.box7,  binding.box8,  binding.box9,  binding.box10,  binding.box11),
            arrayOf(binding.box12,  binding.box13,  binding.box14,  binding.box15,  binding.box16,  binding.box17),
            arrayOf(binding.box18,  binding.box19,  binding.box20,  binding.box21,  binding.box22,  binding.box23),
            arrayOf(binding.box24,  binding.box25,  binding.box26,  binding.box27,  binding.box28,  binding.box29),
            arrayOf(binding.box30,  binding.box31,  binding.box32,  binding.box33,  binding.box34,  binding.box35))

        // 키보드 박스 텍스트
        val boxText = arrayOf(arrayOf(binding.box0Text,  binding.box1Text,  binding.box2Text,  binding.box3Text,  binding.box4Text,  binding.box5Text),
            arrayOf(binding.box6Text,  binding.box7Text,  binding.box8Text,  binding.box9Text,  binding.box10Text,  binding.box11Text),
            arrayOf(binding.box12Text,  binding.box13Text,  binding.box14Text,  binding.box15Text,  binding.box16Text,  binding.box17Text),
            arrayOf(binding.box18Text,  binding.box19Text,  binding.box20Text,  binding.box21Text,  binding.box22Text,  binding.box23Text),
            arrayOf(binding.box24Text,  binding.box25Text,  binding.box26Text,  binding.box27Text,  binding.box28Text,  binding.box29Text),
            arrayOf(binding.box30Text,  binding.box31Text,  binding.box32Text,  binding.box33Text,  binding.box34Text,  binding.box35Text))


        val line = Array(6){ i -> false} // 각 라인별 해결유무
        var currentLine : Int = 0 // 현재 라인
        var currentBox : Int = 0 // 현재 박스 위치

        /**
         * 키보드를 누르면 수행되는 코드
         * viewModel에 keyboardText를 observe하여 boxText에 문자를 입력
         *
         * */
        viewModel.keyboardText.observe(this, {

            if (viewModel.flag.value == false){
                viewModel.keyboardText.value?.let { it ->

                    if(!line[currentLine] && currentBox < 6){
                        boxText[currentLine][currentBox].text = it
                        box[currentLine][currentBox].strokeColor = Color.BLACK

                        box[currentLine][currentBox].animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction {

                            box[currentLine][currentBox].scaleX = 1f
                            box[currentLine][currentBox].scaleY = 1f
                            currentBox++
                        }.start()



                    }else{
                        Toast.makeText(this@MainActivity,"6개의 자음과 모음으로 단어를 확인하세요!", Toast.LENGTH_SHORT).show()
                    }

                }
            }



        })

        /**
         * 삭제를 누르면 수행되는 코드
         * currentLine에 따라 boxText의 currentBox가 ""로 초기화 됨.
         *
         * */
        binding.deleteButton.setOnClickListener{

            if(currentBox in 1..6){
                boxText[currentLine][currentBox-1].text = ""
                box[currentLine][currentBox-1].strokeColor = Color.parseColor("#e2e8f0")
                currentBox--


            }

        }


        /**
         * 입력을 누르면 수행되는 코드
         * currentBox가 6개 이하면 토스트 메시지를 6개 이상이라면 else문 밑에 코드가 수행됨.
         *
         * */
        binding.submitButton.setOnClickListener {
            if(currentBox < 6){
                Toast.makeText(this@MainActivity,"6개의 자음과 모음으로 단어를 입력하세요!", Toast.LENGTH_SHORT).show()


            }else{
                var answer = 0

                // 반복문을 통해 정답 코드를 확인
                for (i in currectText.indices){

                    // 정답 문자 리스트에 boxText[currentLine][i].text가 있다면
                    if (currectText.contains( boxText[currentLine][i].text)){
                        // 정답 문자와 boxText[currentLine][i].text가 같다면 박스에 초록 색상을 입힘
                        if(currectText[i] == boxText[currentLine][i].text.toString()){
                            box[currentLine][i].setCardBackgroundColor(Color.parseColor("#22c55e"))
                            box[currentLine][i].strokeColor = Color.parseColor("#22c55e")
                            answer++



                            // 정답 문자와 boxText[currentLine][i].text가 다르다면 박스에 노란 색상을 입힘
                        }else{
                            box[currentLine][i].setCardBackgroundColor(Color.parseColor("#eab308"))
                            box[currentLine][i].strokeColor = Color.parseColor("#eab308")


                        }

                    // 정답 코드에 boxText[currentLine][i].text가 없기 때문에 박스에 회색 색상을 입힘
                    }else{
                        box[currentLine][i].setCardBackgroundColor(Color.parseColor("#94a3b8"))
                        box[currentLine][i].strokeColor = Color.parseColor("#94a3b8")



                    }
                    boxText[currentLine][i].setTextColor(Color.WHITE)

                }

                if(answer == 6){
                    Log.d("ttt", answer.toString())
                    Toast.makeText(this@MainActivity, "정답입니다!", Toast.LENGTH_SHORT)
                    viewModel.flag.value = true

                }else{

                    line[currentLine] = true // 현재 라인 해결
                    currentLine++ // 다음 라인으로 초기화
                    currentBox = 0 // 현재 박스 위치 초기화

                }



            }

        }


        binding.question.setOnClickListener {
            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            val bottomSheetDialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

        }

    }





    /**
     * 아직 사용하지는 않지만
     * 혹시나 나중에 사용할 수 도 있음.
     *
     * */


    // 유니코드 => 문자열
    private fun convertUnicodeToString(unicodeString: String): String {
        var str: String = unicodeString.split(" ")[0]
        str = str.replace("\\", "")
        val arr = str.split("u").toTypedArray()
        var text = ""
        for (i in 1 until arr.size) {
            val hexVal = arr[i].toInt(16)
            text += hexVal.toChar()
        }

        return text
    }

    // 문자열 => 유니코드
    private fun stringToConvertUnicode(str: String): String {

        var text = ""
        val arr = str.toByteArray(Charsets.UTF_32BE)

        for (b in arr) {
            val st = String.format("%02X", b)
            text += st
        }
        text = "\\u" + text.substring(4 until 8)

        return text
    }


}