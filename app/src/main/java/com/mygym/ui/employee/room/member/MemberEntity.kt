package com.mygym.ui.employee.room.member

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member")
data class MemberEntity(
    var name: String,
    var signUpDate: String,
    var membershipStartDate: String,
    var membershipEndDate: String,
    var phoneNumber: String,
    var email: String,
    var address: String,
    var remark: String
){
    @PrimaryKey(autoGenerate = true)
    var index: Long = 0
}

