package com.mygym.ui.employee.member.list

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mygym.R
import com.mygym.common.YoungsFunction
import com.mygym.databinding.FragmentAddMemberBinding
import com.mygym.ui.employee.room.member.MemberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AddMemberFragment : DialogFragment() {

    lateinit var binding: FragmentAddMemberBinding

    private lateinit var onClickListener: OnDialogDismissListener

    lateinit var selectMember : MemberEntity
    var isUpdate : Boolean = false


    interface OnDialogDismissListener
    {
        fun whenDismiss()
    }

    fun setOnDismissListener(listener: OnDialogDismissListener)
    {
        this@AddMemberFragment.onClickListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onClickListener.whenDismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddMemberBinding.inflate(layoutInflater)

        if (this::selectMember.isInitialized.not())
        {
            selectMember = MemberEntity("","","","","","","","")
        }

        if (isUpdate){ // 수정일때
            binding.textViewTitle.setText(R.string.update_member)
            binding.buttonAdd.setText(R.string.update_member)
            binding.textViewSignupDate.text = selectMember.signUpDate
            binding.editTextName.setText(selectMember.name)
            binding.editTextEmail.setText(selectMember.email)
            binding.editTextPhoneNumber.setText(selectMember.phoneNumber)
            binding.editTextAddress.setText(selectMember.address)
            binding.editTextRemark.setText(selectMember.remark)
        }else { // 삽입일때
            binding.buttonDelete.visibility = View.GONE
            binding.textViewTitle.setText(R.string.add_member)
            binding.buttonAdd.setText(R.string.add_member)
            binding.textViewSignupDate.text = LocalDate.now().toString()
        }


        binding.buttonAdd.setOnClickListener(){

            CoroutineScope(Dispatchers.IO).launch {
                val registMonth = if (binding.editTextRegistMonth.text.isBlank()){
                    0
                }
                else{
                    binding.editTextRegistMonth.text.toString().toLong()
                }

                if (isUpdate.not()) {
                    val addMember = MemberEntity(
                        binding.editTextName.text.toString(),
                        binding.textViewSignupDate.text.toString(),
                        LocalDate.now().toString(),
                        LocalDate.now().plusMonths(registMonth).toString(),
                        binding.editTextPhoneNumber.text.toString(),
                        binding.editTextEmail.text.toString(),
                        binding.editTextAddress.text.toString(),
                        binding.editTextRemark.text.toString()
                    )

                    YoungsFunction.getDataBase(requireContext()).insertMember(addMember)
                }
                else {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val endDate: LocalDate = LocalDate.parse(selectMember.membershipEndDate, formatter)

                    selectMember.name = binding.editTextName.text.toString()
                    selectMember.phoneNumber = binding.editTextPhoneNumber.text.toString()
                    selectMember.membershipEndDate = endDate.plusMonths(registMonth).toString()
                    selectMember.email = binding.editTextEmail.text.toString()
                    selectMember.address = binding.editTextAddress.text.toString()
                    selectMember.remark = binding.editTextRemark.text.toString()
                    YoungsFunction.getDataBase(requireContext()).updateMember(selectMember)
                }
            }
            if (isUpdate.not()) {
                Toast.makeText(
                    requireContext(),
                    "${binding.editTextName.text} 회원이 추가 되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{
                Toast.makeText(requireContext(),"${binding.editTextName.text} 회원이 수정 되었습니다.",Toast.LENGTH_SHORT).show()
            }

            dismiss()
        }

        binding.buttonDelete.setOnClickListener(){

            YoungsFunction.messageBoxOKCancelAction(requireContext(),getString(R.string.warning),"${selectMember.name} 회원을 삭제 하시겠습니까?",
                OKAction = {
                    CoroutineScope(Dispatchers.Default).launch {
                        CoroutineScope(Dispatchers.IO).launch {
                            YoungsFunction.getDataBase(requireContext()).deleteMember(selectMember.name)
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
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }
}