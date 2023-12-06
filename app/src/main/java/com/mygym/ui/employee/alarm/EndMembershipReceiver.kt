package com.mygym.ui.employee.alarm

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.mygym.common.YoungsFunction
import com.mygym.ui.activity.MainActivity
import com.mygym.ui.employee.EmployeeActivity
import com.mygym.ui.employee.room.member.MemberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime


class EndMembershipReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        if(intent?.extras?.get("code") == MainActivity.REQUEST_CODE) {
            alertEndMember(context)
        }
    }

    private fun alertEndMember(context : Context) {

        var content: String = ""
        val endMember: List<MemberEntity> by lazy {
            YoungsFunction.getDataBase(context)
                .getTodayEndMebership(LocalDate.now().toString())
        }


        CoroutineScope(Dispatchers.Default).launch RootCoroutine@{

            var isEmpty : Boolean = true
            CoroutineScope(Dispatchers.IO).launch IOCoroutine@{
                try {
                    content = if (endMember.isEmpty()){
                        return@IOCoroutine
                    } else if (endMember.size == 1) {
                        isEmpty = false
                        "${endMember[0].name} 회원의 만료일입니다."
                    } else {
                        isEmpty = false
                        "${endMember[0].name} 등 ${endMember.size}명의 회원의 만료일입니다."
                    }
                }
                catch (e : java.lang.IndexOutOfBoundsException){
                    return@IOCoroutine
                }
            }.join()
            CoroutineScope(Dispatchers.Main).launch MainCoroutine@{
                if (isEmpty.not()) {
                    YoungsFunction.pushAlert(context, content)
                }
            }.join()
        }

    }
}
