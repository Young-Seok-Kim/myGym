package com.youngs.mygym.ui.employee.cost.list

import android.content.DialogInterface
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


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddCostBinding.inflate(layoutInflater)

        if (this::selectCost.isInitialized.not())
        {
            binding.customlayoutMonth.content.setText("1")
            binding.customlayoutCost.content.setText("120000")
            selectCost = CostEntity(binding.customlayoutMonth.content.text.toString(),binding.customlayoutCost.content.text.toString(),"")
        } else {
            binding.customlayoutMonth.content.setText(selectCost.month.toString())
            binding.customlayoutCost.content.setText(selectCost.cost.toString())
            binding.customlayoutRemark.content.setText(selectCost.remark)
        }

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
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }
}