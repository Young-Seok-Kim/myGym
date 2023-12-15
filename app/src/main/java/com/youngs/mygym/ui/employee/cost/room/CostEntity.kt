package com.youngs.mygym.ui.employee.cost.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cost")
data class CostEntity(
    var month : String,
    var cost: String,
    var remark: String
){
    @PrimaryKey(autoGenerate = true)
    var index: Int = 0
}

