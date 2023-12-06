package com.mygym.common

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import com.mygym.R
import com.mygym.common.Define
import com.mygym.ui.activity.MainActivity
import com.mygym.ui.employee.room.MyGymRoomDataBase
import com.mygym.ui.employee.room.member.MemberDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*


object YoungsFunction {
    fun stringArrayToJson(jsonString : String ) : JSONArray
    {
        val jsonObject = JSONObject(jsonString)

        val resultJson : JSONArray = jsonObject.get("RESULT_LIST") as JSONArray

        if(jsonObject.get("RESULT_LIST").toString() == "[]")
            resultJson.put("")

        return resultJson
    }

    fun stringIntToJson(jsonString: String): Int {
        val jsonObject = JSONObject(jsonString)

        return jsonObject.get("RESULT_LIST").toString().toInt()
    }

    /**
     * 현재 날짜 및 시간을 리턴한다.
     * param ex "yyyyMMdd"  "yyyy-MM-dd" "HH:mm" ...
     */
    fun getNowDate(format: String? = null): String {
        return SimpleDateFormat(if (format.isNullOrEmpty()) "yyyy-MM-dd" else format).format(Date())
    }

    /**
     * Date형식을 입력하면 yyyy-MM-dd 형식의 String으로 반환해준다.
     */
    fun getDate(date: Date): String {
        val simple: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        return simple.format(date)
    }

    /*
    단순 OK 버튼
     */
    fun messageBoxOK(context: Context, title : String, Message : String){
        val messageBox = AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(Message)
        .setPositiveButton("확인") {
                _: DialogInterface, _: Int ->
        }
        .setCancelable(false)
        .show()
    }

    /*
    OK 버튼을 누르면 특정 액션이 동작하도록 하는 메시지 박스
     */
    fun messageBoxOKAction(context: Context, title : String, Message : String, OKAction : () -> Unit){
        val messageBox = AlertDialog.Builder(context)
        messageBox.setTitle(title)
        .setMessage(Message)
        .setPositiveButton("확인") {
                _: DialogInterface, _: Int ->
            OKAction()
        }
        .setCancelable(false)
        .show()
    }
    fun messageBoxOKCancelAction(context: Context, title : String, Message : String, OKAction : () -> Unit, cancelAction : () -> Unit){
        val messageBox = AlertDialog.Builder(context)
        messageBox.setTitle(title)
        .setMessage(Message)
        .setPositiveButton("확인") {
                _: DialogInterface, _: Int ->
            OKAction()
        }
        .setNegativeButton("취소"){
                _: DialogInterface, _: Int ->
            cancelAction()
        }
        .setCancelable(false)
        .show()
    }

    /*
     01012345678과 같은 형식으로 파라미터를 넣으면 국제번호로 값을 return 해준다.
     */
    fun phoneNumber82(msg : String) : String{

        val firstNumber : String = msg.substring(0,3)
        var phoneEdit = msg.substring(3)

        when(firstNumber){
            "010" -> phoneEdit = "+8210$phoneEdit"
            "011" -> phoneEdit = "+8211$phoneEdit"
            "016" -> phoneEdit = "+8216$phoneEdit"
            "017" -> phoneEdit = "+8217$phoneEdit"
            "018" -> phoneEdit = "+8218$phoneEdit"
            "019" -> phoneEdit = "+8219$phoneEdit"
            "106" -> phoneEdit = "+82106$phoneEdit"
        }
        return phoneEdit
    }

    fun getStringFromJson(jsonObject: JSONObject, columnName: String, defaultValue: String?): String? {
        if (jsonObject.has(columnName)) {
            var string = jsonObject.getString(columnName)
            if (string.equals("null", ignoreCase = true)) {
                string = ""
            }
            return string
        } else {
            return defaultValue
        }
    }
    fun getIntFromJson(jsonObject: JSONObject, columnName: String, defaultValue: Int?): Int? {
        return if (jsonObject.has(columnName)) {
            jsonObject.getInt(columnName)
        } else {
            defaultValue
        }
    }

    fun bookSearch(searchWord : String) : JSONObject{

        val clientId : String = Define.NAVER_CLIENT_ID
        val clientSecret : String = Define.NAVER_CLIENT_SECRETE
        var bookJsonObject : JSONObject = JSONObject()

        try {
            runBlocking {
                CoroutineScope(Dispatchers.IO).launch {
                    val text: String = URLEncoder.encode(searchWord, "UTF-8")
                    val apiURL =
                        "https://openapi.naver.com/v1/search/book.json?query=$text&display=20" // json 결과
                    val url = URL(apiURL)
                    val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                    con.setRequestMethod("GET")
                    con.setRequestProperty("X-Naver-Client-Id", clientId)
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret)
//                    Log.d("네이버 url", apiURL)
//                    Log.d("con.responseCode", con.responseCode.toString())

                    val responseCode: Int = con.getResponseCode()
                    val br: BufferedReader
                    if (responseCode == 200) { // 정상 호출
                        br = BufferedReader(InputStreamReader(con.inputStream, "UTF-8"))
                    } else {  // 에러 발생
                        br = BufferedReader(InputStreamReader(con.errorStream))
                    }
                    var inputLine: String?
                    val response = StringBuffer()
                    while (br.readLine().also { inputLine = it } != null) {
                        response.append(inputLine)
                        response.append("\n")
                    }
                    br.close()
                    val naverHtml: String = response.toString()
//                    Log.d("con.responseCode", response.toString())
                    val bun = Bundle()
                    bun.putString("NAVER_HTML", naverHtml)


                    bookJsonObject = JSONObject(response.toString())

                }.join()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bookJsonObject
    }

    fun setDate(context: Context, textView: TextView, divider: String) {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        DatePickerDialog(
            context,
            { _, yearSelected, monthOfYear, dayOfMonth ->
                val date: String
                val dialogMonth: String = if (monthOfYear + 1 < 10) "0" + (monthOfYear + 1) else (monthOfYear + 1).toString() + ""
                val dialogDay: String = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString() + ""
                date = yearSelected.toString() + divider + dialogMonth + divider + dialogDay
                textView.text = date
            },
            year,
            month,
            day
        ).show()
    }

    private fun setTime(context: Context?, tv: TextView, divider: String) {
        val cal = Calendar.getInstance()
        val hour = cal[Calendar.HOUR_OF_DAY]
        val minute = cal[Calendar.MINUTE]
        TimePickerDialog(
            context,
            { _, hourOfDay, min ->
                val time: String
                val dialogHour: String = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString() + ""
                val dialogMinute: String = if (min < 10) "0$min" else min.toString() + ""
                time = dialogHour + divider + dialogMinute + divider + "00"
                tv.text = time
            },
            hour,
            minute,
            true
        ).show()
    }

    fun pushAlert(context : Context, content : String)
    {
        lateinit var builder: NotificationCompat.Builder

        //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
        val CHANNEL_ID = "channel1"
        val CHANNEL_NAME = "Channel1"


        val manager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
        builder = NotificationCompat.Builder(context, CHANNEL_ID)

        //알림창 클릭 시 activity 화면 부름
        val intent2 = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 101, intent2, PendingIntent.FLAG_UPDATE_CURRENT)

        //알림창 제목
        builder.setContentTitle(content)
        //알림창 아이콘
        builder.setSmallIcon(com.google.android.material.R.drawable.ic_mtrl_chip_checked_circle)
        //알림창 터치시 자동 삭제
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)
        val notification: Notification = builder.build()
        manager.notify(1, notification)
    }

    fun getDataBase(context: Context): MemberDao
    {
        return MyGymRoomDataBase.getDatabase(context).memberDao()
    }
}