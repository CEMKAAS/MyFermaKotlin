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

class FinanceChartFragment : Fragment() {
    private lateinit var myDB: MyFermaDatabaseHelper
    private var visitors = mutableListOf<Entry>()
    private lateinit var animalsSpiner: AutoCompleteTextView
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
    private var idColor: Int = 0
    private var year: String? = null

    private var yearList = mutableListOf<String>()
    private var productList = mutableListOf<String>()
    private var productListAll = mutableListOf<String>()

    private var sumCategory = mutableMapOf<String, MutableList<Entry>>()

    private var sumProductYan = mutableMapOf<String, ArrayList<Entry>>()
    private var sumProductFeb = mutableMapOf<String, ArrayList<Entry>>()
    private var sumProductMar = mutableMapOf<String, ArrayList<Entry>>()
    private var sumProductApr = mutableMapOf<String, ArrayList<Entry>>()
    private var sumProductMay = mutableMapOf<String, ArrayList<Entry>>()
    private var sumProductJun = mutableMapOf<String, ArrayList<Entry>>()
    private var sumProductJar = mutableMapOf<String, ArrayList<Entry>>()
    private var sumProductAvg = mutableMapOf<String, ArrayList<Entry>>()
    private var sumProductSep = mutableMapOf<String, ArrayList<Entry>>()
    private var sumProductOkt = mutableMapOf<String, ArrayList<Entry>>()
    private var sumProductNov = mutableMapOf<String, ArrayList<Entry>>()
    private var sumProductDec = mutableMapOf<String, ArrayList<Entry>>()
    private var sumProductAll = mutableMapOf<String, ArrayList<Entry>>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Подключение к базе данных и к календарю
        myDB = MyFermaDatabaseHelper(requireContext())

        add()

        layout = inflater.inflate(R.layout.fragment_finance_chart, container, false)

        // установка спинеров
        animalsSpiner = layout.findViewById<AutoCompleteTextView>(R.id.animals_spiner)
        mountSpiner = layout.findViewById<AutoCompleteTextView>(R.id.animals_spiner2)
        yearSpiner = layout.findViewById<AutoCompleteTextView>(R.id.animals_spiner3)

        // настройка спинеров
        animalsSpiner.setText("Все", false)
        mountSpiner.setText("За весь год", false)

        val calendar = Calendar.getInstance()
        yearSpiner.setText(calendar.get(Calendar.YEAR).toString(), false)
        year = yearSpiner.text.toString()

        //убириаем фаб кнопку
        val fab: ExtendedFloatingActionButton =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.visibility = View.GONE

        val appBar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.title = "Мои Финансы - Продукция"
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        appBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        //Логика просчета
        allProducts()

        //Формируем списки
        all()

        // Формируем график
        spiner()

        animalsSpiner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> spiner() }
        mountSpiner.setOnItemClickListener { parent, view, position, id -> spiner() }
        yearSpiner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> spiner() }

        return layout
    }

    fun spiner() {
        visitors.clear()

        val animalsType: String = animalsSpiner.text.toString()
        val mountString: String = mountSpiner.text.toString()
        val year2: String = yearSpiner.text.toString()

        setMount(mountString)

        val lineChart: LineChart = layout.findViewById(R.id.lineChart)
        lineChart.description.text = "График продукции"

        if (animalsType == "Все") {

            val dataSets: ArrayList<ILineDataSet?> = ArrayList<Any?>()

            if (mount in 1..12) {

                allProductsMount(year2)

                for (product: String in productList) {
                    greatChar(product, dataSets, sumCategory)
                }

            } else {
                if (year == year2) {

                    for (product: String in productList) {
                        greatChar(product, dataSets, sumProductAll)
                    }

                } else {
                    year = year2
                    //Логика просчета
                    allProducts()
                    //Формируем списки
                    all()
                    for (product: String in productList) {
                        greatChar(product, dataSets, sumProductAll)
                    }
                }
            }

            val data: LineData = LineData(dataSets)
            lineChart.invalidate()
            lineChart.data = data
            lineChart.animateY(500)

            if (mount != 13) {
                xaxis(lineChart, mountMass)
            } else {
                xaxis(lineChart, labes)
            }
        } else {
            //Если не все
            storeDataInArrays(animalsType, mountString, year2)

            val dataset: LineDataSet = LineDataSet(visitors, animalsType)
            val data: LineData = LineData(dataset)
            lineChart.invalidate()
            lineChart.data = data
            lineChart.animateY(500)

            if (mount != 13) {
                xaxis(lineChart, mountMass)
            } else {
                xaxis(lineChart, labes)
            }
        }
    }

    // Добавление графиков
    fun greatChar(
        product: String,
        dataSets: ArrayList<ILineDataSet>,
        mapProduct: Map<String, ArrayList<Entry>>
    ) {
        val datasetFirst: LineDataSet = LineDataSet(mapProduct[product], product)
        idColor++
        //График будет зеленого цвета
        datasetFirst.color = setColors() // Todo Логика просчета
        //График будет плавным
        datasetFirst.mode = LineDataSet.Mode.LINEAR
        dataSets.add(datasetFirst)
    }

    private fun setColors(): Int {
        when (idColor) {
            0 -> return Color.GRAY
            1 -> return Color.RED
            2 -> return Color.BLUE
            4 -> return Color.CYAN
            5 -> return Color.GREEN
            6 -> return Color.YELLOW
            7 -> return Color.WHITE
            8 -> return Color.BLACK
            9 -> return Color.GRAY
        }
        return Color.GRAY
    }

    // Добавление значений в мапу
    fun allProducts() {

        sumProductYan.clear()
        sumProductFeb.clear()
        sumProductMar.clear()
        sumProductApr.clear()
        sumProductMay.clear()
        sumProductJun.clear()
        sumProductJar.clear()
        sumProductAvg.clear()
        sumProductSep.clear()
        sumProductOkt.clear()
        sumProductNov.clear()
        sumProductDec.clear()
        sumProductAll.clear()

        val cursor = myDB.readAllDataSale()
        val year2: String = yearSpiner.text.toString()
        if (cursor.count != 0) {
            //проверка за весь год //TODO Сократи это говно плиз и еще ниже будет его тоже, будущий Семён я сделал все что мог
            while (cursor.moveToNext()) {
                //проверка года
                if ((year2 == cursor.getString(5))) {
                    when (cursor.getString(4).toInt()) {
                        1 -> productMount(cursor, sumProductYan, 1f)
                        2 -> productMount(cursor, sumProductFeb, 2f)
                        3 -> productMount(cursor, sumProductMar, 3f)
                        4 -> productMount(cursor, sumProductApr, 4f)
                        5 -> productMount(cursor, sumProductMay, 5f)
                        6 -> productMount(cursor, sumProductJun, 6f)
                        7 -> productMount(cursor, sumProductJar, 7f)
                        8 -> productMount(cursor, sumProductAvg, 8f)
                        9 -> productMount(cursor, sumProductSep, 9f)
                        10 -> productMount(cursor, sumProductOkt, 10f)
                        11 -> productMount(cursor, sumProductNov, 11f)
                        12 -> productMount(cursor, sumProductDec, 12f)
                    }
                }
            }
        }
        cursor.close()
    }

    // Добавление значений по месячно
    private fun productMount(
        cursor: Cursor,
        sumProductMount: MutableMap<String, ArrayList<Entry>>,
        x: Float
    ) {
        if (sumProductMount[cursor.getString(1)] == null) {
            val sd: ArrayList<Entry> = ArrayList<Entry>()
            val y: Float = cursor.getString(6).toFloat()
            sd.add(Entry(x, y))
            sumProductMount[cursor.getString(1)] = sd

        } else {
            var y: Float = cursor.getString(6).toFloat()
            for (ds: Entry in sumProductMount.get(cursor.getString(1))) {
                y += ds.y
            }
            sumProductMount[cursor.getString(1)]!!.clear()
            sumProductMount[cursor.getString(1)]!!.add(Entry(x, y))
            sumProductMount[cursor.getString(1)] = sumProductMount.get(cursor.getString(1))
        }
    }

    // Соединение значений в единую мапу
    private fun all() {
        for (product: String in productList) {
            val entries: ArrayList<Entry> = ArrayList<Entry>()
            entries.addAll(addAll22(sumProductYan, product, 1f))
            entries.addAll(addAll22(sumProductFeb, product, 2f))
            entries.addAll(addAll22(sumProductMar, product, 3f))
            entries.addAll(addAll22(sumProductApr, product, 4f))
            entries.addAll(addAll22(sumProductMay, product, 5f))
            entries.addAll(addAll22(sumProductJun, product, 6f))
            entries.addAll(addAll22(sumProductJar, product, 7f))
            entries.addAll(addAll22(sumProductAvg, product, 8f))
            entries.addAll(addAll22(sumProductSep, product, 9f))
            entries.addAll(addAll22(sumProductOkt, product, 10f))
            entries.addAll(addAll22(sumProductNov, product, 11f))
            entries.addAll(addAll22(sumProductDec, product, 12f))
            sumProductAll[product] = entries
        }
    }

    // Находим пустые мапы и добавляем минимальные значения
    private fun addAll22(
        sumProductYansdad: MutableMap<String, ArrayList<Entry>?>?,
        product1: String,
        x: Float
    ): ArrayList<Entry> {
        val entries: ArrayList<Entry> = ArrayList<Entry>()

        if (sumProductYansdad!![product1] == null) {
            entries.add(Entry(x, 0))
            sumProductYansdad[product1] = entries

        } else {

            entries.addAll(sumProductYansdad[product1]!!)
        }
        return entries
    }

    //Если пользователь выбрал все товары по месяцу за год
    private fun allProductsMount(year2: String) {

        sumCategory.clear()

        val cursor = myDB.selectChartMountFinance1(mount.toString(), year2)

        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                sumCategory[cursor.getString(0)]!!.add(Entry(cursor.getString(2).toFloat(), cursor.getString(1).toFloat()))
            }

        } else {

            val sd: ArrayList<Entry> = ArrayList<Entry>()
            sd.add(Entry(0f, 0f))

            for (product: String in productList) {
                sumCategory[product] = sd
            }

        }
        cursor.close()
    }



    //Товары если не все
    fun storeDataInArrays(animalsType: String, mountString: String, year2: String) {

        setMount(mountString)

        when (mount) {
            in 1..12 -> {
                val cursor: Cursor = myDB.selectChartMount(
                    MyConstanta.DISCROTIONSale, MyConstanta.TABLE_NAMESALE, MyConstanta.TITLESale, animalsType,
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
                    MyConstanta.DISCROTIONSale,
                    MyConstanta.TABLE_NAMESALE,
                    MyConstanta.TITLESale,
                    animalsType,
                    year2
                )

                while (cursor.moveToNext()) {
                    when (cursor.getString(1).toInt()) {
                        1 -> visitors[0] = Entry(1f, cursor.getString(0).toFloat())
                        2 -> visitors[1] = Entry(2f, cursor.getString(0).toFloat())
                        3 -> visitors[2] = Entry(3f, cursor.getString(0).toFloat())
                        4 -> visitors[3] = Entry(4f, cursor.getString(0).toFloat())
                        5 -> visitors[4] = Entry(5f, cursor.getString(0).toFloat())
                        6 -> visitors[5] = Entry(6f, cursor.getString(0).toFloat())
                        7 -> visitors[6] = Entry(7f, cursor.getString(0).toFloat())
                        8 -> visitors[7] = Entry(8f, cursor.getString(0).toFloat())
                        9 -> visitors[8] = Entry(9f, cursor.getString(0).toFloat())
                        10 -> visitors[9] = Entry(10f, cursor.getString(0).toFloat())
                        11 -> visitors[10] = Entry(11f, cursor.getString(0).toFloat())
                        12 -> visitors[11] = Entry(12f, cursor.getString(0).toFloat())
                    }
                }
                cursor.close()
            }

            else -> {
                visitors.add(BarEntry(0f, 0f))
            }
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
            //Настройка спинера с продуктами
            val arrayAdapterProduct = ArrayAdapter<String>(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                productListAll
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

    // Добавляем в списки
    fun add() {
        val yearSet: MutableSet<String> = HashSet()
        val productSet: MutableSet<String> = HashSet()

        val cursor: Cursor = myDB.readAllDataSale()

        while (cursor.moveToNext()) {
            val year: String = cursor.getString(5)
            val product: String = cursor.getString(1)
            yearSet.add(year)
            productSet.add(product)
        }

        cursor.close()

        yearList = yearSet.toMutableList()

        productListAll = productSet.toMutableList()
        productListAll.add("Все")
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