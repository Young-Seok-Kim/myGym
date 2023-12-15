package com.youngs.mygym.ui.employee.member.list

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.youngs.mygym.R
import com.youngs.mygym.common.YoungsFunction
import com.youngs.mygym.databinding.FragmentAddMemberBinding
import com.youngs.mygym.ui.employee.cost.list.CostListFragment
import com.youngs.mygym.ui.employee.cost.room.CostEntity
import com.youngs.mygym.ui.employee.member.room.MemberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AddMemberFragment : DialogFragment() {

    lateinit var binding: FragmentAddMemberBinding

    lateinit var selectCost: CostEntity

    lateinit var selectMember : MemberEntity
    var isUpdate : Boolean = false


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddMemberBinding.inflate(layoutInflater)

        if (this::selectMember.isInitialized.not())
        {
            selectMember = MemberEntity("","","","","","","",0,"")
        }

        if (isUpdate){ // 수정일때
            binding.textViewTitle.setText(R.string.update_member)
            binding.buttonAdd.setText(R.string.update_member)
            binding.customlayoutSignUpDate.content.setText(selectMember.signUpDate) 
            binding.customlayoutName.content.setText(selectMember.name)
            binding.customlayoutEmail.content.setText(selectMember.email)
            binding.customlayoutPhoneNumber.content.setText(selectMember.phoneNumber)
            binding.customlayoutAddress.content.setText(selectMember.address)
            binding.customlayoutRemark.content.setText(selectMember.remark)
            
        }else { // 삽입일때
            binding.buttonDelete.visibility = View.GONE
            binding.textViewTitle.setText(R.string.add_member)
            binding.buttonAdd.setText(R.string.add_member)
            binding.customlayoutSignUpDate.content.setText(LocalDate.now().toString())
        }

        initButton()
    }

    private fun initButton(){
        binding.buttonAdd.setOnClickListener(){

            CoroutineScope(Dispatchers.IO).launch {
                val registMonth = if (this@AddMemberFragment::selectCost.isInitialized.not()){
                    0L
                }
                else{
                    selectCost.month.toLong()
                }

                if (isUpdate.not()) {
                    val addMember = MemberEntity(
                        binding.customlayoutName.content.text.toString(),
                        binding.customlayoutSignUpDate.content.text.toString(),
                        LocalDate.now().toString(),
                        LocalDate.now().plusMonths(registMonth).toString(),
                        binding.customlayoutPhoneNumber.content.text.toString(),
                        binding.customlayoutEmail.content.text.toString(),
                        binding.customlayoutAddress.content.text.toString(),
                        0,
                        binding.customlayoutRemark.content.text.toString()
                    )

                    YoungsFunction.getDataBase(requireContext()).memberDao().insertMember(addMember)
                }
                else {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val endDate: LocalDate = LocalDate.parse(selectMember.membershipEndDate, formatter)

                    selectMember.name = binding.customlayoutName.content.text.toString()
                    selectMember.phoneNumber = binding.customlayoutPhoneNumber.content.text.toString()
                    selectMember.membershipEndDate = endDate.plusMonths(registMonth).toString()
                    selectMember.email = binding.customlayoutEmail.content.text.toString()
                    selectMember.address = binding.customlayoutAddress.content.text.toString()
                    selectMember.remark = binding.customlayoutRemark.content.text.toString()
                    YoungsFunction.getDataBase(requireContext()).memberDao().updateMember(selectMember)
                }
            }
            if (isUpdate.not()) {
                Toast.makeText(
                    requireContext(),
                    "${binding.customlayoutName.content.text} 회원이 추가 되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{
                Toast.makeText(requireContext(),"${binding.customlayoutName.content.text} 회원이 수정 되었습니다.",Toast.LENGTH_SHORT).show()
            }

            dismiss()
        }

        binding.buttonDelete.setOnClickListener(){

            YoungsFunction.messageBoxOKCancelAction(requireContext(),getString(R.string.warning),"${selectMember.name} 회원을 삭제 하시겠습니까?",
                OKAction = {
                    CoroutineScope(Dispatchers.Default).launch {
                        CoroutineScope(Dispatchers.IO).launch {
                            YoungsFunction.getDataBase(requireContext()).memberDao().deleteMember(selectMember.name)
                        }.join()
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(requireContext(), "${selectMember.name} 회원이 삭제 되었습니다.",Toast.LENGTH_SHORT).show()
                            dismiss()
                        }.join()
                    }
                },
                cancelAction = {
                })
        }

        binding.buttonRegistMonth.setOnClickListener {
            CostListFragment().let {
                it.setOnClickListener(object : CostListFragment.OnItemClickListener{
                    override fun whenItemClick(selectCost : CostEntity) {
                        binding.textViewSelectRegistMonth.text = selectCost.month
                        this@AddMemberFragment.selectCost = selectCost
                    }
                })
                it.forSelect = true
                it.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialogTheme)
                it.dialog?.window?.setWindowAnimations(android.R.style.Animation_Dialog)
                it.showNow(childFragmentManager, "")
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }
}