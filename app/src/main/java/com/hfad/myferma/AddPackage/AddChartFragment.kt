package com.hfad.myferma.AddPackage

import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.hfad.myferma.ChartMount
import com.hfad.myferma.R
import com.hfad.myferma.db.MyConstanta
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.util.Calendar


class AddChartFragment : Fragment(){

    private val mountClass = ChartMount()
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var animalsSpiner: AutoCompleteTextView
    private lateinit var mountSpiner: AutoCompleteTextView
    private lateinit var yearSpiner: AutoCompleteTextView
    private var visitors = mutableListOf<BarEntry>()

    private var mount = 0
    private var mountString = "За весь год"

    private lateinit var layout: View
    private var yearList = mutableListOf<String>()
    private var productList = mutableListOf<String>()
    private val labes = mutableListOf(
        "",
        "Январь",
        "Февраль",
        "Март",
        "Апрель",
        "Май",
        "Июнь",
        "Июль",
        "Август",
        "Сентябрь",
        "Октябрь",
        "Ноябрь",
        "Декабрь",
        ""
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Подключение к базе данных
        myDB = MyFermaDatabaseHelper(requireContext())

        add()

        layout = inflater.inflate(R.layout.fragment_add_chart, container, false)
        // установка спинеров
        animalsSpiner = layout.findViewById<View>(R.id.animals_spiner) as AutoCompleteTextView
        mountSpiner = layout.findViewById<View>(R.id.animals_spiner2) as AutoCompleteTextView
        yearSpiner = layout.findViewById<View>(R.id.animals_spiner3) as AutoCompleteTextView

        val calendar = Calendar.getInstance()

        // настройка спинеров
        mountSpiner.setText(mountString, false)
        yearSpiner.setText(calendar[Calendar.YEAR].toString(), false)

        //убириаем фаб кнопку
        val fab: ExtendedFloatingActionButton =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.visibility = View.GONE

        // Настройка аппбара настройка стелочки назад
        val appBar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.title = "Мои товар - График"
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        appBar.setNavigationOnClickListener(View.OnClickListener { requireActivity().supportFragmentManager.popBackStack() })

        //Логика просчета
        storeDataInArrays()
        bar(labes)

        animalsSpiner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                visitors.clear()
                storeDataInArrays()
                if (mount != 13) {
                    bar(mountClass.setMount(mountString))
                } else {
                    bar(labes)
                }

            }
        mountSpiner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                visitors.clear()
                storeDataInArrays()
                if (mount != 13) {
                    bar(mountClass.setMount(mountString))
                } else {
                    bar(labes)
                }

            }
        yearSpiner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                visitors.clear()
                storeDataInArrays()

                if (mount != 13) {
                    bar(mountClass.setMount(mountString))
                } else {
                    bar(labes)
                }

            }
        return layout
    }

    private fun bar(xAsis: MutableList<String>) {
        //установка графиков
        val barChart: BarChart = layout.findViewById(R.id.barChart)
        // настройка графиков
        val barDataSet = BarDataSet(visitors, animalsSpiner.text.toString())
//        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS)
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 16f
        val barData = BarData(barDataSet)
        barChart.invalidate()
        barChart.setFitBars(true)
        barChart.data = barData
        barChart.description.text = "График добавленной продукции на склад"
        barChart.animateY(2000)
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 6 //сколько отображается
        xAxis.valueFormatter = IndexAxisValueFormatter(xAsis)
    }

    override fun onStart() {
        super.onStart()
        val view = view
        if (view != null) {
            //Настройка спинера с продуктами
            val arrayAdapterProduct = ArrayAdapter<String>(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                productList
            )
            animalsSpiner.setAdapter<ArrayAdapter<String>>(arrayAdapterProduct)

            // настройка спинера с годами (выглядил как обычный, и год запоминал)
            val arrayAdapterYear = ArrayAdapter<String>(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                yearList
            )
            yearSpiner.setAdapter<ArrayAdapter<String>>(arrayAdapterYear)
        }
    }

    fun add() {
        val yearSet = mutableSetOf<String>()
        val productSet = mutableSetOf<String>()

        val cursor = myDB.readAllData()

        while (cursor.moveToNext()) {
            val year = cursor.getString(5)
            val product = cursor.getString(1)
            yearSet.add(year)
            productSet.add(product)
        }

        cursor.close()

        yearList = yearSet.toMutableList()
        productList = productSet.toMutableList()

    }

    private fun storeDataInArrays() {

        val animalsType: String = animalsSpiner.text.toString()
        mountString = mountSpiner.text.toString()
        val year2: String = yearSpiner.text.toString()

        mount = mountClass.setMountInt(mountString)

        when (mount) {

            in 1..12 -> {

                val cursor: Cursor = myDB.selectChartMount(
                    MyConstanta.DISCROTION, MyConstanta.TABLE_NAME, MyConstanta.TITLE, animalsType,
                    mount.toString(), year2
                )

                if (cursor.count != 0) {

                    while (cursor.moveToNext()) {

                       val x = cursor.getString(0).toFloat()
                       val y = cursor.getString(1).toFloat()

                        visitors.add(BarEntry(y, x))

                    }

                } else {
                    visitors.add(BarEntry(0f, 0f))
                }

                cursor.close()


            }

            13 -> {

                for (i in 0..12) {
                    visitors.add(BarEntry(i.toFloat(), 0f))
                }

                val cursor = myDB.selectChartYear(
                    MyConstanta.DISCROTION,
                    MyConstanta.TABLE_NAME,
                    MyConstanta.TITLE,
                    animalsType,
                    year2
                )

                while (cursor.moveToNext()) {
                    when (cursor.getString(1).toInt()) {

                        1 -> visitors[0] = BarEntry(1f, cursor.getString(0).toFloat())
                        2 -> visitors[1] = BarEntry(2f, cursor.getString(0).toFloat())
                        3 -> visitors[2] = BarEntry(3f, cursor.getString(0).toFloat())
                        4 -> visitors[3] = BarEntry(4f, cursor.getString(0).toFloat())
                        5 -> visitors[4] = BarEntry(5f, cursor.getString(0).toFloat())
                        6 -> visitors[5] = BarEntry(6f, cursor.getString(0).toFloat())
                        7 -> visitors[6] = BarEntry(7f, cursor.getString(0).toFloat())
                        8 -> visitors[7] = BarEntry(8f, cursor.getString(0).toFloat())
                        9 -> visitors[8] = BarEntry(9f, cursor.getString(0).toFloat())
                        10 -> visitors[9] = BarEntry(10f, cursor.getString(0).toFloat())
                        11 -> visitors[10] = BarEntry(11f, cursor.getString(0).toFloat())
                        12 -> visitors[11] = BarEntry(12f, cursor.getString(0).toFloat())
                    }
                }
                cursor.close()
            }

            else -> {
                visitors.add(BarEntry(0f, 0f))
            }
        }
    }
}