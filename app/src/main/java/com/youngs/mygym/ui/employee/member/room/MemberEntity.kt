package com.youngs.mygym.ui.employee.member.room

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
    var totalCost: Int,
    var remark: String
){
    @PrimaryKey(autoGenerate = true)
    var index: Int = 0
}

