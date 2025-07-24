package com.junjange.kordle.ui.view
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.junjange.kordle.R
import com.junjange.kordle.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.content.Intent
import com.junjange.kordle.data.AnswerCnt
import com.junjange.kordle.databinding.ActivityMainBinding
import com.junjange.kordle.room.KordleEntity
import com.junjange.kordle.ui.view.dialog.ExplanationDialog
import com.junjange.kordle.ui.view.dialog.SetDialog
import kotlin.math.round


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this, MainViewModel.Factory(application))[MainViewModel::class.java] }
    private var timerTask: Timer? = null
    var lastDay : Long = 0
    var eventIdx : Int = 0
    lateinit var kordelWords : List<String>
    lateinit var currectText : ArrayList<String>
    lateinit var kordleModel : KordleEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        CoroutineScope(Dispatchers.Main).launch {
            setObserver()
        }

        kordelWords =  readTextFile() // 모든 코들 단어

        setSupportActionBar(binding.mainToolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_settings_24) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게

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


        var solveLine = Array(6){ i -> false} // 각 라인별 해결유무
        var checkUnicode = Array(6){ i -> ""} // 각 라인별 유니코드
        var currentLine : Int = 0 // 현재 라인
        var currentBox : Int = 0 // 현재 박스 위치

        /**
         * 키보드를 누르면 수행되는 코드
         * viewModel에 keyboardText를 observe하여 boxText에 문자를 입력
         *
         * */
        viewModel.keyboardText.observe(this, {

                viewModel.keyboardText.value?.let { it ->

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
                checkUnicode[currentBox-1] = ""
                currentBox--

            }

            for (i in 0..5){
                boxText[currentLine][i].setTextColor(Color.BLACK)

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




                    // 정답을 맞추었거나 기회를 놓쳤을 경우
                    if(answer == 6 || currentLine == 5){
                        var currentSolve : Boolean
                        var solveProblemsCnt : Int
                        if(answer == 6){
                            solveProblemsCnt = 1
                            currentSolve = true
                        }else{
                            solveProblemsCnt = 0
                            currentSolve = false

                        }


                        // 현재 날짜
                        val today = eventTime()

                        if ((today - lastDay) / (24 * 60 * 60 * 1000) > 0){

                            // DB 저장
                            CoroutineScope(Dispatchers.Main).launch{


                                // 현재 연승
                                var currentWinningStreak: Int
                                if (answer == 0){
                                    currentWinningStreak = 0

                                }else if (kordleModel.preSolve == false){
                                    currentWinningStreak = 1

                                }else{
                                    currentWinningStreak = kordleModel.currentWinningStreak + 1
                                }


                                // 최다 연승
                                var mostWinStreak: Int
                                if (currentWinningStreak >= kordleModel.mostWinStreak){
                                    mostWinStreak = currentWinningStreak
                                }else{
                                    mostWinStreak = kordleModel.mostWinStreak
                                }

                                // 도전 분포
                                var one = kordleModel.AnswerCnt.one
                                var two = kordleModel.AnswerCnt.two
                                var three = kordleModel.AnswerCnt.three
                                var four = kordleModel.AnswerCnt.four
                                var five = kordleModel.AnswerCnt.five
                                var six = kordleModel.AnswerCnt.six

                                if (answer == 6){
                                    when (currentLine){
                                        0 -> one ++
                                        1 -> two ++
                                        2 -> three ++
                                        3 -> four ++
                                        4 -> five ++
                                        5 -> six ++

                                    }

                                }



                                viewModel.updateRoom(KordleEntity(
                                    id = 0,
                                    lastDay = today,
                                    preSolve = currentSolve,
                                    allProblemsCnt = kordleModel.allProblemsCnt +1 ,
                                    solveProblemsCnt= kordleModel.solveProblemsCnt + solveProblemsCnt ,
                                    correctAnswerRate = round((((kordleModel.solveProblemsCnt + solveProblemsCnt).toDouble() / (kordleModel.allProblemsCnt + 1)).toDouble() * 100.0).toDouble() * 10 / 10),
                                    currentWinningStreak = currentWinningStreak ,
                                    mostWinStreak = mostWinStreak,
                                    AnswerCnt = AnswerCnt(one, two, three, four, five, six)))

                            }


                        }




                        // 다음 문제까지 남은 시간 타이머로 보여줌
                        timerTask = kotlin.concurrent.timer(period = 1000) {    // timer() 호출


                            // UI조작을 위한 메서드
                            runOnUiThread {
                                // 남은 시간
                                val currentTimeMillis = System.currentTimeMillis()
                                val mFormat = SimpleDateFormat("HH:mm:ss")
                                var timeRemaining = mFormat.format(Date( mFormat.parse("-09:00:00").time - currentTimeMillis))
                                binding.nextTime.text = timeRemaining


                            }
                        }





                        binding.keyboard0.visibility = View.GONE
                        binding.keyboard1.visibility = View.GONE
                        binding.keyboard2.visibility = View.GONE
                        binding.endGame.visibility = View.VISIBLE






                    }else{

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

        // 이 놀이는? 버튼
        binding.question.setOnClickListener {
            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            val bottomSheetDialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

        }

        // 다시 풀기
        binding.unravelAgainBtn.setOnClickListener {




            // 남은 시간 타이머 끄기
            timerTask?.cancel()


            binding.keyboard0.visibility = View.VISIBLE
            binding.keyboard1.visibility = View.VISIBLE
            binding.keyboard2.visibility = View.VISIBLE
            binding.endGame.visibility = View.GONE

            solveLine = Array(6){ i -> false} // 각 라인별 해결유무
            checkUnicode = Array(6){ i -> ""} // 각 라인별 유니코드
            currentLine  = 0 // 현재 라인
            currentBox  = 0 // 현재 박스 위치

            box.forEach { it.forEach {
                it.setCardBackgroundColor(Color.WHITE)
                it.strokeColor = Color.parseColor("#e2e8f0")


            } }

            boxText.forEach { it.forEach {
                it.setTextColor(Color.BLACK)
                it.text = ""
            } }

            keyboard.values.forEach{
                it.setCardBackgroundColor(Color.parseColor("#e2e8f0"))
                it.strokeColor = Color.parseColor("#e2e8f0")


            }

            keyboardText.values.forEach {
                it.setTextColor(Color.BLACK)

            }


        }



    }

    private fun setObserver() {
        binding.keyboard0.visibility = View.GONE
        binding.keyboard1.visibility = View.GONE
        binding.keyboard2.visibility = View.GONE
        binding.endGame.visibility = View.GONE


        viewModel.roomKordle.observe(this@MainActivity, {
            kordleModel = it[0]
            lastDay = kordleModel.lastDay

            val today = eventTime()

            if (today == lastDay){
                eventIdx = kordleModel.allProblemsCnt
                binding.problemNum.text = eventIdx.toString()

                // 다음 문제까지 남은 시간 타이머로 보여줌
                timerTask = kotlin.concurrent.timer(period = 1000) {    // timer() 호출


                    // UI조작을 위한 메서드
                    runOnUiThread {
                        // 남은 시간
                        val currentTimeMillis = System.currentTimeMillis()
                        val mFormat = SimpleDateFormat("HH:mm:ss")
                        var timeRemaining = mFormat.format(Date( mFormat.parse("-09:00:00").time - currentTimeMillis))
                        binding.nextTime.text = timeRemaining


                    }
                }

                binding.keyboard0.visibility = View.GONE
                binding.keyboard1.visibility = View.GONE
                binding.keyboard2.visibility = View.GONE
                binding.endGame.visibility = View.VISIBLE

            // 마지막으로 문제를 푼 날에서 시간이 흘렀다면
            } else {

                eventIdx = kordleModel.allProblemsCnt + 1
                binding.problemNum.text = eventIdx.toString()


                // 남은 시간 타이머 끄기
                timerTask?.cancel()


                binding.keyboard0.visibility = View.VISIBLE
                binding.keyboard1.visibility = View.VISIBLE
                binding.keyboard2.visibility = View.VISIBLE
                binding.endGame.visibility = View.GONE



            }

            currectText = getWord(kordelWords, eventIdx) // 코들 단어 하나 가져오기

            var answerWord = ""
            currectText.forEach { answerWord += convertUnicodeToString(it)+" " }
            binding.answerWord.text = "정답은 \"${answerWord}\"입니다."


        })

    }




    // appbar navi menu button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{ // 메뉴 버튼
                startActivity(Intent(this, SetDialog::class.java))

            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 현재 날짜와 시작 날짜에 차이를 통해 문제를 갱신
    private fun eventDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val startDate = dateFormat.parse("2022-07-24 09:00:00").time
        val endDate = dateFormat.parse("2022-07-25 00:00:00").time
        val today = eventTime()

//        println("ttt 현재 : ${(today) / (24 * 60 * 60 * 1000)}")
//        println("ttt 현재123 : ${(today)}")
//        println("ttt 현재123 : ${(endDate)}")
//
//
//
//        println("ttt두 날짜간의 차이(일) : ${(endDate - startDate) / (24 * 60 * 60 * 1000)}")
//        println("ttt시작일 부터 경과 일 : ${(1658674800000 - 1658588400000) / (24 * 60 * 60 * 1000)}")
//        println("ttt목표일 까지 남은 일(D-DAY) : ${(endDate - today) / (24 * 60 * 60 * 1000)}")
    }

    private fun eventTime(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time

    }


    // kordle_unicode_words에서 모든 단어 가져오기
    private fun readTextFile(): List<String> {
        val file = resources.openRawResource(R.raw.kordle_unicode_words)
        var txt = ByteArray(file.available())
        file.read(txt)
        file.close()
        var words = txt.toString(Charsets.UTF_8)
        words = words.replace("\"", "")

        var word= words.split(",")


    return  word
    }

    // kordle_unicode_words에서 하나의 단어를 가져오기.
    private fun getWord(word : List<String>, index : Int): ArrayList<String> {
        var temp = ""
        var tempText = arrayListOf<String>()
//        var wordRandom = word.shuffled()[0] // 무작위로 하나의 단어를 가져오기.
        var wordRandom = word[index]
        wordRandom.forEach { it ->
            if(temp.length == 6){
                tempText.add(temp)
                temp =  it.toString()
            }else{
                temp += it

            }
        }
        tempText.add(temp)


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



    // 뒤로가기 2번 눌러야 종료
    private val FINISH_INTERVAL_TIME: Long = 2500
    private var backPressedTime: Long = 0
    private var toast: Toast? = null
    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime


        // 뒤로 가기 할 경우 홈 화면으로 이동
        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
            super.onBackPressed()
            // 앱 종료시 뒤로가기 토스트 종료
            toast!!.cancel()
            finish()
        } else {
            backPressedTime = tempTime
            toast =
                Toast.makeText(applicationContext, "'뒤로'버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT)
            toast!!.show()
        }
    }

}
