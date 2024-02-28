package com.hfad.myferma.Finance

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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.hfad.myferma.R
import com.hfad.myferma.db.MyConstanta
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.util.Calendar

class FinanceChart2Fragment : Fragment() {

    private lateinit var myDB: MyFermaDatabaseHelper

    private var entriesFirst = mutableListOf<Entry>()
    private var entriesSecond = mutableListOf<Entry>()
    private var entriesThird = mutableListOf<Entry>()

    private lateinit var mountSpiner: AutoCompleteTextView
    private lateinit var yearSpiner: AutoCompleteTextView
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
    private var mountMass = mutableListOf<String>()
    private lateinit var layout: View
    private var mount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Подключение к базе данных
        myDB = MyFermaDatabaseHelper(requireContext())

        layout = inflater.inflate(R.layout.fragment_finance_chart2, container, false)

        // установка спинеров
        mountSpiner = layout.findViewById<AutoCompleteTextView>(R.id.mount_spiner)
        yearSpiner = layout.findViewById<AutoCompleteTextView>(R.id.year_spiner)

        val calendar = Calendar.getInstance()
        // настройка спинеров
        mountSpiner.setText("За весь год", false)
        yearSpiner.setText(calendar.get(Calendar.YEAR).toString(), false)

        //убириаем фаб кнопку
        val fab: ExtendedFloatingActionButton =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.visibility = View.GONE

        val appBar: MaterialToolbar =
            requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.title = "Мои Финансы - Общее"
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        appBar.setNavigationOnClickListener { requireActivity().supportFragmentManager.popBackStack() }

        spiner()
        mountSpiner.setOnItemClickListener { parent, view, position, id -> spiner() }
        yearSpiner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> spiner() }

        return layout
    }

    private fun spiner() {
        entriesFirst.clear()
        entriesSecond.clear()
        entriesThird.clear()

        allProducts()

        val lineChart: LineChart = layout.findViewById(R.id.lineChart)
        lineChart.description.text = "График финансов"
        val datasetFirst: LineDataSet = LineDataSet(entriesFirst, "Прибыль")

        // График будет зеленого цвета
        datasetFirst.color = Color.GRAY
        // График будет плавным
        datasetFirst.mode = LineDataSet.Mode.LINEAR
        val datasetSecond: LineDataSet = LineDataSet(entriesSecond, "Чистая прибыль")
        // График будет зеленого цвета
        datasetSecond.color = Color.GREEN
        // График будет плавным
        datasetSecond.mode = LineDataSet.Mode.LINEAR
        val datasetThird: LineDataSet = LineDataSet(entriesThird, "Расходы")
        // График будет зеленого цвета
        datasetThird.color = Color.RED
        // График будет плавным
        datasetThird.mode = LineDataSet.Mode.LINEAR

        val dataSets: ArrayList<ILineDataSet> = arrayListOf()
        dataSets.add(datasetFirst)
        dataSets.add(datasetSecond)
        dataSets.add(datasetThird)

        val data: LineData = LineData(dataSets)
        lineChart.invalidate()
        lineChart.data = data
        lineChart.animateY(500)

        if (mount != 13) {
            xaxis(lineChart, mountMass)
        } else {
            xaxis(lineChart, labes)
        }
    }

    private fun xaxis(lineChart: LineChart, valueX: MutableList<String>) {
        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7
        xAxis.valueFormatter = IndexAxisValueFormatter(valueX)
    }

    override fun onStart() {
        super.onStart()
        val view: View? = view
        if (view != null) {
            // настройка спинера с годами (выглядил как обычный, и год запоминал)
            val arrayAdapterAnimals = ArrayAdapter<String>(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                add()
            )
            yearSpiner.setAdapter<ArrayAdapter<String>>(arrayAdapterAnimals)
        }
    }

    fun add(): MutableList<String> {
        val tempList: MutableSet<String> = HashSet()
        val cursor = myDB.readAllDataSale()

        while (cursor.moveToNext()) {
            tempList.add(cursor.getString(5))
        }
        cursor.close()

        return tempList.toMutableList()
    }

    private fun allProducts() {

        val mountString: String = mountSpiner.text.toString()
        val year2: String = yearSpiner.text.toString()

        setMount(mountString)

        when (mount) {

            in 1..12 -> {
                allProductsMount(
                    myDB.selectChartMountFinance2(
                        MyConstanta.PRICEALL,
                        MyConstanta.TABLE_NAMESALE,
                        mount.toString(),
                        year2
                    ), entriesFirst, 1
                )
                allProductsMount(
                    myDB.selectChartMountFinance2(
                        MyConstanta.TITLEEXPENSES,
                        MyConstanta.TABLE_NAMEEXPENSES,
                        mount.toString(),
                        year2
                    ), entriesThird, -1
                )
            }

            13 -> {

                allProductsYear( myDB.selectChartYearFinance2(
                    MyConstanta.PRICEALL,
                    MyConstanta.TABLE_NAMESALE,
                    year2), entriesFirst,  1)
                allProductsYear(myDB.selectChartYearFinance2(
                    MyConstanta.TITLEEXPENSES,
                    MyConstanta.TABLE_NAMEEXPENSES,
                    year2
                ), entriesThird, -1)

//                allProductsYear(myDB.selectChartYearSumFinance2(year2),entriesSecond,1)

                for (i in 0..11){
                    val x = entriesFirst[i].y
                    val y = entriesThird[i].y
                    entriesSecond[i] = Entry(i.toFloat(),x-y)
                }
            }

            else -> {
                entriesFirst.add(Entry(0f, 0f))
                entriesSecond.add(Entry(0f, 0f))
                entriesThird.add(Entry(0f, 0f))
            }
        }
    }

    private fun allProductsMount(
        cursor: Cursor,
        entries: MutableList<Entry>,
        kof: Int
    ) {
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                val y = cursor.getString(1).toFloat() * kof //Для вычитания
                val x = cursor.getString(0).toFloat()
                entries.add(Entry(x, y))
            }
        } else entries.add(Entry(0f, 0f))
        cursor.close()
    }

    private fun allProductsYear(
        cursor: Cursor,
        entries: MutableList<Entry>,
        kof: Int
    ) {
        for (i in 0..12) {
            entries.add(BarEntry(i.toFloat(), 0f))
        }

        while (cursor.moveToNext()) {
            when (cursor.getString(1).toInt()) {
                1 -> entries[0] = Entry(1f, (cursor.getString(0).toFloat())*kof)
                2 -> entries[1] = Entry(2f, (cursor.getString(0).toFloat())*kof)
                3 -> entries[2] = Entry(3f, (cursor.getString(0).toFloat())*kof)
                4 -> entries[3] = Entry(4f, (cursor.getString(0).toFloat())*kof)
                5 -> entries[4] = Entry(5f, (cursor.getString(0).toFloat())*kof)
                6 -> entries[5] = Entry(6f, (cursor.getString(0).toFloat())*kof)
                7 -> entries[6] = Entry(7f, (cursor.getString(0).toFloat())*kof)
                8 -> entries[7] = Entry(8f, (cursor.getString(0).toFloat())*kof)
                9 -> entries[8] = Entry(9f, (cursor.getString(0).toFloat())*kof)
                10 -> entries[9] = Entry(10f, (cursor.getString(0).toFloat())*kof)
                11 -> entries[10] = Entry(11f, (cursor.getString(0).toFloat())*kof)
                12 -> entries[11] = Entry(12f, (cursor.getString(0).toFloat())*kof)
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