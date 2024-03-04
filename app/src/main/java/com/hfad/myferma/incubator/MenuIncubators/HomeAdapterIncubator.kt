package com.hfad.myferma.incubator.MenuIncubators

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hfad.myferma.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit


class HomeAdapterIncubator constructor(
    private val id: List<String>,
    private val name: List<String>,
    private val type: List<String>,
    private val data: List<String>, private val listener: Listener, private val home: Boolean
) : RecyclerView.Adapter<HomeAdapterIncubator.ViewHolder?>() {


    interface Listener {
        fun onClick(position: Int, name: String, type: String, data: String, id: String)
    }

    class ViewHolder constructor(val cardView: MaterialCardView) : RecyclerView.ViewHolder(
        cardView
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val cv: MaterialCardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_captioned_image, parent, false) as MaterialCardView
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(
        holder: ViewHolder, position: Int
    ) {
        val cardView: MaterialCardView = holder.cardView
        val imageView: ImageView = cardView.findViewById<View>(R.id.info_image) as ImageView
        var diff: Long = 0

        //Установка картинки в карту
        var drawable: Drawable? = null
        if ("Курицы" == type[position]) {
            drawable = cardView.resources.getDrawable(R.drawable.chicken)
        } else if ("Гуси" == type[position]) {
            drawable = cardView.resources
                .getDrawable(R.drawable.external_goose_birds_icongeek26_outline_icongeek26)
        } else if ("Перепела" == type[position]) {
            drawable = cardView.resources.getDrawable(R.drawable.quail)
        } else if ("Утки" == type[position]) {
            drawable = cardView.resources.getDrawable(R.drawable.duck)
        } else if ("Индюки" == type[position]) {
            drawable = cardView.resources.getDrawable(R.drawable.turkeycock)
        }
        imageView.setImageDrawable(drawable)
        imageView.contentDescription = "22"

        var dateIncubator = ""
        if (home) {
            val calendar: Calendar = Calendar.getInstance()
            val dateBefore22: String = data[position].toString()
            val dateBefore222: String =
                (calendar.get(Calendar.DAY_OF_MONTH) + 1).toString() + "." + (calendar.get(
                    Calendar.MONTH
                ) + 1) + "." + calendar.get(Calendar.YEAR)
            val myFormat: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
            try {
                val date1: Date = myFormat.parse(dateBefore22)
                val date2: Date = myFormat.parse(dateBefore222)
                diff = date2.time - date1.time
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }
            dateIncubator =  "Идет " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString() + " день "
        } else {
            dateIncubator = "Завершено"
        }
        val textView: TextView = cardView.findViewById<View>(R.id.name) as TextView
        textView.text = name[position] // имя инкубатора

        val textView1: TextView = cardView.findViewById<View>(R.id.dayEnd) as TextView
        textView1.text = dateIncubator //Какой день

        cardView.setOnClickListener {
            listener.onClick(
                position,
                name[position],
                type[position],
                data[position],
                id[position]
            )
        }
    }

    override fun getItemCount(): Int {
        return id.size
    }
}