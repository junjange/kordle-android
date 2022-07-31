package com.junjange.kordle.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.junjange.kordle.R


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {


//    private val _a by lazy { emptyList<String>() }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)




        Handler(Looper.getMainLooper()).postDelayed({

            val intent= Intent( this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }

    // kordle_unicode_words에서 모든 단어 가져오기
    private fun readTextFile(): List<String> {
        val file = resources.openRawResource(R.raw.kordle_unicode_words)
        var txt = ByteArray(file.available())
        file.read(txt)
        file.close()
        var words = txt.toString(Charsets.UTF_8)
        Toast.makeText(this, txt.toString(Charsets.UTF_8), Toast.LENGTH_SHORT).show()
        words = words.replace("\"", "")

        var word= words.split(",")


        return word
    }

}