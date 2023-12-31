package com.youngs.mygym.ui.employee.member.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.youngs.mygym.databinding.ItemMemberBinding
import com.youngs.mygym.ui.employee.member.room.MemberEntity

class MemberListAdapter(): RecyclerView.Adapter<MemberListAdapter.MyViewHolder>() {

    var dataList = mutableListOf<MemberEntity>()
//    var dataList = mutableListOf<MemberEntity>()

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
        holder.tv_name.text = dataList[position].name
        holder.tv_membershipEndDate.text = dataList[position].membershipEndDate.plus(" 까지")

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
        fun onClick(v: View, position: Int, member : MemberEntity)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener
}