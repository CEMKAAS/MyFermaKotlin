//package com.hfad.myferma.WriteOff
//
//import android.R
//import android.database.Cursor
//import android.graphics.Color
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.AutoCompleteTextView
//import android.widget.RadioButton
//import android.widget.RadioGroup
//import androidx.fragment.app.Fragment
//import com.github.mikephil.charting.charts.BarChart
//import com.google.android.material.appbar.MaterialToolbar
//import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
//import com.hfad.myferma.db.MyFermaDatabaseHelper
//import java.util.Calendar
//
//class WriteOffChartFragment : Fragment() {
//    private lateinit var myDB: MyFermaDatabaseHelper
//    private lateinit var animals_spiner: AutoCompleteTextView
//    private lateinit var mount_spiner: AutoCompleteTextView
//    private lateinit var year_spiner: AutoCompleteTextView
//    private var visitors = mutableListOf<BarEntry>()
//    private lateinit var radioButton1: RadioButton
//    private lateinit var radioButton2: RadioButton
//    private lateinit var radioGroup: RadioGroup
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
//    private var mount: Int = 0
//    private var status: Int = R.drawable.baseline_cottage_24
//    private lateinit var layout: View
//    private var yearList = mutableListOf<String>()
//    private var productList = mutableListOf<String>()
//    private var infoChart: String = "График списанной продукции на собсвенные нужды"
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        layout = inflater.inflate(R.layout.fragment_write_off_chart, container, false)
//
//        //Подключение к базе данных
//        myDB = MyFermaDatabaseHelper(requireContext())
//
//        add()
//
//        // установка радио
//        radioGroup = layout.findViewById<View>(R.id.radioGroup) as RadioGroup
//        radioButton1 = layout.findViewById<View>(R.id.radio_button_1) as RadioButton
//        radioButton2 = layout.findViewById<View>(R.id.radio_button_2) as RadioButton
//
//        // установка спинеров
//        animals_spiner = layout.findViewById<View>(R.id.animals_spiner) as AutoCompleteTextView
//        mount_spiner = layout.findViewById<View>(R.id.mount_spiner) as AutoCompleteTextView
//        year_spiner = layout.findViewById<View>(R.id.year_spiner) as AutoCompleteTextView
//
//        //Создание списка с данными для графиков
//        val calendar = Calendar.getInstance()
//
//        // настройка спинеров
//        mount_spiner.setText("За весь год", false)
//        year_spiner.setText(calendar.get(Calendar.YEAR).toString(), false)
//
//        //убириаем фаб кнопку
//        val fab: ExtendedFloatingActionButton =
//            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
//        fab.visibility = View.GONE
//
//        //настройка верхнего меню фаб кнопку
//        val appBar: MaterialToolbar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
//        appBar.title = "Мои Списания - График"
//        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
//        appBar.setNavigationOnClickListener {
//            requireActivity().supportFragmentManager.popBackStack()
//        }
//
//        //Логика просчета
//        storeDataInArrays()
//
//        // настройка графиков
//        bar(labes, infoChart)
//        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
//            when (checkedId) {
//                R.id.radio_button_1 -> {
//                    visitors!!.clear()
//                    status = R.drawable.baseline_cottage_24
//                    storeDataInArrays()
//                    infoChart = "График списанной продукции на собсвенные нужды"
//                    if (mount != 13) {
//                        bar(mountMass, infoChart)
//                    } else {
//                        bar(labes, infoChart)
//                    }
//                }
//
//                R.id.radio_button_2 -> {
//                    visitors!!.clear()
//                    status = R.drawable.baseline_delete_24
//                    storeDataInArrays()
//                    infoChart = "График списанной продукции на утилизацию"
//                    if (mount != 13) {
//                        bar(mountMass, infoChart)
//                    } else {
//                        bar(labes, infoChart)
//                    }
//                }
//            }
//        })
//        animals_spiner.setOnItemClickListener { parent, view, position, id ->
//            visitors!!.clear()
//            storeDataInArrays()
//            if (mount != 13) {
//                bar(mountMass, infoChart)
//            } else {
//                bar(labes, infoChart)
//            }
//        }
//        mount_spiner.onItemClickListener =
//            AdapterView.OnItemClickListener { parent, view, position, id ->
//                visitors!!.clear()
//                storeDataInArrays()
//                if (mount != 13) {
//                    bar(mountMass, infoChart)
//                } else {
//                    bar(labes, infoChart)
//                }
//            }
//        year_spiner.setOnItemClickListener { parent, view, position, id ->
//            visitors!!.clear()
//            storeDataInArrays()
//            if (mount != 13) {
//                bar(mountMass, infoChart)
//            } else {
//                bar(labes, infoChart)
//            }
//        }
//        return layout
//    }
//
//    private fun bar(xAsis: Array<String>?, info: String?) {
//        val barChart: BarChart = layout.findViewById(R.id.barChart)
//        val barDataSet: BarDataSet = BarDataSet(visitors, animals_spiner.text.toString())
//        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS)
//        barDataSet.setValueTextColor(Color.BLACK)
//        barDataSet.setValueTextSize(16f)
//        val barData: BarData = BarData(barDataSet)
//        barChart.invalidate()
//        barChart.setFitBars(true)
//        barChart.setData(barData)
//        barChart.getDescription().setText(info)
//        barChart.animateY(2000)
//        val xAxis: XAxis = barChart.getXAxis()
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
//        xAxis.setDrawGridLines(false)
//        xAxis.setGranularity(1f) // only intervals of 1 day
//        xAxis.setLabelCount(6) //сколько отображается
//        xAxis.setValueFormatter(IndexAxisValueFormatter(xAsis))
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
//                productList
//            )
//            animals_spiner.setAdapter<ArrayAdapter<String>>(arrayAdapterProduct)
//
//            // настройка спинера с годами (выглядил как обычный, и год запоминал)
//            val arrayAdapterYear = ArrayAdapter<String>(
//                requireContext().applicationContext,
//                R.layout.simple_spinner_dropdown_item,
//                yearList
//            )
//            year_spiner.setAdapter<ArrayAdapter<String>>(arrayAdapterYear)
//        }
//    }
//
//    fun add() {
//        val yearSet: MutableSet<String> = HashSet()
//        val productSet: MutableSet<String> = HashSet()
//        val cursor: Cursor = myDB.readAllDataWriteOff()
//        while (cursor.moveToNext()) {
//            val year: String = cursor.getString(5)
//            val product: String = cursor.getString(1)
//            yearSet.add(year)
//            productSet.add(product)
//        }
//        cursor.close()
//        yearList = ArrayList()
//        productList = ArrayList()
//        for (yearColum: String in yearSet) {
//            yearList.add(yearColum)
//        }
//        for (productColum: String in productSet) {
//            productList.add(productColum)
//        }
//    }
//
//    fun storeDataInArrays() {
//        val cursor: Cursor = myDB.readAllDataWriteOff()
//        val sumCategory: MutableMap<Float, Float> = HashMap()
//        val animalsType: String = animals_spiner.text.toString()
//        val mountString: String = mount_spiner.text.toString()
//        val year2: String = year_spiner.text.toString()
//        setMount(mountString)
//        if (mount <= 12 && mount > 0) {
//            allProductsMount(cursor, animalsType, year2)
//        } else if (cursor.count == 0) {
//            visitors!!.add(BarEntry(0, 0))
//        } else if (mount == 13) {
//            allProductsYear(cursor, sumCategory, year2, animalsType)
//        } else {
//            visitors!!.add(BarEntry(0, 0))
//        }
//    }
//
//    fun allProductsMount(cursor: Cursor, animalsType: String, year2: String) {
//        var x: Float
//        var y: Float
//        while (cursor.moveToNext()) {
//            // проверка статуса
//            if ((animalsType == cursor.getString(1))) {
//                //проверка месяца
//                if (mount == cursor.getString(4).toInt()) {
//                    //проверка года
//                    if ((year2 == cursor.getString(5))) {
//                        //проверка статуса
//                        if (status == cursor.getString(6).toInt()) {
//                            x = cursor.getString(2).toFloat()
//                            y = cursor.getString(3).toFloat()
//                            visitors!!.add(BarEntry(y, x))
//                        }
//                    }
//                }
//            }
//        }
//        cursor.close()
//    }
//
//    fun allProductsYear(
//        cursor: Cursor,
//        sumCategory: MutableMap<Float, Float>,
//        year2: String,
//        animalsType: String
//    ) {
//        sumCategory.put(1f, 0f)
//        sumCategory.put(2f, 0f)
//        sumCategory.put(3f, 0f)
//        sumCategory.put(4f, 0f)
//        sumCategory.put(5f, 0f)
//        sumCategory.put(6f, 0f)
//        sumCategory.put(7f, 0f)
//        sumCategory.put(8f, 0f)
//        sumCategory.put(9f, 0f)
//        sumCategory.put(10f, 0f)
//        sumCategory.put(11f, 0f)
//        sumCategory.put(12f, 0f)
//        while (cursor.moveToNext()) {
//            if ((animalsType == cursor.getString(1))) {
//                if ((year2 == cursor.getString(5))) {
//                    if (status == cursor.getString(6).toInt()) {
//                        val sum: Float =
//                            sumCategory[cursor.getString(4).toFloat()]!! + cursor.getString(2)
//                                .toFloat()
//                        sumCategory[cursor.getString(4).toFloat()] = sum
//                    }
//                }
//            }
//        }
//        cursor.close()
//        for (entry: Map.Entry<Float, Float> in sumCategory.entries) {
//            val name: Float = entry.key
//            val sum: Float = entry.value
//            visitors!!.add(BarEntry(name, sum))
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