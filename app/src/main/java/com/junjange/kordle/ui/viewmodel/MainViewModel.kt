package com.junjange.kordle.ui.viewmodel

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.junjange.kordle.room.KordleEntity
import com.junjange.kordle.room.KordleRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: KordleRepository) : ViewModel() {

    val roomKordle: LiveData<List<KordleEntity>> = repository.roomGetAllKordle()
    val roomKordleInput: MutableLiveData<String> = MutableLiveData()

    private val _keyboardText = MutableLiveData<String>()
    private  val _flag = MutableLiveData<Boolean>(false)


    val keyboardText : MutableLiveData<String>
    get() = _keyboardText

    val flag : MutableLiveData<Boolean>
        get() = _flag

    // 코루틴으로 Room 제어
//    fun insertRoom() = viewModelScope.launch {
//        repository.roomInsertKordle(KordleEntity(0, "", ""))
//        roomKordleInput.value = ""
//    }

    private val keyboard = hashMapOf(
        "q_button" to "ㅂ", "w_button" to "ㅈ", "e_button" to "ㄷ", "r_button" to "ㄱ", "t_button" to "ㅅ", "y_button" to "ㅛ", "u_button" to "ㅕ", "i_button" to "ㅑ",
        "a_button" to "ㅁ", "s_button" to "ㄴ", "d_button" to "ㅇ", "f_button" to "ㄹ", "g_button" to "ㅎ", "h_button" to "ㅗ", "j_button" to "ㅓ", "k_button" to "ㅏ", "l_button" to "ㅣ",
        "z_button" to "ㅋ", "x_button" to "ㅌ", "c_button" to "ㅊ", "v_button" to "ㅍ", "b_button" to "ㅠ", "n_button" to "ㅜ", "m_button" to "ㅡ"
    )

    fun keyboardOnClick(view: View){

        _keyboardText.value = keyboard[view.resources.getResourceEntryName(view.id)]

//        viewModelScope.launch {
//            repository.roomInsertKordle(KordleEntity(0, _keyboardText.value.toString(), ""))
//        }

    }
    class Factory(private val application : Application) : ViewModelProvider.Factory { // factory pattern
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(KordleRepository.getInstance(application)!!) as T
        }

    }
}