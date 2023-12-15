package com.youngs.mygym.common.room

import android.app.Application

class MyGymApplication : Application() {
    val database by lazy { MyGymRoomDataBase.getDatabase(this) }
}