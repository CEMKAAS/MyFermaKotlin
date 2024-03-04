package com.hfad.myferma.Chart

import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.widget.Toolbar
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.hfad.myferma.Settings.InfoFragment
import com.hfad.myferma.R
import com.hfad.myferma.Settings.SettingsFragment
import com.hfad.myferma.db.MyConstanta
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.util.Calendar

class FinanceChartFragment : Fragment() {
    private val mountClass = ChartMount()
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var animalsSpiner: AutoCompleteTextView
    private lateinit var mountSpiner: AutoCompleteTextView
    private lateinit var yearSpiner: AutoCompleteTextView
    private lateinit var button: Button
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private var visitors = mutableListOf<Entry>()
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
    private lateinit var layout: View

    private var mount: Int = 0
    private var mountString = "За весь год"
    private var idColor: Int = 0

    private var yearList = mutableListOf<String>()
    private var productList = mutableListOf<String>()
    private var productListAll = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = inflater.inflate(R.layout.fragment_finance_chart, container, false)
        //Подключение к базе данных и к календарю
        myDB = MyFermaDatabaseHelper(requireContext())

        add()

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
        appBar.menu.findItem(R.id.price).isVisible = false
        appBar.menu.findItem(R.id.magazine).isVisible = false
        appBar.menu.findItem(R.id.filler).isVisible = true
        appBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.filler -> bottomSheetDialog.show()
                R.id.more -> {
                    moveToNextFragment(InfoFragment())
                    appBar.title = "Информация"
                }

                R.id.setting -> {
                    moveToNextFragment(SettingsFragment())
                    appBar.title = "Мои настройки"
                }
            }
            true
        })

        showBottomSheetDialog()

        // настройка спинеров
        animalsSpiner.setText("Все", false)
        mountSpiner.setText(mountString, false)

        val calendar = Calendar.getInstance()
        yearSpiner.setText(calendar.get(Calendar.YEAR).toString(), false)

        // Формируем график
        spiner()

        button.setOnClickListener {
            spiner()
            bottomSheetDialog.dismiss()
        }
        return layout
    }

    //Добавляем bottobSheet
    private fun showBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.fragment_bottom_chart_setting)
        animalsSpiner = bottomSheetDialog.findViewById<AutoCompleteTextView>(R.id.animals_spiner)!!
        mountSpiner = bottomSheetDialog.findViewById<AutoCompleteTextView>(R.id.mounts_spiner)!!
        yearSpiner = bottomSheetDialog.findViewById<AutoCompleteTextView>(R.id.year_spiner)!!
        val radioGroup = bottomSheetDialog.findViewById<RadioGroup>(R.id.radioGroup)!!
        button = bottomSheetDialog.findViewById<Button>(R.id.add_button)!!

        radioGroup.visibility = View.GONE
    }

    private fun spiner() {
        visitors.clear()

        val animalsType: String = animalsSpiner.text.toString()
        mountString = mountSpiner.text.toString()
        val year2: String = yearSpiner.text.toString()

        mount = mountClass.setMountInt(mountString)

        val lineChart: LineChart = layout.findViewById(R.id.lineChart)
        lineChart.description.text = "График продукции"

        if (animalsType == "Все") {

            val dataSets: ArrayList<ILineDataSet> = arrayListOf()

            when (mount) {
                in 1..12 -> allProducts(dataSets, year2, true)
                13 -> allProducts(dataSets, year2, false)
            }

            val data: LineData = LineData(dataSets)
            lineChart.invalidate()
            lineChart.data = data
        } else {
            //Если не все
            storeDataInArrays(animalsType, year2)

            val dataset: LineDataSet = LineDataSet(visitors, animalsType)
            val data: LineData = LineData(dataset)
            lineChart.invalidate()
            lineChart.data = data
        }

        lineChart.animateY(500)

        if (mount != 13) xaxis(lineChart, mountClass.setMount(mountString))
        else xaxis(lineChart, labes)
    }

    // Добавление значений в мапу
    private fun allProducts(
        dataSets: ArrayList<ILineDataSet>,
        year2: String,
        mountBoolean: Boolean
    ) {

        for (product in productList) {
            val sd: ArrayList<Entry> = ArrayList<Entry>()

            val cursor =
                if (mountBoolean) myDB.selectChartMountFinance1(product, mount.toString(), year2)
                else myDB.selectChartYearFinance1(product, year2)

            if (cursor.count != 0) {
                //проверка за весь год //TODO Сократи это говно плиз и еще ниже будет его тоже, будущий Семён я сделал все что мог (2023 март год) // TODO я сделал все что мог дальше ты сам брат (2024 февраль)
                while (cursor.moveToNext()) {
                    sd.add(Entry(cursor.getString(1).toFloat(), cursor.getString(0).toFloat()))
                }

            } else sd.add(Entry(0f, 0f))
            cursor.close()

            greatChar(product, dataSets, sd)

        }
    }

    // Добавление графиков
    private fun greatChar(
        product: String,
        dataSets: ArrayList<ILineDataSet>,
        sd: ArrayList<Entry>
    ) {
        val datasetFirst: LineDataSet = LineDataSet(sd, product)
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


    //Товары если не все
    private fun storeDataInArrays(animalsType: String, year2: String) {

        when (mount) {
            in 1..12 -> {
                val cursor: Cursor = myDB.selectChartMount(
                    MyConstanta.DISCROTIONSale,
                    MyConstanta.TABLE_NAMESALE,
                    MyConstanta.TITLESale,
                    animalsType,
                    mount.toString(),
                    year2
                )
                if (cursor.count != 0) {
                    while (cursor.moveToNext()) {
                        val x = cursor.getString(0).toFloat()
                        val y = cursor.getString(1).toFloat()
                        visitors.add(BarEntry(y, x))

                    }
                } else visitors.add(BarEntry(0f, 0f))
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

                if (cursor.count != 0) {
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
                }
                cursor.close()
            }

            else -> visitors.add(BarEntry(0f, 0f))
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
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                val year: String = cursor.getString(5)
                val product: String = cursor.getString(1)
                yearSet.add(year)
                productSet.add(product)
            }
        } else {
            val calendar = Calendar.getInstance()
            yearSet.add(calendar[Calendar.YEAR].toString())
            productSet.add("Нет товаров")
        }
        cursor.close()

        yearList = yearSet.toMutableList()
        productList = productSet.toMutableList()

        productListAll = productSet.toMutableList()
        productListAll.add("Все")
    }

    private fun moveToNextFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }
}