package com.youngs.mygym.ui.employee.cost.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngs.mygym.R
import com.youngs.mygym.databinding.FragmentMemberListBinding
import com.youngs.mygym.common.room.MyGymRoomDataBase
import com.youngs.mygym.ui.employee.cost.room.CostEntity
import com.youngs.mygym.ui.employee.member.room.MemberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CostListFragment : DialogFragment() {

    lateinit var binding: FragmentMemberListBinding
    private val adapter: CostListAdapter by lazy { CostListAdapter() }
    private val data = mutableListOf<CostEntity>()

    private lateinit var onClickListener: OnItemClickListener

    var forSelect : Boolean = false

    interface OnItemClickListener
    {
        fun whenItemClick(selectCost : CostEntity)
    }

    fun setOnClickListener(listener: OnItemClickListener)
    {
        this@CostListFragment.onClickListener = listener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMemberListBinding.inflate(layoutInflater, null, false)

        binding.title.text = getString(R.string.menu_manage_cost)
        initList()

        CoroutineScope(Dispatchers.IO).launch {
            getList()
        }

        initSelect()
    }

    private fun initSelect() {
        if (forSelect){
            binding.FAB.visibility = View.GONE
        }else
        {
            binding.FAB.setOnClickListener() {
                AddCostFragment().let {
                    it.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_MyGymDialog)
                    it.dialog?.window?.setWindowAnimations(android.R.style.Animation_Dialog)
                    it.showNow(childFragmentManager, "")
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return binding.root
    }

    private fun initList() {
        val mLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
        binding.listview.layoutManager = mLayoutManager

        adapter.setItemClickListener(object : CostListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int, cost: CostEntity) {
                if (forSelect){
                    onClickListener.whenItemClick(cost)
                    dismiss()
                } else{
                    AddCostFragment().let {
                        it.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_MyGymDialog)
                        it.dialog?.window?.setWindowAnimations(android.R.style.Animation_Dialog)
                        it.selectCost = cost
                        it.isUpdate = true
                        it.showNow(childFragmentManager, "")
                    }
                }
            }
        })

        binding.listview.adapter = adapter //리사이클러뷰에 어댑터 연결
    }

    private suspend fun getList() = flow<CostEntity> {
        MyGymRoomDataBase.getDatabase(requireContext()).costDao().getAllCost().collect() {
            adapter.dataList = it as MutableList<CostEntity>
            CoroutineScope(Dispatchers.Main).launch {
                adapter.notifyDataSetChanged()
            }
        }
    }.collect()

}