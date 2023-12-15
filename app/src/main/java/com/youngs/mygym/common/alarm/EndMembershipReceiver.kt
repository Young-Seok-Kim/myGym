package com.youngs.mygym.common.alarm

import android.content.Context
import android.content.Intent
import com.youngs.mygym.common.YoungsFunction
import com.youngs.mygym.ui.activity.MainActivity
import com.youngs.mygym.ui.employee.cost.room.CostEntity
import com.youngs.mygym.ui.employee.member.room.MemberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.time.LocalDate


class EndMembershipReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        if(intent?.extras?.get("code") == MainActivity.REQUEST_CODE) {
            alertEndMember(context)
        }
    }

    private fun alertEndMember(context : Context) {

        var content: String = ""
        val endMember: List<MemberEntity> by lazy {
            YoungsFunction.getDataBase(context).memberDao()
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
