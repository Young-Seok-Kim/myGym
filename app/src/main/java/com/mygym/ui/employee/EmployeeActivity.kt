package com.mygym.ui.employee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mygym.databinding.ActivityEmployeeBinding
import com.mygym.databinding.ActivityMainBinding

class EmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeBinding.inflate(layoutInflater,null,false)
//        binding.test.emoji.text = "이모지"
        binding.test.title.text = "타이틀"
        setContentView(binding.root)
    }
}