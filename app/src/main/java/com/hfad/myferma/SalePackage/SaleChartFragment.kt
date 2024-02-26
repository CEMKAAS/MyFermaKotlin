package com.hfad.myferma.SalePackage

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
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.util.Calendar

class SaleChartFragment : Fragment() {
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var animals_spiner: AutoCompleteTextView
    private lateinit var mount_spiner: AutoCompleteTextView
    private lateinit var year_spiner: AutoCompleteTextView
    private var visitors = mutableListOf<BarEntry>()
    private val labes: Array<String> = arrayOf(
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
    private var mountMass = mutableListOf<String>()
    private var mount: Int = 0
    private lateinit var layout: View
    private var yearList = mutableListOf<String>()
    private var productList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = inflater.inflate(R.layout.fragment_sale_chart, container, false)
        // установка спинеров

        animals_spiner = layout.findViewById<View>(R.id.animals_spiner) as AutoCompleteTextView
        mount_spiner = layout.findViewById<View>(R.id.mount_spiner) as AutoCompleteTextView
        year_spiner = layout.findViewById<View>(R.id.year_spiner) as AutoCompleteTextView

        //Подключение к базе данных
        myDB = MyFermaDatabaseHelper(requireContext())

        add()

        val calendar: Calendar = Calendar.getInstance()

        // настройка спинеров
        mount_spiner.setText("За весь год", false)
        year_spiner.setText(calendar.get(Calendar.YEAR).toString(), false)

        //убириаем фаб кнопку
        val fab: ExtendedFloatingActionButton =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.visibility = View.GONE

        val appBar: MaterialToolbar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.title = "Мои продажи - График"
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        appBar.setNavigationOnClickListener { requireActivity().supportFragmentManager.popBackStack() }

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
        mount_spiner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                visitors.clear()
                storeDataInArrays()
                if (mount != 13) {
                    bar(mountMass)
                } else {
                    bar(labes)
                }
            }
        year_spiner.onItemClickListener =
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

    override fun onStart() {
        super.onStart()
        val view: View? = view
        if (view != null) {
            //Настройка спинера с продуктами
           val  arrayAdapterProduct = ArrayAdapter<String>(
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
            year_spiner.setAdapter<ArrayAdapter<String>>(arrayAdapterYear)
        }
    }

    fun add() {
        val yearSet: MutableSet<String> = HashSet()
        val productSet: MutableSet<String> = HashSet()

        val cursor: Cursor = myDB.readAllDataSale()
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

    private fun bar(xAsis: Array<String>) {
        val barChart: BarChart = layout.findViewById(R.id.barChart)
        val barDataSet: BarDataSet = BarDataSet(visitors, animals_spiner.text.toString())
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS)
        barDataSet.setValueTextColor(Color.BLACK)
        barDataSet.setValueTextSize(16f)
        val barData: BarData = BarData(barDataSet)
        barChart.invalidate()
        barChart.setFitBars(true)
        barChart.data = barData
        barChart.description.text = "График проданной продукции со склада"
        barChart.animateY(2000)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setDrawGridLines(false)
        xAxis.setGranularity(1f) // only intervals of 1 day
        xAxis.setLabelCount(6) //сколько отображается
        xAxis.setValueFormatter(IndexAxisValueFormatter(xAsis))
    }

    private fun storeDataInArrays() {
        val cursor: Cursor = myDB.readAllDataSale()
        var x: Float
        var y: Float
        val animalsType: String = animals_spiner.text.toString()
        val mountString: String = mount_spiner.text.toString()
        val year2: String = year_spiner.text.toString()
        var jan: Float = 0f
        var feb: Float = 0f
        var mar: Float = 0f
        var apr: Float = 0f
        var mai: Float = 0f
        var jun: Float = 0f
        var jul: Float = 0f
        var aug: Float = 0f
        var sep: Float = 0f
        var oct: Float = 0f
        var nov: Float = 0f
        var dec: Float = 0f
        setMount(mountString)
        cursor.moveToNext()
        if (mount <= 12 && mount > 0) {
            if ((animalsType == cursor.getString(1))) {
                //проверка месяца
                if (mount == cursor.getString(4).toInt()) {
                    //проверка года
                    if ((year2 == cursor.getString(5))) {
                        x = cursor.getString(2).toFloat()
                        y = cursor.getString(3).toFloat()
                        visitors.add(BarEntry(y, x))
                    }
                }
            }
            while (cursor.moveToNext()) {
                //проверка товара
                if ((animalsType == cursor.getString(1))) {
                    //проверка месяца
                    if (mount == cursor.getString(4).toInt()) {
                        //проверка года
                        if ((year2 == cursor.getString(5))) {
                            x = cursor.getString(2).toFloat()
                            y = cursor.getString(3).toFloat()
                            visitors.add(BarEntry(y, x))
                        }
                    }
                }
            }
            cursor.close()
        } else if (cursor.count == 0) {
            x = 0f
            y = 0f
            visitors.add(BarEntry(y, x))
        } else if (mount == 13) {
            if ((animalsType == cursor.getString(1))) {
                if (mount == 13) {
                    //проверка года
                    if ((year2 == cursor.getString(5))) {
                        when (cursor.getString(4).toInt()) {
                            1 -> jan += cursor.getString(2).toFloat()
                            2 -> feb += cursor.getString(2).toFloat()
                            3 -> mar += cursor.getString(2).toFloat()
                            4 -> apr += cursor.getString(2).toFloat()
                            5 -> mai += cursor.getString(2).toFloat()
                            6 -> jun += cursor.getString(2).toFloat()
                            7 -> jul += cursor.getString(2).toFloat()
                            8 -> aug += cursor.getString(2).toFloat()
                            9 -> sep += cursor.getString(2).toFloat()
                            10 -> oct += cursor.getString(2).toFloat()
                            11 -> nov += cursor.getString(2).toFloat()
                            12 -> dec += cursor.getString(2).toFloat()
                        }
                    }
                }
            } else {
                x = 0f
                y = 0f
                visitors.add(BarEntry(y, x))
            }
            while (cursor.moveToNext()) {
                if ((animalsType == cursor.getString(1))) {
                    if (mount == 13) {
                        //проверка года
                        if ((year2 == cursor.getString(5))) {
                            when (cursor.getString(4).toInt()) {
                                1 -> jan += cursor.getString(2).toFloat()
                                2 -> feb += cursor.getString(2).toFloat()
                                3 -> mar += cursor.getString(2).toFloat()
                                4 -> apr += cursor.getString(2).toFloat()
                                5 -> mai += cursor.getString(2).toFloat()
                                6 -> jun += cursor.getString(2).toFloat()
                                7 -> jul += cursor.getString(2).toFloat()
                                8 -> aug += cursor.getString(2).toFloat()
                                9 -> sep += cursor.getString(2).toFloat()
                                10 -> oct += cursor.getString(2).toFloat()
                                11 -> nov += cursor.getString(2).toFloat()
                                12 -> dec += cursor.getString(2).toFloat()
                            }
                        }
                    }
                }
            }
            cursor.close()
            visitors.add(BarEntry(1, jan))
            visitors.add(BarEntry(2, feb))
            visitors.add(BarEntry(3, mar))
            visitors.add(BarEntry(4, apr))
            visitors.add(BarEntry(5, mai))
            visitors.add(BarEntry(6, jun))
            visitors.add(BarEntry(7, jul))
            visitors.add(BarEntry(8, aug))
            visitors.add(BarEntry(9, sep))
            visitors.add(BarEntry(10, oct))
            visitors.add(BarEntry(11, nov))
            visitors.add(BarEntry(12, dec))
            // если месяц пустой
        } else {
            x = 0f
            y = 0f
            visitors.add(BarEntry(y, x))
        }
    }

    fun setMount(mountString: String?) {
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