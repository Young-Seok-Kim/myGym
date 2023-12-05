package com.mygym.ui.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mygym.databinding.ActivityMainBinding
import com.mygym.ui.employee.EmployeeActivity
import com.mygym.ui.employee.alarm.EndMembershipReceiver
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 101
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater,null,false)

        alertEndMembership()

       setContentView(binding.root)
    }

    private fun alertEndMembership()
    {
        val alarmManager = binding.root.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = Intent(binding.root.context, EndMembershipReceiver::class.java).let {
            it.putExtra("code", MainActivity.REQUEST_CODE)
            PendingIntent.getBroadcast(binding.root.context, MainActivity.REQUEST_CODE, it, 0)
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0) // 00시 
            set(Calendar.MINUTE, 0) // 00분에 알람이 오도록 설정
        }
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

}