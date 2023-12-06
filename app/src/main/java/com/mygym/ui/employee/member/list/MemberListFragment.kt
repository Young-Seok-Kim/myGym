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
        CoroutineScope(Dispatchers.Default).launch Default@{
            CoroutineScope(Dispatchers.IO).launch IO@{
                val members =
                    MyGymRoomDataBase.getDatabase(requireContext()).memberDao().getAllMembers()
                data.clear()

                for (i in members.indices) {
                    data.add(i, members[i])
                }
                adapter.dataList = data
            }.join()
            CoroutineScope(Dispatchers.Main).launch Main@{
                adapter.notifyDataSetChanged()
            }.join()
        }
    }

    private fun initList() {
        val mLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
        binding.listview.layoutManager = mLayoutManager

        adapter.setItemClickListener(object : MemberListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int, member : MemberEntity) {
                AddMemberFragment().let {
                    it.setOnDismissListener(object : AddMemberFragment.OnDialogDismissListener {
                        override fun whenDismiss() {
                            getList()
                        }
                    })
                    it.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialogTheme)
                    it.dialog?.window?.setWindowAnimations(android.R.style.Animation_Dialog)
                    it.selectMember = member
                    it.isUpdate = true
                    it.showNow(childFragmentManager, "")
                }

            }
        })


        binding.listview.adapter = adapter //리사이클러뷰에 어댑터 연결
    }

}