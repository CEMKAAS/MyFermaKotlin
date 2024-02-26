package com.hfad.myferma.incubator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.R

class ListAdapterIncubator(
    private val massId: MutableList<String>,
    private val massTemp: MutableList<String>,
    private val massDamp: MutableList<String>,
    private val massOver: MutableList<String>,
    private val massAiring: MutableList<String>,
    private val listener: Listener

) : RecyclerView.Adapter<ListAdapterIncubator.MyViewHolder>() {

    interface Listener {
        fun onClick(position: Int, day: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.my_row_incubator, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.idTxt.text = "День " + (position + 1).toString()
        holder.titleTxt.text = massTemp[position]
        holder.discTxt.text = massDamp[position]
        holder.calendarTxt.text = massOver[position]
        holder.airingtxt.text = massAiring[position]

        holder.mainLayout.setOnClickListener {
            listener.onClick(position, position)
        }
    }

    override fun getItemCount(): Int {
        return if (massId[2] == "Курицы") {
            21
        } else if (massId[2] == "Индюки") {
            28
        } else if (massId[2] == "Гуси") {
            30
        } else if (massId[2] == "Утки") {
            28
        } else if (massId[2] == "Перепела") {
            17
        } else {
            30
        }
    }

    inner class MyViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var idTxt: TextView
        var titleTxt: TextView
        var discTxt: TextView
        var calendarTxt: TextView
        var airingtxt: TextView
        var mainLayout: LinearLayout

        init {
            idTxt = itemView.findViewById(R.id.id_txt)
            titleTxt = itemView.findViewById(R.id.title_txt)
            discTxt = itemView.findViewById(R.id.disc_txt)
            calendarTxt = itemView.findViewById(R.id.calendar_txt)
            airingtxt = itemView.findViewById(R.id.airing_txt)
            mainLayout = itemView.findViewById(R.id.mainLayout)

        }
    }
}