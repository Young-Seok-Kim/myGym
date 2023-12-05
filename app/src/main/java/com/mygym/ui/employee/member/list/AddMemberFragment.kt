package com.mygym.ui.employee.member.list

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
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


class AddMemberFragment : DialogFragment() {

    lateinit var binding: FragmentAddMemberBinding

    private lateinit var onClickListener: OnDialogDismissListener

    lateinit var test : MemberEntity
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
        if (isUpdate){
            binding.textViewTitle.setText(R.string.update_member)
            binding.buttonAdd.setText(R.string.update_member)
        }else {
            binding.textViewTitle.setText(R.string.add_member)
            binding.buttonAdd.setText(R.string.add_member)
        }

        if (this::test.isInitialized.not())
        {
            test = MemberEntity("","","","","","","","")
        }
          else
        {
            binding.editTextName.setText(test.name)
            binding.editTextEmail.setText(test.email)
            binding.editTextPhoneNumber.setText(test.phoneNumber)
            binding.editTextAddress.setText(test.address)
            binding.editTextRemark.setText(test.remark)
        }

        Log.d("멤버",test.toString())

        binding.buttonAdd.setOnClickListener(){

            CoroutineScope(Dispatchers.IO).launch {
                val registMonth = if (binding.editTextRegistMonth.text.isBlank()){
                    0
                }
                else{
                    binding.editTextRegistMonth.text.toString().toLong()
                }

                val addMember = MemberEntity(
                    binding.editTextName.text.toString(),
                    LocalDate.now().toString(),
                    LocalDate.now().toString(),
                    LocalDate.now().plusMonths(registMonth).toString(),
                    binding.editTextPhoneNumber.text.toString(),
                    binding.editTextEmail.text.toString(),
                    binding.editTextAddress.text.toString(),
                    binding.editTextRemark.text.toString()
                )
                YoungsFunction.getDataBase(requireContext()).insert(addMember)
            }
            Toast.makeText(requireContext(),"${binding.editTextName.text} 회원이 추가 되었습니다.",Toast.LENGTH_SHORT).show()

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }
}