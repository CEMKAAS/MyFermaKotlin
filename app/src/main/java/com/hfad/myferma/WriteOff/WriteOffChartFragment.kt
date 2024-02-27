package com.hfad.myferma.WriteOff

import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import android.widget.RadioGroup
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

class WriteOffChartFragment : Fragment() {
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var layout: View

    private lateinit var animalsSpiner: AutoCompleteTextView
    private lateinit var mountSpiner: AutoCompleteTextView
    private lateinit var yearSpiner: AutoCompleteTextView

    private lateinit var radioButton1: RadioButton
    private lateinit var radioButton2: RadioButton
    private lateinit var radioGroup: RadioGroup
    private val labes = mutableListOf<String>(
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

    private var visitors = mutableListOf<BarEntry>()
    private var mountMass = mutableListOf<String>()

    private var mount: Int = 0
    private var status: Int = R.drawable.baseline_cottage_24

    private var yearList = mutableListOf<String>()
    private var productList = mutableListOf<String>()

    private var infoChart: String = "График списанной продукции на собсвенные нужды"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = inflater.inflate(R.layout.fragment_write_off_chart, container, false)

        //Подключение к базе данных
        myDB = MyFermaDatabaseHelper(requireContext())

        add()

        // установка радио
        radioGroup = layout.findViewById<View>(R.id.radioGroup) as RadioGroup
        radioButton1 = layout.findViewById<View>(R.id.radio_button_1) as RadioButton
        radioButton2 = layout.findViewById<View>(R.id.radio_button_2) as RadioButton

        // установка спинеров
        animalsSpiner = layout.findViewById<View>(R.id.animals_spiner) as AutoCompleteTextView
        mountSpiner = layout.findViewById<View>(R.id.mount_spiner) as AutoCompleteTextView
        yearSpiner = layout.findViewById<View>(R.id.year_spiner) as AutoCompleteTextView

        //Создание списка с данными для графиков
        val calendar = Calendar.getInstance()

        // настройка спинеров
        mountSpiner.setText("За весь год", false)
        yearSpiner.setText(calendar.get(Calendar.YEAR).toString(), false)

        //убириаем фаб кнопку
        val fab: ExtendedFloatingActionButton =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.visibility = View.GONE

        //настройка верхнего меню фаб кнопку
        val appBar: MaterialToolbar =
            requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.title = "Мои Списания - График"
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        appBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        //Логика просчета
        storeDataInArrays()

        // настройка графиков
        bar(labes, infoChart)

        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                R.id.radio_button_1 -> {
                    visitors.clear()
                    status = R.drawable.baseline_cottage_24
                    storeDataInArrays()
                    infoChart = "График списанной продукции на собсвенные нужды"
                    if (mount != 13) {
                        bar(mountMass, infoChart)
                    } else {
                        bar(labes, infoChart)
                    }
                }

                R.id.radio_button_2 -> {
                    visitors.clear()
                    status = R.drawable.baseline_delete_24
                    storeDataInArrays()
                    infoChart = "График списанной продукции на утилизацию"
                    if (mount != 13) {
                        bar(mountMass, infoChart)
                    } else {
                        bar(labes, infoChart)
                    }
                }
            }
        })
        animalsSpiner.setOnItemClickListener { parent, view, position, id ->
            visitors.clear()
            storeDataInArrays()
            if (mount != 13) {
                bar(mountMass, infoChart)
            } else {
                bar(labes, infoChart)
            }
        }
        mountSpiner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                visitors.clear()
                storeDataInArrays()
                if (mount != 13) {
                    bar(mountMass, infoChart)
                } else {
                    bar(labes, infoChart)
                }
            }
        yearSpiner.setOnItemClickListener { parent, view, position, id ->
            visitors.clear()
            storeDataInArrays()
            if (mount != 13) {
                bar(mountMass, infoChart)
            } else {
                bar(labes, infoChart)
            }
        }
        return layout
    }

    private fun bar(xAsis: MutableList<String>, info: String) {
        val barChart: BarChart = layout.findViewById(R.id.barChart)
        val barDataSet: BarDataSet = BarDataSet(visitors, animalsSpiner.text.toString())
//        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS)
        barDataSet.setValueTextColor(Color.BLACK)
        barDataSet.setValueTextSize(16f)
        val barData: BarData = BarData(barDataSet)
        barChart.invalidate()
        barChart.setFitBars(true)
        barChart.setData(barData)
        barChart.getDescription().setText(info)
        barChart.animateY(2000)
        val xAxis: XAxis = barChart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setDrawGridLines(false)
        xAxis.setGranularity(1f) // only intervals of 1 day
        xAxis.setLabelCount(6) //сколько отображается
        xAxis.setValueFormatter(IndexAxisValueFormatter(xAsis))
    }

    override fun onStart() {
        super.onStart()
        val view: View? = view
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

    //читаем БД для добавления списка в спинеры
    fun add() {
        val yearSet: MutableSet<String> = HashSet()
        val productSet: MutableSet<String> = HashSet()

        val cursor: Cursor = myDB.readAllDataWriteOff()
        while (cursor.moveToNext()) {
            val year: String = cursor.getString(5)
            val product: String = cursor.getString(1)
            yearSet.add(year)
            productSet.add(product)
        }
        cursor.close()

        yearList = yearSet.toMutableList()
        productList = productSet.toMutableList()
    }


//Логика добавления список для графиков
    fun storeDataInArrays() {

        val animalsType: String = animalsSpiner.text.toString()
        val mountString: String = mountSpiner.text.toString()
        val year2: String = yearSpiner.text.toString()

        setMount(mountString)

        when (mount) {
            in 1..12 -> allProductsMount(
                myDB.selectChartMountWriteOff(
                    animalsType,
                    mount.toString(),
                    year2,
                    status.toString()
                )
            )
            13 -> allProductsYear(
                myDB.selectChartYearWriteOff(
                    animalsType,
                    year2,
                    status.toString()
                )
            )
            else -> visitors.add(BarEntry(0f, 0f))
        }

    }

    //Считаем продукцию за месяц
    private fun allProductsMount(cursor: Cursor) {
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                visitors.add(BarEntry(cursor.getString(1).toFloat(), cursor.getString(0).toFloat()))
            }
        } else visitors.add(BarEntry(0f, 0f))
        cursor.close()
    }

    //Считаем продукцию за год
    private fun allProductsYear(cursor: Cursor) {

        for (i in 0..12) {
            visitors.add(BarEntry(i.toFloat(), 0f))
        }

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