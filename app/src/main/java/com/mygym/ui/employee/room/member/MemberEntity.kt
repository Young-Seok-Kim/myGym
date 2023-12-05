package com.mygym.ui.employee.room.member

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity(tableName = "member")
data class MemberEntity(
    val name: String,
    val registDate: String,
    val membershipStartDate: String,
    val membershipEndDate: String,
    val phoneNumber: String,
    val email: String,
    val address: String,
    val remark: String
){
    @PrimaryKey(autoGenerate = true)
    var index: Long = 0
}

