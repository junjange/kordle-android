package com.junjange.kordle.ui.viewmodel

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.junjange.kordle.room.KordleRepository

class MainViewModel(private val repository: KordleRepository) : ViewModel() {

    private val _keyboardText = MutableLiveData<String>()
    private  val _flag = MutableLiveData<Boolean>(false)


    val keyboardText : MutableLiveData<String>
    get() = _keyboardText

    val flag : MutableLiveData<Boolean>
        get() = _flag

    private val keyboard = hashMapOf(
        "q_button" to "ㅂ", "w_button" to "ㅈ", "e_button" to "ㄷ", "r_button" to "ㄱ", "t_button" to "ㅅ", "y_button" to "ㅛ", "u_button" to "ㅕ", "i_button" to "ㅑ",
        "a_button" to "ㅁ", "s_button" to "ㄴ", "d_button" to "ㅇ", "f_button" to "ㄹ", "g_button" to "ㅎ", "h_button" to "ㅗ", "j_button" to "ㅓ", "k_button" to "ㅏ", "l_button" to "ㅣ",
        "z_button" to "ㅋ", "x_button" to "ㅌ", "c_button" to "ㅊ", "v_button" to "ㅍ", "b_button" to "ㅠ", "n_button" to "ㅜ", "m_button" to "ㅡ"
    )

    fun keyboardOnClick(view: View){

        _keyboardText.value = keyboard[view.resources.getResourceEntryName(view.id)]

    }
    class Factory(private val application : Application) : ViewModelProvider.Factory { // factory pattern
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(KordleRepository.getInstance(application)!!) as T
        }

    }
}