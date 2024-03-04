package com.hfad.myferma.ManagerMenuPackage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.myferma.R

class CustomAdapterAdd(
    private val productDB: List<ProductDB>,
    private var myRow: Int,
    private val listener: Listener
) :
    RecyclerView.Adapter<CustomAdapterAdd.MyViewHolder>() {

    interface Listener {
        fun onClick(position: Int, productDB: ProductDB)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(myRow, parent, false)
        return MyViewHolder(view, myRow)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder, position: Int
    ) {
        holder.idTxt.text = productDB[position].id.toString()
        holder.titleTxt.text = productDB[position].name.toString()
        holder.discTxt.text = productDB[position].disc.toString()
        holder.calendarTxt.text = productDB[position].data.toString()

        if (R.layout.my_row_write_off == myRow) {
            holder.photo!!.setImageResource(productDB[position].price)
        } else if (R.layout.my_row_sale == myRow) {
            holder.sixColum!!.text = productDB[position].price.toString()
        }

        holder.mainLayout.setOnClickListener {
            listener.onClick(position, productDB[position])
        }
    }

    override fun getItemCount(): Int {
        return productDB.size
    }

    inner class MyViewHolder(itemView: View, myRow: Int) : RecyclerView.ViewHolder(itemView) {
        var idTxt: TextView
        var titleTxt: TextView
        var discTxt: TextView
        var calendarTxt: TextView
        var sixColum: TextView? = null
        var mainLayout: LinearLayout
        var photo: ImageView? = null

        init {
            idTxt = itemView.findViewById(R.id.id_txt)
            titleTxt = itemView.findViewById(R.id.title_txt)
            discTxt = itemView.findViewById(R.id.disc_txt)
            calendarTxt = itemView.findViewById(R.id.calendar_txt)

            if (R.layout.my_row_write_off == myRow) {
                photo = itemView.findViewById<View>(R.id.status_txt) as ImageView
            } else if (R.layout.my_row_sale == myRow) {
                sixColum = itemView.findViewById(R.id.six_column)
            }

            mainLayout = itemView.findViewById(R.id.mainLayout)

            //Animate Recyclerview
//            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
//            mainLayout.setAnimation(translate_anim);
        }
    }
}