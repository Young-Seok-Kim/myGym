package com.mygym.ui.employee.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mygym.ui.employee.room.member.MemberDao
import com.mygym.ui.employee.room.member.MemberEntity

@Database(entities = [MemberEntity::class], version = 1, exportSchema = false)
public abstract class MyGymRoomDataBase : RoomDatabase() {
    abstract fun memberDao() : MemberDao

    companion object{
        @Volatile
        private var INSTANCE: MyGymRoomDataBase? = null

        fun getDatabase(
            context: Context
        ): MyGymRoomDataBase {
            if(INSTANCE == null){
                synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyGymRoomDataBase::class.java,
                        "my_gym.db"
                    )
//                        .addMigrations(MIGRATION_1_2)
                        .build()
                    INSTANCE = instance
                }
            }
            return INSTANCE!!
        }

//        private val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE member ADD COLUMN registDate TEXT")
//            }
//        }
    }
}