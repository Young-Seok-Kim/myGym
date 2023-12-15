package com.youngs.mygym.common.customlayout

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.youngs.mygym.R
import com.youngs.mygym.databinding.CustomlayoutEdittextBinding

class CustomlayoutEditText (context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var binding: CustomlayoutEdittextBinding = CustomlayoutEdittextBinding.inflate(LayoutInflater.from(context), this, true)

    var title: TextView
        private set
    var content: EditText
        private set

    init {
        // attrs.xml에서 View의 속성 목록을 가져온다.
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomlayoutEditText)


        val mTitle = typedArray.getString(R.styleable.CustomlayoutEditText_textView_title)
        val mContentHint = typedArray.getString(R.styleable.CustomlayoutEditText_editText_content_hint)
        val mContentText = typedArray.getString(R.styleable.CustomlayoutEditText_editText_content_text)
        val mContentInputNumber = typedArray.getBoolean(R.styleable.CustomlayoutEditText_editText_input_number,false)

        binding.textViewTitle.text = mTitle
        binding.editTextContent.hint = mContentHint
        binding.editTextContent.setText(mContentText)

        if (mContentInputNumber)
            binding.editTextContent.inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)

        title = binding.textViewTitle
        content = binding.editTextContent

        // 데이터를 캐싱해두어 가비지컬렉션에서 제외시키도록 하는 함수
        // typedArray 사용 후 호출해야하나, 커스텀뷰가 반복 사용되는 것이 아니라면 호출하지 않아도 무방하다.
        typedArray.recycle()
    }

}