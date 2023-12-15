package com.youngs.mygym.ui.employee.cost.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CostDao {

    @Query("SELECT * FROM cost ORDER BY `index` ASC")
    fun getAllCost() : Flow<List<CostEntity>>

    @Query("SELECT * FROM cost where `index`  = :index")
    suspend fun getCostByIndex(index : String) : CostEntity

//    @Query("SELECT * FROM member where membershipEndDate = :today ORDER BY `index` ASC")
//    fun getTodayEndMebership(today : String) : List<CostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCost(pCostEntity: CostEntity)

    @Update
    fun updateCost(pCostEntity: CostEntity)
    @Query("DELETE FROM cost")
    suspend fun deleteAllCost()

    @Query("DELETE FROM cost where `index` = :pIndex")
    fun deleteCost(pIndex : Int)

}