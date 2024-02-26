//package com.hfad.myferma.Finance
//
//import android.database.Cursor
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
//import java.util.Calendar
//
//class FinanceChartFragment : Fragment() {
//    private lateinit var myDB: MyFermaDatabaseHelper
//    private var visitors = mutableListOf<Entry>()
//    private lateinit var animals_spiner: AutoCompleteTextView
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
//    private lateinit var layout: View
//    private var mount: Int = 0
//    private var idColor: Int = 0
//    private var year: String? = null
//    private var yearList = mutableListOf<String>()
//    private var productList = mutableListOf<String>()
//    private var productListAll = mutableListOf<String>()
//    private var sumCategory = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductYan = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductFeb = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductMar = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductApr = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductMay = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductJun = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductJar = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductAvg = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductSep = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductOkt = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductNov = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductDec = mutableMapOf<String, ArrayList<Entry>>()
//    private var sumProductAll = mutableMapOf<String, ArrayList<Entry>>()
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        //Подключение к базе данных и к календарю
//        myDB = MyFermaDatabaseHelper(requireContext())
//
//        add()
//        val calendar = Calendar.getInstance()
//        layout = inflater.inflate(R.layout.fragment_finance_chart, container, false)
//
//        // установка спинеров
//        animals_spiner = layout.findViewById<AutoCompleteTextView>(R.id.animals_spiner)
//        mount_spiner = layout.findViewById<AutoCompleteTextView>(R.id.animals_spiner2)
//        year_spiner = layout.findViewById<AutoCompleteTextView>(R.id.animals_spiner3)
//
//        // настройка спинеров
//        animals_spiner.setText("Все", false)
//        mount_spiner.setText("За весь год", false)
//        year_spiner.setText(calendar.get(Calendar.YEAR).toString(), false)
//        year = year_spiner.text.toString()
//
//        //убириаем фаб кнопку
//        val fab: ExtendedFloatingActionButton =
//            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
//        fab.visibility = View.GONE
//        val appBar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
//        appBar.title = "Мои Финансы - Продукция"
//        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
//        appBar.setNavigationOnClickListener {
//            requireActivity().supportFragmentManager.popBackStack()
//        }
//
//
//        //Массивы
//
//
//        //Логика просчета
//        allProducts()
//
//        //Формируем списки
//        all()
//
//        // Формируем график
//        spiner()
//
//        animals_spiner.onItemClickListener =
//            AdapterView.OnItemClickListener { parent, view, position, id -> spiner() }
//        mount_spiner.setOnItemClickListener { parent, view, position, id -> spiner() }
//        year_spiner.onItemClickListener =
//            AdapterView.OnItemClickListener { parent, view, position, id -> spiner() }
//
//        return layout
//    }
//
//    fun spiner() {
//        visitors!!.clear()
//        val animalsType: String = animals_spiner.text.toString()
//        val mountString: String = mount_spiner.text.toString()
//        val year2: String = year_spiner.text.toString()
//        setMount(mountString)
//
//        val lineChart: LineChart = layout.findViewById(R.id.lineChart)
//        lineChart.getDescription().setText("График продукции")
//        if ((animalsType == "Все")) {
//            val dataSets: ArrayList<ILineDataSet?> = ArrayList<Any?>()
//            if (mount <= 12 && mount > 0) {
//                allProductsMount(animalsType, year2)
//                for (product: String in productList) {
//                    greatChar(product, dataSets, sumCategory)
//                }
//            } else {
//                if ((year == year2)) {
//                    for (product: String in productList) {
//                        greatChar(product, dataSets, sumProductAll)
//                    }
//                } else {
//                    year = year2
//                    //Логика просчета
//                    allProducts()
//                    //Формируем списки
//                    all()
//                    for (product: String in productList) {
//                        greatChar(product, dataSets, sumProductAll)
//                    }
//                }
//            }
//            val data: LineData = LineData(dataSets)
//            lineChart.invalidate()
//            lineChart.setData(data)
//            lineChart.animateY(500)
//            if (mount != 13) {
//                xaxis(lineChart, mountMass)
//            } else {
//                xaxis(lineChart, labes)
//            }
//        } else {
//
//            storeDataInArrays(animalsType, mountString, year2)
//            val dataset: LineDataSet = LineDataSet(visitors, animalsType)
//            val data: LineData = LineData(dataset)
//            lineChart.invalidate()
//            lineChart.setData(data)
//            lineChart.animateY(500)
//            if (mount != 13) {
//                xaxis(lineChart, mountMass)
//            } else {
//                xaxis(lineChart, labes)
//            }
//        }
//    }
//
//    // Добавление графиков
//    fun greatChar(
//        product: String,
//        dataSets: ArrayList<ILineDataSet?>,
//        mapProduct: Map<String, ArrayList<Entry>?>?
//    ) {
//        val datasetFirst: LineDataSet = LineDataSet(mapProduct!!.get(product), product)
//        idColor++
//        //График будет зеленого цвета
//        datasetFirst.setColor(setColors()) // Todo Логика просчета
//        //График будет плавным
//        datasetFirst.setMode(LineDataSet.Mode.LINEAR)
//        dataSets.add(datasetFirst)
//    }
//
//    fun setColors(): Int {
//        when (idColor) {
//            0 -> return Color.GRAY
//            1 -> return Color.RED
//            2 -> return Color.BLUE
//            4 -> return Color.CYAN
//            5 -> return Color.GREEN
//            6 -> return Color.YELLOW
//            7 -> return Color.WHITE
//            8 -> return Color.BLACK
//            9 -> return Color.GRAY
//        }
//        return Color.GRAY
//    }
//
//    // Добавление значений в мапу
//    fun allProducts() {
//        sumProductYan.clear()
//        sumProductFeb.clear()
//        sumProductMar.clear()
//        sumProductApr.clear()
//        sumProductMay.clear()
//        sumProductJun.clear()
//        sumProductJar.clear()
//        sumProductAvg.clear()
//        sumProductSep.clear()
//        sumProductOkt.clear()
//        sumProductNov.clear()
//        sumProductDec.clear()
//        sumProductAll.clear()
//        val cursor = myDB.readAllDataSale()
//        val year2: String = year_spiner.text.toString()
//        if (cursor.count != 0) {
//            //проверка за весь год //TODO Сократи это говно плиз и еще ниже будет его тоже, будущий Семён я сделал все что мог
//            while (cursor.moveToNext()) {
//                //проверка года
//                if ((year2 == cursor.getString(5))) {
//                    when (cursor.getString(4).toInt()) {
//                        1 -> productMount(cursor, sumProductYan, 1f)
//                        2 -> productMount(cursor, sumProductFeb, 2f)
//                        3 -> productMount(cursor, sumProductMar, 3f)
//                        4 -> productMount(cursor, sumProductApr, 4f)
//                        5 -> productMount(cursor, sumProductMay, 5f)
//                        6 -> productMount(cursor, sumProductJun, 6f)
//                        7 -> productMount(cursor, sumProductJar, 7f)
//                        8 -> productMount(cursor, sumProductAvg, 8f)
//                        9 -> productMount(cursor, sumProductSep, 9f)
//                        10 -> productMount(cursor, sumProductOkt, 10f)
//                        11 -> productMount(cursor, sumProductNov, 11f)
//                        12 -> productMount(cursor, sumProductDec, 12f)
//                    }
//                }
//            }
//        }
//        cursor.close()
//    }
//
//    // Добавление значений по месячно
//    private fun productMount(
//        cursor: Cursor,
//        sumProductMount: MutableMap<String, ArrayList<Entry>>,
//        x: Float
//    ) {
//        if (sumProductMount[cursor.getString(1)] == null) {
//            val sd: ArrayList<Entry> = ArrayList<Entry>()
//            val y: Float = cursor.getString(6).toFloat()
//            sd.add(Entry(x, y))
//            sumProductMount[cursor.getString(1)] = sd
//
//        } else {
//            var y: Float = cursor.getString(6).toFloat()
//            for (ds: Entry in sumProductMount.get(cursor.getString(1))) {
//                y += ds.getY()
//            }
//            sumProductMount[cursor.getString(1)]!!.clear()
//            sumProductMount[cursor.getString(1)]!!.add(Entry(x, y))
//            sumProductMount[cursor.getString(1)] = sumProductMount.get(cursor.getString(1))
//        }
//    }
//
//    // Соединение значений в единую мапу
//    private fun all() {
//        for (product: String in productList) {
//            val entries: ArrayList<Entry> = ArrayList<Entry>()
//            entries.addAll(addAll22(sumProductYan, product, 1f))
//            entries.addAll(addAll22(sumProductFeb, product, 2f))
//            entries.addAll(addAll22(sumProductMar, product, 3f))
//            entries.addAll(addAll22(sumProductApr, product, 4f))
//            entries.addAll(addAll22(sumProductMay, product, 5f))
//            entries.addAll(addAll22(sumProductJun, product, 6f))
//            entries.addAll(addAll22(sumProductJar, product, 7f))
//            entries.addAll(addAll22(sumProductAvg, product, 8f))
//            entries.addAll(addAll22(sumProductSep, product, 9f))
//            entries.addAll(addAll22(sumProductOkt, product, 10f))
//            entries.addAll(addAll22(sumProductNov, product, 11f))
//            entries.addAll(addAll22(sumProductDec, product, 12f))
//            sumProductAll[product] = entries
//        }
//    }
//
//    // Находим пустые мапы и добавляем минимальные значения
//    private fun addAll22(
//        sumProductYansdad: MutableMap<String, ArrayList<Entry>?>?,
//        product1: String,
//        x: Float
//    ): ArrayList<Entry> {
//        val entries: ArrayList<Entry> = ArrayList<Entry>()
//
//        if (sumProductYansdad!![product1] == null) {
//            entries.add(Entry(x, 0))
//            sumProductYansdad[product1] = entries
//
//        } else {
//
//            entries.addAll(sumProductYansdad[product1]!!)
//        }
//        return entries
//    }
//
//    //Если пользователь выбрал все товары по месяцу за год
//    private fun allProductsMount(animalsType: String, year2: String) {
//        var x: Float
//        var y: Float
//        sumCategory.clear()
//        val cursor  = myDB.readAllDataSale()
//        if (cursor.count != 0) {
//            while (cursor.moveToNext()) {
//
//                if ((animalsType == "Все")) {
//                    //проверка месяца
//                    if (mount == cursor.getString(4).toInt()) {
//                        //проверка года
//                        if ((year2 == cursor.getString(5))) {
//
//                            val sd: ArrayList<Entry> = ArrayList<Entry>()
//                            y = cursor.getString(6).toFloat()
//                            x = cursor.getString(3).toFloat()
//
//                            if (sumCategory[cursor.getString(1)] == null) {
//                                sd.add(Entry(x, y))
//                                sumCategory[cursor.getString(1)] = sd
//                            } else {
//                                sumCategory[cursor.getString(1)]!!.add(Entry(x, y))
//                                sumCategory[cursor.getString(1)] = sumCategory.get(cursor.getString(1))
//                            }
//                        }
//                    }
//                }
//            }
//        } else {
//            val sd: ArrayList<Entry> = ArrayList<Entry>()
//            sd.add(Entry(0, 0))
//            for (product: String in productList) {
//                sumCategory[product] = sd
//            }
//        }
//        cursor.close()
//    }
//
//    fun storeDataInArrays(animalsType: String, mountString: String?, year2: String) {
//        val cursor: Cursor = myDB.readAllDataSale()
//        var jan: Float = 0f
//        var feb: Float = 0f
//        var mar: Float = 0f
//        var apr: Float = 0f
//        var mai: Float = 0f
//        var jun: Float = 0f
//        var jul: Float = 0f
//        var aug: Float = 0f
//        var sep: Float = 0f
//        var oct: Float = 0f
//        var nov: Float = 0f
//        var dec: Float = 0f
//        cursor.moveToNext()
//        if (cursor.getCount() != 0) {
//            if (mount <= 12 && mount > 0) {
//                while (cursor.moveToNext()) {
//                    storeDataInArraysMount(cursor, animalsType, year2)
//                }
//            } else if (mount == 13) {
//                while (cursor.moveToNext()) {
//                    if ((animalsType == cursor.getString(1))) {
//                        //проверка года
//                        if ((year2 == cursor.getString(5))) {
//                            when (cursor.getString(4).toInt()) {
//                                1 -> jan += cursor.getString(6).toFloat()
//                                2 -> feb += cursor.getString(6).toFloat()
//                                3 -> mar += cursor.getString(6).toFloat()
//                                4 -> apr += cursor.getString(6).toFloat()
//                                5 -> mai += cursor.getString(6).toFloat()
//                                6 -> jun += cursor.getString(6).toFloat()
//                                7 -> jul += cursor.getString(6).toFloat()
//                                8 -> aug += cursor.getString(6).toFloat()
//                                9 -> sep += cursor.getString(6).toFloat()
//                                10 -> oct += cursor.getString(6).toFloat()
//                                11 -> nov += cursor.getString(6).toFloat()
//                                12 -> dec += cursor.getString(6).toFloat()
//                            }
//                        }
//                    }
//                }
//                cursor.close()
//                visitors!!.add(Entry(1, jan))
//                visitors!!.add(Entry(2, feb))
//                visitors!!.add(Entry(3, mar))
//                visitors!!.add(Entry(4, apr))
//                visitors!!.add(Entry(5, mai))
//                visitors!!.add(Entry(6, jun))
//                visitors!!.add(Entry(7, jul))
//                visitors!!.add(Entry(8, aug))
//                visitors!!.add(Entry(9, sep))
//                visitors!!.add(Entry(10, oct))
//                visitors!!.add(Entry(11, nov))
//                visitors!!.add(Entry(12, dec))
//            }
//        } else {
//            visitors!!.add(Entry(0, 0))
//        }
//        cursor.close()
//    }
//
//    fun storeDataInArraysMount(cursor: Cursor, animalsType: String, year2: String) {
//        if ((animalsType == cursor.getString(1))) {
//            //проверка месяца
//            if (mount == cursor.getString(4).toInt()) {
//                //проверка года
//                if ((year2 == cursor.getString(5))) {
//                    val x: Float = cursor.getString(6).toFloat()
//                    val y: Float = cursor.getString(3).toFloat()
//                    visitors!!.add(Entry(y, x))
//                }
//            }
//        }
//    }
//
//    fun xaxis(lineChart: LineChart, valueX: Array<String>?) {
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
//            //Настройка спинера с продуктами
//            val arrayAdapterProduct = ArrayAdapter<String>(
//                requireContext().applicationContext,
//                R.layout.simple_spinner_dropdown_item,
//                productListAll
//            )
//            animals_spiner.setAdapter<ArrayAdapter<String>>(arrayAdapterProduct)
//
//            // настройка спинера с годами (выглядил как обычный, и год запоминал)
//           val arrayAdapterYear = ArrayAdapter<String>(
//                requireContext().applicationContext,
//                R.layout.simple_spinner_dropdown_item,
//                yearList
//            )
//            year_spiner.setAdapter<ArrayAdapter<String>>(arrayAdapterYear)
//        }
//    }
//
//    // Добавляем в списки
//    fun add() {
//        val yearSet: MutableSet<String> = HashSet()
//        val productSet: MutableSet<String> = HashSet()
//        val cursor: Cursor = myDB.readAllDataSale()
//        while (cursor.moveToNext()) {
//            val year: String = cursor.getString(5)
//            val product: String = cursor.getString(1)
//            yearSet.add(year)
//            productSet.add(product)
//        }
//        cursor.close()
//        yearList = ArrayList()
//        productListAll = ArrayList()
//        for (yearColum: String in yearSet) {
//            yearList.add(yearColum)
//        }
//        for (productColum: String in productSet) {
//            productListAll.add(productColum)
//        }
//        productList = productListAll.clone() as ArrayList<String>?
//        productListAll.add("Все")
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