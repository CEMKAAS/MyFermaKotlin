//package com.hfad.myferma.Finance.ExpensesPackage
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
//
//class ExpensesChartFragment : Fragment() {
//    private lateinit var myDB: MyFermaDatabaseHelper
//    private lateinit var  animals_spiner: AutoCompleteTextView
//    private lateinit var mount_spiner: AutoCompleteTextView
//    private lateinit var year_spiner: AutoCompleteTextView
//    private var visitors = mutableListOf<PieEntry>()
//    private val arrayListAnaimals = mutableListOf<String>()
////    private var arrayAdapterAnimals: ArrayAdapter<String>? = null
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
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val layout: View = inflater.inflate(R.layout.fragment_expenses_chart, container, false)
//
//
//        // установка спинеров
//        mount_spiner = layout.findViewById<AutoCompleteTextView>(R.id.mount_spiner)
//        year_spiner = layout.findViewById<AutoCompleteTextView>(R.id.year_spiner)
//
//        //установка графиков
//        val pieChart: PieChart = layout.findViewById(R.id.barChart)
//
//        //Подключение к базе данных
//        myDB = MyFermaDatabaseHelper(requireContext())
//
//
////Создание списка с данными для графиков
//        visitors = ArrayList<PieEntry>()
//
//        // настройка спинеров
//        mount_spiner.setText("За весь год", false)
//        year_spiner.setText("2023", false)
//
//        //убириаем фаб кнопку
//        val fab: ExtendedFloatingActionButton =
//            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
//        fab.visibility = View.GONE
//        val appBar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
//        appBar.title = "Мои покупки - График"
//        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
//        appBar.setNavigationOnClickListener {
//            requireActivity().supportFragmentManager.popBackStack()
//        }
//
//        //Логика просчета
//        storeDataInArrays()
//
//        // настройка графиков
//        val pieDataSet: PieDataSet = PieDataSet(visitors, "Расходы")
//        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
//        pieDataSet.setValueTextColor(Color.BLACK)
//        pieDataSet.setValueTextSize(16f)
//        val pieData: PieData = PieData(pieDataSet)
//        pieData.setValueFormatter(PercentFormatter())
//        pieData.setValueTextSize(11f)
//        pieData.setValueTextColor(Color.WHITE)
//        pieChart.getDescription().setEnabled(false)
//        pieChart.setCenterText(
//            "Расходы" + "\n" + mount_spiner.text.toString() + "\n" + year_spiner.text
//                .toString()
//        )
//        pieChart.animateX(2000)
//        pieChart.setData(pieData)
//        pieChart.invalidate()
//        val l: Legend = pieChart.getLegend()
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
//        l.setOrientation(Legend.LegendOrientation.VERTICAL)
//        l.setDrawInside(false)
//        l.setXEntrySpace(7f)
//        l.setYEntrySpace(0f)
//        l.setYOffset(0f)
//
//        mount_spiner.onItemClickListener =
//            AdapterView.OnItemClickListener { parent, view, position, id ->
//                visitors!!.clear()
//                storeDataInArrays()
//                val pieChart: PieChart = layout.findViewById(R.id.barChart)
//                val pieDataSet: PieDataSet = PieDataSet(visitors, "Расходы")
//                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
//                pieDataSet.setValueTextColor(Color.BLACK)
//                pieDataSet.setValueTextSize(16f)
//                val pieData: PieData = PieData(pieDataSet)
//                pieData.setValueFormatter(PercentFormatter())
//                pieData.setValueTextSize(11f)
//                pieData.setValueTextColor(Color.WHITE)
//                pieChart.getDescription().setEnabled(false)
//                pieChart.setCenterText(
//                    "Расходы" + "\n" + mount_spiner.text
//                        .toString() + "\n" + year_spiner.text.toString()
//                )
//                pieChart.animateX(2000)
//                pieChart.setData(pieData)
//                pieChart.invalidate()
//                val l: Legend = pieChart.getLegend()
//                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
//                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
//                l.setOrientation(Legend.LegendOrientation.VERTICAL)
//                l.setDrawInside(false)
//                l.setXEntrySpace(7f)
//                l.setYEntrySpace(0f)
//                l.setYOffset(0f)
//            }
//        year_spiner.setOnItemClickListener { parent, view, position, id ->
//            visitors!!.clear()
//            storeDataInArrays()
//            val pieChart: PieChart = layout.findViewById(R.id.barChart)
//            val pieDataSet: PieDataSet = PieDataSet(visitors, "Расходы")
//            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
//            pieDataSet.setValueTextColor(Color.BLACK)
//            pieDataSet.setValueTextSize(16f)
//            val pieData: PieData = PieData(pieDataSet)
//            pieData.setValueFormatter(PercentFormatter())
//            pieData.setValueTextSize(11f)
//            pieData.setValueTextColor(Color.WHITE)
//            pieChart.getDescription().setEnabled(false)
//            pieChart.setCenterText(
//                "Расходы" + "\n" + mount_spiner.text
//                    .toString() + "\n" + year_spiner.text.toString()
//            )
//            pieChart.animateX(2000)
//            pieChart.setData(pieData)
//            pieChart.invalidate()
//            val l: Legend = pieChart.getLegend()
//            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
//            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
//            l.setOrientation(Legend.LegendOrientation.VERTICAL)
//            l.setDrawInside(false)
//            l.setXEntrySpace(7f)
//            l.setYEntrySpace(0f)
//            l.setYOffset(0f)
//        }
//        return layout
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val view: View? = view
//        if (view != null) {
//            // настройка спинера с годами (выглядил как обычный, и год запоминал)
//           val arrayAdapterAnimals = ArrayAdapter<String>(
//                requireActivity().applicationContext,
//                R.layout.simple_spinner_dropdown_item,
//                add()
//            )
//            year_spiner.setAdapter<ArrayAdapter<String>>(arrayAdapterAnimals)
//        }
//    }
//
//    fun add(): ArrayList<String> {
//        val tempList: MutableSet<String> = HashSet()
//        val cursor = myDB.readAllDataExpenses()
//        while (cursor.moveToNext()) {
//            val string1: String = cursor.getString(5)
//            tempList.add(string1)
//        }
//        cursor.close()
//        val tempList1: ArrayList<String> = ArrayList()
//        for (nameExpenses: String in tempList) {
//            tempList1.add(nameExpenses)
//        }
//        return tempList1
//    }
//
//    fun storeDataInArrays() {
//        val cursor: Cursor = myDB.readAllDataExpenses()
//        val sumCategory: MutableMap<String, Float?> = HashMap()
//        val mountString: String = mount_spiner.text.toString()
//        val year2: String = year_spiner.text.toString()
//        setMount(mountString)
//        cursor.moveToNext()
//        if (mount <= 12 && mount > 0) {
//
//            //проверка месяца
//            if (mount == cursor.getString(4).toInt()) {
//                //проверка года
//                if ((year2 == cursor.getString(5))) {
//                    sumCategory[cursor.getString(1)] = cursor.getString(2).toFloat()
//                }
//            }
//            while (cursor.moveToNext()) {
//
//                //проверка месяца
//                if (mount == cursor.getString(4).toInt()) {
//                    //проверка года
//                    if ((year2 == cursor.getString(5))) {
//                        if (sumCategory[cursor.getString(1)] == null) {
//                            sumCategory[cursor.getString(1)] = cursor.getString(2).toFloat()
//                        } else {
//                            val sum: Float =
//                                sumCategory[cursor.getString(1)]!! + cursor.getString(2)
//                                    .toFloat()
//                            sumCategory[cursor.getString(1)] = sum
//                        }
//                    }
//                }
//            }
//            cursor.close()
//
//            //Проверка сколько строк в таблице.
//        } else if (cursor.count == 0) {
//            visitors!!.add(PieEntry(0, "Нет товара"))
//        } else if (mount == 13) {
//            //проверка года
//            if ((year2 == cursor.getString(5))) {
//                sumCategory[cursor.getString(1)] = cursor.getString(2).toFloat()
//            }
//            while (cursor.moveToNext()) {
//                //проверка года
//                if ((year2 == cursor.getString(5))) {
//                    if (sumCategory[cursor.getString(1)] == null) {
//                        sumCategory[cursor.getString(1)] = cursor.getString(2).toFloat()
//                    } else {
//                        val sum: Float =
//                            sumCategory[cursor.getString(1)]!! + cursor.getString(2).toFloat()
//                        sumCategory[cursor.getString(1)] = sum
//                    }
//                }
//            }
//            cursor.close()
//        } else {
//            visitors!!.add(PieEntry(0, "Нет товара"))
//        }
//        for (entry: Map.Entry<String, Float?> in sumCategory.entries) {
//            val name: String = entry.key
//            val sum: Float? = entry.value
//            visitors!!.add(PieEntry(sum, name))
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