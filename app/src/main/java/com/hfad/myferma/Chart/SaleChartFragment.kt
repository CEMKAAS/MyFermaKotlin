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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.hfad.myferma.Settings.InfoFragment
import com.hfad.myferma.R
import com.hfad.myferma.Settings.SettingsFragment
import com.hfad.myferma.db.MyConstanta
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.util.Calendar

class SaleChartFragment : Fragment() {
    private val mountClass = ChartMount()
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var animalsSpiner: AutoCompleteTextView
    private lateinit var mountSpiner: AutoCompleteTextView
    private lateinit var yearSpiner: AutoCompleteTextView
    private lateinit var button: Button
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var layout: View

    private var visitors = mutableListOf<BarEntry>()
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

    private var mount: Int = 0
    private var mountString = "За весь год"

    private var yearList = mutableListOf<String>()
    private var productList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = inflater.inflate(R.layout.fragment_sale_chart, container, false)

        //Подключение к базе данных
        myDB = MyFermaDatabaseHelper(requireContext())

        add()

        //убириаем фаб кнопку
        val fab: ExtendedFloatingActionButton =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.visibility = View.GONE

        val appBar: MaterialToolbar =
            requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.title = "Мои продажи - График"
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        appBar.setNavigationOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
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

        val calendar: Calendar = Calendar.getInstance()
        // настройка спинеров
        mountSpiner.setText(mountString, false)
        animalsSpiner.setText(productList[0], false)
        yearSpiner.setText(calendar.get(Calendar.YEAR).toString(), false)

        //Логика просчета
        storeDataInArrays()

        button.setOnClickListener {
            storeDataInArrays()
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

    private fun bar(xAsis: MutableList<String>) {
        val barChart: BarChart = layout.findViewById(R.id.barChart)
        val barDataSet: BarDataSet = BarDataSet(visitors, animalsSpiner.text.toString())
        barDataSet.colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 16f
        val barData: BarData = BarData(barDataSet)
        barChart.invalidate()
        barChart.setFitBars(true)
        barChart.data = barData
        barChart.description.text = "График проданной продукции со склада"
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

    fun add() {
        val yearSet: MutableSet<String> = HashSet()
        val productSet: MutableSet<String> = HashSet()

        val cursor: Cursor = myDB.readAllDataSale()
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                val year = cursor.getString(5)
                val product = cursor.getString(1)
                yearSet.add(year)
                productSet.add(product)
            }
        }else {
            val calendar = Calendar.getInstance()
            yearSet.add(calendar[Calendar.YEAR].toString())
            productSet.add("Нет товаров")
        }
        cursor.close()

        yearList = yearSet.toMutableList()
        productList = productSet.toMutableList()

    }


    private fun storeDataInArrays() {

        val animalsType: String = animalsSpiner.text.toString()
        mountString = mountSpiner.text.toString()
        val year2: String = yearSpiner.text.toString()

        mount = mountClass.setMountInt(mountString)

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
                bar(mountClass.setMount(mountString))

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
                if (cursor.count !=0) {
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
                }
                cursor.close()
                bar(labes)
            }

            else -> {
                for (i in 0..(labes.size - 2)) {
                    visitors.add(BarEntry(0f, 0f))
                    bar(labes)
                }
            }
        }
    }

    private fun moveToNextFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }
}