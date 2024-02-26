//package com.hfad.myferma.Finance
//
//import android.database.Cursor
//import android.graphics.Color
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.AutoCompleteTextView
//import androidx.fragment.app.Fragment
//import com.google.android.material.appbar.MaterialToolbar
//import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
//import com.hfad.myferma.R
//import com.hfad.myferma.db.MyFermaDatabaseHelper
//import java.security.KeyStore.Entry
//import java.util.Calendar
//
//class FinanceChart2Fragment : Fragment() {
//    private var arrayAdapterAnimals: ArrayAdapter<String>? = null
//    private lateinit var myDB: MyFermaDatabaseHelper
//    private var entriesFirst = mutableListOf<Entry>()
//    private var entriesSecond = mutableListOf<Entry>()
//    private var entriesThird = mutableListOf<Entry>()
//    private lateinit var mount_spiner: AutoCompleteTextView
//    private lateinit var year_spiner: AutoCompleteTextView
//    private val labes: Array<String> = arrayOf(
//        "",
//        "Январь",
//        "Февраль",
//        "Март",
//        "Апрель",
//        "Май",
//        "Июнь",
//        "Июль",
//        "Август",
//        "Сентябрь",
//        "Октябрь",
//        "Ноябрь",
//        "Декабрь",
//        ""
//    )
//    private var mountMass = mutableListOf<String>()
//    private lateinit  var layout: View
//    private var mount: Int = 0
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        //Подключение к базе данных
//        myDB = MyFermaDatabaseHelper(requireContext())
//        val calendar = Calendar.getInstance()
//        layout = inflater.inflate(R.layout.fragment_finance_chart2, container, false)
//
//        // установка спинеров
//        mount_spiner = layout.findViewById<AutoCompleteTextView>(R.id.mount_spiner)
//        year_spiner = layout.findViewById<AutoCompleteTextView>(R.id.year_spiner)
//
//        // настройка спинеров
//        mount_spiner.setText("За весь год", false)
//        year_spiner.setText(calendar.get(Calendar.YEAR).toString(), false)
//
//        //убириаем фаб кнопку
//        val fab: ExtendedFloatingActionButton =
//            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
//        fab.visibility = View.GONE
//        val appBar: MaterialToolbar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
//        appBar.title = "Мои Финансы - Общее"
//        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
//        appBar.setNavigationOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
//
//        //Массивы
//        entriesFirst = ArrayList<Entry>()
//        entriesSecond = ArrayList<Entry>()
//        entriesThird = ArrayList<Entry>()
//        spiner()
//        mount_spiner.setOnItemClickListener { parent, view, position, id -> spiner() }
//        year_spiner.onItemClickListener =
//            AdapterView.OnItemClickListener { parent, view, position, id -> spiner() }
//        return layout
//    }
//
//    private fun spiner() {
//        entriesFirst.clear()
//        entriesSecond.clear()
//        entriesThird.clear()
//        allProducts()
//        val lineChart: LineChart = layout.findViewById(R.id.lineChart)
//        lineChart.getDescription().setText("График финансов")
//        val datasetFirst: LineDataSet = LineDataSet(entriesFirst, "Прибыль")
//
//        // График будет зеленого цвета
//        datasetFirst.setColor(Color.GRAY)
//        // График будет плавным
//        datasetFirst.setMode(LineDataSet.Mode.LINEAR)
//        val datasetSecond: LineDataSet = LineDataSet(entriesSecond, "Чистая прибыль")
//        // График будет зеленого цвета
//        datasetSecond.setColor(Color.GREEN)
//        // График будет плавным
//        datasetSecond.setMode(LineDataSet.Mode.LINEAR)
//        val datasetThird: LineDataSet = LineDataSet(entriesThird, "Расходы")
//        // График будет зеленого цвета
//        datasetThird.setColor(Color.RED)
//        // График будет плавным
//        datasetThird.setMode(LineDataSet.Mode.LINEAR)
//        val dataSets: ArrayList<ILineDataSet?> = ArrayList<Any?>()
//        dataSets.add(datasetFirst)
//        dataSets.add(datasetSecond)
//        dataSets.add(datasetThird)
//        val data: LineData = LineData(dataSets)
//        lineChart.invalidate()
//        lineChart.setData(data)
//        lineChart.animateY(500)
//        if (mount != 13) {
//            xaxis(lineChart, mountMass)
//        } else {
//            xaxis(lineChart, labes)
//        }
//    }
//
//    private fun xaxis(lineChart: LineChart, valueX: Array<String>?) {
//        val xAxis: XAxis = lineChart.getXAxis()
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
//        xAxis.setDrawGridLines(false)
//        xAxis.setGranularity(1f) // only intervals of 1 day
//        xAxis.setLabelCount(7)
//        xAxis.setValueFormatter(IndexAxisValueFormatter(valueX))
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val view: View? = view
//        if (view != null) {
//            // настройка спинера с годами (выглядил как обычный, и год запоминал)
//            arrayAdapterAnimals = ArrayAdapter<String>(
//                requireContext().applicationContext,
//                R.layout.simple_spinner_dropdown_item,
//                add()
//            )
//            year_spiner.setAdapter<ArrayAdapter<String>>(arrayAdapterAnimals)
//        }
//    }
//
//    fun add(): ArrayList<String> {
//        val tempList: MutableSet<String> = HashSet()
//        val cursor = myDB.readAllDataSale()
//
//        while (cursor.moveToNext()) {
//            val string1: String = cursor.getString(5)
//            tempList.add(string1)
//        }
//        cursor.close()
//
//        val tempList1: ArrayList<String> = ArrayList()
//        for (nameExpenses: String in tempList) {
//            tempList1.add(nameExpenses)
//        }
//        return tempList1
//    }
//
//    private fun allProducts() {
//        val cursor = myDB.readAllDataSale()
//        val cursorExpenses = myDB.readAllDataExpenses()
//        val sumCategory: MutableMap<Float, Float?> = HashMap()
//        val sumCategoryExpenses: MutableMap<Float, Float?> = HashMap()
//        val sumCategoryClear: MutableMap<Float, Float?> = HashMap()
//        val mountString: String = mount_spiner.text.toString()
//        val year2: String = year_spiner.text.toString()
//        setMount(mountString)
//        if (mount <= 12 && mount > 0) {
//            allProductsMount(cursor, entriesFirst, 6, 1, year2)
//            allProductsMount(cursorExpenses, entriesThird, 2, -1, year2)
//        } else if (mount == 13) {
//            allProductsYear(cursor, year2, sumCategory, entriesFirst, 6, 1)
//            allProductsYear(cursorExpenses, year2, sumCategoryExpenses, entriesThird, 2, -1)
//            for (entry: Map.Entry<Float, Float?> in sumCategory.entries) {
//                val name: Float = entry.key
//                val sum: Float? = entry.value
//                sumCategoryClear[name] = sum
//            }
//            for (entry2: Map.Entry<Float, Float?> in sumCategoryExpenses.entries) {
//                val nameExpenses: Float = entry2.key
//                val sumExpenses: Float? = entry2.value
//                if (sumCategoryClear[nameExpenses] == null) {
//                    sumCategoryClear[nameExpenses] = sumExpenses
//                } else {
//                    val sum: Float = sumCategoryClear[nameExpenses]!! + (sumExpenses)!!
//                    sumCategoryClear[nameExpenses] = sum
//                }
//            }
//            for (entry: Map.Entry<Float, Float?> in sumCategoryClear.entries) {
//                val name: Float = entry.key
//                val sum: Float? = entry.value
//                entriesSecond.add(Entry(name, sum))
//            }
//        } else {
//            entriesFirst.add(Entry(0f, 0f))
//            entriesSecond.add(Entry(0f, 0f))
//            entriesThird.add(Entry(0f, 0f))
//        }
//        cursorExpenses.close()
//        cursor.close()
//    }
//
//    private fun allProductsMount(
//        cursor: Cursor,
//        entries: ArrayList<Entry>?,
//        price: Int,
//        kof: Int,
//        year2: String
//    ) {
//        var x: Float
//        var y: Float
//        if (cursor.count != 0) {
//            while (cursor.moveToNext()) {
//                //проверка месяца
//                if (mount == cursor.getString(4).toInt()) {
//                    //проверка года
//                    if ((year2 == cursor.getString(5))) {
//                        y = cursor.getString(price).toFloat() * kof //Для вычитания
//                        x = cursor.getString(3).toFloat()
//                        entries!!.add(Entry(x, y))
//                    }
//                }
//            }
//        } else {
//            entries!!.add(Entry(0, 0))
//        }
//        cursor.close()
//    }
//
//    private fun allProductsYear(
//        cursor: Cursor,
//        year2: String,
//        sumCategory: MutableMap<Float, Float?>,
//        entries: ArrayList<Entry>?,
//        price: Int,
//        kof: Int
//    ) {
//        if (cursor.count != 0) {
//            while (cursor.moveToNext()) {
//                //проверка года
//                if ((year2 == cursor.getString(5))) {
//                    if (sumCategory[cursor.getString(4).toFloat()] == null) {
//                        sumCategory[cursor.getString(4).toFloat()] = cursor.getString(price).toFloat() * kof
//                    } else {
//                        val sum: Float =
//                            sumCategory[cursor.getString(4).toFloat()]!! + cursor.getString(
//                                price
//                            ).toFloat() * kof
//                        sumCategory[cursor.getString(4).toFloat()] = sum
//                    }
//                }
//            }
//        }
//        cursor.close()
//        for (i in 1..11) {
//            if (sumCategory.get(i) == null) {
//                entries!!.add(Entry(i, 0))
//            } else {
//                for (entry: Map.Entry<Float, Float?> in sumCategory.entries) {
//                    val name: Float = entry.key
//                    val sum: Float? = entry.value
//                    entries!!.add(Entry(name, sum))
//                }
//            }
//        }
//    }
//
//    fun setMount(mountString: String?) {
//        when (mountString) {
//            "Январь" -> {
//                mount = 1
//                mountMass = arrayOf(
//                    "",
//                    "1",
//                    "2",
//                    "3",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    "8",
//                    "9",
//                    "10",
//                    "11",
//                    "12",
//                    "13",
//                    "14",
//                    "15",
//                    "16",
//                    "17",
//                    "18",
//                    "19",
//                    "20",
//                    "21",
//                    "22",
//                    "23",
//                    "24",
//                    "25",
//                    "26",
//                    "27",
//                    "28",
//                    "29",
//                    "30",
//                    "31",
//                    ""
//                ).toMutableList()
//            }
//
//            "Февраль" -> {
//                mount = 2
//                mountMass = arrayOf(
//                    "",
//                    "1",
//                    "2",
//                    "3",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    "8",
//                    "9",
//                    "10",
//                    "11",
//                    "12",
//                    "13",
//                    "14",
//                    "15",
//                    "16",
//                    "17",
//                    "18",
//                    "19",
//                    "20",
//                    "21",
//                    "22",
//                    "23",
//                    "24",
//                    "25",
//                    "26",
//                    "27",
//                    "28",
//                    ""
//                ).toMutableList()
//            }
//
//            "Март" -> {
//                mount = 3
//                mountMass = arrayOf(
//                    "",
//                    "1",
//                    "2",
//                    "3",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    "8",
//                    "9",
//                    "10",
//                    "11",
//                    "12",
//                    "13",
//                    "14",
//                    "15",
//                    "16",
//                    "17",
//                    "18",
//                    "19",
//                    "20",
//                    "21",
//                    "22",
//                    "23",
//                    "24",
//                    "25",
//                    "26",
//                    "27",
//                    "28",
//                    "29",
//                    "30",
//                    "31",
//                    ""
//                ).toMutableList()
//            }
//
//            "Апрель" -> {
//                mount = 4
//                mountMass = arrayOf(
//                    "",
//                    "1",
//                    "2",
//                    "3",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    "8",
//                    "9",
//                    "10",
//                    "11",
//                    "12",
//                    "13",
//                    "14",
//                    "15",
//                    "16",
//                    "17",
//                    "18",
//                    "19",
//                    "20",
//                    "21",
//                    "22",
//                    "23",
//                    "24",
//                    "25",
//                    "26",
//                    "27",
//                    "28",
//                    "29",
//                    "30",
//                    ""
//                ).toMutableList()
//            }
//
//            "Май" -> {
//                mount = 5
//                mountMass = arrayOf(
//                    "",
//                    "1",
//                    "2",
//                    "3",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    "8",
//                    "9",
//                    "10",
//                    "11",
//                    "12",
//                    "13",
//                    "14",
//                    "15",
//                    "16",
//                    "17",
//                    "18",
//                    "19",
//                    "20",
//                    "21",
//                    "22",
//                    "23",
//                    "24",
//                    "25",
//                    "26",
//                    "27",
//                    "28",
//                    "29",
//                    "30",
//                    "31",
//                    ""
//                ).toMutableList()
//            }
//
//            "Июнь" -> {
//                mount = 6
//                mountMass = arrayOf(
//                    "",
//                    "1",
//                    "2",
//                    "3",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    "8",
//                    "9",
//                    "10",
//                    "11",
//                    "12",
//                    "13",
//                    "14",
//                    "15",
//                    "16",
//                    "17",
//                    "18",
//                    "19",
//                    "20",
//                    "21",
//                    "22",
//                    "23",
//                    "24",
//                    "25",
//                    "26",
//                    "27",
//                    "28",
//                    "29",
//                    "30",
//                    ""
//                ).toMutableList()
//            }
//
//            "Июль" -> {
//                mount = 7
//                mountMass = arrayOf(
//                    "",
//                    "1",
//                    "2",
//                    "3",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    "8",
//                    "9",
//                    "10",
//                    "11",
//                    "12",
//                    "13",
//                    "14",
//                    "15",
//                    "16",
//                    "17",
//                    "18",
//                    "19",
//                    "20",
//                    "21",
//                    "22",
//                    "23",
//                    "24",
//                    "25",
//                    "26",
//                    "27",
//                    "28",
//                    "29",
//                    "30",
//                    "31",
//                    ""
//                ).toMutableList()
//            }
//
//            "Август" -> {
//                mount = 8
//                mountMass = arrayOf(
//                    "",
//                    "1",
//                    "2",
//                    "3",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    "8",
//                    "9",
//                    "10",
//                    "11",
//                    "12",
//                    "13",
//                    "14",
//                    "15",
//                    "16",
//                    "17",
//                    "18",
//                    "19",
//                    "20",
//                    "21",
//                    "22",
//                    "23",
//                    "24",
//                    "25",
//                    "26",
//                    "27",
//                    "28",
//                    "29",
//                    "30",
//                    "31",
//                    ""
//                ).toMutableList()
//            }
//
//            "Сентябрь" -> {
//                mount = 9
//                mountMass = arrayOf(
//                    "",
//                    "1",
//                    "2",
//                    "3",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    "8",
//                    "9",
//                    "10",
//                    "11",
//                    "12",
//                    "13",
//                    "14",
//                    "15",
//                    "16",
//                    "17",
//                    "18",
//                    "19",
//                    "20",
//                    "21",
//                    "22",
//                    "23",
//                    "24",
//                    "25",
//                    "26",
//                    "27",
//                    "28",
//                    "29",
//                    "30",
//                    ""
//                ).toMutableList()
//            }
//
//            "Октябрь" -> {
//                mount = 10
//                mountMass = arrayOf(
//                    "",
//                    "1",
//                    "2",
//                    "3",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    "8",
//                    "9",
//                    "10",
//                    "11",
//                    "12",
//                    "13",
//                    "14",
//                    "15",
//                    "16",
//                    "17",
//                    "18",
//                    "19",
//                    "20",
//                    "21",
//                    "22",
//                    "23",
//                    "24",
//                    "25",
//                    "26",
//                    "27",
//                    "28",
//                    "29",
//                    "30",
//                    "31",
//                    ""
//                ).toMutableList()
//            }
//
//            "Ноябрь" -> {
//                mount = 11
//                mountMass = arrayOf(
//                    "",
//                    "1",
//                    "2",
//                    "3",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    "8",
//                    "9",
//                    "10",
//                    "11",
//                    "12",
//                    "13",
//                    "14",
//                    "15",
//                    "16",
//                    "17",
//                    "18",
//                    "19",
//                    "20",
//                    "21",
//                    "22",
//                    "23",
//                    "24",
//                    "25",
//                    "26",
//                    "27",
//                    "28",
//                    "29",
//                    "30",
//                    ""
//                ).toMutableList()
//            }
//
//            "Декабрь" -> {
//                mount = 12
//                mountMass = arrayOf(
//                    "",
//                    "1",
//                    "2",
//                    "3",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    "8",
//                    "9",
//                    "10",
//                    "11",
//                    "12",
//                    "13",
//                    "14",
//                    "15",
//                    "16",
//                    "17",
//                    "18",
//                    "19",
//                    "20",
//                    "21",
//                    "22",
//                    "23",
//                    "24",
//                    "25",
//                    "26",
//                    "27",
//                    "28",
//                    "29",
//                    "30",
//                    "31",
//                    ""
//                ).toMutableList()
//            }
//
//            "За весь год" -> mount = 13
//        }
//    }
//}