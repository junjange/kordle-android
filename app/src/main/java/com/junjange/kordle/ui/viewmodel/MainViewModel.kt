package com.junjange.kordle.ui.viewmodel

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.junjange.kordle.room.KordleEntity
import com.junjange.kordle.room.KordleRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: KordleRepository) : ViewModel() {

    val roomKordle:  LiveData<List<KordleEntity>> = repository.roomGetAllKordle()

    private val _keyboardText = MutableLiveData<String>()


    val keyboardText : MutableLiveData<String>
    get() = _keyboardText



    // 코루틴으로 Room 제어
    fun updateRoom(kordleEntity: KordleEntity) = viewModelScope.launch {
        repository.roomUpdateKordle(kordleEntity)
    }


    private val keyboardToText = hashMapOf(
        "q_button" to "ㅂ", "w_button" to "ㅈ", "e_button" to "ㄷ", "r_button" to "ㄱ", "t_button" to "ㅅ", "y_button" to "ㅛ", "u_button" to "ㅕ", "i_button" to "ㅑ",
        "a_button" to "ㅁ", "s_button" to "ㄴ", "d_button" to "ㅇ", "f_button" to "ㄹ", "g_button" to "ㅎ", "h_button" to "ㅗ", "j_button" to "ㅓ", "k_button" to "ㅏ", "l_button" to "ㅣ",
        "z_button" to "ㅋ", "x_button" to "ㅌ", "c_button" to "ㅊ", "v_button" to "ㅍ", "b_button" to "ㅠ", "n_button" to "ㅜ", "m_button" to "ㅡ"
    )

    fun keyboardOnClick(view: View){

        _keyboardText.value = keyboardToText[view.resources.getResourceEntryName(view.id)]

    }
    class Factory(private val application : Application) : ViewModelProvider.Factory { // factory pattern
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(KordleRepository.getInstance(application)!!) as T
        }

    }
}