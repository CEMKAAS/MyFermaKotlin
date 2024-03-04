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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.Settings.InfoFragment
import com.hfad.myferma.R
import com.hfad.myferma.Settings.SettingsFragment
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.util.*
import kotlin.collections.HashSet

class ExpensesChartFragment : Fragment() {

    private val mountClass = ChartMount()
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var mountSpiner: AutoCompleteTextView
    private lateinit var yearSpiner: AutoCompleteTextView
    private lateinit var button: Button
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var layout: View

    private var visitors = mutableListOf<PieEntry>()

    private var mount: Int = 0
    private var mountString = "За весь год"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = inflater.inflate(R.layout.fragment_expenses_chart, container, false)

        //Подключение к базе данных
        myDB = MyFermaDatabaseHelper(requireContext())


        //убириаем фаб кнопку
        val fab: ExtendedFloatingActionButton =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.visibility = View.GONE

        val appBar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.title = "Мои покупки - График"
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        appBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
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

        val calendar = Calendar.getInstance()

        // настройка спинеров

        mountSpiner.setText(mountString, false)

        yearSpiner.setText(calendar[Calendar.YEAR].toString(), false)

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
        val animalsSpiner = bottomSheetDialog.findViewById<TextInputLayout>(R.id.menu)!!
        mountSpiner = bottomSheetDialog.findViewById<AutoCompleteTextView>(R.id.mounts_spiner)!!
        yearSpiner = bottomSheetDialog.findViewById<AutoCompleteTextView>(R.id.year_spiner)!!
        val radioGroup = bottomSheetDialog.findViewById<RadioGroup>(R.id.radioGroup)!!
        button = bottomSheetDialog.findViewById<Button>(R.id.add_button)!!

        animalsSpiner.visibility = View.GONE
        radioGroup.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        val view: View? = view
        if (view != null) {
            // настройка спинера с годами (выглядил как обычный, и год запоминал)
            val arrayAdapterAnimals = ArrayAdapter<String>(
                requireActivity().applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                add()
            )
            yearSpiner.setAdapter<ArrayAdapter<String>>(arrayAdapterAnimals)
        }
    }

    fun add(): MutableList<String> {

        val tempList: MutableSet<String> = HashSet()
        val cursor = myDB.readAllDataExpenses()
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                tempList.add(cursor.getString(5))
            }
        }else {
            val calendar = Calendar.getInstance()
            tempList.add(calendar[Calendar.YEAR].toString())
        }
        cursor.close()

        return tempList.toMutableList()
    }

    private fun storeDataInArrays() {

        visitors.clear()
        dataInArrays()

        val pieChart: PieChart = layout.findViewById(R.id.barChart)
        val pieDataSet: PieDataSet = PieDataSet(visitors, "Расходы")
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toMutableList()
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 16f
        val pieData: PieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(11f)
        pieData.setValueTextColor(Color.WHITE)
        pieChart.description.isEnabled = false
        pieChart.centerText = "Расходы" + "\n" + mountSpiner.text
            .toString() + "\n" + yearSpiner.text.toString()
        pieChart.animateX(2000)
        pieChart.data = pieData
        pieChart.invalidate()
        val l: Legend = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
    }


    private fun dataInArrays() {

        mountString = mountSpiner.text.toString()
        val year2: String = yearSpiner.text.toString()

        mount = mountClass.setMountInt(mountString)

        when (mount) {
            in 1..12 -> addVisitors(myDB.selectChartMountExpen(mount.toString(), year2))
            13 -> addVisitors(myDB.selectChartYearExpen(year2))
            else -> visitors.add(PieEntry(0f, "Нет товаров"))
        }
    }

    private fun addVisitors(cursor: Cursor) {
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                visitors.add(PieEntry(cursor.getString(1).toFloat(), cursor.getString(0)))
            }
        } else visitors.add(PieEntry(0f, "Нет товаров"))
        cursor.close()
    }

    private fun moveToNextFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }
}