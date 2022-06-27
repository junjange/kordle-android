package com.junjange.kordle.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
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

        binding.qButton.setOnClickListener {
            Toast.makeText(this@MainActivity,"kooooooooorgle", Toast.LENGTH_SHORT).show()
        }



    }
}