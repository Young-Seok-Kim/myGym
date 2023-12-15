package com.youngs.mygym.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.youngs.mygym.ui.employee.EmployeeActivity


class SplashActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if(true) // 업데이트를 반드시 해야한다면 해당값을 true로 변경
//            versionCheck()
//        else {
            val intent = Intent(this@SplashActivity, EmployeeActivity::class.java)
            startActivity(intent)
            finish()
//        }

    }
//    private fun versionCheck()
//    {
//        val jsonObject : JsonObject = JsonObject()
//        jsonObject.addProperty("clientAppVersion", BuildConfig.VERSION_CODE.toString())
//        CoroutineScope(Dispatchers.Default).launch {
//            NetworkConnect.connectHTTPS("versionCheck.do",
//                jsonObject,
//                applicationContext // 실패했을때 Toast 메시지를 띄워주기 위한 Context
//                , onSuccess = {
//                    val jsonArray : JSONArray = YoungsFunction.stringArrayToJson(NetworkConnect.resultString)
//
//                    if(jsonArray[0].toString().toBoolean()) {
////                        setTheme(R.style.Theme_AppCompat)
//                        val messageBox = AlertDialog.Builder(this@SplashActivity)
//                        messageBox.setTitle("업데이트 필요")
//                            .setMessage("업데이트를 하지않을경우 사용중 앱이 종료되거나, 일부 기능이 제대로 동작하지 않을수 있습니다.")
//                            .setPositiveButton("업데이트") {
//                                    _: DialogInterface, _: Int ->
//                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.youngsbook"))
//                                startActivity(intent)
//                                finish()
//                            }
//                            .setNegativeButton("나중에"){
//                                    _: DialogInterface, _: Int ->
//                                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//                                startActivity(intent)
//                                finish()
//                            }
//                            .setCancelable(false)
//                            .show()
//                    }
//                    else {
//                        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                }
//                , onFailure = {
//                    YoungsFunction.messageBoxOKAction(this@SplashActivity, "네트워크 연결오류!", "인터넷 연결을 확인해주세요.\n인터넷이 연결되어있다면 앱 관리자에게 문의해주세요."
//                    ,
//                        OKAction = {
//                            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                        }
//                    )
//                }
//            )
//        }
//    }
}
