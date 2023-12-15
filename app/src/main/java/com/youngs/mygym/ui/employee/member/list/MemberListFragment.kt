package com.youngs.mygym.ui.employee.member.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngs.mygym.R
import com.youngs.mygym.databinding.FragmentMemberListBinding
import com.youngs.mygym.common.room.MyGymRoomDataBase
import com.youngs.mygym.ui.employee.member.room.MemberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MemberListFragment : DialogFragment() {

    lateinit var binding: FragmentMemberListBinding
    private val adapter: MemberListAdapter by lazy { MemberListAdapter() }
    private val data = mutableListOf<MemberEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMemberListBinding.inflate(layoutInflater, null, false)

        binding.title.text = getString(R.string.menu_manage_member)

        initList()

        CoroutineScope(Dispatchers.IO).launch {
            getList()
        }

        binding.FAB.setOnClickListener() {
            AddMemberFragment().let {
                it.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialogTheme)
                it.dialog?.window?.setWindowAnimations(android.R.style.Animation_Dialog)
                it.showNow(childFragmentManager, "")
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

        adapter.setItemClickListener(object : MemberListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int, member: MemberEntity) {
                AddMemberFragment().let {
                    it.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialogTheme)
                    it.dialog?.window?.setWindowAnimations(android.R.style.Animation_Dialog)
                    it.selectMember = member
                    it.isUpdate = true
                    it.showNow(childFragmentManager, "")
                }

            }
        })


        binding.listview.adapter = adapter
    }

    private suspend fun getList() = flow<MemberEntity> {
        MyGymRoomDataBase.getDatabase(requireContext()).memberDao().getAllMembers().collect() {
            adapter.dataList = it as MutableList<MemberEntity>
            CoroutineScope(Dispatchers.Main).launch {
                adapter.notifyDataSetChanged()
            }
        }
    }.collect()
}