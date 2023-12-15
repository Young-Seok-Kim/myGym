package com.youngs.mygym.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.youngs.mygym.databinding.ActivityLoginBinding
import com.youngs.mygym.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater,null,false)
        setContentView(binding.root)
    }
}