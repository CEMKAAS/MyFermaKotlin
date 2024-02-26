package com.hfad.myferma.incubator

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.R

class AddAdapterIncubator(
    private val massTemp: MutableList<String>,
    private val massDamp: MutableList<String>,
    private val massOver: MutableList<String>,
    private val massAiring: MutableList<String>,
    private val listener: Listener
) : RecyclerView.Adapter<AddAdapterIncubator.MyViewHolder>() {

    interface Listener {
        fun onClick(position: Int, day: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.my_row_incubator_edit, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.idTxt.text = (position + 1).toString()
        holder.titleTxt2.setText(massTemp[position])
        holder.discTxt2.setText(massDamp[position])
        holder.calendarTxt2.setText(massOver[position])
        holder.airingtxt2.setText(massAiring[position])

        holder.mainLayout.setOnClickListener {
//                listener.onClick(position, position + 1)
        }

        holder.titleTxt2.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
               massTemp[position] = p0.toString()
            }
        })

        holder.discTxt2.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                massDamp[position] = p0.toString()
            }
        })
        holder.calendarTxt2.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                massOver[position] = p0.toString()
            }
        })
        holder.airingtxt2.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                massAiring[position] = p0.toString()
            }
        })




    }

override fun getItemCount(): Int {
    return massTemp.size

}

inner class MyViewHolder constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    var idTxt: TextView
    var titleTxt2: EditText
    var discTxt2: EditText
    var calendarTxt2: EditText
    var airingtxt2: EditText
    var mainLayout: LinearLayout

    init {
        idTxt = itemView.findViewById(R.id.id_txt)
        titleTxt2 = itemView.findViewById(R.id.temp_edit)
        discTxt2 = itemView.findViewById(R.id.damp_edit)
        calendarTxt2 = itemView.findViewById(R.id.over_edit)
        airingtxt2 = itemView.findViewById(R.id.airing_edit)
        mainLayout = itemView.findViewById(R.id.mainLayout)
    }

}
}
