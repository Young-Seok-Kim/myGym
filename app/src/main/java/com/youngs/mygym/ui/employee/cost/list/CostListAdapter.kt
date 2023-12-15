package com.youngs.mygym.ui.employee.cost.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.youngs.mygym.databinding.ItemMemberBinding
import com.youngs.mygym.ui.employee.cost.room.CostEntity

class CostListAdapter(): RecyclerView.Adapter<CostListAdapter.MyViewHolder>() {

    var dataList = mutableListOf<CostEntity>()

    inner class MyViewHolder(private val binding: ItemMemberBinding): RecyclerView.ViewHolder(binding.root) {
        var tv_name = binding.tvName
        var tv_membershipEndDate = binding.tvMembershipEndDate
        var memberRoot = binding.root
    }


    //만들어진 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding= ItemMemberBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_name.text = dataList[position].month.toString().plus("달")
        holder.tv_membershipEndDate.text = dataList[position].cost.toString().plus(" 원")

        holder.memberRoot.setOnClickListener(){
            itemClickListener.onClick(it,position,dataList[position])
        }

        if(position % 2 == 0){
            holder.memberRoot.setBackgroundColor(Color.parseColor("#f8f9fa"))
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int, cost : CostEntity)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener
}