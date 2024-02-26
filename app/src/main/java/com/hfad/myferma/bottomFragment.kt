package com.hfad.myferma

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.R

class bottomFragment : BottomSheetDialogFragment() {
    var TAG: String = "bottom"
    private val buttonSheet: Button? = null
    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout: View = inflater.inflate(R.layout.fragment_bottom, container, false)
        val animalsSpinerSheet: AutoCompleteTextView =
            layout.findViewById(R.id.animals_spiner_sheet)
        val dataSheet: TextInputLayout = layout.findViewById(R.id.data_sheet)
        val buttonSheet: Button = layout.findViewById(R.id.button_sheet)
        return layout
    }
}