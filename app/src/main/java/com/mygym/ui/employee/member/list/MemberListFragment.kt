package com.mygym.ui.employee.member.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mygym.R
import com.mygym.databinding.FragmentMemberListBinding
import com.mygym.ui.employee.room.MyGymRoomDataBase
import com.mygym.ui.employee.room.member.MemberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemberListFragment : DialogFragment() {

    lateinit var binding: FragmentMemberListBinding
    private val adapter: MemberListAdapter by lazy { MemberListAdapter() }
    private val data = mutableListOf<MemberEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMemberListBinding.inflate(layoutInflater, null, false)

        initList()
        getList()
        binding.FAB.setOnClickListener() {
            AddMemberFragment().let {
                it.setOnDismissListener(object : AddMemberFragment.OnDialogDismissListener {
                    override fun whenDismiss() {
                        Log.d("닫음", "ㅈㄱㄴ")
                        getList()
                    }
                })
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

    private fun getList() {
        CoroutineScope(Dispatchers.IO).launch {
            val members =
                MyGymRoomDataBase.getDatabase(requireContext()).memberDao().getAllMembers()
            data.clear()

            for (i in members.indices) {
                data.add(i, members[i])
            }
            adapter.dataList = data
            Log.d("adapter.dataList", adapter.dataList.toString())
        }
        adapter.notifyDataSetChanged()
    }

    private fun initList() {
        val mLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
        binding.listview.layoutManager = mLayoutManager

        adapter.setItemClickListener(object : MemberListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int, member : MemberEntity) {
                Log.d(
                    "test",
                    " index : " + member.index
                )
                AddMemberFragment().let {
                    it.setOnDismissListener(object : AddMemberFragment.OnDialogDismissListener {
                        override fun whenDismiss() {
                            Log.d("닫음", "ㅈㄱㄴ")
                            getList()
                        }
                    })
                    it.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialogTheme)
                    it.dialog?.window?.setWindowAnimations(android.R.style.Animation_Dialog)
                    it.test = member
                    it.isUpdate = true
                    it.showNow(childFragmentManager, "")
                }

            }
        })

        binding.listview.adapter = adapter //리사이클러뷰에 어댑터 연결
    }

}