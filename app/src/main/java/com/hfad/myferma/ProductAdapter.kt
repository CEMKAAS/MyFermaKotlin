package com.hfad.myferma

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat


class ProductAdapter constructor(
    private val produtsMap: Map<String, Double>,
    private val name: String,
    private val suffix: String
) : RecyclerView.Adapter<ProductAdapter.MyViewHolder?>() {
    private val productsList = mutableListOf<String>()
    private val unitList = mutableListOf<Double>()
    private val f: DecimalFormat = DecimalFormat("0.00")

    init {
        for (entry: Map.Entry<String, Double> in produtsMap.entries) {
            productsList.add(entry.key)
            unitList.add(entry.value)
        }
    }

    private fun unitString(animals: String): String {
        return if (suffix == " Шт.") {
            when (animals) {
                "Яйца" -> " шт."
                "Молоко" -> " л."
                "Мясо" -> " кг."
                else -> " ед."
            }
        } else " ₽"

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.my_row_products, parent, false)
        return ProductAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder, position: Int
    ) {
        val nameProuct: String = productsList[position]
        holder.products.text = name
        holder.count.text = nameProuct
        holder.unit.text = f.format(unitList[position]).toString() + unitString(nameProuct)
    }

    override fun getItemCount(): Int {
        return produtsMap.size
    }

    class MyViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var products: TextView
        var unit: TextView
        var count: TextView

        init {
            products = itemView.findViewById(R.id.products_text)
            unit = itemView.findViewById(R.id.unit_text)
            count = itemView.findViewById(R.id.count_text)
        }
    }
}