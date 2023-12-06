package com.mygym.ui.employee

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.mygym.R
import com.mygym.common.YoungsFunction
import com.mygym.common.network.YoungsProgressBar
import com.mygym.databinding.ActivityEmployeeBinding
import com.mygym.ui.employee.member.list.MemberListFragment
import com.mygym.ui.employee.room.member.MemberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate


class EmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeBinding
    private val youngsProgressBar: YoungsProgressBar by lazy { YoungsProgressBar(this@EmployeeActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeBinding.inflate(layoutInflater, null, false)

        binding.manageMember.setOnClickListener() {
            MemberListFragment().let {
                it.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialogTheme)
                it.dialog?.window?.setWindowAnimations(android.R.style.Animation_Dialog)
                it.showNow(supportFragmentManager, "")
            }
        }

        binding.test.setOnClickListener() {
            YoungsProgressBar(this).show()
        }

        binding.testAlarm.setOnClickListener {
            alertEndMember()
        }

        setContentView(binding.root)
    }

    private fun alertEndMember() {
        var content: String = ""
        val endMember: List<MemberEntity> by lazy {
            YoungsFunction.getDataBase(this@EmployeeActivity)
                .getTodayEndMebership(LocalDate.now().toString())
        }

        CoroutineScope(Dispatchers.Default).launch RootCoroutine@{
            var isEmpty: Boolean = true
            CoroutineScope(Dispatchers.IO).launch IOCoroutine@{
                try {
                    content = if (endMember.isEmpty()) {
                        return@IOCoroutine
                    } else if (endMember.size == 1) {
                        isEmpty = false
                        "${endMember[0].name} 회원의 만료일입니다."
                    } else {
                        isEmpty = false
                        "${endMember[0].name} 등 ${endMember.size}명의 회원의 만료일입니다."
                    }
                } catch (e: java.lang.IndexOutOfBoundsException) {
                    return@IOCoroutine
                }
            }.join()
            CoroutineScope(Dispatchers.Main).launch MainCoroutine@{
                if (isEmpty.not()) {
                    Toast.makeText(this@EmployeeActivity, content, Toast.LENGTH_SHORT).show()
                    YoungsFunction.pushAlert(this@EmployeeActivity, content)
                }
            }.join()
        }
    }
}