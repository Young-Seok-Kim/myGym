package com.mygym.ui.employee.room.member

import androidx.room.*

@Dao
interface MemberDao {
    /*
    사용법
         CoroutineScope(Dispatchers.IO).launch {
             val testMember = MemberEntity (1, "김영석","", "01044461682", "youngseok1682@gmail.com", "서울특별시 송파구")
             MyGymRoomDataBase.getDatabase(context = this@EmployeeActivity).memberDao().insert(testMember)
         }
    * */

    @Query("SELECT * FROM member ORDER BY `index` ASC")
    fun getAllMembers() : List<MemberEntity>

    @Query("SELECT * FROM member where `index`  = :index")
    suspend fun getMemberByIndex(index : String) : MemberEntity

    @Query("SELECT * FROM member where membershipEndDate = :today ORDER BY `index` ASC")
    fun getTodayEndMebership(today : String) : List<MemberEntity>

    @Query("SELECT * FROM member ORDER BY name ASC")
    suspend fun getAlphabetizedWordsWithCoroutine() : List<MemberEntity> // Flow<List<Member>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMember(pMemberEntity: MemberEntity)
    @Update
    fun updateMember(pMemberEntity: MemberEntity)


    @Query("DELETE FROM member")
    suspend fun deleteAll()

    @Query("DELETE FROM member where name = :pName")
    fun deleteMember(pName : String)

}