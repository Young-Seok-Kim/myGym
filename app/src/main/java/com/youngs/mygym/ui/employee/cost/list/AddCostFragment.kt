package com.youngs.mygym.ui.employee.cost.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.youngs.mygym.R
import com.youngs.mygym.common.YoungsFunction
import com.youngs.mygym.databinding.FragmentAddCostBinding
import com.youngs.mygym.ui.employee.cost.room.CostEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddCostFragment : DialogFragment() {

    lateinit var binding: FragmentAddCostBinding


    lateinit var selectCost : CostEntity
    var isUpdate : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddCostBinding.inflate(layoutInflater)

        if (isUpdate.not()) // insert일때
        {
            binding.customlayoutMonth.content.setText("1")
            binding.customlayoutCost.content.setText("120000")
            binding.buttonDelete.visibility = View.GONE
            selectCost = CostEntity(binding.customlayoutMonth.content.text.toString(),binding.customlayoutCost.content.text.toString(),"")
        } else { // update 일때
            binding.customlayoutMonth.content.setText(selectCost.month.toString())
            binding.customlayoutCost.content.setText(selectCost.cost.toString())
            binding.customlayoutRemark.content.setText(selectCost.remark)
        }

        initButton()
    }

    private fun initButton(){
        binding.buttonAdd.setOnClickListener(){

            CoroutineScope(Dispatchers.IO).launch {

                if (isUpdate.not()) {
                    selectCost = CostEntity(binding.customlayoutMonth.content.text.toString(),binding.customlayoutCost.content.text.toString(),binding.customlayoutRemark.content.text.toString())
                    YoungsFunction.getDataBase(requireContext()).costDao().insertCost(selectCost)
                }
                else {
                    selectCost.month = binding.customlayoutMonth.content.text.toString()
                    selectCost.cost = binding.customlayoutCost.content.text.toString()
                    selectCost.remark = binding.customlayoutRemark.content.text.toString()
                    YoungsFunction.getDataBase(requireContext()).costDao().updateCost(selectCost)
                }
            }
            if (isUpdate.not()) {
                Toast.makeText(requireContext(),"요금이 추가 되었습니다.",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(),"요금이 변경 되었습니다.",Toast.LENGTH_SHORT).show()
            }

            dismiss()
        }

        binding.buttonDelete.setOnClickListener(){
            YoungsFunction.messageBoxOKCancelAction(requireContext(),getString(R.string.warning),"${selectCost.month}달 요금제를 삭제 하시겠습니까?",
                OKAction = {
                    CoroutineScope(Dispatchers.Default).launch {
                        CoroutineScope(Dispatchers.IO).launch {
                            YoungsFunction.getDataBase(requireContext()).costDao().deleteCost(selectCost.index)
                        }.join()
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(requireContext(), "${selectCost.month}달 요금제가 삭제 되었습니다.",Toast.LENGTH_SHORT).show()
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