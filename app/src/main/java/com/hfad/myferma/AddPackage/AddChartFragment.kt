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
import com.hfad.myferma.R
import com.hfad.myferma.db.MyConstanta
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.util.Calendar


class AddChartFragment : Fragment() {

    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var animals_spiner: AutoCompleteTextView
    private lateinit var animals_spiner2: AutoCompleteTextView
    private lateinit var animals_spiner3: AutoCompleteTextView
    private var visitors = mutableListOf<BarEntry>()

    private var mountMass = mutableListOf<String>()
    private var mount = 0
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
        animals_spiner = layout.findViewById<View>(R.id.animals_spiner) as AutoCompleteTextView
        animals_spiner2 = layout.findViewById<View>(R.id.animals_spiner2) as AutoCompleteTextView
        animals_spiner3 = layout.findViewById<View>(R.id.animals_spiner3) as AutoCompleteTextView

        val calendar = Calendar.getInstance()

        // настройка спинеров
        animals_spiner2.setText("За весь год", false)
        animals_spiner3.setText(calendar[Calendar.YEAR].toString(), false)

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

        animals_spiner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                visitors.clear()
                storeDataInArrays()
                if (mount != 13) {
                    bar(mountMass)
                } else {
                    bar(labes)
                }

            }
        animals_spiner2.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                visitors.clear()
                storeDataInArrays()
                if (mount != 13) {
                    bar(mountMass)
                } else {
                    bar(labes)
                }

            }
        animals_spiner3.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                visitors.clear()
                storeDataInArrays()
                if (mount != 13) {
                    bar(mountMass)
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
        val barDataSet = BarDataSet(visitors, animals_spiner.text.toString())
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
            animals_spiner.setAdapter<ArrayAdapter<String>>(arrayAdapterProduct)

            // настройка спинера с годами (выглядил как обычный, и год запоминал)
            val arrayAdapterYear = ArrayAdapter<String>(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                yearList
            )
            animals_spiner3.setAdapter<ArrayAdapter<String>>(arrayAdapterYear)
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

        var x: Float
        var y: Float
        val animalsType: String = animals_spiner.text.toString()
        val mountString: String = animals_spiner2.text.toString()
        val year2: String = animals_spiner3.text.toString()

        setMount(mountString)

        when (mount) {

            in 1..12 -> {

                val cursor: Cursor = myDB.selectChartMount(
                    MyConstanta.DISCROTION, MyConstanta.TABLE_NAME, MyConstanta.TITLE, animalsType,
                    mount.toString(), year2
                )

                if (cursor.count != 0) {

                    while (cursor.moveToNext()) {

                        x = cursor.getString(0).toFloat()
                        y = cursor.getString(1).toFloat()
                        visitors.add(BarEntry(y, x))

                    }

                } else {
                    x = 0f
                    y = 0f
                    visitors.add(BarEntry(y, x))
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

                // если месяц пустой
            }

            else -> {
                x = 0f
                y = 0f
                visitors.add(BarEntry(y, x))
            }
        }
    }

    private fun setMount(mountString: String) {
        when (mountString) {
            "Январь" -> {
                mount = 1
                mountMass = arrayOf(
                    "",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    "31",
                    ""
                ).toMutableList()
            }

            "Февраль" -> {
                mount = 2
                mountMass = arrayOf(
                    "",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    ""
                ).toMutableList()
            }

            "Март" -> {
                mount = 3
                mountMass = arrayOf(
                    "",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    "31",
                    ""
                ).toMutableList()
            }

            "Апрель" -> {
                mount = 4
                mountMass = arrayOf(
                    "",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    ""
                ).toMutableList()
            }

            "Май" -> {
                mount = 5
                mountMass = arrayOf(
                    "",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    "31",
                    ""
                ).toMutableList()
            }

            "Июнь" -> {
                mount = 6
                mountMass = arrayOf(
                    "",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    ""
                ).toMutableList()
            }

            "Июль" -> {
                mount = 7
                mountMass = arrayOf(
                    "",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    "31",
                    ""
                ).toMutableList()
            }

            "Август" -> {
                mount = 8
                mountMass = arrayOf(
                    "",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    "31",
                    ""
                ).toMutableList()
            }

            "Сентябрь" -> {
                mount = 9
                mountMass = arrayOf(
                    "",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    ""
                ).toMutableList()
            }

            "Октябрь" -> {
                mount = 10
                mountMass = arrayOf(
                    "",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    "31",
                    ""
                ).toMutableList()
            }

            "Ноябрь" -> {
                mount = 11
                mountMass = arrayOf(
                    "",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    ""
                ).toMutableList()
            }

            "Декабрь" -> {
                mount = 12
                mountMass = arrayOf(
                    "",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    "31",
                    ""
                ).toMutableList()
            }

            "За весь год" -> mount = 13
        }
    }
}