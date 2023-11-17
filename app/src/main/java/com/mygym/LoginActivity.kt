package com.mygym

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mygym.databinding.ActivityLoginBinding
import com.mygym.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater,null,false)
        setContentView(binding.root)
    }
}