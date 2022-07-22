package com.junjange.kordle.ui.view
import kotlin.random.Random
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.junjange.kordle.R
import com.junjange.kordle.databinding.ActivityMainBinding
import com.junjange.kordle.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this, MainViewModel.Factory(application))[MainViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.mainToolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게

        var eventIdx =  eventDate()
        var kordelWords =  readTextFile()
        var currectText = getWord(kordelWords)
        Log.d("ttt가져온 유니코드 확인", kordelWords.toString())
        Log.d("ttt가져온 유니코드 확인", kordelWords.size.toString())
        Log.d("ttt정답 유니코드 확인", currectText.toString())




        // 텍스트뷰에 현재시간 출력
        val end = 24 * 60 * 60

        // 텍스트뷰에 현재시간 출력
        val start = System.currentTimeMillis()
        Log.d("Ttt123t", start.toString())
        val date = Date(1 * 60 * 24 * 60)
        val mFormat = SimpleDateFormat("HH:mm:ss")

        val time = mFormat.format(date)

        var a = mFormat.format(Date(mFormat.parse("23:59:59").time ))
        var b = mFormat.format(Date(System.currentTimeMillis()))
        Log.d("ttt시간 확인2", (mFormat.format(Date(mFormat.parse("23:59:59").time ))).toString())

        Log.d("ttt시간 확인", (mFormat.format(Date(System.currentTimeMillis()))).toString())
        Log.d("ttt시간 확인333",  mFormat.format(Date( mFormat.parse("23:59:59").time - System.currentTimeMillis())))


        Log.d("Ttt", currectText.joinToString(""))

        // 답안 박스
        val box = arrayOf(arrayOf(binding.box0,  binding.box1,  binding.box2,  binding.box3,  binding.box4,  binding.box5),
            arrayOf(binding.box6,  binding.box7,  binding.box8,  binding.box9,  binding.box10,  binding.box11),
            arrayOf(binding.box12,  binding.box13,  binding.box14,  binding.box15,  binding.box16,  binding.box17),
            arrayOf(binding.box18,  binding.box19,  binding.box20,  binding.box21,  binding.box22,  binding.box23),
            arrayOf(binding.box24,  binding.box25,  binding.box26,  binding.box27,  binding.box28,  binding.box29),
            arrayOf(binding.box30,  binding.box31,  binding.box32,  binding.box33,  binding.box34,  binding.box35))

        // 답안 박스 텍스트
        val boxText = arrayOf(arrayOf(binding.box0Text,  binding.box1Text,  binding.box2Text,  binding.box3Text,  binding.box4Text,  binding.box5Text),
            arrayOf(binding.box6Text,  binding.box7Text,  binding.box8Text,  binding.box9Text,  binding.box10Text,  binding.box11Text),
            arrayOf(binding.box12Text,  binding.box13Text,  binding.box14Text,  binding.box15Text,  binding.box16Text,  binding.box17Text),
            arrayOf(binding.box18Text,  binding.box19Text,  binding.box20Text,  binding.box21Text,  binding.box22Text,  binding.box23Text),
            arrayOf(binding.box24Text,  binding.box25Text,  binding.box26Text,  binding.box27Text,  binding.box28Text,  binding.box29Text),
            arrayOf(binding.box30Text,  binding.box31Text,  binding.box32Text,  binding.box33Text,  binding.box34Text,  binding.box35Text))

        // 답안 박스 라인
        val boxLine = arrayOf(binding.line0, binding.line1, binding.line2, binding.line3, binding.line4, binding.line5)


        // 키보드
        val keyboard = hashMapOf(
            "ㅂ" to binding.qButton, "ㅈ" to binding.wButton, "ㄷ" to binding.eButton, "ㄱ" to binding.rButton, "ㅅ" to binding.tButton, "ㅛ" to  binding.yButton, "ㅕ" to binding.uButton, "ㅑ" to binding.iButton,
            "ㅁ" to binding.aButton, "ㄴ" to binding.sButton, "ㅇ" to binding.dButton, "ㄹ" to binding.fButton, "ㅎ" to binding.gButton, "ㅗ" to  binding.hButton, "ㅓ" to  binding.jButton, "ㅏ" to binding.kButton, "ㅣ" to binding.lButton,
            "ㅋ" to binding.zButton, "ㅌ" to binding.xButton, "ㅊ" to binding.cButton, "ㅍ" to binding.vButton, "ㅠ" to binding.bButton, "ㅜ" to  binding.nButton, "ㅡ" to  binding.mButton
        )

        // 키보드 텍스트
        val keyboardText = hashMapOf(
            "ㅂ" to binding.qText, "ㅈ" to binding.wText, "ㄷ" to binding.eText, "ㄱ" to binding.rText, "ㅅ" to binding.tText, "ㅛ" to  binding.yText, "ㅕ" to binding.uText, "ㅑ" to binding.iText,
            "ㅁ" to binding.aText, "ㄴ" to binding.sText, "ㅇ" to binding.dText, "ㄹ" to binding.fText, "ㅎ" to binding.gText, "ㅗ" to  binding.hText, "ㅓ" to  binding.jText, "ㅏ" to binding.kText, "ㅣ" to binding.lText,
            "ㅋ" to binding.zText, "ㅌ" to binding.xText, "ㅊ" to binding.cText, "ㅍ" to binding.vText, "ㅠ" to binding.bText, "ㅜ" to  binding.nText, "ㅡ" to  binding.mText
        )


        val solveLine = Array(6){ i -> false} // 각 라인별 해결유무
        var checkUnicode = Array(6){ i -> ""} // 각 라인별 유니코드
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
                    Log.d("Tttt", it.toString())

                    if(!solveLine[currentLine] && currentBox < 6){
                        checkUnicode[currentBox] = stringToConvertUnicode(it)
                        boxText[currentLine][currentBox].text = it
                        box[currentLine][currentBox].strokeColor = Color.BLACK
                        currentBox++

                        /**
                         * 텍스트 완성 효과
                         *
                         * 버그와 깔끔하지 못하다는 점에서 보류
                         * 버그로는 텍스트가 물리면서 텍스트를 빠르게 작성할 때 제대로 완성되지 못함.
                         * */
//                            box[currentLine][currentBox].animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).withEndAction {
//
//
//                                box[currentLine][currentBox].scaleX = 1f
//                                box[currentLine][currentBox].scaleY = 1f
//                                currentBox++
//
//                            }.start()

                            //  정답 박스를 다 채웠고 kordelWords에 현재 정답 유니코드가 있는지 확인
                        if (currentBox == 6 && !kordelWords.contains(checkUnicode.joinToString(""))){

                            // kordelWords에 현재 정답 유니코드가 없다면 단어가 없는 것으로 모든 텍스트에 빨간 글씨를 보여줌.
                            boxText[currentLine].forEach { it.setTextColor(Color.parseColor("#f87171")) }

                        }
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
            if (viewModel.flag.value == false){
                if(currentBox in 1..6){
                    boxText[currentLine][currentBox-1].text = ""
                    box[currentLine][currentBox-1].strokeColor = Color.parseColor("#e2e8f0")
                    checkUnicode[currentBox-1] = ""
                    currentBox--
                }

                for (i in 0..5){
                    boxText[currentLine][i].setTextColor(Color.BLACK)

                }

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
                if (!kordelWords.contains(checkUnicode.joinToString(""))){
                    Toast.makeText(this@MainActivity,"아, 목록에 단어가 없네요.", Toast.LENGTH_SHORT).show()

                }else{

                    var answer = 0 // 정답 개수

                    // 반복문을 통해 정답 코드를 확인
                    for (i in currectText.indices){

                        // 정답 문자 리스트에 boxText[currentLine][i].text가 있다면
                        if (currectText.contains(checkUnicode[i])){
                            // 정답 문자와 boxText[currentLine][i].text가 같다면 박스에 초록 색상을 입힘
                            if(currectText[i] == checkUnicode[i]){
                                box[currentLine][i].setCardBackgroundColor(Color.parseColor("#22c55e"))
                                box[currentLine][i].strokeColor = Color.parseColor("#22c55e")


                                // 키보드의 색상을 입힘
                                if (keyboard[boxText[currentLine][i].text]?.cardBackgroundColor?.defaultColor != -14498466){
                                    keyboard[boxText[currentLine][i].text]?.setCardBackgroundColor(Color.parseColor("#22c55e"))
                                    keyboard[boxText[currentLine][i].text]?.strokeColor = Color.parseColor("#22c55e")

                                }


                                // 정답 개수 카운트
                                answer++



                            // 정답 문자와 boxText[currentLine][i].text가 다르다면 박스에 노란 색상을 입힘
                            }else{
                                box[currentLine][i].setCardBackgroundColor(Color.parseColor("#eab308"))
                                box[currentLine][i].strokeColor = Color.parseColor("#eab308")


                                // 키보드의 색상을 입힘
                                if (keyboard[boxText[currentLine][i].text]?.cardBackgroundColor?.defaultColor != -14498466 &&
                                    keyboard[boxText[currentLine][i].text]?.cardBackgroundColor?.defaultColor != -1395960){
                                    keyboard[boxText[currentLine][i].text]?.setCardBackgroundColor(Color.parseColor("#eab308"))
                                    keyboard[boxText[currentLine][i].text]?.strokeColor = Color.parseColor("#eab308")

                                }



                            }

                        // 정답 코드에 boxText[currentLine][i].text가 없기 때문에 박스에 회색 색상을 입힘
                        }else{
                            box[currentLine][i].setCardBackgroundColor(Color.parseColor("#94a3b8"))
                            box[currentLine][i].strokeColor = Color.parseColor("#94a3b8")

                            // 키보드의 색상을 입힘
                            if (keyboard[boxText[currentLine][i].text]?.cardBackgroundColor?.defaultColor != -14498466 &&
                                keyboard[boxText[currentLine][i].text]?.cardBackgroundColor?.defaultColor != -1395960 &&
                                keyboard[boxText[currentLine][i].text]?.cardBackgroundColor?.defaultColor != -7035976){
                                keyboard[boxText[currentLine][i].text]?.setCardBackgroundColor(Color.parseColor("#94a3b8"))
                                keyboard[boxText[currentLine][i].text]?.strokeColor = Color.parseColor("#94a3b8")

                            }

                        }

                        // 박스 텍스트의 색상을 입힘
                        boxText[currentLine][i].setTextColor(Color.WHITE)

                        // 키보드 텍스트의 색상을 입힘
                        if (keyboard[boxText[currentLine][i].text]?.cardBackgroundColor?.defaultColor != -1){
                            keyboardText[boxText[currentLine][i].text]?.setTextColor(Color.WHITE)

                        }


                    }


                    if(answer == 6){
                        Toast.makeText(this@MainActivity, "정답입니다!", Toast.LENGTH_SHORT).show()
                        viewModel.flag.value = true



                    }else if(currentLine == 5){
                        viewModel.flag.value = true
                        Toast.makeText(this@MainActivity, "아쉽게도 정답을 맞추지 못했습니다.", Toast.LENGTH_SHORT).show()

                   } else{

                        boxLine[currentLine].animate().scaleX(1.05f).scaleY(1.05f).setDuration(150).withEndAction {

                            boxLine[currentLine].scaleX = 1f
                            boxLine[currentLine].scaleY = 1f

                            solveLine[currentLine] = true // 현재 라인 해결
                            currentLine++ // 다음 라인으로 초기화
                            currentBox = 0 // 현재 박스 위치 초기화

                        }.start()

                    }

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


    // appbar navi menu button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{ // 메뉴 버튼
                val layoutInflater = LayoutInflater.from(this)
                val view = layoutInflater.inflate(R.layout.alert_dialog, null)

                val alertDialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                    .setView(view)
                    .create()

                val textTitle = view.findViewById<TextView>(R.id.confirmTextView)
                val buttonConfirm =  view.findViewById<TextView>(R.id.yesButton)
                val buttonClose =  view.findViewById<View>(R.id.noButton)

                textTitle.text = "로그인 해볼까요?"
                buttonConfirm.text = "로그인 하기"
                buttonClose.setOnClickListener {
                    alertDialog.dismiss()
                }
                alertDialog.show()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun eventDate(): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val startDate = dateFormat.parse("2022-07-22 00:00:00").time
        val endDate = dateFormat.parse("2022-07-25 00:00:00").time
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time

        println("ttt 현재 : ${(today) / (24 * 60 * 60 * 1000)}")

        println("ttt두 날짜간의 차이(일) : ${(endDate - startDate) / (24 * 60 * 60 * 1000)}")
        println("ttt시작일 부터 경과 일 : ${(today - startDate) / (24 * 60 * 60 * 1000)}")
        println("ttt목표일 까지 남은 일(D-DAY) : ${(endDate - today) / (24 * 60 * 60 * 1000)}")
        return endDate - startDate
    }

    private fun nextEvent(): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val startDate = dateFormat.parse("2022-07-22 00:00:00").time
        val endDate = dateFormat.parse("2022-07-25 00:00:00").time
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time

        println("ttt 현재 : ${(today) / (24 * 60 * 60 * 1000)}")

        println("ttt두 날짜간의 차이(일) : ${(endDate - startDate) / (24 * 60 * 60 * 1000)}")
        println("ttt시작일 부터 경과 일 : ${(today - startDate) / (24 * 60 * 60 * 1000)}")
        println("ttt목표일 까지 남은 일(D-DAY) : ${(endDate - today) / (24 * 60 * 60 * 1000)}")
        return endDate - startDate
    }




    // kordle_unicode_words에서 모든 단어 가져오기/
    private fun readTextFile(): List<String> {
        val file = resources.openRawResource(R.raw.kordle_unicode_words)
        var txt = ByteArray(file.available())
        file.read(txt)
        file.close()
        var words = txt.toString(Charsets.UTF_8)
        Toast.makeText(this, txt.toString(Charsets.UTF_8),Toast.LENGTH_SHORT).show()
        words = words.replace("\"", "")

        var word= words.split(",")


    return  word
    }

    // kordle_unicode_words에서 하나의 단어를 가져오기.
    private fun getWord(word : List<String>): ArrayList<String> {
        var temp = ""
        var tempText = arrayListOf<String>()
        while (tempText.size != 6){
            tempText = arrayListOf<String>()
            var wordRandom = word.shuffled()[0]
            wordRandom.forEach { it ->
                if(temp.length == 6){
                    tempText.add(temp)
                    temp =  it.toString()
                }else{
                    temp += it

                }
            }
            tempText.add(temp)


        }
        return tempText

    }

    // 문자열 => 유니코드
    private fun stringToConvertUnicode(str: String): String {

        var text = ""
        val arr = str.toByteArray(Charsets.UTF_32BE)

        for (b in arr) {
            val st = String.format("%02X", b)
            text += st.toLowerCase()
        }
        text = "\\u" + text.substring(4 until 8)

        return text
    }



    /**
     * 아직 사용하지는 않지만
     * 혹시나 나중에 사용할 수 도 있음.
     *
     * */

//    // 유니코드 => 문자열
//    private fun convertUnicodeToString(unicodeString: String): String {
//        var str: String = unicodeString.split(" ")[0]
//        str = str.replace("\\", "")
//        val arr = str.split("u").toTypedArray()
//        var text = ""
//        for (i in 1 until arr.size) {
//            val hexVal = arr[i].toInt(16)
//            text += hexVal.toChar()
//        }
//
//        return text
//    }

}