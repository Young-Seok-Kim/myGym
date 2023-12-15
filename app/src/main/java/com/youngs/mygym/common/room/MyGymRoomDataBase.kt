package com.youngs.mygym.common.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.youngs.mygym.ui.employee.cost.room.CostDao
import com.youngs.mygym.ui.employee.cost.room.CostEntity
import com.youngs.mygym.ui.employee.member.room.MemberDao
import com.youngs.mygym.ui.employee.member.room.MemberEntity

@Database(entities = [MemberEntity::class, CostEntity::class], version = 1, exportSchema = false)
public abstract class MyGymRoomDataBase : RoomDatabase() {
    abstract fun memberDao() : MemberDao
    abstract fun costDao() : CostDao

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